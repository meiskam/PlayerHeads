/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import com.github.crashdemons.playerheads.TexturedSkullType;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines the configuration for the plugin, keys, and datatypes.
 * 
 * <i>Note:</i> This documentation was inferred after the fact and may be inaccurate.
 * @author meiskam
 */

public final class Config {
    
    private Config(){}

    /**
     * The data-types used for each particular configuration value
     */
    public enum configType {
        DOUBLE, BOOLEAN, INT, LONG
    }

    /**
     * A map of the keys supported by the plugin configuration and their associated data-type expected.
     */
    @SuppressWarnings("serial")
    public static final Map<String, configType> configKeys = new HashMap<String, configType>() {
        {
            put("pkonly", configType.BOOLEAN);
            put("droprate", configType.DOUBLE);
            put("lootingrate", configType.DOUBLE);
            put("mobpkonly", configType.BOOLEAN);
            for (TexturedSkullType skullType : TexturedSkullType.values()) {
                if(skullType==TexturedSkullType.PLAYER) continue;
                put(skullType.getConfigName(), configType.DOUBLE);
            }
            put("fixcase", configType.BOOLEAN);
            put("updatecheck", configType.BOOLEAN);
            put("broadcast", configType.BOOLEAN);
            put("broadcastrange", configType.INT);
            put("broadcastmob", configType.BOOLEAN);
            put("broadmobcastrange", configType.INT);
            put("antideathchest", configType.BOOLEAN);
            put("dropboringplayerheads", configType.BOOLEAN);
            put("dropvanillaheads", configType.BOOLEAN);
            put("convertvanillaheads", configType.BOOLEAN);
            put("nerfdeathspam", configType.BOOLEAN);
            put("addlore", configType.BOOLEAN);
            
            
            put("clickspamcount", configType.INT);
            put("clickspamthreshold", configType.LONG);
            put("deathspamcount", configType.INT);
            put("deathspamthreshold", configType.LONG);
            put("fixdroppedheads",configType.BOOLEAN);
        }
    };
    /**
     * A string containing all of the supported configuration keys in a human-readable list.
     */
    public static final String configKeysString = String.join(", ", configKeys.keySet());//should this use Lang.COMMA_SPACE instead since it's used exclusively in user messages?
    /**
     * The default size of itemstack used when dropping or spawning heads.
     * 
     * (defaults to 1)
     */
    public static final int defaultStackSize = 1;
    /**
     * String used to identify the plugin page on Curse (used in links)
     */
    public static final String updateSlug = "player-heads";
    /**
     * The Project ID for the plugin on Curse (used by the updater)
     */
    public static final int updateID = 46244;

}
