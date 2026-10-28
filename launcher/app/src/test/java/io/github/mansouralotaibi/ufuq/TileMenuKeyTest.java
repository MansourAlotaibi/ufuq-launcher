package io.github.mansouralotaibi.ufuq;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TileMenuKeyTest {
    @Test public void shortPressPassesThroughToLaunchApp() {
        TileMenuKey key = new TileMenuKey();
        assertEquals(TileMenuKey.PASS, key.event(true, false, 0, false));
        assertEquals(TileMenuKey.PASS, key.event(false, true, 0, false));
    }

    @Test public void repeatedKitKatEnterOpensOnceAndConsumesRelease() {
        TileMenuKey key = new TileMenuKey();
        assertEquals(TileMenuKey.PASS, key.event(true, false, 0, false));
        assertEquals(TileMenuKey.OPEN, key.event(true, false, 1, false));
        assertEquals(TileMenuKey.CONSUME, key.event(true, false, 2, false));
        assertEquals(TileMenuKey.CONSUME, key.event(false, true, 0, false));
    }

    @Test public void frameworkLongPressAlsoOpensMenu() {
        TileMenuKey key = new TileMenuKey();
        assertEquals(TileMenuKey.OPEN, key.event(true, false, 0, true));
        assertEquals(TileMenuKey.CONSUME, key.event(false, true, 0, false));
    }

    @Test public void endingHoldAtPopupLetsFirstMenuPressThroughWhenRemoteOmitsKeyUp() {
        TileMenuKey key = new TileMenuKey();
        assertEquals(TileMenuKey.OPEN, key.event(true, false, 1, false));
        key.reset();
        assertEquals(TileMenuKey.PASS, key.event(true, false, 0, false));
        assertEquals(TileMenuKey.PASS, key.event(false, true, 0, false));
    }
}
