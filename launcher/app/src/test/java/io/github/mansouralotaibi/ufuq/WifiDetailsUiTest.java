package io.github.mansouralotaibi.ufuq;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Locale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowWifiInfo;
import org.robolectric.util.ReflectionHelpers;
import static org.junit.Assert.*;

@RunWith(org.robolectric.RobolectricTestRunner.class)
@Config(sdk={19,28}, application=android.app.Application.class, qualifiers="en-rUS-land-xhdpi")
public class WifiDetailsUiTest {
    private Button button(View root, String label) {
        if (root instanceof Button && label.contentEquals(((Button) root).getText())) return (Button) root;
        if (root instanceof ViewGroup) for (int i=0;i<((ViewGroup) root).getChildCount();i++) {
            Button found=button(((ViewGroup) root).getChildAt(i), label);
            if(found!=null) return found;
        }
        return null;
    }
    private WifiInfo connect(Activity a) throws Exception {
        Shadows.shadowOf((android.app.Application)a.getApplicationContext())
                .grantPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION);
        WifiInfo info=ShadowWifiInfo.newInstance();
        org.robolectric.shadows.ShadowWifiInfo shadow=Shadows.shadowOf(info);
        shadow.setSSID("Demo network"); shadow.setBSSID("02:00:00:00:00:01");
        shadow.setNetworkId(1); shadow.setSupplicantState(SupplicantState.COMPLETED);
        shadow.setRssi(-50); shadow.setInetAddress(InetAddress.getByAddress(new byte[]{(byte)192,0,2,1}));
        WifiManager wifi=(WifiManager)a.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Shadows.shadowOf(wifi).setConnectionInfo(info);
        ScanResult scan=new ScanResult(); scan.SSID="Demo network";scan.BSSID="02:00:00:00:00:01";
        scan.capabilities="[WPA2-PSK-CCMP][ESS]";scan.level=-50;
        Shadows.shadowOf(wifi).setScanResults(Collections.singletonList(scan));
        return info;
    }
    @Test public void detailsAndBackUseSameWindowAcrossThemesAndLanguages() throws Exception {
        for(String language:new String[]{"ar","en"}) for(int theme=0;theme<3;theme++) {
            try(ActivityController<Activity> controller=Robolectric.buildActivity(Activity.class).setup().visible()) {
                Activity a=controller.get();
                Configuration config=new Configuration(a.getResources().getConfiguration());
                config.setLocale(new Locale(language));
                a.getResources().updateConfiguration(config,a.getResources().getDisplayMetrics());
                UiTheme.select(a,theme); connect(a);
                WifiPanel panel=new WifiPanel(a);panel.show();
                Dialog dialog=ReflectionHelpers.getField(panel,"dialog");
                Button open=button(dialog.getWindow().getDecorView(),a.getString(R.string.wifi_connection_details));
                assertNotNull(open);open.performClick();
                TextView detail=ReflectionHelpers.getField(panel,"details");
                String copy=detail.getText().toString();
                assertTrue(copy.contains("Demo network"));assertTrue(copy.contains("192.0.2.1"));
                assertTrue(copy.contains("WPA/WPA2 Personal"));
                assertEquals(language.equals("ar")?View.LAYOUT_DIRECTION_RTL:View.LAYOUT_DIRECTION_LTR,
                        ((View)detail.getParent().getParent()).getLayoutDirection());
                a.sendBroadcast(new Intent(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                Shadows.shadowOf(android.os.Looper.getMainLooper()).idle();
                assertSame(detail,ReflectionHelpers.getField(panel,"details"));
                dialog.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_BACK));
                dialog.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_BACK));
                assertTrue(dialog.isShowing());assertFalse(ReflectionHelpers.<Boolean>getField(panel,"detailsMode"));
                assertSame(dialog,ReflectionHelpers.getField(panel,"dialog"));
                panel.close();assertFalse(ReflectionHelpers.<Boolean>getField(panel,"receiverRegistered"));
            }
        }
    }
    @Test public void connectedNetworkOkOpensDetailsWithoutConnectingAgain() throws Exception {
        try(ActivityController<Activity> c=Robolectric.buildActivity(Activity.class).setup().visible()) {
            Activity a=c.get();connect(a);WifiPanel panel=new WifiPanel(a);panel.show();
            a.sendBroadcast(new Intent(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            Shadows.shadowOf(android.os.Looper.getMainLooper()).idle();
            android.widget.ListView list=ReflectionHelpers.getField(panel,"list");
            assertEquals(2,list.getCount());
            list.performItemClick(list.getAdapter().getView(1,null,list),1,1);
            assertTrue(ReflectionHelpers.<Boolean>getField(panel,"detailsMode"));
            WifiManager wifi=(WifiManager)a.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            assertNull(Shadows.shadowOf(wifi).getLastEnabledNetwork());
            assertTrue(wifi.getConfiguredNetworks().isEmpty());
            panel.close();
        }
    }
    @Test public void sameNameDifferentAccessPointDoesNotInventSecurity() throws Exception {
        try(ActivityController<Activity> c=Robolectric.buildActivity(Activity.class).setup().visible()) {
            Activity a=c.get();WifiInfo info=connect(a);Shadows.shadowOf(info).setBSSID("02:00:00:00:00:02");
            WifiPanel panel=new WifiPanel(a);panel.show();Dialog d=ReflectionHelpers.getField(panel,"dialog");
            button(d.getWindow().getDecorView(),a.getString(R.string.wifi_connection_details)).performClick();
            TextView detail=ReflectionHelpers.getField(panel,"details");
            assertFalse(detail.getText().toString().contains("WPA/WPA2 Personal"));
            assertTrue(detail.getText().toString().contains(a.getString(R.string.wifi_detail_unavailable)));
            panel.close();
        }
    }
    @Test public void disconnectClearsAddressAndPermissionDenialShowsUnavailable() throws Exception {
        try(ActivityController<Activity> c=Robolectric.buildActivity(Activity.class).setup().visible()) {
            Activity a=c.get();WifiInfo info=connect(a);WifiPanel panel=new WifiPanel(a);panel.show();
            Dialog dialog=ReflectionHelpers.getField(panel,"dialog");
            button(dialog.getWindow().getDecorView(),a.getString(R.string.wifi_connection_details)).performClick();
            Shadows.shadowOf(info).setSupplicantState(SupplicantState.DISCONNECTED);
            a.sendBroadcast(new Intent(WifiManager.NETWORK_STATE_CHANGED_ACTION));
            Shadows.shadowOf(android.os.Looper.getMainLooper()).idle();
            TextView detail=ReflectionHelpers.getField(panel,"details");
            assertFalse(detail.getText().toString().contains("192.0.2.1"));
            WifiManager wifi=(WifiManager)a.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            Shadows.shadowOf(wifi).setAccessWifiStatePermission(false);
            a.sendBroadcast(new Intent(WifiManager.NETWORK_STATE_CHANGED_ACTION));
            Shadows.shadowOf(android.os.Looper.getMainLooper()).idle();
            assertTrue(detail.getText().toString().contains(a.getString(R.string.wifi_detail_unavailable)));
            panel.close();
        }
    }
}
