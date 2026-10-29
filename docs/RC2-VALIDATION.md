# RC2 validation — 29 October 2026

## Build and framework evidence

From `launcher/`: `./gradlew --no-daemon testReleaseUnitTest lintRelease assembleRelease`.
JDK 17, Gradle 8.9, SDK 34; release build PASS. Tests: **267**, failures/errors/skips **0**.
Robolectric runs on API 19, 28 and 34; this is framework simulation, not extra physical devices.
Release lint: **0 errors, 147 warnings**. No lint baseline or suppression was added; see [lint review](LINT-REVIEW.md).

New regression coverage includes visible saved greeting and all independent date combinations, navigation hit bounds when dates are hidden, Large shelf defaults and controls, dim/interval persistence across palettes, unboxed native prayer/football views, immediate selected-team cache rendering, wrong-team rejection, RC1 cache migration and the slow old-team response race.

## Physical TV evidence

Confirmed Nobel/CVTE SMART, board CVTM638_768_PC822, Android 4.4.4 / API 19, 1920×1080.
A separate public Ufuq installation was updated using the native Android package installer, preserving its data. The APK uses `io.github.mansouralotaibi.ufuq` and the existing independent public certificate. Installed bytes are checked against the signed candidate checksum.

- Arabic RTL and English LTR Home captured in Classic, Midnight and Coast. All use the same cinematic layout.
- Large clock, saved greeting, independent dates, opposite reflection, integrated prayer/football horizon and bottom favorites are present.
- All four Gregorian/Hijri visibility combinations checked on the TV in Arabic; English both-on and framework combinations checked. A date-off touch-area defect was reproduced and corrected.
- Three palettes selected and recovered across public-app process restarts. No protected app was force-stopped.
- Small/Large favorites and a D-pad pick/move/drop/save exercised; ordering survives updates/restarts. New-user Large default is also covered by framework tests; no physical app-data clear was used.
- Fixed bundled wallpapers, 0%/60% dimming and saved 30% dimming exercised independently of palette. Per-scene readability remains selectable.
- Automatic random rotation observed during 91 seconds of uninterrupted foreground at the actual user-configured one-minute interval. This is not a claim that the default 15-minute interval or long-duration soak was tested.
- Quran and Hadith previews and full-text attribution dialogs exercised. Offline source/license/hash verification passes; no text or rights notices were removed.
- Cached Nassr/Hilal fixtures, team changes and return-to-cache exercised. A bounded live Al Ittihad request returned HTTP 200, parsed OK and displayed a verified fixture; no certificate bypass or synthetic fixture was used as device evidence.
- Direct Apps access, injected D-pad reflection/module focus, favorites ordering and native Wi-Fi panel exercised. Wi-Fi connection settings were not changed. The one-time OEM launcher picker opened the OEM launcher successfully without setting a default. Original TV Settings foreground was restored afterward. Protected package version/update/enabled metadata and the normalized preferred-HOME tuple match the pre-test baseline.

The owner approved one read-only capture of installed MansourTV 2.8.5 for side-by-side visual comparison. Its personal wallpaper, greeting and favorite choices were not copied into public source. Reference and all test screenshots remain owner-only local evidence, excluded from Git. No generated mockup is used as runtime evidence.

## Lifecycle diagnosis

RC1 recorded seven distinct nonfatal `ActivityThread` pause warning events during settings-driven recreation. Home called `recreate()` during lifecycle callback delivery on KitKat. RC2 posts recreation until callback delivery completes; repeated theme/language/date changes and process recovery are inspected for the same diagnostic. They are treated as a defect to fix, not ordinary expected log noise. Final captured logs contain 0 fatal exceptions, 0 public-app ANRs, 0 out-of-memory errors and 0 recurrence of the pause warning (164 unique exact-public-PID log lines across bounded captures). This is observed evidence, not an indefinite stability guarantee.

## Limits and review gate

D-pad input is injected ADB key events, **not a physical remote press**. Other TV hardware, overscan/density extremes, physical accessibility-helper scan codes, reboot/low-memory stress and long-duration rotation remain unverified. Prayer rendering matches the offline calculation; independent religious timetable accuracy and lunar observation are not certified.

API 19 lacks standard TV Input Framework support. The personal firmware's optional Inputs adapter is retained as configurable code; it is not enabled blindly on unrelated devices. Public bundled procedural wallpaper styling differs from the restricted personal photo while preserving composition and legally distributable assets.

No RC2 GitHub push, main update, visibility change, signed APK upload or Release occurred. Private remote main remains at `4faeede5e1c9d7189a3928a2215afab75cd23767`. The RC2 development commit is for owner review before any main update. CI for RC2 must run after a separately approved push; local results are not represented as a new GitHub Actions run.
