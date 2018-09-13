/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.junit.Test;
import static org.junit.Assert.*;
import org.shininet.bukkit.playerheads.LegacySkullType;

/**
 *
 * @author crash
 */
public class SkullConverterTest {
    
    public SkullConverterTest() {
    }

    @Test
    public void testDropConfigFromSkullType_Player() {
        System.out.println("dropConfigFromSkullType Player");
        String expResult = "droprate";
        String result = SkullConverter.dropConfigFromSkullType(TexturedSkullType.PLAYER);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDropConfigFromSkullType_Mob() {
        System.out.println("dropConfigFromSkullType Player");
        String expResult = "witherskeletondroprate";
        String result = SkullConverter.dropConfigFromSkullType(TexturedSkullType.WITHER_SKELETON);
        assertEquals(expResult, result);
    }
    

    @Test
    public void testSkullTypeFromEntityType_Unsupported() {
        System.out.println("skullTypeFromEntityType_Unsupported");
        TexturedSkullType expResult = null;
        TexturedSkullType result = SkullConverter.skullTypeFromEntityType(EntityType.ARROW);
        assertEquals(expResult, result);
    }
    @Test
    public void testSkullTypeFromEntityType_Player() {
        System.out.println("skullTypeFromEntityType_Player");
        TexturedSkullType expResult = TexturedSkullType.PLAYER;
        TexturedSkullType result = SkullConverter.skullTypeFromEntityType(EntityType.PLAYER);
        assertEquals(expResult, result);
    }
    @Test
    public void testSkullTypeFromEntityType_Mob() {
        System.out.println("skullTypeFromEntityType_Mob");
        TexturedSkullType expResult = TexturedSkullType.WITHER_SKELETON;
        TexturedSkullType result = SkullConverter.skullTypeFromEntityType(EntityType.WITHER_SKELETON);
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void testIsPlayerHead_Player() {
        System.out.println("isPlayerHead Player");
        Material mat = Material.PLAYER_HEAD;
        boolean expResult = true;
        boolean result = SkullConverter.isPlayerHead(mat);
    }
    @Test
    public void testIsPlayerHead_PlayerWall() {
        System.out.println("isPlayerHead PlayerWall");
        Material mat = Material.PLAYER_WALL_HEAD;
        boolean expResult = true;
        boolean result = SkullConverter.isPlayerHead(mat);
    }
    @Test
    public void testIsPlayerHead_Mob() {
        System.out.println("isPlayerHead Mob");
        Material mat = Material.SKELETON_SKULL;
        boolean expResult = false;
        boolean result = SkullConverter.isPlayerHead(mat);
    }
    

    @Test
    public void testUpgradeSkullTypeLegacy_Valid() {
        System.out.println("upgradeSkullTypeLegacy Valid");
        LegacySkullType oldType = LegacySkullType.IRON_GOLEM;
        TexturedSkullType expResult = TexturedSkullType.IRON_GOLEM;
        TexturedSkullType result = SkullConverter.upgradeSkullTypeLegacy(oldType);
        assertEquals(expResult, result);
    }

    @Test
    public void testEntityTypeFromSkullType_Player() {
        System.out.println("entityTypeFromSkullType Player");
        TexturedSkullType skullType = TexturedSkullType.PLAYER;
        EntityType expResult = EntityType.PLAYER;
        EntityType result = SkullConverter.entityTypeFromSkullType(skullType);
        assertEquals(expResult, result);
    }
    @Test
    public void testEntityTypeFromSkullType_Mob() {
        System.out.println("entityTypeFromSkullType Mob");
        TexturedSkullType skullType = TexturedSkullType.BLAZE;
        EntityType expResult = EntityType.BLAZE;
        EntityType result = SkullConverter.entityTypeFromSkullType(skullType);
        assertEquals(expResult, result);
    }
    @Test
    public void testEntityTypeFromSkullType_Mob_1_13() {
        System.out.println("entityTypeFromSkullType Mob 1.13");
        TexturedSkullType skullType = TexturedSkullType.TROPICAL_FISH;
        EntityType expResult = EntityType.TROPICAL_FISH;
        EntityType result = SkullConverter.entityTypeFromSkullType(skullType);
        assertEquals(expResult, result);
    }
}
