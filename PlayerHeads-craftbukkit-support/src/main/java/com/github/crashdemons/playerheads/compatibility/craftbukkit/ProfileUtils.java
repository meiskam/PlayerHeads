/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.craftbukkit;

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
 * Credit goes to x7aSv for basic implementation of custom head creation.
 * @author crashdemons (crashenator at gmail.com)
 * @deprecated This class is server implementation-specific and should not be used directly if possible, the namespace may change in the future.
 */
@Deprecated
public class ProfileUtils {
    private static Field getProfileField(Object obj) throws IllegalArgumentException,NoSuchFieldException,SecurityException,IllegalAccessException{
        if(!(obj instanceof SkullMeta || obj instanceof Skull)) throw new IllegalArgumentException("Object is not a supported type: SkullMeta or Skull (blockstate)");
        Field profileField = obj.getClass().getDeclaredField("profile");
        profileField.setAccessible(true);
        return profileField;
    }
    private static GameProfile createProfile(UUID uuid, String name, String texture){
        GameProfile profile = new GameProfile(uuid, name);
        if(texture!=null && !texture.isEmpty()) profile.getProperties().put("textures", new Property("textures", texture));
        return profile;
    }
    private static GameProfile createProfile(UUID uuid, String texture){
        return createProfile(uuid, null, texture);
    }
    
    public static GameProfile getProfile(Object obj) throws IllegalStateException{
        try {
            return (GameProfile) getProfileField(obj).get(obj);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            throw new IllegalStateException("The profile field value could not be retrieved");
        }
    }
    public static boolean setProfile(Object obj, GameProfile profile) throws IllegalStateException{
        try {
            getProfileField(obj).set(obj, profile);
            return true;
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            throw new IllegalStateException("The profile field value could not be retrieved");
        }
    }
    public static boolean setProfile(Object obj, UUID uuid, String texture) throws IllegalStateException{
        return setProfile(obj, createProfile(uuid,texture));
    }
    
    
    /**
     * Set a profile field in the supplied item meta using a UUID and Texture string
     * @param headMeta the item meta to apply the profile on
     * @param uuid A UUID to be associated with this profile and texture
     * @param texture The Base64-encoded Texture-URL tags.
     * @return True: the profile was successfully set. False: the profile could not be set.
     */
    public static boolean setProfile(ItemMeta headMeta, UUID uuid, String texture) throws IllegalStateException{//credit to x7aSv for original 
        return setProfile((Object)headMeta, uuid, texture);
    }
    
    /**
     * Set a profile field in the supplied block state using a UUID and Texture string
     * @param headBlockState the block state to apply the profile on
     * @param uuid A UUID to be associated with this profile and texture
     * @param texture The Base64-encoded Texture-URL tags.
     * @return True: the profile was successfully set. False: the profile could not be set.
     */
    public static boolean setProfile(Skull headBlockState, UUID uuid, String texture) throws IllegalStateException{
        return setProfile((Object)headBlockState, uuid, texture);
    }
    
    /**
     * Gets a UUID from the head's internal profile field.
     * @param headMeta the item meta of the head to check
     * @return the UUID, or null if not found.
     */
    public static UUID getProfileUUID(SkullMeta headMeta) throws IllegalStateException{
        GameProfile profile = getProfile(headMeta);
        if(profile==null) return null;
        return profile.getId();
    }
    /**
     * Gets a UUID from the head's internal profile field.
     * @param skullBlockState the block state of the head to check
     * @return the UUID, or null if not found.
     */
    public static UUID getProfileUUID(Skull skullBlockState) throws IllegalStateException{
        GameProfile profile = getProfile(skullBlockState);
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
    public static OfflinePlayer getProfilePlayer(SkullMeta headMeta) throws IllegalStateException{
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
    public static OfflinePlayer getProfilePlayer(Skull skullBlockState) throws IllegalStateException{
        UUID id = getProfileUUID(skullBlockState);
        if(id==null) return null;
        return Bukkit.getOfflinePlayer(id);
    }
}
