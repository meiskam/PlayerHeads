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
 * @author meiskam
 */

@SuppressWarnings("WeakerAccess")
public class Tools {

    @SuppressWarnings("unused")
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

        for (TexturedSkullType skullType : TexturedSkullType.values()) {
            if (skullOwnerLC.equals(skullType.getSpawnName().toLowerCase())) {
                return SkullManager.MobSkull(skullType, quantity);
            }
        }
        return SkullManager.PlayerSkull(skullOwner,quantity);
    }
    
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

    public static void formatMsg(CommandSender player, String text, String... replacement) {
        player.sendMessage(format(text, replacement));
    }

    public static String formatStrip(String text, String... replacement) {
        return ChatColor.stripColor(format(text, replacement));
    }

}
