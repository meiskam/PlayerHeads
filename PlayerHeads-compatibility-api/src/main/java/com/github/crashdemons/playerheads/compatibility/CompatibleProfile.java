/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads.compatibility;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class that may be used by compatibility providers to handle head profile information.
 * A profile is defined as a username,id, and optional properties such as a texture.
 * Using this class is currently optional.
 * Implementations should treat a null or empty value for any field as not existing/not present.
 * @author crashdemons (crashenator at gmail.com)
 */
public abstract class CompatibleProfile{
    protected UUID id;
    protected String name;
    protected String texture;
    
    /**
     * Constructs an object containing information about a head.
     * @param id the UUID of the head (either a player's or a unique one for each head type)
     * @param name the username of the head (custom heads should be null)
     */
    public CompatibleProfile(@Nullable UUID id, @Nullable String name){
        this.id=id;
        this.name=name;
        this.texture=null;
        if(!hasName() && !hasId()) throw new IllegalArgumentException("Name or ID must be present for a valid profile.");
    }
    
    /**
     * Constructs a profile object from some implementation-defined representation of profiles
     * This method should be overridden to prevent exceptions
     * @param internalProfile 
     */
    public CompatibleProfile(Object internalProfile){
        throw new IllegalStateException("CompatibleProfile internal constructor not properly overridden");
    }
    
    /**
     * Sets fields/values of the profile object from an implementation-defined representation of profiles.
     * This method is overridden by each implementation.
     * If no internal implementation is available, providers should take CompatibleProfile itself instead of an exception.
     * @param internalProfile implementation-defined profile representation.
     */
    public abstract void setFromInternalObject(Object internalProfile);
    
    /**
     * Gets an implementation-defined representation for the profile information.
     * If no internal implementation is available, providers should return CompatibleProfile itself instead of null/exception
     * @return the implementation-defined profile object.
     */
    public abstract Object toInternalObject();
    
    
    /**
     * whether the id is present (not null)
     * @return whether the id is present (not null)
     */
    public boolean hasId(){ return id!=null ; }
    
    /**
     * whether the username is present (not null)
     * @return whether the username is present (not null)
     */
    public boolean hasName(){ return name!=null && !name.isEmpty(); }
    
    /**
     * whether the texture string is present (not null)
     * @return whether the texture string is present (not null)
     */
    public boolean hasTexture(){ return texture!=null && !texture.isEmpty(); }

    /**
     * Get the UUID associated with the head profile.
     * @return the id
     */
    @Nullable 
    public UUID getId() {
        return id;
    }

    /**
     * Set the UUID associated with the head profile.
     * This should be either the UUID of a player or a unique ID for each head type.
     * @param id 
     */
    public void setId(UUID id) {
        if(id==null && name.isEmpty()) throw new IllegalArgumentException("Name/ID both blank");
        this.id = id;
    }

    /**
     * The username associated with the head profile
     * @return the username
     */
    @Nullable 
    public String getName() {
        return name;
    }

    /**
     * Set the username associated with the head profile
     * @param name the username
     */
    public void setName(String name) {
        if(id==null && name.isEmpty()) throw new IllegalArgumentException("Name/ID both blank");
        this.name = name;
    }

    /**
     * Get the texture Base64 textures tag string associated with the profile.
     * @return textures string
     */
    @Nullable 
    public String getTextures() {
        return texture;
    }

    /**
     * Set the texture Base64 textures tag string associated with the profile.
     * @param texture textures string
     */
    public void setTextures(@Nullable String texture) {
        this.texture = texture;
    }
   
}
