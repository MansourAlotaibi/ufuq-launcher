package io.github.mansouralotaibi.ufuq;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import java.io.File;
import java.util.List;
import java.util.Locale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import static org.junit.Assert.*;
@RunWith(org.robolectric.RobolectricTestRunner.class)
@org.robolectric.annotation.Config(sdk={19,28,34},application=android.app.Application.class)
public class PhotographicWallpapersTest {
 @Test public void photographsAreAvailableOfflineAtBounded4kSizeAndLocalized() throws Exception {
  Context c=RuntimeEnvironment.getApplication();
  String[] files={"utah-dunes.jpg","peyto-lake.jpg","lofoten-evening.jpg","bled-blue-hour.jpg"};
  List<File> collection=BundledWallpapers.collection(c,null);
  for(String name:files){
   File match=null;for(File f:collection)if(f.getName().equals(name))match=f;
   assertNotNull(name,match);assertTrue(match.length()>0);
   BitmapFactory.Options o=new BitmapFactory.Options();o.inJustDecodeBounds=true;BitmapFactory.decodeFile(match.getPath(),o);
   assertEquals(3840,o.outWidth);assertEquals(2160,o.outHeight);assertTrue(WallpaperSize.accepted(o.outWidth,o.outHeight));
   Configuration en=new Configuration(c.getResources().getConfiguration());en.setLocale(Locale.ENGLISH);
   Configuration ar=new Configuration(en);ar.setLocale(new Locale("ar"));
   String english=WallpaperNames.label(c.createConfigurationContext(en),name),arabic=WallpaperNames.label(c.createConfigurationContext(ar),name);
   assertNotEquals(name,english);assertNotEquals(english,arabic);
  }
  assertEquals(9,collection.size());
 }
 @Test public void existingChoiceAndIntervalSurviveCatalogExpansion(){
  Context c=RuntimeEnvironment.getApplication();
  c.getSharedPreferences(Config.SETTINGS_NAME,0).edit().putBoolean(Config.WALLPAPER_RANDOM_KEY,false).putString(Config.WALLPAPER_CHOICE_KEY,"moraine-lake.jpg").putInt(Config.ROTATION_MINUTES_KEY,30).commit();
  assertEquals("moraine-lake.jpg",Config.load(c).wallpapers.get(0).getName());assertEquals(30,Config.load(c).wallpaperRotationMinutes);
 }
}
