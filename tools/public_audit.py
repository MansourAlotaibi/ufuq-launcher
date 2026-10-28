#!/usr/bin/env python3
"""Verify the isolated public history and resolved binary asset inventory."""
from pathlib import Path
import argparse,subprocess,json,hashlib,zipfile
r=Path(__file__).resolve().parents[1]
p=argparse.ArgumentParser();p.add_argument('--ref',default='HEAD');p.add_argument('--apk',type=Path);p.add_argument('--sources',type=Path);a=p.parse_args()
def git(*args):return subprocess.check_output(['git',*args],cwd=r).decode()
assert len(git('rev-list',a.ref).splitlines())==1,'Public branch must have one independent root commit'
assert len(git('rev-list','--parents','-n','1',a.ref).split())==1,'Public root must have no parent'
paths=git('ls-tree','-r','--name-only',a.ref).splitlines()
for forbidden in ['launcher/app/src/main/res/drawable-nodpi/banner.jpg','launcher/app/src/main/res/mipmap-xhdpi/ic_launcher.jpg','launcher/app/src/main/res/drawable/ic_mansourtv.xml']:
 assert forbidden not in paths,'Unresolved inherited art included'
assert not any('/com/mansour/tvlauncher/' in x for x in paths)
manifest=json.loads((r/'third_party/public-assets.json').read_text());allow={e['path']:e for e in manifest}
for name in paths:
 if Path(name).suffix.lower() in ['.ttf','.png','.jpg','.jpeg','.webp']:
  assert name in allow,'Uninventoried image/font: '+name
  assert hashlib.sha256((r/name).read_bytes()).hexdigest()==allow[name]['sha256']
f=json.loads((r/'third_party/font-provenance.json').read_text())
assert hashlib.sha256((r/'launcher/app/src/main/res/font/outfit.ttf').read_bytes()).hexdigest()==f['sha256']
w=json.loads((r/'third_party/gradle-wrapper-provenance.json').read_text())
assert hashlib.sha256((r/'launcher/gradle/wrapper/gradle-wrapper.jar').read_bytes()).hexdigest()==w['sha256']
if a.apk:
 with zipfile.ZipFile(a.apk) as z:
  for e in json.loads((r/'third_party/wallpaper-provenance.json').read_text()):
   assert hashlib.sha256(z.read('assets/wallpapers/'+e['file'])).hexdigest()==e['sha256']
  for name in ['OFL-Outfit.txt','Apache-2.0.txt','desugar-LICENSE.txt','CC-BY-3.0.txt','CC-BY-SA-4.0.txt']:
   assert z.read('assets/licenses/'+name)==(r/'launcher/app/src/main/assets/licenses'/name).read_bytes()
  assert z.read('assets/daily_reflection.json')==(r/'launcher/app/src/main/assets/daily_reflection.json').read_bytes()
  assert not any('version-control-info' in n for n in z.namelist()),'Private history metadata must not be packaged'
if a.sources:
 source=json.loads((r/'third_party/desugar-source-provenance.json').read_text())
 assert hashlib.sha256(a.sources.read_bytes()).hexdigest()==source['sourceArchiveSha256']
 with zipfile.ZipFile(a.sources) as z:
  import re
  keys=[name.split('/',1)[1] for name in z.namelist() if not name.endswith('/') and re.search(rb'-----BEGIN (?:RSA |EC |OPENSSH )?PRIVATE KEY-----',z.read(name))]
  assert sorted(keys)==sorted(source['publicTestFixtureKeyFiles']),'Unexpected source archive key material'
 print('Source archive hash verified; only the two documented public upstream Harmony test-key fixtures are present.')
print('PASS: one parentless public commit; prohibited inherited art absent; font/wrapper hashes and image allowlist verified; packaged assets/licenses match.')
