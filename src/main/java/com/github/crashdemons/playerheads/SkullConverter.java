/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import org.bukkit.entity.EntityType;

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
    public static EntityType entityTypeFromSkullType(TexturedSkullType skullType){
        String skullName = skullType.name().toUpperCase();
        try{
            return EntityType.valueOf(skullName);
        }catch(IllegalArgumentException e){
            return null;
        }
    }
}
