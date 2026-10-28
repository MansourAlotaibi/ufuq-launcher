package io.github.mansouralotaibi.ufuq;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/** Validates team ownership and converts TheSportsDB UTC timestamps to the television time zone. */
final class FootballFixtureParser {
    enum Status { OK, EMPTY, PAST_ONLY, TEAM_MISMATCH, MALFORMED }

    static final class Fixture {
        final String home, away, date, score;
        final int minute;
        Fixture(String home, String away, String date, int minute, String score) {
            this.home = home; this.away = away; this.date = date; this.minute = minute; this.score = score;
        }
    }

    static final class Result {
        final Status status;
        final Fixture fixture;
        Result(Status status, Fixture fixture) { this.status = status; this.fixture = fixture; }
    }

    static Result parse(String raw, String teamId, ZoneId zone, ZonedDateTime now) {
        try {
            JSONArray events = new JSONObject(raw).optJSONArray("events");
            if (events == null || events.length() == 0) return new Result(Status.EMPTY, null);
            boolean matched = false, past = false;
            for (int i = 0; i < events.length(); i++) {
                JSONObject event = events.optJSONObject(i);
                if (event == null) continue;
                if (!teamId.equals(event.optString("idHomeTeam"))
                        && !teamId.equals(event.optString("idAwayTeam"))) continue;
                matched = true;
                ZonedDateTime local = localTime(event, zone);
                if (local == null) continue;
                if (local.isBefore(now.minusMinutes(5))) { past = true; continue; }
                String home = event.optString("strHomeTeam").trim();
                String away = event.optString("strAwayTeam").trim();
                if (home.length() == 0 || away.length() == 0) continue;
                String homeScore = value(event, "intHomeScore");
                String awayScore = value(event, "intAwayScore");
                String score = homeScore.length() > 0 && awayScore.length() > 0
                        ? homeScore + "–" + awayScore : "";
                return new Result(Status.OK, new Fixture(home, away, local.toLocalDate().toString(),
                        local.getHour() * 60 + local.getMinute(), score));
            }
            if (past) return new Result(Status.PAST_ONLY, null);
            return new Result(matched ? Status.MALFORMED : Status.TEAM_MISMATCH, null);
        } catch (Exception ignored) { return new Result(Status.MALFORMED, null); }
    }

    private static ZonedDateTime localTime(JSONObject event, ZoneId zone) {
        String timestamp = event.optString("strTimestamp", "").trim();
        try {
            if (timestamp.length() > 0)
                return LocalDateTime.parse(timestamp).atZone(ZoneOffset.UTC).withZoneSameInstant(zone);
            String date = event.optString("dateEvent", "").trim();
            String time = event.optString("strTime", "").trim();
            if (time.length() >= 5) time = time.substring(0, 5) + ":00";
            return LocalDateTime.parse(date + "T" + time).atZone(ZoneOffset.UTC).withZoneSameInstant(zone);
        } catch (Exception ignored) { return null; }
    }

    private static String value(JSONObject event, String key) {
        if (event.isNull(key)) return "";
        String value = event.optString(key, "").trim();
        return "null".equalsIgnoreCase(value) ? "" : value;
    }

    private FootballFixtureParser() {}
}
