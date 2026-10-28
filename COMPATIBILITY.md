# Compatibility

| Target | Evidence for this RC | Status |
|---|---|---|
| API 19 / Android 4.4 | minSdk 19, desugaring, release lint | Build target; runtime untested |
| API 21–34 | guarded TV-input APIs and package discovery | Runtime untested |
| API 35+ | no runtime evidence | Untested |
| Nobel/CVTE | no device accessed | Protected; not a test target |
| D-pad / physical remote | JVM navigation tests only | Physical remote untested |
| Arabic RTL / English LTR | resources and unit tests | Visual/device verification pending |
| Three palettes / persistence | persisted preference IDs and palette tests | Runtime visual verification pending |

Wi-Fi API 19–28 retains the native panel; API 23–28 requires a user-approved scan permission and
may require system location services enabled. Permission denial opens system settings. API 29+ uses
system settings because ordinary apps cannot use the legacy configuration APIs. Missing handlers show unavailable.
TV input discovery is guarded by Android version; streaming boxes may have no inputs.
Vendor source integrations are optional configuration and off by default.
Application discovery uses package-manager intent queries; vendor restrictions can hide apps.
Layouts use Android density units; extreme overscan, density and aspect ratios require visual tests.
HOME support is controlled by firmware and user settings, never an automatic installation step.
TLS uses platform trust and hostname validation; old trust stores may reject modern servers.
No emulator executable was available in the inspected SDK. No connected-device commands were run.

Before final release: test cold starts, Back, every D-pad edge, focus restoration, RTL/LTR,
all palettes, process restart persistence, fixed/random wallpapers and low-memory recovery
on disposable targets. Do not infer compatibility from installation alone.

See [feature comparison](docs/FEATURE-PARITY.md) for source evidence and differences from 2.8.5.
