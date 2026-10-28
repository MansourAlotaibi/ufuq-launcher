# Final security and license audit scope — 2026-10-29

Commands: tools/security_audit.py (all local refs and separately the public ref, plus signed APK),
tools/public_audit.py (public ref, signed APK and accompanying dependency source), and
tools/verify_reflection_content.py (offline corpus evidence). These checks pass at handoff.

Public graph scope: one new parentless root. No inherited import/preparation commit is reachable;
old unresolved raster files and personal branding vector are absent. Existing local refs are preserved.
Image/font binaries are allowlisted; official font/wrapper hashes and packaged wallpaper/license bytes
are checked. Keys, passwords, APKs, diagnostics and release-local/ are excluded from Git.

Pattern checks cover common tokens/private-key headers, absolute user paths, prohibited filenames,
installer commands, protected package identity, permission API bounds and local Markdown links.
No matching private material was found in Ufuq source/history/APK. Public certificate fingerprints
are intentionally recorded. Actual key and password files remain ignored with owner-only permissions.
The signed candidate verifies with that independent certificate; no personal key was accessed.

The complete upstream desugaring source archive has a pinned hash. It includes two public Harmony
TLS test-fixture key literals, explicitly allowlisted by exact upstream filenames in its provenance
manifest. These are unchanged public sample fixtures, not Ufuq signing keys or user credentials;
they are neither in the public Git tree nor in the APK. No unexpected key files were found there.
The complete archive remains intact for accompanying-source compliance.

Licenses and attribution are based on primary source evidence, not disclaimers. Quran/Hadith data
is not relicensed as MIT. TheSportsDB restrictions remain separate from code licensing. All new
wallpapers are original deterministic procedural artwork with no external/model image inputs.
Dependency licenses and content notices are packaged and accessible from Settings.

Limits: pattern scans are not a proof of absence of every novel secret or a CVE certification.
Framework tests are not hardware evidence. No source, APK, key or archive has been uploaded.
