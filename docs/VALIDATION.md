# Current candidate: RC2 hold-OK / reflection correction

See [latest correction and manual remote gate](REORDER-GLASS-VALIDATION.md). Required local checks pass 305 tests; release lint 0 errors / 147 warnings; release build passes. Physical remote acceptance is explicitly pending.

# Preceding candidate: RC2 functional-parity correction

See [functional/stability parity](FUNCTIONAL-PARITY.md) for 295 passing tests, release lint 0 errors / 147 warnings, a successful release build and current device evidence. [Visual parity](VISUAL-PARITY.md) and [earlier RC2 validation](RC2-VALIDATION.md) record preceding iterations. The following RC1 publication snapshot is retained as historical evidence.

# Final publication validation — 2026-10-29

Command from launcher/: `./gradlew --no-daemon testReleaseUnitTest lintRelease assembleRelease`.
Result PASS with JDK 17, Gradle 8.9, AGP 8.5.2 and SDK 34.
Tests: 243 across 42 classes; failures 0, errors 0, skips 0.
Lint: 0 errors and 146 warnings (accepted baseline: 200). No lint baseline or global suppression added.
See LINT-REVIEW.md for every category's disposition.

## Android framework simulation

Robolectric 4.12.2 (test only) ran on API 19, 28 and 34. It checks all three saved palette IDs across
settings recreation; independent fixed/random wallpaper choices and three bundled images without
external files; English/Arabic locale direction and injected D-pad focus transitions; English,
Arabic and Hebrew Home inflation with all required IDs; and favourites, greeting and optional
module settings persistence after reopening. Both modern style inheritance and stale Hebrew layout
startup failures were reproduced by tests and fixed.

These are framework simulations, not a device/emulator, graphical screenshot or physical-remote test.
No emulator executable is installed in the inspected SDK. No ADB/device commands or protected TV
interactions occurred. Visual theme rendering on TV, actual process-kill/reboot recovery, low-memory
performance, vendor inputs, Wi-Fi hardware and API 19 TLS remain unverified. The original procedural
wallpaper image files were visually inspected separately; that is not an app screenshot claim.

## Build and signing

Package io.github.mansouralotaibi.ufuq, version 1.0.0-rc1, code 10000, minimum API 19, target 34.
Unsigned APK SHA-256: `5f5e5c6de6225e59869fe4fb60a82b96a0177d6e4bb9a2dfc97672d72bd05889`.
Signed APK SHA-256: `502d85b8c13468ee4025143523bf89f7107ebc67c012a6ead7d307415741aaff`.
Public certificate SHA-256: `3b91e63bb81d85dd4cfb30c63227b46c08ddea300c9c1d8e347bebc942be7bd3`.
A new independent RSA-3072 key was generated; no personal key was accessed. apksigner verification
passes v1/v2/v3 and zipalign checks pass. Non-runtime AGP META-INF app-metadata is removed before
alignment/signing to avoid an unprotected v1 metadata entry; application code/resources are unchanged.
VCS history metadata is excluded from release APKs. No signing secret is in Git or the APK.

Owner-only ignored local provisioning files are not an encrypted recovery backup. Separate encrypted
backup and recovery testing remain an owner action before public binary distribution.

## Additional checks

- Reflection source hash/verbatim/license verification passes; all Quran/Hadith/football features remain.
- Prior live TheSportsDB response parsed OK on the desktop; Android network/TLS behavior is unverified.
- All image/font assets are allowlisted with hashes and source-specific licenses. Official Outfit and
  Gradle wrapper provenance verified. Three MIT wallpapers are actually packaged and selectable.
- Complete upstream desugaring release source is prepared with a verified archive hash for distribution
  alongside future binaries. It is not embedded in the app or public Git tree.
- Public history, source, APK contents, local document links and secret/artifact policy are audited at handoff.

CI has not run on GitHub because no push is authorized. Cross-machine byte-identical reproducibility
and an external vulnerability-database certification are not claimed.

## Remaining lint category counts

- AppBundleLocaleChanges: 1
- ApplySharedPref: 6
- DiscouragedApi: 4
- HardcodedText: 12
- InlinedApi: 10
- MergeRootFrame: 1
- MissingQuantity: 5
- NotifyDataSetChanged: 2
- ObsoleteSdkInt: 2
- Overdraw: 2
- RtlHardcoded: 22
- SetTextI18n: 5
- Typos: 1
- Untranslatable: 10
- UnusedAttribute: 34
- UnusedQuantity: 1
- UnusedResources: 24
- UselessParent: 1
- ViewConstructor: 3
