/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import com.github.crashdemons.playerheads.testutils.TestOutput;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class CompatibleSkullMaterialTest {
    private final TestOutput out=new TestOutput(this);
    
    public CompatibleSkullMaterialTest() {
    }
    
    @Test
    public void testEntityNameCompatibility(){
        out.println("EntityNameCompatibility");
        for(CompatibleSkullMaterial csm : CompatibleSkullMaterial.values()){
            EntityType et = EntityType.valueOf( csm.name() );
            out.println("   "+csm.name()+" => "+et.name());
        }
    }
    @Test
    public void testSkullTypeCompatibility(){
        out.println("SkullTypeCompatibility");
        assertEquals( SkullType.values().length, CompatibleSkullMaterial.values().length );
        assertEquals( org.bukkit.SkullType.values().length, CompatibleSkullMaterial.values().length );
    }
    
    
/*

    @Test
    public void testGetDetails() {
        out.println("getDetails");
        CompatibleSkullMaterial instance = null;
        SkullDetails expResult = null;
        SkullDetails result = instance.getDetails();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsSupported() {
        out.println("isSupported");
        CompatibleSkullMaterial instance = null;
        boolean expResult = false;
        boolean result = instance.isSupported();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGet_SkullType() {
        out.println("get");
        SkullType type = null;
        CompatibleSkullMaterial expResult = null;
        CompatibleSkullMaterial result = CompatibleSkullMaterial.get(type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGet_ItemStack() {
        out.println("get");
        ItemStack stack = null;
        CompatibleSkullMaterial expResult = null;
        CompatibleSkullMaterial result = CompatibleSkullMaterial.get(stack);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGet_BlockState() {
        out.println("get");
        BlockState state = null;
        CompatibleSkullMaterial expResult = null;
        CompatibleSkullMaterial result = CompatibleSkullMaterial.get(state);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    */
}
