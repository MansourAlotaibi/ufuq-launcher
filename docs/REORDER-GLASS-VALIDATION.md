# RC2 hold-OK and reflection fidelity correction

Authoritative original: MansourTV Ufuq 2.8.5 source at `b3eab5931da6742742ce00b6ccead3c6fc82b456`. Entry public baseline: `c2d9cd033837ca5485314bab3c6079dd693b6bd0`, development branch `codex/rc2-original-ux`. Protected source/history/application/data are not changed.

## Long-press root cause and correction

Original `themes/scenes/config.json` explicitly sets `tile_menu: true`; both original and entry public `res/values/config.xml` default it to false. Public Home had adopted cinematic defaults without adopting this menu default. Consequently its Move/Remove/App info implementation and repeat-aware `TileMenuKey` were gated off unless an external theme enabled them. Testing reordering through Settings did not exercise this broken entry route.

The public resource default now enables the original tile menu, retaining explicit external configuration overrides. Original Activity-level CENTER/ENTER/NUMPAD_ENTER repeat/long-press detection and View long-click handling remain. A short press passes through to the normal application click. This correction does not impose a new hold threshold: the original event path opens on the framework long-press flag or the first repeated key-down; touch long-click uses Android's existing timeout. Unusual remotes that deliver neither remain a physical-test limitation, with Menu and Settings → Reorder as alternatives.

The original immediate reset after opening the popup also leaked a held release to the underlying tile during KitKat window handoff. This inherited edge case was reproduced only in public Ufuq with injected events; it is not a claim that the protected original failed with its physical remote. The guard now survives until release; a fresh repeat-zero DOWN explicitly recovers from an omitted UP, and pause resets the gesture.

Popup rows now require a fresh repeat-zero key-down before a release may select Move. Trailing repeats from the opening hold cannot arm the row, and cancelled releases do not activate actions. Remotes omitting the opening key-up retain the original fresh-DOWN recovery. No indefinite wait or timer is introduced.

Move retains original pick/move/drop and atomic saved-order handling, plus whole-card pagination. Left/right follows screen direction and crosses page boundaries; ends clamp rather than wrap. Up/down moves by the actual visible page capacity (six Large tiles on Nobel), maintaining its lane or clamping to the last tile of an incomplete page, instead of the inherited seven-column grid step. The carried tile stays focused, other tiles dim, and a persistent, localized, non-focusable footer identifies the selected app and key instructions. OK or Back places the tile; a subsequent Back saves and exits. Back does **not** undo moves already made, matching the original behavior. With no move, exit leaves order unchanged. Pausing safely saves the current order as before.

## Original and corrected reflection parameters

| Parameter | Original 2.8.5 | Entry RC2 | Corrected Classic |
| --- | --- | --- | --- |
| Diagonal glass gradient | `0x48142C3A → 0x18131D2A` | `0x20142C3A → 0x08131D2A` | Exact original |
| Fill alpha | 72/255 → 24/255 (28.2% → 9.4%) | 32/255 → 8/255 (12.5% → 3.1%) | Exact original |
| Resting border | `0x1FFBF4E8` (12.2%) | `0x0CFBF4E8` (4.7%) | Exact original |
| Focus border | `0xFFF2B25B`, 3dp | Theme accent, 3dp | Exact original Classic; palette accent in variants |
| Focus underline | `0x99F2B25B`, 2dp | Opaque palette accent, 2dp | Original alpha with palette tint |
| Corner radius | 22dp | 22dp | Unchanged |
| Drawing inset | 1dp resting / 2dp focused | Same | Unchanged |
| Resting stroke | max(1px, 0.65dp) | Same | Unchanged |
| Inner padding | 24/18/24/16dp (L/T/R/B) | Same | Unchanged |
| Kicker/source | 14sp; `0x99FBF4E8` / `0xC7FBF4E8` | Same | Unchanged |
| Quote | Initial 22sp, measured 24/22/19sp; `0xFFFDF8EF`; RTL | Same measured sizes; corrected fitting | Unchanged from reviewed RC2 |
| Source position | Bottom of weighted quote area, right aligned | Same | Unchanged |

Midnight uses `0x48192336 → 0x18101722`; Coast uses `0x48102F3C → 0x180B2130`. Both retain the original fill and border alpha, geometry, typography and padding, with their existing focus accents. No blur/WebView/animation is added. The prior six-dp quote-ink clearance and verbatim-source-safe measured preview remain to prevent the verified KitKat clipping defect. Corpus, citations, complete reading, credits and text sizing are untouched.

## Direct visual/source comparison and classifications

| Area | Comparison and final disposition | Classification |
| --- | --- | --- |
| Clock / greeting / dates | Original source anchors, clock hierarchy and independent date controls retained. Actual digit widths, name, calendar output and time depend on each independent installation. No new geometry change | Intentional improvement: independent saved choices; runtime differences disclosed |
| Reflection surface | Reduced alpha was unintended; restore exact Classic values above; retain current clipping correction and legal full-text data | Regression corrected |
| Reflection bounds / prayer / football | Reference geometry from direct 1920×1080 capture remains shared by all palettes; same original integrated horizon rather than new cards. Selected-team cache correctness retained | Intentional improvement |
| Top controls | Original floating geometry retained; public glyphs/palettes differ. Unsupported private CVTE Inputs adapter is hidden on generic API19 rather than invoking a firmware-specific service | Necessary compatibility adaptation |
| Favorites dock | Original stable order engine and Move menu; restore missing default entry route. Whole cards/pagination retained; Large is 230×129 versus reference 247×139, with ten-pixel higher label placement and symmetric focus clearance | Regression corrected; intentional pagination improvement |
| Reorder feedback | Original carried-tile dimming/glass plus persistent app name/instructions; no disappearing-only toast dependency | Intentional improvement |
| Focus and typography | Original font/padding geometry retained; focused top pills keep a single palette-accent fill (Classic `0xFFF2B25B`) versus original `0xFFF4BE69→0xFFE89A3A`, with the same 21dp radius/resting glass. This existing palette mapping is retained and disclosed, not pixel parity; Classic focus underline restored to original opacity; theme focus colors remain. Physical scan codes require manual verification | Intentional improvement / unverified remote behavior |
| Wallpapers / overlays | Verified public-domain photographic landscapes plus original alternatives replace unverified personal artwork; saved dim and scene/global controls retained. Different photograph/content prevents pixel identity | Licensing-related substitution |
| Home margins | Recorded clock/date/reflection/prayer anchors unchanged; intentional symmetric dock safety margins differ from asymmetric original | Intentional improvement |
| Settings | Original Launcher/Smart Home separation retained, three palettes and explicit dim/interval/sizing controls exposed; unsafe permissions and HOME remain system-mediated | Intentional improvement / compatibility adaptation |
| Long-duration and other devices | Bounded Nobel checks do not certify other firmware, physical remotes or indefinite stability | Unverified behavior |

Earlier anchor measurements and exact remaining geometry differences: [visual comparison](VISUAL-PARITY.md). Complete feature inventory: [functional parity](FUNCTIONAL-PARITY.md). The owner-only evidence report records the final commit/APK, exact native screenshots, injected key sequences, order persistence, build/lint/security results and protected-state checks. Existing owner-approved original TV screenshots are actual device evidence; they are not synthetic or a newly modified personal installation.

## Required manual physical-remote acceptance

This procedure remains **not passed** until a human uses the physical remote; injected ADB events do not satisfy it.

1. Open the separate Ufuq app explicitly, without selecting a new default HOME. Record language and theme.
2. Focus a harmless favorite with the physical D-pad. Short OK must open that app; return to Ufuq explicitly.
3. Hold OK. Confirm the native Move menu appears, release, and verify release alone does not launch the app or select an action. Press a fresh short OK on Move.
4. Confirm the carried tile, dimmed neighbors and persistent instructions. Move left/right within a page and across both page boundaries. Check first/last limits; no partial cards or loss of focus.
5. Press OK to place, then Back to save/exit. Reopen Ufuq and verify the saved order. Repeat for Arabic RTL and English LTR with at least seven favorites.
6. Enter Move again, press Back to place and Back again to exit. With no movement, order must remain unchanged. With movement, Back saves that placement; it is not an undo command.
7. If the remote sends a missing release, repeats, or ENTER rather than CENTER, record the actual observed result. Do not infer it from injected tests. Use Menu or Settings → Reorder as a safe fallback while reporting a failing physical hold.
8. Restore the owner's desired public-app order/language/theme. Never launch or change protected applications or HOME defaults as part of this checklist.

No push, main update, visibility change or public release is authorized by this validation.
