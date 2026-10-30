package io.github.mansouralotaibi.ufuq;

import android.content.Context;

/** Shared prayer/football axis. Focus never moves the horizontal line. */
final class HorizonStyle {
    static int axis(Context c) { return Math.round(14*c.getResources().getDisplayMetrics().density); }
    static int stroke(Context c, boolean focused) { return Math.round((focused?3:1)*c.getResources().getDisplayMetrics().density); }
    static int color(Context c, boolean focused) {
        int[] tint={0xFBF4E8,0xDCE6F3,0xD9F3F0};
        return (focused?0x80000000:0x3D000000)|tint[UiTheme.selected(c)];
    }
    private HorizonStyle() { }
}
