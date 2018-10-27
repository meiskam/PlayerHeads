/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import com.github.crashdemons.playerheads.compatibility.bukkit_1_13.Provider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.shininet.bukkit.playerheads.LegacySkullType;

/**
 *
 * @author crash
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class})
@Ignore
public class SkullConverterTest {
    
    public SkullConverterTest() {
        Provider x;
        Mocks.setupFakeServerVersion();
    }

    /*
    @Test
    public void testDropConfigFromSkullType_Player() {
        System.out.println("dropConfigFromSkullType Player");
        String expResult = "droprate";
        String result = TexturedSkullType.PLAYER.getConfigName();//SkullConverter.dropConfigFromSkullType(TexturedSkullType.PLAYER);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDropConfigFromSkullType_Mob() {
        System.out.println("dropConfigFromSkullType Player");
        String expResult = "witherskeletondroprate";
        String result = TexturedSkullType.WITHER_SKELETON.getConfigName();//SkullConverter.dropConfigFromSkullType(TexturedSkullType.WITHER_SKELETON);
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
    */
    
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
    
    @Test
    public void testSkullTypeFromBlockState_NotAHead(){
        System.out.println("testSkullTypeFromBlockState not a head");
        BlockState state = Mocks.getMockBlockState_Stone();
        assertEquals(SkullConverter.skullTypeFromBlockState(state),null);
    }
        @Test
    public void testSkullTypeFromBlockState_NullPlayerhead(){
        System.out.println("testSkullTypeFromBlockState null playerhead");
        BlockState state = Mocks.getMockBlockState_PHead(null);
        assertEquals(SkullConverter.skullTypeFromBlockState(state),TexturedSkullType.PLAYER);
    }
    @Test
    public void testSkullTypeFromBlockState_UnknownPlayerhead(){
        System.out.println("testSkullTypeFromBlockState unknown playerhead");
        OfflinePlayer op = Mocks.getMockOfflinePlayer("3437cf83-c9b0-4709-a686-b8632b8d6172", "crashdemons");
        BlockState state = Mocks.getMockBlockState_PHead(op);
        assertEquals(SkullConverter.skullTypeFromBlockState(state),TexturedSkullType.PLAYER);
    }
    @Test
    public void testSkullTypeFromBlockState_SupportedPlayerhead(){
        System.out.println("testSkullTypeFromBlockState supported playerhead");
        String id = TexturedSkullType.ENDERMITE.getOwner().toString();
        OfflinePlayer op = Mocks.getMockOfflinePlayer(id, null);
        BlockState state = Mocks.getMockBlockState_PHead(op);
        assertEquals(SkullConverter.skullTypeFromBlockState(state),TexturedSkullType.ENDERMITE);
    }
    @Test
    public void testSkullTypeFromBlockState_VanillaHead(){
        System.out.println("testSkullTypeFromBlockState not a head");
        BlockState state = Mocks.getMockBlockState_Skull();
        assertEquals(SkullConverter.skullTypeFromBlockState(state),TexturedSkullType.SKELETON);
    }
    
    @Test
    public void testSkullTypeFromBlockStateLegacy_NullPlayerhead(){
        System.out.println("testSkullTypeFromBlockStateLegacy null playerhead");
        BlockState state = Mocks.getMockBlockState_PHead(null);
        assertEquals(SkullConverter.skullTypeFromBlockStateLegacy(state),TexturedSkullType.PLAYER);
    }
    @Test
    public void testSkullTypeFromBlockStateLegacy_UnknownPlayerhead(){
        System.out.println("testSkullTypeFromBlockStateLegacy unknown playerhead");
        OfflinePlayer op = Mocks.getMockOfflinePlayer("3437cf83-c9b0-4709-a686-b8632b8d6172", "crashdemons");
        BlockState state = Mocks.getMockBlockState_PHead(op);
        assertEquals(SkullConverter.skullTypeFromBlockStateLegacy(state),TexturedSkullType.PLAYER);
    }
    @Test
    public void testSkullTypeFromBlockStateLegacy_SupportedLegacyPlayerhead(){
        System.out.println("testSkullTypeFromBlockStateLegacy supported legacy playerhead");
        OfflinePlayer op = Mocks.getMockOfflinePlayer("1bee9df5-4f71-42a2-bf52-d97970d3fea3", "MHF_Ocelot");
        BlockState state = Mocks.getMockBlockState_PHead(op);
        assertEquals(SkullConverter.skullTypeFromBlockStateLegacy(state),TexturedSkullType.OCELOT);
    }
    
}
