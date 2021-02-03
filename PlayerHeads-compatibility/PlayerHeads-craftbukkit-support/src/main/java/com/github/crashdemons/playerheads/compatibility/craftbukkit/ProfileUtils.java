/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.craftbukkit;

import com.github.crashdemons.playerheads.compatibility.CompatibleProfile;
import com.mojang.authlib.GameProfile;
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
    
    //internal methods - can be typed
    
    private static Field getProfileField(Object skull) throws IllegalArgumentException,NoSuchFieldException,SecurityException,IllegalAccessException{
        if(!(skull instanceof SkullMeta || skull instanceof Skull)) throw new IllegalArgumentException("Object is not a supported type: SkullMeta or Skull (blockstate)");
        Field profileField = skull.getClass().getDeclaredField("profile");
        profileField.setAccessible(true);
        return profileField;
    }
    
    //-------------------------------------------------------------------------
    
    public static GameProfile getInternalProfile(Object skull) throws IllegalStateException{
        try {
            return (GameProfile) getProfileField(skull).get(skull);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            throw new IllegalStateException("The profile field value could not be retrieved", error);
        }
    }
    public static CompatibleProfile getProfile(Object skull) throws IllegalStateException{
        GameProfile profile = getInternalProfile(skull);
        if(profile==null) return new CompatibleProfileCB();
        return new CompatibleProfileCB(profile);
    }
    public static boolean setInternalProfile(Object skull, GameProfile profile) throws IllegalStateException{
        try {
            getProfileField(skull).set(skull, profile);
            return true;
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            throw new IllegalStateException("The profile field value could not be retrieved/set", error);
        }
    }
    public static boolean setProfile(Object skull, CompatibleProfile profile) throws IllegalStateException{
        return setInternalProfile(skull, (GameProfile) profile.toInternalObject());
    }
    
    
    //-------------------------------------------------------------------------
    
    /**
     * Set a profile field in the supplied item meta using a UUID and Texture string
     * @param skull the item/block skull object to apply the profile on
     * @param uuid A UUID to be associated with this profile and texture
     * @param texture The Base64-encoded Texture-URL tags.
     * @return True: the profile was successfully set. False: the profile could not be set.
     */
    public static boolean setProfile(Object skull, UUID uuid, String texture) throws IllegalStateException{//credit to x7aSv for original
        CompatibleProfile profile = new CompatibleProfileCB(uuid,null);
        profile.setTextures(texture);
        return setProfile(skull, profile);
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
    
    public static boolean clearProfile(Object skull){
        if(!(skull instanceof SkullMeta || skull instanceof Skull)) throw new IllegalArgumentException("Object is not a supported type: SkullMeta or Skull (blockstate)");
        try{
            return setInternalProfile(skull, null);
        }catch(Exception e){
            return false;
        }
    }
}
