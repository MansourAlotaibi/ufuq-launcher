package io.github.mansouralotaibi.ufuq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class DailyContentPreviewTest {
    @Test public void shortTextRemainsExact() {
        DailyContent.Entry entry = new DailyContent.Entry("نص قصير", "مصدر");
        assertEquals("نص قصير", entry.homePreview(20, "المزيد"));
    }

    @Test public void longTextHasExplicitMoreMarkerWithoutChangingFullText() {
        String full = "هذا نص ديني طويل محفوظ بالكامل ويظهر مختصراً فقط في الواجهة الرئيسية";
        DailyContent.Entry entry = new DailyContent.Entry(full, "مصدر");
        String preview = entry.homePreview(28, "المزيد");
        assertTrue(preview.endsWith("… المزيد"));
        assertEquals(full, entry.text);
    }
}
