/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.backports;

import com.mojang.authlib.GameProfile;
import java.lang.reflect.Field;
import java.util.UUID;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;


/**
 *
 * @author crash
 */
final public class Backports {
    private Backports(){}
    public static final Predicate<ItemStack> isVanillaSkull = new Predicate<ItemStack>(){
        @Override
        public boolean test(ItemStack itemStack){ return isVanillaSkull(itemStack.getType()); }
    };
    
    public static boolean isVanillaSkull(Material mat){
        return (mat==Material.SKULL || mat==Material.SKULL_ITEM);
    }
    
    public static UUID getOwningUUID(SkullMeta headMeta){
        GameProfile profile = null;
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profile = (GameProfile) profileField.get(headMeta);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
            return null;
        }
        if(profile==null) return null;
        return profile.getId();
    }
    public static UUID getOwningUUID(Skull skullBlockState){
        GameProfile profile = null;
        try {
            Field profileField = skullBlockState.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profile = (GameProfile) profileField.get(skullBlockState);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
            return null;
        }
        if(profile==null) return null;
        return profile.getId();
    }
    public static OfflinePlayer getOwningPlayer(SkullMeta headMeta){
        UUID id = getOwningUUID(headMeta);
        if(id==null) return null;
        return Bukkit.getOfflinePlayer(id);
    }
    public static OfflinePlayer getOwningPlayer(Skull skullBlockState){
        UUID id = getOwningUUID(skullBlockState);
        if(id==null) return null;
        return Bukkit.getOfflinePlayer(id);
    }
    public static void setOwningPlayer_ByName(SkullMeta meta, OfflinePlayer op){
        String name = op.getName();
        meta.setOwner(name);
    }
    
    public static SkullType getDedicatedItem(FutureMaterial futureMaterial){
        return futureMaterial.getSkullType();
    }
    public static boolean isDedicatedItem(FutureMaterial futureMaterial){
        return futureMaterial.getSkullType()!=null;
    }
    
    
    
    public static FutureMaterial getFutureMaterialFromBlockState(BlockState bs){
        if(isVanillaSkull(bs.getType())){
            Skull state = (Skull) bs;
            SkullType type = state.getSkullType();
            for(FutureMaterial mat : FutureMaterial.values()){
                if(mat.getSkullType()==type)
                    return mat;
            }
        }
        return null;
    }
}
