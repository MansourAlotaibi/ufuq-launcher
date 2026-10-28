package io.github.mansouralotaibi.ufuq;
import org.junit.Test;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import static org.junit.Assert.*;
public class DailyContentCorpusTest {
 private List<DailyContent.Entry> corpus() throws Exception {
  return DailyContent.parse(new String(Files.readAllBytes(Paths.get("src/main/assets/daily_reflection.json")), StandardCharsets.UTF_8));
 }
 @Test public void sevenEntriesRetainOfflineQuranAndHadithCoverageAndAttribution() throws Exception {
  List<DailyContent.Entry> all=corpus(); assertEquals(7,all.size()); int q=0,h=0;
  for (DailyContent.Entry e:all) {
   if (e.kind.equals("quran")) {q++; assertTrue(e.notice.contains("CHANGING IT IS NOT ALLOWED"));}
   else {h++; assertTrue(e.licenseUrl.contains("by-sa/4.0"));}
   assertTrue(e.sourceUrl.startsWith("https://")); assertFalse(e.attribution.isEmpty());
   assertTrue(e.details().contains(e.text)); assertTrue(e.details().contains(e.sourceUrl));
  }
  assertEquals(4,q); assertEquals(3,h);
 }
 @Test public void eachModeRotatesOnlyMatchingEntriesIncludingBeforeEpoch() throws Exception {
  List<DailyContent.Entry> all=corpus();
  for (String mode:new String[]{"quran","hadith"}) {
   HashSet<String> texts=new HashSet<>();
   for(long day=-7;day<7;day++) {DailyContent.Entry e=DailyContent.select(all,day,mode); assertEquals(mode,e.kind);texts.add(e.text);}
   assertEquals(mode.equals("quran")?4:3,texts.size());
  }
  HashSet<String> mixed=new HashSet<>();
  for(long day=0;day<7;day++)mixed.add(DailyContent.select(all,day,"both").text);
  assertEquals(7,mixed.size());
 }
}
