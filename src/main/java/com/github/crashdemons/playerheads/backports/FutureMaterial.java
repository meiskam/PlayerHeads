/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.backports;

import org.bukkit.SkullType;

/**
 *
 * @author crash
 */
public enum FutureMaterial {
    CREEPER_HEAD(SkullType.CREEPER),
    CREEPER_WALL_HEAD(SkullType.CREEPER),
    
    SKELETON_SKULL(SkullType.SKELETON),
    SKELETON_WALL_SKULL(SkullType.SKELETON),
    
    WITHER_SKELETON_SKULL(SkullType.WITHER),
    WITHER_SKELETON_WALL_SKULL(SkullType.WITHER),
    
    ZOMBIE_HEAD(SkullType.ZOMBIE),
    ZOMBIE_WALL_HEAD(SkullType.ZOMBIE),
    
    
    //not until 1.9
    DRAGON_HEAD(null),
    DRAGON_WALL_HEAD(null),
    
    
    PLAYER_HEAD(SkullType.PLAYER),
    PLAYER_WALL_HEAD(SkullType.PLAYER),
    
    
    UNSUPPORTED(null);
    
    
    
    private SkullType skullType=null;
    
    
    FutureMaterial(SkullType oldSkullType){
        skullType = oldSkullType;
    }
    
    SkullType getSkullType(){return skullType;}
}
