/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import java.util.HashMap;

/**
 * Class that holds supported compatibility implementations in this release.
 * NOTE: This is an implementation-specific replacement of the core class, meant to replace it during shading.
 * By default, before shading, this is mostly empty and it is up to the downstream projects (like this one) to add support.
 * @author crashdemons (crashenator at gmail.com)
 */
public class CompatibilitySupport {
    /**
     * Map containing the supported server implementations and their supported versions.
     * This list must be in order of descending version numbers for each type.
     * Types do not have to be in any specific order.
     */
    public static final HashMap<String,Integer[][]> VERSIONS;
    static{
        VERSIONS=new HashMap<>();
        VERSIONS.put("craftbukkit", new Integer[][]{
            {1,16},
            {1,13},
            {1,8}
        });
        VERSIONS.put("glowstone", new Integer[][]{
            {1,12}
        });
    }
    /**
     * Specifies whether compatibility support has been added by a downstream project yet.
     * If this is false, the project was not created properly.
     * @return whether support has been added
     */
    public static boolean isFinalized(){ return true; }
}
