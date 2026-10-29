# RC2 functional and UX refinement

Baseline: `099d3c98d133095f70d52e7e62459d006dcec862`, branch `codex/rc2-original-ux`. The owner reported a physical-remote Move activation failure; that report is accepted as a defect. The personal MansourTV 2.8.5 source/installation is protected. Runtime checks target only the independently signed public package.

## Before and after

| Issue | Before / cause | Implemented behavior | Evidence boundary |
| --- | --- | --- | --- |
| Move needs another selection | Menu callbacks ran immediately while the dismissed popup could still own window focus. A fresh OEM gesture carrying long/repeat flags could also be rejected by a flag-only guard | The first row receives focus again after its first real layout (a pre-layout PopupWindow focus request can be lost on KitKat). First selection dismisses immediately; one pending action waits for actual Home window focus, then clears the hold guard and picks up the tile. Event down-time distinguishes opening repeats from a new selection. Pending actions cancel on pause; duplicate releases cannot activate twice. First movement is handled by the reorder state | Framework gesture tests and native injected flows; physical confirmation remains a separate gate. No physical event trace was available to prove which cause occurred on the owner's remote |
| Interval selectable in Fixed mode | No dependency on the mode | Disabled, nonfocusable, muted and guarded against activation in Fixed; enabled in Random. Saved frequency remains intact; only wallpaper rows refresh when mode changes | Native state/activation/rotation evidence is recorded in the owner report |
| Raw asset names | Filename served as UI label | Localized names for all five bundled scenes; unknown external names fall back unchanged. Internal identifiers never change | Native pickers/settings and source mapping |
| Home disappears after Settings | Every settings revision scheduled Activity recreation | Ordinary greeting/date/clock/palette/font/tile-size/wallpaper updates keep the Activity and root. Wallpaper is replaced behind the existing image; palette/font/tiles rebind only when relevant; unchanged information modules are retained. Focus returns by stable tile key/module role/control ID | Local lifecycle create/resume IDs and actual end-to-end flows; no loading overlay |
| Font selection absent | Only the approved fixed typography | Two relevant choices per language, independently saved: approved Default or Amiri for Arabic; approved Default or Lato for English. Font faces cached per process. Clock numerals and Quran/Hadith reading keep approved typography | Static glyph/license audit, framework persistence and actual TV rendering; other hardware remains unverified |
| Alternate font geometry | Real TV testing exposed taller Amiri metrics and a hidden legacy clock enlarging the header | Preserve approved measured line heights while retaining the selected lettering; dynamic text receives the same metric span. Cinematic navigation uses the original 48dp minimum lane, preventing invisible legacy text from moving the dock. No font-size reduction is used as a blanket fix | Failed intermediate captures are retained privately; final geometry assertions/captures distinguish corrected behavior |
| Football lines too close | Football explicitly zeroed line spacing while title/fixture/date used different relative font sizes | Keep existing sizes and horizon geometry. Add 2dp baseline spacing plus 4dp paragraph leading derived from real run metrics after title/fixture. Allow up to five visual lines for wrapped names/status. Full fixture details, local times and team-scoped caches are unchanged | Framework short/mixed-script long names plus native examples; independent provider accuracy is not certified |

## Fonts and redistribution

The approved default is unchanged (including the existing pre-26 platform fallback). Optional, unmodified static files come from official `google/fonts` revision `6157dfaa640ca5738b4d1a7070fe389c3036d7fc`:

- **Amiri Regular**, Arabic UI: copyright 2010–2022 The Amiri Project Authors, SIL OFL 1.1.
- **Lato Regular**, English UI: copyright 2010–2014 tyPoland Łukasz Dziedzic; Reserved Font Name Lato, SIL OFL 1.1.

[Revision-pinned URLs, hashes and complete-notice inventory](../third_party/ui-font-provenance.json). Complete notices are packaged under `assets/licenses/amiri-OFL.txt` and `assets/licenses/lato-OFL.txt`; [third-party notices](../THIRD-PARTY-NOTICES.md) retain the font licenses separately from application licensing. Fonts are not modified, fetched at runtime, sold separately or silently relabeled. `tools/verify_ui_fonts.py` verifies static format, exact hashes/notices, Arabic shaping tables/diacritics and Latin glyph coverage. These checks do not replace device rendering tests.

## Lifecycle exceptions and remaining limits

Language changes still use controlled recreation: the API 19 per-Activity locale-wrapped context and already inflated direction/string resources need replacement. This is distinguished from ordinary settings changes; the old root is not covered with a fake loading screen.

Optional legacy noncinematic column/row/dock-opacity changes preserve their pre-existing recreation behavior because their layout listeners capture structural configuration. This is an implementation limitation, **not** claimed to be an Android platform requirement or a fully resolved in-place-update path. Those controls are hidden for the public cinematic shelf. Festival-boundary rebuilding is also retained. The listed current cinematic settings use the in-place path.

Local `Ufuq.Lifecycle` logs record only Home create/resume instance identifiers, never preference values, network names or credentials. This supports explicit recreation evidence without telemetry or polling. Memory snapshots and bounded crash/ANR logs do not certify leak freedom or indefinite stability.

Physical-remote acceptance must verify: one fresh OK on Move, immediate first-arrow movement, placement/save/restart, short launch and Back in Arabic and English, with multiple pages. Injected ADB events are identified as such. Other Android versions/devices, unusual remote firmware and long-duration memory stress remain unverified unless separately evidenced.

No push, main update, repository visibility change, public release, signing-credential change, protected-app modification or default-HOME selection is part of this work.

## Local release checks

The required `testReleaseUnitTest lintRelease assembleRelease` command passes **323 tests, 0 failures/errors/skips**, release lint **0 errors / 147 warnings** and release assembly. Framework tests include API 19/28/34. Font hashes/licenses/glyph tables, reflection provenance, packaged asset allowlist and scoped public-history/security checks pass. These are supporting checks, not proof of physical-remote behavior or all-device compatibility. No warning suppression or application identity/build-toolchain change was introduced for this work.

## Native API 19 observations

The independently signed public package was updated through the normal Android package installer on the owner-confirmed Nobel/CVTE SMART television, Android 4.4.4/API19. Installed APK bytes were pulled and compared with the signed candidate hash. Captures are actual 1920×1080 device images and remain owner-only/ignored because they contain personal labels. The original MansourTV source was checked against its approved commit; its app/data were not modified.

| Flow | Observed result | Qualification |
| --- | --- | --- |
| English hold Enter → one OK on Move → first Left | Carrying state and first movement visible; crosses the page boundary and persists after public-app restart | Injected keys, not physical remote |
| Arabic hold OK → one OK immediately followed by Left | First movement occurs in carried state, crosses the page boundary; save/restart passes | Injected keys; selection and first arrow sent together without an intervening screenshot |
| Short OK in Arabic/English | Opens the temporary Downloads favorite, without entering Move | Tested only on the safe temporary app; no protected favorite launched |
| Back without a move | Places/exits, leaves saved order unchanged; no accidental app launch | Injected keys; original two-Back save semantics preserved |
| Fixed/Random settings | Disabled/nonfocusable Fixed interval, ignored activation; enabled Random interval retains frequency | Real settings hierarchy and screenshots, pointer/injected input |
| One-minute wallpaper rotation | Background changes during 94.36 seconds without input after applying interval from Settings | Supporting intermediate build with unchanged wallpaper logic; later date-metric defect corrected separately |
| Arabic Amiri / English Lato | Relevant picker options, separate saved choices, language return and public process restart | Native rendering plus framework persistence; purpose typography retained |
| Font clock tick | Independent Gregorian/Hijri and grid bounds identical before/after ~88 seconds without input | Final font implementation on native API19; not a long-duration stress test |
| Ordinary settings | Greeting/date/palette/font/size/wallpaper changes update on return; sampled resume groups keep their Home instance | Bounded lifecycle logs and actual UI observations; no frame-time guarantee |
| Language | Correct translated direction/strings after controlled recreation | A scripted Back sent during Settings recreation had to be repeated after observing the settled screen; seamless locale transition is not claimed |
| Football | Short/long fixtures have separated title/name/date, Arabic/English and shared palette geometry | Live provider data observed; provider accuracy/availability remains external |

The temporary shortcut, owner greeting and original six-favorite order are restored through public UI. The final safety report compares protected package metadata and HOME preference records before/after; no system launcher/default operations were used. Physical remote confirmation, other devices/versions and prolonged memory testing remain acceptance limits.
