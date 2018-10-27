/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public enum SkullType {
//just in case this gets removed with deprecation of SkullType in bukkit
//note: must maintain same ordinal values as SkullType (ordering and number)
    SKELETON(true), WITHER_SKELETON(true), ZOMBIE(false), PLAYER(false), CREEPER(false), DRAGON(false);
    
    public final boolean isSkull;
    SkullType(boolean skull){ isSkull=skull; }
    
}
