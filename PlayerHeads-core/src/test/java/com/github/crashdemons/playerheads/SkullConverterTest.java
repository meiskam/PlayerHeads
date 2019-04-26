/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

//import com.github.crashdemons.playerheads.compatibility.craftbukkit_1_13.Provider;
import com.github.crashdemons.playerheads.testutils.Mocks;
import com.github.crashdemons.playerheads.testutils.TestOutput;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.EntityType;
import org.junit.Test;
import static org.junit.Assert.*;
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
public class SkullConverterTest {
    
    private final TestOutput out=new TestOutput(this);
    public SkullConverterTest() {
        //Provider x;
        Mocks.setupFakeServerSupport();
    }

    @Test
    public void testDropConfigFromSkullType_Player() {
        out.println("dropConfigFromSkullType Player");
        String expResult = "droprate";
        String result = TexturedSkullType.PLAYER.getConfigName();//SkullConverter.dropConfigFromSkullType(TexturedSkullType.PLAYER);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDropConfigFromSkullType_Mob() {
        out.println("dropConfigFromSkullType Player");
        String expResult = "witherskeletondroprate";
        String result = TexturedSkullType.WITHER_SKELETON.getConfigName();//SkullConverter.dropConfigFromSkullType(TexturedSkullType.WITHER_SKELETON);
        assertEquals(expResult, result);
    }
    

    /*
    @Test
    public void testSkullTypeFromEntityType_Unsupported() {
        out.println("skullTypeFromEntityType_Unsupported");
        TexturedSkullType expResult = null;
        TexturedSkullType result = SkullConverter.skullTypeFromEntityType(EntityType.ARROW);
        assertEquals(expResult, result);
    }
    @Test
    public void testSkullTypeFromEntityType_Player() {
        out.println("skullTypeFromEntityType_Player");
        TexturedSkullType expResult = TexturedSkullType.PLAYER;
        TexturedSkullType result = SkullConverter.skullTypeFromEntityType(EntityType.PLAYER);
        assertEquals(expResult, result);
    }
    @Test
    public void testSkullTypeFromEntityType_Mob() {
        out.println("skullTypeFromEntityType_Mob");
        TexturedSkullType expResult = TexturedSkullType.WITHER_SKELETON;
        TexturedSkullType result = SkullConverter.skullTypeFromEntityType(EntityType.WITHER_SKELETON);
        assertEquals(expResult, result);
    }
    */
    
    @Test
    public void testUpgradeSkullTypeLegacy_Valid() {
        out.println("upgradeSkullTypeLegacy Valid");
        LegacySkullType oldType = LegacySkullType.IRON_GOLEM;
        TexturedSkullType expResult = TexturedSkullType.IRON_GOLEM;
        TexturedSkullType result = SkullConverter.upgradeSkullTypeLegacy(oldType);
        assertEquals(expResult, result);
    }

    @Test
    public void testEntityTypeFromSkullType_Player() {
        out.println("entityTypeFromSkullType Player");
        TexturedSkullType skullType = TexturedSkullType.PLAYER;
        EntityType expResult = EntityType.PLAYER;
        EntityType result = SkullConverter.entityTypeFromSkullType(skullType);
        assertEquals(expResult, result);
    }
    @Test
    public void testEntityTypeFromSkullType_Mob() {
        out.println("entityTypeFromSkullType Mob");
        TexturedSkullType skullType = TexturedSkullType.BLAZE;
        EntityType expResult = EntityType.BLAZE;
        EntityType result = SkullConverter.entityTypeFromSkullType(skullType);
        assertEquals(expResult, result);
    }
    @Test
    public void testEntityTypeFromSkullType_Mob_1_13() {
        out.println("entityTypeFromSkullType Mob 1.13");
        TexturedSkullType skullType = TexturedSkullType.TROPICAL_FISH;
        EntityType expResult = EntityType.TROPICAL_FISH;
        EntityType result = SkullConverter.entityTypeFromSkullType(skullType);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSkullTypeFromBlockState_NotAHead(){
        out.println("testSkullTypeFromBlockState not a head");
        BlockState state = Mocks.getMockBlockState_Stone();
        assertEquals(SkullConverter.skullTypeFromBlockState(state),null);
    }
        @Test
    public void testSkullTypeFromBlockState_NullPlayerhead(){
        out.println("testSkullTypeFromBlockState null playerhead");
        BlockState state = Mocks.getMockBlockState_PHead(null);
        assertEquals(SkullConverter.skullTypeFromBlockState(state),TexturedSkullType.PLAYER);
    }
    @Test
    public void testSkullTypeFromBlockState_UnknownPlayerhead(){
        out.println("testSkullTypeFromBlockState unknown playerhead");
        OfflinePlayer op = Mocks.getMockOfflinePlayer("3437cf83-c9b0-4709-a686-b8632b8d6172", "crashdemons");
        BlockState state = Mocks.getMockBlockState_PHead(op);
        assertEquals(SkullConverter.skullTypeFromBlockState(state),TexturedSkullType.PLAYER);
    }
    @Test
    public void testSkullTypeFromBlockState_SupportedPlayerhead(){
        out.println("testSkullTypeFromBlockState supported playerhead");
        String id = TexturedSkullType.ENDERMITE.getOwner().toString();
        OfflinePlayer op = Mocks.getMockOfflinePlayer(id, null);
        Skull state = Mocks.getMockBlockState_PHead(op);
        OfflinePlayer op2 = state.getOwningPlayer();
        System.out.println(op2.toString());
        System.out.println(op2.getUniqueId()+" (original "+id+")");
        assertEquals(SkullConverter.skullTypeFromBlockState(state),TexturedSkullType.ENDERMITE);
    }
    @Test
    public void testSkullTypeFromBlockState_VanillaHead(){
        out.println("testSkullTypeFromBlockState not a head");
        BlockState state = Mocks.getMockBlockState_Skull();
        assertEquals(SkullConverter.skullTypeFromBlockState(state),TexturedSkullType.SKELETON);
    }
    
    @Test
    public void testSkullTypeFromBlockStateLegacy_NullPlayerhead(){
        out.println("testSkullTypeFromBlockStateLegacy null playerhead");
        BlockState state = Mocks.getMockBlockState_PHead(null);
        assertEquals(SkullConverter.skullTypeFromBlockStateLegacy(state),TexturedSkullType.PLAYER);
    }
    @Test
    public void testSkullTypeFromBlockStateLegacy_UnknownPlayerhead(){
        out.println("testSkullTypeFromBlockStateLegacy unknown playerhead");
        OfflinePlayer op = Mocks.getMockOfflinePlayer("3437cf83-c9b0-4709-a686-b8632b8d6172", "crashdemons");
        BlockState state = Mocks.getMockBlockState_PHead(op);
        assertEquals(SkullConverter.skullTypeFromBlockStateLegacy(state),TexturedSkullType.PLAYER);
    }
    @Test
    public void testSkullTypeFromBlockStateLegacy_SupportedLegacyPlayerhead(){
        out.println("testSkullTypeFromBlockStateLegacy supported legacy playerhead");
        OfflinePlayer op = Mocks.getMockOfflinePlayer("1bee9df5-4f71-42a2-bf52-d97970d3fea3", "MHF_Ocelot");
        BlockState state = Mocks.getMockBlockState_PHead(op);
        assertEquals(SkullConverter.skullTypeFromBlockStateLegacy(state),TexturedSkullType.OCELOT);
    }
    
}
