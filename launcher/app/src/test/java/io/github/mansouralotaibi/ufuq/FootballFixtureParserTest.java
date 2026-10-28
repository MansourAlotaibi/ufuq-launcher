package io.github.mansouralotaibi.ufuq;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class FootballFixtureParserTest {
    private static final ZoneId RIYADH = ZoneId.of("Asia/Riyadh");
    private static final ZonedDateTime NOW = ZonedDateTime.parse("2026-10-27T12:00:00+03:00[Asia/Riyadh]");

    @Test public void acceptsAwayFixtureAndConvertsUtcToTvTime() {
        FootballFixtureParser.Result result = FootballFixtureParser.parse(event("141174", "136013",
                "Al-Shamal", "Al-Hilal", "2026-10-27T18:15:00"), "136013", RIYADH, NOW);
        assertEquals(FootballFixtureParser.Status.OK, result.status);
        assertEquals(21 * 60 + 15, result.fixture.minute);
        assertEquals("2026-10-27", result.fixture.date);
        assertEquals("", result.fixture.score);
    }

    @Test public void rejectsEventForAnotherTeam() {
        FootballFixtureParser.Result result = FootballFixtureParser.parse(event("1", "2",
                "A", "B", "2026-10-29T18:00:00"), "136013", RIYADH, NOW);
        assertEquals(FootballFixtureParser.Status.TEAM_MISMATCH, result.status);
    }

    @Test public void distinguishesEmptyAndPastOnly() {
        assertEquals(FootballFixtureParser.Status.EMPTY,
                FootballFixtureParser.parse("{\"events\":null}", "136013", RIYADH, NOW).status);
        assertEquals(FootballFixtureParser.Status.PAST_ONLY,
                FootballFixtureParser.parse(event("136013", "2", "A", "B",
                        "2026-10-27T07:00:00"), "136013", RIYADH, NOW).status);
    }

    private static String event(String homeId, String awayId, String home, String away, String timestamp) {
        return "{\"events\":[{\"idHomeTeam\":\"" + homeId + "\",\"idAwayTeam\":\""
                + awayId + "\",\"strHomeTeam\":\"" + home + "\",\"strAwayTeam\":\""
                + away + "\",\"strTimestamp\":\"" + timestamp + "\"}]}";
    }
}
