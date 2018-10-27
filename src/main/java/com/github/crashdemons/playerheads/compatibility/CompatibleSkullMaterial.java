/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;


/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public enum CompatibleSkullMaterial {//should maintain compatibility with EntityType
    SKELETON(SkullType.SKELETON),
    WITHER_SKELETON(SkullType.WITHER_SKELETON),
    ZOMBIE(SkullType.ZOMBIE),
    PLAYER(SkullType.PLAYER),
    CREEPER(SkullType.CREEPER),
    ENDER_DRAGON(SkullType.DRAGON)
    ;
    
    private final SkullType skullType;
    private SkullDetails cachedDetails=null;
    
    CompatibleSkullMaterial(SkullType type){
        skullType=type;
    }
    public SkullDetails getDetails(){
        if(cachedDetails==null) cachedDetails=Compatibility.getProvider().getSkullDetails(skullType);
        return cachedDetails;
    }
    
    public boolean isSupported(){
        return this==PLAYER || !getDetails().isBackedByPlayerhead();
    }
    
    public static CompatibleSkullMaterial get(SkullType type){
        if(type==null) return null;
        if(type==SkullType.DRAGON) return ENDER_DRAGON;//item to entity correlation here
        if(type==SkullType.WITHER_SKELETON) return WITHER_SKELETON;//item to entity correlation here
        return RuntimeReferences.getCompatibleMaterialByName(type.name());//otherwise, our SkullType has a 1:1 mapping with CompatibleSkullMaterial
    }
    public static CompatibleSkullMaterial get(ItemStack stack){
        return get(Compatibility.getProvider().getSkullType(stack));
    }
    public static CompatibleSkullMaterial get(BlockState state){
        return get(Compatibility.getProvider().getSkullType(state));
    }
    
}
