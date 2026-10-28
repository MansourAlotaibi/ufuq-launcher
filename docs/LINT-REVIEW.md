# Release lint review

The accepted baseline reported 200 warnings. Release-critical findings were resolved:

- Removed stale Hebrew layout variants that omitted required Home views.
- Fixed API 21/23 style inheritance dropping required layout width/height; framework inflation tests cover these variants.
- Removed per-draw temporary arrays/rectangles and a per-layout list allocation.
- Increased a 10sp TV label to 12sp.
- Replaced a vector color resource that could fail pre-21 rasterization with a literal color.
- Added explicit backup/data-transfer exclusions for newer Android versions.

No lint baseline or global error suppression was added. Remaining categories were reviewed as follows.
Exact final counts are recorded in VALIDATION.md; this table describes the disposition, not a zero-warning claim.

| Category | Disposition |
|---|---|
| AppBundleLocaleChanges | Locale configuration is declared; public per-app English/Arabic override does not change system locale. |
| ApplySharedPref | Existing synchronous writes used for ordered state transitions; no unbounded I/O introduced. Review performance on low-memory hardware. |
| DataExtractionRules | Fixed: explicit cloud-backup/device-transfer exclusions added alongside allowBackup=false. |
| DiscouragedApi | Guarded resource/vendor compatibility lookups; no new privileged API dependency. |
| HardcodedText | Inherited programmatic labels/provider metadata and fixed scientific notation. Localization polish; no startup failure identified. |
| InlinedApi | Constants are compiled inline; intent handlers are checked or missing settings use defaults. Runtime framework tests exercise API 19. Vendor behavior remains unverified. |
| MergeRootFrame | Potential layout simplification only; not a correctness issue. |
| MissingQuantity | Incomplete inherited plural categories; fallback other forms retained. Translation completeness remains a known limitation. |
| NotifyDataSetChanged | Small TV app/card lists; correctness preserved. No continuous polling. |
| ObsoleteSdkInt | Compatibility checks retained intentionally for the API 19 boundary. |
| Overdraw | Small background layering retained for readability; no large new animation layers. |
| RtlHardcoded | Reviewed physical-coordinate drawing/alignment and explicit RTL branches. Framework locale/focus tests pass; real visual layout remains unverified. |
| SetTextI18n | Composed time/status/provider strings. Locale behavior tested structurally; further language polish needed. |
| Typos | Inherited wording; cosmetic. |
| Untranslatable | Inherited locale entries for intentionally stable identifiers/numerals; fallback behavior retained. |
| UnusedAttribute | Attributes for newer Android versions are intentionally ignored on old versions; guarded/native fallbacks remain. No missing layout dimensions remain. |
| UnusedQuantity | Retained translation compatibility resource. |
| UnusedResources | Retained configurable/vendor/localization resources. Cleanup deferred to avoid reducing optional features. |
| UselessParent | Potential layout simplification only; not a correctness issue. |
| VectorRaster | Fixed: replaced transparent resource reference with a literal transparent color for API 19 raster generation. |
| ViewConstructor | Programmatically created views, not XML-inflated. Actual Home XML inflation passes on API 19/28/34. |

Robolectric tests are framework simulations, not graphical/device proof. RTL visual polish, vendor APIs,
legacy Wi-Fi hardware and performance still require non-production device/emulator evidence.
