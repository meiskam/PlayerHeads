/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.bukkit_1_13;

import com.github.crashdemons.playerheads.compatibility.RuntimeReferences;
import com.github.crashdemons.playerheads.compatibility.SkullDetails;
import com.github.crashdemons.playerheads.compatibility.SkullType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
class SkullDetails_113 implements SkullDetails{
    Material material;
    Material materialWall;
    private final SkullType skullType;
    public SkullDetails_113(SkullType type){
        if(type==null){
            type=SkullType.PLAYER;
            material=Material.PLAYER_HEAD;
            materialWall=Material.PLAYER_WALL_HEAD;
        }
        
        String typeName=type.name();
        String suffix=type.isSkull? "_SKULL" : "_HEAD";
        material=RuntimeReferences.getMaterialByName(typeName+suffix);
        materialWall=RuntimeReferences.getMaterialByName(typeName+"_WALL"+suffix);
        if(material==null){
            type=SkullType.PLAYER;
            material=Material.PLAYER_HEAD;
            materialWall=Material.PLAYER_WALL_HEAD;
        }
        this.skullType=type;
    }
    @Override public boolean isVariant(){ return false; }
    @Override public boolean isBackedByPlayerhead(){ return material==Material.PLAYER_HEAD; }
    @Override public boolean isSkinnable(){ return isBackedByPlayerhead(); }
    @Override public ItemStack createItemStack(int quantity){ return new ItemStack(material,quantity); }
    @Override public Material getItemMaterial(){ return material; }
    @Override public Material getFloorMaterial(){ return material; }
    @Override public Material getWallMaterial(){ return materialWall; }
}
