# Photographic wallpapers and balanced Home

First merged as development source `1.0.0-rc3-dev`; released as `1.0.0-rc3`, versionCode10002. This source update does not modify or replace published RC2, its tag, APK, screenshots or release assets.

## Presentation

Four additional real landscape photographs are bundled offline at 3840×2160: Utah Dunes at Sunset, Peyto Lake, Lofoten Evening and Lake Bled Blue Hour. All five existing images and their saved identifiers remain unchanged. Arabic and English labels are provided. All nine choices remain independent of Classic/Midnight/Coast palette selection, fixed/random mode, interval and dimming.

The cinematic foreground moves down 24dp without changing wallpaper/root/overlay bounds. This includes controls, clock/dates/greeting, reflection, information horizon, app title, dock and optional caption. Short favorites pages share unused cell width equally between their two edges. Full-page widths, card sizes, order, focus identifiers and existing navigation/reorder logic are preserved. No fabricated empty app tiles are added.

## Asset licensing

See [third-party notices](../THIRD-PARTY-NOTICES.md) and [exact provenance](../third_party/wallpaper-provenance.json). The images retain their public-domain, CC BY-SA4.0 or GFDL terms; they are not relicensed as MIT code. The GFDL original source JPEG is included under `third_party/wallpaper-sources`, along with the derivative in application assets and full license/attribution/history notices. CC BY-SA derivative notices identify the center crop and resize. No generative editing was used.

## Validation scope

The owner physically accepted these shared presentation changes in the separate private edition on the tested Nobel Android4.4.4/API19 television. That is evidence for the shared presentation, not a new physical certification of this public package. Public validation consists of its own full unit suite, release lint/build, API19/28/34 framework geometry tests, actual RecyclerView decoration checks for RTL/LTR, fixed-choice persistence, source/asset/license auditing and GitHub CI. RC3 remains a pre-release with public-binary physical validation explicitly outstanding; no universal hardware certification is claimed. Gallery images continue to depict published RC2; private device captures and diagnostics are not included.

No personal OEM adapter, system permissions, signing material, private history, device logs or screenshots are ported. The public package remains `io.github.mansouralotaibi.ufuq`; API19, native Java/Views, D-pad behavior and bounded image decoding remain unchanged.

## Public local verification

381 unit/framework tests passed with zero failures/errors/skips. Release lint has zero errors, 149 warnings and 255 informational findings; existing warnings were not suppressed. Release build, content/font verification and release-policy tests passed. Source/history security and APK asset/license audits are required before push, alongside matching dependency-source provenance. The new APK is an unsigned validation artifact only; no public signing identity is accessed and no new binary release is created.
