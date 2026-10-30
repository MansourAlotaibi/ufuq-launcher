#!/usr/bin/env python3
"""Exercise real scanner enforcement in disposable, synthetic Git repositories."""
from pathlib import Path
import shutil, subprocess, tempfile, unittest
SCANNER=Path(__file__).with_name('security_audit.py')
class ReleasePolicyTest(unittest.TestCase):
 def fixture(self,root):
  (root/'tools').mkdir();shutil.copyfile(SCANNER,root/'tools/security_audit.py')
  (root/'launcher/app/src/main').mkdir(parents=True)
  (root/'launcher/app/src/main/AndroidManifest.xml').write_text('<manifest xmlns:android="http://schemas.android.com/apk/res/android"><application/></manifest>')
  for n in ['build-and-install.sh','build-and-install.ps1']:(root/'launcher'/n).write_text('# Safe fixture only\n')
  self.git(root,'init','-q');self.git(root,'add','.');self.git(root,'commit','-qm','Synthetic baseline')
 def git(self,r,*args):
  subprocess.run(['git','-c','user.name=Policy fixture','-c','user.email=noreply@ufuq.invalid',*args],cwd=r,check=True,stdout=subprocess.DEVNULL,stderr=subprocess.DEVNULL)
 def scan(self,r):return subprocess.run(['python3','tools/security_audit.py','--ref','HEAD'],cwd=r,capture_output=True,text=True)
 def test_safe_fixture_passes(self):
  with tempfile.TemporaryDirectory() as d:
   r=Path(d);self.fixture(r);self.assertEqual(self.scan(r).returncode,0)
 def test_force_added_private_history_fails_even_without_secret_content(self):
  with tempfile.TemporaryDirectory() as d:
   r=Path(d);self.fixture(r);(r/'private').mkdir();(r/'private/example.png').write_bytes(b'innocuous synthetic content')
   self.git(r,'add','-f','private/example.png');self.git(r,'commit','-qm','Synthetic prohibited evidence')
   result=self.scan(r);self.assertNotEqual(result.returncode,0);self.assertIn('history',result.stdout);self.assertIn('prohibited filename private/example.png',result.stdout)
 def test_untracked_release_evidence_fails(self):
  with tempfile.TemporaryDirectory() as d:
   r=Path(d);self.fixture(r);(r/'release-local').mkdir();(r/'release-local/report.txt').write_text('synthetic evidence')
   result=self.scan(r);self.assertNotEqual(result.returncode,0);self.assertIn('worktree: prohibited filename release-local/report.txt',result.stdout)
if __name__=='__main__':unittest.main()
