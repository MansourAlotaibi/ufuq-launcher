package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/** Tiny allocation-free Wi-Fi strength glyph for the KitKat network sheet. */
final class WifiSignalView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF barBounds = new RectF();
    private int level;
    private boolean connected;

    WifiSignalView(Context context) { super(context); }

    void bind(int value, boolean isConnected) {
        level = Math.max(0, Math.min(3, value)); connected = isConnected; invalidate();
    }

    @Override protected void onDraw(Canvas canvas) {
        float unit = getResources().getDisplayMetrics().density;
        float bar = 4 * unit, gap = 3 * unit, bottom = getHeight() - 8 * unit;
        for (int i = 0; i < 4; i++) {
            paint.setColor(i <= level ? (connected ? UiTheme.accent(getContext()) : 0xE6FBF4E8) : 0x38FBF4E8);
            float height = (6 + i * 5) * unit;
            barBounds.set(i * (bar + gap), bottom - height, i * (bar + gap) + bar, bottom);
            canvas.drawRoundRect(barBounds, 2 * unit, 2 * unit, paint);
        }
    }
}
