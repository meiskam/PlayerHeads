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
        ZOMBIE;
        public static SkullType fromMaterial(org.bukkit.Material mat){
            switch(mat){
                case CREEPER_HEAD:
                case CREEPER_WALL_HEAD:
                    return SkullType.CREEPER;
                case DRAGON_HEAD:
                case DRAGON_WALL_HEAD:
                    return SkullType.DRAGON;
                case PLAYER_HEAD:
                case PLAYER_WALL_HEAD:
                    return SkullType.PLAYER;
                case SKELETON_SKULL:
                case SKELETON_WALL_SKULL:
                    return SkullType.SKELETON;
                case WITHER_SKELETON_SKULL:
                case WITHER_SKELETON_WALL_SKULL:
                    return SkullType.WITHER;
                case ZOMBIE_HEAD:
                case ZOMBIE_WALL_HEAD:
                    return SkullType.ZOMBIE;
            }
            return SkullType.PLAYER;
        }
        public static org.bukkit.Material toMaterial(SkullType type){
            switch(type){
                    case CREEPER:
                        return org.bukkit.Material.CREEPER_HEAD;
                    case DRAGON:
                        return org.bukkit.Material.DRAGON_HEAD;
                    case PLAYER:
                        return org.bukkit.Material.PLAYER_HEAD;
                    case SKELETON:
                        return org.bukkit.Material.SKELETON_SKULL;
                    case WITHER:
                        return org.bukkit.Material.WITHER_SKELETON_SKULL;
                    case ZOMBIE:
                        return org.bukkit.Material.ZOMBIE_HEAD;
            }
            return org.bukkit.Material.PLAYER_HEAD;
        }
    }
    
    public static boolean isSkull(org.bukkit.Material mat){
        switch(mat){
            case CREEPER_WALL_HEAD:
            case DRAGON_WALL_HEAD:
            case PLAYER_WALL_HEAD:
            case SKELETON_WALL_SKULL:
            case WITHER_SKELETON_WALL_SKULL:
            case ZOMBIE_WALL_HEAD:
            case CREEPER_HEAD:
            case DRAGON_HEAD:
            case PLAYER_HEAD:
            case SKELETON_SKULL:
            case WITHER_SKELETON_SKULL:
            case ZOMBIE_HEAD:
                return true;
        }
        return false;
    }
    public static SkullType getSkullType(org.bukkit.inventory.ItemStack stack){
        return SkullType.fromMaterial(stack.getType() );
    }
    public static SkullType getSkullType(org.bukkit.block.Block block){
        return SkullType.fromMaterial(block.getType() );
    }

    private static org.bukkit.Material inferItemType(org.bukkit.Material mat, SkullType skulltype){
        switch(mat){
            case LEGACY_SKULL:
            case LEGACY_SKULL_ITEM://this is probably the only important one.
                return SkullType.toMaterial(skulltype);
        }
        return mat;
    }
    public static class ItemStack extends org.bukkit.inventory.ItemStack{
        public ItemStack(org.bukkit.Material mat, int quantity){ super(mat,quantity); }
        public ItemStack(Material mat, int quantity, SkullType skulltype){
            super(
                    SkullType.toMaterial(skulltype),
                    quantity
            );
        }
        public ItemStack(Material mat, int quantity, short skulltype){
            this(mat,quantity, SkullType.values()[skulltype] );
        }
    }
}
