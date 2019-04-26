/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import com.github.crashdemons.playerheads.testutils.TestOutput;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class RuntimeReferencesTest {
    
    private final TestOutput out=new TestOutput(this);
    public RuntimeReferencesTest() {
    }

    @Test
    public void testGetCompatibleMaterialByName_invalid() {
        out.println("getCompatibleMaterialByName invalid");
        assertEquals(null, RuntimeReferences.getCompatibleMaterialByName("EGG"));
    }
    @Test
    public void testGetCompatibleMaterialByName_valid() {
        out.println("getCompatibleMaterialByName valid");
        assertEquals("CREEPER", RuntimeReferences.getCompatibleMaterialByName("CREEPER").name());
    }

    @Test
    public void testGetMaterialByName_invalid() {
        out.println("getMaterialByName invalid");
        assertEquals(null, RuntimeReferences.getMaterialByName("120usahbbd"));
    }
    @Test
    public void testGetMaterialByName_valid() {
        out.println("getMaterialByName valid");
        assertEquals("STONE", RuntimeReferences.getMaterialByName("STONE").name());
    }

    @Test
    public void testGetSkullTypeByName_valid() {
        out.println("getSkullTypeByName valid");
        assertEquals("PLAYER", RuntimeReferences.getSkullTypeByName("PLAYER").name());
    }

    @Test
    public void testGetSkullTypeByName_invalid() {
        out.println("getSkullTypeByName invalid");
        assertEquals(null, RuntimeReferences.getSkullTypeByName("3das9d8as"));
    }

    @Test
    public void testHasClass_invalid() {
        out.println("hasClass invalid");
        assertEquals(false,RuntimeReferences.hasClass("s0d0239uds2d.ClassNameHere"));
    }
    @Test
    public void testHasClass_valid() {
        out.println("hasClass valid");
        assertEquals(true,RuntimeReferences.hasClass("org.shininet.bukkit.playerheads.events.PlayerDropHeadEvent"));
    }
}
