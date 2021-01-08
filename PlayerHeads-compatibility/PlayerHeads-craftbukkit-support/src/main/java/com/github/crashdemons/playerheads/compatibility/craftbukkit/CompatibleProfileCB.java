/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.craftbukkit;

import com.github.crashdemons.playerheads.compatibility.CompatibleProfile;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class CompatibleProfileCB extends CompatibleProfile {
    
    public CompatibleProfileCB(){
        super();
    }
    public CompatibleProfileCB(UUID id, String name){
        super(id,name);
    }
    
    public CompatibleProfileCB(Object internalProfile){
         super();
        if(internalProfile==null) throw new IllegalArgumentException("Constructing from null Profile");
         setFromInternalObject(internalProfile);
    }
    
    
    private static GameProfile createInternalObject(UUID id, String name){
        return new GameProfile(id, name);
    }
    private static GameProfile setInternalTextures(GameProfile profile, String textures){
        if(CompatibleProfile.hasField(textures))
            profile.getProperties().put("textures", new Property("textures", textures));
        return profile;
    }
    private static GameProfile setInternalTextures(GameProfile profile, Collection<Property> textures){
        profile.getProperties().putAll("textures", textures);
        return profile;
    }
    private static Collection<Property> getInternalTextures(GameProfile profile){
        return profile.getProperties().get("textures");
    }
    private static Optional<Property> getInternalTexture(GameProfile profile){
        for(Property prop : getInternalTextures(profile)){
            if(prop.getName().equals("textures"))
                return Optional.of(prop);
        }
        return Optional.empty();
    }
    
    
    @Override
    public Object toInternalObject(){
        return setInternalTextures(createInternalObject(id,name), textures);
    }
    
    @Override
    public void setFromInternalObject(Object profileObj){
        if(!(profileObj instanceof GameProfile)) throw new IllegalArgumentException("Cannot read profile information - passed argument was not a GameProfile");
        GameProfile profile = (GameProfile) profileObj;
        id = profile.getId();
        name = profile.getName();
        
        Optional<Property> texturePropOptional = getInternalTexture(profile);
        if(texturePropOptional.isPresent()){
            Property textureProp = texturePropOptional.get();
            textures = textureProp.getValue();
        }
        if(!hasRequiredFields(id,name)) throw new IllegalArgumentException("Name or ID must be present for a valid profile.");
    }
    
    
}
