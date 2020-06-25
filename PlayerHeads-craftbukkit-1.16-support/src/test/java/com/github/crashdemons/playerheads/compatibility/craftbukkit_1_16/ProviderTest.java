/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads.compatibility.craftbukkit_1_16;

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
        CompatibilityProvider provider = new com.github.crashdemons.playerheads.compatibility.craftbukkit_1_16.Provider();
        EntityType expected = EntityType.ZOMBIFIED_PIGLIN;
        EntityType actual = provider.getEntityTypeFromTypename("PIG_ZOMBIE");
        assertEquals(expected,actual);
        
    }
    @Test
    public void getEntityTypeFromTypename_TestZombifiedPiglin(){
        System.out.println("getEntityTypeFromTypenameTest");
        CompatibilityProvider provider = new com.github.crashdemons.playerheads.compatibility.craftbukkit_1_16.Provider();
        EntityType expected = EntityType.ZOMBIFIED_PIGLIN;
        EntityType actual = provider.getEntityTypeFromTypename("ZOMBIFIED_PIGLIN");
        assertEquals(expected,actual);
    }
}
