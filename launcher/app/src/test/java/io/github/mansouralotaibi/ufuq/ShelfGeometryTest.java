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
 @Test public void reorderUpDownUsesVisiblePageSizeAndClampsIncompletePage() {
  assertEquals(6,ShelfGeometry.verticalMove(0,7,6,true));
  assertEquals(6,ShelfGeometry.verticalMove(5,7,6,true));
  assertEquals(0,ShelfGeometry.verticalMove(6,7,6,false));
  assertEquals(6,ShelfGeometry.verticalMove(6,7,6,true));
  assertEquals(5,ShelfGeometry.verticalMove(5,7,6,false));
  assertEquals(7,ShelfGeometry.verticalMove(13,14,6,false));
  for(int slots=1;slots<=8;slots++) for(int count=1;count<=30;count++)
   for(int from=0;from<count;from++) for(boolean down:new boolean[]{true,false}) {
    int to=ShelfGeometry.verticalMove(from,count,slots,down);
    assertTrue(to>=0 && to<count);
    if(to!=from) assertEquals(down ? from/slots+1 : from/slots-1,to/slots);
   }
 }
 @Test public void shortPagesAreCenteredWithoutChangingPageWidthOrOrder() {
  for(int slots=1;slots<=8;slots++) for(int count=1;count<=30;count++) {
   int cell=267,unused=ShelfGeometry.tail(count,slots)*cell;
   int lastStart=ShelfGeometry.pageStart(count-1,slots),lead=0,trail=0;
   for(int position=0;position<count;position++) {
    int l=ShelfGeometry.edgeSpace(count,position,slots,cell,true);
    int t=ShelfGeometry.edgeSpace(count,position,slots,cell,false);
    if(position<lastStart){assertEquals(0,l);assertEquals(0,t);}
    lead+=l;trail+=t;
   }
   assertEquals(unused,lead+trail);assertTrue(Math.abs(lead-trail)<=1);
   assertEquals(slots*cell,(count-lastStart)*cell+lead+trail);
  }
  assertEquals(266,ShelfGeometry.edgeSpace(4,0,6,266,true));
  assertEquals(266,ShelfGeometry.edgeSpace(4,3,6,266,false));
 }
}
