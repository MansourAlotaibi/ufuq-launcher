# Initial private GitHub push plan — not executed

Local source ref: refs/heads/codex/public-release. Resolve its exact reviewed SHA using
`git rev-parse refs/heads/codex/public-release`; it is reported at handoff. This ref has one
parentless commit and no ancestry shared with the protected import graph.

Proposed destination: refs/heads/main in the existing private GitHub repository. At inspection the
remote is empty. Before approval/execution, recheck remote refs and private visibility. Do not
force-push if remote history has appeared; inspect it and present a revised plan.

Only after explicit initial-push approval, the intended refspec is:

```sh
git push origin refs/heads/codex/public-release:refs/heads/main
```

Do not push --all, --mirror, --tags, protected local main, the import tag, or preparation/work branches.
No release tag, APK, key or private directory is proposed for this source push. Original local refs
remain unchanged: main at 057253e; source-import-2.8.5 at that import; accepted preparation at 32a44a93.
The public root keeps upstream copyright attribution without transferring unresolved old asset blobs.

This is approval for an eventual private source push only. Public repository visibility and a GitHub
Release require separate authorization. Any future binary release must include the verified dependency
source archive and applicable notices. Local encrypted-key recovery custody remains an owner action.
