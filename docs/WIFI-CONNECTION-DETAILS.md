# Wi-Fi connection details / تفاصيل اتصال Wi-Fi

On Android API 19–28, open Wi-Fi and select **Connection details**, or press OK on the currently connected network. The same panel displays status, network name, signal strength, the access point’s advertised security and the assigned IPv4 address. Back returns to the network list; Back again closes Wi-Fi. No disconnect control or new permission is added. Android 29+ retains the existing system Wi-Fi settings fallback.

في API 19–28 افتح Wi-Fi ثم «تفاصيل الاتصال»، أو اضغط OK على الشبكة المتصلة. تظهر الحالة والاسم وقوة الإشارة والحماية المعلنة وعنوان IPv4. يعيد BACK إلى الشبكات، ثم يغلق اللوحة بالضغطة التالية. لا تضيف الميزة زر قطع الاتصال أو صلاحيات جديدة. تبقى إدارة Wi-Fi على أندرويد 29 فأحدث في إعدادات النظام.

Security comes from scan results matched to the connected access point, not a guessed negotiated cipher. Missing scan data, denied access, unknown fields and unassigned addresses display Unavailable. Signal and security are device-reported estimates. This panel is not a network diagnostic or credential viewer. It never logs, persists or transmits these details and does not display passwords, MAC addresses or device identifiers. No polling is added; existing visible-panel broadcasts refresh the details. No new external asset or dependency is introduced.

## Validation gate

Automated framework tests exercise the details view on API 19 and 28, Arabic/English and all three palettes, scan refresh while reading details, BACK to the same window, disconnect clearing the old IP, permission denial, receiver cleanup, advertised security classification and IPv4 byte order. These simulations do not establish physical remote behavior or OEM compatibility.

Before public publication, test a separately approved same-signed Personal candidate on the Nobel TV: open details from both entry points; compare displayed values privately with OEM settings; navigate/scroll with the physical remote; BACK to the same list; test Arabic/English and all themes; close/reopen and confirm no Home recreation, connectivity change or HOME change. Do not disconnect the TV for testing. Do not publish SSIDs, IPs or screenshots containing network identifiers. Modern Android system fallback remains intentional. Public publication/PR follows actual owner verification; the published RC2 remains untouched.

## Local validation results

Public candidate: 365 unit/framework tests passed, zero failed/skipped; release lint zero errors, 149 warnings and 251 informational findings; release unsigned build passed. Existing security/history/document-policy, font/content provenance and packaged-asset/license checks passed. No new permissions, dependencies or binary assets. API19 Wi-Fi row text direction is now guarded for the API23-only constant. These results are local; no GitHub push, new CI run or physical-device test has occurred for this feature.
