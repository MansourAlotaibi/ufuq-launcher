# Changelog

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
