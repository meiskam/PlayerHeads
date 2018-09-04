/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import org.bukkit.entity.EntityType;
import org.shininet.bukkit.playerheads.CustomSkullType;

/**
 *
 * @author crash
 */
public class Test {
    public static void main(String[] args) {
        // Prints "Hello, World" to the terminal window.
        CustomSkullType sk = CustomSkullType.get("CarlosTheCow");
        System.out.println(sk);
        System.out.println(sk.getOwner());
        System.out.println(sk.getDisplayName());
        System.out.println(sk.getSpawnName());
        
        
        testSkullTypes();
    }
    public static void testSkullTypes(){
        for(EntityType type : EntityType.values()){
            if(!type.isAlive()) continue;
            try{
                TexturedSkullType type2 = TexturedSkullType.valueOf( type.name().toUpperCase() );
                System.out.println("Mob skull: "+type.name()+" <-> "+type2 +" vanillaitem?"+type2.hasDedicatedItem());
            }catch(Exception e){
                System.out.println("Mob skull unsupported for: "+type.name());
            }
        }
        System.out.println("===========================================");
        for(TexturedSkullType type : TexturedSkullType.values()){
            try{
                EntityType type2 = EntityType.valueOf( type.name().toUpperCase() );
                System.out.println("Mob skull: "+type.name()+" <-> "+type2+" vanillaitem?"+type.hasDedicatedItem());
            }catch(Exception e){
                System.out.println("Mob skull has no associated entity: "+type.name()+" - BUG!");
            }
        }
    }
}
