# Ufuq Launcher 1.0.0 RC3

A signed public **pre-release**, package `io.github.mansouralotaibi.ufuq`, versionCode10002, minimum Android4.4/API19. It updates RC2 using the same independent public certificate. This is a GitHub sideload release, not an app-store submission or universal hardware certification.

## What's new

- Four additional real 3840×2160 landscape photographs: Utah Dunes at Sunset, Peyto Lake, Lofoten Evening and Lake Bled Blue Hour. Nine offline wallpaper choices in total, with Arabic/English names; all existing selections, dimming and fixed/random preferences remain available.
- A slightly lower cinematic composition and horizontally centered incomplete favorites pages across Classic, Midnight and Coast, preserving card sizes, saved order and navigation.
- Wi-Fi connection details from the dedicated button or connected network row: status, network name, signal, advertised security and IPv4 when Android permits. Modern Android retains the guarded system-settings fallback.
- Bilingual documentation, scoped dependency maintenance policy and explicit photograph licenses/provenance.

Retained: Arabic RTL/English LTR; three palettes; language-specific fonts; personalized greeting; independent Gregorian/Hijri dates; prayer horizon; Quran/Hadith Daily Reflection with full-entry reading and attribution; optional text-only TheSportsDB fixtures and team-scoped caching; favorites/reordering/pages; app library; Wi-Fi; safe launcher switching. The curated religious corpus is four complete Quran verses and three Hadith excerpts, not a complete scripture library.

## Install or update

Download `ufuq-launcher-1.0.0-rc3.apk` and compare its SHA-256 with `SHA256SUMS.txt`. Use Android's normal package installer. Existing official RC2 installations can update in place with the same signing identity; do not uninstall or clear data to bypass a signature mismatch. Open Ufuq through Apps. Installation does not automatically select HOME or disable any launcher. Unknown-source consent is controlled by Android. No root, system privileges or inherited installation script is needed.

Certificate SHA-256: `3b91e63bb81d85dd4cfb30c63227b46c08ddea300c9c1d8e347bebc942be7bd3`.

Android normally blocks downgrades to RC2/code10001. Preserve your data; a serious regression should be handled with a verified forward-version fix rather than uninstalling or forcing a downgrade.

## Validation and limitations

381 public unit/framework tests pass with zero failures/errors/skips on simulated API19/28/34. Release lint: zero errors, 149 existing warnings and 255 informational findings. Release build, source/history security scans, font/religious-content checks, photograph/source/license inventory and dependency-source companion verification pass. API19 v1/JAR and v2 signatures and ZIP alignment are verified on the signed APK. These checks are not physical-device certification.

RC2's public package was physically tested on the owner's Nobel/CVTE Android4.4.4/API19 television. The shared new layout/wallpaper changes and Wi-Fi flow were physically accepted in the separate private edition on that device. **This newly signed public RC3 APK has not been installed on that TV in this release procedure.** Other Android TV/Google TV hardware, API35+, extreme overscan/density and extended low-memory/reboot testing remain unverified. No private OEM source-control implementation is included; input availability depends on platform/firmware handlers. Wi-Fi information may be unavailable when firmware or permissions restrict it. Football depends on network/provider availability and legacy TLS trust stores. Prayer calculations currently support five Saudi cities using UTC+03:00; Home calendars/clock and football times remain device-local.

## Licenses, source and security

MIT application source preserves CleanYourTV/upstream attribution. Fonts, religious data, photos and dependencies keep their own licenses. The four new photos retain public-domain, CC BY-SA4.0 or GFDL rights and adaptation notices; the GFDL original source and transparent JPEG derivative are included in source. Quran text is verbatim Tanzil with attribution; Hadith excerpts retain Wikisource attribution/history. TheSportsDB is attributed, no provider artwork is fetched, and GitHub distribution is not represented as an app-store permission or an unrestricted production API grant. App-store distribution requires separate provider/subscription and rights review.

Keep the accompanying Ufuq source archive, license bundle and `desugar_jdk_libs-2.1.5-source.zip` with binary redistribution. The gallery contains the 21 previously approved privacy-clean **RC2 captures**, not fabricated RC3 runtime screenshots. See the source READMEs and THIRD-PARTY-NOTICES.md for exact provenance.

Report security issues privately: https://github.com/MansourAlotaibi/ufuq-launcher/security/advisories/new . Read SECURITY.md before reporting; never post credentials or private device logs. GitHub Issues are for non-sensitive bugs and compatibility reports.

The encrypted local public-signing backup and independent local-copy recovery/signing test passed. Independent off-device/offline custody and recovery remain outstanding and intentionally deferred by the owner; full disaster-recovery protection is not claimed. No signing/recovery material is distributed.

## العربية

إصدار مرشح عام موقّع **RC3** يدعم Android4.4 فما فوق، مع أربع خلفيات فوتوغرافية حقيقية بدقة4K، وتوازن رأسي أفضل وتوسيط صف التطبيقات، وتفاصيل اتصال Wi-Fi. تبقى الثيمات الثلاثة والعربية والإنجليزية وجميع وظائف أفق محفوظة.

نزّل APK وتحقق من بصمته في SHA256SUMS.txt ثم ثبّته كتحديث عادي فوق RC2؛ التوقيع العام نفسه والبيانات تبقى محفوظة. لا تحذف التطبيق أو تمسح بياناته لتجاوز اختلاف التوقيع، ولا يلزم تغيير HOME أو منح صلاحيات نظام. أندرويد لا يسمح عادة بالرجوع إلى إصدار أقل؛ معالجة أي خلل تكون بتحديث موثّق برقم أعلى.

نجح381 اختبارًا عامًا والبناء وفحوص الأمان والتراخيص والتوقيع. اختُبرت التعديلات المشتركة فعليًا في النسخة الشخصية على Nobel/API19؛ لم يُثبت APK العام الجديد RC3 على التلفزيون خلال هذا النشر، ولا ندّعي اختبار كل أجهزة Android TV وGoogle TV. صور المعرض توثّق RC2. الترخيص ونسب الصور والنصوص والمصدر المصاحب محفوظة، والنسخة الاحتياطية المستقلة خارج الماك لمفتاح التوقيع ما زالت مسؤولية مؤجلة.
