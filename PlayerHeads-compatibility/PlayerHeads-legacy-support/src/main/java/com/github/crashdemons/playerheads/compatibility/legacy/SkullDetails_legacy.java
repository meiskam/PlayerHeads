/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.legacy;

import com.github.crashdemons.playerheads.compatibility.Compatibility;
import com.github.crashdemons.playerheads.compatibility.SkullBlockAttachment;
import com.github.crashdemons.playerheads.compatibility.SkullDetails;
import com.github.crashdemons.playerheads.compatibility.SkullType;
import com.github.crashdemons.playerheads.compatibility.Version;
import com.github.crashdemons.playerheads.compatibility.common.SkullDetails_common;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;

/**
 * SkullDetails implementation for 1.8+ support
 * @author crashdemons (crashenator at gmail.com)
 */

public class SkullDetails_legacy extends SkullDetails_common implements SkullDetails {
    //public final Material materialItem;
    public final Material materialBlock;
    public final short datavalue;
    private final org.bukkit.SkullType bukkitSkullType;
    
    
    private enum LegacyHeadData{
        FLOOR(0x1,true),// On the floor (rotation is stored in the tile entity)
        WALL_NORTH(0x2,false),// On a wall, facing north
        WALL_SOUTH(0x3,false),// On a wall, facing south
        WALL_WEST(0x4,false),// On a wall, facing east
        WALL_EAST(0x5,false)// On a wall, facing west
        ;
        public final byte value;
        public final boolean needsTileData;
        private LegacyHeadData(int b, boolean needsMoreData){
            value = (byte) b;
            needsTileData = needsMoreData;
        }
        
        public static LegacyHeadData get(BlockFace rotation, SkullBlockAttachment attachment){
            if(attachment==SkullBlockAttachment.FLOOR) return LegacyHeadData.FLOOR;
            try{
                return LegacyHeadData.valueOf("WALL_"+rotation.name().toUpperCase());
            }catch(Exception e){
                return null;
            }
        }
    }
    
    public SkullDetails_legacy(SkullType skullType){
        materialBlock=Material.SKULL;
        materialItem=Material.SKULL_ITEM;
        if( skullType==null || (skullType==SkullType.DRAGON && Version.checkUnder(1, 9)) ){
            this.skullType=SkullType.PLAYER;
            datavalue=SkullType.PLAYER.legacyDataValue;
        }else{
            this.skullType=skullType;
            datavalue=skullType.legacyDataValue;
        }
        
        org.bukkit.SkullType foundBukkitSkullType = null;
        try{
            foundBukkitSkullType = org.bukkit.SkullType.valueOf(skullType.name().toUpperCase());
        }catch(Exception e){
            foundBukkitSkullType = null;//this should not happen
        }
        bukkitSkullType = foundBukkitSkullType;
        
    }
    
    @Override public boolean isVariant(){ return true; }//always SKULL_ITEM
    @Override public boolean isBackedByPlayerhead(){ return skullType==SkullType.PLAYER; }
    //@Override public boolean isSkinnable(){ return isBackedByPlayerhead(); }
    @Override public ItemStack createItemStack(int quantity){ return new ItemStack(materialItem,quantity,datavalue); }
    //@Override public Material getItemMaterial(){ return materialItem; }
    @Override public Material getFloorMaterial(){ return materialBlock; }
    @Override public Material getWallMaterial(){ return materialBlock; }
    
    
    private void debugBlockState(Block b, BlockState state, BlockFace rotation, SkullBlockAttachment attachment){
        System.out.println(" debug block "+b+" "+state+" | requested "+rotation+" "+attachment);
        System.out.println(" - block type "+b.getType()+" "+b.getTypeId());
        System.out.println(" - block data "+b.getData());
        System.out.println(" - state type "+state.getType()+" "+b.getTypeId());
        System.out.println(" - state data "+state.getData());
        System.out.println(" - state rawdata "+state.getRawData());
        
        
        if(state instanceof org.bukkit.block.Skull){
            org.bukkit.block.Skull skullState = (org.bukkit.block.Skull) state;
            System.out.println(" - - state skullstate "+skullState);
            System.out.println(" - - state skullstate rotation "+skullState.getRotation());
            System.out.println(" - - state skullstate skulltype "+skullState.getSkullType());
        }
        
        
        MaterialData matData = state.getData();
        System.out.println(" - materialdata "+matData);
        System.out.println(" - - materialdata type "+matData.getItemType()+" "+matData.getItemTypeId());
        System.out.println(" - - materialdata data "+matData.getData());
        if(matData instanceof Directional){
            Directional dir = (Directional) matData;
            System.out.println(" - - materialdata dir "+dir);
            System.out.println(" - - materialdata dir facing "+dir.getFacing());
        }
        if(matData instanceof org.bukkit.material.Skull){
            org.bukkit.material.Skull matSkull = (org.bukkit.material.Skull) matData;
            System.out.println(" - - materialdata matskull "+matSkull);
            System.out.println(" - - materialdata matskull facing "+matSkull.getFacing());
        }
    }
    
    
    @Override
    protected void setBlockDetails(Block b, BlockFace rotation, SkullBlockAttachment attachment){
        BlockState state = b.getState();
        //setBlockSkullType(b,state);
        //state.update();
        
        //BlockFaceXZ rotationXZ = BlockFaceXZ.from(rotation);
        //System.out.println("converted BlockFace."+rotation+" -> BlockFaceXZ."+rotationXZ);
        
        
        debugBlockState(b,b.getState(),rotation,attachment);

        if(attachment==SkullBlockAttachment.FLOOR){
            b.setData((byte) 1);
            final org.bukkit.block.Skull skullState = (org.bukkit.block.Skull) b.getState();
            skullState.setSkullType(bukkitSkullType);
            //skullState.setOwner(null);
            Compatibility.getProvider().clearProfile(skullState);
            skullState.setRotation(rotation);
            skullState.update();
        }
        if(attachment==SkullBlockAttachment.WALL){
            LegacyHeadData legacydata = LegacyHeadData.get(rotation, attachment);
            b.setData(legacydata.value);
            final org.bukkit.block.Skull skullState = (org.bukkit.block.Skull) b.getState();
            skullState.setSkullType(bukkitSkullType);
            //skullState.setOwner(null);
            Compatibility.getProvider().clearProfile(skullState);
            skullState.setRotation(BlockFace.NORTH);
            skullState.update();
        }
        
        debugBlockState(b,b.getState(),rotation,attachment);
        
        
        //setBlockOrientation(b,state,rotation,attachment);
    }
    
    
    @Override
    public Material getBlockMaterial(SkullBlockAttachment attachment){
        return materialBlock;
    }
    
    
}
