# Next reviewed source push — approval required

The initial private push is complete. The remote is not empty: `origin/main` was last verified at `4faeede5e1c9d7189a3928a2215afab75cd23767`. The clean public root is the parentless `0d5faa9df40f968d7270c048fcee66f9a603922c`; subsequent reviewed RC2/audit commits descend from it, not from the protected import graph.

The current candidate is prepared on isolated local `codex/final-product-audit`. Its exact final SHA, audit evidence and proposed ancestry are recorded in [the final audit](FINAL-PRODUCT-AUDIT.md) and the owner handoff. The reviewed `codex/rc2-original-ux` reference remains at `96932baba086b222a830a0a6c65b81afa1e6ffc0` until separately reviewed integration.

Before an owner-approved push:

1. Recheck repository identity/private visibility and resolve remote main without mutating it.
2. Compare exact reviewed SHA/parents and list every proposed commit from remote main through the candidate. Confirm a normal fast-forward and the single audited public root.
3. Re-run scoped security/history/asset/font/content/APK checks; confirm no private evidence, signing material or original import ancestry is reachable.
4. Obtain explicit approval for that exact source SHA and destination. No approval is inferred from local development/testing.
5. Only then use a normal non-force exact refspec, such as `git push origin <approved-full-SHA>:refs/heads/main`, and monitor CI. If remote main changed or diverged, stop and present a revised plan; do not force or rewrite.

Never push `--all`, `--mirror`, `--tags`, protected local main, the import tag or unrelated development refs. No release tag, signed APK, private screenshot or secret is proposed by this source-only plan. Private visibility must remain unchanged.

Public visibility, GitHub Release/binary upload and source pushing are separately controlled actions requiring their own authorization. Before eventual binary distribution, confirm secure offline signing-key recovery custody, a working private security-reporting route, verified accompanying dependency source/notices/checksums, and provider terms for the actual channel. GitHub distribution is not automatically equated with an app store.
