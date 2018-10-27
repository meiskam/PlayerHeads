/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public interface SkullDetails {
    public boolean isVariant();
    public boolean isBackedByPlayerhead();
    public boolean isSkinnable();
    public ItemStack createItemStack(int quantity);
    public Material getItemMaterial();
    public Material getFloorMaterial();
    public Material getWallMaterial();
}
