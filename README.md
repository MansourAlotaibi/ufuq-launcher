# Ufuq Launcher

[العربية](README_AR.md)

A native Java Android TV launcher for API 19 and newer, prepared from the protected
MansourTV Ufuq 2.8.5 source import. This is the full-feature development project,
not a deliberately reduced public edition. Version **1.0.0-rc2** is a local candidate;
an independently signed local candidate is prepared, but no release has been published.

Features: cinematic Home, Classic/Midnight/Coast palettes, fixed/random local wallpapers,
prayer times, Hijri calendar, daily Quran/Hadith reflections with full-text details,
favourites, app library, personalized greetings, Wi-Fi, TV inputs, clock/screensaver,
optional remote helpers and user-driven launcher switching.

Appearance is selected in launcher Settings and saved separately from wallpaper choices.
No telemetry, WebView, Compose or background API polling is added. Package
`io.github.mansouralotaibi.ufuq` is independent of the personal MansourTV installation.

[Feature-by-feature comparison](docs/FEATURE-PARITY.md) · [Build](BUILDING.md)
· [Install safely](INSTALLATION.md) · [Compatibility](COMPATIBILITY.md)
· [Privacy](PRIVACY.md) · [Release readiness](docs/RELEASE-READINESS.md)
· [Contributing](CONTRIBUTING.md) · [Security](SECURITY.md)

## Content sources and distribution conditions

Application code derives from CleanYourTV under MIT, with upstream notices retained.
Outfit is SIL OFL 1.1. Content and external services have their own terms, not the application's MIT grant.
[Third-party notices](THIRD-PARTY-NOTICES.md) and [LICENSE](LICENSE) control the distinctions below.

- **Quran:** four complete Arabic verses from official Tanzil Uthmani 1.1. Copyright
  Tanzil Project, CC BY 3.0 and source-specific verbatim-copy conditions. Text is not
  edited; complete source notices and https://tanzil.net links accompany it. No translation is bundled.
- **Hadith:** three Arabic Sahih al-Bukhari matn excerpts from revision-pinned Arabic
  Wikisource chapters, CC BY-SA 4.0. Contributor attribution, source/history links,
  selection changes and share-alike terms are retained. No modern translation or commentary is copied.
- **Football:** working text-only TheSportsDB integration, optional and off until enabled
  and a team selected. Source: https://www.thesportsdb.com/; terms:
  https://www.thesportsdb.com/docs_terms_of_use.php. The documented public test identifier
  `123` is not a private credential. The provider permits official-endpoint data reuse
  subject to conditions and requires a paid subscription for app-store publication.
  GitHub source hosting and GitHub Releases are not named as app stores in those terms;
  that clause is not treated as a blanket ban on this GitHub project, nor as permission
  for every commercial/production distribution. Recheck terms for the intended channel.
  Do not resell the API or embed premium/private keys. No team logos or provider images are downloaded.
  Selected-team fixtures appear from the private local cache immediately. Freshness is one hour; retries have a one-minute floor per team, foreground only.
  The provider currently documents 30 requests/minute for the free tier; availability and data quality vary.
- **Wallpapers:** the full local wallpaper engine is retained. Two verified public-domain landscape photographs and three original MIT procedural landscapes are bundled; see [wallpaper credits](THIRD-PARTY-NOTICES.md#photographic-cinematic-wallpapers-visual-parity-update). No wallpaper collection was
  present in the protected source import; these are new public-project assets.
  Import only images whose rights permit your intended use/distribution. Private wallpaper
  folders and device screenshots are not accessed or bundled. In-app images remain independent of palettes.

The source snapshots, selected text, revision IDs, hashes and license files make the
replacement corpus auditable offline. Run `python3 tools/verify_reflection_content.py`.
A README disclaimer is not permission: unverified artwork must not be bundled or promoted
until its specific license or written permission has been established.

## Platform differences

On API 19–28, the native Wi-Fi panel scans/connects with declared legacy permissions;
API 23–28 requests location permission when the user opens it. Denial falls back to system
settings. Android 10+ restricts ordinary applications' Wi-Fi configuration, so connection
management uses system settings there. No installer changes HOME or grants privileges.
See [compatibility](COMPATIBILITY.md) for untested behavior and [privacy](PRIVACY.md) for data handling.

## Clean public history and validation

The public ancestry begins at the audited parentless commit `0d5faa9`; the initial private push and CI repair are complete. Current release preparation continues on isolated local development/audit branches.
Original local history is preserved separately and excluded from the push plan. Never push --all or --tags.
See [push plan](docs/PUBLIC-PUSH-PLAN.md), [signing identity](docs/PUBLIC-SIGNING-IDENTITY.json),
[lint review](docs/LINT-REVIEW.md) and [validation](docs/VALIDATION.md). Android framework simulation
exercises API 19/28/34. Framework simulation is separate from the documented Nobel API19 captures and owner-reported physical-remote checks; other hardware remains unverified.

The current local RC2 [functional and stability comparison](docs/FUNCTIONAL-PARITY.md) records original-source differences, corrections, actual TV evidence and remaining unverified behavior. It is not a claim of full hardware certification.

Font Style retains the approved default and adds Cairo, Tajawal, IBM Plex Sans Arabic, Readex Pro and Almarai for Arabic UI; English retains Default/Lato. Selections are saved independently. A legacy Amiri selection safely migrates to Default. Clock and religious-text typography remain purpose-specific. Optional fonts are unmodified static SIL OFL 1.1 files; see [font provenance](third_party/ui-font-provenance.json) and [third-party notices](THIRD-PARTY-NOTICES.md).
