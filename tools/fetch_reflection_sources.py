#!/usr/bin/env python3
"""Fetch official source evidence; no app runtime network dependency."""
import urllib.request,urllib.parse,json
from pathlib import Path
root=Path(__file__).resolve().parents[1]
for key,revision in [('intentions',484874),('quran',512181),('manners',627517)]:
 url='https://ar.wikisource.org/w/api.php?'+urllib.parse.urlencode({'action':'parse','oldid':revision,'prop':'text|revid','format':'json','formatversion':2})
 req=urllib.request.Request(url,headers={'User-Agent':'UfuqLauncher-provenance/1.0 (local source verification)'})
 data=json.load(urllib.request.urlopen(req,timeout=30))
 if 'error' in data:raise ValueError(data['error'])
 p=data['parse'];(root/'third_party/content-source'/f'wikisource-{key}.json').write_text(json.dumps(p,ensure_ascii=False,indent=2))
 print(key,p['revid'],len(p['text']))
