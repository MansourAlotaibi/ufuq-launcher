package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.widget.TextView;
import android.text.TextUtils;

/** Editorial type floating in the Scene. Focus is expressed with light, never a permanent container. */
class SmartModuleView extends TextView {
    private final Paint edge = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint accent = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint quoteGlass = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int variant;
    private String module;

    SmartModuleView(Context context, boolean arabic) {
        super(context);
        setWillNotDraw(false);
        setTextColor(0xFFF7FBFF);
        setTextSize(17);
        setTypeface(UiFont.get(context, R.string.font_light));
        setGravity(Gravity.CENTER_VERTICAL | (arabic ? Gravity.RIGHT : Gravity.LEFT));
        setTextDirection(arabic ? TEXT_DIRECTION_RTL : TEXT_DIRECTION_LTR);
        setPadding(dp(6), dp(18), dp(6), dp(5));
        setLineSpacing(dp(3), 1.07f);
        setMaxLines(4);
        setEllipsize(TextUtils.TruncateAt.END);
        setFocusable(true);
        setClickable(true);
        setIncludeFontPadding(false);
        edge.setStyle(Paint.Style.STROKE);
        edge.setStrokeWidth(dp(1));
        accent.setStyle(Paint.Style.STROKE);
        accent.setStrokeWidth(dp(2));
        setShadowLayer(dp(5), 0, dp(2), 0xC0000000);
    }

    void bindModule(String key) {
        module = key;
        setTag(key);
        if (SmartPreferences.CONTENT.equals(key)) {
            setGravity(Gravity.CENTER_VERTICAL | (getTextDirection() == TEXT_DIRECTION_RTL
                    ? Gravity.RIGHT : Gravity.LEFT));
            setTextSize(17);
            setPadding(dp(6), dp(18), dp(6), dp(5));
            // Content creates its own explicit preview marker and opens a complete detail dialog.
            setEllipsize(null);
        } else if (SmartPreferences.HIJRI.equals(key)) {
            setTextSize(17);
        } else if (SmartPreferences.WEATHER.equals(key)) {
            setTextSize(17);
            setPadding(dp(6), dp(18), dp(6), dp(5));
        } else if (SmartPreferences.FOOTBALL.equals(key)) {
            // Team names and the source/timestamp can span four visual lines in RTL.
            // Keep the station inside the information rail on the 720p CVTE canvas.
            setTextSize(15);
            setLineSpacing(dp(2), 1.0f);
            setMaxLines(5);
            setPadding(dp(6), dp(14), dp(6), dp(3));
        }
    }

    void setSurfaceVariant(int value) {
        if (variant != value) { variant = value; invalidate(); }
    }

    @Override public void setText(CharSequence value, BufferType type) {
        if (value == null) { super.setText(null, type); return; }
        String raw = value.toString();
        SpannableStringBuilder styled = new SpannableStringBuilder(raw);
        int first = raw.indexOf('\n');
        int second = first < 0 ? -1 : raw.indexOf('\n', first + 1);
        if (first > 0) {
            styled.setSpan(new RelativeSizeSpan(.78f), 0, first, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            styled.setSpan(new ForegroundColorSpan(0x80FBF4E8), 0, first, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        int primaryStart = first < 0 ? 0 : first + 1;
        int primaryEnd = second < 0 ? raw.length() : second;
        if (primaryEnd > primaryStart) {
            styled.setSpan(new RelativeSizeSpan(SmartPreferences.CONTENT.equals(module) ? 1.34f : 1.25f),
                    primaryStart, primaryEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            styled.setSpan(new StyleSpan(Typeface.NORMAL), primaryStart, primaryEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (second >= 0 && raw.length() > second + 1) {
            styled.setSpan(new RelativeSizeSpan(.78f), second + 1, raw.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            styled.setSpan(new ForegroundColorSpan(0xBDFBF4E8), second + 1, raw.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (SmartPreferences.FOOTBALL.equals(module) && first > 0) {
            // Gap follows the measured metrics of the enlarged fixture runs.
            android.text.style.LineHeightSpan gap = (text,start,end,spanStart,top,fm) -> {
                fm.descent += dp(4); fm.bottom = Math.max(fm.bottom, fm.descent);
            };
            styled.setSpan(gap, 0, second < 0 ? raw.length() : second + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        UiFont.prepare(this, styled);
        super.setText(styled, type);
        setContentDescription(raw.replace('\n', ' '));
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        quoteGlass.setShader(new android.graphics.LinearGradient(0, 0, w, h,
                0x9C173548, 0x6B102432, android.graphics.Shader.TileMode.CLAMP));
    }

    @Override protected void onDraw(Canvas canvas) {
        edge.setColor(isFocused() ? UiTheme.accent(getContext()) : 0x3DFBF4E8);
        edge.setStrokeWidth(isFocused() ? dp(3) : dp(1));
        canvas.drawLine(0, isFocused() ? dp(2) : dp(1), getWidth(), isFocused() ? dp(2) : dp(1), edge);
        if (isFocused()) {
            edge.setColor(0xD8FBF4E8);
            edge.setStrokeWidth(dp(2));
            float inset = dp(9), arm = dp(14);
            canvas.drawLine(inset, inset, inset + arm, inset, edge);
            canvas.drawLine(inset, inset, inset, inset + arm, edge);
            canvas.drawLine(getWidth() - inset, inset, getWidth() - inset - arm, inset, edge);
            canvas.drawLine(getWidth() - inset, inset, getWidth() - inset, inset + arm, edge);
        }
        drawModuleMark(canvas);
        if (isFocused()) {
            accent.setColor(UiTheme.accent(getContext()));
            float y = getHeight() - dp(5);
            canvas.drawLine(getWidth() * .38f, y, getWidth() * .62f, y, accent);
        }
        super.onDraw(canvas);
    }

    private void drawModuleMark(Canvas canvas) {
        Object value = getTag();
        if (!(value instanceof String)) return;
        String key = (String) value;
        float x = getTextDirection() == TEXT_DIRECTION_RTL ? dp(27) : getWidth() - dp(27);
        float y = getHeight() * .5f;
        Paint mark = accent;
        mark.setColor(isFocused() ? UiTheme.accent(getContext()) : 0x80FBF4E8);
        mark.setStrokeWidth(dp(1));
        float r = dp(9);
        if (SmartPreferences.PRAYER.equals(key)) {
            canvas.drawArc(new RectF(x-r, y-r, x+r, y+r), 55, 250, false, mark);
            canvas.drawCircle(x + dp(5), y - dp(6), dp(1), mark);
        } else if (SmartPreferences.HIJRI.equals(key)) {
            canvas.drawRoundRect(new RectF(x-r, y-r+dp(2), x+r, y+r), dp(2), dp(2), mark);
            canvas.drawLine(x-r, y-dp(3), x+r, y-dp(3), mark);
        } else if (SmartPreferences.CONTENT.equals(key)) {
            canvas.drawLine(x, y-r, x, y+r, mark);
            canvas.drawLine(x, y-r, x-r, y-r+dp(3), mark);
            canvas.drawLine(x, y-r, x+r, y-r+dp(3), mark);
            canvas.drawLine(x-r, y-r+dp(3), x-r, y+dp(6), mark);
            canvas.drawLine(x+r, y-r+dp(3), x+r, y+dp(6), mark);
        } else if (SmartPreferences.FOOTBALL.equals(key)) {
            canvas.drawCircle(x, y, r, mark);
            canvas.drawCircle(x, y, dp(3), mark);
            canvas.drawLine(x-r, y, x+r, y, mark);
        } else if (SmartPreferences.WEATHER.equals(key)) {
            canvas.drawCircle(x, y, dp(5), mark);
            canvas.drawLine(x, y-r, x, y-r-dp(3), mark);
            canvas.drawLine(x, y+r, x, y+r+dp(3), mark);
        }
    }

    @Override protected void drawableStateChanged() {
        super.drawableStateChanged();
        setTextColor(isFocused() ? 0xFFFFFFFF : 0xFFF2F6F8);
        invalidate();
    }

    private int dp(int value) { return Math.round(value * getResources().getDisplayMetrics().density); }
}
