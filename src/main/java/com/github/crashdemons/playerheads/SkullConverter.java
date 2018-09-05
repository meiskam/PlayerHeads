/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import java.util.Map;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.EntityType;
import org.shininet.bukkit.playerheads.CustomSkullType;

/**
 * Converts between entities, custom skull type, etc
 * @author crash
 */
public abstract class SkullConverter {
    /*
    
                    CustomSkullType customSkullType = CustomSkullType.valueOf(entityType.name());
                    EntityDeathHelper(event, customSkullType, plugin.configFile.getDouble(customSkullType.name().replace("_", "").toLowerCase() + "droprate") * lootingrate);
    */
    public static String dropConfigFromSkullType(TexturedSkullType skullType){
        return skullType.name().replace("_", "").toLowerCase() + "droprate";
    }
    public static TexturedSkullType skullTypeFromEntityType(EntityType entityType){
        String entityName = entityType.name().toUpperCase();
        try{
            return TexturedSkullType.valueOf(entityName);
        }catch(IllegalArgumentException e){
            return null;
        }
    }
    
    public static boolean isPlayerHead(Material mat){
        return (mat==Material.PLAYER_HEAD || mat==Material.PLAYER_WALL_HEAD);
    }
    
    public static TexturedSkullType skullTypeFromBlockState(BlockState state){
        Skull skull = (Skull) state;
        TexturedSkullType type = TexturedSkullType.get(skull.getType());//guess skull by material
        if(type==null){
            System.out.println("Material not found "+skull.getType().name());
            return null;
        }
        if(type.hasDedicatedItem() && type!=TexturedSkullType.PLAYER) return type;//if it's not a player then it's a dedicated skull item reserved for the mob
        //if it's a playerhead, then we need to resolve further
        OfflinePlayer op =skull.getOwningPlayer();
        if(op==null) return TexturedSkullType.PLAYER;
        UUID owner = op.getUniqueId();
        if(owner==null) return TexturedSkullType.PLAYER;
        TexturedSkullType match = TexturedSkullType.get(owner);//check if the UUID matches any in our textured skull list
        if(match==null) return TexturedSkullType.PLAYER;
        return match;//if match was not null
    }
    public static EntityType entityTypeFromSkullType(TexturedSkullType skullType){
        String skullName = skullType.name().toUpperCase();
        try{
            return EntityType.valueOf(skullName);
        }catch(IllegalArgumentException e){
            return null;
        }
    }
    public static TexturedSkullType upgradeSkullType(CustomSkullType oldType){
        try{
            return TexturedSkullType.valueOf(oldType.name().toUpperCase());
        }catch(IllegalArgumentException e){
            System.out.println("ERROR - Could not upgrade head: "+oldType.name());
            return TexturedSkullType.PLAYER;
        }
    }

}
