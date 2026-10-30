# Ufuq Launcher 1.0.0 RC2 — publication draft

Status: accepted source baseline pushed to the existing **private** repository. No public release, APK upload, visibility change or release tag has been authorized or performed. These notes are prepared locally for owner review.

Approved application source: `7091b90f9bd669823918e947f6e4af6c0e9028f3`. Later local documentation/gallery commits preserve that application tree. The final publication source commit and tag must be approved separately; these notes do not claim those commits are already on GitHub.

[Private CI run](https://github.com/MansourAlotaibi/ufuq-launcher/actions/runs/41077946236): success; 355 tests with zero failures/errors/skips, release lint: 0 errors / 149 warnings / 243 informational findings, release assembly and security/content/license-policy checks passed. CI artifacts are unsigned validation builds, not official signed releases.

## The Ufuq experience

- Cinematic MansourTV-inspired composition: prominent clock, saved greeting, independent Gregorian/Hijri dates, integrated Reflection, five-prayer horizon and favorite-team fixtures.
- Classic, Midnight and Coast share the same geometry and features; palette and wallpaper selection remain independent.
- Arabic RTL and English LTR, independent language-specific font preferences, Default plus Cairo/Tajawal/IBM Plex Sans Arabic/Readex Pro/Almarai, and the existing English Default/Lato choices.
- Offline Daily Reflection with full entry reading and source attribution. The current licensed curated corpus contains four complete Quran verses and three Hadith excerpts; this is not a complete Quran/Hadith-book library.
- Offline prayer calculation for five Saudi cities using selected-city UTC+03:00; Home time/calendars and fixture times remain device-local.
- TheSportsDB text fixtures, immediate team-scoped cache, asynchronous refresh and stale-team protection. Provider outages, rights and channel-specific terms remain external constraints.
- Whole-card favorite pages, physical/injected D-pad navigation, long-OK menu, one-selection Move activation, cross-page ordering, persistent sizing and order.
- Five redistribution-audited cinematic scenes, Fixed/Random modes, remembered fixed choice, rotation frequency and dimming controls.
- Native app library, guarded Wi-Fi/system fallback, safe user-initiated launcher switching and capability-based Inputs.

## Final audit corrections

Improved remote focus versus saved selection, bright-scene control contrast, fixed-wallpaper memory, dependent control focusability, truthful Apps/settings labels, prayer date/marker timezone consistency, bounded HTTP responses and Wi-Fi focus/receiver ownership. No Home redesign or functioning core-feature removal. Original reflection glass geometry/content remains intact.

## Evidence and limits

Actual Nobel/CVTE Android 4.4.4 / API 19 1920×1080 captures cover Arabic/English, all themes, representative fonts, full Quran/Hadith reading, bright/dark scenes and settings. Owner confirms the final signed candidate's physical-remote full flow and ordering pass in both languages. Earlier one-off scripted ordering drift remains recorded as non-reproduced, with no speculative code-fix claim.

API 19/28/34 framework tests are simulations. Other firmware, Google TV hardware, API 35+, extreme font scale/overscan, screen-reader certification and long memory soak are unverified. Wi-Fi network mutation, protected launcher execution and default-HOME changes were deliberately excluded. Language switching has a controlled Activity recreation; ordinary cinematic settings apply in place. Three cold-start samples on Nobel were2.68–2.86 seconds; this is not a general benchmark.

See [COMPATIBILITY](../COMPATIBILITY.md), [INSTALLATION](../INSTALLATION.md), [final audit](FINAL-PRODUCT-AUDIT.md), [PRIVACY](../PRIVACY.md) and [third-party notices](../THIRD-PARTY-NOTICES.md). GitHub distribution is not automatically an app-store distribution; review actual provider terms before selecting another channel.

## Signed candidate identity

- Package `io.github.mansouralotaibi.ufuq`; versionName `1.0.0-rc2`, versionCode10001.
- Certificate SHA-256 `3b91e63bb81d85dd4cfb30c63227b46c08ddea300c9c1d8e347bebc942be7bd3`.
- APK `ufuq-launcher-1.0.0-rc2.apk` SHA-256:

```text
17fe36e3e416f1f33627a7d0715846adbe5158d1c5ce426451b1c3b0e893f766
```

APK signatures v1/v2/v3 and alignment were locally verified; v1 preserves API 19 installation. No personal signing material is used. The candidate must be distributed unchanged after approval; any APK modification requires re-signing and new checksums.

Eventual binary distribution must include this exact signed APK, its checksum, full Ufuq source corresponding to the approved commit, applicable notices/licenses and the verified pinned desugaring source companion. The ZIP's two documented public upstream test-key fixtures are not personal credentials. Do not attach device evidence, raw logs, other APKs, passwords or private backup files. The encrypted local backup and separate-local-copy recovery test were completed and owner-confirmed on 2026-10-30, including recovered-key disposable signing, the exact certificate match, API 19 v1/v2 verification and cleanup. Independent off-device backup/recovery custody and a verified private reporting route remain publication gates. See [recovery status](SIGNING-RECOVERY.md).
