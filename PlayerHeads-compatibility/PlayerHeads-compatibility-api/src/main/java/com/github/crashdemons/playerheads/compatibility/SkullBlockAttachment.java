/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads.compatibility;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.block.BlockFace;

/**
 * Enumeration of the different head-block attachment styles supported.
 * @author crashdemons (crashenator at gmail.com)
 * @since 5.2.14-SNAPSHOT
 */
public enum SkullBlockAttachment {
    /**
     * Denotes a head that is placed on top of a block (a "floor head").
     * That is, it is against/aligned-with the top side of the block it is above (a "floor")
     */
    FLOOR,
    /**
     * Denotes a head that is placed on the side of a block (a "wall head").
     * That is, it is against/aligned-with the horizontal side of some block (a "wall")
     */
    WALL;
    
    private static final Set<BlockFace> WALL_DIRECTIONS = new HashSet<>(Arrays.asList(
            BlockFace.EAST,
            BlockFace.WEST,
            BlockFace.NORTH,
            BlockFace.SOUTH
    ));
    private static final Set<BlockFace> FLOOR_DIRECTIONS = new HashSet<>(Arrays.asList(
            BlockFace.EAST,
            BlockFace.EAST_NORTH_EAST,
            BlockFace.EAST_SOUTH_EAST,
            BlockFace.NORTH,
            BlockFace.NORTH_EAST,
            BlockFace.NORTH_NORTH_EAST,
            BlockFace.NORTH_NORTH_WEST,
            BlockFace.NORTH_WEST,
            BlockFace.SOUTH,
            BlockFace.SOUTH_EAST,
            BlockFace.SOUTH_SOUTH_EAST,
            BlockFace.SOUTH_SOUTH_WEST,
            BlockFace.SOUTH_WEST,
            BlockFace.WEST,
            BlockFace.WEST_NORTH_WEST,
            BlockFace.WEST_SOUTH_WEST
    ));
    
    /**
     * Determines if the provided rotation is compatible with the provided head block attachment style.
     * This is relevant because floor-heads support many directions (Cardinal NSEW, intercardinal - NW,SE,etc and secondary intercardinal - NNW, etc.).
     * However, wall-heads only support four facing directions (NSEW).
     * @param rotation the rotation of the head
     * @param attachment the head block attachment style (FLOOR or WALL)
     * @return whether the rotation is valid for the attachment style
     */
    public static boolean isValidOrientation(BlockFace rotation, SkullBlockAttachment attachment){
        if(attachment==SkullBlockAttachment.WALL) return WALL_DIRECTIONS.contains(rotation);
        return FLOOR_DIRECTIONS.contains(rotation);
    }
    
    /**
     * Determines if the provided rotation is compatible with this head-block attachment style.
     * This is relevant because floor-heads support many directions (Cardinal NSEW, intercardinal - NW,SE,etc and secondary intercardinal - NNW, etc.).
     * However, wall-heads only support four facing directions (NSEW).
     * @param rotation the rotation of the head
     * @return whether the rotation is valid for the attachment style
     */
    public boolean isValidOrientation(BlockFace rotation){
        return isValidOrientation(rotation,this);
    }
}
