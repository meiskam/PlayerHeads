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
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import org.shininet.bukkit.playerheads.Config;

/**
 *
 * @author crash
 * @author x7aSv
 */
public class SkullManager {
    
    private static void applyLore(SkullMeta headMeta,String extra){
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(" ");
        lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"PlayerHeads");
        if(!extra.isEmpty()) lore.add(extra);
        headMeta.setLore(lore);
    }
    
    private static void applyOwningPlayer(SkullMeta headMeta,OfflinePlayer owner){
        headMeta.setOwningPlayer( owner );
    }
    private static void applyDisplayName(SkullMeta headMeta,String display){
        headMeta.setDisplayName(display);
    }
    private static boolean applyTexture(SkullMeta headMeta, UUID uuid, String texture){//credit to x7aSv
        //System.out.println("Applying texture...");
        GameProfile profile = new GameProfile(uuid, null);
        profile.getProperties().put("textures", new Property("textures", texture));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
            return false;
        }
       // System.out.println("done applying.");
        return true;
    }
    
    
    public static ItemStack MobSkull(TexturedSkullType type, boolean useVanillaHeads){
        return MobSkull(type,Config.defaultStackSize, useVanillaHeads);
    }
    public static ItemStack MobSkull(TexturedSkullType type,int quantity,boolean useVanillaHeads){
        Material mat = type.getMaterial();
        
        
        if(type.hasDedicatedItem()){
            if(useVanillaHeads)
                return new ItemStack(mat,quantity);
            else mat=Material.PLAYER_HEAD;
        }
        
        //System.out.println("Player-head");
        ItemStack stack = new ItemStack(mat,quantity);
        SkullMeta headMeta = (SkullMeta) stack.getItemMeta();
        //applyOwningPlayer(headMeta,Bukkit.getOfflinePlayer(type.getOwner()));
        applyTexture(headMeta,type.getOwner(),type.getTexture());
        applyDisplayName(headMeta,ChatColor.RESET + "" + ChatColor.YELLOW + type.getDisplayName());
        applyLore(headMeta,ChatColor.GREEN+"Mob Drop");
        stack.setItemMeta(headMeta);
        return stack;
    }
    private static ItemStack PlayerSkull(OfflinePlayer owner){
        return PlayerSkull(owner,Config.defaultStackSize);
    }
    private static ItemStack PlayerSkull(OfflinePlayer owner, int quantity){
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD,quantity);
        SkullMeta headMeta = (SkullMeta) stack.getItemMeta();
        String name=null;
        if(owner!=null){
            applyOwningPlayer(headMeta,owner);
            name = owner.getName();
        }
        if(name==null) name="Unknown";//only used for display purposes.
        applyDisplayName(headMeta,ChatColor.RESET + "" + ChatColor.YELLOW + TexturedSkullType.getDisplayName(name));
        applyLore(headMeta,ChatColor.RED+"Player Drop");
        stack.setItemMeta(headMeta);
        return stack;
    }
    public static ItemStack PlayerSkull(String owner){
        return PlayerSkull(owner,Config.defaultStackSize);
    }
    public static ItemStack PlayerSkull(String owner, int quantity){
        OfflinePlayer op = Bukkit.getOfflinePlayer(owner);
        return PlayerSkull(op,quantity);
    }
    public static void updatePlayerSkullState(BlockState skullState){
        //for a skull belonging to a player drop, this shouldn't really be necessary to reset the owner.
        //and for textured mobheads, the texture is embedded, so shouldn't need updating...
        /*
        OfflinePlayer op = skullState.getOwningPlayer();
        String owner=null;
        if(op!=null) owner=op.getName();
        if(owner==null) owner=skullState.getOwner();//this is deprecated, but the above method does NOT get the name tag from the NBT.
        if(owner==null) return;

        skullState.setOwningPlayer(Bukkit.getOfflinePlayer(skullType.getOwner()));
        */
        skullState.update();
    }
    /*
    //these do not properly update head skin in server testing.
    public static ItemStack PlayerSkull(UUID owner){
        return PlayerSkull(owner,Config.defaultStackSize);
    }
    
    public static ItemStack PlayerSkull(UUID owner, int quantity){
        OfflinePlayer op = Bukkit.getOfflinePlayer(owner);
        //this is great but it doesn't update the texture
        return PlayerSkull(op,quantity);
    }
    */
}
