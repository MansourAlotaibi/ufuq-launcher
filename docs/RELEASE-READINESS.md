# Ufuq Launcher 1.0.0 RC1 — initial-push evidence

Current assessment: [final product/release audit](FINAL-PRODUCT-AUDIT.md). Earlier revision-specific results below are historical evidence.


The accepted full-feature baseline at 32a44a93 is retained. Public preparation adds three original MIT
cinematic landscapes, official pinned OFL font, original geometric icons, explicit licenses/credits,
critical layout/style fixes and additional API 19/28/34 framework tests. Football and offline Quran/
Hadith modes are enabled; provider/content restrictions are preserved. See THIRD-PARTY-NOTICES.md.

## Proposed clean source history

`codex/public-release` is a new parentless public root prepared from the curated source tree. The exact
SHA is reported at handoff. It excludes old unresolved raster/branding assets and original import ancestry.
Local main remains 057253e4b36f51268eb68f0ab9568b6483384cff, the annotated import tag remains unchanged,
and the accepted preparation/work branches remain at 32a44a93. No existing history is rewritten.
Only the new public ref is proposed; never push --all, --mirror or --tags. See PUBLIC-PUSH-PLAN.md.

## Evidence

- Required build/test/lint: PASS, 243 tests, zero failures, zero lint errors/146 reviewed warnings.
- Framework simulation: three palettes, RTL/LTR, injected D-pad focus, bundled wallpapers and persistence;
  Home layouts inflate on API 19/28/34 in English/Arabic/Hebrew.
- Signed local RC verifies v1/v2/v3 with a new independent public certificate; alignment passes.
- Signed checksum: `502d85b8c13468ee4025143523bf89f7107ebc67c012a6ead7d307415741aaff`. See SHA256SUMS-SIGNED.txt and PUBLIC-SIGNING-IDENTITY.json.
- Current asset provenance is resolved through official licensed replacements or original MIT artwork.
  The old unresolved assets remain only in preserved local history outside the proposed public graph.
- Complete desugaring source archive and licenses are prepared alongside release artifacts; distribute
  them together with future binaries. CI also packages companion source with unsigned validation artifacts.
- Final scoped public-history/source/APK/content/license checks are recorded in SECURITY-SCAN.md.

## Explicit limitations and owner actions

No emulator, real-TV visual check or physical remote test was performed. No protected TV, personal
MansourTV project or backup was accessed. Wi-Fi hardware, vendor inputs, low-memory behavior, process
kill/reboot persistence and API 19 network trust require non-production device evidence. Framework
simulation must not be represented as hardware compatibility certification.

Signing is provisioned in encrypted PKCS12 with owner-only local files; the local password file is
plaintext and must remain private. Before public binary distribution, put key/recovery material in
separate encrypted/offline custody and test recovery. No personal backup was created or modified.
Enable/test private security reporting before a public launch. Recheck provider distribution terms
for the intended channel. No unresolved inherited artwork is proposed in the clean public root.

GitHub remains private; source push, public visibility and GitHub Release require their respective
explicit approvals. No remote mutation or artifact publication is performed by this preparation.
