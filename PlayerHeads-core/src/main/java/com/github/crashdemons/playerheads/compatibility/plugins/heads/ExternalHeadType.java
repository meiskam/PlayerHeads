/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.plugins.heads;

import java.util.HashMap;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public enum ExternalHeadType {
    SLIMEFUN_HEAD("CSCoreLib",null,ExternalHeadHandling.NO_INTERACTION),
    
    
    ;
    
    private final String ownerName;
    private final UUID ownerID;
    private final ExternalHeadHandling handling;
    
    private static class Mapping{
        public static final HashMap<String,ExternalHeadType> nameToHead = new HashMap<>();
        public static final HashMap<UUID,ExternalHeadType> idToHead = new HashMap<>();
    }
    
    ExternalHeadType(String username, UUID uuid, ExternalHeadHandling handling){
        ownerName = username;
        ownerID = uuid;
        this.handling=handling;
        if(ownerName!=null) Mapping.nameToHead.put(username, this);
        if(ownerID!=null) Mapping.idToHead.put(uuid, this);
    }
    
    public ExternalHeadHandling getHandling(){
        return handling;
    }
    
    @Nullable
    public static ExternalHeadType get(String username){
        if(username==null) return null;
        return Mapping.nameToHead.get(username);
    }
    
    @Nullable
    public static ExternalHeadType get(UUID id){
        if(id==null) return null;
        return Mapping.idToHead.get(id);
    }
    
    
    
}
