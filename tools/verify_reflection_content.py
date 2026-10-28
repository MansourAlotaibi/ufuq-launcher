#!/usr/bin/env python3
"""Offline integrity and attribution verification against downloaded original source snapshots."""
from pathlib import Path
import json,hashlib,html,re
r=Path(__file__).resolve().parents[1];s=r/'third_party/content-source'
a=json.loads((r/'launcher/app/src/main/assets/daily_reflection.json').read_text())['entries']
p=json.loads((s/'provenance.json').read_text())
raw=(s/'tanzil-download.txt').read_bytes()
assert hashlib.sha256(raw).hexdigest()==p['tanzil']['downloadSha256']
q=raw.decode(); verses={line.split('|',2)[2] for line in q.splitlines() if re.match(r'^\d+\|\d+\|',line)}
assert len(a)==7
for e in a:
 if e['kind']=='quran':
  assert e['text'] in verses
  assert e['notice']==q[q.index('# PLEASE DO NOT REMOVE'):]
  assert e['licenseUrl']=='https://creativecommons.org/licenses/by/3.0/'
 else:
  evidence=next(x for x in p['hadith'] if x['sourceUrl']==e['sourceUrl'])
  raw=(s/f"wikisource-{evidence['key']}.json").read_bytes()
  assert hashlib.sha256(raw).hexdigest()==evidence['parseJsonSha256']
  source=html.unescape(re.sub('<[^>]*>','',json.loads(raw)['text']))
  assert e['text'] in source
  assert hashlib.sha256(e['text'].encode()).hexdigest()==evidence['textSha256']
  assert e['licenseUrl']=='https://creativecommons.org/licenses/by-sa/4.0/'
 assert e['attribution'] and e['sourceUrl'] and e['notice']
print('PASS: four complete Tanzil verses and three revision-pinned Wikisource Hadith excerpts; source hashes, verbatim text and notices match.')
