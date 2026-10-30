# Compatibility

## RC2 evidence — 30 October 2026

| Target | Evidence | Status |
|---|---|---|
| Nobel/CVTE SMART, board CVTM638_768_PC822, Android 4.4.4 / API 19, 1920×1080 | Independently signed public package exercised on the physical TV | Tested for the specific flows in the RC2 report; not a firmware-wide certification |
| API 19, 28, 34 | Robolectric framework regression tests, release lint/build | Simulated; API 28/34 hardware not tested |
| API 21–34 other devices | Runtime guards, package discovery and desugaring | Hardware untested |
| API 35+ | No runtime evidence | Untested |
| Arabic RTL / English LTR, Classic / Midnight / Coast | Actual TV captures and framework tests | Tested screens only |
| D-pad | Injected events and owner-reported complete Arabic/English physical-remote flow on final signed 17fe audit candidate | Owner confirms full flow and all ordering pass; individual optional substeps were not separately itemized. See final audit for the earlier non-reproduced scripted observation. |
| Process restart | Only the independent public package may be stopped for recovery testing | Does not establish TV reboot or low-memory soak behavior |

The public application ID is `io.github.mansouralotaibi.ufuq`. It cannot update the protected personal package. Installation does not set HOME, disable another launcher or grant accessibility/system permissions. The owner expressly authorized a single read-only reference capture of the installed personal Home; its settings and installation were not changed.

Wi-Fi API 19–28 retains the native panel; API 23–28 requires user-approved scan permission and may require system location services. API 29+ uses system settings because ordinary apps cannot use legacy configuration APIs. Missing handlers show unavailable. No Wi-Fi connection was changed in the device tests.

TV inputs are guarded by Android version and actual handlers. API 19 lacks the standard TV Input Framework; a firmware-specific input button in the personal installation is not evidence that an independent application can use it generically. Vendor mappings remain optional, not public defaults. Streaming boxes may have no physical inputs.

Application discovery uses package-manager intent queries; firmware can hide apps. HOME behavior remains firmware/user controlled. Legacy TLS uses platform certificate/hostname verification with TLS 1.2 enabled where needed; old trust stores can still reject a provider. Live provider success on one TV is not universal network compatibility.

Layouts use native Android Views and density units. Extreme density, overscan, font scale and aspect ratios need additional visual testing. No emulator was available in the installed SDK. No synthetic mockup is device evidence.

See [feature comparison](docs/FEATURE-PARITY.md) and [validation](docs/VALIDATION.md). Private screenshots, device dumps and APKs are deliberately excluded from the source repository.

All supported prayer cities are Saudi cities. Prayer dates, displayed times and the marker use UTC+03:00; Home calendars/clock and football match display remain device-local. No global device timezone is changed. WEP, enterprise and modern security schemes use system Wi-Fi configuration rather than the legacy WPA-PSK form.
