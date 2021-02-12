/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.faketestserver_1_0;

import com.github.crashdemons.playerheads.compatibility.CompatibilityProvider;
import com.github.crashdemons.playerheads.compatibility.CompatibleProfile;
import com.github.crashdemons.playerheads.compatibility.RuntimeReferences;
import com.github.crashdemons.playerheads.compatibility.SkullDetails;
import com.github.crashdemons.playerheads.compatibility.SkullType;
import com.github.crashdemons.playerheads.compatibility.common.Provider_common;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

/**
 * CompatibilityProvider Implementation for 1.13+ support
 * @author crashdemons (crashenator at gmail.com)
 */
@SuppressWarnings( "deprecation" )
public class Provider extends Provider_common implements CompatibilityProvider {
    public Provider(){}
    @Override public String getType(){ return "faketestserver"; }
    @Override public String getVersion(){ return "1.0"; }
    @Override public OfflinePlayer getOwningPlayerDirect(SkullMeta skullItemMeta){ return skullItemMeta.getOwningPlayer(); }
    @Override public OfflinePlayer getOwningPlayerDirect(Skull skullBlockState){ return skullBlockState.getOwningPlayer(); }
    @Override public String getOwnerDirect(SkullMeta skullItemMeta){ return skullItemMeta.getOwner(); }
    @Override public String getOwnerDirect(Skull skullBlockState){ return skullBlockState.getOwner(); }
    @Override public boolean setOwningPlayer(SkullMeta skullItemMeta, OfflinePlayer op){ return false; }
    @Override public void    setOwningPlayer(Skull skullBlockState, OfflinePlayer op){  }
    @Override public boolean setOwner(SkullMeta skullItemMeta, String owner){ return false; }
    @Override public boolean setOwner(Skull skullBlockState, String owner){ return false; }
    @Override public ItemStack getItemInMainHand(Player p){ return null; }
    @Override public ItemStack getItemInMainHand(LivingEntity p){ return null; }
    @Override public void setItemInMainHand(Player p,ItemStack s){  }
    @Override public SkullDetails getSkullDetails(SkullType type){ return new SkullDetails_113(type); }
    @Override public boolean getKeepInventory(World world){ return false; }
    @Override public SkullType getSkullType(ItemStack s){ return getSkullType(s.getType()); }
    @Override public SkullType getSkullType(BlockState s){ return getSkullType(s.getType()); }
    @Override public boolean isHead(ItemStack s){ return getSkullType(s)!=null; }
    @Override public boolean isHead(BlockState s){ return getSkullType(s)!=null; }
    @Override public boolean isPlayerhead(ItemStack s){ return getSkullType(s)==SkullType.PLAYER; }
    @Override public boolean isPlayerhead(BlockState s){ return getSkullType(s)==SkullType.PLAYER; }
    @Override public boolean isMobhead(ItemStack s){ SkullType t=getSkullType(s); return (t!=null && t!=SkullType.PLAYER);}
    @Override public boolean isMobhead(BlockState s){ SkullType t=getSkullType(s); return (t!=null && t!=SkullType.PLAYER);}
    @Override public String getCompatibleNameFromEntity(Entity e){ return e.getType().name().toUpperCase(); }
    @Override public OfflinePlayer getOfflinePlayerByName(String username){ return null; }
    
    @Override public EntityType getEntityTypeFromTypename(String typename){ throw new IllegalStateException("using unsupported method in test"); }//TODO: test properly
    //@Override public boolean setProfile(Skull skull, UUID id, String texture){throw new IllegalStateException("using unsupported method in test"); }//TODO: test properly
    
    
    
    @Override public OfflinePlayer getOwningPlayer(SkullMeta skull){
        return getOwningPlayerDirect(skull);
    }
    @Override public OfflinePlayer getOwningPlayer(Skull skull){
        return getOwningPlayerDirect(skull);
    }
    
    
    @Override public String getOwner(SkullMeta skull){
        return getOwnerDirect(skull);
    }
    @Override public String getOwner(Skull skull){
        return getOwnerDirect(skull);
    }
    
    @Override public boolean setProfile(ItemMeta headMeta, UUID uuid, String texture){
        return false;
    }
    @Override public boolean setProfile(Skull headBlockState, UUID uuid, String texture){
        return false;
    }
    
    private SkullType getSkullType(Material mat){
        String typeName = mat.name();
        typeName=typeName.replaceFirst("_WALL", "").replaceFirst("_HEAD", "").replaceFirst("_SKULL", "");
        return RuntimeReferences.getSkullTypeByName(typeName);
    }
    
    
    //-----------5.2.12 providers-----------//
    @Override
    public Object getProfile(ItemMeta headMeta) throws IllegalStateException{
        throw new IllegalStateException("Not supported by test class");
    }
    
    @Override
    public Object getProfile(Skull headBlockState) throws IllegalStateException{
        throw new IllegalStateException("Not supported by test class");
    }
    
    
    
    @Override
    public boolean setProfile(ItemMeta headMeta, Object profile) throws IllegalStateException, IllegalArgumentException{
        throw new IllegalStateException("Not supported by test class");
    }
    
    
    @Override
    public boolean setProfile(Skull headBlockState, Object profile) throws IllegalStateException, IllegalArgumentException{
        throw new IllegalStateException("Not supported by test class");
    }
    
    //-------------5.2.13 providers ----------------//
    public boolean setCompatibleProfile(Object skull, CompatibleProfile profile) throws IllegalArgumentException{
        throw new IllegalStateException("Not supported by test class");
    }
    public CompatibleProfile getCompatibleProfile(Object skull) throws IllegalArgumentException{
        throw new IllegalStateException("Not supported by test class");
    }
    
    
    public CompatibleProfile createCompatibleProfile(@Nullable String name, @Nullable UUID id, @Nullable String texture){
        throw new IllegalStateException("Not supported by test class");
    }
    
    public boolean clearProfile(Object o){
        throw new IllegalStateException("Not supported by test class");
    }
    
}
