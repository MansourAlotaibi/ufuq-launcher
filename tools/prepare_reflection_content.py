#!/usr/bin/env python3
"""Create the seven-entry offline replacement from pinned, attributed source evidence."""
import json,re,html,hashlib
from pathlib import Path
r=Path(__file__).resolve().parents[1];d=r/'third_party/content-source';assets=r/'launcher/app/src/main/assets';assets.mkdir(exist_ok=True)
q=(d/'tanzil-download.txt').read_bytes();qt=q.decode();notice=qt[qt.index('# PLEASE DO NOT REMOVE'):]
entries=[];selected=[]
for sura,aya,label in [(94,6,'الشرح · الآية ٦'),(13,28,'الرعد · الآية ٢٨'),(2,152,'البقرة · الآية ١٥٢'),(2,286,'البقرة · الآية ٢٨٦')]:
 line=next(x for x in qt.splitlines() if x.startswith(f'{sura}|{aya}|'));selected.append(line)
 entries.append(dict(kind='quran',text=line.split('|',2)[2],source=label,sourceUrl=f'https://tanzil.net/#{sura}:{aya}',licenseUrl='https://creativecommons.org/licenses/by/3.0/',attribution='Tanzil Project — Quran Text, Uthmani 1.1',notice=notice))
(d/'tanzil-selected.txt').write_text('\n'.join(selected)+'\n\n'+notice)
evidence={'tanzil':{'downloadUrl':'https://tanzil.net/pub/download/index.php?quranType=uthmani&outType=txt-2&agree=true','downloadSha256':hashlib.sha256(q).hexdigest(),'version':'Uthmani 1.1','changes':'Four complete verse records selected; verse text unchanged.'},'hadith':[]}
choices=[('intentions','إنما الأعمال بالنيات','فهجرته إلى ما هاجر إليه.','صحيح البخاري · ١'),('quran','(خيركم من تعلم القرآن وعلمه)','(خيركم من تعلم القرآن وعلمه)','صحيح البخاري · ٥٠٢٧ (ويكي مصدر: ٤٧٣٩)'),('manners','(من كان يؤمن بالله واليوم الآخر فلا يؤذ جاره،','أو ليصمت).','صحيح البخاري · ٦٠١٨ (ويكي مصدر: ٥٦٧٢)')]
for key,start,end,label in choices:
 p=d/f'wikisource-{key}.json';raw=p.read_bytes();data=json.loads(raw);plain=html.unescape(re.sub('<[^>]*>','',data['text']))
 a=plain.index(start);b=plain.index(end,a)+len(end) if start!=end else a+len(start);text=plain[a:b]
 url=f"https://ar.wikisource.org/w/index.php?oldid={data['revid']}"
 entry=dict(kind='hadith',text=text,source=label,sourceUrl=url,licenseUrl='https://creativecommons.org/licenses/by-sa/4.0/',attribution='Sahih al-Bukhari — Arabic Wikisource contributors; contributor history available from the source page.',notice='Arabic matn excerpt selected from the attributed chapter. Narrator chains and surrounding commentary omitted. Wording in the excerpt is unchanged. This transcription selection is distributed under CC BY-SA 4.0; the independent application code remains MIT. No endorsement is implied.')
 entries.append(entry);evidence['hadith'].append(dict(key=key,title=data['title'],revision=data['revid'],sourceUrl=url,parseJsonSha256=hashlib.sha256(raw).hexdigest(),textSha256=hashlib.sha256(text.encode()).hexdigest(),text=text,changes=entry['notice']))
# Preserve the original relative order (four Quran entries and three Hadith entries) without truncating verses.
entries=[entries[i] for i in [0,1,2,4,5,3,6]]
(assets/'daily_reflection.json').write_text(json.dumps({'formatVersion':1,'entries':entries},ensure_ascii=False,indent=2)+'\n')
(d/'provenance.json').write_text(json.dumps(evidence,ensure_ascii=False,indent=2)+'\n')
print('Prepared',len(entries),'entries; Quran:',sum(x['kind']=='quran' for x in entries),'Hadith:',sum(x['kind']=='hadith' for x in entries))
