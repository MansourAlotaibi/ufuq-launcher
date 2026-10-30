package io.github.mansouralotaibi.ufuq;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import static org.junit.Assert.*;

@RunWith(org.robolectric.RobolectricTestRunner.class)
@org.robolectric.annotation.Config(sdk={19,28,34},application=android.app.Application.class,qualifiers="en-rUS-land-xhdpi")
public class UxRefinementTest {
 public static class ObservedHome extends MainActivity { int recreations; @Override public void recreate(){recreations++;} }
 @Test public void fixedIntervalCannotReceiveFocusOrOpenPickerAndKeepsSavedValue() {
  Context c=RuntimeEnvironment.getApplication();
  c.getSharedPreferences(Config.SETTINGS_NAME,0).edit().putBoolean(Config.WALLPAPER_RANDOM_KEY,false)
    .putInt(Config.ROTATION_MINUTES_KEY,15).commit();
  try(ActivityController<LauncherSettingsActivity> a=Robolectric.buildActivity(LauncherSettingsActivity.class).setup()) {
   View row=a.get().findViewById(R.id.rotationIntervalChoice);
   assertFalse(row.isEnabled());assertFalse(row.isFocusable());row.performClick();
   assertNull(org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog());
   a.get().findViewById(R.id.wallpaperModeChoice).performClick();
   assertTrue(row.isEnabled());assertTrue(row.isFocusable());
   assertEquals(15,Config.load(a.get()).wallpaperRotationMinutes);
  }
 }
 @Test public void fontPreferencesAreIndependentAndApprovedPurposeTypographySurvives() {
  Context base=RuntimeEnvironment.getApplication();Configuration ar=new Configuration(base.getResources().getConfiguration());ar.setLocale(new java.util.Locale("ar"));
  Context arabic=base.createConfigurationContext(ar);Configuration en=new Configuration(ar);en.setLocale(java.util.Locale.ENGLISH);Context english=base.createConfigurationContext(en);
  UiFont.select(arabic,1);assertEquals(1,UiFont.selection(arabic));assertEquals(0,UiFont.selection(english));
  UiFont.select(english,1);UiFont.select(arabic,0);assertEquals(0,UiFont.selection(arabic));assertEquals(1,UiFont.selection(english));
  assertSame(UiFont.selected(english),UiFont.selected(english));
  TextView verse=new TextView(english);verse.setTag(UiFont.KEEP_APPROVED);verse.setTypeface(Typeface.SERIF);UiFont.apply(verse);assertSame(Typeface.SERIF,verse.getTypeface());
  TextView clock=new TextView(english);clock.setId(R.id.cinematicClock);clock.setTypeface(Typeface.MONOSPACE);UiFont.apply(clock);assertSame(Typeface.MONOSPACE,clock.getTypeface());
 }
 @Test public void ordinarySettingsRetainHomeInstanceAndRootWithUpdatedGreetingAndPalette() {
  try(ActivityController<ObservedHome> c=Robolectric.buildActivity(ObservedHome.class).setup().visible().windowFocusChanged(true)) {
   MainActivity a=c.get();View root=a.findViewById(R.id.root);
   c.pause();SmartPreferences.get(a).edit().putString(SmartPreferences.GREETING_NAME,"Refinement").commit();
   UiTheme.select(a,2);c.resume();org.robolectric.shadows.ShadowLooper.idleMainLooper();
   assertSame(root,a.findViewById(R.id.root));assertEquals(0,((ObservedHome)a).recreations);
   assertTrue(((TextView)a.findViewById(R.id.ufuqGreeting)).getText().toString().contains("Refinement"));assertEquals(2,UiTheme.selected(a));
  }
 }
 @Test public void footballHasMeasuredParagraphGapsAndAllThreeSemanticLines() {
  for(String value:new String[]{"Favorite team\nHome × Away\nFri 30 Oct · 9:00 PM","الفريق المفضل\nManchester United × نادي الهلال\n٣٠ أكتوبر · ٩:٠٠ مساءً"}) {
   SmartModuleView view=new SmartModuleView(RuntimeEnvironment.getApplication(),value.startsWith("الفريق"));view.bindModule(SmartPreferences.FOOTBALL);view.setText(value);
   view.measure(View.MeasureSpec.makeMeasureSpec(506,View.MeasureSpec.EXACTLY),View.MeasureSpec.makeMeasureSpec(198,View.MeasureSpec.EXACTLY));view.layout(0,0,506,198);
   assertTrue(view.getLayout().getLineCount()>=3);
   android.text.Spanned text=(android.text.Spanned)view.getText();assertEquals(1,text.getSpans(0,text.length(),android.text.style.LineHeightSpan.class).length);
   assertTrue(view.getLayout().getLineBaseline(1)>view.getLayout().getLineBaseline(0));
   assertTrue(view.getText().toString().endsWith(value.substring(value.lastIndexOf('\n')+1)));
  }
 }
 @Test public void freshOemMenuGestureWithStickyLongFlagActivatesOnceAfterOpeningGestureIsIgnored() throws Exception {
  try(ActivityController<MainActivity> c=Robolectric.buildActivity(MainActivity.class).setup().visible().windowFocusChanged(true)) {
   MainActivity a=c.get();org.robolectric.util.ReflectionHelpers.setField(a,"lastOkDownTime",100L);
   android.widget.LinearLayout rows=new android.widget.LinearLayout(a);a.addContentView(rows,new android.view.ViewGroup.LayoutParams(400,300));
   java.util.concurrent.atomic.AtomicInteger calls=new java.util.concurrent.atomic.AtomicInteger();
   java.lang.reflect.Method add=MainActivity.class.getDeclaredMethod("addSettingsMenuRow",android.widget.LinearLayout.class,android.widget.PopupWindow.class,int.class,Runnable.class);add.setAccessible(true);
   add.invoke(a,rows,new android.widget.PopupWindow(a),R.string.tile_move,(Runnable)calls::incrementAndGet);
   View row=rows.getChildAt(0);Object info=org.robolectric.util.ReflectionHelpers.callInstanceMethod(row,"getListenerInfo");View.OnKeyListener listener=org.robolectric.util.ReflectionHelpers.getField(info,"mOnKeyListener");
   int key=android.view.KeyEvent.KEYCODE_ENTER;
   listener.onKey(row,key,new android.view.KeyEvent(100,600,0,key,2,0,0,0,android.view.KeyEvent.FLAG_LONG_PRESS));
   listener.onKey(row,key,new android.view.KeyEvent(100,650,1,key,0));org.robolectric.shadows.ShadowLooper.idleMainLooper();assertEquals(0,calls.get());
   listener.onKey(row,key,new android.view.KeyEvent(1000,1100,0,key,1,0,0,0,android.view.KeyEvent.FLAG_LONG_PRESS));
   listener.onKey(row,key,new android.view.KeyEvent(1000,1150,1,key,0));org.robolectric.shadows.ShadowLooper.idleMainLooper();assertEquals(1,calls.get());
   listener.onKey(row,key,new android.view.KeyEvent(1000,1200,1,key,0));org.robolectric.shadows.ShadowLooper.idleMainLooper();assertEquals(1,calls.get());
   // Some remotes reuse downTime; a fresh ordinary DOWN must still select once.
   listener.onKey(row,key,new android.view.KeyEvent(100,1300,0,key,0));
   listener.onKey(row,key,new android.view.KeyEvent(100,1350,1,key,0));org.robolectric.shadows.ShadowLooper.idleMainLooper();assertEquals(2,calls.get());
  }
 }
 @Test public void dynamicFontUpdatesKeepOneMetricSpanAndDoNotDisturbEditing() {
  Context c=RuntimeEnvironment.getApplication();UiFont.select(c,1);
  TextView date=new TextView(c);date.setText("Friday 30 October");UiFont.apply(date);
  for(int i=0;i<20;i++) {
   String value="Friday "+(i+1)+" October";date.setText(value);
   assertEquals(value,date.getText().toString());
   assertEquals(1,((android.text.Spanned)date.getText()).getSpans(0,date.length(),android.text.style.LineHeightSpan.class).length);
  }
  android.widget.EditText edit=new android.widget.EditText(c);edit.setText("Editable");edit.setSelection(4);Typeface original=edit.getTypeface();UiFont.apply(edit);assertSame(original,edit.getTypeface());assertEquals(4,edit.getSelectionEnd());
 }
 @Test public void legacyAmiriMigratesOnlyArabicFontAndAllFiveFamiliesRemainIndependent() {
  Context base=RuntimeEnvironment.getApplication();Configuration conf=new Configuration(base.getResources().getConfiguration());conf.setLocale(new java.util.Locale("ar"));Context ar=base.createConfigurationContext(conf);
  android.content.SharedPreferences p=base.getSharedPreferences(Config.SETTINGS_NAME,0);
  p.edit().clear().putInt("font_ar",1).putInt("font_en",1).putInt(Config.ROTATION_MINUTES_KEY,30).putString(Config.WALLPAPER_CHOICE_KEY,"desert-sunset.jpg").commit();
  assertEquals(0,UiFont.selection(ar));assertFalse(p.contains("font_ar"));assertEquals("default",p.getString(UiFont.ARABIC_KEY,null));assertEquals(1,p.getInt("font_en",0));assertEquals(30,p.getInt(Config.ROTATION_MINUTES_KEY,0));assertEquals("desert-sunset.jpg",p.getString(Config.WALLPAPER_CHOICE_KEY,null));
  assertEquals(6,UiFont.names(ar).length);assertEquals(2,UiFont.names(base).length);
  for(int i=1;i<=5;i++){UiFont.select(ar,i);assertEquals(i,UiFont.selection(ar));assertSame(UiFont.selected(ar),UiFont.selected(ar));assertEquals(1,UiFont.selection(base));assertFalse(UiFont.names(ar)[i].contains("Amiri"));}
  UiFont.select(ar,0);assertEquals(0,UiFont.selection(ar));assertEquals(1,UiFont.selection(base));
 }
 @Test public void duplicateLongPressCannotStackTileMenus() {
  try(ActivityController<MainActivity> c=Robolectric.buildActivity(MainActivity.class).setup().visible().windowFocusChanged(true)) {
   MainActivity a=c.get();AppAdapter fake=new AppAdapter(a,a,6,Config.load(a)) {
    @Override String keyAt(int i){return "example.safe/app";}
    @Override String packageAt(int i){return "example.safe";}
    @Override int movableCount(){return 2;}
   };
   org.robolectric.util.ReflectionHelpers.setField(a,"adapter",fake);
   View tile=new View(a);a.addContentView(tile,new android.view.ViewGroup.LayoutParams(300,180));tile.layout(0,700,300,880);
   a.onTileLongPress(0,tile);android.widget.PopupWindow first=org.robolectric.util.ReflectionHelpers.getField(a,"openPopup");assertNotNull(first);assertTrue(first.isShowing());
   a.onTileLongPress(0,tile);assertSame(first,org.robolectric.util.ReflectionHelpers.getField(a,"openPopup"));first.dismiss();assertFalse(first.isShowing());assertNull(org.robolectric.util.ReflectionHelpers.getField(a,"openPopup"));
  }
 }
 @Test public void horizonChildrenKeepSharedAxisInAllPalettesAndDirections() {
  Context ctx=RuntimeEnvironment.getApplication();
  for(int palette=0;palette<3;palette++)for(boolean rtl:new boolean[]{false,true}){
   UiTheme.select(ctx,palette);SmartCompositionLayout group=new SmartCompositionLayout(ctx,null);group.setLayoutDirection(rtl?View.LAYOUT_DIRECTION_RTL:View.LAYOUT_DIRECTION_LTR);
   group.addModule(SmartPreferences.PRAYER,new DayRailView(ctx,rtl));SmartModuleView football=new SmartModuleView(ctx,rtl);football.bindModule(SmartPreferences.FOOTBALL);group.addModule(SmartPreferences.FOOTBALL,football);
   group.measure(View.MeasureSpec.makeMeasureSpec(1200,View.MeasureSpec.EXACTLY),View.MeasureSpec.makeMeasureSpec(140,View.MeasureSpec.EXACTLY));group.layout(0,0,1200,140);
   assertEquals(group.getChildAt(0).getTop(),group.getChildAt(1).getTop());assertEquals(Math.round(14*ctx.getResources().getDisplayMetrics().density),HorizonStyle.axis(ctx));assertTrue(HorizonStyle.stroke(ctx,false)>0);assertEquals(0x3D,HorizonStyle.color(ctx,false)>>>24);
  }
 }

}
