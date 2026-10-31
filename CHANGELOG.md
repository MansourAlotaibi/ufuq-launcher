# Changelog

## Unreleased — Wi-Fi connection details

- Show connection status, signal, advertised security and assigned IPv4 in the legacy Wi-Fi panel, with Arabic/English and palette-aware focus.
- Open details with OK on the connected network; BACK returns to networks without reconnecting or disconnecting.
- Keep modern Android system settings fallback and show unavailable data honestly. The owner verified both details entry points and BACK with a physical remote in the same-code Personal candidate on the API19 Nobel TV; see [the scoped validation record](docs/WIFI-CONNECTION-DETAILS.md).

## Accepted RC2 audit baseline — 2026-10-30

Approved source 7091b90 is on private GitHub main with passing CI:355 tests, lint 0 errors / 149 warnings and successful release build/security/packaged-license checks. The final signed APK and owner physical-remote verification are recorded in [the release draft](docs/RELEASE-NOTES-1.0.0-RC2.md). Prior sections below are historical iteration records, not the current readiness verdict. Public publication remains separately gated.

Final audit improves fixed-wallpaper memory, focus/selection contrast, bright-scene controls, city-time consistency, bounded HTTP reads and Wi-Fi focus/lifecycle ownership, without removing core features or redesigning Home.


## RC2 functional-parity correction

Restores the measured 2.8.5 reflection preview on KitKat, separates readable full text from the complete accessible copyright notice, fixes selected-team football details/timestamps/full names and cache clock rollback, and restores native focus after pointer-selected navigation. System-language selection restores the process locale; invisible routing helpers use their original plain Activity. See [functional parity matrix](docs/FUNCTIONAL-PARITY.md).

## RC2 visual-parity review

Whole-card paged favorites with RTL/LTR navigation, original Home anchors and glass controls, subtler reflection, and two provenance-verified photographic wallpapers. See [measured differences and validation](docs/VISUAL-PARITY.md).

## 1.0.0-rc2 — local review candidate

- Restore cinematic clock, saved greeting, independent Gregorian/Hijri dates and the original Home hierarchy.
- Preserve three palettes without adding dashboard backgrounds to the native prayer/football rail; apply palette accents to dock/Apps focus and guarded native dialog chrome.
- Expose Large-by-default favorite sizing, readable app labels and direct Apps access.
- Add persistent native wallpaper dimming and rotation-interval settings, independent of palette and wallpaper choice.
- Render validated selected-team fixtures from cache immediately; refresh asynchronously with per-team retry limits and stale-response protection.
- Defer settings-driven Home recreation until lifecycle callback delivery completes on KitKat.
- Derive local signed-candidate filenames from verified public APK identity/version; preserve the existing independent signing identity.

Physical-device results and remaining limits are recorded in the RC2 validation report. This candidate is local; main and publication remain gated on review.

## 1.0.0-rc1 — local preparation, 2026-10-29

- Independent public package and namespace; protected identity overrides rejected.
- Classic, Midnight and Coast palette selection, saved separately from wallpapers.
- Build-only legacy helpers; manual installation and HOME choice.
- Preserved native Wi-Fi on API 19–28 with user permission handling; system settings fallback on newer devices.
- Unsigned release default with optional separate owner-managed public signing.
- Removed inherited raster branding; added lightweight native shapes.
- Restored TheSportsDB text-only fixtures with source attribution, cache and retry floor.
- English/Arabic documentation, privacy notes, compatibility matrix and CI.

Known limitations: no emulator/hardware validation, no public signing key, incomplete inherited asset
provenance and history licensing, residual vendor-specific optional code and lint warnings.
No official release or public compatibility certification is implied.

Daily Reflection is fully enabled with seven independently sourced Quran/Hadith records, per-source licensing, offline integrity checks and complete detail attribution.

Publication preparation adds a clean independent public history, official font provenance, original
icons and three bundled MIT landscapes, independent local signing, packaged dependency licenses/source
material, API 19/28/34 framework tests and fixes for locale-layout/style startup risks.

### RC2 hold-OK and glass follow-up

- Enable the original cinematic Move menu without a private theme; ignore trailing hold repeats/cancelled menu releases and keep persistent reorder feedback.
- Restore exact Classic reflection glass opacity, border and focus underline; palette variants share the original geometry and alpha.
- Preserve whole-card pages, selected-team cache fixes, licensed content and independent signing. Physical remote validation remains a manual gate.

### RC2 final functional and UX refinement

- Defer menu actions until Home regains focus and identify distinct OK gestures.
- Apply current cinematic settings in place; retain documented locale/legacy-geometry recreation exceptions.
- Add independent Arabic/English font choices with static, pinned OFL fonts and protected reading/clock typography.
- Disable rotation frequency in Fixed mode, retain it in Random, and localize bundled scene names.
- Add measured football paragraph leading without redesigning the information horizon.

### RC2 Arabic font and horizon follow-up

- Replace selectable Amiri with five official static Arabic families plus the approved Default; migrate legacy Amiri to Default without changing English or other preferences.
- Reject duplicate tile-menu openings from overlapping long-press paths; retain the focus/dismissal handoff and original move/save semantics. Physical menu-closure acceptance is tracked separately.
- Share a fixed 14dp prayer/football horizon axis and matching palette-aware neutral stroke, retaining the progress marker, spacing and data behavior.
