/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import com.github.crashdemons.playerheads.compatibility.CompatibleSkullMaterial;
import com.github.crashdemons.playerheads.compatibility.bukkit_1_13.Provider;
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
@Ignore
public class TexturedSkullTypeTest {
    
    public TexturedSkullTypeTest() {
        Provider x;
        Mocks.setupFakeServerVersion();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
        //these tasks aren't really test-related but there's not a better place to put them really.
        //generateLangDefinitions();
        //generateLangEntries();
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
        System.out.println("testEntityCompatibility (skull maps to entity correctly)");
        for(TexturedSkullType skull : TexturedSkullType.values()){
            EntityType type2 = EntityType.valueOf( skull.name().toUpperCase() );//throws IllegalArgumentException if conversion fails
        }
        
    }
    
    /**
     * tests enum entry support for all org.bukkit.entity.EntityType's (which are living)
     * This test cannot fail, but provides information about unsupported mobs.
     */
    @Test
    public void testEntitySupport(){
        System.out.println("testEntitySupport (entities map to skulls)");
        for(EntityType type : EntityType.values()){
            if(!type.isAlive()) continue;
            try{
                TexturedSkullType type2 = TexturedSkullType.valueOf( type.name().toUpperCase() );
                //System.out.println("Mob skull: "+type.name()+" <-> "+type2 +" vanillaitem?"+type2.hasDedicatedItem());
            }catch(Exception e){
                System.out.println("   Mob skull missing for entity: "+type.name()+"  (nonfatal)");
            }
        }
    }
    

    /**
     * Test of get method, of class TexturedSkullType.
     */
    @Test
    public void testGet_UUID() {
        System.out.println("get by UUID");
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
        System.out.println("get by Material");
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
        System.out.println("isPlayerHead with Player skull");
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
        System.out.println("isPlayerHead with nonvanilla skull");
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
        System.out.println("isPlayerHead with vanilla skull");
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
        System.out.println("hasDedicatedItem with Player skull");
        
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
        System.out.println("hasDedicatedItem with nonvanilla skull");
        
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
        System.out.println("hasDedicatedItem with vanilla skull");
        
        boolean expResult;
        TexturedSkullType instance;
        boolean result;
        
        instance = TexturedSkullType.SKELETON;
        expResult = true;
        result = instance.hasDedicatedItem();
        assertEquals(expResult, result);
        
    }
    
    

    private static String camelCase(String str)
    {
        StringBuilder builder = new StringBuilder(str);
        // Flag to keep track if last visited character is a 
        // white space or not
        boolean isLastSpace = true;

        // Iterate String from beginning to end.
        for(int i = 0; i < builder.length(); i++)
        {
                char ch = builder.charAt(i);

                if(isLastSpace && ch >= 'a' && ch <='z')
                {
                        // Character need to be converted to uppercase
                        builder.setCharAt(i, (char)(ch + ('A' - 'a') ));
                        isLastSpace = false;
                }else if (ch != ' ')
                        isLastSpace = false;
                else
                        isLastSpace = true;
        }

        return builder.toString();
    }
    
    public static void generateLangDefinitions(){
        System.out.println("===========================================");
        System.out.println("Generating Lang Definitions:");
        System.out.println("===========================================");
        for(EntityType type : EntityType.values()){
            if(!type.isAlive()) continue;
            if(type==EntityType.ARMOR_STAND || type==EntityType.PLAYER || type==EntityType.GIANT) continue;
            String name=type.name();
            String spaced = type.name().replace("_", " ").toLowerCase();
            String spawn=type.name().replace("_", "").toLowerCase();
            String caps = camelCase(spaced);
            System.out.println("public static String HEAD_"+name+";");//+"="+caps+" Head");
            System.out.println("public static String HEAD_SPAWN_"+name+";");//=#"+spawn);
        }
        System.out.println("===========================================");
    }
    
    public static void generateLangEntries(){
        System.out.println("===========================================");
        System.out.println("Generating Lang Entrie: (needs manual editing for skulls)");
        System.out.println("===========================================");
        for(EntityType type : EntityType.values()){
            if(!type.isAlive()) continue;
            if(type==EntityType.ARMOR_STAND || type==EntityType.PLAYER || type==EntityType.GIANT) continue;
            String name=type.name();
            String spaced = type.name().replace("_", " ").toLowerCase();
            String spawn=type.name().replace("_", "").toLowerCase();
            String caps = camelCase(spaced);
            System.out.println("HEAD_"+name+"="+caps+" Head");
            System.out.println("HEAD_SPAWN_"+name+"=#"+spawn);
        }
        System.out.println("===========================================");
    }
    
    
    
    
}
