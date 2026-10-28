#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
echo "Build only. For manual installation see ../INSTALLATION.md."
exec ./gradlew --no-daemon testReleaseUnitTest lintRelease assembleRelease
