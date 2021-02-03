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
 * Type of head blocks supported
 * @author crashdemons (crashenator at gmail.com)
 * @since 5.2.14-SNAPSHOT
 */
public enum SkullBlockAttachment {
    FLOOR,
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
    
    public static boolean isValidOrientation(BlockFace rotation, SkullBlockAttachment attachment){
        if(attachment==SkullBlockAttachment.WALL) return WALL_DIRECTIONS.contains(rotation);
        return FLOOR_DIRECTIONS.contains(rotation);
    }
    
    public boolean isValidOrientation(BlockFace rotation){
        return isValidOrientation(rotation,this);
    }
}
