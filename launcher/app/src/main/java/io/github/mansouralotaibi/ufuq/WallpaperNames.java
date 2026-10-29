package io.github.mansouralotaibi.ufuq;

import android.content.Context;

/** Presentation only: saved asset identifiers and external collection filenames never change. */
final class WallpaperNames {
    static String label(Context c, String file) {
        String[] files = {"desert-sunset.jpg", "silver-peaks.png", "golden-dunes.png", "turquoise-horizon.png", "moraine-lake.jpg"};
        int[] ids = {R.string.wallpaper_name_0, R.string.wallpaper_name_1, R.string.wallpaper_name_2, R.string.wallpaper_name_3, R.string.wallpaper_name_4};
        for (int i=0;i<files.length;i++) if (files[i].equals(file)) return c.getString(ids[i]);
        return file;
    }
    private WallpaperNames() { }
}
