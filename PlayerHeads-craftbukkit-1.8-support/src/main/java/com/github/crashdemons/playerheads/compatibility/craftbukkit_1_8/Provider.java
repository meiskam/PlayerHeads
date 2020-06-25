/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.craftbukkit_1_8;

import com.github.crashdemons.playerheads.compatibility.craftbukkit.ProfileUtils;
import com.github.crashdemons.playerheads.compatibility.CompatibilityProvider;
import com.github.crashdemons.playerheads.compatibility.legacy.Provider_legacy;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * CompatibilityProvider Implementation for 1.8-1.12.2 support.
 * @author crashdemons (crashenator at gmail.com)
 */
@SuppressWarnings( "deprecation" )
public class Provider extends Provider_legacy implements CompatibilityProvider {
    public Provider(){}
    @Override public String getType(){ return "craftbukkit"; }
    @Override public String getVersion(){ return "1.8"; }
    @Override public boolean setProfile(ItemMeta headMeta, UUID uuid, String texture){
        return ProfileUtils.setProfile(headMeta, uuid, texture);
    }
    @Override public boolean setProfile(Skull headBlockState, UUID uuid, String texture){
        return ProfileUtils.setProfile(headBlockState, uuid, texture);
    }
    @Override public OfflinePlayer getOwningPlayer(SkullMeta skull){
        //OfflinePlayer op = getOwningPlayer(skull);//skullMeta.getOwningPlayer();
        //if(op!=null) return op;
        return ProfileUtils.getProfilePlayer(skull);//same method as above in 1.8 implementation
    }
    @Override public OfflinePlayer getOwningPlayer(Skull skull){
        //OfflinePlayer op = getOwningPlayer(skull);//skullMeta.getOwningPlayer();
        //if(op!=null) return op;
        return ProfileUtils.getProfilePlayer(skull);//same method as above in 1.8 implementation
    }
    @Override public OfflinePlayer getOwningPlayerDirect(SkullMeta skullItemMeta){ return ProfileUtils.getProfilePlayer(skullItemMeta); }
    @Override public OfflinePlayer getOwningPlayerDirect(Skull skullBlockState){ return ProfileUtils.getProfilePlayer(skullBlockState); }
    /*
    //@Override public String getOwnerDirect(SkullMeta skullItemMeta){ return skullItemMeta.getOwner(); }
    //@Override public String getOwnerDirect(Skull skullBlockState){ return skullBlockState.getOwner(); }
    @Override public boolean setOwningPlayer(SkullMeta skullItemMeta, OfflinePlayer op){ return skullItemMeta.setOwner(op.getName()); }
    @Override public void    setOwningPlayer(Skull skullBlockState, OfflinePlayer op){ skullBlockState.setOwner(op.getName()); }
    //@Override public boolean setOwner(SkullMeta skullItemMeta, String owner){ return skullItemMeta.setOwner(owner); }
    //@Override public boolean setOwner(Skull skullBlockState, String owner){ return skullBlockState.setOwner(owner); }
    @Override public ItemStack getItemInMainHand(Player p){ return p.getEquipment().getItemInHand(); }
    @Override public void setItemInMainHand(Player p,ItemStack s){ p.getEquipment().setItemInHand(s); }
    @Override public SkullDetails getSkullDetails(SkullType type){ return new SkullDetails_legacy(type); }
    @Override public boolean getKeepInventory(World world){ return Boolean.valueOf(world.getGameRuleValue("keepInventory")); }
    @Override public SkullType getSkullType(ItemStack s){
        if(s.getType()!=Material.SKULL_ITEM) return null;
        short dmg = s.getDurability();
        try{
            return SkullType.values()[dmg];
        }catch(Exception e){
            return null;
        }
    }
    @Override public SkullType getSkullType(BlockState s){
        if(s.getType()!=Material.SKULL) return null;
        Skull skullState = (Skull) s;
        return adaptSkullType(skullState.getSkullType());
    }
    
    
    @Override public String getOwner(SkullMeta skull){
        String owner=null;
        OfflinePlayer op = getOwningPlayer(skull);//skullMeta.getOwningPlayer();
        //if(op==null) op = ProfileUtils.getProfilePlayer(skull);//this does happen on textured heads with a profile but without an OwningPlayer // same method as above in 1.8 implementation
        if(op!=null) owner=op.getName();
        if(owner==null) owner=getOwnerDirect(skull);//skullMeta.getOwner();
        return owner;
    }
    @Override public String getOwner(Skull skull){
        String owner=null;
        OfflinePlayer op = getOwningPlayer(skull);//skullMeta.getOwningPlayer();
        //if(op==null) op = ProfileUtils.getProfilePlayer(skull);//this does happen on textured heads with a profile but without an OwningPlayer // same method as above in 1.8 implementation
        if(op!=null) owner=op.getName();
        if(owner==null) owner=getOwnerDirect(skull);//skullMeta.getOwner();
        return owner;
    }
    
    
    
    //@Override public boolean isHead(ItemStack s){ return getSkullType(s)!=null; }
    //@Override public boolean isHead(BlockState s){ return getSkullType(s)!=null; }
    //@Override public boolean isPlayerhead(ItemStack s){ return getSkullType(s)==SkullType.PLAYER; }
    //@Override public boolean isPlayerhead(BlockState s){ return getSkullType(s)==SkullType.PLAYER; }
    //@Override public boolean isMobhead(ItemStack s){ SkullType t=getSkullType(s); return (t!=null && t!=SkullType.PLAYER);}
   // @Override public boolean isMobhead(BlockState s){ SkullType t=getSkullType(s); return (t!=null && t!=SkullType.PLAYER);}
    @Override public String getCompatibleNameFromEntity(Entity e){
        if(Version.checkUnder(1, 11)){//skeleton and zombie variants were separated after 1.11, and have their own EntityType
            if(e instanceof Horse){
                Horse.Variant var = ((Horse) e).getVariant();
                switch (var) {
                    case DONKEY:
                        return "DONKEY";
                    case MULE:
                        return "MULE";
                    case SKELETON_HORSE:
                        return "SKELETON_HORSE";
                    case UNDEAD_HORSE:
                        return "ZOMBIE_HORSE";
                    default:
                        return "HORSE";
                }
            }
            if(e instanceof Zombie){
                if(((Zombie) e).isVillager()) return "ZOMBIE_VILLAGER";
                if(e.getName().equalsIgnoreCase("HUSK")) return "HUSK";//there is no ZombieType/Zombie.Variant enum in bukkit 1.10 so we have no way of knowing what mobs are husks before 1.11
            }
            if(e instanceof Skeleton){//in 1.10, skeleton variants can be retrieved by SkeletonType
                SkeletonType skeleType = ((Skeleton) e).getSkeletonType();
                if(skeleType == SkeletonType.WITHER) return "WITHER_SKELETON";
                else if(skeleType.name().equalsIgnoreCase("STRAY")) return "STRAY";//added in 1.10, since this in 1.8 code we can't directly reference it.
            }
        }
        return super.getCompatibleNameFromEntity(e);
    }
    
    @Override public OfflinePlayer getOfflinePlayerByName(String username){ return Bukkit.getOfflinePlayer(username); }
    
    protected SkullType adaptSkullType(org.bukkit.SkullType bukkitType){
        try{
            return SkullType.values()[bukkitType.ordinal()];
        }catch(Exception e){
            return null;
        }
    }
    protected org.bukkit.SkullType adaptSkullType(SkullType compatType){
        if(compatType==SkullType.DRAGON && Version.checkUnder(1, 9)) return org.bukkit.SkullType.PLAYER;
        try{
            return org.bukkit.SkullType.values()[compatType.ordinal()];
        }catch(Exception e){
            return null;
        }
    }*/
}