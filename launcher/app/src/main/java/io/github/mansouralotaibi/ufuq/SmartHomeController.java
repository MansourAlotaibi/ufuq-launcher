package io.github.mansouralotaibi.ufuq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Premium asymmetric Smart Home composition. No polling: one stale-aware refresh per visible session. */
final class SmartHomeController {
    private static final long PRAYER_TTL = 6 * 60 * 60 * 1000L;
    private static final long FOOTBALL_TTL = 60 * 60 * 1000L;
    private final Activity activity;
    private final SmartCompositionLayout composition;
    private final FrameLayout reflectionHost;
    private final SharedPreferences p;
    private final NativeApiClient api = new NativeApiClient();
    private final Map<String, SmartModuleView> cards = new HashMap<>();
    private final SharedPreferences.OnSharedPreferenceChangeListener cacheListener;
    private ReflectionView reflection;
    private boolean active;

    SmartHomeController(Activity activity) {
        this.activity = activity;
        composition = activity.findViewById(R.id.smartCards);
        reflectionHost = activity.findViewById(R.id.reflectionHost);
        p = SmartPreferences.get(activity);
        cacheListener = (preferences, key) -> {
            if (active && ("cache_football_team".equals(key)
                    || "cache_football_display".equals(key)
                    || "cache_football_at".equals(key))) {
                activity.runOnUiThread(() -> renderCached(SmartPreferences.FOOTBALL));
            }
        };
        p.registerOnSharedPreferenceChangeListener(cacheListener);
        build();
    }

    private boolean arabic() { return "ar".equals(activity.getResources().getConfiguration().locale.getLanguage()); }

    private void build() {
        composition.clearModules(); cards.clear(); reflectionHost.removeAllViews(); reflection = null;
        renderClockBlock();
        if (p.getBoolean(SmartPreferences.MINIMAL, false)) {
            composition.setVisibility(View.GONE);
            reflectionHost.setVisibility(View.GONE);
            wireTopFocus(R.id.grid);
            return;
        }
        composition.setLayoutDirection(arabic() ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        reflectionHost.setLayoutDirection(arabic() ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        if (SmartPreferences.enabled(p, SmartPreferences.CONTENT)) {
            reflection = new ReflectionView(activity, arabic());
            reflection.setId(R.id.smartReflection);
            reflectionHost.addView(reflection, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            reflectionHost.setVisibility(View.VISIBLE);
        } else reflectionHost.setVisibility(View.GONE);

        boolean first = true;
        // Ufuq has a deliberate information hierarchy. Legacy card-order preferences remain stored,
        // but cannot arbitrarily rearrange this integrated composition.
        String[] ufuqOrder = {SmartPreferences.PRAYER, SmartPreferences.FOOTBALL};
        for (String key : ufuqOrder) {
            if (!SmartPreferences.enabled(p, key)) continue;
            SmartModuleView card = SmartPreferences.PRAYER.equals(key)
                    ? new DayRailView(activity, arabic()) : new SmartModuleView(activity, arabic());
            card.bindModule(key);
            if (first) { card.setId(R.id.smartCardFirst); first = false; }
            else card.setId(View.generateViewId());
            card.setOnClickListener(v -> activity.startActivity(new Intent(activity, SmartSettingsActivity.class)));
            composition.addModule(key, card); cards.put(key, card);
        }
        composition.setVisibility(cards.isEmpty() ? View.GONE : View.VISIBLE);
        int lower = cards.isEmpty() ? R.id.grid : R.id.smartCardFirst;
        if (reflection != null) {
            reflection.setNextFocusUpId(R.id.btnWifi);
            reflection.setNextFocusDownId(lower);
            reflection.setNextFocusLeftId(R.id.smartReflection);
            reflection.setNextFocusRightId(R.id.smartReflection);
            composition.setModuleFocusUpId(R.id.smartReflection);
        } else composition.setModuleFocusUpId(R.id.btnWifi);
        int down = reflection == null ? lower : R.id.smartReflection;
        wireTopFocus(down);
        renderOffline();
    }

    private void wireTopFocus(int down) {
        int[] top = {R.id.btnHomeTab, R.id.btnAppsTab, R.id.btnInputsTab, R.id.btnInputs,
                R.id.btnWifi, R.id.btnReorder, R.id.btnLibraryTop, R.id.btnSettings};
        for (int id : top) { View v = activity.findViewById(id); if (v != null) v.setNextFocusDownId(down); }
    }

    private void renderClockBlock() {
        TextView greeting = activity.findViewById(R.id.ufuqGreeting);
        TextView hijri = activity.findViewById(R.id.ufuqHijri);
        TextView gregorian = activity.findViewById(R.id.ufuqGregorian);
        if (greeting == null || hijri == null || gregorian == null) return;
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int greetingId = hour < 12 ? R.string.good_morning
                : hour < 17 ? R.string.good_afternoon
                : hour < 21 ? R.string.good_evening : R.string.good_night;
        String name = p.getString(SmartPreferences.GREETING_NAME,
                activity.getString(R.string.greeting_name)).trim();
        greeting.setText(name.length() == 0 ? activity.getString(greetingId)
                : activity.getString(greetingId) + (arabic() ? "، " : ", ") + name);
        hijri.setText(HijriDateFormatter.format(LocalDate.now(),
                p.getInt(SmartPreferences.HIJRI_ADJUST, 0), arabic()));
        Locale locale = arabic() ? new Locale("ar") : Locale.ENGLISH;
        String date = new SimpleDateFormat(arabic() ? "EEEE، d MMMM" : "EEEE d MMMM", locale)
                .format(new Date());
        gregorian.setText(NumeralStyle.apply(date, NumeralStyle.current(activity)));
        int gravity = arabic() ? Gravity.RIGHT : Gravity.LEFT;
        greeting.setGravity(gravity); hijri.setGravity(gravity); gregorian.setGravity(gravity);
    }

    void onResume() {
        active = true; api.setActive(true); build();
        if (cards.containsKey(SmartPreferences.FOOTBALL)) refreshFootball();
    }

    void onPause() { active = false; api.setActive(false); }
    void close() {
        p.unregisterOnSharedPreferenceChangeListener(cacheListener);
        api.close();
    }

    boolean focusFirstCard() {
        if (composition.getVisibility() == View.VISIBLE && composition.focusEntry()) return true;
        return reflection != null && reflection.requestFocus();
    }

    boolean isSmartMode() { return !p.getBoolean(SmartPreferences.MINIMAL, false); }

    private void renderOffline() {
        if (reflection != null) {
            String mode = p.getString(SmartPreferences.CONTENT_MODE, "both");
            DailyContent.Entry entry = DailyContent.today(activity, mode);

            reflection.setEntry(entry, title(R.string.card_content_title),
                    activity.getString(R.string.content_more));
            DailyContent.Entry fullEntry = entry;
            reflection.setOnClickListener(v -> showContentDetails(reflection, fullEntry));
        }
        renderOfflinePrayer();
        renderCached(SmartPreferences.FOOTBALL);
    }

    private String title(int id) { return activity.getString(id); }
    private String cacheBody(String key) { return p.getString("cache_" + key + "_display", null); }
    private long cacheAt(String key) { return p.getLong("cache_" + key + "_at", 0); }
    private void save(String key, String raw, String display) {
        p.edit().putString("cache_" + key, raw).putString("cache_" + key + "_display", display)
                .putLong("cache_" + key + "_at", System.currentTimeMillis()).apply();
    }
    private void renderCached(String key) {
        SmartModuleView card = cards.get(key); if (card == null) return;
        String value = SmartPreferences.FOOTBALL.equals(key) ? footballFromRaw() : cacheBody(key);
        if (value == null) card.setText(cardTitle(key) + "\n" + activity.getString(R.string.card_unavailable));
        else if (SmartPreferences.FOOTBALL.equals(key)) card.setText(cardTitle(key) + "\n" + value);
        else card.setText(cardTitle(key) + "\n" + value + "\n" + updated(cacheAt(key)));
        if (SmartPreferences.FOOTBALL.equals(key)) card.setOnClickListener(v -> showFootballDetails(card));
    }
    private String updated(long at) {
        return activity.getString(R.string.card_updated, LauncherTimeFormat.timestamp(activity, at));
    }
    private String cardTitle(String key) {
        return title(SmartPreferences.PRAYER.equals(key) ? R.string.card_prayer_title : R.string.card_football_title);
    }
    private boolean fresh(String key, long ttl) { return System.currentTimeMillis() - cacheAt(key) < ttl; }

    private void renderOfflinePrayer() {
        SmartModuleView card = cards.get(SmartPreferences.PRAYER); if (card == null) return;
        City city = City.selected(p.getString(SmartPreferences.CITY, "riyadh"));
        PrayerCalculator.Times t=PrayerCalculator.calculate(LocalDate.now(),city.lat,city.lon,3);
        if (card instanceof DayRailView) {
            ((DayRailView) card).setPrayerTimes(t);
            return;
        }
        int[] values={t.fajr,t.dhuhr,t.asr,t.maghrib,t.isha}; String[] en={"Fajr","Dhuhr","Asr","Maghrib","Isha"};
        String[] ar={"الفجر","الظهر","العصر","المغرب","العشاء"};
        Calendar now=Calendar.getInstance(); int minute=now.get(Calendar.HOUR_OF_DAY)*60+now.get(Calendar.MINUTE);
        int pick=0,value=1440+t.fajr; for(int i=0;i<values.length;i++)if(values[i]>minute){pick=i;value=values[i];break;}
        int left=value-minute; String prayerTime = LauncherTimeFormat.minute(activity, values[pick], arabic());
        String timing=arabic()
                ? prayerTime+" · "+arabicDuration(left)
                : "\u200E"+prayerTime+" · "
                        +NumeralStyle.apply(englishDuration(left),NumeralStyle.LATIN)+"\u200E";
        String display=(arabic()?ar[pick]:en[pick])+" · "+timing
                +"\n"+(arabic()?"حساب أم القرى · دون اتصال":"Umm al-Qura calculation · offline");
        card.setText(cardTitle(SmartPreferences.PRAYER)+"\n"+display);
    }

    private void refreshFootball() {
        String id = p.getString(SmartPreferences.TEAM, "none");
        if ("none".equals(id)) return;
        if (id.equals(cachedFootballTeam()) && fresh(SmartPreferences.FOOTBALL, FOOTBALL_TTL)) {
            // A request owned by the previous Activity can finish while Home is being recreated after
            // leaving settings. The cache is then fresh, but this Activity built its view just before the
            // write. Repaint here instead of returning with the pre-request placeholder still visible.
            renderCached(SmartPreferences.FOOTBALL);
            return;
        }
        // Foreground-only requests: hourly successful cache plus a global one-minute retry floor.
        long now = System.currentTimeMillis();
        long last = p.getLong("football_last_attempt", 0);
        if (last > 0 && now >= last && now - last < 60_000L) return;
        p.edit().putLong("football_last_attempt", now).apply();
        String endpoint = "https://www.thesportsdb.com/api/v1/json/123/eventsnext.php?id=" + Uri.encode(id);
        api.get(endpoint, response -> {
            String selectedNow = p.getString(SmartPreferences.TEAM, "none");
            if (!id.equals(selectedNow)) {
                android.util.Log.i("MansourFootball", "team=" + id
                        + " status=DISCARDED_STALE_RESPONSE selected=" + selectedNow);
                return;
            }
            if (!response.successful()) {
                android.util.Log.w("MansourFootball", "team=" + id + " http=" + response.httpCode
                        + " error=" + response.error);
                if (active) renderFootballTransient(response.error != null ? R.string.card_fixture_network_error
                        : R.string.card_fixture_server_error);
                return;
            }
            FootballFixtureParser.Result parsed = FootballFixtureParser.parse(response.body, id,
                    ZoneId.systemDefault(), ZonedDateTime.now());
            android.util.Log.i("MansourFootball", "team=" + id + " status=" + parsed.status);
            if (parsed.status == FootballFixtureParser.Status.OK) {
                saveFootball(id, response.body, fixtureDisplay(parsed.fixture));
            } else if (parsed.status == FootballFixtureParser.Status.EMPTY) {
                saveFootball(id, response.body, activity.getString(R.string.card_fixture_free_empty));
            } else if (parsed.status == FootballFixtureParser.Status.PAST_ONLY) {
                saveFootball(id, response.body, activity.getString(R.string.card_fixture_past_only));
            } else {
                if (active) renderFootballTransient(R.string.card_fixture_invalid);
                return;
            }
            if (active) renderCached(SmartPreferences.FOOTBALL);
        });
    }

    private String footballFromRaw() {
        String selected = p.getString(SmartPreferences.TEAM, "none");
        if (!selected.equals(cachedFootballTeam())) return null;
        String raw = p.getString("cache_" + SmartPreferences.FOOTBALL, null);
        if (raw == null) return cacheBody(SmartPreferences.FOOTBALL);
        FootballFixtureParser.Result parsed = FootballFixtureParser.parse(raw, selected,
                ZoneId.systemDefault(), ZonedDateTime.now());
        return parsed.status == FootballFixtureParser.Status.OK
                ? fixtureDisplay(parsed.fixture) : cacheBody(SmartPreferences.FOOTBALL);
    }

    private String cachedFootballTeam() {
        return p.getString("cache_" + SmartPreferences.FOOTBALL + "_team", "none");
    }

    private void saveFootball(String teamId, String raw, String display) {
        p.edit().putString("cache_" + SmartPreferences.FOOTBALL, raw)
                .putString("cache_" + SmartPreferences.FOOTBALL + "_display", display)
                .putString("cache_" + SmartPreferences.FOOTBALL + "_team", teamId)
                .putLong("cache_" + SmartPreferences.FOOTBALL + "_at", System.currentTimeMillis()).commit();
    }

    private void renderFootballTransient(int message) {
        SmartModuleView card = cards.get(SmartPreferences.FOOTBALL);
        if (card == null) return;
        String cached = footballFromRaw();
        card.setText(cardTitle(SmartPreferences.FOOTBALL) + "\n"
                + (cached == null ? activity.getString(message) : cached + "\n"
                + activity.getString(R.string.card_refresh_failed)));
    }

    private String fixtureDisplay(FootballFixtureParser.Fixture fixture) {
        String home = TeamCatalog.localizedName(activity, fixture.home);
        String away = TeamCatalog.localizedName(activity, fixture.away);
        return fixturePreview(home, away) + "\n" + fixtureDate(fixture.date) + " · "
                + LauncherTimeFormat.minute(activity, fixture.minute, arabic());
    }
    private String fixtureDetails(FootballFixtureParser.Fixture fixture) {
        String score = fixture.score.length() == 0 ? "" : "\n" + digits(fixture.score);
        return fixtureDisplay(fixture).replace("\n", "\n\n") + score + "\n\nTheSportsDB · "
                + updated(cacheAt(SmartPreferences.FOOTBALL));
    }
    private String fixtureDate(String isoDate) {
        try {
            Locale locale = arabic() ? new Locale("ar") : Locale.ENGLISH;
            String pattern = arabic() ? "d MMMM" : "EEE d MMM";
            return digits(LocalDate.parse(isoDate).format(DateTimeFormatter.ofPattern(pattern, locale)));
        } catch (Exception ignored) {
            return digits(isoDate);
        }
    }
    private String englishDuration(int minute) { return minute>=60 ? (minute/60)+"h "+(minute%60)+"m" : minute+"m"; }
    private String arabicDuration(int minute) { return digits(minute>=60 ? (minute/60)+" س "+(minute%60)+" د" : minute+" د"); }
    private String digits(String value) { return NumeralStyle.apply(value, NumeralStyle.current(activity)); }
    private String fixturePreview(String home, String away) {
        String full = home + " × " + away;
        if (full.length() <= 38) return full;
        return full.substring(0, 35).trim() + "…";
    }

    private void showContentDetails(View returnTo, DailyContent.Entry entry) {
        AlertDialog dialog = new ThemedDialogBuilder(activity).setTitle(R.string.card_content_title)
                .setMessage(entry.details())
                .setPositiveButton(android.R.string.ok, null).create();
        dialog.setOnDismissListener(ignored -> returnTo.requestFocus());
        dialog.show();
        UiTheme.apply(dialog.getWindow().getDecorView());
        android.widget.TextView messageView = dialog.findViewById(android.R.id.message);
        if (messageView != null) {
            android.text.util.Linkify.addLinks(messageView, android.text.util.Linkify.WEB_URLS);
            messageView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
        }
    }

    private void showFootballDetails(View returnTo) {
        String raw = p.getString("cache_" + SmartPreferences.FOOTBALL, null);
        String selected = p.getString(SmartPreferences.TEAM, "none");
        FootballFixtureParser.Result parsed = raw == null ? null : FootballFixtureParser.parse(raw,
                selected, ZoneId.systemDefault(), ZonedDateTime.now());
        String message = parsed != null && parsed.status == FootballFixtureParser.Status.OK
                ? fixtureDetails(parsed.fixture)
                : cacheBody(SmartPreferences.FOOTBALL);
        if (message == null) message = activity.getString(R.string.card_no_fixture);
        AlertDialog dialog = new ThemedDialogBuilder(activity).setTitle(R.string.card_football_title)
                .setMessage(message + "\n\n" + activity.getString(R.string.ufuq_football_attribution)).setPositiveButton(android.R.string.ok, null).create();
        dialog.setOnDismissListener(ignored -> returnTo.requestFocus());
        dialog.show();
        UiTheme.apply(dialog.getWindow().getDecorView());
        android.widget.TextView messageView = dialog.findViewById(android.R.id.message);
        if (messageView != null) {
            android.text.util.Linkify.addLinks(messageView, android.text.util.Linkify.WEB_URLS);
            messageView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
        }
    }
    private static final class City {
        final String id; final double lat,lon; final int name;
        City(String id,double lat,double lon,int name){this.id=id;this.lat=lat;this.lon=lon;this.name=name;}
        String name(Activity a){return a.getString(name);}
        static City selected(String id){
            if("jeddah".equals(id))return new City(id,21.5433,39.1728,R.string.city_jeddah);
            if("makkah".equals(id))return new City(id,21.3891,39.8579,R.string.city_makkah);
            if("madinah".equals(id))return new City(id,24.5247,39.5692,R.string.city_madinah);
            if("dammam".equals(id))return new City(id,26.4207,50.0888,R.string.city_dammam);
            return new City("riyadh",24.7136,46.6753,R.string.city_riyadh);
        }
    }
}
