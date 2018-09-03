/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;



/**
 *
 * @author crash
 */
public abstract class Shim {
    
    public static enum Material{
        SKULL_ITEM
    }
    public static enum SkullType {
        CREEPER,	
        DRAGON,
        PLAYER,
        SKELETON,	
        WITHER,
        ZOMBIE
    }
    
    private static org.bukkit.Material inferItemType(Material mat, SkullType skulltype){
        switch(skulltype){
                    case CREEPER:
                        return org.bukkit.Material.CREEPER_HEAD;
                    case DRAGON:
                        return org.bukkit.Material.DRAGON_HEAD;
                    case PLAYER:
                        return org.bukkit.Material.PLAYER_HEAD;//TODO: warning for lack of nbt?
                    case SKELETON:
                        return org.bukkit.Material.SKELETON_SKULL;
                    case WITHER:
                        return org.bukkit.Material.WITHER_SKELETON_SKULL;
                    case ZOMBIE:
                        return org.bukkit.Material.ZOMBIE_HEAD;
        }
        return org.bukkit.Material.PLAYER_HEAD;
    }
    private static org.bukkit.Material inferItemType(org.bukkit.Material mat, SkullType skulltype){
        switch(mat){
            case LEGACY_SKULL:
            case LEGACY_SKULL_ITEM://this is probably the only important one.
                return inferItemType(Material.SKULL_ITEM, skulltype);
        }
        return mat;
    }
    public static class ItemStack extends org.bukkit.inventory.ItemStack{
        public ItemStack(org.bukkit.Material mat, int quantity){ super(mat,quantity); }
        public ItemStack(Material mat, int quantity, SkullType skulltype){
            super(
                    inferItemType(mat,skulltype),
                    quantity
            );
        }
        public ItemStack(Material mat, int quantity, short skulltype){
            this(mat,quantity, SkullType.values()[skulltype] );
        }
    }
}
