package io.github.mansouralotaibi.ufuq;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.text.BidiFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** A remote-first Wi-Fi picker that stays inside Home. Scanning starts only while this dialog is visible. */
final class WifiPanel {
    private final Activity activity;
    private final WifiManager wifi;
    private final float density;
    private final List<ScanResult> networks = new ArrayList<>();
    private final NetworkAdapter adapter = new NetworkAdapter();
    private Dialog dialog;
    private TextView status;
    private ListView list;
    private boolean receiverRegistered;
    private boolean passwordMode;

    WifiPanel(Activity activity) {
        this.activity = activity;
        this.wifi = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.density = activity.getResources().getDisplayMetrics().density;
    }

    static final int LOCATION_REQUEST = 218;

    static boolean needsLocation(Activity activity) {
        return android.os.Build.VERSION.SDK_INT >= 23 && android.os.Build.VERSION.SDK_INT < 29
                && androidx.core.content.ContextCompat.checkSelfPermission(activity,
                   android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED;
    }

    static void systemSettings(Activity activity) {
        Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
        try {
            if (intent.resolveActivity(activity.getPackageManager()) != null) activity.startActivity(intent);
            else Toast.makeText(activity, R.string.not_available, Toast.LENGTH_LONG).show();
        } catch (android.content.ActivityNotFoundException | SecurityException unavailable) {
            Toast.makeText(activity, R.string.not_available, Toast.LENGTH_LONG).show();
        }
    }

    void show() {
        if (!DeviceCapabilities.legacyWifi(android.os.Build.VERSION.SDK_INT)) {
            Toast.makeText(activity, R.string.ufuq_wifi_system_required, Toast.LENGTH_LONG).show();
            systemSettings(activity);
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= 23 && needsLocation(activity)) {
            activity.requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        if (wifi == null) {
            Toast.makeText(activity, R.string.not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(content());
        UiTheme.apply(dialog.getWindow().getDecorView());
        dialog.setOnDismissListener(d -> unregister());
        dialog.setOnKeyListener((d, key, event) -> {
            if (passwordMode && key == android.view.KeyEvent.KEYCODE_BACK
                    && event.getAction() == android.view.KeyEvent.ACTION_UP) {
                passwordMode = false;
                dialog.setContentView(content());
                UiTheme.apply(dialog.getWindow().getDecorView());
                loadResults(null);
                return true;
            }
            return false;
        });
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams lp = window.getAttributes();
            int screenW = activity.getResources().getDisplayMetrics().widthPixels;
            int screenH = activity.getResources().getDisplayMetrics().heightPixels;
            // A side sheet keeps Home spatially present and gives long SSIDs a predictable reading column.
            lp.width = Math.min(dp(570), Math.max(dp(240), screenW - dp(32)));
            lp.height = Math.min(dp(610), Math.max(dp(180), screenH - dp(32)));
            lp.dimAmount = 0.58f;
            window.setAttributes(lp);
            window.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        register();
        dialog.show();
        UiTheme.apply(dialog.getWindow().getDecorView());
        refresh();
    }

    private View content() {
        LinearLayout panel = new LinearLayout(activity);
        panel.setLayoutDirection(activity.getResources().getConfiguration().locale.getLanguage().equals("ar")
                ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(dp(28), dp(24), dp(28), dp(22));
        panel.setBackground(roundStroke(ThemePalette.SURFACES[UiTheme.selected(activity)], 26, (UiTheme.accent(activity) & 0x00FFFFFF) | 0x55000000));

        LinearLayout header = new LinearLayout(activity);
        header.setGravity(Gravity.CENTER_VERTICAL);
        TextView title = text(activity.getString(R.string.wifi_panel_title), 25, Color.WHITE);
        header.addView(title, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        panel.addView(header);
        Button settings = new Button(activity);
        settings.setText(R.string.ufuq_wifi_system_settings);
        settings.setOnClickListener(view -> systemSettings(activity));
        panel.addView(settings, new LinearLayout.LayoutParams(-1, -2));

        status = text("", 15, 0xCCFFFFFF);
        LinearLayout.LayoutParams statusLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        statusLp.topMargin = dp(8);
        statusLp.bottomMargin = dp(16);
        panel.addView(status, statusLp);

        list = new ListView(activity);
        list.setId(View.generateViewId());
        list.setDividerHeight(dp(6));
        list.setDivider(round(Color.TRANSPARENT, 0));
        list.setSelector(roundStroke(0x403B2D28, 16, UiTheme.accent(activity)));
        list.setAdapter(adapter);
        // Refresh is item zero in the same ListView as the networks. This is intentional: several KitKat TV
        // builds trap DPAD_UP inside ListView and never hand focus to a separate header button.
        list.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) refresh();
            else choose(networks.get(position - 1));
        });
        panel.addView(list, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        return panel;
    }

    private void refresh() {
        updateStatus(activity.getString(R.string.wifi_scanning));
        boolean started;
        try { started = wifi.startScan(); } catch (SecurityException e) { started = false; }
        if (!started) loadResults(activity.getString(R.string.wifi_scan_cached));
    }

    private void register() {
        if (receiverRegistered) return;
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        activity.registerReceiver(receiver, filter);
        receiverRegistered = true;
    }

    private void unregister() {
        if (!receiverRegistered) return;
        try { activity.unregisterReceiver(receiver); } catch (IllegalArgumentException ignored) { }
        receiverRegistered = false;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) loadResults(null);
            else updateStatus(null);
        }
    };

    @android.annotation.SuppressLint("MissingPermission")
    private void loadResults(String note) {
        List<ScanResult> scanned;
        try { scanned = wifi.getScanResults(); } catch (SecurityException e) { scanned = Collections.emptyList(); }
        networks.clear();
        Set<String> seen = new HashSet<>();
        if (scanned != null) for (ScanResult result : scanned) {
            if (result == null || TextUtils.isEmpty(result.SSID) || !seen.add(result.SSID)) continue;
            networks.add(result);
        }
        Collections.sort(networks, new Comparator<ScanResult>() {
            @Override public int compare(ScanResult a, ScanResult b) { return b.level - a.level; }
        });
        adapter.notifyDataSetChanged();
        updateStatus(note);
        if (!list.hasFocus()) list.post(() -> {
            list.requestFocus();
            // Start on the connected network when present; Up always reaches Refresh in one press.
            String current = cleanSsid(wifi.getConnectionInfo() == null ? null : wifi.getConnectionInfo().getSSID());
            int selected = 0;
            for (int i = 0; i < networks.size(); i++) if (networks.get(i).SSID.equals(current)) selected = i + 1;
            list.setSelection(selected);
        });
    }

    private void updateStatus(String note) {
        if (status == null) return;
        WifiInfo info = wifi.getConnectionInfo();
        String ssid = cleanSsid(info == null ? null : info.getSSID());
        String line = TextUtils.isEmpty(ssid) || "<unknown ssid>".equalsIgnoreCase(ssid)
                ? activity.getString(R.string.wifi_disconnected)
                : activity.getString(R.string.wifi_connected_to,
                        BidiFormatter.getInstance().unicodeWrap(ssid), signal(info.getRssi()));
        if (!TextUtils.isEmpty(note)) line += "\n" + note;
        status.setText(line);
    }

    static boolean supportedSecurity(String capabilities) {
        String c = capabilities == null ? "" : capabilities;
        return !(c.contains("EAP") || c.contains("SAE") || c.contains("OWE") || c.contains("SUITE_B"));
    }

    private void choose(ScanResult result) {
        if (!supportedSecurity(result.capabilities)) {
            Toast.makeText(activity, R.string.ufuq_wifi_security_system, Toast.LENGTH_LONG).show();
            systemSettings(activity);
            return;
        }
        WifiInfo current = wifi.getConnectionInfo();
        if (sameNetwork(result.SSID, current == null ? null : current.getSSID())) {
            updateStatus(activity.getString(R.string.wifi_already_connected));
            return;
        }
        WifiConfiguration saved = saved(result.SSID);
        if (saved != null) {
            connect(saved.networkId, result.SSID);
            return;
        }
        if (security(result).equals(activity.getString(R.string.wifi_open))) {
            WifiConfiguration open = baseConfig(result.SSID);
            open.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            addAndConnect(open, result.SSID);
            return;
        }
        askPassword(result);
    }

    private void askPassword(ScanResult result) {
        passwordMode = true;
        dialog.setContentView(passwordContent(result));
        UiTheme.apply(dialog.getWindow().getDecorView());
    }

    /** Password entry replaces the network list inside the same window, avoiding stacked KitKat dialogs. */
    private View passwordContent(ScanResult result) {
        LinearLayout panel = new LinearLayout(activity);
        panel.setLayoutDirection(activity.getResources().getConfiguration().locale.getLanguage().equals("ar")
                ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(dp(30), dp(26), dp(30), dp(26));
        panel.setBackground(round(ThemePalette.SURFACES[UiTheme.selected(activity)], 24));

        TextView title = text(BidiFormatter.getInstance().unicodeWrap(result.SSID), 26, Color.WHITE);
        panel.addView(title, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(46)));
        TextView message = text(activity.getString(R.string.wifi_password_message), 16, 0xCCFFFFFF);
        LinearLayout.LayoutParams messageLp = new LinearLayout.LayoutParams(-1, -2);
        messageLp.bottomMargin = dp(20);
        panel.addView(message, messageLp);

        EditText password = new EditText(activity);
        password.setSingleLine(true);
        password.setTextColor(Color.WHITE);
        password.setHintTextColor(0x88FFFFFF);
        password.setHint(R.string.wifi_password_hint);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password.setBackground(round(0x22FFFFFF, 12));
        password.setPadding(dp(18), 0, dp(18), 0);
        panel.addView(password, new LinearLayout.LayoutParams(-1, dp(58)));

        LinearLayout actions = new LinearLayout(activity);
        actions.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams actionsLp = new LinearLayout.LayoutParams(-1, dp(66));
        actionsLp.topMargin = dp(18);
        panel.addView(actions, actionsLp);
        Button cancel = new Button(activity);
        cancel.setText(R.string.wifi_cancel);
        cancel.setTextColor(Color.WHITE);
        cancel.setBackground(round(0x20FFFFFF, 20));
        cancel.setOnClickListener(v -> {
            password.setText("");
            passwordMode = false;
            dialog.setContentView(content());
            UiTheme.apply(dialog.getWindow().getDecorView());
            loadResults(null);
        });
        actions.addView(cancel, new LinearLayout.LayoutParams(dp(120), dp(48)));
        Button connect = new Button(activity);
        connect.setText(R.string.wifi_connect);
        connect.setTextColor(Color.BLACK);
        connect.setBackground(round(0xFFF1F1F3, 20));
        LinearLayout.LayoutParams connectLp = new LinearLayout.LayoutParams(dp(120), dp(48));
        connectLp.setMargins(dp(12), 0, 0, 0);
        actions.addView(connect, connectLp);
        connect.setOnClickListener(v -> {
            String secret = password.getText().toString();
            if (secret.length() < 8) {
                password.setError(activity.getString(R.string.wifi_password_too_short));
                return;
            }
            WifiConfiguration config = baseConfig(result.SSID);
            config.preSharedKey = quote(secret);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            password.setText("");
            passwordMode = false;
            dialog.setContentView(content());
            UiTheme.apply(dialog.getWindow().getDecorView());
            addAndConnect(config, result.SSID);
        });
        password.requestFocus();
        password.post(() -> {
            InputMethodManager input = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (input != null) input.showSoftInput(password, InputMethodManager.SHOW_IMPLICIT);
        });
        return panel;
    }

    private void addAndConnect(WifiConfiguration config, String ssid) {
        int id;
        try { id = wifi.addNetwork(config); } catch (SecurityException e) { id = -1; }
        if (id < 0) {
            updateStatus(activity.getString(R.string.wifi_add_failed));
            return;
        }
        connect(id, ssid);
    }

    private void connect(int networkId, String ssid) {
        updateStatus(activity.getString(R.string.wifi_connecting, ssid));
        boolean enabled;
        try { enabled = wifi.enableNetwork(networkId, true); } catch (SecurityException e) { enabled = false; }
        if (!enabled) updateStatus(activity.getString(R.string.wifi_connect_failed));
    }

    @android.annotation.SuppressLint("MissingPermission")
    private WifiConfiguration saved(String ssid) {
        List<WifiConfiguration> saved;
        try { saved = wifi.getConfiguredNetworks(); } catch (SecurityException e) { return null; }
        if (saved == null) return null;
        for (WifiConfiguration config : saved)
            if (ssid.equals(cleanSsid(config.SSID))) return config;
        return null;
    }

    private static WifiConfiguration baseConfig(String ssid) {
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = quote(ssid);
        return config;
    }

    private static String quote(String text) { return "\"" + text + "\""; }
    private static String cleanSsid(String text) {
        if (text == null) return "";
        return text.length() > 1 && text.charAt(0) == '"' && text.charAt(text.length() - 1) == '"'
                ? text.substring(1, text.length() - 1) : text;
    }
    static boolean sameNetwork(String selected, String currentSsid) {
        return selected != null && !selected.isEmpty() && selected.equals(cleanSsid(currentSsid));
    }
    private String security(ScanResult result) {
        String c = result.capabilities == null ? "" : result.capabilities;
        if (c.contains("WEP")) return "WEP";
        if (c.contains("PSK")) return "WPA/WPA2";
        if (c.contains("EAP")) return "Enterprise";
        return activity.getString(R.string.wifi_open);
    }
    private String signal(int rssi) {
        int level = WifiManager.calculateSignalLevel(rssi, 4);
        return activity.getString(level >= 3 ? R.string.wifi_signal_strong
                : level == 2 ? R.string.wifi_signal_good
                : level == 1 ? R.string.wifi_signal_weak : R.string.wifi_signal_very_weak);
    }

    private final class NetworkAdapter extends BaseAdapter {
        @Override public int getCount() { return networks.size() + 1; }
        @Override public Object getItem(int position) { return position == 0 ? null : networks.get(position - 1); }
        @Override public long getItemId(int position) {
            if (position == 0) return Long.MIN_VALUE;
            ScanResult result = networks.get(position - 1);
            return result.BSSID == null ? position : result.BSSID.hashCode();
        }
        @Override public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout row = convertView instanceof LinearLayout ? (LinearLayout) convertView : new LinearLayout(activity);
            row.removeAllViews();
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(dp(20), dp(8), dp(20), dp(8));
            if (position == 0) {
                TextView refresh = text(activity.getString(R.string.wifi_refresh), 17, Color.WHITE);
                refresh.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_popup_sync, 0);
                refresh.setCompoundDrawablePadding(dp(12));
                row.addView(refresh, new LinearLayout.LayoutParams(-1, dp(36)));
                row.setBackground(roundStroke(0x183B2D28, 16, 0x22F2B25B));
                row.setFocusable(false);
                row.setClickable(false);
                return row;
            }
            ScanResult result = networks.get(position - 1);
            String current = cleanSsid(wifi.getConnectionInfo() == null ? null : wifi.getConnectionInfo().getSSID());
            boolean connected = result.SSID.equals(current);
            WifiSignalView bars = new WifiSignalView(activity);
            bars.bind(WifiManager.calculateSignalLevel(result.level, 4), connected);
            LinearLayout.LayoutParams barsLp = new LinearLayout.LayoutParams(dp(34), dp(44));
            // End is the text-facing side in both directions: right in English, left in Arabic.
            barsLp.setMarginEnd(dp(16));
            row.addView(bars, barsLp);
            LinearLayout copy = new LinearLayout(activity);
            copy.setOrientation(LinearLayout.VERTICAL);
            TextView name = text(BidiFormatter.getInstance().unicodeWrap(result.SSID), 17, Color.WHITE);
            name.setTextDirection(View.TEXT_DIRECTION_FIRST_STRONG_LTR);
            copy.addView(name);
            String detail = security(result) + "  •  " + signal(result.level)
                    + (connected ? "  •  " + activity.getString(R.string.wifi_connected)
                    : saved(result.SSID) != null ? "  •  " + activity.getString(R.string.wifi_saved) : "");
            TextView meta = text(detail, 13, 0xAFFFFFFF);
            meta.setTextDirection(View.TEXT_DIRECTION_LOCALE);
            copy.addView(meta);
            row.addView(copy, new LinearLayout.LayoutParams(0, -2, 1));
            // The ListView owns remote focus and dispatches DPAD_CENTER through OnItemClickListener.
            // Focusable child rows consume ENTER on KitKat without ever invoking that listener.
            row.setFocusable(false);
            row.setClickable(false);
            row.setBackground(roundStroke(connected ? 0x283B2D28 : 0x0FFFFFFF, 16,
                    connected ? 0x80F2B25B : 0x14FFFFFF));
            return row;
        }
    }

    private TextView text(String value, float size, int color) {
        TextView text = new TextView(activity);
        text.setText(value);
        text.setTextSize(size);
        text.setTextColor(color);
        text.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        text.setTypeface(UiFont.get(activity, R.string.font_regular));
        return text;
    }
    private GradientDrawable round(int color, float radiusDp) {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(color);
        bg.setCornerRadius(dp(radiusDp));
        return bg;
    }
    private GradientDrawable roundStroke(int color, float radiusDp, int stroke) {
        GradientDrawable bg = round(color, radiusDp);
        bg.setStroke(dp(1), stroke);
        return bg;
    }
    private int dp(float value) { return Math.round(value * density); }
}
