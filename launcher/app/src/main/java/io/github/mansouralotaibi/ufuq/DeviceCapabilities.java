package io.github.mansouralotaibi.ufuq;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
/** Generic probes; vendor integrations remain separate and opt-in. No model-name allowlist. */
final class DeviceCapabilities {
 static boolean legacyWifi(int api) { return api >= 19 && api <= 28; }
 static boolean wifiSettings(Context c) {
  return c.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI)
   && new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS).resolveActivity(c.getPackageManager()) != null;
 }
 static boolean homeSettings(Context c) {
  return new Intent(android.provider.Settings.ACTION_HOME_SETTINGS).resolveActivity(c.getPackageManager()) != null;
 }
 static boolean tvInputs(Context c) { return Build.VERSION.SDK_INT >= 21; }
 static boolean dpad(Context c) {
  return c.getResources().getConfiguration().navigation == android.content.res.Configuration.NAVIGATION_DPAD;
 }
 static boolean granted(Context c, String permission) {
  return androidx.core.content.ContextCompat.checkSelfPermission(c, permission) == PackageManager.PERMISSION_GRANTED;
 }
 private DeviceCapabilities() {}
}
