# Agent instructions

This repository contains the independent Ufuq Launcher project. It is not the CleanYourTV maintenance repository and must not be used to debloat or modify a television.

- Work only inside this repository unless the user explicitly names another target.
- Never install, uninstall, disable, clear, or modify MansourTV, CleanYourTV, an OEM launcher, or a default HOME selection.
- Preserve API 19 compatibility, native Java/Android Views, D-pad navigation, RTL/LTR support, and low memory use.
- Do not add WebView UI, Compose, analytics, telemetry, persistent polling, insecure networking, or embedded private credentials.
- Do not commit APKs, signing keys, ADB keys, device dumps, private screenshots, Wi-Fi names, IP addresses, account data, or absolute user paths.
- Treat `main` and the `source-import-2.8.5` tag as protected baselines. Use a development branch and do not rewrite history.
- Run unit tests, release lint, and the release build for source changes. Physical-TV claims require real device evidence; injected keys are not proof of a physical remote press.
- A public build must use an application ID and externally visible actions/authorities that cannot overwrite or interfere with the protected MansourTV package.

