package io.github.mansouralotaibi.ufuq;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PrayerRailMathTest {
    @Test public void stationsUseRealElapsedTime() {
        assertEquals(0f, PrayerRailMath.fraction(300, 300, 1100), 0.0001f);
        assertEquals(.25f, PrayerRailMath.fraction(500, 300, 1100), 0.0001f);
        assertEquals(1f, PrayerRailMath.fraction(1100, 300, 1100), 0.0001f);
    }

    @Test public void overnightClampsAndWrapsToFajr() {
        int[] prayers = {300, 700, 900, 1050, 1100};
        assertEquals(0f, PrayerRailMath.fraction(120, 300, 1100), 0.0001f);
        assertEquals(1f, PrayerRailMath.fraction(1300, 300, 1100), 0.0001f);
        assertEquals(0, PrayerRailMath.next(1300, prayers));
        assertEquals(3, PrayerRailMath.next(1000, prayers));
    }
}
