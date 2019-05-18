/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads.api;

import com.github.crashdemons.playerheads.compatibility.SkullDetails;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * Object representing the type of Head supported by PlayerHeads. You can obtain
 * the headtype for various things through API methods - to compare head types,
 * you can use equals()
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public interface HeadType {

    // 4.10.0 API
    //------------------------------------------------------------------
    
    /**
     * Get the UUID associated with the skulltype (randomly assigned to it
     * specifically)
     *
     * @return The UUID
     */
    @NotNull
    public UUID getOwner();

    /**
     * Get the Base64-encoded texture string associated with the skulltype
     *
     * @return A base64 string
     */
    @NotNull
    public String getTexture();

    /**
     * Gets the item displayname for the associated skulltype, as defined in the
     * "lang" file.
     *
     * @return A string containing the skulltype's displayname
     */
    public String getDisplayName();

    /**
     * Checks whether the skulltype uses a playerhead internally. This indicates
     * that either the skulltype is PLAYER or a mob without a vanilla head item,
     * generally.
     *
     * Note: This does not necessarily indicate the material of the head - On
     * certain server versions, even Player-Head items are variants of Skeleton
     * skulls.
     *
     * @return true: the skulls is implemented using a playerhead. false: the
     * skull has a different associated item
     */
    public boolean isPlayerHead();

    /**
     * Determines if a head of this type is implemented with a vanilla mob head
     * or not [for the current server version].
     *
     * @return whether the head of is implemented with a vanilla mob head
     */
    public boolean isVanilla();

    /**
     * Get the underlying bukkit implementation details for the head on this
     * server version. Includes things like the material id for blocks, items,
     * skinnability, etc.
     *
     * @return an object providing implementation details
     */
    public SkullDetails getImplementationDetails();

    /**
     * Determine if this headtype is the same as another.
     *
     * @param h the headtype to check
     * @return whether the types were equal
     */
    public boolean equals(Object h);
    
    // 5.0.0 API
    //------------------------------------------------------------------
    /**
     * Return the associated Enum value for the HeadType
     * @return enum value
     */
    public Enum toEnum();
    
    @Override
    public int hashCode();
}
