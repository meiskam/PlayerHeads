/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.glowstone_1_12;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.github.crashdemons.playerheads.compatibility.CompatibleProfile;
import com.github.crashdemons.playerheads.compatibility.legacy.Provider_legacy;
import com.github.crashdemons.playerheads.compatibility.paperapi.CompatibleProfilePA;
import com.github.crashdemons.playerheads.compatibility.paperapi.ProfileUtils;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public abstract class Provider_paperapi_112 extends Provider_legacy {
    
    @Override public OfflinePlayer getOwningPlayerDirect(SkullMeta skullItemMeta){ return skullItemMeta.getOwningPlayer(); }
    @Override public OfflinePlayer getOwningPlayerDirect(Skull skullBlockState){ return skullBlockState.getOwningPlayer(); }
    @Override public OfflinePlayer getOwningPlayer(SkullMeta skull){ return getOwningPlayerDirect(skull); }
    @Override public OfflinePlayer getOwningPlayer(Skull skull){ return getOwningPlayerDirect(skull); }
    
    @Override public boolean setProfile(ItemMeta headMeta, UUID uuid, String texture){
        return ProfileUtils.setProfile(headMeta, uuid, texture);
    }
    @Override public boolean setProfile(Skull skull, UUID uuid, String texture){
        //return ProfileUtils.setProfile(headBlockState, uuid, texture);
        //TODO: find glowstone implementations for texturing!
        //OfflinePlayer op=Bukkit.getOfflinePlayer(uuid);
        //setPlayerProfile(headBlockState, createProfile(uuid,texture));
        //headBlockState.setPlayerProfile(createProfile(uuid,texture));// doesn't exist in this version of paper-api
        return ProfileUtils.setProfile(skull, uuid, texture);
    }
    
    //-----------5.2.12 providers-----------//
    @Override
    public Object getProfile(ItemMeta headMeta) throws IllegalStateException{
        if(headMeta instanceof SkullMeta){
            SkullMeta skull = (SkullMeta) headMeta;
            return skull.getPlayerProfile();
        }else{
            return null;
        }
    }
    
    @Override
    public Object getProfile(Skull headBlockState) throws IllegalStateException{
        //headBlockState.getPlayerProfile();//NOTE: this does not exist in Paper-API 1.12.2
        throw new IllegalStateException("Retrieving Profiles from BlockStates is not supported in Paper-api-1.12.2");
    }
    
    
    
    @Override
    public boolean setProfile(ItemMeta headMeta, Object profile) throws IllegalStateException, IllegalArgumentException{
        if(!(profile instanceof PlayerProfile)) throw new IllegalArgumentException("Argument passed was not a PlayerProfile");
        if(headMeta instanceof SkullMeta){
            SkullMeta skull = (SkullMeta) headMeta;
            skull.setPlayerProfile((PlayerProfile) profile);//NOTE: Paper-API defines this as a void method, so it is assumed to succeed.
            return true;
        }else{
            return false;
        }
    }
    
    
    @Override
    public boolean setProfile(Skull headBlockState, Object profile) throws IllegalStateException, IllegalArgumentException{
        if(!(profile instanceof PlayerProfile)) throw new IllegalArgumentException("Argument passed was not a PlayerProfile");
         throw new IllegalStateException("Setting Profiles from BlockStates is not supported in Paper-api-1.12.2");
         //skull.setPlayerProfile((PlayerProfile) profile);//NOTE: This method does not exist in Paper-API 1.12.2
         //return true;
    }
    
    //--------5.2.13 providers -----//
    
    @Override
    public boolean setCompatibleProfile(Object skull, CompatibleProfile profile) throws IllegalArgumentException{
        return ProfileUtils.setProfile(skull, profile);
    }
    @Override
    public CompatibleProfile getCompatibleProfile(Object skull) throws IllegalArgumentException{
        return ProfileUtils.getProfile(skull);
    }
    
    @Override
    public CompatibleProfile createCompatibleProfile(@Nullable String name, @Nullable UUID id, @Nullable String texture){
        CompatibleProfile profile = new CompatibleProfilePA(id,name);
        profile.setTextures(texture);
        return profile;
    }
    
    @Override
    public boolean clearProfile(Object skull) throws IllegalArgumentException{
        return ProfileUtils.clearProfile(skull);
    }
}
