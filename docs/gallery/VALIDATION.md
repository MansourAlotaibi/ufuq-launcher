# Bilingual gallery validation

Prepared 2026-10-30 for local owner review. No push, upload, Release, public visibility change, TV interaction, installation or HOME change was performed.

## Results

- 21 genuine full-frame 1920×1080 losslessly optimized PNG derivatives, approximately 17 MiB total. Six theme/Home images cover Arabic and English. Every Home retains the full dock and recognizable common app icons; private app icons/labels alone are masked.
- Original source image hashes unchanged. RGB pixels outside declared opaque privacy masks match the original exactly. No image is cropped, scaled, rearranged or supplied with replacement names/icons.
- Private greeting values, SSID labels, private app entries and selected sensitive catalog entries are masked. Reading-dialog masks exclude the real modal; canonical Quran/Hadith text/citations are not edited. Signal/security controls remain visible in the redacted Wi-Fi panel. Launcher names are masked and no completed switch is claimed.
- Only whitelisted public source/copyright/edit metadata is embedded. No raw screenshot, diagnostic XML/log, credential, private backup, account data, unique device identifier or absolute local path is part of this gallery.
- Input/output SHA-256 and exact mask rectangles are in [PROVENANCE](PROVENANCE.json). The source originals remain private; neutral capture IDs/hashes permit owner comparison without redistribution.
- Complete Arabic/English READMEs cover the same introduction, themes, features, customization, compatibility, installation, build, privacy/security, licenses and contribution topics. Build commands match exactly and use the launcher Gradle directory.
- Both galleries cover the same 14 feature groups. Shared Arabic-only feature captures are explicitly labeled in the English gallery; no translated or fictional screenshot is substituted. English current-build captures exist for Home/themes, Move, fonts and Settings. Other language-specific capture gaps remain explicit.
- Local browser checks at 595px viewport: Arabic direction rtl, English direction ltr; 14 feature sections on each page; no horizontal overflow and no image aspect-ratio distortion among loaded images. All 21 file dimensions are independently checked. Lazy-loaded image behavior is not falsely reported as every image loaded during the first viewport.
- Source/history/security/asset/license checks are run locally before handoff. This is a documentation/media change; application sources/configuration/signing are unchanged from the accepted code. No new device test or GitHub CI run is claimed. The accepted app baseline 7091b90 already passed 355 tests, lint 0 errors / 149 warnings and release assembly in its recorded CI run.

## Evidence limits

No current English-specific captures exist in this selected final-build set for library, full-reading dialogs, team groups, wallpaper controls, Wi-Fi or launcher picker. Their real Arabic frames are clearly labeled and have equivalent English descriptions. Religious text intentionally remains Arabic in either UI language. Some third-party app names follow the installed app/system locale.

The personalized name is redacted, not replaced with a fake demo name. The common applications shown are actual capture contents, not a curated fictional installation. Masked cards/cells do not indicate missing or broken functionality.

Screenshots alone do not prove persistence, elapsed-time rotation, a live provider response, a completed launcher switch, a physical key press or broad device compatibility. See the application audit for those separately scoped results. Standard Inputs are absent on the tested API 19 platform and are documented rather than drawn into a screenshot.

Owner should review the final pixels, language coverage and copyright/brand context before authorizing any public use. Keep the privacy-edit disclosure and [CREDITS](CREDITS.md) with the images. PNGs and credits are promotional documentation, not a grant to reuse third-party app icons independently.

## ملخص التحقق

أُعدّت 21 صورة فعلية كاملة 1920×1080 ، مع بقاء الشريط والأيقونات العامة والنسب الأصلية. حُجبت البيانات الخاصة دون تركيب أسماء أو واجهة أو تغيير النصوص الدينية. يتطابق كل بكسل خارج الأقنعة مع الأصل، وبقيت الصور الأصلية دون تعديل.

يحتوي المعرضان على 14 مجموعة مزايا متكافئة، مع توضيح لغة كل صورة واللقطات العربية المشتركة في المعرض الإنجليزي. جرى فحص اتجاهي العرض والروابط والأبعاد والبصمات، دون ادعاء اختبار جهاز أو تشغيل CI جديد. لا يوجد تغيير في التطبيق أو التوقيع أو المشغّلات المثبتة أو HOME ، ولا نشر أو رفع إلى GitHub.

The seven trailing-space diagnostics inside the fenced Tanzil copyright block are intentionally retained because that block is copied byte-for-byte from the licensed source. Its complete bytes are verified separately; whitespace checks pass for other content. No global lint/whitespace suppression is added.
