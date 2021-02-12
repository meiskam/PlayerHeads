/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.craftbukkit_1_8;

import com.github.crashdemons.playerheads.compatibility.CompatibleProfile;
import com.github.crashdemons.playerheads.compatibility.craftbukkit.CompatibleProfileCB;
import com.github.crashdemons.playerheads.compatibility.craftbukkit.ProfileUtils;
import com.github.crashdemons.playerheads.compatibility.legacy.Provider_legacy;
import com.mojang.authlib.GameProfile;
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
public abstract class Provider_craftbukkit_18 extends Provider_legacy {
    
    @Override public boolean setProfile(ItemMeta headMeta, UUID uuid, String texture){
        return ProfileUtils.setProfile(headMeta, uuid, texture);
    }
    @Override public boolean setProfile(Skull headBlockState, UUID uuid, String texture){
        return ProfileUtils.setProfile(headBlockState, uuid, texture);
    }
    @Override public OfflinePlayer getOwningPlayer(SkullMeta skull){
        //OfflinePlayer op = getOwningPlayer(skull);//skullMeta.getOwningPlayer();
        //if(op!=null) return op;
        return ProfileUtils.getProfile(skull).getOwningPlayer();
    }
    @Override public OfflinePlayer getOwningPlayer(Skull skull){
        //OfflinePlayer op = getOwningPlayer(skull);//skullMeta.getOwningPlayer();
        //if(op!=null) return op;
        return ProfileUtils.getProfile(skull).getOwningPlayer();
    }
    @Override public OfflinePlayer getOwningPlayerDirect(SkullMeta skull){ return ProfileUtils.getProfile(skull).getOwningPlayer(); }
    @Override public OfflinePlayer getOwningPlayerDirect(Skull skull){ return ProfileUtils.getProfile(skull).getOwningPlayer(); }
    
    
    //-----------5.2.12 providers-----------//
    @Override
    public Object getProfile(ItemMeta headMeta) throws IllegalStateException{
        return ProfileUtils.getProfile(headMeta);
    }

    @Override
    public Object getProfile(Skull headBlockState) throws IllegalStateException{
        return ProfileUtils.getProfile(headBlockState);
    }

    @Override
    public boolean setProfile(ItemMeta headMeta, Object profile) throws IllegalArgumentException{
        if(!(profile instanceof GameProfile)) throw new IllegalArgumentException("Passed argument was not a GameProfile object");
        return ProfileUtils.setInternalProfile(headMeta, (GameProfile) profile);
    }
    
    @Override
    public boolean setProfile(Skull headBlockState, Object profile) throws IllegalArgumentException{
        if(!(profile instanceof GameProfile)) throw new IllegalArgumentException("Passed argument was not a GameProfile object");
        return ProfileUtils.setInternalProfile(headBlockState, (GameProfile) profile);
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
        CompatibleProfile profile = new CompatibleProfileCB(id,name);
        profile.setTextures(texture);
        return profile;
    }
    
    
    @Override
    public boolean clearProfile(Object skull) throws IllegalArgumentException{
        return ProfileUtils.clearProfile(skull);
    }
}
