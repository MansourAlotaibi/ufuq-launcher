package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;

import java.util.Calendar;
import java.util.Arrays;

/** Ufuq's prayer-day horizon. It redraws only when its data or focus changes. */
final class DayRailView extends SmartModuleView {
    private final Paint hair = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint accent = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint name = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint time = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final boolean arabic;
    private final float[] stationX = new float[5], labelX = new float[5], half = new float[5];
    private final int[] order = new int[5];
    private int[] minutes = new int[0];
    private String[] names = new String[0];

    DayRailView(Context context, boolean arabic) {
        super(context, arabic);
        this.arabic = arabic;
        setGravity(Gravity.TOP);
        setPadding(0, 0, 0, 0);
        setText(null);
        hair.setColor(0x3DFBF4E8);
        accent.setColor(UiTheme.accent(getContext()));
        name.setColor(0x80FBF4E8);
        name.setTextSize(dp(13));
        name.setTypeface(UiFont.get(context, R.string.font_regular));
        time.setColor(0xBDFBF4E8);
        time.setTextSize(dp(15));
        time.setTypeface(UiFont.get(context, R.string.font_light));
        setContentDescription(context.getString(R.string.card_prayer_title));
    }

    void setPrayerTimes(PrayerCalculator.Times t) {
        // Sunrise remains available in PrayerCalculator, but Ufuq presents the five obligatory prayers.
        minutes = new int[]{t.fajr, t.dhuhr, t.asr, t.maghrib, t.isha};
        names = arabic
                ? new String[]{"الفجر", "الظهر", "العصر", "المغرب", "العشاء"}
                : new String[]{"Fajr", "Dhuhr", "Asr", "Maghrib", "Isha"};
        StringBuilder description = new StringBuilder(getContext().getString(R.string.card_prayer_title));
        for (int i = 0; i < names.length; i++) description.append(' ').append(names[i]).append(' ')
                .append(LauncherTimeFormat.minute(getContext(), minutes[i], arabic));
        setContentDescription(description.toString());
        invalidate();
    }

    @Override protected void onDraw(Canvas canvas) {
        if (minutes.length != 5) return;
        // The marker radius is 12dp. Keeping the rail 14dp below the view edge prevents the Canvas
        // from clipping its upper arc while retaining a quiet single-line composition.
        float y = dp(14), left = dp(6), right = getWidth() - dp(6);
        int now = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60
                + Calendar.getInstance().get(Calendar.MINUTE);
        int start = minutes[0], end = minutes[minutes.length - 1];
        // Before Fajr and after Isha are deliberately clamped to the rail edges. The next prayer
        // still wraps to Fajr, without pretending that overnight hours belong to today's rail.
        float progress = PrayerRailMath.fraction(now, start, end);
        float sun = left + (right - left) * (arabic ? 1f - progress : progress);
        hair.setStrokeWidth(isFocused() ? dp(3) : dp(1));
        hair.setColor(isFocused() ? 0x80FBF4E8 : 0x3DFBF4E8);
        canvas.drawLine(left, y, right, y, hair);
        accent.setStrokeWidth(isFocused() ? dp(3) : dp(2));
        if (arabic) canvas.drawLine(right, y, sun, y, accent);
        else canvas.drawLine(left, y, sun, y, accent);
        accent.setColor(0x46000000 | (UiTheme.accent(getContext()) & 0x00FFFFFF));
        canvas.drawCircle(sun, y, dp(12), accent);
        accent.setColor(UiTheme.accent(getContext()));
        canvas.drawCircle(sun, y, dp(7), accent);

        int next = PrayerRailMath.next(now, minutes);
        for (int i = 0; i < minutes.length; i++) {
            float logical = PrayerRailMath.fraction(minutes[i], start, end);
            stationX[i] = left + (right - left) * (arabic ? 1f - logical : logical);
            labelX[i] = stationX[i];
            half[i] = Math.max(name.measureText(names[i]), time.measureText(
                    LauncherTimeFormat.minute(getContext(), minutes[i], arabic))) / 2f;
            order[i] = i;
        }
        for (int i = 1; i < minutes.length; i++) {
            int item = order[i], j = i - 1;
            while (j >= 0 && stationX[order[j]] > stationX[item]) { order[j + 1] = order[j]; j--; }
            order[j + 1] = item;
        }
        float padding = dp(10);
        for (int n = 0; n < order.length; n++) {
            int i = order[n];
            labelX[i] = Math.max(labelX[i], left + half[i]);
            if (n > 0) {
                int before = order[n - 1];
                labelX[i] = Math.max(labelX[i], labelX[before] + half[before] + half[i] + padding);
            }
        }
        for (int n = order.length - 1; n >= 0; n--) {
            int i = order[n];
            labelX[i] = Math.min(labelX[i], right - half[i]);
            if (n < order.length - 1) {
                int after = order[n + 1];
                labelX[i] = Math.min(labelX[i], labelX[after] - half[after] - half[i] - padding);
            }
        }
        for (int i = 0; i < minutes.length; i++) {
            // Station position reflects elapsed solar/prayer time, not equal visual spacing.
            float x = labelX[i];
            name.setTextAlign(Paint.Align.CENTER); time.setTextAlign(Paint.Align.CENTER);
            boolean isNext = i == next;
            name.setColor(isNext ? UiTheme.accent(getContext()) : i < next ? 0x66FBF4E8 : 0x80FBF4E8);
            time.setColor(isNext ? UiTheme.accent(getContext()) : i < next ? 0x66FBF4E8 : 0xBDFBF4E8);
            if (Math.abs(x - stationX[i]) > dp(2)) {
                hair.setStrokeWidth(dp(1));
                hair.setColor(0x35FBF4E8);
                canvas.drawLine(stationX[i], y + dp(7), x, dp(34), hair);
            }
            canvas.drawText(names[i], x, dp(48), name);
            String shown = LauncherTimeFormat.minute(getContext(), minutes[i], arabic);
            canvas.drawText(shown, x, dp(74), time);
        }
    }

    @Override protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
