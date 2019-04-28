/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.common;

import com.github.crashdemons.playerheads.compatibility.CompatibilityProvider;
import com.github.crashdemons.playerheads.compatibility.SkullType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Tameable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * CompatibilityProvider Implementation for 1.8-1.12.2 support.
 * @author crashdemons (crashenator at gmail.com)
 */
@SuppressWarnings( "deprecation" )
abstract public class Provider_common implements CompatibilityProvider {

    @Override public String getOwnerDirect(SkullMeta skullItemMeta){ return skullItemMeta.getOwner(); }
    @Override public String getOwnerDirect(Skull skullBlockState){ return skullBlockState.getOwner(); }
    @Override public boolean setOwner(SkullMeta skullItemMeta, String owner){ return skullItemMeta.setOwner(owner); }
    @Override public boolean setOwner(Skull skullBlockState, String owner){ return skullBlockState.setOwner(owner); }
    
    
    
    @Override public boolean isHead(ItemStack s){ return getSkullType(s)!=null; }
    @Override public boolean isHead(BlockState s){ return getSkullType(s)!=null; }
    @Override public boolean isPlayerhead(ItemStack s){ return getSkullType(s)==SkullType.PLAYER; }
    @Override public boolean isPlayerhead(BlockState s){ return getSkullType(s)==SkullType.PLAYER; }
    @Override public boolean isMobhead(ItemStack s){ SkullType t=getSkullType(s); return (t!=null && t!=SkullType.PLAYER);}
    @Override public boolean isMobhead(BlockState s){ SkullType t=getSkullType(s); return (t!=null && t!=SkullType.PLAYER);}
    @Override public String getCompatibleNameFromEntity(Entity e){
        if(isLegacyCat(e)) return "CAT";
        return e.getType().name().toUpperCase();
    }
    
    @Override public OfflinePlayer getOfflinePlayerByName(String username){ return Bukkit.getOfflinePlayer(username); }
    

    protected boolean isLegacyCat(Entity e){
        if(e instanceof Ocelot && e instanceof Tameable)
            return ((Tameable) e).isTamed();
        
        return false;
    }
}