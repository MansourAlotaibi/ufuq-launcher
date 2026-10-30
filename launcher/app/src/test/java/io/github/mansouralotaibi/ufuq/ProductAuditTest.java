package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.graphics.Paint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.util.ReflectionHelpers;
import static org.junit.Assert.*;

@RunWith(org.robolectric.RobolectricTestRunner.class)
@org.robolectric.annotation.Config(sdk={19,28,34},application=android.app.Application.class,qualifiers="en-rUS-land-xhdpi")
public class ProductAuditTest {
 @Test public void exactResponseLimitSucceedsAndOversizeNeverBecomesPartialSuccess() throws Exception {
  byte[] exact=new byte[262144];java.util.Arrays.fill(exact,(byte)'x');
  assertEquals(262144,NativeApiClient.readBounded(new java.io.ByteArrayInputStream(exact)).length());
  try {NativeApiClient.readBounded(new java.io.ByteArrayInputStream(new byte[262145]));fail("Oversized body must fail");} catch(java.io.IOException expected) { }
 }
 @Test public void prayerCityDateAndMarkerAreIndependentOfDeviceZoneAtMidnight() {
  java.util.TimeZone before=java.util.TimeZone.getDefault();java.time.Instant now=java.time.Instant.parse("2026-10-29T22:30:00Z");
  try {for(String zone:new String[]{"UTC","Asia/Riyadh","Europe/London","America/Los_Angeles"}) {
   java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone(zone));assertEquals(java.time.LocalDate.of(2026,10,30),PrayerCityTime.date(now));assertEquals(90,PrayerCityTime.minute(now));
  }}finally{java.util.TimeZone.setDefault(before);}
 }
 @Test public void fixedWallpaperSurvivesRandomRoundTripAndDependenciesCannotFocus() {
  Context c=RuntimeEnvironment.getApplication();c.getSharedPreferences(Config.SETTINGS_NAME,0).edit().putBoolean(Config.WALLPAPER_RANDOM_KEY,false).putString(Config.WALLPAPER_CHOICE_KEY,"moraine-lake.jpg").putInt(Config.ROTATION_MINUTES_KEY,30).putBoolean("animations",false).commit();
  try(ActivityController<LauncherSettingsActivity> a=Robolectric.buildActivity(LauncherSettingsActivity.class).setup()){
   assertFalse(a.get().findViewById(R.id.advancedChoice).isFocusable());a.get().findViewById(R.id.wallpaperModeChoice).performClick();
   assertFalse(a.get().findViewById(R.id.wallpaperChoice).isFocusable());assertTrue(a.get().findViewById(R.id.rotationIntervalChoice).isFocusable());
   a.get().findViewById(R.id.wallpaperModeChoice).performClick();assertEquals("moraine-lake.jpg",Config.load(a.get()).wallpapers.get(0).getName());assertEquals(30,Config.load(a.get()).wallpaperRotationMinutes);
  }
 }
 @Test public void standaloneWallpaperAndNoneSurviveModeRoundTrip() throws Exception {
  Context c=RuntimeEnvironment.getApplication();java.io.File dir=Config.dir(c);assertNotNull(dir);dir.mkdirs();java.io.File single=new java.io.File(dir,Config.WALLPAPER_NAME);try(java.io.FileOutputStream out=new java.io.FileOutputStream(single)){out.write(1);}
  for(String name:new String[]{Config.WALLPAPER_NAME,"none"}) {
   c.getSharedPreferences(Config.SETTINGS_NAME,0).edit().putBoolean(Config.WALLPAPER_RANDOM_KEY,false).putString(Config.WALLPAPER_CHOICE_KEY,name).commit();
   try(ActivityController<LauncherSettingsActivity> a=Robolectric.buildActivity(LauncherSettingsActivity.class).setup()){
    a.get().findViewById(R.id.wallpaperModeChoice).performClick();a.get().findViewById(R.id.wallpaperModeChoice).performClick();assertEquals(name,c.getSharedPreferences(Config.SETTINGS_NAME,0).getString(Config.WALLPAPER_CHOICE_KEY,null));
   }
  }
  single.delete();
 }
 @Test public void focusRingDiffersFromSavedSelectionInEveryPalette() {
  Context c=RuntimeEnvironment.getApplication();for(int i=0;i<3;i++){
   UiTheme.select(c,i);TextView view=new TextView(c);view.setFocusable(true);view.setClickable(true);UiTheme.apply(view);
   android.graphics.drawable.StateListDrawable bg=(android.graphics.drawable.StateListDrawable)view.getBackground();
   bg.setState(new int[]{android.R.attr.state_selected});Paint saved=ReflectionHelpers.getField(bg.getCurrent(),"mStrokePaint");float selected=saved.getStrokeWidth();
   bg.setState(new int[]{android.R.attr.state_selected,android.R.attr.state_focused});Paint focus=ReflectionHelpers.getField(bg.getCurrent(),"mStrokePaint");assertTrue(focus.getStrokeWidth()>selected);assertEquals(0xFFF7FBFF,focus.getColor());
   int overlay=UiTheme.restingPillColor(c);int color=androidx.core.graphics.ColorUtils.compositeColors(overlay,0xFFFFFFFF);assertTrue(androidx.core.graphics.ColorUtils.calculateContrast(0xFFF5F5F5,color)>=4.5);
  }
 }
 @Test public void wifiDialogAndReceiverCloseWithOwningActivity() {
  try(ActivityController<MainActivity> a=Robolectric.buildActivity(MainActivity.class).setup().visible()){
   MainActivity home=a.get();ReflectionHelpers.callInstanceMethod(home,"showWifiPanel");WifiPanel panel=ReflectionHelpers.getField(home,"wifiPanel");assertNotNull(panel);a.pause().stop().destroy();assertFalse(ReflectionHelpers.<Boolean>getField(panel,"receiverRegistered"));assertNull(ReflectionHelpers.getField(panel,"dialog"));
  }
 }
 @Test public void fullAppsHintsMatchOpenAndHoldActionsWithoutChangingSelectionMode() throws Exception {
  try(ActivityController<MainActivity> a=Robolectric.buildActivity(MainActivity.class).setup().visible()){
   MainActivity home=a.get();AppLibraryDialog full=new AppLibraryDialog(home,false,true,false,p->false,()->{},()->{});
   String add=ReflectionHelpers.callInstanceMethod(full,"hint",ReflectionHelpers.ClassParameter.from(boolean.class,false));String remove=ReflectionHelpers.callInstanceMethod(full,"hint",ReflectionHelpers.ClassParameter.from(boolean.class,true));assertEquals(home.getString(R.string.library_hold_add),add);assertEquals(home.getString(R.string.library_hold_remove),remove);full.close();
  }
 }
 @Test @org.robolectric.annotation.Config(sdk={19,28}) public void wifiScanUpdateKeepsHeaderFocusAndSelectedNetworkIdentity() {
  try(ActivityController<MainActivity> a=Robolectric.buildActivity(MainActivity.class).setup().visible()) {
   MainActivity home=a.get();org.robolectric.Shadows.shadowOf((android.app.Application)home.getApplicationContext()).grantPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION);android.net.wifi.WifiManager wifi=(android.net.wifi.WifiManager)home.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
   android.net.wifi.ScanResult first=ReflectionHelpers.callConstructor(android.net.wifi.ScanResult.class);first.SSID="ExampleA";first.level=-40;
   android.net.wifi.ScanResult second=ReflectionHelpers.callConstructor(android.net.wifi.ScanResult.class);second.SSID="ExampleB";second.level=-60;
   org.robolectric.Shadows.shadowOf(wifi).setScanResults(java.util.Arrays.asList(first,second));
   ReflectionHelpers.callInstanceMethod(home,"showWifiPanel");WifiPanel panel=ReflectionHelpers.getField(home,"wifiPanel");org.robolectric.shadows.ShadowLooper.idleMainLooper();
   android.app.Dialog dialog=ReflectionHelpers.getField(panel,"dialog");android.widget.ListView list=ReflectionHelpers.getField(panel,"list");
   android.widget.Button header=findButton(dialog.getWindow().getDecorView());assertNotNull(header);header.requestFocusFromTouch();assertTrue(header.hasFocus());
   ReflectionHelpers.callInstanceMethod(panel,"loadResults",ReflectionHelpers.ClassParameter.from(String.class,null));org.robolectric.shadows.ShadowLooper.idleMainLooper();assertTrue("Scan must not steal header focus",header.hasFocus());
   list.requestFocusFromTouch();list.setSelection(2);second.level=-20;
   ReflectionHelpers.callInstanceMethod(panel,"loadResults",ReflectionHelpers.ClassParameter.from(String.class,null));org.robolectric.shadows.ShadowLooper.idleMainLooper();
   java.util.List<android.net.wifi.ScanResult> results=ReflectionHelpers.getField(panel,"networks");assertEquals("ExampleB",results.get(list.getSelectedItemPosition()-1).SSID);panel.close();
  }
 }
 private static android.widget.Button findButton(View v) {
  if(v instanceof android.widget.Button)return (android.widget.Button)v;
  if(v instanceof android.view.ViewGroup){android.view.ViewGroup g=(android.view.ViewGroup)v;for(int i=0;i<g.getChildCount();i++){android.widget.Button b=findButton(g.getChildAt(i));if(b!=null)return b;}}
  return null;
 }

}
