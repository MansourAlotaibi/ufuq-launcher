# Security policy

Ufuq Launcher **1.0.0-rc2 is a published public pre-release**. It has no production security-support or response-time guarantee. The latest maintained release and current development branch are the reporting targets; older RC1/development snapshots should be updated before reproducing a report. See the [RC2 release](https://github.com/MansourAlotaibi/ufuq-launcher/releases/tag/v1.0.0-rc2) for the exact source, signed APK, checksums and tested compatibility scope.

## Report privately

Private Vulnerability Reporting is enabled. Use **[Report a vulnerability](https://github.com/MansourAlotaibi/ufuq-launcher/security/advisories/new)** from [Ufuq Security Advisories](https://github.com/MansourAlotaibi/ufuq-launcher/security/advisories). This creates a private report, not a public issue.

Do not put exploit details, keys, Wi-Fi identifiers, private device logs or personal data in public issues, discussions or screenshots. Include the affected version, Android version, minimum reproduction, impact and a safely redacted proof of concept. Never attach a signing key, password, encrypted recovery backup or unredacted device dump.

If the private reporting entry point is unavailable, wait for an owner-designated private channel rather than disclose sensitive details publicly. No fallback email address has been designated. Enabled reporting and a visible entry point do not establish a tested notification-delivery guarantee; maintainers should monitor private reports and configure security notifications.

Maintainers should acknowledge, reproduce and coordinate remediation privately, and agree on disclosure timing with the reporter. Do not publish an advisory automatically or promise a response SLA that has not been staffed.

## Security boundaries

- Independent public package/signing identity; no personal MansourTV key or privileged installation grants.
- HTTPS certificate and hostname verification must remain enabled.
- CI receives no release-signing credentials and produces unsigned validation artifacts only.
- Permissions, local data, API usage and legacy Wi-Fi behavior are documented in [PRIVACY](PRIVACY.md).
- Preserve published release tags, APK bytes and checksums. Report problems without replacing the published RC2 artifacts.
- The owner confirmed encrypted local signing backup and separate-local-copy recovery, including API 19 signing verification. Independent off-device recovery remains outstanding and was explicitly deferred until after launch; full disaster-recovery protection is not claimed. See [SIGNING-RECOVERY](docs/SIGNING-RECOVERY.md). Never include signing or recovery files in project archives, CI or releases.

Reference: [GitHub's private-reporting configuration guide](https://docs.github.com/en/code-security/how-tos/report-and-fix-vulnerabilities/configure-vulnerability-reporting/configure-for-a-repository).
