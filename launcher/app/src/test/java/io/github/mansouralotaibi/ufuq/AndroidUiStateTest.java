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

}
