/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.craftbukkit_1_8;

import com.github.crashdemons.playerheads.compatibility.RuntimeReferences;
import com.github.crashdemons.playerheads.compatibility.SkullDetails;
import com.github.crashdemons.playerheads.compatibility.SkullType;
import com.github.crashdemons.playerheads.compatibility.Version;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * SkullDetails implementation for 1.8+ support
 * @author crashdemons (crashenator at gmail.com)
 */

class SkullDetails_18 implements SkullDetails {
    public final Material materialItem;
    public final Material materialBlock;
    public final short datavalue;
    private final SkullType skullType;
    
    public SkullDetails_18(SkullType skullType){
        materialBlock=Material.SKULL;
        materialItem=Material.SKULL_ITEM;
        if( skullType==null || (skullType==SkullType.DRAGON && Version.checkUnder(1, 9)) ){
            this.skullType=SkullType.PLAYER;
            datavalue=(short)SkullType.PLAYER.ordinal();
        }else{
            this.skullType=skullType;
            datavalue=(short) skullType.ordinal();
        }
    }
    
    @Override public boolean isVariant(){ return true; }//always SKULL_ITEM
    @Override public boolean isBackedByPlayerhead(){ return skullType==SkullType.PLAYER; }
    @Override public boolean isSkinnable(){ return isBackedByPlayerhead(); }
    @Override public ItemStack createItemStack(int quantity){ return new ItemStack(materialItem,quantity,datavalue); }
    @Override public Material getItemMaterial(){ return materialItem; }
    @Override public Material getFloorMaterial(){ return materialBlock; }
    @Override public Material getWallMaterial(){ return materialBlock; }
}
