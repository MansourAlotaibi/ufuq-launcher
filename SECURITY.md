# Security policy

The accepted 1.0.0-rc2 candidate is undergoing final publication preparation. It has no production security-support or response-time guarantee. The latest maintained candidate is the supported reporting target; older RC1/development snapshots should be updated before reproducing a report.

## Report privately

Do not put exploit details, keys, Wi-Fi identifiers, private device logs or personal data in public issues, discussions or screenshots.

When enabled, use **Report a vulnerability** on [Ufuq Security Advisories](https://github.com/MansourAlotaibi/ufuq-launcher/security/advisories). This is a private reporting mechanism, not a normal public issue. Include the affected version, Android version, minimum reproduction, impact and a safely redacted proof of concept. Never attach a signing key, password or unredacted device dump.

GitHub documents this feature for public repositories. While Ufuq remains private, availability must not be assumed. If the button is absent, wait for an owner-designated private channel rather than disclose sensitive details publicly. No fallback email address has yet been designated; none is invented here. Enabling and testing a private reporting route, or designating and testing an owner-controlled fallback before publication, is an owner publication gate. See [the publication checklist](docs/PUBLICATION-CHECKLIST.md).

Maintainers should acknowledge, reproduce and coordinate remediation privately, and agree on disclosure timing with the reporter. Do not publish an advisory automatically or promise a response SLA that has not been staffed.

## Security boundaries

- Independent public package/signing identity; no personal MansourTV key or privileged installation grants.
- HTTPS certificate and hostname verification must remain enabled.
- CI receives no release-signing credentials and produces unsigned validation artifacts only.
- Permissions, local data, API usage and legacy Wi-Fi behavior are documented in [PRIVACY](PRIVACY.md).
- Key custody and a real backup-based signing test follow [the owner recovery procedure](docs/SIGNING-RECOVERY.md).

Reference: [GitHub's private-reporting configuration guide](https://docs.github.com/en/code-security/how-tos/report-and-fix-vulnerabilities/configure-vulnerability-reporting/configure-for-a-repository).
