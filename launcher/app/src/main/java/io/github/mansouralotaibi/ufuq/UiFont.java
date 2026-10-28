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
    private UiFont() {}

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
