/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import java.util.HashMap;

//override default compatibilitysupport
public class CompatibilitySupport {
    public static final HashMap<String,Integer[][]> VERSIONS;
    static{
        VERSIONS=new HashMap<>();
        VERSIONS.put("faketestserver", new Integer[][]{//to enable testing
            {1,13},
            {1,0}
        });
    }
    public static boolean isFinalized(){ return true; }
}
