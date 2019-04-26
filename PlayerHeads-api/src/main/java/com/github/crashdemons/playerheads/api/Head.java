/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads.api;

import java.util.UUID;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public interface Head {
    
    /**
     * Get the UUID associated with the skulltype (randomly assigned to it specifically)
     * @return The UUID
     */
    public UUID getOwner();
    /**
     * Get the Base64-encoded texture string associated with the skulltype
     * @return A base64 string
     */
    public String getTexture();
    /**
     * Gets the item displayname for the associated skulltype, as defined in the "lang" file.
     * @return A string containing the skulltype's displayname
     */
    public String getDisplayName();
    
    /**
     * Checks whether the skulltype uses a playerhead item/material internally.
     * 
     * This indicates that either the skulltype is PLAYER or a mob without a vanilla head item, generally.
     * 
     * @return true: the skulls associated material was a playerhead. false: the skull has a different associated material.
     */
    public boolean isPlayerHead();
    
    public boolean equals(Head h);
}
