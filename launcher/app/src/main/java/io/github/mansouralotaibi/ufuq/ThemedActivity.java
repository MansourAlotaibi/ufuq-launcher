package io.github.mansouralotaibi.ufuq;

/** Reapply on foreground/focus transitions; no polling, timers or global Activity references. */
public class ThemedActivity extends android.app.Activity {
    @Override protected void onResume() {
        super.onResume();
        if (!(this instanceof MainActivity) && !(this instanceof ClockActivity)) {
            android.view.ViewGroup content = findViewById(android.R.id.content);
            if (content != null && content.getChildCount() > 0)
                content.getChildAt(0).setBackgroundColor(ThemePalette.SURFACES[UiTheme.selected(this)]);
        }
        UiTheme.apply(getWindow().getDecorView());
    }
    @Override public void onWindowFocusChanged(boolean focused) {
        super.onWindowFocusChanged(focused);
        if (focused) UiTheme.apply(getWindow().getDecorView());
    }
}
