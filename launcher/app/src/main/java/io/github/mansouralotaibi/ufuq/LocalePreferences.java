package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

/** Per-launcher language override. It never changes the TV's system language. */
final class LocalePreferences {
    static final String KEY = "launcher_language";
    static final String SYSTEM = "system";
    static final String ARABIC = "ar";
    static final String ENGLISH = "en";

    private LocalePreferences() {}

    static Context wrap(Context base) {
        SharedPreferences prefs = base.getSharedPreferences(Config.SETTINGS_NAME, Context.MODE_PRIVATE);
        String choice = prefs.getString(KEY, SYSTEM);
        if (SYSTEM.equals(choice)) {
            // Undo our process-local override; never write the device's system language.
            Locale.setDefault(base.getResources().getConfiguration().locale);
            return base;
        }
        Locale locale = new Locale(ARABIC.equals(choice) ? ARABIC : ENGLISH);
        Locale.setDefault(locale);
        Configuration config = new Configuration(base.getResources().getConfiguration());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            config.setLayoutDirection(locale);
            return base.createConfigurationContext(config);
        }
        config.locale = locale;
        base.getResources().updateConfiguration(config, base.getResources().getDisplayMetrics());
        return base;
    }

    static String current(Context context) {
        return context.getSharedPreferences(Config.SETTINGS_NAME, Context.MODE_PRIVATE)
                .getString(KEY, SYSTEM);
    }
}
