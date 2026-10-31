package io.github.mansouralotaibi.ufuq;

import android.view.View;
import android.view.ViewGroup;

/** Shared foreground spacing; wallpapers and overlays keep their full-screen bounds. */
final class HomeGeometry {
    static final int VERTICAL_SHIFT_DP = 24;
    static void balance(View root, float density) {
        int shift = Math.round(VERTICAL_SHIFT_DP * density);
        View content = (View) root.findViewById(R.id.homeHeader).getParent();
        content.setPadding(content.getPaddingLeft(), content.getPaddingTop() + shift,
                content.getPaddingRight(), content.getPaddingBottom());
        for (int id : new int[]{R.id.cinematicClockPanel, R.id.reflectionHost, R.id.smartCards}) {
            View child = root.findViewById(id);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            p.topMargin += shift; child.setLayoutParams(p);
        }
        View page = root.findViewById(R.id.shelfPage);
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) page.getLayoutParams();
        p.bottomMargin = Math.max(0, p.bottomMargin - shift); page.setLayoutParams(p);
    }
    private HomeGeometry() { }
}
