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
 * Converts between entities, custom skullState type, etc
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
        TexturedSkullType type = TexturedSkullType.get(state.getType());//guess skullState by material
        if(type==null){
            System.out.println("Material not found "+state.getType().name());
            return null;
        }
        if(type.hasDedicatedItem() && type!=TexturedSkullType.PLAYER) return type;//if it's not a player then it's a dedicated skullState item reserved for the mob
        //if it's a playerhead, then we need to resolve further
        Skull skullState = (Skull) state;
        OfflinePlayer op =skullState.getOwningPlayer();
        if(op==null) return TexturedSkullType.PLAYER;
        UUID owner = op.getUniqueId();
        if(owner==null) return TexturedSkullType.PLAYER;
        TexturedSkullType match = TexturedSkullType.get(owner);//check if the UUID matches any in our textured skullState list
        if(match==null) return TexturedSkullType.PLAYER;
        return match;//if match was not null
    }
    
    @Deprecated
    public static TexturedSkullType skullTypeFromBlockStateLegacy(BlockState state){//with legacy name matching support
        TexturedSkullType type = skullTypeFromBlockState(state);
        if(type==null || type!=TexturedSkullType.PLAYER) return type;//don't really need to check null here, but it's more explicit this way.
        //now we're checking legacy player skulls
        Material mat = state.getType();
        if(mat!=Material.PLAYER_HEAD && mat!=Material.PLAYER_WALL_HEAD) return null;
        Skull skullState = (Skull) state;
        String owner=null;
        OfflinePlayer op = skullState.getOwningPlayer();
        if(op!=null) owner=op.getName();
        if(owner==null) owner=skullState.getOwner();//this is deprecated, but the above method does NOT get the name tag from the NBT unless user has logged in!
        if(owner==null) return TexturedSkullType.PLAYER;//we cannot resolve an owner name for this playerhead, so it can only be considered a Player
        
        CustomSkullType oldtype = CustomSkullType.get(owner);
        if(oldtype==null) return TexturedSkullType.PLAYER;//we can't resolve a legacy type for this playerhead so...
        
        return upgradeSkullTypeLegacy(oldtype);
        
    }
    
    @Deprecated
    public static TexturedSkullType upgradeSkullTypeLegacy(CustomSkullType oldType){
        try{
            return TexturedSkullType.valueOf(oldType.name().toUpperCase());
        }catch(IllegalArgumentException e){
            System.out.println("ERROR - Could not upgrade head: "+oldType.name());
            return TexturedSkullType.PLAYER;
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
