package io.github.mansouralotaibi.ufuq;
import org.junit.Test;
import static org.junit.Assert.*;
public class ShelfGeometryTest {
 @Test public void nobelLargeMatchesSixWholeCards() {
  assertEquals(6, ShelfGeometry.slots(1596, 1.5f, 130));
  assertEquals(230, ShelfGeometry.tile(1596, 1.5f, 130));
 }
 @Test public void pagesNeverExposePartialCardsAcrossDisplaySizes() {
  for (int width : new int[]{640, 960, 1280, 1668, 1751, 2560, 3500})
   for (float density : new float[]{1, 1.5f, 2, 3})
    for (int scale : new int[]{95,100,112,115,130}) {
     int slots=ShelfGeometry.slots(width,density,scale),cell=ShelfGeometry.cell(width,density,scale);
     assertTrue(slots*cell <= width);
     assertTrue(width-slots*cell < slots);
     assertTrue(ShelfGeometry.tile(width,density,scale)*1.1f < cell);
     for(int count=1;count<=30;count++) assertEquals(0,(count+ShelfGeometry.tail(count,slots))%slots);
    }
 }
 @Test public void sizeChoicesRemainDistinctWhenTwoChoicesFitTheSamePageCount() {
  assertTrue(ShelfGeometry.tile(1596,1.5f,100)<ShelfGeometry.tile(1596,1.5f,115));
  assertTrue(ShelfGeometry.tile(1596,1.5f,115)<ShelfGeometry.tile(1596,1.5f,130));
 }
 @Test public void overflowIsAPageWithLogicalRtlIndependentOrder() {
  assertEquals(0,ShelfGeometry.pageStart(5,6));
  assertEquals(6,ShelfGeometry.pageStart(6,6));
  assertEquals(12,ShelfGeometry.pageStart(13,6));
 }
}
