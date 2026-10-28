package io.github.mansouralotaibi.ufuq;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TeamCatalogTest {
    @Test public void catalogKeepsVerifiedIdsAndGroups() {
        assertEquals("Al Hilal", TeamCatalog.find("136013").english);
        assertEquals("Al Nassr", TeamCatalog.find("136022").english);
        assertEquals("Al Shabab", TeamCatalog.find("136020").english);
        assertEquals("Juventus", TeamCatalog.find("133676").english);
        assertEquals(8, TeamCatalog.group(TeamCatalog.SAUDI).length);
        assertEquals(9, TeamCatalog.group(TeamCatalog.INTERNATIONAL).length);
    }
}
