package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/** A measured editorial surface for the complete or explicitly shortened daily reflection. */
final class ReflectionView extends LinearLayout {
    private final TextView kicker;
    private final TextView quote;
    private final TextView source;
    private final TextView more;
    private final RectF bounds = new RectF();
    private final Paint glass = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint edge = new Paint(Paint.ANTI_ALIAS_FLAG);
    private DailyContent.Entry entry;

    ReflectionView(Context context, boolean arabic) {
        super(context);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setWillNotDraw(false);
        setFocusable(true);
        setClickable(true);
        setPadding(dp(24), dp(18), dp(24), dp(16));

        kicker = label(14, 0x99FBF4E8, R.string.font_medium);
        kicker.setGravity(arabic ? Gravity.RIGHT : Gravity.LEFT);
        kicker.setTextDirection(arabic ? TEXT_DIRECTION_RTL : TEXT_DIRECTION_LTR);
        addView(kicker, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        quote = label(22, 0xFFFDF8EF, R.string.font_light);
        quote.setGravity(Gravity.RIGHT);
        quote.setTextDirection(TEXT_DIRECTION_RTL);
        quote.setLineSpacing(dp(4), 1.08f);
        // KitKat's Arabic glyph ink can extend below its layout metrics; reserve a quiet optical inset.
        quote.setPadding(0, 0, 0, dp(6));
        quote.addOnLayoutChangeListener((v,l,t,r,b,ol,ot,or,ob) -> {
            if (r-l != or-ol || b-t != ob-ot) post(this::fitText);
        });
        LayoutParams quoteParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f);
        quoteParams.topMargin = dp(7);
        addView(quote, quoteParams);

        source = label(14, 0xC7FBF4E8, R.string.font_regular);
        source.setGravity(Gravity.RIGHT);
        source.setTextDirection(TEXT_DIRECTION_RTL);
        addView(source, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        more = label(12, UiTheme.accent(getContext()), R.string.font_medium);
        more.setGravity(arabic ? Gravity.RIGHT : Gravity.LEFT);
        more.setTextDirection(arabic ? TEXT_DIRECTION_RTL : TEXT_DIRECTION_LTR);
        LayoutParams moreParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        moreParams.topMargin = dp(4);
        addView(more, moreParams);

        edge.setStyle(Paint.Style.STROKE);
        setOnFocusChangeListener((view, focused) -> invalidate());
    }

    void setEntry(DailyContent.Entry value, String title, String moreLabel) {
        entry = value;
        kicker.setText(title);
        source.setText(value.source);
        more.setText(moreLabel);
        setContentDescription(title + " " + value.text + " " + value.source);
        requestLayout();
        post(this::fitText);
    }

    private TextView label(float size, int color, int font) {
        TextView view = new TextView(getContext());
        view.setTextSize(size);
        view.setTextColor(color);
        view.setTypeface(UiFont.get(getContext(), font));
        view.setIncludeFontPadding(false);
        return view;
    }

    @SuppressWarnings("deprecation")
    private void fitText() {
        if (entry == null || quote.getWidth() <= 0 || quote.getHeight() <= 0) return;
        int width = quote.getWidth();
        // Prefer the complete verified text. Short reflections get more presence, while medium and
        // long entries step down only to a still-TV-readable 19sp before using an explicit preview.
        float[] sizes = {24f, 22f, 19f};
        int maxLines = 2;
        for (float size : sizes) {
            quote.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            maxLines = availableLines();
            StaticLayout full = layout(entry.text, width);
            if (full.getLineCount() <= maxLines && full.getHeight() <= quoteContentHeight()) {
                quote.setMaxLines(maxLines);
                quote.setEllipsize(null);
                quote.setText(entry.text);
                more.setVisibility(GONE);
                return;
            }
        }

        quote.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19f);
        maxLines = availableLines();
        // KitKat's weighted TextView can ignore maxLines for drawing. Use the original 2.8.5
        // measured display-excerpt algorithm; the entry, asset and full-text dialog stay verbatim.
        int low = 1, high = entry.text.length(), best = 1;
        while (low <= high) {
            int middle = (low + high) >>> 1;
            StaticLayout measured = layout(preview(entry.text, middle), width);
            if (measured.getLineCount() <= maxLines && measured.getHeight() <= quoteContentHeight()) {
                best = middle;
                low = middle + 1;
            } else high = middle - 1;
        }
        quote.setMaxLines(maxLines);
        quote.setEllipsize(null);
        quote.setText(preview(entry.text, best));
        more.setVisibility(VISIBLE);
    }

    private String preview(String text, int limit) {
        if (limit >= text.length()) return text;
        int cut = Math.max(1, Math.min(limit, text.length()));
        while (cut > 1 && !Character.isWhitespace(text.charAt(cut - 1))) cut--;
        if (cut < Math.min(24, limit / 2)) cut = limit;
        return text.substring(0, cut).trim() + "…";
    }

    private int quoteContentHeight() {
        return Math.max(0, quote.getHeight() - quote.getCompoundPaddingTop() - quote.getCompoundPaddingBottom());
    }

    private int availableLines() {
        // Font spacing alone omitted the explicit inter-line gap and could expose half a fourth line.
        StaticLayout measured = layout(entry.text, Math.max(1, quote.getWidth()));
        int lines = 0;
        while (lines < Math.min(6, measured.getLineCount())
                && measured.getLineBottom(lines) <= quoteContentHeight()) lines++;
        return Math.max(1, lines);
    }

    @SuppressWarnings("deprecation")
    private StaticLayout layout(String text, int width) {
        return new StaticLayout(text, quote.getPaint(), Math.max(1, width), Layout.Alignment.ALIGN_NORMAL,
                1.08f, dp(4), false);
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        glass.setShader(new LinearGradient(0, 0, w, h, 0x20142C3A, 0x08131D2A,
                Shader.TileMode.CLAMP));
        post(this::fitText);
    }

    @Override protected void onDraw(Canvas canvas) {
        float inset = isFocused() ? dp(2) : dp(1);
        bounds.set(inset, inset, getWidth() - inset, getHeight() - inset);
        canvas.drawRoundRect(bounds, dp(22), dp(22), glass);
        edge.setColor(isFocused() ? UiTheme.accent(getContext()) : 0x0CFBF4E8);
        edge.setStrokeWidth(isFocused() ? dp(3) : Math.max(1f, dp(1) * .65f));
        canvas.drawRoundRect(bounds, dp(22), dp(22), edge);
        if (isFocused()) {
            edge.setColor(UiTheme.accent(getContext()));
            edge.setStrokeWidth(dp(2));
            canvas.drawLine(dp(24), getHeight() - dp(8), getWidth() - dp(24),
                    getHeight() - dp(8), edge);
        }
        super.onDraw(canvas);
    }

    @Override protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
