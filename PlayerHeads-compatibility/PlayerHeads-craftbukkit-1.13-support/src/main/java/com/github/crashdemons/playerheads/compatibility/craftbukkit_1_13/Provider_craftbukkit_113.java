/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.craftbukkit_1_13;

import com.github.crashdemons.playerheads.compatibility.CompatibleProfile;
import com.github.crashdemons.playerheads.compatibility.craftbukkit.CompatibleProfileCB;
import com.github.crashdemons.playerheads.compatibility.craftbukkit.ProfileUtils;
import com.github.crashdemons.playerheads.compatibility.modern.Provider_modern;
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
public abstract class Provider_craftbukkit_113 extends Provider_modern {
    
    
    @Override public OfflinePlayer getOwningPlayerDirect(SkullMeta skullItemMeta){ return skullItemMeta.getOwningPlayer(); }
    @Override public OfflinePlayer getOwningPlayerDirect(Skull skullBlockState){ return skullBlockState.getOwningPlayer(); }
    //@Override public String getOwnerDirect(SkullMeta skullItemMeta){ return skullItemMeta.getOwner(); }
    //@Override public String getOwnerDirect(Skull skullBlockState){ return skullBlockState.getOwner(); }
    @Override public boolean setOwningPlayer(SkullMeta skullItemMeta, OfflinePlayer op){ return skullItemMeta.setOwningPlayer(op); }
    @Override public void    setOwningPlayer(Skull skullBlockState, OfflinePlayer op){ skullBlockState.setOwningPlayer(op); }
    //@Override public boolean setOwner(SkullMeta skullItemMeta, String owner){ return skullItemMeta.setOwner(owner); }
    //@Override public boolean setOwner(Skull skullBlockState, String owner){ return skullBlockState.setOwner(owner); }
    
    
    @Override public OfflinePlayer getOwningPlayer(SkullMeta skull){
        OfflinePlayer op = getOwningPlayerDirect(skull);//skullMeta.getOwningPlayer();
        if(op!=null) return op;
        return ProfileUtils.getProfile(skull).getOwningPlayer();
    }
    @Override public OfflinePlayer getOwningPlayer(Skull skull){
        OfflinePlayer op = getOwningPlayerDirect(skull);//skullMeta.getOwningPlayer();
        if(op!=null) return op;
        return ProfileUtils.getProfile(skull).getOwningPlayer();
    }
    
    
    @Override public String getOwner(SkullMeta skull){
        String owner=null;
        OfflinePlayer op = getOwningPlayerDirect(skull);//skullMeta.getOwningPlayer();
        if(op==null) op = ProfileUtils.getProfile(skull).getOwningPlayer();//this does happen on textured heads with a profile but without an OwningPlayer
        if(op!=null) owner=op.getName();
        if(owner==null) owner=getOwnerDirect(skull);//skullMeta.getOwner();
        return owner;
    }
    @Override public String getOwner(Skull skull){
        String owner=null;
        OfflinePlayer op = getOwningPlayerDirect(skull);//skullMeta.getOwningPlayer();
        if(op==null) op = ProfileUtils.getProfile(skull).getOwningPlayer();//this does happen on textured heads with a profile but without an OwningPlayer
        if(op!=null) owner=op.getName();
        if(owner==null) owner=getOwnerDirect(skull);//skullMeta.getOwner();
        return owner;
    }
    
    @Override public boolean setProfile(ItemMeta headMeta, UUID uuid, String texture){
        return ProfileUtils.setProfile(headMeta, uuid, texture);
    }
    @Override public boolean setProfile(Skull headBlockState, UUID uuid, String texture){
        return ProfileUtils.setProfile(headBlockState, uuid, texture);
    }
    
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
