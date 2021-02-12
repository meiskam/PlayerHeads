/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.common;

import com.github.crashdemons.playerheads.compatibility.SkullBlockAttachment;
import com.github.crashdemons.playerheads.compatibility.SkullDetails;
import com.github.crashdemons.playerheads.compatibility.SkullType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * SkullDetails implementation for 1.8+ support
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public abstract class SkullDetails_common implements SkullDetails {

    protected Material materialItem;
    protected SkullType skullType;
    

    @Override
    public boolean isSkinnable() {
        return isBackedByPlayerhead();
    }

    @Override
    public Material getItemMaterial() {
        return materialItem;
    }
    
    protected abstract void setBlockDetails(Block b, BlockFace rotation, SkullBlockAttachment attachment);
        //TODO: modern provider - set rotation (attachment already done by material type)
        //TODO: legacy provider - set rotation and attachment type.
    
    
    @Override
    public Block setBlock(Location loc, BlockFace rotation, SkullBlockAttachment attachment){
        World w = loc.getWorld();
        if(w==null) return null;
        Block b = w.getBlockAt(loc);
        if(b==null) return null;
        Material blockMat = getBlockMaterial(attachment);
        System.out.println("SkullDetails setblock - got material "+blockMat+" for attachment "+attachment+" rot "+rotation);//TODO: debug
        b.setType(blockMat);
        setBlockDetails(b, rotation, attachment);
        return b;
    }
    

}
