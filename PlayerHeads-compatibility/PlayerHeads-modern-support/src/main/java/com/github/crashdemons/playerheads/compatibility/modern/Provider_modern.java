/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.modern;

import com.github.crashdemons.playerheads.compatibility.CompatibilityProvider;
import com.github.crashdemons.playerheads.compatibility.RuntimeReferences;
import com.github.crashdemons.playerheads.compatibility.SkullDetails;
import com.github.crashdemons.playerheads.compatibility.SkullType;
import com.github.crashdemons.playerheads.compatibility.Version;
import com.github.crashdemons.playerheads.compatibility.common.Provider_common;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * CompatibilityProvider Implementation for 1.13+ support
 * @author crashdemons (crashenator at gmail.com)
 */
@SuppressWarnings( "deprecation" )
public abstract class Provider_modern extends Provider_common implements CompatibilityProvider {
    public Provider_modern(){}

    
    @Override public ItemStack getItemInMainHand(LivingEntity p){ 
        EntityEquipment equipment = p.getEquipment();
        if(equipment == null) return null;
        return equipment.getItemInMainHand(); 
    }
    @Override public ItemStack getItemInMainHand(Player p){ return p.getEquipment().getItemInMainHand(); }
    @Override public void setItemInMainHand(Player p,ItemStack s){ p.getEquipment().setItemInMainHand(s); }
    @Override public SkullDetails getSkullDetails(SkullType type){ return new SkullDetails_modern(type); }
    @Override public boolean getKeepInventory(World world){ return world.getGameRuleValue(GameRule.KEEP_INVENTORY); }
    @Override public SkullType getSkullType(ItemStack s){ return getSkullType(s.getType()); }
    @Override public SkullType getSkullType(BlockState s){ return getSkullType(s.getType()); }
    //@Override public boolean isHead(ItemStack s){ return getSkullType(s)!=null; }
    //@Override public boolean isHead(BlockState s){ return getSkullType(s)!=null; }
    //@Override public boolean isPlayerhead(ItemStack s){ return getSkullType(s)==SkullType.PLAYER; }
    //@Override public boolean isPlayerhead(BlockState s){ return getSkullType(s)==SkullType.PLAYER; }
    //@Override public boolean isMobhead(ItemStack s){ SkullType t=getSkullType(s); return (t!=null && t!=SkullType.PLAYER);}
    //@Override public boolean isMobhead(BlockState s){ SkullType t=getSkullType(s); return (t!=null && t!=SkullType.PLAYER);}
    //@Override public String getCompatibleNameFromEntity(Entity e){
    //    if(Version.checkUnder(1, 14) && isLegacyCat(e)) return "CAT";
    //    return e.getType().name().toUpperCase();
    //}
    //@Override public OfflinePlayer getOfflinePlayerByName(String username){ return Bukkit.getOfflinePlayer(username); }
    

    
    protected SkullType getSkullType(Material mat){
        String typeName = mat.name();
        typeName=typeName.replaceFirst("_WALL", "").replaceFirst("_HEAD", "").replaceFirst("_SKULL", "");
        return RuntimeReferences.getSkullTypeByName(typeName);
    }
    
    @Override
    public EntityType getEntityTypeFromTypename(String typename){
        if(isZombiePigmanTypename(typename)) return getCurrentZombiePigmanType();
        return super.getEntityTypeFromTypename(typename);
    }
    
    @Override
    protected boolean isLegacyCat(Entity e){//remove the necessity for this check in 1.14
        if(Version.checkAtLeast(1, 14)) return false;
        return super.isLegacyCat(e);
    }

}
