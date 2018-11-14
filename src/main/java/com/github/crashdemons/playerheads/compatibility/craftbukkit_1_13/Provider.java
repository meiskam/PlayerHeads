/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.craftbukkit_1_13;

import com.github.crashdemons.playerheads.ProfileUtils;
import com.github.crashdemons.playerheads.compatibility.Compatibility;
import com.github.crashdemons.playerheads.compatibility.CompatibilityProvider;
import com.github.crashdemons.playerheads.compatibility.RuntimeReferences;
import com.github.crashdemons.playerheads.compatibility.SkullDetails;
import com.github.crashdemons.playerheads.compatibility.SkullType;
import java.util.UUID;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * CompatibilityProvider Implementation for 1.13+ support
 * @author crashdemons (crashenator at gmail.com)
 */
@SuppressWarnings( "deprecation" )
public class Provider implements CompatibilityProvider {
    public Provider(){}
    @Override public String getType(){ return "craftbukkit"; }
    @Override public String getVersion(){ return "1.13"; }
    @Override public OfflinePlayer getOwningPlayerDirect(SkullMeta skullItemMeta){ return skullItemMeta.getOwningPlayer(); }
    @Override public OfflinePlayer getOwningPlayerDirect(Skull skullBlockState){ return skullBlockState.getOwningPlayer(); }
    @Override public String getOwnerDirect(SkullMeta skullItemMeta){ return skullItemMeta.getOwner(); }
    @Override public String getOwnerDirect(Skull skullBlockState){ return skullBlockState.getOwner(); }
    @Override public boolean setOwningPlayer(SkullMeta skullItemMeta, OfflinePlayer op){ return skullItemMeta.setOwningPlayer(op); }
    @Override public void    setOwningPlayer(Skull skullBlockState, OfflinePlayer op){ skullBlockState.setOwningPlayer(op); }
    @Override public boolean setOwner(SkullMeta skullItemMeta, String owner){ return skullItemMeta.setOwner(owner); }
    @Override public boolean setOwner(Skull skullBlockState, String owner){ return skullBlockState.setOwner(owner); }
    @Override public ItemStack getItemInMainHand(Player p){ return p.getEquipment().getItemInMainHand(); }
    @Override public void setItemInMainHand(Player p,ItemStack s){ p.getEquipment().setItemInMainHand(s); }
    @Override public SkullDetails getSkullDetails(SkullType type){ return new SkullDetails_113(type); }
    @Override public boolean getKeepInventory(World world){ return world.getGameRuleValue(GameRule.KEEP_INVENTORY); }
    @Override public SkullType getSkullType(ItemStack s){ return getSkullType(s.getType()); }
    @Override public SkullType getSkullType(BlockState s){ return getSkullType(s.getType()); }
    @Override public boolean isHead(ItemStack s){ return getSkullType(s)!=null; }
    @Override public boolean isHead(BlockState s){ return getSkullType(s)!=null; }
    @Override public boolean isPlayerhead(ItemStack s){ return getSkullType(s)==SkullType.PLAYER; }
    @Override public boolean isPlayerhead(BlockState s){ return getSkullType(s)==SkullType.PLAYER; }
    @Override public boolean isMobhead(ItemStack s){ SkullType t=getSkullType(s); return (t!=null && t!=SkullType.PLAYER);}
    @Override public boolean isMobhead(BlockState s){ SkullType t=getSkullType(s); return (t!=null && t!=SkullType.PLAYER);}
    @Override public String getCompatibleNameFromEntity(Entity e){ return e.getType().name().toUpperCase(); }
    
    
    @Override public OfflinePlayer getOwningPlayer(SkullMeta skull){
        OfflinePlayer op = getOwningPlayerDirect(skull);//skullMeta.getOwningPlayer();
        if(op!=null) return op;
        return ProfileUtils.getProfilePlayer(skull);
    }
    @Override public OfflinePlayer getOwningPlayer(Skull skull){
        OfflinePlayer op = getOwningPlayerDirect(skull);//skullMeta.getOwningPlayer();
        if(op!=null) return op;
        return ProfileUtils.getProfilePlayer(skull);
    }
    
    
    @Override public String getOwner(SkullMeta skull){
        String owner=null;
        OfflinePlayer op = getOwningPlayerDirect(skull);//skullMeta.getOwningPlayer();
        if(op==null) op = ProfileUtils.getProfilePlayer(skull);//this does happen on textured heads with a profile but without an OwningPlayer
        if(op!=null) owner=op.getName();
        if(owner==null) owner=getOwnerDirect(skull);//skullMeta.getOwner();
        return owner;
    }
    @Override public String getOwner(Skull skull){
        String owner=null;
        OfflinePlayer op = getOwningPlayerDirect(skull);//skullMeta.getOwningPlayer();
        if(op==null) op = ProfileUtils.getProfilePlayer(skull);//this does happen on textured heads with a profile but without an OwningPlayer
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
    
    private SkullType getSkullType(Material mat){
        String typeName = mat.name();
        typeName=typeName.replaceFirst("_WALL", "").replaceFirst("_HEAD", "").replaceFirst("_SKULL", "");
        return RuntimeReferences.getSkullTypeByName(typeName);
    }
    
    
}
