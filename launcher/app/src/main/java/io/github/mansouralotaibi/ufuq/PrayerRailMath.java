package io.github.mansouralotaibi.ufuq;

/** Pure geometry helpers keep the five-prayer rail proportional and testable off-device. */
final class PrayerRailMath {
    static float fraction(int minute, int fajr, int isha) {
        if (isha <= fajr) return 0f;
        return Math.max(0f, Math.min(1f, (minute - fajr) / (float) (isha - fajr)));
    }

    static int next(int now, int[] prayers) {
        for (int i = 0; i < prayers.length; i++) if (prayers[i] > now) return i;
        return 0;
    }

    private PrayerRailMath() {}
}
