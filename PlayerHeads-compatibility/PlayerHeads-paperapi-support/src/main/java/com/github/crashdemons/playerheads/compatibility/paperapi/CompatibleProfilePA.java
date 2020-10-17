/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.paperapi;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.crashdemons.playerheads.compatibility.CompatibleProfile;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class CompatibleProfilePA extends CompatibleProfile {
    
    public CompatibleProfilePA(UUID id, String name){
        super(id,name);
    }
    
    public CompatibleProfilePA(Object internalProfile){
         super();
        if(internalProfile==null) throw new IllegalArgumentException("Constructing from null Profile");
         setFromInternalObject(internalProfile);
    }
    
    private static Predicate<ProfileProperty> isTextureProperty = new Predicate<ProfileProperty>() {
        @Override public boolean test(ProfileProperty prop) {
            return prop.getName().equals("textures");
        }               
    };
    
    private static PlayerProfile createInternalObject(UUID id, String name){
        PlayerProfile profile = Bukkit.createProfile(id,name);
        return profile;
    }
    private static PlayerProfile setInternalTextures(PlayerProfile profile, String textures){
        if(!CompatibleProfile.hasField(textures)) return profile;
        profile.setProperty(new ProfileProperty("textures", textures));
        return profile;
    }
    private static PlayerProfile setInternalTextures(PlayerProfile profile, Set<ProfileProperty> textures){
        profile.setProperties(textures);
        return profile;
    }
    private static Set<ProfileProperty> getInternalTextures(PlayerProfile profile){
        return profile.getProperties().stream().filter(isTextureProperty).collect(Collectors.<ProfileProperty>toSet());//Note: fails to compile with Set<Object> != Set<ProfileProperty> if you don't use the typed method.
    }
    private static Optional<ProfileProperty> getInternalTexture(PlayerProfile profile){
        for(ProfileProperty prop : getInternalTextures(profile)){
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
        if(!(profileObj instanceof PlayerProfile)) throw new IllegalArgumentException("Cannot read profile information - passed argument was not a PlayerProfile");
        PlayerProfile profile = (PlayerProfile) profileObj;
        id = profile.getId();
        name = profile.getName();
        
        Optional<ProfileProperty> texturePropOptional = getInternalTexture(profile);
        if(texturePropOptional.isPresent()){
            ProfileProperty textureProp = texturePropOptional.get();
            textures = textureProp.getValue();
        }
        if(!hasRequiredFields(id,name)) throw new IllegalArgumentException("Name or ID must be present for a valid profile.");
    }
    
    
}
