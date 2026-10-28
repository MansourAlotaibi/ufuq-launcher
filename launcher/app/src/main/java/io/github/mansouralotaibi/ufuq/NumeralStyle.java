package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import android.content.SharedPreferences;

/** Lightweight, locale-independent digit shaping for the launcher clock and date. */
final class NumeralStyle {
    static final String KEY = "numeral_style";
    static final String SYSTEM = "system";
    static final String ARABIC_INDIC = "arabic_indic";
    static final String LATIN = "latin";

    private NumeralStyle() {}

    static String current(Context context) {
        // The old preference remains stored for downgrade compatibility, but Ufuq now has one
        // predictable rule across clocks, dates and information modules.
        String language = context.getResources().getConfiguration().locale.getLanguage();
        return "ar".equals(language) ? ARABIC_INDIC : LATIN;
    }

    static String apply(CharSequence value, String style) {
        if (value == null) return "";
        String text = value.toString();
        if (SYSTEM.equals(style)) return text;
        StringBuilder out = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int digit = c >= '0' && c <= '9' ? c - '0'
                    : c >= '\u0660' && c <= '\u0669' ? c - '\u0660'
                    : c >= '\u06F0' && c <= '\u06F9' ? c - '\u06F0' : -1;
            if (digit < 0) out.append(c);
            else out.append((char) ((ARABIC_INDIC.equals(style) ? '\u0660' : '0') + digit));
        }
        return out.toString();
    }
}
