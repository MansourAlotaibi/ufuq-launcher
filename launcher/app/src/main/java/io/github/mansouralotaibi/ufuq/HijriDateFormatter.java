package io.github.mansouralotaibi.ufuq;

import java.time.LocalDate;
import java.time.chrono.HijrahDate;
import java.time.temporal.ChronoField;

/** Offline Hijrah chronology. The desugared JDK data uses the Umm al-Qura calendar table. */
final class HijriDateFormatter {
    private static final String[] AR = {"محرم", "صفر", "ربيع الأول", "ربيع الآخر", "جمادى الأولى",
            "جمادى الآخرة", "رجب", "شعبان", "رمضان", "شوال", "ذو القعدة", "ذو الحجة"};
    private static final String[] EN = {"Muharram", "Safar", "Rabi I", "Rabi II", "Jumada I",
            "Jumada II", "Rajab", "Sha'ban", "Ramadan", "Shawwal", "Dhu al-Qidah", "Dhu al-Hijjah"};

    static String format(LocalDate date, int adjustment, boolean arabic) {
        HijrahDate h = HijrahDate.from(date.plusDays(adjustment));
        int day = h.get(ChronoField.DAY_OF_MONTH);
        int month = h.get(ChronoField.MONTH_OF_YEAR);
        int year = h.get(ChronoField.YEAR_OF_ERA);
        String value = day + " " + (arabic ? AR[month - 1] : EN[month - 1]) + " " + year;
        return arabic ? NumeralStyle.apply(value, NumeralStyle.ARABIC_INDIC) : value + " AH";
    }

    private HijriDateFormatter() {}
}
