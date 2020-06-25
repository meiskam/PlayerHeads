/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.craftbukkit_1_13;

import com.github.crashdemons.playerheads.compatibility.CompatibilityProvider;
import org.bukkit.entity.EntityType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class ProviderTest {
    
    public ProviderTest() {
    }

    @Test
    public void getEntityTypeFromTypename_TestLegacyPigZombie(){
        System.out.println("getEntityTypeFromTypenameTest");
        CompatibilityProvider provider = new com.github.crashdemons.playerheads.compatibility.craftbukkit_1_13.Provider();
        EntityType expected = EntityType.PIG_ZOMBIE;
        EntityType actual = provider.getEntityTypeFromTypename("PIG_ZOMBIE");
        assertEquals(expected,actual);
        
    }
    @Test
    public void getEntityTypeFromTypename_TestZombifiedPiglin(){
        System.out.println("getEntityTypeFromTypenameTest");
        CompatibilityProvider provider = new com.github.crashdemons.playerheads.compatibility.craftbukkit_1_13.Provider();
        EntityType expected = EntityType.PIG_ZOMBIE;
        EntityType actual = provider.getEntityTypeFromTypename("ZOMBIFIED_PIGLIN");
        assertEquals(expected,actual);
    }
    
}
