#!/usr/bin/env python3
"""Fetch the pinned upstream source archive for distribution alongside release binaries."""
from pathlib import Path
import urllib.request,json,hashlib
r=Path(__file__).resolve().parents[1]
p=json.loads((r/'third_party/desugar-source-provenance.json').read_text())
request=urllib.request.Request(p['sourceUrl'],headers={'User-Agent':'Ufuq-source-compliance'})
data=urllib.request.urlopen(request,timeout=90).read()
assert hashlib.sha256(data).hexdigest()==p['sourceArchiveSha256'],'Upstream archive hash changed; review required'
out=r/'release-local/dependency-sources';out.mkdir(parents=True,exist_ok=True)
(out/p['sourceArchiveFilename']).write_bytes(data)
print('Pinned desugaring source archive verified and prepared locally; nothing published.')
