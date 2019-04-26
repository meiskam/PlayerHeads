/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import com.github.crashdemons.playerheads.testutils.Mocks;
import com.github.crashdemons.playerheads.testutils.TestOutput;
import com.github.crashdemons.playerheads.compatibility.Compatibility;
import com.github.crashdemons.playerheads.compatibility.CompatibleSkullMaterial;
//import com.github.crashdemons.playerheads.compatibility.craftbukkit_1_13.Provider;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Tests TexturedSkullType
 * @author crash
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class})
public class TexturedSkullTypeTest {
    
    private final TestOutput out=new TestOutput(this);
    public TexturedSkullTypeTest() {
        //Provider x;
        Mocks.setupFakeServerSupport();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSkullConfigName_Player(){
        assertEquals(TexturedSkullType.PLAYER.getConfigName(),"droprate");
    }
    @Test
    public void testSkullConfigName_Mob(){
        assertEquals(TexturedSkullType.WITHER_SKELETON.getConfigName(),"witherskeletondroprate");
    }
    
    
    /**
     * tests enum entry compatibility with org.bukkit.entity.EntityType
     */
    @Test
    public void testEntityCompatibility(){
        out.println("testEntityCompatibility (skull maps to entity correctly)");
        for(TexturedSkullType skull : TexturedSkullType.values()){
            try{
                EntityType type2 = EntityType.valueOf( skull.name().toUpperCase() );//throws IllegalArgumentException if conversion fails
            }catch(Exception e){
                 out.println("   Matching entity missing for skull: "+skull.name()+"  (nonfatal)");
            }
        }
        
    }
    
    /**
     * tests enum entry support for all org.bukkit.entity.EntityType's (which are living)
     * This test cannot fail, but provides information about unsupported mobs.
     */
    @Test
    public void testEntitySupport(){
        out.println("testEntitySupport (entities map to skulls)");
        for(EntityType type : EntityType.values()){
            if(!type.isAlive()) continue;
            try{
                TexturedSkullType type2 = TexturedSkullType.valueOf( type.name().toUpperCase() );
                //out.println("Mob skull: "+type.name()+" <-> "+type2 +" vanillaitem?"+type2.hasDedicatedItem());
            }catch(Exception e){
                out.println("   Mob skull missing for entity: "+type.name()+"  (nonfatal)");
            }
        }
    }
    

    /**
     * Test of get method, of class TexturedSkullType.
     */
    @Test
    public void testGet_UUID() {
        out.println("get by UUID");
        UUID owner = null;
        TexturedSkullType expResult = null;
        TexturedSkullType result = TexturedSkullType.get(owner);
        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class TexturedSkullType.
     */
    @Test
    public void testGet_Material() {
        out.println("get by Material");
        CompatibleSkullMaterial mat = null;
        TexturedSkullType expResult = null;
        TexturedSkullType result = TexturedSkullType.get(mat);
        assertEquals(expResult, result);
    }



    /**
     * Test of isPlayerHead method, of class TexturedSkullType.
     */
    @Test
    public void testIsPlayerHead_Player() {
        out.println("isPlayerHead with Player skull");
        boolean expResult;
        TexturedSkullType instance;
        boolean result;
        
        
        instance = TexturedSkullType.PLAYER;
        expResult = true;
        result = instance.isPlayerHead();
        assertEquals(expResult, result);

    }
    
        /**
     * Test of isPlayerHead method, of class TexturedSkullType.
     */
    @Test
    public void testIsPlayerHead_Nonvanilla() {
        out.println("isPlayerHead with nonvanilla skull");
        boolean expResult;
        TexturedSkullType instance;
        boolean result;
        instance = TexturedSkullType.GHAST;
        expResult = true;
        result = instance.isPlayerHead();
        assertEquals(expResult, result);
        
        instance = TexturedSkullType.SKELETON;
        expResult = false;
        result = instance.isPlayerHead();
        assertEquals(expResult, result);

    }
    /**
     * Test of isPlayerHead method, of class TexturedSkullType.
     */
    @Test
    public void testIsPlayerHead_Vanilla() {
        out.println("isPlayerHead with vanilla skull");
        boolean expResult;
        TexturedSkullType instance;
        boolean result;
        
        instance = TexturedSkullType.SKELETON;
        expResult = false;
        result = instance.isPlayerHead();
        assertEquals(expResult, result);

    }
    
    /**
     * Test of hasDedicatedItem method, of class TexturedSkullType.
     */
    @Test
    public void testHasDedicatedItem_Player() {
        out.println("hasDedicatedItem with Player skull");
        
        boolean expResult;
        TexturedSkullType instance;
        boolean result;
        
        
        instance = TexturedSkullType.PLAYER;
        expResult = true;
        result = instance.hasDedicatedItem();
        assertEquals(expResult, result);
        
    }
    /**
     * Test of hasDedicatedItem method, of class TexturedSkullType.
     */
    @Test
    public void testHasDedicatedItem_Nonvanilla() {
        out.println("hasDedicatedItem with nonvanilla skull");
        
        boolean expResult;
        TexturedSkullType instance;
        boolean result;
        
        instance = TexturedSkullType.GHAST;
        expResult = false;
        result = instance.hasDedicatedItem();
        assertEquals(expResult, result);
        
        
    }
    /**
     * Test of hasDedicatedItem method, of class TexturedSkullType.
     */
    @Test
    public void testHasDedicatedItem_Vanilla() {
        out.println("hasDedicatedItem with vanilla skull");
        
        boolean expResult;
        TexturedSkullType instance;
        boolean result;
        
        instance = TexturedSkullType.SKELETON;
        expResult = true;
        result = instance.hasDedicatedItem();
        assertEquals(expResult, result);
        
    }
    
    @Test
    public void test_1_14_preparations() {
        out.println("test 1.14 mob support preparations");
        
        String mobs[]={
            "CAT",
            "FOX",
            "PANDA",
            "PILLAGER",
            "RAVAGER",
            "TRADER_LLAMA",
            "WANDERING_TRADER"
        };
        for(String mob : mobs){
            out.println("   testing for "+mob);
            TexturedSkullType type;
            try{
                type = TexturedSkullType.valueOf(mob);
            }catch(Exception e){
                type = null;
            }
            out.println("      "+type);
            assertNotNull(type);
        }        
    }
}
