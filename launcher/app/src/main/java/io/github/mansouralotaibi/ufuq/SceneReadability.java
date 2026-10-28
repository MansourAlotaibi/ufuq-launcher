package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** Optional per-file dim values, applied once while decoding a Scene bitmap. */
final class SceneReadability {
    static final String KEY_MODE = "scene_readability_mode";
    static final String SCENE = "scene";
    static final String GLOBAL = "global";
    private final int global;
    private final boolean enabled;
    private final Map<String, Integer> values;

    private SceneReadability(int global, boolean enabled, Map<String, Integer> values) {
        this.global = clamp(global);
        this.enabled = enabled;
        this.values = values;
    }

    static SceneReadability load(Context context, int global) {
        SharedPreferences p = context.getSharedPreferences(Config.SETTINGS_NAME, Context.MODE_PRIVATE);
        boolean enabled = !GLOBAL.equals(p.getString(KEY_MODE, SCENE));
        File dir = Config.dir(context);
        File config = dir == null ? new File(Config.FILE_NAME) : new File(dir, Config.FILE_NAME);
        Map<String, Integer> result = new HashMap<>();
        try (FileInputStream in = new FileInputStream(config)) {
            byte[] data = new byte[(int) Math.min(config.length(), 128 * 1024)];
            int count = in.read(data);
            JSONObject root = new JSONObject(new String(data, 0, Math.max(0, count), StandardCharsets.UTF_8));
            JSONObject object = root.optJSONObject("scene_readability");
            if (object != null) {
                Iterator<String> keys = object.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    result.put(key, clamp(object.optInt(key, global)));
                }
            }
        } catch (Exception ignored) {
            // Missing and old themes retain their existing global dim value.
        }
        return new SceneReadability(global, enabled, Collections.unmodifiableMap(result));
    }

    int dimFor(File file) {
        if (!enabled || file == null) return global;
        Integer value = values.get(file.getName());
        return value == null ? global : value;
    }

    static int clamp(int value) { return Math.max(0, Math.min(90, value)); }
}
