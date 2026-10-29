package io.github.mansouralotaibi.ufuq;

/** Whole-cell pages: focus room is part of each cell, never a clipped overflow hint. */
final class ShelfGeometry {
    static int slots(int width, float density, int scale) {
        float desired = (152f * Math.max(95, Math.min(130, scale)) / 130f + 24f) * density;
        return Math.max(1, (int) (width / desired));
    }
    static int cell(int width, float density, int scale) {
        return Math.max(1, width / slots(width, density, scale));
    }
    static int tile(int width, float density, int scale) {
        int requested = Math.round((120f + 36f * (Math.max(95, Math.min(130, scale)) - 95) / 35f) * density);
        return Math.max(1, Math.min(requested, Math.min(cell(width, density, scale) - Math.round(24 * density),
                (int) (cell(width, density, scale) / 1.1f) - 1)));
    }
    static int pageStart(int position, int slots) { return Math.max(0, position) / slots * slots; }
    static int tail(int count, int slots) { return (slots - count % slots) % slots; }
    private ShelfGeometry() { }
}
