package io.github.mansouralotaibi.ufuq;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class ArabicAppLabelsTest {
    @Test public void detectsArabicLabels() {
        assertTrue(ArabicAppLabels.containsArabic("إعدادات التلفاز"));
        assertFalse(ArabicAppLabels.containsArabic("TV Settings"));
    }
}
