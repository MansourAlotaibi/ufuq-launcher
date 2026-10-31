package io.github.mansouralotaibi.ufuq;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
    @Test public void ipv4UsesAndroidByteOrderAndMissingAddressStaysMissing() {
        assertEquals("", WifiPanel.ipv4(0));
        // RFC 5737 documentation address, not a device address.
        assertEquals("192.0.2.1", WifiPanel.ipv4(0x010200C0));
        assertEquals("203.0.113.255", WifiPanel.ipv4(0xFF7100CB));
    }
    @Test public void advertisedModernSecurityIsNeverMislabelledOpen() {
        assertEquals("WPA2/WPA3 Personal", WifiPanel.securityName("[WPA2-PSK+SAE-CCMP][ESS]"));
        assertEquals("WPA3 Personal", WifiPanel.securityName("[RSN-SAE-CCMP][ESS]"));
        assertEquals("Enhanced Open (OWE)", WifiPanel.securityName("[OWE][ESS]"));
        assertEquals("WPA3 Enterprise", WifiPanel.securityName("[RSN-EAP_SUITE_B_192][ESS]"));
        assertEquals("WPA/WPA2 Enterprise", WifiPanel.securityName("[WPA2-EAP-CCMP][ESS]"));
        assertEquals("", WifiPanel.securityName("[ESS]"));
        assertNull(WifiPanel.securityName(null));
        assertNull(WifiPanel.securityName("[UNRECOGNIZED][ESS]"));
    }
    @Test public void legacyConfigurationIsLimitedToSupportedAndroidVersions() {
        assertFalse(DeviceCapabilities.legacyWifi(18));
        assertTrue(DeviceCapabilities.legacyWifi(19));
        assertTrue(DeviceCapabilities.legacyWifi(28));
        assertFalse(DeviceCapabilities.legacyWifi(29));
        assertFalse(DeviceCapabilities.legacyWifi(34));
    }
}
