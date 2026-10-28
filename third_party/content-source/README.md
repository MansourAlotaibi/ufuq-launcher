# Daily Reflection source evidence

These are public text sources, not private user/device data. They are not all MIT-licensed.

- `tanzil-download.txt`: exact official Uthmani 1.1 plain-text download, with its full copyright
  block intact. CC BY 3.0 and Tanzil verbatim-copy/attribution terms; see ../../THIRD-PARTY-NOTICES.md.
- `tanzil-selected.txt`: four complete selected verse records and the unchanged source copyright block.
- `wikisource-*.json`: parsed Arabic chapter snapshots from the pinned `revid` values, attributed
  to Arabic Wikisource contributors under CC BY-SA 4.0. The original ancient work is Sahih al-Bukhari;
  no assumption that age alone clears modern digital transcription rights is used.
- `provenance.json`: source URLs, revisions, source/text hashes and identified selection changes.

The application packages only seven selected records plus their complete notices and license texts,
not these complete source downloads. Hadith matn excerpts omit chains/commentary without rewriting
the selected wording. See source URLs/history for contributors. Quran verse text is copied verbatim.

`python3 tools/verify_reflection_content.py` checks existing snapshots without network access.
`tools/fetch_reflection_sources.py` retrieves the pinned Hadith revisions when intentionally updating
source evidence. `tools/prepare_reflection_content.py` derives the packaged corpus from the snapshots;
inspect all regenerated changes, hashes, attributions and rendering before committing an update.
Tanzil's upstream copyright year/header can change; do not silently update a recorded hash or text.
