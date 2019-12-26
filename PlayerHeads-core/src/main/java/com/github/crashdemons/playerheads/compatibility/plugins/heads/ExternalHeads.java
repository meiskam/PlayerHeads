/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.plugins.heads;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class ExternalHeads {
    private ExternalHeads(){}
    
    private static final HashMap<String,ExternalHeadHandling> nameToHandling = new HashMap<>();
    private static final HashMap<UUID,ExternalHeadHandling> idToHandling = new HashMap<>();
    
    public static void loadNamesFromConfig(ConfigurationSection section, String key, ExternalHeadHandling handling){
        nameToHandling.clear();
        List<String> names = section.getStringList(key);
        if(names==null) return;
        for(String name : names)
            nameToHandling.put(name,handling);
    }
    
    public static void loadIdsFromConfig(ConfigurationSection section, String key, ExternalHeadHandling handling){
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
    
    @Nullable
    public static ExternalHeadHandling getHandling(String username){
        if(username==null) return null;
        return nameToHandling.get(username);
    }
    
    @Nullable
    public static ExternalHeadHandling getHandling(UUID id){
        if(id==null) return null;
        return idToHandling.get(id);
    }
    
    
}
