package io.github.mansouralotaibi.ufuq;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WifiPanelTest {
    @Test public void connectedSsidMatchesAndroidsQuotedValue() {
        assertTrue(WifiPanel.sameNetwork("Living room", "\"Living room\""));
    }

    @Test public void anotherOrUnknownNetworkDoesNotMatch() {
        assertFalse(WifiPanel.sameNetwork("Guest", "\"Living room\""));
        assertFalse(WifiPanel.sameNetwork("Guest", null));
        assertFalse(WifiPanel.sameNetwork("", "\"Living room\""));
    }
    @Test public void enterpriseAndModernSecurityUseSystemSettings() {
        assertFalse(WifiPanel.supportedSecurity("[WEP][ESS]"));
        assertFalse(WifiPanel.supportedSecurity("[WPA2-EAP-CCMP][ESS]"));
        assertFalse(WifiPanel.supportedSecurity("[SAE][ESS]"));
        assertFalse(WifiPanel.supportedSecurity("[OWE][ESS]"));
        assertTrue(WifiPanel.supportedSecurity("[WPA2-PSK-CCMP][ESS]"));
        assertTrue(WifiPanel.supportedSecurity("[ESS]"));
    }
    @Test public void legacyConfigurationIsLimitedToSupportedAndroidVersions() {
        assertFalse(DeviceCapabilities.legacyWifi(18));
        assertTrue(DeviceCapabilities.legacyWifi(19));
        assertTrue(DeviceCapabilities.legacyWifi(28));
        assertFalse(DeviceCapabilities.legacyWifi(29));
        assertFalse(DeviceCapabilities.legacyWifi(34));
    }
}
