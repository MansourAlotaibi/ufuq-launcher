# Building 1.0.0-rc2

Use JDK 17, Android SDK platform 34 and build-tools 34.0.0. Set JAVA_HOME and
ANDROID_HOME locally; never commit local.properties. Gradle 8.9 is checksum-pinned.

```sh
cd launcher
./gradlew --no-daemon testReleaseUnitTest lintRelease assembleRelease
```

The default output is `app/build/outputs/apk/release/app-release-unsigned.apk`.
Unsigned output cannot be installed and is never an official release.
The build rejects `-PlauncherApplicationId`; the public identity is fixed.
Dependencies are version-pinned, but byte-identical builds across machines have not been established.

## Separate public signing identity

This project already has its independent public Ufuq identity; reuse it for updates and never run key generation to replace it. Never use a MansourTV key. Only an independent fork starting a new identity should generate its own key. Example for such a fork, run interactively (no passwords on the command line):

```sh
keytool -genkeypair -keystore <private-public-ufuq-key.jks> -alias ufuq-public -keyalg RSA -keysize 3072 -validity 10000
```

Supply `UFUQ_KEYSTORE`, `UFUQ_KEY_ALIAS`, `UFUQ_STORE_PASSWORD`, and `UFUQ_KEY_PASSWORD`
through a private local environment. Run assembleRelease again. Do not use shell tracing.
Do not supply keys to pull-request workflows. Confirm the certificate fingerprint is different
from the personal key using owner-held records; this project must not access personal key material.

Keep two encrypted offline backups and record the alias, certificate SHA-256, recovery instructions
and password-manager location separately. Loss of the key prevents normal updates.

```sh
apksigner verify --verbose --print-certs <signed-apk>
shasum -a 256 <signed-apk>
```

Record the signed APK checksum independently: it differs from the unsigned candidate checksum.
A fresh independent RSA-3072 public identity has now been provisioned locally. Its public certificate
fingerprint is recorded in docs/PUBLIC-SIGNING-IDENTITY.json. It is unrelated to the personal key,
which was never accessed. Do not run `init` to replace an existing identity.

Local signing helper (no secret values in command arguments/output):

```sh
python3 tools/public_signing.py sign --apk launcher/app/build/outputs/apk/release/app-release-unsigned.apk
```

The encrypted PKCS12 and high-entropy password file are under ignored private/public-signing/;
the directory is mode 700 and both secret files are mode 600. This is protected local provisioning,
not a substitute for an encrypted offline recovery backup. Before public binary distribution, the
owner must put key and recovery information in separate encrypted/offline custody, test recovery,
and remove any plaintext provisioning password from the workstation only after verifying recovery.
Never copy private/ into a release or commit it. The helper uses password files for apksigner, not
password arguments. The signed APK is written to ignored release-local/.

Run `python3 tools/prepare_dependency_sources.py` to prepare the pinned complete desugaring source
archive. Publish that archive alongside any future signed binary, plus Ufuq source/build instructions.
No key, binary or source archive is uploaded by these helpers. VCS metadata is excluded from APKs
to avoid embedding private development-history references.

The local signing helper verifies the public application ID and derives the output filename from the APK version. It strips non-runtime AGP metadata before alignment/signing, then verifies the pinned public certificate. RC2 does not overwrite the preserved RC1 candidate.
