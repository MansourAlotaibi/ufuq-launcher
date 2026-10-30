# Final publication gate — owner review

## Completed source-only operation

`7091b90f9bd669823918e947f6e4af6c0e9028f3` was pushed normally to private `origin/main`. No other branch/tag/import history was pushed. The approved audit branch remains unchanged. The clean history has one audited parentless root; protected local main and import tag remain preserved separately.

[GitHub Actions 41077946236](https://github.com/MansourAlotaibi/ufuq-launcher/actions/runs/41077946236) completed successfully for that exact SHA. Downloaded CI reports confirm355 tests / 0 failures / 0 errors / 0 skips and lint: 0 errors / 149 warnings / 243 informational. Build, security policy, reflection/font integrity and packaged assets/accompanying source checks passed. No signed APK was uploaded; Actions' unsigned validation artifacts remain private and are not official release binaries.

Additional publication documentation and bilingual galleries descend through local `codex/publication-preparation` and `codex/bilingual-promotional-gallery`. It has not been pushed and does not modify application source, signing material or the accepted APK. Review and authorize any subsequent documentation push separately; approval for7091b90 is not approval for later commits.

## Owner actions before public publication

1. **Signing custody:** the AES-256 local backup and separate-local-copy recovery test are **COMPLETED**, owner-executed/confirmed on 2026-10-30. Integrity, recovered-file checksums, exact certificate match, recovered-key disposable signing, API 19 v1 and v2 verification, unmount and temporary cleanup all passed. See [SIGNING-RECOVERY](SIGNING-RECOVERY.md). **Independent off-device encrypted/offline custody and recovery remain OUTSTANDING**; the same-Mac exercise does not cover loss of the Mac or independent passphrase recovery. Complete that separate disaster-recovery requirement before public binary distribution. Never disclose secrets or backup locations in public reports.
2. **Private reporting:** GitHub documents private vulnerability reporting for public repositories. The read-only check while private returned404, so no enabled state is claimed. Choose an owner-controlled private fallback before publication or explicitly include enabling GitHub private reporting in the later public-visibility action. As soon as available, use Settings→Security and quality→Advanced Security→Private vulnerability reporting, enable it, check reporter-facing **Report a vulnerability**, and send a harmless test through an independent account. Verify private receipt/maintainer notification and no public issue; keep the test private. If this cannot be verified, hold publication. No email/fallback or successful test is invented.
3. **Policy/documentation:** review the local SECURITY policy, release notes, installation guide, compatibility limits and dated audit evidence. Authorize any desired documentation commit for source push explicitly. App/SDK/signing configuration is unchanged.
4. **Promotional privacy:** review only the curated local promotional set and its provenance manifest. Exclude raw audit galleries, Wi-Fi/network labels, personal greetings, private app inventories, diagnostic XML/logs and device identifiers. Edited images must be labeled promotional derivatives, never fresh runtime evidence.
5. **Final artifact review:** confirm package, independent certificate, signed APK SHA-256, source SHA and matching source/notices/companion archive. Review TheSportsDB's actual distribution-channel terms and retained font/wallpaper/religious attribution. Do not treat a disclaimer as redistribution permission.
6. **Separate explicit public approval:** specify the exact source SHA, desired public visibility, release/tag name, reviewed notes, exact signed APK checksum, accompanying files and approved promotional images. Public visibility and Release/APK upload remain separate from the already completed private source push. Recheck main/privacy/terms and audit before executing any later approved action.

## Required candidate checksum

```text
17fe36e3e416f1f33627a7d0715846adbe5158d1c5ce426451b1c3b0e893f766  ufuq-launcher-1.0.0-rc2.apk
```

Final verdict remains **CONDITIONALLY READY** in the tested Nobel scope. Broader hardware/accessibility/soak coverage and noncritical lint/localization debt remain documented; installation alone is not compatibility proof. No TV interaction is needed or performed for this source-push/publication-materials task.

References: [private reporting](https://docs.github.com/en/code-security/how-tos/report-and-fix-vulnerabilities/configure-vulnerability-reporting/configure-for-a-repository), [release draft](RELEASE-NOTES-1.0.0-RC2.md), [recovery](SIGNING-RECOVERY.md).

## Exact next publication sequence

See [PUBLIC-PUSH-PLAN](PUBLIC-PUSH-PLAN.md) for the ordered source-only private push, CI, owner gates and separately approved public release steps. No future push, tag, upload or visibility change is authorized by this report.
