/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.backports;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author crash
 */
public class FutureItemStack extends org.bukkit.inventory.ItemStack{
    public FutureItemStack(FutureMaterial futureMaterial, int quantity){
        super(Material.SKULL_ITEM, quantity, (short) futureMaterial.getSkullTypeSafe().ordinal());
    }
}
