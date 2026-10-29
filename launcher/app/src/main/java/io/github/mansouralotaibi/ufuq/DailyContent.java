package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/** Small offline corpus. Source text and attribution are retained verbatim in an independently licensed asset. */
final class DailyContent {
    static final class Entry {
        final String text, source, kind, sourceUrl, licenseUrl, attribution, notice;
        Entry(String text, String source) {
            this(text, source, "quran", "", "", "", "");
        }
        Entry(String text, String source, String kind, String sourceUrl, String licenseUrl,
              String attribution, String notice) {
            this.text=text; this.source=source; this.kind=kind; this.sourceUrl=sourceUrl;
            this.licenseUrl=licenseUrl; this.attribution=attribution; this.notice=notice;
        }
        String readingDetails() {
            return text + "\n\n" + source + "\n\n" + attribution + "\n" + sourceUrl + "\n" + licenseUrl;
        }
        String details() { return readingDetails() + "\n\n" + notice; }
        String homePreview(int limit, String more) {
            if (text.length() <= limit) return text;
            int cut = Math.min(limit, text.length());
            while (cut > 0 && !Character.isWhitespace(text.charAt(cut - 1))) cut--;
            if (cut < limit / 2) cut = limit;
            return text.substring(0, cut).trim() + "… " + more;
        }
    }

    private static List<Entry> cached;
    static synchronized Entry today(Context context, String mode) {
        if (cached == null) {
            try (InputStream in = context.getAssets().open("daily_reflection.json")) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096]; int n;
                while ((n = in.read(buffer)) != -1) out.write(buffer, 0, n);
                cached = parse(new String(out.toByteArray(), StandardCharsets.UTF_8));
            } catch (Exception error) {
                throw new IllegalStateException("Bundled Daily Reflection corpus is invalid", error);
            }
        }
        return select(cached, LocalDate.now().toEpochDay(), mode);
    }
    static List<Entry> parse(String json) throws org.json.JSONException {
        JSONArray values = new JSONObject(json).getJSONArray("entries");
        List<Entry> result = new ArrayList<>();
        for (int i = 0; i < values.length(); i++) {
            JSONObject e = values.getJSONObject(i);
            String kind=e.getString("kind");
            if (!("quran".equals(kind) || "hadith".equals(kind))) throw new IllegalArgumentException("Unknown content kind");
            if (e.getString("text").isEmpty() || !e.getString("sourceUrl").startsWith("https://"))
                throw new IllegalArgumentException("Missing text or provenance");
            result.add(new Entry(e.getString("text"), e.getString("source"), kind, e.getString("sourceUrl"),
                    e.getString("licenseUrl"), e.getString("attribution"), e.getString("notice")));
        }
        if (result.isEmpty()) throw new IllegalArgumentException("Empty corpus");
        return Collections.unmodifiableList(result);
    }
    static Entry select(List<Entry> entries, long day, String mode) {
        List<Entry> eligible = new ArrayList<>();
        for (Entry e : entries) if (!("quran".equals(mode) || "hadith".equals(mode)) || mode.equals(e.kind)) eligible.add(e);
        if (eligible.isEmpty()) throw new IllegalArgumentException("No entries for mode");
        return eligible.get((int) Math.floorMod(day, eligible.size()));
    }
    private DailyContent() {}
}
