package io.github.mansouralotaibi.ufuq;
import org.junit.Test;
import static org.junit.Assert.*;
public class ThemePaletteTest {
 @Test public void unknownOrMissingSelectionFallsBackSafely() {
  assertEquals(0, ThemePalette.index(null)); assertEquals(0, ThemePalette.index("future"));
 }
 @Test public void persistedIdsResolveToDistinctPalettes() {
  for (int i=0;i<3;i++) assertEquals(i, ThemePalette.index(ThemePalette.IDS[i]));
  assertNotEquals(ThemePalette.ACCENTS[0],ThemePalette.ACCENTS[1]);
  assertNotEquals(ThemePalette.ACCENTS[1],ThemePalette.ACCENTS[2]);
 }
 @Test public void reflectionRestoresOriginalGlassOpacityWithEquivalentThemeTints() {
  assertEquals(0x48142C3A,ThemePalette.REFLECTION_TOP[0]);
  assertEquals(0x18131D2A,ThemePalette.REFLECTION_BOTTOM[0]);
  assertEquals(0x1FFBF4E8,ThemePalette.REFLECTION_BORDER);
  for(int i=0;i<3;i++) {
   assertEquals(0x48,ThemePalette.REFLECTION_TOP[i] >>> 24);
   assertEquals(0x18,ThemePalette.REFLECTION_BOTTOM[i] >>> 24);
  }
 }
}
