# Feature comparison: MansourTV Ufuq 2.8.5 source import → Ufuq 1.0.0 RC1

Comparison baseline: protected `source-import-2.8.5`, commit
`057253e4b36f51268eb68f0ab9568b6483384cff`. Only this repository was inspected.
This compares implementation, not a physical-TV installation or personal external theme folder.
All 77 baseline Java files remain. The initial namespace-normalized comparison found 59 unchanged;
the file-by-file hashes/current classifications are recorded in SOURCE-COMPARISON.json.
No claim is made that a source comparison proves runtime or visual parity.

| Feature in imported 2.8.5 | Current candidate | Evidence / differences |
|---|---|---|
| Native Java / Android Views / API 19 | Retained | minSdk 19, Java/desugaring, no WebView/Compose; release build/lint |
| Cinematic Home and app artwork | Retained | MainActivity, AppAdapter, HeroArt, GlassView, Sky, TopFade; public cinematic default enabled |
| Existing cinematic gold appearance | Retained as Classic | External background and festival accent respected in Classic; public branding independent |
| Midnight / Coast | Added | Central palettes, saved selection, shared settings/dialog/focus styling |
| Local cinematic wallpapers | Full engine retained | Config, WallpaperPlayer, WallpaperRotation, WallpaperMood, WallpaperCaptions, WallpaperSize unchanged after namespace migration |
| Fixed/random, shuffle, captions, dimming, rotation | Retained | Existing preference keys and selection logic untouched; palette uses a separate preference file |
| Bundled wallpaper collection | Added three MIT cinematic landscapes | New original procedural assets; personal images remain untouched |
| Prayer times and five-prayer day rail | Retained | Offline PrayerCalculator/DayRailView, city selection, next-prayer countdown; prayer tests |
| Hijri calendar / adjustment | Retained | HijriDateFormatter and adjustment settings; offline tests |
| Daily Quran / Hadith / mixed / off | Restored and enhanced | Seven offline records covering the baseline references; modes rotate within the selected category instead of using a fixed first entry |
| Full Daily Reflection detail / Home preview | Retained | Complete source strings, visual-only clipping, full detail dialog and source/license attribution |
| Quran/Hadith provenance | Replaced with documented permitted sources | Four complete Tanzil verses; three CC BY-SA Wikisource Hadith excerpts; pinned revisions, raw snapshots, hashes and offline verification |
| TheSportsDB fixtures, team selection and details | Restored | Original official endpoint, parsing, stale-response discard, timezone handling, last-good cache; no artwork downloaded |
| Football caching and errors | Retained with retry floor | Successful cache one hour; minimum one minute between foreground request attempts; provider error/offline states retained |
| Favorites, reorder and dock slots | Retained | Order, DockSlots, AppAdapter and app-library code; existing tests |
| Apps / recent / app discovery | Retained | AppLibraryDialog, LibraryApps/Loads, ArabicAppLabels; no arbitrary app disabling |
| Personalized greetings and date/clock controls | Retained | Local greeting text, Arabic/English handling and time-format settings |
| Native Wi-Fi network list, signal, saved networks, password entry | Restored on API 19–28 | Legacy permissions scoped to API 28; API 23–28 asks user for scan permission; no installer grants |
| Wi-Fi on API 29+ | System-managed equivalent | Android restricts legacy connection APIs for ordinary apps; system settings fallback, not a provider/content-driven reduction |
| Unsupported enterprise/WPA3/OWE configuration | Safe system fallback | Native panel does not misconfigure unsupported security; system-settings action always available |
| Connection SSID/signal and VPN pill | Restored | Local display subject to platform permission/redaction; not logged or sent to football provider |
| TV Inputs / tuner / vendor source adapters | Retained | API guards and optional Coocaa/vendor mappings retained; no vendor package assumed as public default |
| User-driven launcher switching | Retained | Open other launcher and Android HOME Settings; firmware may refuse third-party defaults |
| Clock/screensaver / idle clock | Retained | ClockActivity, ClockDream, ClockScene; no new device claims |
| Reduced motion / advanced effects / festive configuration | Retained | Existing config and rendering implementations preserved |
| Optional remote accessibility helpers | Retained | HomeKeyService/InputKeyService remain opt-in through system accessibility settings |
| Automatic privileged accessibility restoration | Intentionally not automatic | Public app does not request WRITE_SECURE_SETTINGS; users re-enable helpers in system settings. No privileged installer behavior is reintroduced |
| Automatic HOME change / disabling stock launcher via scripts | Removed as required safety boundary | Legacy-named scripts only build; this does not remove user-controlled in-app switching |
| Source package identity and provider authority | Isolated | Public namespace/application ID/custom categories; sandbox-local files/preferences; protected overrides rejected |
| Private branding raster files | Replaced | Native Ufuq shapes; original files remain in protected history with unresolved file-level provenance |
| Weather live provider | No new omission | Imported composition itself only builds prayer/football modules; no live weather provider existed in that baseline |

## Validation boundaries

Local JVM tests and release lint/build cover code/data behavior. A desktop read-only TheSportsDB
request returned an event; it does not prove Android API 19 TLS or runtime operation.
No TV, ADB, protected application, original project or personal backup was accessed.
No emulator executable was available. Runtime persistence, all three palettes, RTL/LTR, physical
D-pad, low-memory behavior, Wi-Fi and vendor input behavior still require non-production testing.

## Remaining publication gates

Separate public signing, historical asset/content provenance, packaged dependency notices,
private security contact, lint-warning review and runtime verification remain. GitHub remains private;
no push or release is authorized by this comparison. A disclaimer is not a substitute for missing rights.

Public snapshot update: old unresolved asset history is excluded through a new independent root;
Outfit now has a pinned official source, icons have original replacements, and three original
wallpapers are bundled. Android framework simulation now covers locales, focus and persistence.
See VALIDATION.md for scope; no physical/visual equivalence is inferred.
