/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.backports;

import com.github.crashdemons.playerheads.TexturedSkullType;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Field;
import java.util.UUID;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
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
            profileField.get(headMeta);
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
    
    public static void setOwningUuidDirtyStorageHack(SkullMeta headMeta,UUID id){
        headMeta.setOwner(id.toString());
    }
    public static UUID getOwningUuidDirtyStorageHack(String owner){
        try{ return UUID.fromString(owner); }catch(Exception e){ return null; }
    }
}
