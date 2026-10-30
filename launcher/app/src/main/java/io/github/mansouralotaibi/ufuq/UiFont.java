package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;

import androidx.core.content.res.ResourcesCompat;

/**
 * The typeface behind one of the font_* strings in res/values/fonts.xml, for views built in code. The string
 * points at a bundled font, or, in locales Outfit can't draw (values-vi), names a system family (#833).
 */
final class UiFont {
    static final String KEEP_APPROVED = "ufuq.approvedTypography";
    private static final java.util.Map<String, Typeface> loaded = new java.util.HashMap<>();
    private static final java.util.WeakHashMap<android.widget.TextView, FontState> originals = new java.util.WeakHashMap<>();
    private static final class FontState {
        Typeface original;
        boolean active;
        boolean replacing;
        final android.graphics.Paint.FontMetricsInt approved = new android.graphics.Paint.FontMetricsInt();
        int selectedHeight;
        final android.text.style.LineHeightSpan metrics = (text,start,end,spanStart,top,fm) -> {
            float scale = selectedHeight <= 0 ? 1 : (float)(fm.descent-fm.ascent)/selectedHeight;
            fm.ascent = (int)Math.floor(approved.ascent*scale);
            fm.descent = (int)Math.ceil(approved.descent*scale);
            fm.top = (int)Math.floor(approved.top*scale);
            fm.bottom = (int)Math.ceil(approved.bottom*scale);
        };
        void span(android.text.Spannable value) {
            if (active && value.getSpanStart(metrics)==0 && value.getSpanEnd(metrics)==value.length()) return;
            value.removeSpan(metrics);
            if (active && value.length()>0) value.setSpan(metrics,0,value.length(),
                    android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE | (1 << android.text.Spanned.SPAN_PRIORITY_SHIFT));
        }
    }
    private UiFont() {}
    static boolean arabic(Context c) { return "ar".equals(c.getResources().getConfiguration().locale.getLanguage()); }
    static final String ARABIC_KEY = "font_ar_family";
    private static final String[] ARABIC_IDS = {"default", "cairo", "tajawal", "ibm-plex-sans-arabic", "readex-pro", "almarai"};
    private static final String[] ARABIC_FILES = {null, "Cairo-Regular.ttf", "Tajawal-Regular.ttf", "IBMPlexSansArabic-Regular.ttf", "ReadexPro-Regular.ttf", "Almarai-Regular.ttf"};
    static String[] names(Context c) {
        return arabic(c) ? new String[]{c.getString(R.string.font_default), "Cairo · القاهرة", "Tajawal · تجوال",
                "IBM Plex Sans Arabic", "Readex Pro · ريدكس برو", "Almarai · المراعي"}
                : new String[]{c.getString(R.string.font_default), "Lato"};
    }
    static int selection(Context c) {
        android.content.SharedPreferences p = c.getSharedPreferences(Config.SETTINGS_NAME, 0);
        if (!arabic(c)) return p.getInt("font_en", 0) == 1 ? 1 : 0;
        if (!p.contains(ARABIC_KEY)) {
            // Legacy font_ar=1 meant Amiri, never Cairo. Migrate only font keys, once.
            p.edit().putString(ARABIC_KEY, "default").remove("font_ar").apply();
        }
        String id = p.getString(ARABIC_KEY, "default");
        for (int i=0;i<ARABIC_IDS.length;i++) if (ARABIC_IDS[i].equals(id)) return i;
        return 0;
    }
    static void select(Context c, int value) {
        android.content.SharedPreferences.Editor e = c.getSharedPreferences(Config.SETTINGS_NAME, 0).edit();
        if (arabic(c)) e.putString(ARABIC_KEY, ARABIC_IDS[value>=0 && value<ARABIC_IDS.length ? value : 0]).remove("font_ar");
        else e.putInt("font_en", value==1 ? 1 : 0);
        e.apply();
    }
    static synchronized Typeface selected(Context c) {
        int chosen = selection(c);
        if (chosen == 0) return get(c, R.string.font_regular);
        String name = arabic(c) ? ARABIC_FILES[chosen] : "Lato-Regular.ttf";
        Typeface font = loaded.get(name);
        if (font == null) {
            font = Typeface.createFromAsset(c.getAssets(), "fonts/" + name);
            loaded.put(name, font);
        }
        return font;
    }
    static void apply(android.view.View v) {
        applyOne(v);
        if (v instanceof android.view.ViewGroup) {
            android.view.ViewGroup group = (android.view.ViewGroup) v;
            for (int i=0;i<group.getChildCount();i++) apply(group.getChildAt(i));
        }
    }
    static void applyOne(android.view.View v) {
        if (!(v instanceof android.widget.TextView) || v instanceof android.widget.EditText || KEEP_APPROVED.equals(v.getTag())
                || v.getId() == R.id.cinematicClock || v.getId() == R.id.clock) return;
        android.widget.TextView text = (android.widget.TextView) v;
        FontState state = originals.get(text);
        if (state == null) {
            state = new FontState(); state.original = text.getTypeface(); originals.put(text,state);
            final FontState observed = state;
            text.addTextChangedListener(new android.text.TextWatcher() {
                public void beforeTextChanged(CharSequence s,int start,int count,int after) { }
                public void onTextChanged(CharSequence s,int start,int before,int count) { }
                public void afterTextChanged(android.text.Editable value) {
                    observed.span(value);
                    if (observed.active && !observed.replacing && !(text instanceof SmartModuleView)) {
                        // KitKat may build its layout before AFTER-text callbacks. Supply the metric
                        // span in the input to the replacement layout, not only after it was measured.
                        observed.replacing = true;
                        text.setText(new android.text.SpannableStringBuilder(value), android.widget.TextView.BufferType.SPANNABLE);
                        observed.replacing = false;
                    }
                }
            });
        }
        boolean wasActive = state.active;
        state.active = selection(v.getContext()) != 0;
        android.graphics.Paint measure = new android.graphics.Paint(text.getPaint());
        measure.setTypeface(state.original); measure.getFontMetricsInt(state.approved);
        Typeface desired = !state.active ? state.original
                : Typeface.create(selected(v.getContext()), state.original == null ? Typeface.NORMAL : state.original.getStyle());
        boolean changed = wasActive != state.active || text.getTypeface() != desired;
        text.setTypeface(desired);
        android.graphics.Paint.FontMetricsInt selected = text.getPaint().getFontMetricsInt();
        state.selectedHeight = selected.descent-selected.ascent;
        if (changed) {
            android.text.SpannableStringBuilder prepared = new android.text.SpannableStringBuilder(text.getText());
            state.span(prepared); state.replacing = true;
            text.setText(prepared, android.widget.TextView.BufferType.SPANNABLE); state.replacing = false;
        } else if (text.getText() instanceof android.text.Spannable) state.span((android.text.Spannable)text.getText());
    }

    static void prepare(android.widget.TextView view, android.text.Spannable text) {
        FontState state = originals.get(view);
        if (state != null) state.span(text);
    }

    static Typeface get(Context context, int fontString) {
        Resources res = context.getResources();
        TypedValue value = new TypedValue();
        res.getValue(fontString, value, true);
        String name = String.valueOf(value.string);
        if (name.startsWith("res/")) {
            // Font resources arrived in the framework in API 26. ResourcesCompat can load ordinary bundled TTFs
            // earlier, but KitKat cannot instantiate this variable-font family XML and throws NotFoundException.
            // The system families preserve the intended hierarchy without crashing menus built in code.
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return legacy(fontString);
            try {
                Typeface font = ResourcesCompat.getFont(context, value.resourceId);
                return font == null ? legacy(fontString) : font;
            } catch (RuntimeException e) {
                return legacy(fontString);
            }
        }
        return Typeface.create(name, Typeface.NORMAL);
    }

    private static Typeface legacy(int fontString) {
        if (fontString == R.string.font_medium) return Typeface.create("sans-serif-medium", Typeface.NORMAL);
        if (fontString == R.string.font_light || fontString == R.string.font_extralight)
            return Typeface.create("sans-serif-light", Typeface.NORMAL);
        return Typeface.create("sans-serif", Typeface.NORMAL);
    }
}
