/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

/**
 * An enumeration of skulls and heads that are supported in vanilla servers.
 * 
 * This enum corresponds in ordinal value to entries of the deprecated Bukkit SkullType enum,
 * however the names here instead generally correspond to the vanilla item name (Material).
 * 
 * This enum adds information to entries about whether each type is a proper skull or a head - which is relevant to specific implementation materials and naming.
 * @author crashdemons (crashenator at gmail.com)
 * @see org.bukkit.Material
 * @see org.bukkit.SkullType
 */
public enum SkullType {
//just in case this gets removed with deprecation of SkullType in bukkit
//note: must maintain same ordinal values as SkullType (ordering and number)
    SKELETON(true), WITHER_SKELETON(true), ZOMBIE(false), PLAYER(false), CREEPER(false), DRAGON(false);
    
    /**
     * A property specifying whether the skulltype entry is a proper skull or a head.
     */
    public final boolean isSkull;
    
    SkullType(boolean skull){ isSkull=skull; }
    
}
