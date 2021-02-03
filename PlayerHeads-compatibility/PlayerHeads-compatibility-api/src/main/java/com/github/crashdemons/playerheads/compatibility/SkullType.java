/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

/**
 * An enumeration of skulls and heads that are supported in vanilla servers.
 * <p>
 * This enum corresponds in ordinal value to entries of the deprecated Bukkit
 * SkullType enum,
 * however the names here instead generally correspond to the vanilla item name
 * (Material).
 * <p>
 * This enum adds information to entries about whether each type is a proper
 * skull or a head - which is relevant to specific implementation materials and
 * naming.
 *
 * @author crashdemons (crashenator at gmail.com)
 * @see org.bukkit.Material
 * @see org.bukkit.SkullType
 */
public enum SkullType {
//just in case this gets removed with deprecation of SkullType in bukkit
//note: must maintain same ordinal values as SkullType (ordering and number)
    SKELETON(true,0), WITHER_SKELETON(true,1), ZOMBIE(false,2), PLAYER(false,3), CREEPER(false,4), DRAGON(false,5);

    /**
     * A property specifying whether the skulltype entry is a proper skull (semantically - for types of skeletons, etc) or a
     * head.
     */
    public final boolean isSkull;
    public final short legacyDataValue;

    SkullType(final boolean skull, final int legacyDV) {
        isSkull = skull;
        legacyDataValue = (short) legacyDV;
    }
}
