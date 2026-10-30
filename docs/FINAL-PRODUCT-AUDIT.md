# Final product, UX, architecture and release audit

Reviewed baseline: `96932baba086b222a830a0a6c65b81afa1e6ffc0`. Work is isolated on `codex/final-product-audit`. Original MansourTV Ufuq2.8.5 source at `b3eab5931da6742742ce00b6ccead3c6fc82b456` is authoritative and read only. No Home redesign, functioning-core removal, protected installation/history modification or publication is authorized.

## Executive assessment and verdict

**CONDITIONALLY READY** for owner publication review following current-candidate physical verification. No known reproducible release-blocking application defect remains in the tested Nobel scope. Operational publication gates and documented coverage limitations remain. No confirmed unresolved release-blocking defect was identified on the owner-tested96932ba baseline. The final audit sequence had an ordering observation described below; subsequent owner physical full-flow verification did not reproduce it. This is retained as an observation, not claimed code-fixed. Current-candidate device evidence and exact commit/checksum are completed in the owner-only handoff; until a particular runtime check is recorded, source/test existence is not a claim that it passed.

The product has a coherent cinematic composition, three shared-geometry palettes, meaningful offline information, bounded optional football networking and remote-first favorites. The largest remaining limitations are breadth of hardware/accessibility certification, curated religious-content breadth, legacy optional configuration complexity and operational publication/recovery gates. They do not justify a release-time architecture rewrite or reduced public edition.

Owner gates before public publication: verify encrypted/offline signing-key recovery custody and recovery test; establish/test a private vulnerability-reporting route; approve exact source SHA/destination and subsequently public visibility/binary release separately. No push or public action is performed here. Promotional screenshots require a separate privacy-clean capture set; private test evidence must not be published.

## Review method and evidence levels

The independent review covered Android architecture/reliability, Android TV product/design, UX/accessibility/QA and security/licensing/release perspectives. Source findings were assessed for severity, impact, evidence, regression risk, complexity, reversibility and testability before edits. Changes are narrow and reversible. `355` framework/unit tests currently pass on the supported simulated SDK sets; three scanner negative-policy tests also pass. These are supporting evidence, not hardware certification.

Evidence notation used below: **S** source inspection/original comparison; **F** framework or pure regression test; **N** actual Nobel API19 capture/action; **P** owner-reported physical remote. The reviewed96932ba baseline has P evidence for first-selection visible menu closure without Exit, move/place/save/reopen, short OK and Back in Arabic/English. Physical cross-page movement was not separately attested. Current audit-candidate N/P results are distinguished from this baseline rather than silently inherited. The owner subsequently confirmed the final audit binary physical full flow and ordering in both languages; see the dated closing record below.

No protected application is launched to compare screenshots. The earlier approved actual MansourTV reference image may be reused with an explicit historical label; original source hashes are verified read only. Actual before/after images are local ignored artifacts, not synthetic previews.

## Prioritized justified corrections

| Issue / priority | Evidence and user impact | Correction | Risk and validation |
|---|---|---|---|
| Misleading Apps instructions, P2 | Inherited hints promise add/options, while full Apps short OK opens and hold toggles Home; can cause accidental launch | Correct EN/AR hints for current Home membership; selection-mode actions unchanged | Low; real callback/hint tests and safe Downloads native journey |
| Fixed wallpaper forgotten, P2 | Approved build reproduced on TV: Fixed Moraine→Random→Fixed becomes Desert | Remember last fixed ID, including None and valid standalone theme file; stale/missing file falls back safely; clear remembered ID with appearance/theme reset | Low; mode/persistence tests and matched native before/after |
| Focus indistinguishable from selected state, P2 | Same accent fill/stroke used for both |3dp near-white focus ring; saved selection keeps palette accent without ring | Low; actual drawable-state tests and native D-pad screenshot |
| Bright-scene top-control contrast, P2 | Actual snow capture gives only~1.31–1.41:1 for pure white against sampled pill background | Bounded dark palette-surface underlay inside existing pills; same dimensions/borders/accent/focus behavior | Low; compositing test>=4.5:1 for near-white text over worst-case white background and actual matched capture; not global WCAG certification |
| Reflection text over bright snow, P2/P3 | Secondary low-alpha labels and white quote can be weak on bright art | Restrained2dp dark text shadow; canonical text, font sizes, glass gradients/alpha/radius/padding and fitting unchanged | Low; existing verbatim/long-line tests plus actual bright/dark reading checks |
| Scene/readability and reset labels overpromise/obscure scope, P3 | No shipped per-image map; missing external profiles use chosen dimming. Reset does not reset palette/language/fonts/Home info | Explain profile fallback and exact appearance/motion reset scope | Low; wording/source comparison, no new onboarding or removed controls |
| Small palette inconsistency, P3 | Favorites/Wi-Fi decorative strokes remained Classic gold in variants | Same alpha, selected palette RGB; semantic green remains green | Low; Classic RGB unchanged; variant captures |
| Prayer city/device timezone mismatch, P2 | Calculation used UTC+3 but date/marker used device zone | Shared Saudi-city date/minute helper for currently supported five Saudi cities, including midnight; clock/calendars/football stay device-local | Low on Nobel UTC+3 (same result); fixed-Instant zone tests; no global time change |
| Oversized HTTP response treated as partial success, P2 | Chunk loop could overshoot262144bytes and expose truncated HTTP200 body | Check before write, reject oversize, close streams reliably; retain always-deliver cache recovery and HTTPS verification | Low; exact-limit/over-limit tests, existing failure-cache tests |
| Wi-Fi scan focus/identity, P2 | Source and failing API19/28 regression showed scan updates force focus from header and signal-order changes can retarget selection | Initial list focus once; retain selected SSID across refresh without moving header focus | Low; synthetic changing-scan test plus native header/refresh navigation; no network changed |
| Wi-Fi window/receiver ownership, P2 | Main did not retain the Dialog owner; only dismissal unregistered receiver | Retain one panel, idempotently dismiss/unregister on Activity destruction/replacement | Low; lifecycle test, native panel open/close/background/reopen; no blanket dismissal of reading dialogs on pause |
| WEP misconfigured as WPA, P2 | Fresh WEP accepted by policy but password form creates WPA_PSK | Route WEP through existing system-settings fallback, like unsupported enterprise/modern security | Low; policy tests; no actual Wi-Fi connection changes |
| Disabled customization could retain focusability, P3 | Wallpaper selection/Advanced effects flags inconsistent with interval dependency | Explicit dependent focus flags | Low; framework flags and native skip/disabled state |
| Private evidence protection relies only on ignores, P2 | Force-added innocuous private PNG/text could evade secret patterns | Reject private/evidence directory prefixes in history/worktree/APK checks; synthetic force-add/untracked negative tests | Low; scanner exercised in disposable synthetic repos, no actual private data committed |
| CI omits asset/history/package checks, P2 | Existing audit not called | Add clean-root/allowlist checks before build and APK/source-companion checks afterward; no signing secrets | Low; local workflow-equivalent commands/YAML validation |
| Stale publication/key instructions, P2/P3 | Docs still describe empty repo, RC1/no TV evidence and creation of another key | Current exact-ref push plan, historical banners, existing-identity reuse and RC2 policy | Low; relative-link audit and owner review; no remote mutation |

## Complete visible feature/settings disposition

Every group below is retained. “Simplify” means wording or dependency behavior, not feature deletion. No user-facing feature was removed or consolidated into a different navigation model.

| Surface / control | User value and actual behavior | Classification / reliability, compatibility and cost assessment |
|---|---|---|
| First launch | Direct Home with safe defaults; language available in Settings; no original wizard exists | Keep unchanged; F fresh-state inflation. Do not invent/re-run onboarding by clearing owner data |
| Cinematic Home | Large clock, opposite Reflection, integrated rail, low favorites dock | Keep unchanged geometry; S/F/N approved captures; no dashboard/card redesign |
| Show clock | Hides/shows clock independently | Keep; ordinary in-place preference, F/N specific flow evidence |
| System/12h/24h format | Locale-aware time selection | Keep; separate from date/font; F/N labels. Device time accuracy remains external |
| Greeting name | Optional locally saved personal name | Keep; native editor, no network disclosure; owner value restored, no test name published |
| Gregorian toggle | Independent Gregorian visibility | Keep; original date independence, F/N |
| Hijri toggle/adjust−2…+2 | Independent civil Hijri display with manual adjustment | Keep; does not alter Quran text or system date; F/N, local religious calendar differences documented |
| Minimal information mode | Reduces optional Home information without removing features | Keep; useful focus mode. Not a reduced public edition; default/persistence reviewed |
| Reflection modes Both/Quran/Hadith/Off | Offline daily curated entries | Keep;4complete licensed Quran verses/3Hadith excerpts, canonical data hash checked; breadth expansion deferred |
| Reflection full reading/source/credits | Opens complete entry and source/license details | Keep; dynamic preview preserves full source, F/N; external source return and pause behavior retained |
| Prayer enable/city | Offline five-prayer Umm al-Qura calculation for Riyadh/Jeddah/Makkah/Madinah/Dammam | Keep and improve timezone consistency; selected-city UTC+3 scope labeled; global coordinates deferred |
| Prayer rail/progress | Measured stations/labels, next prayer and marker | Keep; horizon/marker unchanged; minute tick no network polling |
| Football enable/team groups | Optional fixtures for selected team | Keep; provider dependency justified by user value; no artwork download or account requirement |
| Football cached/loading/error details | Immediate valid team cache, async refresh, stale-team rejection, retry message/full names/local match time | Keep;1h freshness/1min attempt floor and cache-preserving failure. Provider availability remains external |
| Classic/Midnight/Coast | Same geometry and controls with palettes | Keep; polish inconsistent small accent tokens; wallpaper choice independent |
| Arabic Default+Cairo/Tajawal/IBM Plex Sans Arabic/Readex Pro/Almarai | Six relevant choices, stable family IDs | Keep; cached official static files, shaping/diacritics/Latin coverage and Native baseline checked |
| English Default/Lato | Two relevant choices | Keep; separate preferences; religious/clock/input typography exceptions deliberate |
| System/Arabic/English language | Per-app direction/resources, no global locale setting | Keep; controlled locale recreation remains; inherited other System-language translations incomplete |
| Favorite Small/Medium/Large | User-selected whole-card tile geometry; Large default | Keep; F/N bounds/page tests; no partial-card overflow |
| Favorite short OK | Opens chosen app | Keep; safe Downloads used in tests; no protected launcher opened |
| Long OK/Move/Remove/App info | Clear context menu, carrying feedback, order persistence | Keep; owner P baseline and current injected scope separately recorded; no input architecture rewrite |
| Reorder Settings entry | Alternative to long press for remotes | Keep; intentional discoverability/accessibility alternative, not harmful duplication |
| Favorites cross-page/ordering | Visual-direction moves and whole-card pages | Keep; stable keys/atomic order save; Back places then saves/exits (not undo) |
| Apps/Home catalog tabs | Full app discovery versus favorite selection | Keep; truthful input hints; inherited unused full Apps actions are not silently restored/deleted |
| Fixed/Random wallpaper | Cinematic scene choice versus timed rotation | Keep/improve fixed memory; mode does not change palette; valid custom/None fallback tested |
| Bundled scenes/custom/None/collection default | Two verified public-domain photos plus original MIT artwork, optional app-owned files | Keep; friendly EN/AR names; no unverified inherited imagery |
| Rotation1/5/15/30/60min | User frequency, saved independently | Keep; disabled/nonfocusable in Fixed, enabled immediately in Random; foreground rotation only |
| Dimming0/15/30/45/60/75/90% | Direct readability control | Keep; chosen dimming sets global mode explicitly. Zero dimming cannot guarantee contrast on every custom photo |
| Per-image profiles/global dimming | Optional compatible collection profiles with chosen-dim fallback | Simplify wording; no invented shipped profiles or hidden picture alteration |
| Animations/Advanced effects | Optional motion/advanced visual cost | Keep; advanced disabled without animation, explicit opt-in warning; low-memory mode remains available |
| Reset appearance/motion | Returns layout/motion/wallpaper settings to defaults | Simplify precise wording; preserves palette/language/fonts/favorites/Home info. Not run on owner data |
| Home information Settings | Groups greeting/calendars/reflection/prayer/team controls | Keep; grouping matches function; no major navigation change |
| System TV Settings | Opens a supported system handler | Keep; handler/fallback guarded; no implicit settings writes |
| Other launcher open-once | User selects an alternative HOME Activity for one launch | Keep; no default selection/package disable. Protected launcher execution deliberately excluded from tests |
| Inputs | Standard supported TIF/handler options, optional vendor adapters separate | Keep; unavailable on generic API19, hidden/explained; vendor actions not blindly restored |
| Wi-Fi status/refresh | Native legacy scan/status on19–28, system route on29+ | Keep; user-triggered scan, permission-denial fallback; private SSIDs not published |
| Wi-Fi system settings/remote navigation | Header button reachable from list on tested Nobel | Keep unchanged after actual baseline Up escape passed; speculative focus rewrite rejected |
| Wi-Fi open/WPA-PSK/saved networks | User-initiated connection through Android | Keep; actual network mutation excluded from audit to protect environment; framework/source scope clear |
| WEP/enterprise/modern security | System Wi-Fi configuration instead of incorrect PSK form | Compatibility/security adaptation, no insecure reimplementation |
| Credits/licenses | Native user-visible notices and full source links | Keep; separate code/font/content/provider licenses preserved |
| Hidden legacy columns/rows/dock opacity | External-layout compatibility, not operative on cinematic shelf | Keep hidden when inapplicable; no removal merely to shrink source; legacy structural recreation limitation remains |
| Optional remote helpers/clock screensaver/external module APIs | Inherited opt-in compatibility/customization paths | Keep unchanged, not automatically enabled. Privileged restoration remains disabled; hardware breadth unverified |

## Visual, typography and accessibility assessment

Classic retains the approved RC2 geometry and the original clock hierarchy, Reflection glass gradients/alpha/radius/padding and integrated horizon. The accepted public whole-card dock is preserved: Large tiles remain230×129 on Nobel versus the original247×139, an intentional reviewed pagination/focus-clearance improvement rather than a claim of pixel identity. Only bounded readability/focus differences with concrete evidence are changed. Midnight/Coast use identical layout and semantic controls. New top underlay uses palette surfaces; selected accent and semantic green retain meaning. White3dp focus ring distinguishes remote focus from a saved gold/silver/turquoise choice without relying on color alone.

All Arabic families retain metric fitting and cached typefaces; no Quran/Hadith text is altered for fitting. Long content opens complete reading with attribution. Mixed-script labels/times retain bidi handling. Shadows improve bright-scene edges but are not a claim that all images/dimming/fonts meet every accessibility contrast criterion. Native Holo radio tint and some late-created list-row typography remain OEM-style exceptions. TalkBack/screen-reader, extreme font scale/overscan and motor-accessibility certification remain deferred, not fabricated.

## Architecture, reliability and performance

Preserve the proven focus/order engine, team-scoped cache rejection, foreground lifecycle refresh, sampled wallpaper decoding, background rotation decoding, bounded weak font-view state and active-view guards. Initial wallpaper decode remains synchronous as an intentional no-black-frame tradeoff; measured startup is reported separately, not presumed fast. No WebView/Compose, analytics, telemetry or persistent API polling is introduced.

Large inherited MainActivity and hidden external-config complexity remain maintainability debt. Splitting it, a global-city picker, cache-render coalescing, whole-response time budgets and callback fault-injection/fallback hardening are deferred unless a concrete failure justifies a narrow fix. Native API19 checks do not establish all vendor callback behavior. No emulator binary/AVD is available in the configured SDK; framework simulation is not presented as emulator evidence. Per-read/connection timeouts are8s each, not an asserted total wall-clock deadline.

## Security, licensing, CI and open-source readiness

Public identity/signing remain independent. HTTPS uses platform hostname/trust verification, TLS1.2 adaptation on19 and no redirects. No private key/token/network/name data is introduced. Backup/transfer exclusions and bounded legacy permissions remain. Theme provider is package-derived, permission-gated and whitelisted; Ufuq does not request privileged restoration grants. Screenshots, signing credentials, APKs and evidence stay ignored and owner-only; scanner enforcement now rejects force-added private/evidence paths.

Font/wallpaper/reflection hashes and notices pass. TheSportsDB's [official terms](https://www.thesportsdb.com/docs_terms_of_use.php) and [API documentation](https://www.thesportsdb.com/docs/api) were rechecked: third-party rights and channel restrictions remain, app-store publication requires the stated subscription, and GitHub hosting is not automatically equated with an app store. Preserve the implementation and accurate restrictions; a disclaimer does not grant rights.29recorded runtime coordinates plus desugaring returned no OSV advisory matches in the current scoped query; this does not cover all build/test transitives or prove vulnerability absence. No age-only dependency upgrade was made at the cost of API19.

CI has read-only permissions, unsigned-only artifacts and no signing secrets. It now runs source/history/allowlist, packaged notices/assets and verified companion-source checks, plus negative enforcement tests. GitHub Actions for the new commit cannot run until a separately approved push. Local workflow-equivalent checks are required before handoff. No warning suppression/baseline was added:149warnings include the prior147 plus two obsolete legacy hint resources after correcting Apps labels; source cleanup/remaining translation debt is noncritical and explicit.

Two full task-executed unsigned builds on this machine produced identical SHA256 `cde472d258838f99812b0aa72942b770a03eafc99643cf22438e3f2442922597`. Cross-machine byte identity remains unverified; reproducible instructions/tool versions and provenance are supplied. The complete pinned desugaring source archive must accompany eventual binary distribution with applicable notices and Ufuq source.

## Change control, removals and deferred work

No functioning feature is removed; no controversial removal is performed. WEP is routed to the supported system UI, not silently disabled. Wording is simplified where behavior was misleading. Alternative reorder entries and hidden optional layout support are retained deliberately.

Deferred enhancements: global cities/coordinates; larger licensed content corpus; full accessibility certification; richer nonfavorite Apps actions; large architecture decomposition; broad legacy translation cleanup; dependency verification/immutable Action SHA pins; exotic firmware/overscan/low-memory soak matrix. These are not reasons to reduce the accepted public feature set.

## Release/push control

Protected local main/import tag and reviewed96932ba branch remain unchanged. The clean public ancestry must retain its single audited parentless root, excluding the protected import graph. The exact audited commit/range is supplied at handoff and in [the updated push plan](PUBLIC-PUSH-PLAN.md). Recheck private remote main and normal fast-forward immediately before any separately approved push; never force, mirror, push all refs or push tags. Public visibility and binary Release/APK upload remain separate approvals.

## Final local validation record

The signed audit APK SHA-256 is `17fe36e3e416f1f33627a7d0715846adbe5158d1c5ce426451b1c3b0e893f766`. Its public certificate remains independent and unchanged; signed binaries and device evidence are deliberately excluded from Git. The required `testReleaseUnitTest lintRelease assembleRelease` run passes355tests, zero failures/errors/skips, zero lint errors,149warnings and243informational findings. Three scanner-policy tests pass. Font, religious-content, clean-root/history/image-allowlist, packaged-notice and companion-source checks pass. Configured scans are not a guarantee that arbitrary confidential material or every vulnerability is detectable.

Fresh actual Nobel captures cover Arabic/English and Classic/Midnight/Coast; Cairo/Readex Pro/Almarai and English Lato; bright and dark scenes; Quran/Hadith preview and complete verbatim reading; launcher picker cancellation; Wi-Fi header focus and background/foreground return. All five additional Arabic families were exercised in the preceding approved candidate; this audit does not pretend to repeat every family/scene/content combination. The intermediate1615binary demonstrates fixed-image retention, disabled/random dependencies, selected-versus-focused states and93.7seconds of passive1minute rotation; the final17febinary adds only the subsequently tested Wi-Fi focus correction. Runtime screenshot provenance and any later final checks are recorded in the private handoff. No original installed launcher is opened for fresh comparison.

Ordinary theme/font/settings return captures retain the same logged Home instance across repeated resumes. Language switching remains a deliberate controlled recreation; explicit public-only cold restarts for persistence/performance are labeled separately. Screenshot appearance alone is not lifecycle or functional proof. Remote GitHub main was rechecked at `4faeede5e1c9d7189a3928a2215afab75cd23767`, repository private. New-commit GitHub CI awaits separately approved push; local validation is not a claim of a new hosted Actions pass.

Final17fe native injected-key flow passes in both Arabic and English: long OK/Enter opens menu; one Move selection visibly dismisses it; immediate direction moves the carried card; whole-card cross-page movement, placement/save, cold restart/order restoration, safe Downloads short-OK launch and Back cancellation/exit pass. The temporary Downloads favorite is removed and the original six-item order matches the fresh audit-entry capture. These are injected-device results, not new physical-remote testimony.

Final native football switching shows Real Madrid's Racing Santander fixture and then the restored owner's Hilal–Fateh fixture, with distinct selected-team results and localized times. This confirms the observed live/cache path on this connection; framework fake-client cases cover stale-team rejection/failure retention, not a claim of forced-offline hardware testing.

Focused Nobel startup sampling (three public-only cold launches under the same fixed image/font/dimming) records baseline2639/2829/2595ms and audit2857/2741/2682ms. This overlapping small sample does not establish a speed improvement or statistically proven regression. Audit cold PSS is22196KiB with one Activity/root; baseline39423KiB had three Activities/five roots and is not a controlled memory comparison. No soak/leak claim or invented modern jank/FPS metric is made from legacy gfxinfo.

### Final ordering observation — retained history and physical follow-up

A later restoration check found Play Store/ES File Explorer swapped after a mixed scripted add/remove/reorder/language/team sequence, although the immediate teardown capture matched entry order and each moved Downloads position persisted in the dedicated AR/EN tests. The exact cause is unverified; the code has not been speculatively rewritten. Restore the owner order through the normal public Move UI and verify repeated cold starts and Settings return. A successful restoration is not proof that the longer-sequence observation is resolved. Treat reproduction/diagnosis or independent physical confirmation of that sequence as a release-review hold, not a claim that all persistence testing passed.

The exact owner order has now been restored using the normal Move UI and verified after two separate cold starts plus an ordinary Settings return. This does not diagnose the earlier longer-sequence discrepancy. Because favorites persistence is core functionality, the audit conservatively withholds a public-release verdict until a focused reproduction/trace or independent physical repetition resolves that uncertainty. No speculative reorder rewrite is included.

Final device closure verifies installed signed bytes, unchanged protected package metadata and unchanged observed HOME resolution. The bounded public-process log capture reports zero crashes, ANRs, out-of-memory errors, Activity Pause warnings and configured rendering-error signatures. Owner preferences are restored and the original TV Settings surface is foreground. This does not assert soak-test coverage or erase the unresolved ordering observation.

### Closing owner physical verification — 2026-10-30

After all injected TV input stopped, the owner tested the installed signed17fe audit candidate with the physical remote. The requested procedure covered one-selection Move/menu closure, moving a favorite back to its original position, saving/restarting, all-favorite order in Arabic and English, and the optional Downloads/add/cross-page/remove sequence. The owner answered: **“Full flow and all ordering pass.”** Record this as owner-reported physical evidence, not an automated observation of individual button presses; optional substeps were not individually itemized in the reply.

This independent current-candidate result closes the requested physical-verification hold. The earlier one-off scripted Play/ES swap was not reproduced in the owner report, its cause remains unproven, and no speculative ordering-code change is claimed. Retain it in the regression checklist. The final verdict supersedes the conservative interim hold recorded above: **CONDITIONALLY READY**, with no known reproducible blocker in the tested Nobel scope. No push/publication approval is inferred from test feedback. Broader hardware/accessibility/soak coverage, remaining lint/localization debt and the operational signing-recovery/security-reporting/privacy-clean-publicity gates remain explicit.
