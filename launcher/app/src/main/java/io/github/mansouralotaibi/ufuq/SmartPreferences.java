package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import android.content.SharedPreferences;

final class SmartPreferences {
    static final String NAME = "smart_home_v2";
    static final String MINIMAL = "minimal";
    static final String HIJRI = "hijri";
    static final String CONTENT = "content";
    static final String CONTENT_MODE = "content_mode";
    static final String PRAYER = "prayer";
    static final String WEATHER = "weather";
    static final String FOOTBALL = "football";
    static final String ORDER = "order";
    static final String HIJRI_ADJUST = "hijri_adjust";
    static final String CITY = "city";
    static final String TEAM = "team";
    static final String GREETING_NAME = "greeting_name";
    static final String[] DEFAULT_ORDER = {HIJRI, PRAYER, WEATHER, CONTENT, FOOTBALL};

    static SharedPreferences get(Context context) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    static boolean enabled(SharedPreferences p, String key) {
        if (CONTENT.equals(key)) return !"off".equals(p.getString(CONTENT_MODE, "both"));
        if (HIJRI.equals(key)) return p.getBoolean(key, true);
        return p.getBoolean(key, false);
    }

    static String[] order(SharedPreferences p) {
        String saved = p.getString(ORDER, null);
        if (saved == null || saved.length() == 0) return DEFAULT_ORDER.clone();
        String[] values = saved.split(",");
        if (values.length != DEFAULT_ORDER.length) return DEFAULT_ORDER.clone();
        return values;
    }

    private SmartPreferences() {}
}
