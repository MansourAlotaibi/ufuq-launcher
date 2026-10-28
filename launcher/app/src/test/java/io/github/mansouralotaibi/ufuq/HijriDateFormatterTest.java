package io.github.mansouralotaibi.ufuq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.time.LocalDate;

public class HijriDateFormatterTest {
    @Test public void publishedRiyadhDateMatchesAladhanUaq() {
        // Aladhan calendarMethod=UAQ returns 15-05-1448 for 26 October 2026.
        assertEquals("15 Jumada I 1448 AH",
                HijriDateFormatter.format(LocalDate.of(2026, 10, 26), 0, false));
    }

    @Test public void manualAdjustmentMovesOneDay() {
        assertTrue(HijriDateFormatter.format(LocalDate.of(2026, 10, 26), 1, false).startsWith("16 "));
    }

    @Test public void arabicUsesArabicIndicNumerals() {
        assertTrue(HijriDateFormatter.format(LocalDate.of(2026, 10, 26), 0, true).startsWith("١٥ "));
    }
}
