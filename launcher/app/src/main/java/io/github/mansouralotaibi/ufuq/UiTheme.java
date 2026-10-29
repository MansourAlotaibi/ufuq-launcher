package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/** UI palette preferences never touch wallpaper or external configuration preferences. */
final class UiTheme {
    static final String KEY = "palette";
    static int selected(Context c) {
        return ThemePalette.index(c.getSharedPreferences("ufuq_appearance", 0).getString(KEY, "classic"));
    }
    static int accent(Context c) { return ThemePalette.ACCENTS[selected(c)]; }
    static void select(Context c, int index) {
        c.getSharedPreferences("ufuq_appearance", 0).edit().putString(KEY, ThemePalette.IDS[index]).apply();
        android.content.SharedPreferences p = c.getSharedPreferences(Config.SETTINGS_NAME, 0);
        p.edit().putLong(Config.SETTINGS_REVISION_KEY, p.getLong(Config.SETTINGS_REVISION_KEY, 0) + 1).apply();
    }
    private static GradientDrawable shape(Context c, boolean focus) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(focus ? accent(c) : ThemePalette.SURFACES[selected(c)]);
        d.setCornerRadius(12 * c.getResources().getDisplayMetrics().density);
        d.setStroke((int) Math.ceil(c.getResources().getDisplayMetrics().density),
                focus ? accent(c) : 0x55FFFFFF);
        return d;
    }
    static void apply(View v) {
        // Native Holo dialog chrome is outside the app's resource IDs. Tint known framework
        // title/divider views when present; unfamiliar OEM dialogs retain their safe platform style.
        if (v.getId() != View.NO_ID && v.getResources() != null && v.getContext() != null) {
            try {
                String name = "android".equals(v.getResources().getResourcePackageName(v.getId()))
                        ? v.getResources().getResourceEntryName(v.getId()) : "";
                if ("alertTitle".equals(name) && v instanceof TextView)
                    ((TextView) v).setTextColor(accent(v.getContext()));
                else if ("titleDivider".equals(name) || "titleDividerTop".equals(name))
                    v.setBackgroundColor(accent(v.getContext()));
            } catch (android.content.res.Resources.NotFoundException ignored) { }
        }
        if (v instanceof android.widget.ListView) {
            GradientDrawable selector = shape(v.getContext(), false);
            selector.setStroke((int) Math.ceil(2 * v.getResources().getDisplayMetrics().density), accent(v.getContext()));
            ((android.widget.ListView) v).setSelector(selector);
        }
        if (v instanceof SmartModuleView) {
            v.setBackground(null);
            v.invalidate();
            return;
        }
        if (v instanceof TextView) {
            TextView t = (TextView) v;
            if (t.isFocusable() && t.isClickable()) {
                StateListDrawable bg = new StateListDrawable();
                bg.addState(new int[]{android.R.attr.state_focused}, shape(v.getContext(), true));
                bg.addState(new int[]{android.R.attr.state_pressed}, shape(v.getContext(), true));
                bg.addState(new int[]{android.R.attr.state_activated}, shape(v.getContext(), true));
                bg.addState(new int[]{android.R.attr.state_selected}, shape(v.getContext(), true));
                bg.addState(new int[]{}, shape(v.getContext(), false));
                t.setBackground(bg);
                t.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_focused}, {android.R.attr.state_activated}, {android.R.attr.state_selected}, {}},
                        new int[]{0xFF10151F, 0xFF10151F, 0xFF10151F, 0xFFF5F5F5}));
            } else if (t.getCurrentTextColor() == 0xFFF2B25B) t.setTextColor(accent(v.getContext()));
        }
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            for (int i = 0; i < group.getChildCount(); i++) apply(group.getChildAt(i));
        }
    }
    private UiTheme() {}
}
