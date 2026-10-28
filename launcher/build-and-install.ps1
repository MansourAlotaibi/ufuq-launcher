$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
Write-Host 'Build only. For manual installation see ../INSTALLATION.md.'
.\gradlew.bat --no-daemon testReleaseUnitTest lintRelease assembleRelease
if ($LASTEXITCODE -ne 0) { throw 'Validation failed' }
