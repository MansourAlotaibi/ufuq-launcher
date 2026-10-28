package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.TextClock;

/** TextClock that applies MansourTV's numeral preference without changing the TV locale. */
public final class NumeralTextClock extends TextClock {
    public NumeralTextClock(Context c) { super(c); init(); }
    public NumeralTextClock(Context c, AttributeSet a) { super(c, a); init(); }
    public NumeralTextClock(Context c, AttributeSet a, int s) { super(c, a, s); init(); }

    private void init() {
        setTransformationMethod(new TransformationMethod() {
            @Override public CharSequence getTransformation(CharSequence source, View view) {
                return NumeralStyle.apply(source, NumeralStyle.current(getContext()));
            }
            @Override public void onFocusChanged(View view, CharSequence source, boolean focused,
                                                  int direction, Rect previous) { }
        });
    }
}
