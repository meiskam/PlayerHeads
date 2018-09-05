/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import java.util.UUID;
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
    public static String camelCase(String str)
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
    public static void testSkullTypes(){
        long count=0;
        for(EntityType type : EntityType.values()){
            if(!type.isAlive()) continue;
            count++;
            try{
                TexturedSkullType type2 = TexturedSkullType.valueOf( type.name().toUpperCase() );
                //System.out.println("Mob skull: "+type.name()+" <-> "+type2 +" vanillaitem?"+type2.hasDedicatedItem());
            }catch(Exception e){
                System.out.println("Mob skull unsupported for: "+type.name());
            }
        }
        System.out.println("===========================================");
        for(TexturedSkullType type : TexturedSkullType.values()){
            try{
                EntityType type2 = EntityType.valueOf( type.name().toUpperCase() );
                //System.out.println("Mob skull: "+type.name()+" <-> "+type2+" vanillaitem?"+type.hasDedicatedItem());
            }catch(Exception e){
                System.out.println("Mob skull has no associated entity: "+type.name()+" - BUG!");
            }
        }
        TexturedSkullType.debug();
        System.out.println("========================");
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
        System.out.println(count+" mobs in total");
        /*
        for(int i=0;i<100;i++){
            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();
            System.out.println(randomUUIDString);
        }*/
    }
}
