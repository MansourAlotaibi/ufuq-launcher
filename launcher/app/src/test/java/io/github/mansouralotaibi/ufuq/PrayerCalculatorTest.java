package io.github.mansouralotaibi.ufuq;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.time.LocalDate;
public class PrayerCalculatorTest {
    private static void near(int a,int e){assertTrue("actual="+a+" expected="+e,Math.abs(a-e)<=2);}
    @Test public void riyadhMatchesPublishedUmmAlQuraSample(){PrayerCalculator.Times t=PrayerCalculator.calculate(LocalDate.of(2026,10,26),24.7136,46.6753,3);near(t.fajr,278);near(t.dhuhr,697);near(t.asr,893);near(t.maghrib,1037);near(t.isha,1127);}
}
