package io.github.mansouralotaibi.ufuq;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.OffsetDateTime;

/** All currently selectable prayer cities use Saudi civil time, without DST. */
final class PrayerCityTime {
    private static final ZoneOffset ZONE = ZoneOffset.ofHours(3);
    static LocalDate date(Instant now) { return now.atOffset(ZONE).toLocalDate(); }
    static int minute(Instant now) {
        OffsetDateTime city=now.atOffset(ZONE); return city.getHour()*60+city.getMinute();
    }
    private PrayerCityTime() { }
}
