# Safe installation

Use only a separately signed Ufuq build after verifying its SHA-256 and signing certificate.
The public package must be `io.github.mansouralotaibi.ufuq`; reject any other identifier.
Never install an unsigned CI artifact as an official release.

1. Start on a non-production Android TV or emulator, not the protected Nobel TV.
2. Open the signed APK through the device's package installer and explicitly approve installation.
3. Open Ufuq from the device app list. Installation does not prove full compatibility.
4. If desired, choose a HOME app yourself in device Settings. Some vendors prohibit third-party HOME.
5. To stop testing, return to the original launcher through system Settings before removing Ufuq.

Never disable, clear, uninstall or grant privileged permissions to another launcher.
Never enable accessibility through shell commands. Optional remote helpers require explicit
user selection in Android accessibility settings and are not necessary for ordinary D-pad use.
Legacy Wi-Fi entry passes credentials directly to Android; modern devices use system settings.
Do not grant WRITE_SECURE_SETTINGS.

The legacy-named build-and-install scripts now ONLY build and validate. They do not install.

## RC2 verification and recovery

The accepted candidate is version1.0.0-rc2/code 10001, independently signed for `io.github.mansouralotaibi.ufuq`. Its SHA-256 is:

```text
17fe36e3e416f1f33627a7d0715846adbe5158d1c5ce426451b1c3b0e893f766
```

Verify against a trusted checksum and [public certificate](docs/PUBLIC-SIGNING-IDENTITY.json) before installation. A failed certificate/package check is a stop condition. Android may require the user's normal unknown-source installation consent. Review that prompt yourself; do not grant accessibility, device-admin or secure-settings permissions merely to install Ufuq. Open Ufuq explicitly from the app list; keep your current HOME unless you deliberately choose otherwise later. No inherited installer script is needed.

If an update fails because of a signature mismatch, stop and verify provenance. Do not uninstall a protected launcher or clear anyone's data to bypass the error. Inspect [compatibility](COMPATIBILITY.md) before assuming missing Wi-Fi/Inputs/system controls are bugs. Unsupported actions use guarded system handlers or an unavailable explanation. Arabic and English can be selected in Ufuq Settings; font preferences are independent. Ordinary cinematic settings update in place; language change has a controlled recreation.

Tested hardware is the specific Nobel/CVTE API 19 device described in the compatibility matrix. API 28/34 tests are simulated; Google TV/other firmware and API 35+ hardware remain untested. Existing-owner validation does not make arbitrary devices certified. See [RC2 notes](docs/RELEASE-NOTES-1.0.0-RC2.md) for current scope. Release drafts/checksums prepared locally are not evidence of a public download being available yet.
