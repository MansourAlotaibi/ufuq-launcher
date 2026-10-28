# Security policy

The 1.0.0-rc1 candidate is under review and has no production security-support guarantee.
Do not post vulnerabilities, keys or private device data in public issues.
Use GitHub private vulnerability reporting if enabled by the owner. If it is unavailable,
wait for an owner-provided private reporting channel; do not disclose sensitive details publicly.
Enabling and testing that channel is a publication gate.

No embedded API credentials, personal keys or privileged installation grants are allowed.
Never bypass TLS certificate/hostname verification. CI receives no signing secrets.
See [privacy](PRIVACY.md) and [build/signing](BUILDING.md).
