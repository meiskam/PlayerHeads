/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.common;

import com.github.crashdemons.playerheads.compatibility.Compatibility;
import com.github.crashdemons.playerheads.compatibility.CompatibilityProvider;
import com.github.crashdemons.playerheads.compatibility.RuntimeReferences;
import com.github.crashdemons.playerheads.compatibility.SkullType;
import com.github.crashdemons.playerheads.compatibility.Version;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Tameable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * CompatibilityProvider Implementation for 1.8-1.12.2 support.
 *
 * @author crashdemons (crashenator at gmail.com)
 */
@SuppressWarnings("deprecation")
public abstract class Provider_common implements CompatibilityProvider {

    @Override
    public String getOwnerDirect(SkullMeta skullItemMeta) {
        return skullItemMeta.getOwner();
    }

    @Override
    public String getOwnerDirect(Skull skullBlockState) {
        return skullBlockState.getOwner();
    }

    @Override
    public boolean setOwner(SkullMeta skullItemMeta, String owner) {
        return skullItemMeta.setOwner(owner);
    }

    @Override
    public boolean setOwner(Skull skullBlockState, String owner) {
        return skullBlockState.setOwner(owner);
    }

    @Override
    public boolean isHead(ItemStack s) {
        return getSkullType(s) != null;
    }

    @Override
    public boolean isHead(BlockState s) {
        return getSkullType(s) != null;
    }

    @Override
    public boolean isPlayerhead(ItemStack s) {
        return getSkullType(s) == SkullType.PLAYER;
    }

    @Override
    public boolean isPlayerhead(BlockState s) {
        return getSkullType(s) == SkullType.PLAYER;
    }

    @Override
    public boolean isMobhead(ItemStack s) {
        SkullType t = getSkullType(s);
        return (t != null && t != SkullType.PLAYER);
    }

    @Override
    public boolean isMobhead(BlockState s) {
        SkullType t = getSkullType(s);
        return (t != null && t != SkullType.PLAYER);
    }

    @Override
    public String getCompatibleNameFromEntity(Entity e) {
        if(isZombiePigman(e)) return "ZOMBIFIED_PIGLIN";
        if (isLegacyCat(e)) {
            return "CAT";
        }
        return e.getType().name().toUpperCase();
    }

    @Override
    public OfflinePlayer getOfflinePlayerByName(String username) {
        return Bukkit.getOfflinePlayer(username);
    }
    
    //default implementation without version-specific name checking
    @Override
    public EntityType getEntityTypeFromTypename(String typename){
        try{
            return EntityType.valueOf(typename.toUpperCase());
        }catch(Exception e){
            return null;
        }
    }
    
    protected static final String ETYPE_ZOMBIE_PIGMAN_PRE116 = "PIG_ZOMBIE";
    protected static final String ETYPE_ZOMBIE_PIGMAN_POST116 = "ZOMBIFIED_PIGLIN";
    
    protected boolean isZombiePigmanTypename(String typename){
        typename=typename.toUpperCase();
        return typename!=null && (typename.equals(ETYPE_ZOMBIE_PIGMAN_PRE116) || typename.equals(ETYPE_ZOMBIE_PIGMAN_POST116));
    }
    
    
    private EntityType currentZombiePigmanType = null;
    protected EntityType getCurrentZombiePigmanType(){
        if(currentZombiePigmanType==null){
            if(Version.checkAtLeast(1, 16)) currentZombiePigmanType = RuntimeReferences.getEntityTypeByName(ETYPE_ZOMBIE_PIGMAN_POST116);
            else currentZombiePigmanType = RuntimeReferences.getEntityTypeByName(ETYPE_ZOMBIE_PIGMAN_PRE116);
        }
        return currentZombiePigmanType;
    }
    
    
    
    
    protected boolean isZombiePigman(Entity e){
        return (e instanceof PigZombie);
    }

    protected boolean isLegacyCat(Entity e) {
        if (e instanceof Ocelot && e instanceof Tameable) {
            return ((Tameable) e).isTamed();
        }

        return false;
    }
    
    
    
    //-----------5.2.12 providers-----------//
    @Override
    public Optional<Object> getOptionalProfile(ItemMeta skullMeta){
        try{
            return Optional.of(Compatibility.getProvider().getProfile(skullMeta));
        }catch(Exception e){
            return Optional.empty();
        }
    }
    @Override
    public Optional<Object> getOptionalProfile(Skull skullState){
        try{
            return Optional.of(Compatibility.getProvider().getProfile(skullState));
        }catch(Exception e){
            return Optional.empty();
        }
    }
    @Override
    public boolean setOptionalProfile(Skull skullState, Optional<Object> profile){
        if(!profile.isPresent()) return false;
        try{
            return Compatibility.getProvider().setProfile(skullState,profile.get());
        }catch(Exception e){
            return false;
        }
    }
    @Override
    public boolean setOptionalProfile(ItemMeta skullMeta, Optional<Object> profile){
        if(!profile.isPresent()) return false;
        try{
            return Compatibility.getProvider().setProfile(skullMeta,profile.get());
        }catch(Exception e){
            return false;
        }
    }
    
}
