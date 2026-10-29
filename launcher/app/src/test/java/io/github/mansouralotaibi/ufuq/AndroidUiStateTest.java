package io.github.mansouralotaibi.ufuq;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.KeyEvent;
import java.util.List;
import java.io.File;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import static org.junit.Assert.*;

/** Android framework simulation, not emulator or physical-remote certification. */
@RunWith(RobolectricTestRunner.class)
@org.robolectric.annotation.Config(sdk = {19, 28, 34}, application = Application.class, qualifiers = "en-rUS-land-xhdpi")
public class AndroidUiStateTest {
 @Test public void allPalettesPersistAcrossSettingsRecreationWithoutChangingWallpaper() {
  Context c=RuntimeEnvironment.getApplication();
  c.getSharedPreferences(Config.SETTINGS_NAME,0).edit()
    .putBoolean(Config.WALLPAPER_RANDOM_KEY,false).putString(Config.WALLPAPER_CHOICE_KEY,"silver-peaks.png").commit();
  for(int i=0;i<3;i++) {
   UiTheme.select(c,i);
   try(ActivityController<LauncherSettingsActivity> activity=Robolectric.buildActivity(LauncherSettingsActivity.class).setup()) {
    assertEquals(i,UiTheme.selected(activity.get()));
    assertNotNull(activity.get().findViewById(R.id.paletteChoice));
   }
   assertEquals("silver-peaks.png",c.getSharedPreferences(Config.SETTINGS_NAME,0).getString(Config.WALLPAPER_CHOICE_KEY,null));
   assertFalse(c.getSharedPreferences(Config.SETTINGS_NAME,0).getBoolean(Config.WALLPAPER_RANDOM_KEY,true));
  }
 }
 @Test public void bundledArtWorksWithoutExternalImagesAndFixedRandomChoicesPersist() {
  Context c=RuntimeEnvironment.getApplication();
  List<File> art=BundledWallpapers.collection(c,null); assertEquals(3,art.size());
  for(File file:art)assertTrue(file.length()>0);
  c.getSharedPreferences(Config.SETTINGS_NAME,0).edit().clear().putBoolean(Config.WALLPAPER_RANDOM_KEY,true).commit();
  Config random=Config.load(c); assertTrue(random.wallpaperEnabled);assertEquals(3,random.wallpapers.size());
  c.getSharedPreferences(Config.SETTINGS_NAME,0).edit().putBoolean(Config.WALLPAPER_RANDOM_KEY,false)
   .putString(Config.WALLPAPER_CHOICE_KEY,"turquoise-horizon.png").commit();
  Config fixed=Config.load(c);assertEquals(1,fixed.wallpapers.size());assertEquals("turquoise-horizon.png",fixed.wallpaper.getName());
 }
 @Test public void englishAndArabicSettingsSupportDirectionalFocus() {
  Context c=RuntimeEnvironment.getApplication();
  for(String language:new String[]{"en","ar"}) {
   c.getSharedPreferences(Config.SETTINGS_NAME,0).edit().putString(LocalePreferences.KEY,language).commit();
   try(ActivityController<LauncherSettingsActivity> controller=Robolectric.buildActivity(LauncherSettingsActivity.class).setup()) {
    LauncherSettingsActivity a=controller.get();
    assertEquals(language,a.getResources().getConfiguration().locale.getLanguage());
    assertEquals(language.equals("ar") ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR,
        a.getResources().getConfiguration().getLayoutDirection());
    View decor=a.getWindow().getDecorView();
    decor.measure(View.MeasureSpec.makeMeasureSpec(1920,View.MeasureSpec.EXACTLY),View.MeasureSpec.makeMeasureSpec(1080,View.MeasureSpec.EXACTLY));
    decor.layout(0,0,1920,1080);
    View palette=a.findViewById(R.id.paletteChoice);assertTrue(palette.requestFocus());
    View down=palette.focusSearch(View.FOCUS_DOWN);assertNotNull(down);assertTrue(down.isFocusable());
    a.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_DOWN));
    a.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DPAD_DOWN));
    assertSame(down,decor.findFocus());
    assertTrue(a.findViewById(R.id.languageChoice).isFocusable());
   }
  }
 }
 @Test public void localeLayoutsContainAllRequiredHomeViews() {
  Context base=RuntimeEnvironment.getApplication();
  for(String language:new String[]{"en","ar","he"}) {
   android.content.res.Configuration cfg=new android.content.res.Configuration(base.getResources().getConfiguration());
   cfg.setLocale(new java.util.Locale(language)); cfg.setLayoutDirection(new java.util.Locale(language));
   Context localized=base.createConfigurationContext(cfg);
   View home=android.view.LayoutInflater.from(localized).inflate(R.layout.activity_main,null);
   for(int id:new int[]{R.id.ufuqGreeting,R.id.cinematicClock,R.id.cinematicAmPm,R.id.ufuqHijri,
      R.id.ufuqGregorian,R.id.reflectionHost,R.id.smartCards,R.id.grid,R.id.btnWifi,R.id.btnSettings})
    assertNotNull("Missing required Home ID in locale "+language,home.findViewById(id));
  }
 }

 @Test public void favoritesAndPersonalizedFeatureSettingsSurviveReopening() {
  Context c=RuntimeEnvironment.getApplication();
  File file=new File(c.getFilesDir(),Order.FILE_NAME);
  java.util.List<String> favorites=java.util.Arrays.asList("example.one/.Home","example.two/.Home");
  assertTrue(Order.save(file,new Order("",favorites,2)));
  SmartPreferences.get(c).edit().putString(SmartPreferences.GREETING_NAME,"Ufuq")
   .putBoolean(SmartPreferences.PRAYER,true).putBoolean(SmartPreferences.FOOTBALL,true).commit();
  try(ActivityController<LauncherSettingsActivity> ignored=Robolectric.buildActivity(LauncherSettingsActivity.class).setup()) {
   assertEquals(favorites,Order.load(file).keys);
   assertEquals("Ufuq",SmartPreferences.get(c).getString(SmartPreferences.GREETING_NAME,""));
   assertTrue(SmartPreferences.enabled(SmartPreferences.get(c),SmartPreferences.PRAYER));
   assertTrue(SmartPreferences.enabled(SmartPreferences.get(c),SmartPreferences.FOOTBALL));
  }
 }

 @Test public void cinematicDefaultsExposeLargeShelfAndIndependentWallpaperControls() {
  Context c=RuntimeEnvironment.getApplication();
  Config config=Config.load(c);
  assertTrue(config.cinematicHome); assertTrue(config.appLibrary); assertEquals(0,config.cinematicArt);
  assertEquals(130,AppAdapter.DEFAULT_SHELF_SCALE);
  try(ActivityController<LauncherSettingsActivity> a=Robolectric.buildActivity(LauncherSettingsActivity.class).setup()) {
   assertEquals(View.VISIBLE,a.get().findViewById(R.id.shelfSizeChoices).getVisibility());
   assertTrue(a.get().findViewById(R.id.shelfLarge).isSelected());
   assertTrue(a.get().findViewById(R.id.wallpaperDimChoice).isFocusable());
   assertTrue(a.get().findViewById(R.id.rotationIntervalChoice).isFocusable());
  }
 }
 @Test public void userDimmingAndIntervalSurviveThemeChangesAndFixedWallpaper() {
  Context c=RuntimeEnvironment.getApplication();
  c.getSharedPreferences(Config.SETTINGS_NAME,0).edit()
   .putInt(Config.WALLPAPER_DIM_KEY,45).putInt(Config.ROTATION_MINUTES_KEY,1)
   .putBoolean(Config.WALLPAPER_RANDOM_KEY,false).putString(Config.WALLPAPER_CHOICE_KEY,"silver-peaks.png").commit();
  for(int i=0;i<3;i++) {
   UiTheme.select(c,i);
   Config config=Config.load(c);assertEquals(45,config.wallpaperDim);assertEquals(1,config.wallpaperRotationMinutes);
   assertEquals(45,SceneReadability.load(c,config.wallpaperDim).dimFor(new File("silver-peaks.png")));
   assertEquals(1,config.wallpapers.size());
  }
 }
 @Test public void themeNeverAddsDashboardBackgroundToNativePrayerAndFootballViews() {
  Context c=RuntimeEnvironment.getApplication();
  for(int i=0;i<3;i++) {
   UiTheme.select(c,i);
   SmartModuleView football=new SmartModuleView(c,false);
   DayRailView prayer=new DayRailView(c,false);
   UiTheme.apply(football);UiTheme.apply(prayer);
   assertNull(football.getBackground());assertNull(prayer.getBackground());
  }
 }
 @Test public void cachedFixturesAppearDuringConstructionAndNeverCrossSelectedTeams() {
  try(ActivityController<android.app.Activity> controller=Robolectric.buildActivity(android.app.Activity.class).setup()) {
   android.app.Activity a=controller.get();a.setContentView(R.layout.activity_main);
   android.content.SharedPreferences p=SmartPreferences.get(a);
   p.edit().putBoolean(SmartPreferences.FOOTBALL,true).putString(SmartPreferences.TEAM,"136013")
    .putString("fixture_136013_raw","{\"events\":[{\"idHomeTeam\":\"136013\",\"idAwayTeam\":\"2\",\"strHomeTeam\":\"Test Hilal\",\"strAwayTeam\":\"Test Away\",\"strTimestamp\":\"2099-10-29T18:00:00\"}]}").commit();
   SmartHomeController smart=new SmartHomeController(a);
   android.view.ViewGroup modules=a.findViewById(R.id.smartCards);
   assertTrue(((android.widget.TextView)modules.getChildAt(0)).getText().toString().contains("Test Hilal"));
   smart.close();
   p.edit().putString(SmartPreferences.TEAM,"999").commit();
   smart=new SmartHomeController(a);
   assertFalse(((android.widget.TextView)modules.getChildAt(0)).getText().toString().contains("Test Hilal"));
   smart.close();
  }
 }

 @Test public void homeRendersSavedGreetingAndIndependentDatesWithBothModules() {
  Context c=RuntimeEnvironment.getApplication();
  SmartPreferences.get(c).edit().putString(SmartPreferences.GREETING_NAME,"Ufuq RC2")
   .putBoolean(SmartPreferences.PRAYER,true).putBoolean(SmartPreferences.FOOTBALL,true).commit();
  for(boolean gregorian:new boolean[]{false,true})for(boolean hijri:new boolean[]{false,true}) {
   c.getSharedPreferences(Config.SETTINGS_NAME,0).edit().putBoolean(MainActivity.SHOW_DATE_KEY,gregorian).commit();
   SmartPreferences.get(c).edit().putBoolean(SmartPreferences.HIJRI,hijri).commit();
   try(ActivityController<MainActivity> controller=Robolectric.buildActivity(MainActivity.class).setup()) {
    MainActivity a=controller.get();
    assertTrue(((android.widget.TextView)a.findViewById(R.id.ufuqGreeting)).getText().toString().contains("Ufuq RC2"));
    assertEquals(View.VISIBLE,a.findViewById(R.id.cinematicClockPanel).getVisibility());
    assertEquals(gregorian?View.VISIBLE:View.GONE,a.findViewById(R.id.ufuqGregorian).getVisibility());
    assertEquals(hijri?View.VISIBLE:View.GONE,a.findViewById(R.id.ufuqHijri).getVisibility());
    assertEquals(2,((android.view.ViewGroup)a.findViewById(R.id.smartCards)).getChildCount());
    View decor=a.getWindow().getDecorView();
    decor.measure(View.MeasureSpec.makeMeasureSpec(1920,View.MeasureSpec.EXACTLY),View.MeasureSpec.makeMeasureSpec(1080,View.MeasureSpec.EXACTLY));
    decor.layout(0,0,1920,1080);
    View navigation=a.findViewById(R.id.topButtons),header=a.findViewById(R.id.homeHeader);
    assertTrue("Navigation must remain inside its touch parent's bounds",navigation.getBottom()<=header.getHeight());

   }
  }
 }

 @Test public void rc1CacheSurvivesAnotherTeamReplacingTheLegacySlot() {
  try(ActivityController<android.app.Activity> controller=Robolectric.buildActivity(android.app.Activity.class).setup()) {
   android.app.Activity a=controller.get();a.setContentView(R.layout.activity_main);
   android.content.SharedPreferences p=SmartPreferences.get(a);
   String raw="{\"events\":[{\"idHomeTeam\":\"136013\",\"idAwayTeam\":\"2\",\"strHomeTeam\":\"Legacy Hilal\",\"strAwayTeam\":\"Legacy Away\",\"strTimestamp\":\"2099-10-29T18:00:00\"}]}";
   p.edit().putBoolean(SmartPreferences.FOOTBALL,true).putString(SmartPreferences.TEAM,"136013")
    .putString("cache_football_team","136013").putString("cache_football",raw).commit();
   SmartHomeController smart=new SmartHomeController(a);smart.close();
   p.edit().putString("cache_football_team","999").putString("cache_football","{\"events\":null}").commit();
   smart=new SmartHomeController(a);
   android.view.ViewGroup modules=a.findViewById(R.id.smartCards);
   assertTrue(((android.widget.TextView)modules.getChildAt(0)).getText().toString().contains("Legacy Hilal"));
   smart.close();
  }
 }

 @Test public void slowOldTeamResponseStartsSelectedTeamRequestWithoutDisplayingOldTeam() {
  class Requests extends NativeApiClient {
   final java.util.List<String> urls=new java.util.ArrayList<>();
   final java.util.List<Callback> callbacks=new java.util.ArrayList<>();
   @Override void get(String url,Callback callback){urls.add(url);callbacks.add(callback);}
  }
  try(ActivityController<android.app.Activity> controller=Robolectric.buildActivity(android.app.Activity.class).setup()) {
   android.app.Activity a=controller.get();a.setContentView(R.layout.activity_main);
   android.content.SharedPreferences p=SmartPreferences.get(a);
   p.edit().putBoolean(SmartPreferences.FOOTBALL,true).putString(SmartPreferences.TEAM,"136013").commit();
   Requests requests=new Requests();SmartHomeController smart=new SmartHomeController(a,requests);smart.onResume();
   assertEquals(1,requests.urls.size());smart.onPause();p.edit().putString(SmartPreferences.TEAM,"999").commit();smart.onResume();
   requests.callbacks.get(0).complete(new NativeApiClient.Result("{\"events\":null}",200,null));
   assertEquals(2,requests.urls.size());assertTrue(requests.urls.get(1).endsWith("id=999"));
   String raw="{\"events\":[{\"idHomeTeam\":\"999\",\"idAwayTeam\":\"2\",\"strHomeTeam\":\"Selected Team\",\"strAwayTeam\":\"Selected Away\",\"strTimestamp\":\"2099-10-29T18:00:00\"}]}";
   requests.callbacks.get(1).complete(new NativeApiClient.Result(raw,200,null));
   android.view.ViewGroup modules=a.findViewById(R.id.smartCards);
   assertTrue(((android.widget.TextView)modules.getChildAt(0)).getText().toString().contains("Selected Team"));
   smart.close();
  }
 }

 @Test public void nativeDialogTitleUsesTheChosenPaletteAcrossSupportedFrameworks() {
  try(ActivityController<android.app.Activity> controller=Robolectric.buildActivity(android.app.Activity.class).setup()) {
   android.app.Activity a=controller.get();
   for(int i=0;i<3;i++) {
    UiTheme.select(a,i);
    android.app.AlertDialog d=new ThemedDialogBuilder(a).setTitle("Ufuq").setPositiveButton(android.R.string.ok,null).show();
    int title=a.getResources().getIdentifier("alertTitle","id","android");
    android.widget.TextView view=d.findViewById(title);assertNotNull(view);
    assertEquals(ThemePalette.ACCENTS[i],view.getCurrentTextColor());d.dismiss();
   }
  }
 }

}
