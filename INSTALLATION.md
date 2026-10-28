# Safe installation

Use only a separately signed Ufuq build after verifying its SHA-256 and signing certificate.
The public package must be `io.github.mansouralotaibi.ufuq`; reject any other identifier.
Never install an unsigned CI artifact as an official release.

1. Start on a non-production Android TV or emulator, not the protected Nobel TV.
2. Open the signed APK through the device's package installer and explicitly approve installation.
3. Open Ufuq from the device app list. Installation does not prove full compatibility.
4. If desired, choose a HOME app yourself in device Settings. Some vendors prohibit third-party HOME.
5. To stop testing, return to the original launcher through system Settings before removing Ufuq.

Never disable, clear, uninstall or grant privileged permissions to another launcher.
Never enable accessibility through shell commands. Optional remote helpers require explicit
user selection in Android accessibility settings and are not necessary for ordinary D-pad use.
Legacy Wi-Fi entry passes credentials directly to Android; modern devices use system settings.
Do not grant WRITE_SECURE_SETTINGS.

The legacy-named build-and-install scripts now ONLY build and validate. They do not install.
