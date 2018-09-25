/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;


import com.github.crashdemons.playerheads.SkullManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import com.github.crashdemons.playerheads.TexturedSkullType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Defines a collection of utility methods for the plugin inventory management, item management, and message formatting.
 * @author meiskam
 */

@SuppressWarnings("WeakerAccess")
public class Tools {

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
            inv.setItem(firstEmpty, Tools.Skull(skullOwner, quantity, usevanillaskulls));
            return true;
        }
    }

    /**
     * Attempts to find the correct casing for the input username by searching online players, then offline players.
     * @param inputName the username to fix
     * @return the same username, but with the case changed, if there was a match.
     */
    public static String fixcase(String inputName) {
        String inputNameLC = inputName.toLowerCase();
        Player player = Bukkit.getServer().getPlayerExact(inputNameLC);

        if (player != null) {
            return player.getName();
        }

        for (OfflinePlayer offPlayer : Bukkit.getServer().getOfflinePlayers()) {
            if ((offPlayer.getName() != null) && (offPlayer.getName().toLowerCase().equals(inputNameLC))) {
                return offPlayer.getName();
            }
        }

        return inputName;
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
    
    /**
     * Formats a message with arguments with a list of parameters to use as replacements for those arguments.
     * 
     * Note: this method also translates ampersand-encoded color-codes.
     * @param text A string, potentially with arguments for replacement.
     * @param replacement a string or set of strings used to fill the replacement arguments.
     * @return The formatted string after replacement.
     */
    public static String format(String text, String... replacement) {
        String output = text;
        for (int i = 0; i < replacement.length; i++) {
            if (output != null) {
                if(replacement[i]==null){
                    
                    replacement[i]="<null:"+replacement.length+":>";
                }
                output = output.replace("%" + (i + 1) + "%", replacement[i]);
            }
        }
        //noinspection ConstantConditions
        return ChatColor.translateAlternateColorCodes('&', output);
    }

    /**
     * Formats a message with replacement arguments and transmits the message to a player.
     * 
     * See format() for more details.
     * @param player The player to send the message to
     * @param text the string to be formatted
     * @param replacement the replacement string or strings used to fill the replacement arguments.
     * @see #format(java.lang.String, java.lang.String...) 
     */
    public static void formatMsg(CommandSender player, String text, String... replacement) {
        player.sendMessage(format(text, replacement));
    }

    /**
     * Formats a message with replacement arguments and then strips color-codes from it.
     * 
     * See format() for more details.
     * @param text the string to be formatted
     * @param replacement the replacement string or strings used to fill the replacement arguments.
     * @return The formatted string after replacement and color-stripping.
     * @see #format(java.lang.String, java.lang.String...) 
     * @see org.bukkit.ChatColor#stripColor(java.lang.String) 
     */
    public static String formatStrip(String text, String... replacement) {
        return ChatColor.stripColor(format(text, replacement));
    }

}
