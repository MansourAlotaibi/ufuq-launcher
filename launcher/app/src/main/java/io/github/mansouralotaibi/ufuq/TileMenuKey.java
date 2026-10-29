package io.github.mansouralotaibi.ufuq;

/**
 * Separates a remote's short OK press from a held press. Some KitKat TV builds do not turn a
 * repeated DPAD_CENTER/ENTER key into View.performLongClick(), even though they do send repeats.
 */
final class TileMenuKey {
    static final int PASS = 0;
    static final int OPEN = 1;
    static final int CONSUME = 2;

    private boolean opened;

    int event(boolean down, boolean up, int repeatCount, boolean frameworkLongPress) {
        if (down) {
            // A fresh press starts a new gesture even when the previous remote omitted UP.
            if (opened && repeatCount == 0 && !frameworkLongPress) {
                opened = false;
                return PASS;
            }
            if (!opened && (frameworkLongPress || repeatCount > 0)) {
                opened = true;
                return OPEN;
            }
            return opened ? CONSUME : PASS;
        }
        if (up) {
            boolean consume = opened;
            opened = false;
            return consume ? CONSUME : PASS;
        }
        opened = false;
        return PASS;
    }

    void reset() { opened = false; }

    boolean active() { return opened; }
}
