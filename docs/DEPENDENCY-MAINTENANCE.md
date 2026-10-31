# Dependency maintenance and API19

Routine dependency updates remain enabled and are reviewed before merging. Latest does not imply compatible. Preserve native Views, minSdk19 and API19 framework-test coverage.

## Compatibility holds

- Robolectric 4.12.2 remains the API19-capable test baseline. The standalone 4.17 update in PR2 failed 43/355 tests, including API19 UnknownSdk and additional class-loading failures. Upstream 4.13 release notes document removal of API19 support. Do not remove legacy tests to approve an update. This is a test dependency, not an APK runtime requirement.
- DynamicAnimation 1.0.0 remains the runtime baseline. PR3's 1.1.0 declares minSdk21 and fails manifest merging. Do not raise Ufuq minSdk or override the library's manifest to accept it.
- AGP8.5.2 / Gradle8.9 / JDK17 remain the passing toolchain. PR4's isolated AGP9.5.0 bump requires Gradle9.6.0. Future toolchain upgrades need coordinated review of Gradle/JDK/SDK, API19, signing, desugaring-source distribution, CI and reproducible build instructions.

Dependabot ignores routine minor/major version updates only for Robolectric and DynamicAnimation, and routine major AGP updates. Patch updates, other dependencies and GitHub Actions remain scheduled as before. These update-types filters do not suppress security updates; any security alert or security PR still needs manual assessment and an API19-compatible remediation plan. Do not dismiss a vulnerability just because its easiest upgrade conflicts with old Android. Inspect transitive dependencies and actual APK impact when applicable. Revisit these holds when project support requirements or upstream compatibility changes.

Arabic: نحافظ على دعم API19 واختباراته. تبقى تحديثات التصحيح وبقية المكتبات والإجراءات مجدولة، ونؤجل تحديثات النسخ غير المتوافقة أو ترقية أدوات البناء غير المكتملة. لا تُلغى التنبيهات الأمنية، ولا تُرفع نسخة أندرويد الدنيا لمجرد تمرير فحص فاشل.

## Evidence

- [PR2](https://github.com/MansourAlotaibi/ufuq-launcher/pull/2), [failed CI](https://github.com/MansourAlotaibi/ufuq-launcher/actions/runs/41129972691), [Robolectric4.13 release](https://github.com/robolectric/robolectric/releases/tag/robolectric-4.13).
- [PR3](https://github.com/MansourAlotaibi/ufuq-launcher/pull/3), [failed CI](https://github.com/MansourAlotaibi/ufuq-launcher/actions/runs/41129985834).
- [PR4](https://github.com/MansourAlotaibi/ufuq-launcher/pull/4), [failed CI](https://github.com/MansourAlotaibi/ufuq-launcher/actions/runs/41129995430).
- [GitHub update-types reference](https://docs.github.com/en/code-security/reference/supply-chain-security/dependabot-options-reference).

This policy does not change dependency versions, application functionality, permissions, signing, released APKs, tags or installed devices. It is reversible through an ordinary reviewed configuration PR.
