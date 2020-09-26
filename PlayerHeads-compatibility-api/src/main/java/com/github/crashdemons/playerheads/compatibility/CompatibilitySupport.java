/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import java.util.HashMap;

/**
 * Class that holds supported compatibility implementations in this release.
 * By default, before shading, this is mostly empty and it is up to the
 * downstream projects to add support.
 * Downstream projects that shade the package into their project should exclude
 * or replace this class as needed.
 * 
 * @author crashdemons (crashenator at gmail.com)
 */
public final class CompatibilitySupport {
    /** static utility class - prevent instantiation */
    private CompatibilitySupport() {}
    /** state holder for use in isFinalized methods if necessary - this field is not to be used for outside checking. Use isFinalized() instead */
    private static final boolean FINALIZED = false;
    /**
     * Map containing the supported server implementations and their supported
     * versions.
     * This list must be in order of descending version numbers for each type.
     * Types do not have to be in any specific order.
     * For example: VERSIONS.put("craftbukkit", new Integer[][]{ {1,16},{1,13},{1,8} });
     */
    public static final HashMap<String, Integer[][]> VERSIONS = new HashMap<>();

    /**
     * Specifies whether compatibility support has been added by a downstream
     * project yet.
     * If this is false, the project was not created properly.
     *
     * @return whether support has been added
     */
    public static boolean isFinalized() {
        return false;
    }
}
