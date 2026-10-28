package io.github.mansouralotaibi.ufuq;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
public class NumeralStyleTest {
    @Test public void shapesBothWays() {
        assertEquals("١٢:٣٤", NumeralStyle.apply("12:34", NumeralStyle.ARABIC_INDIC));
        assertEquals("12:34", NumeralStyle.apply("۱۲:۳۴", NumeralStyle.LATIN));
    }
    @Test public void systemLeavesTextUntouched() {
        assertEquals("Sun 12", NumeralStyle.apply("Sun 12", NumeralStyle.SYSTEM));
    }
}
