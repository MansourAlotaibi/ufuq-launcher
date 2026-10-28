package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/** One source of truth for every time shown by MansourTV, not only the header clock. */
final class LauncherTimeFormat {
    private LauncherTimeFormat() {}

    static boolean twelveHour(Context context) {
        SharedPreferences p = context.getSharedPreferences(Config.SETTINGS_NAME, Context.MODE_PRIVATE);
        String value = p.getString(MainActivity.CLOCK_FORMAT_KEY, "system");
        return "12".equals(value) || "system".equals(value)
                && !android.text.format.DateFormat.is24HourFormat(context);
    }

    static String minute(Context context, int minute, boolean arabic) {
        minute = ((minute % 1440) + 1440) % 1440;
        int hour = minute / 60;
        String result;
        if (twelveHour(context)) {
            int shown = hour % 12;
            if (shown == 0) shown = 12;
            String period = context.getString(hour < 12 ? R.string.clock_am : R.string.clock_pm);
            result = String.format(Locale.US, "%d:%02d", shown, minute % 60) + " " + period;
        } else {
            result = String.format(Locale.US, "%02d:%02d", hour, minute % 60);
        }
        return NumeralStyle.apply(result, NumeralStyle.current(context));
    }

    static String timestamp(Context context, long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return minute(context, c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE),
                "ar".equals(context.getResources().getConfiguration().locale.getLanguage()));
    }

    static String event(Context context, String source) {
        if (source == null || source.length() < 5) return source == null ? "" : source;
        try {
            Date date = new SimpleDateFormat("HH:mm", Locale.US).parse(source.substring(0, 5));
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return minute(context, c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE),
                    "ar".equals(context.getResources().getConfiguration().locale.getLanguage()));
        } catch (Exception ignored) { return source; }
    }
}
