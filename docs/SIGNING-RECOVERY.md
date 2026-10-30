# Public signing-key backup and recovery — owner procedure

Scope: only the independent Ufuq public signing identity. Never copy or open a personal MansourTV key, ADB key, existing personal backup or unrelated credential. The procedure is owner-executed. The assistant does not receive encryption passphrases or signing secrets. The dated status below distinguishes owner-confirmed execution from remaining disaster-recovery custody.

## Verified local recovery — 2026-10-30

**COMPLETED — owner-executed and owner-confirmed in a private local Terminal.** This records the owner's reported results, not an assistant-observed signing session or a new application test.

| Check | Result |
|---|---|
| AES-256 encrypted image integrity | PASS |
| Recovery from a separate local encrypted-image copy | PASS |
| Recovered file checksums | PASS |
| Recovered public certificate SHA-256 | MATCH — fingerprint below |
| Disposable APK signed using the recovered key | PASS |
| API 19 v1/JAR signature verification | PASS |
| v2 signature verification | PASS |
| Image unmounted and temporary test material removed | PASS |
| Recovery script completion | RECOVERY TEST COMPLETE |

The existing public identity was preserved; no replacement key was generated. No secret values, secret-file digests, backup location, image contents or private terminal transcript are recorded here. The owner confirmed cleanup; no claim of physical SSD erasure is made. This local exercise does not establish that a passphrase can be recovered after loss of this Mac, or that another machine can recover the key.

**OUTSTANDING — independent off-device disaster-recovery custody.** Copy the closed encrypted image and its integrity record to new owner-controlled offline storage, verify its checksum, keep it physically separate, and test recovery from that copy. Keep the encryption passphrase independently recoverable outside this Mac and separate from the image. Do not overwrite or touch personal backups. The local backup and separate local test copy do not satisfy this requirement. Preserve the verified local backup; do not recreate it simply because this procedure has been updated. The procedure copy inside the existing backup reflects the version at backup time and its existing manifest must remain valid.

Expected certificate SHA-256:

```text
3b91e63bb81d85dd4cfb30c63227b46c08ddea300c9c1d8e347bebc942be7bd3
```

Alias `ufuq-public`, RSA-3072/SHA256withRSA, package `io.github.mansouralotaibi.ufuq`. Losing this key prevents compatible updates to existing API 19 installations. Do not generate a replacement or run `tools/public_signing.py init` for this project.

## 1. Prepare two separate encrypted/offline copies

Use a trusted Mac with FileVault enabled, a private terminal without screen sharing/recording, and two new owner-selected offline destinations. Keep the encryption passphrase in an independently recoverable password manager, separate from both image copies and the development machine. Use a new strong passphrase; never send it to an assistant, put it in a command argument, shell history, GitHub secret, issue or repository file. Do not overwrite an existing image or backup.

Apple documents [encrypted disk images](https://support.apple.com/guide/disk-utility/create-a-disk-image-dskutl11888/mac). The following uses macOS `hdiutil` and its interactive password prompt. Run from the repository root. Replace the destination with a **new directory** on your chosen backup media. No disk is erased or reformatted.

```sh
set -eu
umask 077
# Set this yourself to a new backup directory, never a protected personal backup.
export UFUQ_NEW_BACKUP_DIRECTORY='/Volumes/NEW_OWNER_BACKUP/Ufuq-public-only'
mkdir "$UFUQ_NEW_BACKUP_DIRECTORY"
ufuq_recovery_image="$UFUQ_NEW_BACKUP_DIRECTORY/Ufuq-public-signing-recovery.dmg"
test ! -e "$ufuq_recovery_image" || exit 1
hdiutil create -size 128m -fs APFS -volname UfuqPublicRecovery \
  -format UDRW -encryption AES-256 "$ufuq_recovery_image"
# Enter and confirm the independent image passphrase at the hidden prompt.
# Do not save it only in this Mac's Keychain.
ufuq_recovery_mount=$(mktemp -d "${TMPDIR:-/tmp}/ufuq-public-recovery.XXXXXX")
hdiutil attach "$ufuq_recovery_image" -mountpoint "$ufuq_recovery_mount" -nobrowse
mkdir -m 700 "$ufuq_recovery_mount/signing"
cp -p private/public-signing/ufuq-public.p12 "$ufuq_recovery_mount/signing/"
cp -p private/public-signing/store-password.txt "$ufuq_recovery_mount/signing/"
chmod 600 "$ufuq_recovery_mount/signing/ufuq-public.p12" \
  "$ufuq_recovery_mount/signing/store-password.txt"
cp docs/PUBLIC-SIGNING-IDENTITY.json "$ufuq_recovery_mount/"
cp docs/SIGNING-RECOVERY.md "$ufuq_recovery_mount/"
(cd "$ufuq_recovery_mount" && shasum -a 256 \
  signing/ufuq-public.p12 signing/store-password.txt \
  PUBLIC-SIGNING-IDENTITY.json SIGNING-RECOVERY.md > SHA256SUMS)
(cd "$ufuq_recovery_mount" && shasum -a 256 -c SHA256SUMS)
hdiutil detach "$ufuq_recovery_mount"
rmdir "$ufuq_recovery_mount"
(cd "$UFUQ_NEW_BACKUP_DIRECTORY" && shasum -a 256 \
  Ufuq-public-signing-recovery.dmg > Ufuq-public-signing-recovery.dmg.sha256)
```

Check every command succeeds; stop on failure. Do not use `-ov`, `sudo`, raw disks or an existing backup directory. The keystore password file is sensitive and is copied only inside the encrypted image. Its checksum manifest must also remain inside that image. Store the image checksum separately as an integrity record; it is not a substitute for encryption or trusted custody.

Copy the **closed encrypted image**, its checksum and this procedure to a second new offline destination using the owner's file manager without replacing anything. Verify both images' SHA-256 against the recorded digest. Disconnect one copy and keep the copies physically separate. Record only the date, certificate fingerprint, copy locations and custody/recovery outcome in your private password manager; never record secret values in a public checklist.

## 2. Verify recovery independently from a backup

Use the second copy, ideally on another trusted Mac with JDK 17 and Android Build Tools 34.0.0. Do not use the original provisioning key to perform the test. This mounts the encrypted backup read-only and proves that its private key can sign; it does not install or publish anything and does not replace the working key.

Set `JAVA_HOME` and `ANDROID_HOME` to your installed JDK/SDK. Set `ufuq_recovery_image` to the actual backup copy. The image passphrase must be entered at the prompt, never placed in the command.

```sh
set -eu
umask 077
ufuq_recovery_mount=$(mktemp -d "${TMPDIR:-/tmp}/ufuq-public-recovery.XXXXXX")
hdiutil attach "$ufuq_recovery_image" -readonly -nobrowse \
  -mountpoint "$ufuq_recovery_mount"
(cd "$ufuq_recovery_mount" && shasum -a 256 -c SHA256SUMS)
# Temporary output contains a PUBLIC certificate and disposable signed test APK, not a copied key.
mkdir -p private
chmod 700 private
ufuq_recovery_probe=$(mktemp -d private/recovery-verification.XXXXXX)
"$JAVA_HOME/bin/keytool" -exportcert -storetype PKCS12 -alias ufuq-public \
  -keystore "$ufuq_recovery_mount/signing/ufuq-public.p12" \
  -storepass:file "$ufuq_recovery_mount/signing/store-password.txt" \
  -file "$ufuq_recovery_probe/recovered.der"
shasum -a 256 "$ufuq_recovery_probe/recovered.der"
# STOP unless the digest equals the expected certificate fingerprint above.
"$ANDROID_HOME/build-tools/34.0.0/aapt" dump badging \
  launcher/app/build/outputs/apk/release/app-release-unsigned.apk
# STOP unless package is io.github.mansouralotaibi.ufuq and minSdk is19.
"$ANDROID_HOME/build-tools/34.0.0/apksigner" sign \
  --ks "$ufuq_recovery_mount/signing/ufuq-public.p12" \
  --ks-key-alias ufuq-public \
  --ks-pass "file:$ufuq_recovery_mount/signing/store-password.txt" \
  --v1-signing-enabled true --v2-signing-enabled true \
  --out "$ufuq_recovery_probe/recovery-probe.apk" \
  launcher/app/build/outputs/apk/release/app-release-unsigned.apk
"$ANDROID_HOME/build-tools/34.0.0/apksigner" verify \
  --verbose --print-certs --min-sdk-version 19 \
  "$ufuq_recovery_probe/recovery-probe.apk"
# Require verification success, v1=true, and the SAME certificate SHA-256.
hdiutil detach "$ufuq_recovery_mount"
rmdir "$ufuq_recovery_mount"
```

If the unsigned APK is missing, build the exact approved source using [BUILDING](../BUILDING.md) first. Never overwrite an official APK: `--out` intentionally targets a new temporary directory. Do not distribute the recovery probe as a release. This is a recovery exercise, not a reproducibility assertion for the signed bytes.

Success means: the offline copy unlocks with independently recovered passphrase, every checksum matches, the certificate matches, the backup's key signs, and API 19 signature verification passes. Merely listing the keystore or checking a public certificate is insufficient. Record pass/fail/date privately. Dismount images even after a failed command; retain failed-test evidence privately for diagnosis without exposing secrets.

## 3. Actual disaster recovery

Recover into a **new empty owner-only directory** on a trusted encrypted machine; copy the two signing files from the verified backup and set directory 700 / files 600. Verify the certificate and repeat the signing probe. If working signing files already exist, stop: do not overwrite or initialize a new identity. Only the owner should deliberately reconnect the verified recovered identity to the normal ignored provisioning location. Restore the existing public identity, never the personal MansourTV identity. Keep key/password out of CI, Git and releases. Never rely on GitHub or this working checkout as the sole backup.

See [Android apksigner](https://developer.android.com/tools/apksigner) for file-based password input, signature verification and the requirement to align an APK before signing.
