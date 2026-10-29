# Privacy and permissions

No analytics, telemetry, accounts or background API polling. Preferences, favorites, appearance,
optional greeting and provider cache remain in Ufuq's sandbox. Android backup is disabled.
The separate package never reads or migrates personal MansourTV preferences. Local wallpaper files
are in the package's external-files theme directory. Theme selection and wallpaper choices are independent.

| Permission | Purpose / restriction |
|---|---|
| INTERNET | Optional, user-enabled TheSportsDB fixtures over HTTPS |
| ACCESS_NETWORK_STATE | Connectivity/VPN/status pill |
| ACCESS_WIFI_STATE | Local signal/connection status; SSID can be displayed on supported devices |
| CHANGE_WIFI_STATE | User-requested native network connection on API 19–28 only |
| ACCESS_FINE_LOCATION / ACCESS_COARSE_LOCATION | Wi-Fi scan prerequisites, declared only through API 28; requested at runtime only when opening the legacy panel on API 23–28 |
| READ/WRITE_EXTERNAL_STORAGE | API 19 only for app-owned wallpaper files on legacy FUSE firmware |
| Yandex TV-source permission | Optional vendor input launching; ignored elsewhere |

On Android 10+, configuration opens system Wi-Fi settings. Permission denial also offers system settings.
Legacy Wi-Fi passwords are entered by the user and passed to Android's Wi-Fi configuration API; Ufuq
does not save them in its own preferences or log them. SSIDs and signal are shown locally and are not
sent to TheSportsDB. Do not include Wi-Fi/account details in screenshots or reports.
No installer grants permissions, changes HOME or enables accessibility. Optional remote helpers require
user selection in Android accessibility settings. WRITE_SECURE_SETTINGS is not requested; automatic
accessibility restoration is not performed. The theme provider uses a package-derived authority and
restricts external callers with that system permission; it does not grant the app that permission.

Football is off by default; selecting a team and enabling the card sends that public team ID to
TheSportsDB, along with normal connection metadata (including the network's public address).
No device identifier, SSID, personalized greeting or favorites list is sent. The provider has its own
terms/privacy policy linked from its site. Successful per-team results have a one-hour freshness window and remain in the private local cache until replaced or Ufuq data is removed; attempts have a
one-minute floor per team and occur only on foreground lifecycle events. Last successful data can remain visible
when offline. Clearing Ufuq app data removes preferences and caches.

The HTTPS worker uses platform certificate and hostname validation, 8-second connection/read timeouts,
bounded responses and no automatic redirects. API 19 trust/TLS compatibility is not established by a desktop
request; unsupported certificates fail without bypassing verification. Daily Reflection is fully offline;
source/license links open externally only when selected by the user. No runtime corpus download occurs.
