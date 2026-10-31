# Third-party notices, evidence and usage restrictions

Reviewed 2026-10-29. Code, content and services are licensed separately. Notices are not permission.
No restriction is bypassed by calling a distribution a GitHub release.

## Code and tools

| Component | License / primary evidence | Disposition |
|---|---|---|
| CleanYourTV launcher | MIT, copyright 2026 Gathin; included LICENSE; https://github.com/Gathin23/cleanyourtv/blob/main/LICENSE | Retained with notices |
| Outfit | SIL OFL 1.1, Outfit Project Authors; FONT-LICENSE-OFL.txt | Replaced with the official unmodified Google Fonts binary; pinned commit/hash in third_party/font-provenance.json |
| AndroidX, Kotlin stdlib, JetBrains annotations, Guava listenablefuture | Apache-2.0; artifact license notices and https://source.android.com/docs/setup/about/licenses | Runtime versions recorded in docs/RUNTIME-DEPENDENCIES.txt; Apache license and component notices are packaged |
| desugar_jdk_libs 2.1.5 | GPLv2 with Classpath Exception; https://github.com/google/desugar_jdk_libs | GPLv2/ClassPath license packaged; complete release source archive prepared for accompanying binaries |
| JUnit 4.13.2 | EPL-1.0; https://github.com/junit-team/junit4 | Test only |
| org.json 20240303 | JSON License; https://github.com/stleary/JSON-java | Test only, not bundled as that test dependency in APK |
| Gradle / AGP | Apache-2.0 | Build tools |

## Quran: permitted replacement, full feature retained

The previous anonymous seven-entry transcription was replaced with independently sourced
text covering the same four Quran references and three Hadith subjects. The feature still
rotates offline, supports Quran/Hadith/both/off modes, and opens complete text details.
This is not a full-Quran reader; the 2.8.5 import itself contained seven reflection entries.

Quran text comes directly from the official Tanzil download:
https://tanzil.net/pub/download/index.php?quranType=uthmani&outType=txt-2&agree=true
Version: Uthmani 1.1. Four complete verses: 94:6, 13:28, 2:152, 2:286.
Primary permission: https://tanzil.net/docs/text_license and https://tanzil.net/download/.
License: https://creativecommons.org/licenses/by/3.0/ with the source's express requirement
to distribute text verbatim, identify Tanzil, link to tanzil.net, and retain its complete notice.
The downloaded file currently states Copyright (C) 2007-2026 Tanzil Project; that exact block,
including the update link, is preserved in the source download, selection and every Quran asset record.
The documentation page's older date range is not substituted for the downloaded copyright block.

Verse strings are copied unchanged; no diacritic normalization, translation or rewriting is performed.
The Home view renders an explicitly marked display excerpt when space is limited; the underlying
record and detail view retain the complete unchanged verse. Source and license URLs are accessible in the same detail view.
The Quran data is not relicensed as MIT.

## Hadith: permitted transcription replacement

Original work: Sahih al-Bukhari, Muhammad ibn Ismail al-Bukhari (9th century).
The digital Arabic transcription is attributed to Arabic Wikisource contributors under
the page footer's **CC BY-SA 4.0**, not assumed to be cleared merely because the original is ancient.
Primary reuse policy: https://foundation.wikimedia.org/wiki/Policy:Terms_of_Use/en#7._Licensing_of_Content.
License: https://creativecommons.org/licenses/by-sa/4.0/.

| Subject | Revision-pinned transcription source | Source numbering |
|---|---|---|
| Intentions | https://ar.wikisource.org/w/index.php?oldid=484874 | Book of beginning of revelation, hadith 1 |
| Learning/teaching Quran | https://ar.wikisource.org/w/index.php?oldid=512181 | Quran merits, 4739/4740; conventional 5027 |
| Neighbour, guest and speech | https://ar.wikisource.org/w/index.php?oldid=627517 | Manners, 5672; conventional 6018 |

Contributor attribution is provided through each source URL and its linked contributor history,
as permitted by Wikimedia's reuse policy. The selected matn wording is unchanged. Selection omits
narrator chains and surrounding commentary; that selection is identified as a change and distributed
under CC BY-SA 4.0. The independent application code remains MIT. No new claim of scholarly
certification is made; readers can inspect the source. No translation or modern grading commentary is bundled.

The separately licensed JSON data, raw source snapshots and provenance manifest are under
launcher/app/src/main/assets/daily_reflection.json and third_party/content-source/.
Official CC license texts are packaged in assets/licenses/. The original full snapshots remain
available for audit; their text is subject to the same respective source licenses.
Verify hashes and verbatim selections with `python3 tools/verify_reflection_content.py`.

## Football: TheSportsDB

Primary terms (updated 2026-09-17): https://www.thesportsdb.com/docs_terms_of_use.php.
Documentation: https://www.thesportsdb.com/docs/api. Data source: TheSportsDB / TheDataDB Ltd.
The integration calls an official v1 endpoint with the documented public test identifier `123`,
keeps a one-hour successful cache, and waits at least one minute between request attempts.
It is optional, foreground-driven and does not download artwork. A read-only verification request
returned JSON containing an event; this is endpoint evidence, not Android/API-19 runtime certification.

The terms allow copying/modifying official-endpoint content subject to image/third-party conditions.
They require paid subscription for publishing apps to an app store; neither GitHub source hosting nor
GitHub Releases is explicitly named as an app store. Therefore that clause alone is not a reason to
disable the GitHub development integration. This is a reading of the published scope, not a provider
confirmation or an unrestricted production license. Recheck actual intended distribution before release;
app-store distribution requires a paid subscription and any other applicable permissions.
Do not resell the API. Respect rate limits and third-party rights. Provider data is not MIT-licensed
by this repository. App credits and football detail dialogs identify TheSportsDB and link to its site.

Names/IDs in TeamCatalog are factual metadata, not logo artwork. Team/league trademarks belong to
their owners; no endorsement is implied. API records may contain image URLs but Ufuq does not render
or retrieve them. If artwork is added later, obtain image-specific creator/source/license/version evidence;
a Creative Commons flag or generic disclaimer is insufficient. Keep private premium keys out of source/APKs.

## Wallpapers and branding

Three redistribution-safe 1920×1080 landscapes are bundled: Golden Dunes, Silver Peaks and
Turquoise Horizon. They are original deterministic mathematical artwork generated by
`tools/generate_wallpapers.py`, with no photographic, model-generated or third-party image input.
Source code and PNG outputs are MIT, copyright 2026 Ufuq Launcher contributors. Exact hashes are
in third_party/wallpaper-provenance.json. The independent font is OFL, not MIT.

The original unverified raster branding and personal branding vector are absent from the clean
public branch. The inherited Outfit binary was replaced by an official OFL download pinned to
Google Fonts commit 8b0a1d0f5983c89bc2b93f1b5fb55f9e252744b5. Menu icons are new geometric
Ufuq artwork under MIT. Other XML shapes/native rendering code remain covered by the upstream MIT
source notice. No unresolved external bitmap source is included. The binary asset allowlist and hashes
are in third_party/public-assets.json; Gradle wrapper provenance is independently checksum-verified.

The public branch is a new parentless snapshot and does not include the private import ancestry,
old raster art or older anonymous religious transcription. Original local branches and the import
tag remain untouched and must not be pushed with --all or --tags. Source attribution is preserved
in LICENSE/NOTICE.md; starting a new Git graph does not erase upstream copyright.

## Packaged dependency licenses and accompanying source

The APK contains Apache-2.0, OFL, GPLv2 with Classpath Exception, CC BY 3.0 and CC BY-SA 4.0
license texts plus these notices in assets/licenses/ and assets/third_party_notices.txt.
Settings exposes an offline Licenses and data sources dialog. Runtime dependencies are pinned;
Robolectric 4.12.2 and its transitive libraries are test-only and not in the release APK.

For desugar_jdk_libs 2.1.5, official release-source preparation commit
73170c345e6a762fc6a1f0301bb15218850023ef and the complete source archive are recorded in
third_party/desugar-source-provenance.json. A verified 30 MB archive is prepared under ignored
release-local/dependency-sources/. Distribute it from the same release location alongside signed
binaries, together with Ufuq source and build instructions; do not substitute a generic disclaimer
or a vague upstream link for the accompanying-source package. No source archive is published now.
The archive preserves source notices and upstream build material; Ufuq does not modify that library.

The Gradle wrapper matches Gradle's official 8.9 wrapper checksum and is Apache-2.0; see
third_party/gradle-wrapper-provenance.json. No signing key or password is part of the public snapshot.


### Photographic cinematic wallpapers (visual-parity update)

- **Desert sunset scenic** — Steve Hillebrand, U.S. Fish and Wildlife Service.
  Source: https://commons.wikimedia.org/wiki/File:Desert_sunset_scenic.jpg
  The file description identifies an official U.S. federal employee work, public domain;
  permission expressly allows commercial use. See https://www.fws.gov/disclaimer .
- **Moraine Lake 17092005** — Gorgo, via Wikimedia Commons.
  Source: https://commons.wikimedia.org/wiki/File:Moraine_Lake_17092005.jpg
  Author dedicates the photograph to the public domain worldwide, with an unrestricted
  any-purpose permission fallback where dedication is not legally possible.

Both original JPEG files are included without pixel edits. The launcher crops them to the
screen and applies the user's dimming preference at runtime. Author credit is preserved here
and in the bundled in-app notices; no endorsement is implied. These photographs are not
claimed as MIT-authored Ufuq artwork. Original dimensions, source URLs, verification date
and exact SHA-256 hashes are in `third_party/wallpaper-provenance.json`.


Optional UI fonts (unmodified static files, SIL OFL 1.1): Cairo, copyright 2009 The Cairo Project Authors; Tajawal, copyright 2018 Boutros International; IBM Plex Sans Arabic, copyright 2017 IBM Corp., Reserved Font Name Plex; Readex Pro, copyright 2018 The Readex Pro Project Authors, Reserved Font Name RevReading Lexend; Almarai, copyright 2019 The Almarai Project Authors; Lato, copyright 2010–2014 tyPoland Łukasz Dziedzic, Reserved Font Name Lato. Complete individual copyright/license notices are bundled under assets/licenses/*-OFL.txt. Revision-pinned official sources, unchanged file hashes and per-file revisions are in third_party/ui-font-provenance.json. Cairo uses the last official static release before variable-font conversion for API19; Readex Pro is the author's official static TTF. No font file has been converted, subsetted or renamed internally. These licenses remain separate from application licensing. Clock/religious reading typography stays purpose-specific. Amiri is absent from current builds; its previously verified historical asset remains inventoried for clean-history auditing.

### Additional 4K photographs (development after RC2)

These four photographs are not AI-generated artwork. Each bundled derivative is a centered 16:9 crop downsampled to 3840×2160 JPEG. No generative fill or image-content replacement was used. The author, original source, original/downloaded hash, packaged hash and changes are recorded in third_party/wallpaper-provenance.json. Existing RC2 images are unchanged.

- **Utah Dunes at Sunset** — Bob Wick / Bureau of Land Management - Utah. License: Public domain (U.S. federal government official work). Source: https://commons.wikimedia.org/wiki/File:Utah_Dunes_Landscape_-_West_Desert_District.jpg

- **Peyto Lake** — Douglaspperkins. License: CC-BY-SA-4.0. Source: https://commons.wikimedia.org/wiki/File:Peyto_Lake_2025.jpg

- **Lofoten Evening** — Simo Räsänen (Ximonic). License: GFDL-1.2-or-later. Source: https://commons.wikimedia.org/wiki/File:Gims%C3%B8ystraumen%2C_2009_09.jpg

- **Lake Bled Blue Hour** — Jakub T. Jankiewicz. License: CC-BY-SA-4.0. Source: https://commons.wikimedia.org/wiki/File:Bled_Church_island_in_Blue_hour.jpg


Peyto Lake and Lake Bled Blue Hour, including the cropped derivatives, remain CC BY-SA 4.0: preserve attribution, link the license, identify changes, and distribute adaptations of these images under that license. See assets/licenses/CC-BY-SA-4.0.txt. They are separate works in the application collection, not MIT artwork.


**Lofoten Evening — Ufuq 4K edition.** Original title: Gimsøystraumen, 2009 09; copyright Simo Räsänen (Ximonic), 2010. Cropping/resizing adaptation copyright Ufuq Launcher contributors, 2026. Publisher of the adaptation: Ufuq Launcher contributors. Permission is granted to copy, distribute and/or modify this image under the terms of the GNU Free Documentation License, Version 1.2 or any later version published by the Free Software Foundation; with no Invariant Sections, no Front-Cover Texts, and no Back-Cover Texts. A copy of the license is bundled in assets/licenses/GFDL-1.3.txt, with the image-specific notice in assets/licenses/lofoten-evening-NOTICE.txt. The editable JPEG derivative is in assets/wallpapers/lofoten-evening.jpg and the unchanged original source is in third_party/wallpaper-sources/lofoten-evening-original.jpg in the source repository.


**History:** 2010 — original Gimsøystraumen, 2009 09 by Simo Räsänen; original source https://commons.wikimedia.org/wiki/File:Gims%C3%B8ystraumen,_2009_09.jpg . 2026 — Lofoten Evening — Ufuq 4K edition, by Ufuq Launcher contributors; only center-cropping and downsampling. Image source and revision history: https://github.com/MansourAlotaibi/ufuq-launcher . No endorsement by photographers or agencies is implied. These asset licenses do not change the application source license. The source and derivative JPEG files are transparent image copies under the GFDL definition.
