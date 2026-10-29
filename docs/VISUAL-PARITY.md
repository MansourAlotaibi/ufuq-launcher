# RC2 focused visual-parity correction

The authoritative implementation was the owner's read-only MansourTV source reporting version 2.8.5. ClockScene, Home XML, reflection painting, tile sizing, pill surfaces and composition were compared directly. A previously owner-approved actual 1920×1080 capture of installed 2.8.5 is the visual reference; no private photographic asset was copied from it.

## Changes

- Favorites use whole-card pages rather than arbitrary horizontal pixel scrolling. Width, spacing and reserved focus room derive from the available viewport, density and saved Small/Medium/Large setting. RTL reverses navigation, not the saved favorite order. Overflow is indicated by a localized page counter. All favorites remain reachable; no hidden favorite was removed.
- On the tested Nobel at Large, six complete cards fit per page. The dock's top edge is restored to y=756, matching the reference. Up exits the single-row dock from every slot/page. Partial cards are not used as overflow hints.
- Home top controls retain translucent rounded glass instead of opaque settings-row backgrounds. Newly authored geometric gear and nine-square Apps icons more closely match the reference's visual vocabulary. Theme accents still control focus.
- Daily Reflection keeps the original bounds/type sizes/padding and full-text interaction. Its resting gradient/edge are lighter; focus retains the theme outline and underline. No content was removed.
- The static Ufuq headline is restored when focus is outside the dock; a focused app still supplies its title.
- Two landscape photographs were added with file-specific public-domain provenance, original-byte hashes and bundled credits. Existing procedural wallpapers and fixed/random/dimming controls remain available. Theme changes never select a different wallpaper automatically.

## Inputs safety

Inputs is displayed only when the existing runtime checks find a supported TV Input Framework input, a resolvable configured Live TV entry, or an explicitly configured supported adapter. The Nobel API 19 configuration has no generic supported input mapping, so the control stays hidden there. No firmware component, service, permission grant or source-switch broadcast was added to imitate the personal installation. API 21+ Inputs hardware behavior remains untested in this pass.

## Measured remaining differences

Measured from actual Arabic Classic UI hierarchies, in physical pixels:

| Element | Original 2.8.5 | Corrected RC2 |
| --- | --- | --- |
| Greeting | y=51–81, right=1821 | Same |
| Clock | y=81–313, right=1821 | Same; digit width varies with current time |
| Hijri | y=327–380 | Same |
| Gregorian | y=386–420 | Same |
| Reflection | (102,186)–(912,464) | Same bounds; lighter resting glass |
| Static Ufuq title | (1735,438)–(1818,501) | Same |
| Prayer module | (644,555)–(1818,753) | Same |
| Dock card top | y=756 | Same |
| Resting Large card | 247×139 | 230×129, with whole-cell focus clearance |
| Resting label | y=897–930 | y=887–920 |

The corrected dock deliberately has symmetric safe-area alignment, rather than reproducing the reference's asymmetric edge placement. A last overflow page may contain fewer cards, aligned at logical start, with blank unused slots and a page counter. Focus enlargement/glow has room at both physical edges. These are intentional differences, not a claim of pixel-identical reproduction.

The public photographs differ from the unverified personal photograph. Installed favorite icons/order, greeting, football team, time and next-prayer marker reflect independent app preferences and the capture time. Original-source copyright notices and content/provider attribution are preserved.

## Validation and limits

`./gradlew --no-daemon testReleaseUnitTest lintRelease assembleRelease` passes: **271 tests, 0 failures/errors/skips; lint 0 errors, 147 warnings**. Geometry coverage includes multiple widths/densities/sizes and incomplete final pages. API 19/28/34 Robolectric checks remain framework simulation.

Actual Nobel Android 4.4.4 screenshots are owner-only local evidence, excluded from Git. They cover Classic/Midnight/Coast in Arabic and English, dock focus and overflow. Actual Small/Medium/Large cards measure 188×105, 211×118 and 230×129 pixels; Large was restored and recovered across restart. D-pad events are injected ADB input, not physical remote evidence. Other hardware, overscan extremes and physical-remote scan codes remain unverified. The owner-only report records the exact signed APK checksum, captures and final protected-state/log comparisons.

No push, main update, publication, visibility change or GitHub Release is part of this correction. RC2 CI can run only after a separately approved push; the last remote passing CI is not presented as a run of this local commit.
