# Arabic fonts, tile-menu ownership and shared horizon

Follow-up to reviewed `ab78f171f8511c3e439263debdb399242860d071` on `codex/rc2-original-ux`. MansourTV 2.8.5 remains the protected functional/visual baseline. This follow-up does not redesign Home, alter public package/signing identity, change data providers/calculations or modify installed protected launchers/HOME.

## Fonts and migration

Arabic offers approved Default plus **Cairo, Tajawal, IBM Plex Sans Arabic, Readex Pro, Almarai**. English retains Default/Lato. The Arabic preference uses stable family IDs under `font_ar_family`; legacy `font_ar=1` meant Amiri and migrates to Default, never Cairo. Only Arabic font keys change; English and unrelated settings are preserved. Unknown IDs fall back safely. Fonts are loaded lazily and cached by file (at most five Arabic faces plus Lato).

All six optional fonts are unmodified official static TTF files with SIL OFL 1.1 notices bundled individually. Cairo is the last official Google Fonts static release before variable conversion; Readex Pro comes from the author's official static directory. Exact upstream repository revisions, URLs, hashes and notices are recorded [per file](../third_party/ui-font-provenance.json). No conversion/subsetting or uncertain mirror was used. `tools/verify_ui_fonts.py` rejects variable fonts and verifies notices, Arabic glyphs/diacritics, GSUB/GPOS shaping tables and Latin coverage. [Third-party attribution](../THIRD-PARTY-NOTICES.md) preserves copyrights and reserved font names.

Amiri is not selectable or packaged in current builds. Its legally verified historical hash remains in the asset inventory as `historicalOnly` so prior accepted commits can be audited without rewriting history. The approved default, clock numerals and Quran/Hadith reading typography remain purpose-specific. UI metric handling retains the preceding API19 tick/clipping correction; prayer labels drawn directly on Canvas use the selected UI face and measured widths.

## Menu defects and correction

The owner's latest physical observation supersedes the narrower earlier acceptance: movement alone did not establish that the menu visibly closed. Both Activity repeat detection and View long-click can call the same tile-menu method. It previously created a new PopupWindow without checking an already visible one; selecting/dismissing the newest can leave the first behind. The duplicate-open framework test exercises that concrete defect. The tile-menu entry now rejects a second visible menu or an action awaiting focus handoff. First selection still dismisses synchronously, waits for actual Home window focus, then enters carrying mode once; repeat releases cannot launch the app.

A separate flag/down-time check could reject a new ordinary press from remotes that reuse down-time. A fresh repeat-zero/non-long DOWN is accepted even then, while opening repeats/cancelled releases stay ignored. Local menu diagnostics record only lifecycle and numeric OK action/repeat/cancellation state, never input text, app names or network data. Physical-event causation is not asserted solely from these source/framework cases: native screenshots/logs and explicit physical confirmation must establish visible closure on the first selection.

Whole-card pages, cross-page movement, normal short OK and original Back behavior remain: Back places a carried tile; another Back saves/exits. Back does not undo moves already made. No installation/default-HOME/system-package action is added.

## Horizon geometry

Prayer and football children already share the same top coordinate. Prayer drew its line at 14dp to clear the 12dp marker; football drew at 1dp (2dp focused), producing the visible mismatch. Both now use the same fixed **14dp** axis, neutral palette tint/alpha and stroke calculation from `HorizonStyle`. Focus changes do not shift that axis. Existing section widths and 24dp inter-section gap remain; progress calculations/marker and football text/cache behavior are untouched. Classic retains the original resting neutral tint; Midnight/Coast use equivalent palette tints. Text remains below the line without a new card.

## Evidence boundaries

Framework tests cover all five cached families, independent preferences and legacy migration, duplicate menu ownership, reused-down-time OK events, shared RTL/LTR horizon layout and existing settings behavior on API19/28/34. Font parsing and compilation are not runtime/physical acceptance. Actual TV screenshots, installed APK checksum, scoped security/license/build results and physical-remote status are recorded in the owner-only evidence report. Other hardware, long-duration stress and any unconfirmed physical flow remain explicitly unverified. No push, release or visibility change is authorized by this follow-up.

## Current local checks and native observations

Required release command passes **332 tests, zero failures/errors/skips**, release lint **zero errors /147 warnings** (unchanged), and release assembly with the existing toolchain. Official static font/license/glyph, reflection provenance, packaged asset/notice and scoped security checks pass. Each Arabic family was displayed and its saved choice verified after a public-only process restart on native API19. Date/grid bounds match the approved default; a passive ~89-second tick observation retains those bounds. English shows only Default/Lato, Lato survives restart and Arabic Almarai returns after language switching. Quran and Hadith full readings contain the unchanged complete bundled entry and retain source/license links.

Actual1920×1080 image analysis finds a pre-correction18.5px prayer/football mismatch and final **0.0px** difference in all six theme/language captures (both axes575.5px on this TV). Sampled palette changes reuse one Home instance per language; controlled locale recreation remains distinct. Fixed/Random interval enablement, ignored disabled touch and retained saved value were checked and the entry Random mode restored. Screenshots/logs remain private and ignored because of personal labels.

The Arabic and English injected first-selection Move captures visibly close the popup, with one PopupWindow in the Arabic before window list and none after either language selection, then carry/move the tile and save across restart. Short OK to the temporary Downloads favorite and Back behavior also pass; the temporary shortcut is removed and the original favorite order restored. This is stronger than observing movement alone, but it is **not physical-remote approval**. The owner subsequently confirmed the newly installed candidate: **“Full flow passes in both languages”**, explicitly answering the test requiring visible closure on the first Move selection without Exit, movement/place/save/reopen persistence, short OK and Back in Arabic/English. This is owner-reported physical evidence, separate from injected testing; the prior narrower response is not reused. Physical cross-page movement was not separately attested. No push/publication is performed.

Owner physical confirmation was received on30October2026 for signed candidate `d609dc10…`. The physical menu-closure gate for the asked flow is now satisfied by that confirmation. Other devices and prolonged stress remain unverified.
