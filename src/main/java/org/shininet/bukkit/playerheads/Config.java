/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.HashMap;
import java.util.Map;

/**
 * @author meiskam
 */

public class Config {

    public enum configType {
        DOUBLE, BOOLEAN, INT
    }

    @SuppressWarnings("serial")
    public static final Map<String, configType> configKeys = new HashMap<String, configType>() {
        {
            put("pkonly", configType.BOOLEAN);
            put("droprate", configType.DOUBLE);
            put("lootingrate", configType.DOUBLE);
            put("mobpkonly", configType.BOOLEAN);
            put("creeperdroprate", configType.DOUBLE);
            put("zombiedroprate", configType.DOUBLE);
            put("skeletondroprate", configType.DOUBLE);
            put("witherdroprate", configType.DOUBLE);
            for (CustomSkullType customSkullType : CustomSkullType.values()) {
                put(customSkullType.name().replace("_", "").toLowerCase() + "droprate", configType.DOUBLE);
            }
            put("fixcase", configType.BOOLEAN);
            put("updatecheck", configType.BOOLEAN);
            put("broadcast", configType.BOOLEAN);
            put("broadcastrange", configType.INT);
            put("antideathchest", configType.BOOLEAN);
            put("dropboringplayerheads", configType.BOOLEAN);
        }
    };
    public static final String configKeysString = String.join(", ", configKeys.keySet());
    public static final int defaultStackSize = 1;
    public static final String updateSlug = "player-heads";
    public static final int updateID = 46244;

}
