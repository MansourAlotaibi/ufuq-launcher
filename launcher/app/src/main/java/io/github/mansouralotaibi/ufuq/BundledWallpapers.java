package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/** Bounded, one-time local extraction: bundled art works even without external storage. */
final class BundledWallpapers {
    private static final String[] NAMES = {"desert-sunset.jpg", "moraine-lake.jpg", "golden-dunes.png", "silver-peaks.png", "turquoise-horizon.png", "utah-dunes.jpg", "peyto-lake.jpg", "lofoten-evening.jpg", "bled-blue-hour.jpg"};
    static synchronized List<File> collection(Context context, File themeDir) {
        File dir = new File(context.getFilesDir(), "bundled-wallpapers-v1");
        List<File> result = new ArrayList<>();
        if (dir.isDirectory() || dir.mkdirs()) for (String name : NAMES) {
            File target = new File(dir, name);
            if (!target.isFile() || target.length() == 0) {
                File partial = new File(dir, name + ".tmp");
                try (InputStream in = context.getAssets().open("wallpapers/" + name);
                     FileOutputStream out = new FileOutputStream(partial)) {
                    byte[] buffer = new byte[8192]; int count;
                    while ((count = in.read(buffer)) != -1) out.write(buffer, 0, count);
                } catch (Exception error) {
                    partial.delete();
                    continue;
                }
                if (!partial.renameTo(target)) { partial.delete(); continue; }
            }
            result.add(target);
        }
        // A user's explicitly supplied file with the same name takes precedence.
        for (File custom : WallpaperRotation.images(themeDir == null ? null : new File(themeDir, "wallpapers"))) {
            for (int i = result.size() - 1; i >= 0; i--) if (result.get(i).getName().equals(custom.getName())) result.remove(i);
            result.add(custom);
        }
        return result;
    }
    private BundledWallpapers() {}
}
