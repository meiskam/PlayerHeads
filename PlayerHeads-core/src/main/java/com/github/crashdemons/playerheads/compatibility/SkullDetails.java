/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Defines an interface of implementation-specific details and methods relating to a skull item/block.
 * 
 * Generally, a set of Skull implementation details is available for each vanilla skull type.
 * 
 * Depending on implemnentation, some of the block and item materials returned by methods here may be the same or different.
 * @author crashdemons (crashenator at gmail.com)
 */
public interface SkullDetails {
    /**
     * Gets whether the skull is a variant type 
     * @return whether the skull is a variant type 
     */
    public boolean isVariant();
    /**
     * Gets whether the skull must be implemented with a playerhead
     * @return whether the skull must be implemented with a playerhead
     */
    public boolean isBackedByPlayerhead();
    /**
     * Gets whether this skull, as implemented, is texturable or username-skinnable.
     * @return whether this skull, as implemented, is texturable or username-skinnable.
     */
    public boolean isSkinnable();
    /**
     * Create an ItemStack of a given quantity for this specific skull implementation.
     * @param quantity the number of skulls to create in the stack
     * @return the itemstack that was created
     */
    public ItemStack createItemStack(int quantity);
    /**
     * Gets the bukkit material corresponding to an Item of this skull
     * @return the material requested
     */
    public Material getItemMaterial();
    /**
        * Gets the bukkit material corresponding to a block of this skull placed on a floor (not against a wall) 
     * @return the material requested
     */
    public Material getFloorMaterial();
    /**
     * Gets the bukkit material corresponding to a block of this skull placed against a wall (not on a floor or other placement) 
     * @return the material requested
     */
    public Material getWallMaterial();
}
