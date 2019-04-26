/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Provides methods for working with profiles on items and blocks.
 * @author crashdemons (crashenator at gmail.com)
 * @deprecated This class is server implementation-specific and should not be used directly if possible, the namespace may change in the future.
 */
public class ProfileUtils {
    
    /**
     * Set a profile field in the supplied item meta using a UUID and Texture string
     * @param headMeta the item meta to apply the profile on
     * @param uuid A UUID to be associated with this profile and texture
     * @param texture The Base64-encoded Texture-URL tags.
     * @return True: the profile was successfully set. False: the profile could not be set.
     */
    public static boolean setProfile(ItemMeta headMeta, UUID uuid, String texture){//credit to x7aSv
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
        return true;
    }
    
    /**
     * Set a profile field in the supplied block state using a UUID and Texture string
     * @param headBlockState the block state to apply the profile on
     * @param uuid A UUID to be associated with this profile and texture
     * @param texture The Base64-encoded Texture-URL tags.
     * @return True: the profile was successfully set. False: the profile could not be set.
     */
    public static boolean setProfile(Skull headBlockState, UUID uuid, String texture){
        GameProfile profile = new GameProfile(uuid, null);
        profile.getProperties().put("textures", new Property("textures", texture));
        try {
            Field profileField = headBlockState.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headBlockState, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * Gets a UUID from the head's internal profile field.
     * @param headMeta the item meta of the head to check
     * @return the UUID, or null if not found.
     */
    public static UUID getProfileUUID(SkullMeta headMeta){
        GameProfile profile = null;
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profile = (GameProfile) profileField.get(headMeta);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            //error.printStackTrace();
            return null;
        }
        if(profile==null) return null;
        return profile.getId();
    }
    /**
     * Gets a UUID from the head's internal profile field.
     * @param skullBlockState the block state of the head to check
     * @return the UUID, or null if not found.
     */
    public static UUID getProfileUUID(Skull skullBlockState){
        GameProfile profile = null;
        try {
            Field profileField = skullBlockState.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profile = (GameProfile) profileField.get(skullBlockState);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            //error.printStackTrace();
            return null;
        }
        if(profile==null) return null;
        return profile.getId();
    }
    /**
     * Infers an Owning Player (OfflinePlayer) from the internal profile field information using the UUID.
     * 
     * This is equivalent to Bukkit.getOfflinePlayer(ProfileUtils.getProfileUUID(...)).
     * @param headMeta the item meta for the head to check
     * @see #getProfileUUID(org.bukkit.inventory.meta.SkullMeta)
     * @return The OfflinePlayer, or null if no profile was found.
     */
    public static OfflinePlayer getProfilePlayer(SkullMeta headMeta){
        UUID id = getProfileUUID(headMeta);
        if(id==null) return null;
        return Bukkit.getOfflinePlayer(id);
    }
    /**
     * Infers an Owning Player (OfflinePlayer) from the internal profile field information using the UUID.
     * 
     * This is equivalent to Bukkit.getOfflinePlayer(ProfileUtils.getProfileUUID(...)).
     * @param skullBlockState the blockstate for the head to check
     * @see #getProfileUUID(org.bukkit.inventory.meta.SkullMeta)
     * @return The OfflinePlayer, or null if no profile was found.
     */
    public static OfflinePlayer getProfilePlayer(Skull skullBlockState){
        UUID id = ProfileUtils.getProfileUUID(skullBlockState);
        if(id==null) return null;
        return Bukkit.getOfflinePlayer(id);
    }
}
