package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** Ufuq station board: a single day horizon, deliberately without dashboard cards. */
public final class SmartCompositionLayout extends ViewGroup {
    private int moduleFocusUpId = R.id.btnWifi;
    public SmartCompositionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClipChildren(false);
        setClipToPadding(false);
    }

    void clearModules() { removeAllViews(); }

    void addModule(String key, SmartModuleView child) {
        child.setTag(key);
        addView(child, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    }

    boolean focusEntry() {
        View prayer = find(SmartPreferences.PRAYER);
        if (prayer != null && prayer.getVisibility() == VISIBLE) return prayer.requestFocus();
        for (int i = 0; i < getChildCount(); i++) if (getChildAt(i).getVisibility() == VISIBLE)
            return getChildAt(i).requestFocus();
        return false;
    }

    void setModuleFocusUpId(int id) {
        moduleFocusUpId = id;
        requestLayout();
    }

    private View find(String key) {
        for (int i = 0; i < getChildCount(); i++) if (key.equals(getChildAt(i).getTag())) return getChildAt(i);
        return null;
    }

    @Override protected void onMeasure(int widthSpec, int heightSpec) {
        int width = MeasureSpec.getSize(widthSpec), height = MeasureSpec.getSize(heightSpec);
        int available = Math.max(0, width - getPaddingLeft() - getPaddingRight());
        int gap = dp(24); float units = 0f;
        for (int i = 0; i < getChildCount(); i++) units += weight(getChildAt(i));
        int content = Math.max(0, available - gap * Math.max(0, getChildCount() - 1));
        for (int i = 0; i < getChildCount(); i++) {
            int childWidth = units <= 0 ? 0 : Math.round(content * weight(getChildAt(i)) / units);
            getChildAt(i).measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        }
        setMeasuredDimension(resolveSize(width, widthSpec), resolveSize(height, heightSpec));
    }

    @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int x = getPaddingLeft(), gap = dp(24);
        boolean rtl = getLayoutDirection() == LAYOUT_DIRECTION_RTL;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(rtl ? getChildCount() - 1 - i : i);
            child.layout(x, 0, x + child.getMeasuredWidth(), getHeight());
            x += child.getMeasuredWidth() + gap;
        }
        wireDirectionalFocus();
    }

    private float weight(View child) {
        String key = String.valueOf(child.getTag());
        if (SmartPreferences.PRAYER.equals(key)) return 3.6f;
        if (SmartPreferences.FOOTBALL.equals(key)) return 1.55f;
        return 1f;
    }

    private void wireDirectionalFocus() {
        List<View> visual = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) visual.add(getChildAt(i));
        Collections.sort(visual, Comparator.comparingInt(View::getLeft));
        for (int i = 0; i < visual.size(); i++) {
            View child = visual.get(i);
            child.setNextFocusLeftId(i == 0 ? child.getId() : visual.get(i - 1).getId());
            child.setNextFocusRightId(i == visual.size() - 1 ? child.getId() : visual.get(i + 1).getId());
            child.setNextFocusUpId(moduleFocusUpId);
            child.setNextFocusDownId(R.id.grid);
        }
    }

    private int dp(int value) { return Math.round(value * getResources().getDisplayMetrics().density); }
}
