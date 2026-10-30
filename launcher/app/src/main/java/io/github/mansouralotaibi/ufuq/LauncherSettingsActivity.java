package io.github.mansouralotaibi.ufuq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/** Remote-friendly settings owned by the launcher; system settings stay one row away. */
public final class LauncherSettingsActivity extends ThemedActivity {
    static final String LAST_FIXED_WALLPAPER = Config.LAST_FIXED_WALLPAPER_KEY;
    static final String EXTRA_REORDER = "io.github.mansouralotaibi.ufuq.extra.REORDER_FROM_SETTINGS";
    private SharedPreferences choices;
    private final TextView[] columnChoices = new TextView[4];
    private final TextView[] rowChoices = new TextView[4];
    private static final int[] DOCK_OPACITIES = {100, 70, 50, 30};
    private final TextView[] dockChoices = new TextView[DOCK_OPACITIES.length];
    private static final int[] SHELF_SCALES = {100, 115, AppAdapter.DEFAULT_SHELF_SCALE};
    private final TextView[] shelfChoices = new TextView[SHELF_SCALES.length];
    private TextView wallpaperChoice, wallpaperModeChoice;
    private TextView animationsChoice;
    private TextView advancedChoice;
    private TextView followThemeChoice;
    private TextView languageChoice;
    private TextView showClockChoice;
    private TextView readabilityChoice;
    private final TextView[] clockFormatChoices = new TextView[3];
    private int columns;
    private int rowsBelowDock;
    private int dockOpacity;
    private int shelfScale;
    private boolean animations;
    private boolean advancedEffects;
    private boolean showClock;
    private String clockFormat;
    private String readabilityMode;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalePreferences.wrap(base));
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_launcher_settings);
        choices = getSharedPreferences(Config.SETTINGS_NAME, MODE_PRIVATE);
        findViewById(R.id.creditsChoice).setOnClickListener(v -> ThirdPartyCredits.show(this));
        TextView palette = findViewById(R.id.paletteChoice);
        String[] names = getResources().getStringArray(R.array.ufuq_themes);
        palette.setText(getString(R.string.ufuq_theme, names[UiTheme.selected(this)]));
        palette.setOnClickListener(v -> {
            AlertDialog dialog = new ThemedDialogBuilder(this)
                    .setTitle(R.string.ufuq_theme_title)
                    .setSingleChoiceItems(names, UiTheme.selected(this), (d, which) -> {
                        UiTheme.select(this, which);
                        d.dismiss();
                        UiTheme.apply(getWindow().getDecorView());
                        refresh();
                    }).setNegativeButton(android.R.string.cancel, null).create();
            dialog.show();
            UiTheme.apply(dialog.getWindow().getDecorView());
        });
        TextView font = findViewById(R.id.fontChoice);
        font.setText(R.string.font_style);
        font.setOnClickListener(v -> {
            String[] fontNames = UiFont.names(this);
            new ThemedDialogBuilder(this).setTitle(R.string.font_style)
                    .setSingleChoiceItems(fontNames, UiFont.selection(this), (d, which) -> {
                        UiFont.select(this, which); d.dismiss(); changed();
                        UiFont.apply(getWindow().getDecorView()); refresh();
                    }).setNegativeButton(android.R.string.cancel, null).show();
        });
        Config config = Config.load(this);
        languageChoice = findViewById(R.id.languageChoice);
        languageChoice.setOnClickListener(v -> showLanguagePicker());
        showClock = choices.getBoolean(MainActivity.SHOW_CLOCK_KEY, true);
        clockFormat = choices.getString(MainActivity.CLOCK_FORMAT_KEY, "system");
        readabilityMode = choices.getString(SceneReadability.KEY_MODE, SceneReadability.SCENE);
        showClockChoice = findViewById(R.id.showClockChoice);
        showClockChoice.setOnClickListener(v -> {
            showClock = !showClock;
            choices.edit().putBoolean(MainActivity.SHOW_CLOCK_KEY, showClock).apply();
            changed();
            refresh();
        });
        int[] clockIds = {R.id.clockSystem, R.id.clock12, R.id.clock24};
        String[] clockValues = {"system", "12", "24"};
        for (int i = 0; i < clockIds.length; i++) {
            final String value = clockValues[i];
            clockFormatChoices[i] = findViewById(clockIds[i]);
            clockFormatChoices[i].setOnClickListener(v -> {
                clockFormat = value;
                choices.edit().putString(MainActivity.CLOCK_FORMAT_KEY, value).apply();
                changed();
                refresh();
            });
        }
        boolean cinematicShelf = config.cinematicHome;
        // The Google-TV Scenes Home deliberately has one horizontally scrolling favourites shelf; its remaining
        // applications live in Apps. The legacy columns/rows controls never affected that LinearLayoutManager,
        // so showing selected values was misleading. Do not offer controls that cannot change this layout.
        findViewById(R.id.columnsLabel).setVisibility(cinematicShelf ? View.GONE : View.VISIBLE);
        findViewById(R.id.columnsChoices).setVisibility(cinematicShelf ? View.GONE : View.VISIBLE);
        findViewById(R.id.rowsLabel).setVisibility(cinematicShelf ? View.GONE : View.VISIBLE);
        findViewById(R.id.rowsChoices).setVisibility(cinematicShelf ? View.GONE : View.VISIBLE);
        findViewById(R.id.dockOpacityLabel).setVisibility(cinematicShelf ? View.GONE : View.VISIBLE);
        findViewById(R.id.dockOpacityChoices).setVisibility(cinematicShelf ? View.GONE : View.VISIBLE);
        findViewById(R.id.shelfSizeLabel).setVisibility(cinematicShelf ? View.VISIBLE : View.GONE);
        findViewById(R.id.shelfSizeChoices).setVisibility(cinematicShelf ? View.VISIBLE : View.GONE);
        View reorderChoice = findViewById(R.id.reorderChoice);
        reorderChoice.setVisibility(config.launcherSettingsButton ? View.VISIBLE : View.GONE);
        reorderChoice.setOnClickListener(v -> {
            setResult(RESULT_OK, new Intent().putExtra(EXTRA_REORDER, true));
            finish();
        });
        columns = config.columns;
        rowsBelowDock = config.rowsBelowDock;
        dockOpacity = config.dockOpacity;
        shelfScale = choices.getInt(Config.SHELF_SCALE_KEY, AppAdapter.DEFAULT_SHELF_SCALE);
        animations = config.animations;
        advancedEffects = config.animations && config.advancedEffects;

        int[] ids = {R.id.columns4, R.id.columns5, R.id.columns6, R.id.columns7};
        for (int i = 0; i < ids.length; i++) {
            final int value = i + 4;
            columnChoices[i] = findViewById(ids[i]);
            columnChoices[i].setOnClickListener(v -> {
                if (columns == value) return;
                columns = value;
                choices.edit().putInt("columns", value).apply();
                changed();
                refresh();
            });
        }
        int[] rowIds = {R.id.rows0, R.id.rows1, R.id.rows2, R.id.rows3};
        for (int i = 0; i < rowIds.length; i++) {
            final int value = i;
            rowChoices[i] = findViewById(rowIds[i]);
            rowChoices[i].setOnClickListener(v -> {
                if (rowsBelowDock == value) return;
                rowsBelowDock = value;
                choices.edit().putInt(Config.ROWS_BELOW_DOCK_KEY, value).apply();
                changed();
                refresh();
            });
        }
        int[] dockIds = {R.id.dock100, R.id.dock70, R.id.dock50, R.id.dock30};
        for (int i = 0; i < dockIds.length; i++) {
            final int value = DOCK_OPACITIES[i];
            dockChoices[i] = findViewById(dockIds[i]);
            dockChoices[i].setOnClickListener(v -> {
                if (dockOpacity == value) return;
                dockOpacity = value;
                choices.edit().putInt(Config.DOCK_OPACITY_KEY, value).apply();
                changed();
                refresh();
            });
        }
        int[] shelfIds = {R.id.shelfSmall, R.id.shelfMedium, R.id.shelfLarge};
        for (int i = 0; i < shelfIds.length; i++) {
            final int value = SHELF_SCALES[i];
            shelfChoices[i] = findViewById(shelfIds[i]);
            shelfChoices[i].setOnClickListener(v -> {
                if (shelfScale == value) return;
                shelfScale = value;
                choices.edit().putInt(Config.SHELF_SCALE_KEY, value).apply();
                changed();
                refresh();
            });
        }
        // Keep the numeric choices increasing in the Hebrew reading direction. Reorder the
        // actual views so D-pad focus follows their positions without changing the saved values.
        if (getResources().getBoolean(R.bool.rtl_app_grid)) {
            reverseRow(columnChoices);
            reverseRow(rowChoices);
            reverseRow(dockChoices);
            reverseRow(shelfChoices);
            reverseRow(clockFormatChoices);
        }
        wallpaperChoice = findViewById(R.id.wallpaperChoice);
        wallpaperChoice.setOnClickListener(v -> showWallpaperPicker());
        wallpaperModeChoice = findViewById(R.id.wallpaperModeChoice);
        wallpaperModeChoice.setOnClickListener(v -> toggleWallpaperMode());
        findViewById(R.id.wallpaperDimChoice).setOnClickListener(v -> {
            int[] values = {0, 15, 30, 45, 60, 75, 90};
            String[] labels = new String[values.length];
            for (int i = 0; i < values.length; i++) labels[i] = values[i] + "%";
            new ThemedDialogBuilder(this).setTitle(R.string.wallpaper_dim_label_title)
                    .setItems(labels, (d, which) -> {
                        choices.edit().putInt(Config.WALLPAPER_DIM_KEY, values[which])
                                .putString(SceneReadability.KEY_MODE, SceneReadability.GLOBAL).apply();
                        readabilityMode = SceneReadability.GLOBAL;
                        changed(); refresh();
                    }).setNegativeButton(android.R.string.cancel, null).show();
        });
        findViewById(R.id.rotationIntervalChoice).setOnClickListener(v -> {
            if (!v.isEnabled()) return;
            int[] values = {1, 5, 15, 30, 60};
            String[] labels = new String[values.length];
            for (int i = 0; i < values.length; i++) labels[i] = getString(R.string.rotation_interval_label, values[i]);
            new ThemedDialogBuilder(this).setTitle(R.string.rotation_interval_title)
                    .setItems(labels, (d, which) -> {
                        choices.edit().putInt(Config.ROTATION_MINUTES_KEY, values[which]).apply();
                        changed(); refresh();
                    }).setNegativeButton(android.R.string.cancel, null).show();
        });
        readabilityChoice = findViewById(R.id.readabilityChoice);
        readabilityChoice.setOnClickListener(v -> {
            readabilityMode = SceneReadability.GLOBAL.equals(readabilityMode)
                    ? SceneReadability.SCENE : SceneReadability.GLOBAL;
            choices.edit().putString(SceneReadability.KEY_MODE, readabilityMode).apply();
            changed();
            refresh();
        });
        animationsChoice = findViewById(R.id.animationsChoice);
        animationsChoice.setOnClickListener(v -> {
            animations = !animations;
            if (!animations) advancedEffects = false;
            choices.edit().putBoolean("animations", animations)
                    .putBoolean("advanced_effects", advancedEffects).apply();
            changed();
            refresh();
        });
        advancedChoice = findViewById(R.id.advancedChoice);
        advancedChoice.setOnClickListener(v -> {
            if (advancedEffects) {
                setAdvanced(false);
            } else {
                new ThemedDialogBuilder(this)
                        .setTitle(R.string.advanced_effects)
                        .setMessage(R.string.advanced_effects_warning)
                        .setPositiveButton(R.string.enable, (dialog, which) -> setAdvanced(true))
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });
        followThemeChoice = findViewById(R.id.followThemeChoice);
        followThemeChoice.setOnClickListener(v -> {
            if (!hasOnTvChoices()) return;
            choices.edit().remove("columns").remove(Config.ROWS_BELOW_DOCK_KEY).remove("animations")
                    .remove("advanced_effects").remove(Config.WALLPAPER_CHOICE_KEY)
                    .remove(Config.WALLPAPER_RANDOM_KEY).remove(LAST_FIXED_WALLPAPER)
                    .remove(Config.DOCK_OPACITY_KEY).remove(Config.SHELF_SCALE_KEY)
                    .remove(SceneReadability.KEY_MODE).remove(Config.WALLPAPER_DIM_KEY)
                    .remove(Config.ROTATION_MINUTES_KEY).commit();
            Config current = Config.load(this);
            columns = current.columns;
            rowsBelowDock = current.rowsBelowDock;
            dockOpacity = current.dockOpacity;
            animations = current.animations;
            advancedEffects = current.animations && current.advancedEffects;
            shelfScale = AppAdapter.DEFAULT_SHELF_SCALE;
            readabilityMode = SceneReadability.SCENE;
            changed();
            refresh();
        });
        TextView launcherSwitch = findViewById(R.id.launcherSwitchChoice);
        TextView launcherSwitchHelp = findViewById(R.id.launcherSwitchHelp);
        boolean hasAlternatives = !alternativeHomes().isEmpty();
        launcherSwitch.setVisibility(hasAlternatives ? View.VISIBLE : View.GONE);
        launcherSwitchHelp.setVisibility(hasAlternatives ? View.VISIBLE : View.GONE);
        launcherSwitch.setOnClickListener(v -> showLauncherPicker());
        findViewById(R.id.smartHomeChoice).setOnClickListener(v ->
                startActivity(new Intent(this, SmartSettingsActivity.class)));
        findViewById(R.id.tvSettingsChoice).setOnClickListener(v ->
                open(SystemSettings.intent(this, Settings.ACTION_SETTINGS)));
        refresh();
        if (config.launcherSettingsButton) reorderChoice.requestFocus();
        else columnChoices[Math.max(0, Math.min(columns - 4, 3))].requestFocus();
    }

    private void reverseRow(TextView[] views) {
        LinearLayout row = (LinearLayout) views[0].getParent();
        row.removeAllViews();
        for (int i = views.length - 1; i >= 0; i--) row.addView(views[i]);
    }

    private void showLanguagePicker() {
        String[] values = {LocalePreferences.SYSTEM, LocalePreferences.ARABIC, LocalePreferences.ENGLISH};
        String[] labels = {getString(R.string.language_system), getString(R.string.language_arabic),
                getString(R.string.language_english)};
        String current = LocalePreferences.current(this);
        int checked = LocalePreferences.ARABIC.equals(current) ? 1
                : LocalePreferences.ENGLISH.equals(current) ? 2 : 0;
        new ThemedDialogBuilder(this)
                .setTitle(R.string.language_picker_title)
                .setSingleChoiceItems(labels, checked, (dialog, which) -> {
                    dialog.dismiss();
                    if (values[which].equals(LocalePreferences.current(this))) return;
                    choices.edit().putString(LocalePreferences.KEY, values[which]).commit();
                    changed();
                    recreate();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    /** Opens another installed HOME once. It deliberately does not claim or change the system default. */
    private void showLauncherPicker() {
        List<ResolveInfo> homes = alternativeHomes();
        if (homes.isEmpty()) {
            Toast.makeText(this, R.string.no_other_launcher, Toast.LENGTH_SHORT).show();
            return;
        }
        String[] labels = new String[homes.size()];
        for (int i = 0; i < homes.size(); i++) {
            CharSequence label = homes.get(i).loadLabel(getPackageManager());
            labels[i] = label == null ? homes.get(i).activityInfo.packageName : label.toString();
        }
        new ThemedDialogBuilder(this).setTitle(R.string.open_other_launcher)
                .setItems(labels, (dialog, which) -> {
                    ResolveInfo selected = homes.get(which);
                    Intent openHome = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
                            .setComponent(new ComponentName(selected.activityInfo.packageName,
                                    selected.activityInfo.name))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        startActivity(openHome);
                    } catch (ActivityNotFoundException | SecurityException unavailable) {
                        Toast.makeText(this, R.string.not_available, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null).show();
    }

    private List<ResolveInfo> alternativeHomes() {
        Intent query = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> all = getPackageManager().queryIntentActivities(query,
                PackageManager.MATCH_DEFAULT_ONLY);
        List<ResolveInfo> alternatives = new java.util.ArrayList<>();
        for (ResolveInfo candidate : all) {
            if (candidate.activityInfo != null
                    && !getPackageName().equals(candidate.activityInfo.packageName)) alternatives.add(candidate);
        }
        return alternatives;
    }

    private void showWallpaperPicker() {
        File dir = Config.dir(this);
        List<File> images = new java.util.ArrayList<>(BundledWallpapers.collection(this, dir));
        // A theme with a single wallpaper.jpg offers that picture too (Config maps the choice back to it).
        File single = dir == null ? null : new File(dir, Config.WALLPAPER_NAME);
        if (single != null && single.isFile()) images.add(0, single);
        String[] labels = new String[images.size() + 1];
        for (int i = 0; i < images.size(); i++) labels[i] = WallpaperNames.label(this, images.get(i).getName());
        labels[images.size()] = getString(R.string.wallpaper_none);
        String current = choices.getString(Config.WALLPAPER_CHOICE_KEY, null);
        int checked = "none".equals(current) ? images.size()
                : indexOf(images, current);
        new ThemedDialogBuilder(this)
                .setTitle(R.string.wallpaper_label)
                .setSingleChoiceItems(labels, checked, (dialog, which) -> {
                    dialog.dismiss();
                    if (which == images.size()) {
                        choices.edit().putBoolean(Config.WALLPAPER_RANDOM_KEY, false)
                                .putString(Config.WALLPAPER_CHOICE_KEY, "none").apply();
                    } else {
                        choices.edit().putBoolean(Config.WALLPAPER_RANDOM_KEY, false)
                                .putString(Config.WALLPAPER_CHOICE_KEY,
                                images.get(which).getName()).apply();
                    }
                    changed();
                    Toast.makeText(this, R.string.wallpaper_applied, Toast.LENGTH_SHORT).show();
                    refresh();
                })
                .setNeutralButton(R.string.wallpaper_theme_default, (dialog, which) -> {
                    choices.edit().remove(Config.WALLPAPER_CHOICE_KEY)
                            .remove(Config.WALLPAPER_RANDOM_KEY).remove(LAST_FIXED_WALLPAPER).apply();
                    changed();
                    refresh();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void toggleWallpaperMode() {
        String current = choices.getString(Config.WALLPAPER_CHOICE_KEY, null);
        boolean random = choices.getBoolean(Config.WALLPAPER_RANDOM_KEY,
                !choices.contains(Config.WALLPAPER_CHOICE_KEY));
        if (random) {
            File dir = Config.dir(this);
            List<File> images = BundledWallpapers.collection(this, dir);
            SharedPreferences.Editor editor = choices.edit().putBoolean(Config.WALLPAPER_RANDOM_KEY, false);
            String saved = choices.getString(LAST_FIXED_WALLPAPER, null);
            boolean single = dir != null && Config.WALLPAPER_NAME.equals(saved) && new File(dir,Config.WALLPAPER_NAME).isFile();
            if ("none".equals(saved) || single || indexOf(images,saved)>=0) editor.putString(Config.WALLPAPER_CHOICE_KEY,saved);
            else if (!images.isEmpty()) editor.putString(Config.WALLPAPER_CHOICE_KEY,images.get(0).getName());
            editor.apply();
        } else {
            SharedPreferences.Editor editor=choices.edit().putBoolean(Config.WALLPAPER_RANDOM_KEY,true)
                    .remove(Config.WALLPAPER_CHOICE_KEY);
            if (current!=null) editor.putString(LAST_FIXED_WALLPAPER,current);
            else editor.remove(LAST_FIXED_WALLPAPER);
            editor.apply();
        }
        changed(); refreshWallpaper();
    }

    private static int indexOf(List<File> images, String name) {
        if (name == null) return -1;
        for (int i = 0; i < images.size(); i++) {
            if (images.get(i).getName().equals(name)) return i;
        }
        return -1;
    }

    /**
     * A choice was saved. Home rebuilds when it next resumes because the change count moved, whether these
     * settings are left with Back or with the remote's Home button (which cancels the activity result).
     */
    private void changed() {
        setResult(RESULT_OK);
        choices.edit().putLong(Config.SETTINGS_REVISION_KEY,
                choices.getLong(Config.SETTINGS_REVISION_KEY, 0) + 1).apply();
    }

    private void setAdvanced(boolean enabled) {
        advancedEffects = enabled;
        choices.edit().putBoolean("advanced_effects", enabled).apply();
        changed();
        refresh();
    }

    private void refresh() {
        ((TextView)findViewById(R.id.paletteChoice)).setText(getString(R.string.ufuq_theme,
                getResources().getStringArray(R.array.ufuq_themes)[UiTheme.selected(this)]));
        ((TextView)findViewById(R.id.fontChoice)).setText(getString(R.string.font_style_state,
                UiFont.names(this)[UiFont.selection(this)]));
        String language = LocalePreferences.current(this);
        int languageName = LocalePreferences.ARABIC.equals(language) ? R.string.language_arabic
                : LocalePreferences.ENGLISH.equals(language) ? R.string.language_english
                : R.string.language_system;
        languageChoice.setText(getString(R.string.language_label, getString(languageName)));
        showClockChoice.setText(getString(R.string.show_clock_state,
                getString(showClock ? R.string.on : R.string.off)));
        String[] clockValues = {"system", "12", "24"};
        for (int i = 0; i < clockFormatChoices.length; i++)
            clockFormatChoices[i].setSelected(clockValues[i].equals(clockFormat));
        for (int i = 0; i < columnChoices.length; i++) {
            columnChoices[i].setSelected(columns == i + 4);
        }
        for (int i = 0; i < rowChoices.length; i++) {
            rowChoices[i].setSelected(rowsBelowDock == i);
        }
        for (int i = 0; i < dockChoices.length; i++) {
            dockChoices[i].setSelected(dockOpacity == DOCK_OPACITIES[i]);
        }
        for (int i = 0; i < shelfChoices.length; i++) {
            shelfChoices[i].setSelected(shelfScale == SHELF_SCALES[i]);
        }
        animationsChoice.setText(getString(R.string.animations_state,
                getString(animations ? R.string.on : R.string.off)));
        advancedChoice.setText(getString(R.string.advanced_effects_state,
                getString(advancedEffects ? R.string.on : R.string.off)));
        advancedChoice.setEnabled(animations);
        advancedChoice.setFocusable(animations);
        advancedChoice.setAlpha(animations ? 1f : 0.5f);
        refreshWallpaper();
        readabilityChoice.setText(getString(R.string.readability_state,
                getString(SceneReadability.GLOBAL.equals(readabilityMode)
                        ? R.string.readability_global : R.string.readability_scene)));
        followThemeChoice.setSelected(!hasOnTvChoices());
    }

    private void refreshWallpaper() {
        String wallpaperCurrent = choices.getString(Config.WALLPAPER_CHOICE_KEY, null);
        boolean randomWallpaper = choices.getBoolean(Config.WALLPAPER_RANDOM_KEY,
                !choices.contains(Config.WALLPAPER_CHOICE_KEY));
        wallpaperModeChoice.setText(getString(R.string.wallpaper_mode, getString(randomWallpaper
                ? R.string.wallpaper_mode_random : R.string.wallpaper_mode_fixed)));
        wallpaperChoice.setEnabled(!randomWallpaper);
        wallpaperChoice.setFocusable(!randomWallpaper);
        wallpaperChoice.setAlpha(randomWallpaper ? 0.55f : 1f);
        wallpaperChoice.setText(getString(R.string.wallpaper_state,
                "none".equals(wallpaperCurrent) ? getString(R.string.wallpaper_none)
                        : wallpaperCurrent != null ? WallpaperNames.label(this, wallpaperCurrent)
                        : getString(R.string.wallpaper_theme_default)));
        ((TextView) findViewById(R.id.wallpaperDimChoice)).setText(getString(R.string.wallpaper_dim_label, Config.load(this).wallpaperDim));
        View rotation = findViewById(R.id.rotationIntervalChoice);
        rotation.setEnabled(randomWallpaper);
        rotation.setFocusable(randomWallpaper);
        rotation.setAlpha(randomWallpaper ? 1f : .4f);
        ((TextView) rotation).setTextColor(randomWallpaper ? 0xFFF5F5F5 : 0xFF86909B);
        ((TextView) rotation).setText(getString(R.string.rotation_interval_label, Config.load(this).wallpaperRotationMinutes));
        UiTheme.apply(rotation);
    }

    private boolean hasOnTvChoices() {
        return choices.contains("columns") || choices.contains(Config.ROWS_BELOW_DOCK_KEY)
                || choices.contains("animations") || choices.contains("advanced_effects")
                || choices.contains(Config.WALLPAPER_CHOICE_KEY) || choices.contains(Config.WALLPAPER_RANDOM_KEY)
                || choices.contains(Config.DOCK_OPACITY_KEY)
                || choices.contains(Config.SHELF_SCALE_KEY) || choices.contains(SceneReadability.KEY_MODE)
                || choices.contains(Config.WALLPAPER_DIM_KEY) || choices.contains(Config.ROTATION_MINUTES_KEY);
    }

    private void open(Intent intent) {
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException | SecurityException e) {
            Toast.makeText(this, R.string.not_available, Toast.LENGTH_LONG).show();
        }
    }
}
