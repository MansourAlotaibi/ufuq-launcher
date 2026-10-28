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
}
