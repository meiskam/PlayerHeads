/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
* @author meiskam
*/

public final class PlayerHeads extends JavaPlugin implements Listener {

    private PlayerHeadsCommandExecutor commandExecutor;
    private PlayerHeadsListener listener;

    public static enum configType {DOUBLE, BOOLEAN};
    @SuppressWarnings("serial")
    public static final Map<String, configType> configKeys = new HashMap<String, configType>(){
        {
            put("pkonly", configType.BOOLEAN);
            put("droprate", configType.DOUBLE);
            put("lootingrate", configType.DOUBLE);
            put("clickinfo", configType.BOOLEAN);
            put("mobpkonly", configType.BOOLEAN);
            put("creeperdroprate", configType.DOUBLE);
            put("zombiedroprate", configType.DOUBLE);
            put("skeletondroprate", configType.DOUBLE);
            put("witherdroprate", configType.DOUBLE);
            for (CustomSkullType customSkullType : CustomSkullType.values()) {
                put(customSkullType.name().replace("_", "").toLowerCase()+"droprate", configType.DOUBLE);
            }
            put("fixcase", configType.BOOLEAN);
            put("updatecheck", configType.BOOLEAN);
            put("broadcast", configType.BOOLEAN);
            put("antideathchest", configType.BOOLEAN);
            put("dropboringplayerheads", configType.BOOLEAN);
        }
    };
    public static final String configKeysString = implode(configKeys.keySet(), ", ");
    //public Map<String, Object> configMap = new HashMap<String, Object>();
    public Logger logger;
    public FileConfiguration configFile;
    private static boolean updateReady = false;
    private static String updateName = "";
    private static long updateSize = 0;
    public static final String updateSlug = "player-heads";
    public static int defaultStackSize = 1;

    @Override
    public void onEnable(){
        logger = getLogger();
        configFile = getConfig();
        configFile.options().copyDefaults(true);
        saveDefaultConfig();
        Lang.init(this);
        try {
            BukkitMetrics metrics = new BukkitMetrics(this);
            metrics.start();
        } catch (Exception e) {
            logger.warning(Lang.ERROR_METRICS);
        }
        try {
            if (configFile.getBoolean("updatecheck") && !(updateReady)) {
                Updater updater = new Updater(this, updateSlug, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
                updateReady = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
                updateName = updater.getLatestVersionString(); // Get the latest version
                updateSize = updater.getFileSize(); // Get latest size
            }
        } catch (Exception e) {
            logger.warning(Lang.ERROR_UPDATER);
        }
        listener = new PlayerHeadsListener(this);
        commandExecutor = new PlayerHeadsCommandExecutor(this);
        getServer().getPluginManager().registerEvents(listener, this);
        getCommand("PlayerHeads").setExecutor(commandExecutor);
    }

    @Override
    public void onDisable() {
        EntityDeathEvent.getHandlerList().unregister(listener);
        PlayerInteractEvent.getHandlerList().unregister(listener);
        PlayerJoinEvent.getHandlerList().unregister(listener);
        BlockBreakEvent.getHandlerList().unregister(listener);
    }

    public boolean getUpdateReady() {
        return updateReady;
    }

    public String getUpdateName() {
        return updateName;
    }

    public long getUpdateSize() {
        return updateSize;
    }

    public static boolean addHead(Player player, String skullOwner) {
        return addHead(player, skullOwner, defaultStackSize);
    }

    public static boolean addHead(Player player, String skullOwner, int quantity) {
        PlayerInventory inv = player.getInventory();
        int firstEmpty = inv.firstEmpty();
        if (firstEmpty == -1) {
            return false;
        } else {
            inv.setItem(firstEmpty, PlayerHeads.Skull(skullOwner, quantity));
            return true;
        }
    }

    public static String implode(Set<String> input, String glue) {
        int i = 0;
        StringBuilder output = new StringBuilder();
        for (String key : input) {
            if (i++ != 0) output.append(glue);
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
            if (offPlayer.getName().toLowerCase().equals(inputNameLC)) {
                return offPlayer.getName();
            }
        }

        return inputName;
    }

    public static ItemStack Skull(String skullOwner) {
        return Skull(skullOwner, defaultStackSize);
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
        } else if (skullOwnerLC.equals(Lang.HEAD_SPAWN_WITHER)) {
            return Skull(SkullType.WITHER, quantity);
        } else {
            return Skull(skullOwner, null, quantity);
        }
    }

    public static ItemStack Skull(String skullOwner, String displayName) {
        return Skull(skullOwner, displayName, defaultStackSize);
    }

    public static ItemStack Skull(String skullOwner, String displayName, int quantity) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, quantity, (short)SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
        skullMeta.setOwner(skullOwner);
        if (displayName != null) {
            skullMeta.setDisplayName(ChatColor.RESET+displayName);
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public static ItemStack Skull(CustomSkullType type) {
        return Skull(type, defaultStackSize);
    }

    public static ItemStack Skull(CustomSkullType type, int quantity) {
        return Skull(type.getOwner(), type.getDisplayName(), quantity);
    }

    public static ItemStack Skull(SkullType type) {
        return Skull(type, defaultStackSize);
    }

    public static ItemStack Skull(SkullType type, int quantity) {
        return new ItemStack(Material.SKULL_ITEM, quantity, (short)type.ordinal());
    }

    public static String format(String text, String... replacement) {
        String output = text;
        for (int i = 0; i < replacement.length; i++) {
            output = output.replace("%"+(i+1)+"%", replacement[i]);
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
