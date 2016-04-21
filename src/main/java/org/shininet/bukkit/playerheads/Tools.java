/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * @author meiskam
 */

public class Tools {

    public static boolean addHead(Player player, String skullOwner) {
        return addHead(player, skullOwner, Config.defaultStackSize);
    }

    public static boolean addHead(Player player, String skullOwner, int quantity) {
        PlayerInventory inv = player.getInventory();
        int firstEmpty = inv.firstEmpty();
        if (firstEmpty == -1) {
            return false;
        } else {
            inv.setItem(firstEmpty, Tools.Skull(skullOwner, quantity));
            return true;
        }
    }

    public static String implode(Set<String> input, String glue) {
        int i = 0;
        StringBuilder output = new StringBuilder();
        for (String key : input) {
            if (i++ != 0) {
                output.append(glue);
            }
            output.append(key);
        }
        return output.toString();
    }

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

    public static ItemStack Skull(String skullOwner) {
        return Skull(skullOwner, Config.defaultStackSize);
    }

    public static ItemStack Skull(String skullOwner, int quantity) {
        String skullOwnerLC = skullOwner.toLowerCase();

        for (CustomSkullType skullType : CustomSkullType.values()) {
            if (skullOwnerLC.equals(skullType.getSpawnName().toLowerCase())) {
                return Skull(skullType, quantity);
            }
        }

        if (skullOwnerLC.equals(Lang.HEAD_SPAWN_CREEPER)) {
            return Skull(SkullType.CREEPER, quantity);
        } else if (skullOwnerLC.equals(Lang.HEAD_SPAWN_ZOMBIE)) {
            return Skull(SkullType.ZOMBIE, quantity);
        } else if (skullOwnerLC.equals(Lang.HEAD_SPAWN_SKELETON)) {
            return Skull(SkullType.SKELETON, quantity);
        //} else if (skullOwnerLC.equals(Lang.HEAD_SPAWN_WITHER)) {
        //    return Skull(SkullType.WITHER, quantity);
        } else {
            return Skull(skullOwner, null, quantity);
        }
    }

    public static ItemStack Skull(String skullOwner, String displayName) {
        return Skull(skullOwner, displayName, Config.defaultStackSize);
    }

    public static ItemStack Skull(String skullOwner, String displayName, int quantity) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, quantity, (short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        boolean shouldSet = false;
        if ((skullOwner != null) && (!skullOwner.equals(""))) {
        	skullMeta.setOwner(skullOwner);
            shouldSet = true;
        }
        if (displayName != null) {
            skullMeta.setDisplayName(ChatColor.RESET + displayName);
            shouldSet = true;
        }
        if (shouldSet) {
            skull.setItemMeta(skullMeta);
        }
        return skull;
    }

    public static ItemStack Skull(CustomSkullType type) {
        return Skull(type, Config.defaultStackSize);
    }

    public static ItemStack Skull(CustomSkullType type, int quantity) {
        return Skull(type.getOwner(), type.getDisplayName(), quantity);
    }

    public static ItemStack Skull(SkullType type) {
        return Skull(type, Config.defaultStackSize);
    }

    public static ItemStack Skull(SkullType type, int quantity) {
        return new ItemStack(Material.SKULL_ITEM, quantity, (short) type.ordinal());
    }

    public static String format(String text, String... replacement) {
        String output = text;
        for (int i = 0; i < replacement.length; i++) {
            output = output.replace("%" + (i + 1) + "%", replacement[i]);
        }
        return ChatColor.translateAlternateColorCodes('&', output);
    }

    public static void formatMsg(CommandSender player, String text, String... replacement) {
        player.sendMessage(format(text, replacement));
    }

    public static String formatStrip(String text, String... replacement) {
        return ChatColor.stripColor(format(text, replacement));
    }

}
