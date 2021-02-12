/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.plugins.heads;

import com.github.crashdemons.playerheads.compatibility.Compatibility;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

/**
 * Maintains a list of recognized custom-heads from other plugins and their recommended handling (modifiability by the current plugin).
 * @author crashdemons (crashenator at gmail.com)
 */
public class ExternalHeads {
    private ExternalHeads(){}
    
    private static final HashMap<String,HeadModificationHandling> nameToHandling = new HashMap<>();
    private static final HashMap<UUID,HeadModificationHandling> idToHandling = new HashMap<>();
    
    /**
     * Load a list of usernames from a configuration entry and records their recommended handling.
     * @param section
     * @param key
     * @param handling the recommended handling for these head usernames
     */
    public static void loadNamesFromConfig(ConfigurationSection section, String key, HeadModificationHandling handling){
        nameToHandling.clear();
        List<String> names = section.getStringList(key);
        if(names==null) return;
        for(String name : names)
            nameToHandling.put(name,handling);
    }
    
    /**
     * Load a list of UUIDs from a configuration entry and records their recommended handling.
     * @param section
     * @param key
     * @param handling the recommended handling for these head UUIDs
     */
    public static void loadIdsFromConfig(ConfigurationSection section, String key, HeadModificationHandling handling){
        idToHandling.clear();
        List<String> ids = section.getStringList(key);
        if(ids==null) return;
        for(String id : ids){
            UUID uuid = null;
            try{
                uuid = UUID.fromString(id);
                idToHandling.put(uuid,handling);
            }catch(IllegalArgumentException e){
                //do nothing - invalid UUID String, no logging facility for this internal class.
            }
        }
    }

    /**
     * Retrieves the recommended handling (modifiability) for previously-recorded head username
     * @param username
     * @return the handling
     */
    @Nullable
    public static HeadModificationHandling getHandling(String username){
        if(username==null) return null;
        return nameToHandling.get(username);
    }
    
    /**
     * Retrieves the recommended handling (modifiability) for previously-recorded head UUID
     * @param id
     * @return the handling
     */
    @Nullable
    public static HeadModificationHandling getHandling(UUID id){
        if(id==null) return null;
        return idToHandling.get(id);
    }
    
    
}
