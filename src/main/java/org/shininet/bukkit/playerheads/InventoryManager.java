/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.playerheads;

import com.github.crashdemons.playerheads.SkullManager;
import com.github.crashdemons.playerheads.TexturedSkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Defines a collection of utility methods for the plugin inventory management, item management
 * @author meiskam
 */
public class InventoryManager {
    
    /**
     * Adds a head-item to a player's inventory.
     * 
     * The quantity of heads added to the inventory is controlled by Config.defaultStackSize (usually 1).
     * @param player the player receiving the head.
     * @param skullOwner the owner username of the head-item, or the "spawn" string for a supported skull.
     * @param usevanillaskulls whether vanilla mobheads are permitted (if relevant to the owner parameter).
     * @return true: the head was added successfully. false: there was no empty inventory slot to add the item.
     * @see Config#defaultStackSize
     */
    @SuppressWarnings("unused")
    public static boolean addHead(Player player, String skullOwner, boolean usevanillaskulls) {
        return addHead(player, skullOwner, Config.defaultStackSize, usevanillaskulls);
    }

    /**
     * Adds a head-item to a player's inventory.
     * @param player the player receiving the head.
     * @param skullOwner the owner username of the head-item, or the "spawn" string for a supported skull.
     * @param quantity the number of this item to add
     * @param usevanillaskulls whether vanilla mobheads are permitted (if relevant to the owner parameter).
     * @return true: the head was added successfully. false: there was no empty inventory slot to add the item.
     */
    public static boolean addHead(Player player, String skullOwner, int quantity, boolean usevanillaskulls) {
        PlayerInventory inv = player.getInventory();
        int firstEmpty = inv.firstEmpty();
        if (firstEmpty == -1) {
            return false;
        } else {
            inv.setItem(firstEmpty, Skull(skullOwner, quantity, usevanillaskulls));
            return true;
        }
    }
        /**
     * Creates a skull or head itemstack as indicated by the input.
     * @param skullOwner the owner username of the head-item, or the "spawn" string for a supported skull.
     * @param usevanillaskulls whether vanilla mobheads are permitted (if relevant to the owner parameter).
     * @return The itemstack desired.
     */
    public static ItemStack Skull(String skullOwner, boolean usevanillaskulls) {
        return Skull(skullOwner, Config.defaultStackSize, usevanillaskulls);
    }

    /**
     * Creates a skull or head itemstack as indicated by the input.
     * @param skullOwner the owner username of the head-item, or the "spawn" string for a supported skull.
     * @param quantity the number of this item to have in the stack.
     * @param usevanillaskulls whether vanilla mobheads are permitted (if relevant to the owner parameter).
     * @return The itemstack desired.
     */
    public static ItemStack Skull(String skullOwner, int quantity, boolean usevanillaskulls) {
        String skullOwnerLC = skullOwner.toLowerCase();

        for (TexturedSkullType skullType : TexturedSkullType.values()) {
            if (skullOwnerLC.equals(skullType.getSpawnName().toLowerCase())) {
                
                return SkullManager.MobSkull(skullType, quantity, usevanillaskulls);
            }
        }
        return SkullManager.PlayerSkull(skullOwner,quantity);
    }

}
