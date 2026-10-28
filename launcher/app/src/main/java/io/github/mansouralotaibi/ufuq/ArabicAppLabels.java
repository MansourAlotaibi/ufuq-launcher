package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Small offline fallback table used only when an app does not provide an Arabic label. */
final class ArabicAppLabels {
    private static final Map<String, String> NAMES;
    static {
        Map<String, String> n = new HashMap<>();
        n.put("org.videolan.vlc", "مشغّل VLC");
        n.put("free.rm.skytube.legacy.oss", "سكاي تيوب");
        n.put("com.android.vending", "متجر Play");
        n.put("com.estrongs.android.pop", "مدير الملفات");
        n.put("cm.aptoidetv.pt.cvt512_apps", "متجر Aptoide TV");
        n.put("com.opera.browser", "متصفح Opera");
        n.put("com.gsetech.smartiptv", "تلفاز IPTV");
        n.put("com.ecloud.eairplay", "البث الهوائي");
        n.put("com.cvte.tv.setting", "إعدادات التلفاز");
        n.put("de.szalkowski.activitylauncher", "مشغّل الأنشطة");
        NAMES = Collections.unmodifiableMap(n);
    }

    private ArabicAppLabels() {}

    static String resolve(Context context, String pkg, String appLabel, Map<String, String> overrides) {
        String custom = overrides == null ? null : overrides.get(pkg);
        if (custom != null && !custom.trim().isEmpty()) return custom;
        String clean = Cards.cleanLabel(appLabel, pkg);
        Locale locale = context.getResources().getConfiguration().locale;
        if (!"ar".equals(locale.getLanguage()) || containsArabic(clean)) return clean;
        String fallback = NAMES.get(pkg);
        return fallback == null ? clean : fallback;
    }

    static boolean containsArabic(String text) {
        if (text == null) return false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= '\u0600' && c <= '\u06FF') return true;
        }
        return false;
    }
}
