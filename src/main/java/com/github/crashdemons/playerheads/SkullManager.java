/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import java.util.UUID;
import java.lang.reflect.Field;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import org.shininet.bukkit.playerheads.Config;

/**
 *
 * @author crash
 * @author x7aSv
 */
public class SkullManager {
    private static boolean applyTexture(ItemStack head, UUID uuid, String texture){//credit to x7aSv
        System.out.println("Applying texture...");
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
            return false;
        }
        head.setItemMeta(headMeta);
        System.out.println("done applying.");
        return true;
    }
    
    
    public static ItemStack Skull(TexturedSkullType type){
        return Skull(type,Config.defaultStackSize);
    }
    public static ItemStack Skull(TexturedSkullType type,int quantity){
        Material mat = type.getMaterial();
        if(type.isPlayerHead()){
            System.out.println("Player-head");
            ItemStack stack = new ItemStack(mat,quantity);
            applyTexture(stack,type.getOwner(),type.getTexture());
            return stack;
        }else{
            return new ItemStack(mat,quantity);
        }
    }
}
