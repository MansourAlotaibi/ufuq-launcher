package io.github.mansouralotaibi.ufuq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

/** A single remote-friendly surface for all optional Smart Home modules. */
public final class SmartSettingsActivity extends ThemedActivity {
    private SharedPreferences p, launcher;
    private TextView minimal, greetingName, gregorian, hijri, content, prayer, football, adjust, city, team;

    @Override protected void attachBaseContext(Context base) { super.attachBaseContext(LocalePreferences.wrap(base)); }

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_smart_settings);
        p = SmartPreferences.get(this);
        launcher = getSharedPreferences(Config.SETTINGS_NAME, MODE_PRIVATE);
        minimal = findViewById(R.id.minimalMode); hijri = findViewById(R.id.hijriCard);
        greetingName = findViewById(R.id.greetingName);
        gregorian = findViewById(R.id.gregorianDate);
        content = findViewById(R.id.contentCard); prayer = findViewById(R.id.prayerCard);
        football = findViewById(R.id.footballCard);
        adjust = findViewById(R.id.hijriAdjust);
        city = findViewById(R.id.cityChoice); team = findViewById(R.id.teamChoice);
        minimal.setOnClickListener(v -> toggle(SmartPreferences.MINIMAL, false));
        greetingName.setOnClickListener(v -> chooseGreetingName());
        gregorian.setOnClickListener(v -> {
            boolean next = !launcher.getBoolean(MainActivity.SHOW_DATE_KEY, true);
            launcher.edit().putBoolean(MainActivity.SHOW_DATE_KEY, next).apply(); changed(); refresh();
        });
        hijri.setOnClickListener(v -> toggle(SmartPreferences.HIJRI, true));
        prayer.setOnClickListener(v -> toggle(SmartPreferences.PRAYER, false));
        football.setOnClickListener(v -> toggle(SmartPreferences.FOOTBALL, false));
        content.setOnClickListener(v -> cycleContent());
        adjust.setOnClickListener(v -> chooseAdjustment());
        city.setOnClickListener(v -> chooseCity());
        team.setOnClickListener(v -> chooseTeam());
        refresh();
        minimal.requestFocus();
    }

    private void toggle(String key, boolean defaultValue) {
        p.edit().putBoolean(key, !p.getBoolean(key, defaultValue)).apply(); changed(); refresh();
    }

    private void cycleContent() {
        String[] modes = {"both", "quran", "hadith", "off"};
        String now = p.getString(SmartPreferences.CONTENT_MODE, "both");
        int index = Arrays.asList(modes).indexOf(now);
        p.edit().putString(SmartPreferences.CONTENT_MODE, modes[(index + 1) % modes.length]).apply();
        changed(); refresh();
    }

    private void chooseAdjustment() {
        String[] labels = {"-2", "-1", "0", "+1", "+2"};
        new ThemedDialogBuilder(this).setTitle(R.string.smart_hijri_adjust)
                .setItems(labels, (d, which) -> { p.edit().putInt(SmartPreferences.HIJRI_ADJUST, which - 2).apply(); changed(); refresh(); })
                .setNegativeButton(android.R.string.cancel, null).show();
    }

    private void chooseGreetingName() {
        EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setText(p.getString(SmartPreferences.GREETING_NAME, ""));
        input.setSelection(input.length());
        input.setHint(R.string.greeting_name_hint);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        int horizontal = Math.round(22 * getResources().getDisplayMetrics().density);
        input.setPadding(horizontal, input.getPaddingTop(), horizontal, input.getPaddingBottom());
        new ThemedDialogBuilder(this).setTitle(R.string.greeting_name_title).setView(input)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String value = input.getText().toString().trim();
                    if (value.length() > 24) value = value.substring(0, 24).trim();
                    p.edit().putString(SmartPreferences.GREETING_NAME, value).apply();
                    changed(); refresh();
                })
                .setNeutralButton(R.string.clear_name, (dialog, which) -> {
                    p.edit().remove(SmartPreferences.GREETING_NAME).apply();
                    changed(); refresh();
                })
                .setNegativeButton(android.R.string.cancel, null).show();
    }

    private void chooseCity() {
        int[] names = {R.string.city_riyadh, R.string.city_jeddah, R.string.city_makkah, R.string.city_madinah, R.string.city_dammam};
        String[] ids = {"riyadh", "jeddah", "makkah", "madinah", "dammam"};
        String[] labels = new String[names.length]; for (int i=0;i<names.length;i++) labels[i]=getString(names[i]);
        new ThemedDialogBuilder(this).setTitle(R.string.smart_city).setItems(labels, (d,w) -> {
            p.edit().putString(SmartPreferences.CITY, ids[w]).apply(); changed(); refresh();
        }).setNegativeButton(android.R.string.cancel, null).show();
    }

    private void chooseTeam() {
        String[] labels = {getString(R.string.team_none), getString(R.string.team_group_saudi),
                getString(R.string.team_group_international)};
        new ThemedDialogBuilder(this).setTitle(R.string.smart_team_title).setItems(labels, (d,w) -> {
            if (w == 0) selectTeam(TeamCatalog.NONE);
            else chooseTeamGroup(w == 1 ? TeamCatalog.SAUDI : TeamCatalog.INTERNATIONAL);
        }).setNegativeButton(android.R.string.cancel, null).show();
    }

    private void chooseTeamGroup(String group) {
        TeamCatalog.Team[] teams = TeamCatalog.group(group);
        String[] labels = new String[teams.length];
        for (int i = 0; i < teams.length; i++) labels[i] = teams[i].name(this);
        new ThemedDialogBuilder(this).setTitle(group.equals(TeamCatalog.SAUDI)
                ? R.string.team_group_saudi : R.string.team_group_international)
                .setItems(labels, (dialog, which) -> selectTeam(teams[which]))
                .setNegativeButton(android.R.string.cancel, null).show();
    }

    private void selectTeam(TeamCatalog.Team selected) {
        p.edit().putString(SmartPreferences.TEAM, selected.id)
                .putBoolean(SmartPreferences.FOOTBALL, selected != TeamCatalog.NONE)
                .remove("cache_" + SmartPreferences.FOOTBALL)
                .remove("cache_" + SmartPreferences.FOOTBALL + "_display")
                .remove("cache_" + SmartPreferences.FOOTBALL + "_at")
                .remove("cache_" + SmartPreferences.FOOTBALL + "_team").commit();
        // Team selection changes content, not the Ufuq layout. Bumping the layout revision here recreated
        // Home while its network request was completing, leaving the newly created view on its placeholder.
        // The existing controller refreshes the selected team when Home resumes.
        refresh();
    }

    private void changed() {
        launcher.edit().putLong(Config.SETTINGS_REVISION_KEY,
                launcher.getLong(Config.SETTINGS_REVISION_KEY, 0) + 1).apply();
    }

    private String state(boolean value) { return getString(value ? R.string.on : R.string.off); }
    private void refresh() {
        minimal.setText(getString(R.string.smart_minimal, state(p.getBoolean(SmartPreferences.MINIMAL, false))));
        String name = p.getString(SmartPreferences.GREETING_NAME, "").trim();
        greetingName.setText(getString(R.string.greeting_name_state,
                name.length() == 0 ? getString(R.string.greeting_name_none) : name));
        gregorian.setText(getString(R.string.smart_gregorian,
                state(launcher.getBoolean(MainActivity.SHOW_DATE_KEY, true))));
        hijri.setText(getString(R.string.smart_hijri, state(p.getBoolean(SmartPreferences.HIJRI, true))));
        String mode = p.getString(SmartPreferences.CONTENT_MODE, "both");
        int modeLabel = "quran".equals(mode) ? R.string.content_mode_quran
                : "hadith".equals(mode) ? R.string.content_mode_hadith
                : "off".equals(mode) ? R.string.off : R.string.content_mode_both;
        content.setText(getString(R.string.smart_content, getString(modeLabel)));
        prayer.setText(getString(R.string.smart_prayer, state(p.getBoolean(SmartPreferences.PRAYER, false))));
        football.setText(getString(R.string.smart_football, state(p.getBoolean(SmartPreferences.FOOTBALL, false))));
        adjust.setText(getString(R.string.smart_hijri_adjust, p.getInt(SmartPreferences.HIJRI_ADJUST, 0)));
        String cityId = p.getString(SmartPreferences.CITY, "riyadh");
        int cityName = "jeddah".equals(cityId) ? R.string.city_jeddah : "makkah".equals(cityId) ? R.string.city_makkah
                : "madinah".equals(cityId) ? R.string.city_madinah : "dammam".equals(cityId) ? R.string.city_dammam : R.string.city_riyadh;
        city.setText(getString(R.string.smart_city, getString(cityName)));
        TeamCatalog.Team selected = TeamCatalog.find(p.getString(SmartPreferences.TEAM, "none"));
        team.setText(getString(R.string.smart_team, selected.name(this)));
    }
}
