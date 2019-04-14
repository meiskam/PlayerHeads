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
     */
    public static final HashMap<String,Integer[][]> versions;
    static{
        versions=new HashMap<>();
        versions.put("craftbukkit", new Integer[][]{
            {1,13},
            {1,8}
        });
        versions.put("glowstone", new Integer[][]{
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
