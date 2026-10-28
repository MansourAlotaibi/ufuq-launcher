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
