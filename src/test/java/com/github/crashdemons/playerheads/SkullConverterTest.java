/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import com.github.crashdemons.playerheads.backports.FutureMaterial;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.junit.Test;
import org.junit.Ignore;
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
        TexturedSkullType expResult = TexturedSkullType.SKELETON;
        TexturedSkullType result = SkullConverter.skullTypeFromEntityType(EntityType.SKELETON);
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void testIsPlayerHead_Player() {
        System.out.println("isPlayerHead Player");
        FutureMaterial mat = FutureMaterial.PLAYER_HEAD;
        boolean expResult = true;
        boolean result = SkullConverter.isPlayerHead(mat);
    }
    @Test
    public void testIsPlayerHead_PlayerWall() {
        System.out.println("isPlayerHead PlayerWall");
        FutureMaterial mat = FutureMaterial.PLAYER_WALL_HEAD;
        boolean expResult = true;
        boolean result = SkullConverter.isPlayerHead(mat);
    }
    @Test
    public void testIsPlayerHead_Mob() {
        System.out.println("isPlayerHead Mob");
        FutureMaterial mat = FutureMaterial.SKELETON_SKULL;
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
    /*
    @Test
    public void testEntityTypeFromSkullType_Mob_1_13() {
        System.out.println("entityTypeFromSkullType Mob 1.13");
        TexturedSkullType skullType = TexturedSkullType.TROPICAL_FISH;
        EntityType expResult = EntityType.TROPICAL_FISH;
        EntityType result = SkullConverter.entityTypeFromSkullType(skullType);
        assertEquals(expResult, result);
    }*/
    
    @Test
    public void testSkullTypeFromBlockState_NotAHead(){
        System.out.println("testSkullTypeFromBlockState not a head");
        BlockState state = Mocks.getMockBlockState_Stone();
        assertEquals(SkullConverter.skullTypeFromBlockState(state),null);
    }
    
    @Ignore
    @Test
    public void testSkullTypeFromBlockState_NullPlayerhead(){
        System.out.println("testSkullTypeFromBlockState null playerhead");
        BlockState state = Mocks.getMockBlockState_PHead(null);
        assertEquals(SkullConverter.skullTypeFromBlockState(state),TexturedSkullType.PLAYER);
    }
    
    @Ignore
    @Test
    public void testSkullTypeFromBlockState_UnknownPlayerhead(){
        System.out.println("testSkullTypeFromBlockState unknown playerhead");
        OfflinePlayer op = Mocks.getMockOfflinePlayer("3437cf83-c9b0-4709-a686-b8632b8d6172", "crashdemons");
        BlockState state = Mocks.getMockBlockState_PHead(op);
        assertEquals(SkullConverter.skullTypeFromBlockState(state),TexturedSkullType.PLAYER);
    }
    @Ignore
    @Test
    public void testSkullTypeFromBlockState_SupportedPlayerhead(){
        System.out.println("testSkullTypeFromBlockState supported playerhead");
        String id = TexturedSkullType.ENDERMITE.getOwner().toString();
        OfflinePlayer op = Mocks.getMockOfflinePlayer(id, null);
        BlockState state = Mocks.getMockBlockState_PHead(op);
        assertEquals(SkullConverter.skullTypeFromBlockState(state),TexturedSkullType.ENDERMITE);
    }
    
    @Ignore
    @Test
    public void testSkullTypeFromBlockState_VanillaHead(){
        System.out.println("testSkullTypeFromBlockState not a head");
        BlockState state = Mocks.getMockBlockState_Skull();
        assertEquals(SkullConverter.skullTypeFromBlockState(state),TexturedSkullType.SKELETON);
    }
    
    @Ignore
    @Test
    public void testSkullTypeFromBlockStateLegacy_NullPlayerhead(){
        System.out.println("testSkullTypeFromBlockStateLegacy null playerhead");
        BlockState state = Mocks.getMockBlockState_PHead(null);
        assertEquals(SkullConverter.skullTypeFromBlockStateLegacy(state),TexturedSkullType.PLAYER);
    }
    
    @Ignore
    @Test
    public void testSkullTypeFromBlockStateLegacy_UnknownPlayerhead(){
        System.out.println("testSkullTypeFromBlockStateLegacy unknown playerhead");
        OfflinePlayer op = Mocks.getMockOfflinePlayer("3437cf83-c9b0-4709-a686-b8632b8d6172", "crashdemons");
        BlockState state = Mocks.getMockBlockState_PHead(op);
        assertEquals(SkullConverter.skullTypeFromBlockStateLegacy(state),TexturedSkullType.PLAYER);
    }
    
    @Ignore
    @Test
    public void testSkullTypeFromBlockStateLegacy_SupportedLegacyPlayerhead(){
        System.out.println("testSkullTypeFromBlockStateLegacy supported legacy playerhead");
        OfflinePlayer op = Mocks.getMockOfflinePlayer("1bee9df5-4f71-42a2-bf52-d97970d3fea3", "MHF_Ocelot");
        BlockState state = Mocks.getMockBlockState_PHead(op);
        assertEquals(SkullConverter.skullTypeFromBlockStateLegacy(state),TexturedSkullType.OCELOT);
    }
    
}
