/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author crash
 */
public enum VanillaSkullType {//deprecated in api, but we stll need to keep track of vanilla heads, and there is no method for checking isSkull or isHead in the api...
    CREEPER(Material.CREEPER_HEAD,Material.CREEPER_WALL_HEAD),	
    DRAGON(Material.DRAGON_HEAD,Material.DRAGON_WALL_HEAD),
    PLAYER(Material.PLAYER_HEAD,Material.PLAYER_WALL_HEAD),
    SKELETON(Material.SKELETON_SKULL,Material.SKELETON_WALL_SKULL),	
    WITHER(Material.WITHER_SKELETON_SKULL,Material.WITHER_SKELETON_WALL_SKULL),
    ZOMBIE(Material.ZOMBIE_HEAD,Material.ZOMBIE_WALL_HEAD);

    private Material material;
    private Material wallMaterial;

    private static class Mappings{
        public static HashMap<Material,VanillaSkullType> skullTypeByMaterial = new HashMap<>();
    }

    VanillaSkullType(Material mat, Material wallMat){
        material=mat;
        wallMaterial=wallMat;
        Mappings.skullTypeByMaterial.put(mat, this);
        Mappings.skullTypeByMaterial.put(wallMat, this);
    }

    public Material getMaterial(){
        return material;
    }
    public static VanillaSkullType fromMaterial(Material mat){
        return Mappings.skullTypeByMaterial.get(mat);
    }
    public static Material toMaterial(VanillaSkullType type){
        return type.getMaterial();
    }
    public static boolean hasMaterial(Material mat){
        return Mappings.skullTypeByMaterial.containsKey(mat);
    }
    public static boolean hasItem(ItemStack stack){
        return hasMaterial(stack.getType());
    }
    public static boolean hasBlock(Block block){
        return hasMaterial(block.getType());
    }
    public static VanillaSkullType fromItem(ItemStack stack){
        return fromMaterial(stack.getType() );
    }
    public static VanillaSkullType fromBlock(Block block){
        return fromMaterial(block.getType() );
    }
}