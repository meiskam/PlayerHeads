/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Defines an interface of implementation-specific details and methods relating
 * to a skull item/block.
 *
 * Generally, a set of Skull implementation details is available for each
 * vanilla skull type.
 *
 * Depending on implemnentation, some of the block and item materials returned
 * by methods here may be the same or different.
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public interface SkullDetails {

    /**
     * Gets whether the skull is a variant type (legacy heads based on skeleton
     * skull variants).
     *
     * @return whether the skull is a variant type
     */
    public boolean isVariant();

    /**
     * Gets whether the skull must be implemented with a playerhead
     *
     * @return whether the skull must be implemented with a playerhead
     */
    public boolean isBackedByPlayerhead();

    /**
     * Gets whether this skull, as implemented, is texturable or
     * username-skinnable.
     *
     * @return whether this skull, as implemented, is texturable or
     * username-skinnable.
     */
    public boolean isSkinnable();

    /**
     * Create an ItemStack of a given quantity for this specific skull
     * implementation.
     *
     * @param quantity the number of skulls to create in the stack
     * @return the itemstack that was created
     */
    public ItemStack createItemStack(int quantity);
    
    /**
     * Sets a block in the world to the specific skull for this implementation.
     * implementation.
     *
     * @param loc the location of the block to set
     * @param rotation the rotation of the skull block to use
     * @param attachment whether the block should be attached to the wall or floor
     * @return the block set, or null if it cannot be set.
     * @since 5.2.14-SNAPSHOT
     */
    public Block setBlock(Location loc, BlockFace rotation, SkullBlockAttachment attachment);
    
    /**
     * Gets the block material for a specific block attachment.
     * For some older implementations, this will be a constant value.
     * If there is no type for the given attachment, a default type should be given instead. 
     * @param attachment how the block should be attached to others
     * @return The block material
     * @since 5.2.14-SNAPSHOT
     */
    @NotNull
    public Material getBlockMaterial(SkullBlockAttachment attachment);

    /**
     * Gets the bukkit material corresponding to an Item of this skull
     *
     * @return the material requested
     */
    public Material getItemMaterial();

    /**
     * Gets the bukkit material corresponding to a block of this skull placed on
     * a floor (not against a wall)
     *
     * @return the material requested
     */
    public Material getFloorMaterial();

    /**
     * Gets the bukkit material corresponding to a block of this skull placed
     * against a wall (not on a floor or other placement)
     *
     * @return the material requested
     */
    public Material getWallMaterial();
   
}
