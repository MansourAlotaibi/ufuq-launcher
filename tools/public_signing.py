#!/usr/bin/env python3
"""Local-only public signing. Never reads a personal keystore or prints a password.
Provisioning files stay in ignored private/public-signing with owner-only permissions.
Move recovery material to separate encrypted/offline storage before public publication.
"""
from pathlib import Path
import argparse,os,secrets,subprocess,hashlib,json,zipfile
r=Path(__file__).resolve().parents[1];p=r/'private/public-signing';key=p/'ufuq-public.p12';password=p/'store-password.txt'
parser=argparse.ArgumentParser();parser.add_argument('action',choices=['init','sign']);parser.add_argument('--apk',type=Path);args=parser.parse_args()
def run(cmd,**kw):
 result=subprocess.run(cmd,capture_output=True,text=True,**kw)
 if result.returncode:
  message=result.stderr or result.stdout or 'Signing tool failed'
  if password.exists():message=message.replace(password.read_text().strip(),'[REDACTED]')
  raise SystemExit(message)
 return result
jdk=Path(os.environ['JAVA_HOME']);sdk=Path(os.environ['ANDROID_HOME']);env=os.environ.copy()
if args.action=='init':
 if key.exists() or password.exists():raise SystemExit('Refusing to replace an existing public signing identity.')
 p.mkdir(parents=True,mode=0o700,exist_ok=True);os.chmod(p,0o700);os.chmod(p.parent,0o700)
 secret=secrets.token_urlsafe(48)
 fd=os.open(password,os.O_WRONLY|os.O_CREAT|os.O_EXCL,0o600)
 with os.fdopen(fd,'w') as f:f.write(secret+'\n')
 env['UFUQ_NEW_KEY_PASSWORD']=secret
 try:
  run([str(jdk/'bin/keytool'),'-genkeypair','-alias','ufuq-public','-keystore',str(key),'-storetype','PKCS12','-keyalg','RSA','-keysize','3072','-sigalg','SHA256withRSA','-validity','10000','-dname','CN=Ufuq Public Release,O=Ufuq Launcher','-storepass:env','UFUQ_NEW_KEY_PASSWORD','-keypass:env','UFUQ_NEW_KEY_PASSWORD'],env=env)
  os.chmod(key,0o600)
  cert=p/'ufuq-public.der'
  run([str(jdk/'bin/keytool'),'-exportcert','-alias','ufuq-public','-keystore',str(key),'-storepass:env','UFUQ_NEW_KEY_PASSWORD','-file',str(cert)],env=env)
  fingerprint=hashlib.sha256(cert.read_bytes()).hexdigest()
  (r/'docs/PUBLIC-SIGNING-IDENTITY.json').write_text(json.dumps(dict(alias='ufuq-public',algorithm='RSA-3072 / SHA256withRSA',certificateSha256=fingerprint,provisioned='2026-10-29',origin='New key generated for independent public Ufuq only. No personal signing identity accessed.',secretStorage='Ignored owner-only local provisioning directory; separate encrypted/offline recovery backup remains an owner action.'),indent=2)+'\n')
  print('New independent public identity created. Certificate SHA-256:',fingerprint)
 finally:env.pop('UFUQ_NEW_KEY_PASSWORD',None)
else:
 if not args.apk:raise SystemExit('--apk is required')
 for file in [key,password]:
  if file.stat().st_mode & 0o077:raise SystemExit('Signing material must have owner-only permissions.')
 dest=r/'release-local';dest.mkdir(exist_ok=True)
 output=dest/'ufuq-launcher-1.0.0-rc1.apk'
 # AGP's build metadata is not runtime input and is excluded from v1 JAR coverage.
 # Remove it before signing, then restore standard APK alignment for API 19+.
 stripped=dest/'signing-input.apk';aligned=dest/'signing-aligned.apk'
 with zipfile.ZipFile(args.apk) as src, zipfile.ZipFile(stripped,'w') as target:
  for info in src.infolist():
   if info.filename != 'META-INF/com/android/build/gradle/app-metadata.properties':
    target.writestr(info,src.read(info.filename))
 run([str(sdk/'build-tools/34.0.0/zipalign'),'-f','4',str(stripped),str(aligned)],env=env)
 tool=sdk/'build-tools/34.0.0/apksigner'
 run([str(tool),'sign','--ks',str(key),'--ks-key-alias','ufuq-public','--ks-pass','file:'+str(password),'--v1-signing-enabled','true','--v2-signing-enabled','true','--out',str(output),str(aligned)],env=env)
 run([str(sdk/'build-tools/34.0.0/zipalign'),'-c','4',str(output)],env=env)
 result=run([str(tool),'verify','--verbose','--print-certs',str(output)],env=env)
 expected=json.loads((r/'docs/PUBLIC-SIGNING-IDENTITY.json').read_text())['certificateSha256']
 if expected not in result.stdout:raise SystemExit('Unexpected certificate fingerprint')
 (dest/'signature-verification.txt').write_text(result.stdout)
 digest=hashlib.sha256(output.read_bytes()).hexdigest()
 (r/'docs/SHA256SUMS-SIGNED.txt').write_text(digest+'  '+output.name+'\n')
 print('Signed candidate verified with independent public certificate. SHA-256:',digest)
