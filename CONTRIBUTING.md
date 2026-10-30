# Contributing

Ufuq targets televisions where compatibility and predictable D-pad behaviour matter more than framework novelty.

Before proposing a change:

1. Keep API 19 compatibility.
2. Use native Java/Android Views; do not add WebView dashboards or Compose.
3. Do not add analytics, telemetry, insecure TLS workarounds, persistent polling, or unnecessary services.
4. Keep networking off the UI thread and preserve bounded caches and timeouts.
5. Test Arabic RTL and English LTR, all D-pad edges, Back behaviour, and focus restoration.
6. Never include private device data, credentials, signing material, APKs, or screenshots with Wi-Fi/account identifiers.
7. Run `./gradlew --no-daemon testReleaseUnitTest lintRelease assembleRelease` from `launcher/`.

Changes to HOME handling, accessibility services, Wi-Fi management, sources/inputs, package identity, or signing require explicit compatibility and recovery notes.


Content changes must retain feature behavior and carry source-specific permission, attribution and
provenance evidence. Run `python3 tools/verify_reflection_content.py` after changing reflection assets.
Do not treat a README disclaimer or a repository-wide software license as third-party content permission.

## Pull requests and release preservation

Create a topic branch and open a pull request to `main`, including for owner maintenance. The protected branch requires the GitHub Actions `validate` check, an up-to-date branch and resolved review threads. No second reviewer is mandatory for solo maintenance; passing checks do not authorize automatic merging. Do not force-push main or update/delete published version tags.

Keep the `validate` check name stable and unique when changing workflows. Dependabot proposes Action and Gradle updates for manual review; do not automatically merge toolchain or dependency upgrades. Retain JDK 17, the approved Gradle/SDK configuration and API 19 tests unless a separate compatibility change is justified and tested.

For documentation-only changes, check bilingual accuracy, links, privacy and attribution; the required CI still runs. Configuration changes must validate workflow behavior and relevant build/security checks. Physical-TV claims always require actual device evidence.

The published RC2 tag and its ten assets remain unchanged. Prepare future releases completely in draft before publishing, because repository release immutability applies to future releases. Signing keys, passwords and encrypted recovery images are never release or ordinary project-archive contents.

Report vulnerabilities through [private reporting](https://github.com/MansourAlotaibi/ufuq-launcher/security/advisories/new), not a public issue. See [SECURITY](SECURITY.md).
