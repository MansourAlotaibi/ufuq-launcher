#!/usr/bin/env python3
"""Local source/history/artifact policy checks; no network, credentials or device access."""
import argparse, re, subprocess, zipfile
from pathlib import Path
ROOT = Path(__file__).resolve().parents[1]
def git(*args):
    return subprocess.check_output(['git', *args], cwd=ROOT)
PATTERNS = {
 'private key': rb'-----BEGIN (?:RSA |EC |DSA |OPENSSH )?PRIVATE KEY-----',
 'GitHub token': rb'(?:gh[pousr]_[A-Za-z0-9]{20,}|github_pat_[A-Za-z0-9_]{30,})',
 'AWS access key': rb'AKIA[A-Z0-9]{16}',
 'OpenAI key': rb'sk-proj-[A-Za-z0-9_-]{25,}',
 'absolute user path': rb'(?:/' + rb'Users/[^/\s]+/|/' + rb'home/[^/\s]+/|C:' + rb'\\Users\\[^\\\s]+\\)',
}
SENSITIVE = re.compile(r'(?i)(\.(apk|aab|jks|keystore|p12|pfx|pem|key|hprof|dump|zip|tar|gz)$|(^|/)(\.env(?:\..*)?|adbkey.*|id_rsa.*|id_ed25519.*|local.properties|key.properties)$)')
PRIVATE_PATH = re.compile(r'(?i)(^|/)(private|release-local|captures|device-dumps|screenshots-private)(/|$)')
def prohibited(name): return bool(SENSITIVE.search(name) or PRIVATE_PATH.search(name))
parser=argparse.ArgumentParser();parser.add_argument('--apk',type=Path);parser.add_argument('--ref',help='Audit only this reachable history; defaults to all local refs')
args=parser.parse_args()
errors=[]
def scan(label, data):
 for name, pattern in PATTERNS.items():
  if re.search(pattern,data): errors.append(f'{label}: {name} pattern (value redacted)')
seen=set(); commits=git('rev-list',args.ref if args.ref else '--all').decode().splitlines()
for rev in commits:
 for line in git('ls-tree','-r',rev).decode().splitlines():
  meta,name=line.split('\t',1); sha=meta.split()[2]
  if prohibited(name):errors.append(f'history {rev[:7]}: prohibited filename {name}')
  if sha not in seen:
   seen.add(sha);scan(f'history {rev[:7]} {name}',git('cat-file','blob',sha))
# Include new unstaged source while preparing the candidate.
paths=set(git('ls-files','--cached','--others','--exclude-standard','-z').decode().split('\0'))
for name in sorted(paths):
 if not name:continue
 p=ROOT/name
 if not p.is_file():continue
 if prohibited(name):errors.append(f'worktree: prohibited filename {name}')
 scan(name,p.read_bytes())
for name in ['launcher/build-and-install.sh','launcher/build-and-install.ps1']:
 if re.search(r'pm (?:disable|clear|install)|set-home-activity|settings put|tv\.py|b64push', (ROOT/name).read_text()):
  errors.append(name+': unsafe installer behavior')
manifest=(ROOT/'launcher/app/src/main/AndroidManifest.xml').read_text()
if 'com.mansour.tvlauncher' in manifest:errors.append('manifest: protected identifier')
import xml.etree.ElementTree as ET
ns='{http://schemas.android.com/apk/res/android}'
for perm in ET.fromstring(manifest).findall('uses-permission'):
 name=perm.get(ns+'name','').split('.')[-1]
 if name == 'WRITE_SECURE_SETTINGS':errors.append('manifest: secure-settings grant prohibited')
 if name in ['ACCESS_FINE_LOCATION','ACCESS_COARSE_LOCATION','CHANGE_WIFI_STATE'] and perm.get(ns+'maxSdkVersion') != '28':
  errors.append('manifest: legacy Wi-Fi permissions must stop at API 28')
 if name in ['READ_EXTERNAL_STORAGE','WRITE_EXTERNAL_STORAGE'] and perm.get(ns+'maxSdkVersion') != '19':
  errors.append('manifest: legacy storage must stop at API 19')
# Check relative Markdown document links, ignoring URLs and heading-only links.
for name in sorted(paths):
 p=ROOT/name
 if not name.endswith('.md') or not p.is_file():continue
 for target in re.findall(r'\]\(([^)]+)\)',p.read_text()):
  target=target.split('#',1)[0]
  if target and '://' not in target and not (p.parent/target).exists():errors.append(f'{p.name}: broken link {target}')
if args.apk:
 with zipfile.ZipFile(args.apk) as z:
  for name in z.namelist():
   if prohibited(name):errors.append('APK: sensitive filename '+name)
   scan('APK '+name,z.read(name))
print(f'Checked {len(commits)} reachable commits, {len(seen)} unique history blobs, {len(paths)-1} worktree paths.')
print('Checks: token/key/path patterns, prohibited filenames, installer policy, identity, permission grants, document links.')
print('Limitations: not a malware scan, entropy scan or legal clearance; examples and undisclosed data need human review.')
if errors:
 print('\n'.join(errors));raise SystemExit(1)
print('PASS: no configured security/policy pattern found.')
