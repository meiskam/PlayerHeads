/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import com.github.crashdemons.playerheads.TexturedSkullType;
import com.github.crashdemons.playerheads.compatibility.RuntimeReferences;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

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
        DOUBLE, BOOLEAN, INT, LONG, STRING, STRINGLIST;
        
        @Deprecated
        public static configType LIST = configType.STRINGLIST;
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
                put(skullType.getConfigName().toLowerCase(), configType.DOUBLE);
            }
            put("fixcase", configType.BOOLEAN);
            put("updatecheck", configType.BOOLEAN);
            put("broadcast", configType.BOOLEAN);
            put("broadcastrange", configType.INT);
            put("broadcastmob", configType.BOOLEAN);
            put("broadcastmobrange", configType.INT);
            put("antideathchest", configType.BOOLEAN);
            put("dropboringplayerheads", configType.BOOLEAN);
            put("dropvanillaheads", configType.BOOLEAN);
            put("convertvanillaheads", configType.BOOLEAN);
            put("nerfdeathspam", configType.BOOLEAN);
            put("addlore", configType.BOOLEAN);
            
            
            put("requireitem", configType.BOOLEAN);
            put("requireditems", configType.STRINGLIST);
            
            put("considermobkillers", configType.BOOLEAN);     
              
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
    
    static String getValueDisplayString(FileConfiguration configFile, String key){
        Object configValue = configFile.get(key);
        String value = ""+configValue; //converted value from type
        if(configValue==null) return "(unset)";
        configType type = configKeys.get(key);
        if(type==null) type=configType.STRING;
        switch(type){
            case STRING:
                return '"'+value+'"';
            case DOUBLE:
                try {
                    double d = configFile.getDouble(key); //actual interpreted double value
                    return value + " (" + d + ")";
                } catch (Exception e) {
                    return value + " (?)";
                }
            default:
                return value;
        }
    }
    
    private static boolean getBooleanInputValue(String inputValue){
        String value = inputValue.toLowerCase();
        if(value.equals("true") || value.equals("yes") || value.equals("1")) return true;
        return false;
    }
    
    static void setValue(FileConfiguration configFile, String inputKey, String inputValue) throws NumberFormatException{
        String key = inputKey.toLowerCase();
        
        configType type = configKeys.get(key); //we can do this directly without the convoluted lookup since both input and recorded keys are lowercase.
        if(type==null) type=configType.STRING;
        try {
            switch (type) {
                case STRING:
                    configFile.set(key, inputValue);
                    break;
                case BOOLEAN:
                    configFile.set(key, getBooleanInputValue(inputValue));
                    break;
                case DOUBLE:
                    configFile.set(key, Double.parseDouble(inputValue));
                    break;
                case INT:
                    configFile.set(key, Integer.parseInt(inputValue));
                    break;
                case LONG:
                    configFile.set(key, Long.parseLong(inputValue));
                    break;
                case STRINGLIST:
                    String[] materials = inputValue
                            .replace('[', ',')
                            .replace(']', ',')
                            .toLowerCase()
                            .split("[, ]");
                    List<String> configMats = new ArrayList<>();
                    for(String matname : materials){
                        Material mat = RuntimeReferences.getMaterialByName(matname.toUpperCase());
                        if(mat!=null) configMats.add(mat.name().toLowerCase());
                        else System.out.println("Unsupported material: "+matname);
                    }
                    configFile.set(key, configMats);
                    break;
                default:
                    throw new IllegalStateException("The specified configuration key has an unsupported data type");
            }
        } catch (NumberFormatException e) { throw e; }

    }
}
