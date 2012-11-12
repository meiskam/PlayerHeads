/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import net.minecraft.server.EntityItem;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

/**
* @author meiskam
*/

public final class PlayerHeads extends JavaPlugin implements Listener {
	
	private final Random prng = new Random();
	private PlayerHeadsCommandExecutor commandExecutor;
	private PlayerHeadsListener listener;
	
	public static enum configType {DOUBLE, BOOLEAN};
	@SuppressWarnings("serial")
	public static final Map<String, configType> configKeys = new HashMap<String, configType>(){
		{
			put("pkonly", configType.BOOLEAN);
			put("droprate", configType.DOUBLE);
			put("hookbreak", configType.BOOLEAN);
			put("clickinfo", configType.BOOLEAN);
			put("mobpkonly", configType.BOOLEAN);
			put("creeperdroprate", configType.DOUBLE);
			put("zombiedroprate", configType.DOUBLE);
			put("skeletondroprate", configType.DOUBLE);
			put("fixcase", configType.BOOLEAN);
			put("autoupdate", configType.BOOLEAN);
		}
	};
	public static final String configKeysString = implode(configKeys.keySet(), ", ");
	//public Map<String, Object> configMap = new HashMap<String, Object>();
	public Logger logger;
	public FileConfiguration configFile;
	private static boolean updateReady = false;
	private static String updateName = "";
	private static long updateSize = 0;
	private static final String updateSlug = "player-heads";
	
	@Override
	public void onEnable(){
		logger = getLogger();
		configFile = getConfig();
		configFile.options().copyDefaults(true);
		saveDefaultConfig();
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (Exception e) {
			logger.warning("Failed to start Metrics");
		}
		try {
			if (configFile.getBoolean("autoupdate") && !(updateReady)) {
				Updater updater = new Updater(this, updateSlug, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
				updateReady = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
				updateName = updater.getLatestVersionString(); // Get the latest version
				updateSize = updater.getFileSize(); // Get latest size
			}
		} catch (Exception e) {
			logger.warning("Failed to start Updater");
		}
		listener = new PlayerHeadsListener(this);
		commandExecutor = new PlayerHeadsCommandExecutor(this);
		getServer().getPluginManager().registerEvents(listener, this);
		getCommand("PlayerHeads").setExecutor(commandExecutor);
	}

	@Override
	public void onDisable() {
		EntityDeathEvent.getHandlerList().unregister(listener);
		BlockBreakEvent.getHandlerList().unregister(listener);
		BlockDamageEvent.getHandlerList().unregister(listener);
		//BlockPlaceEvent
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

	public void update() {
		new Updater(this, updateSlug, getFile(), Updater.UpdateType.NO_VERSION_CHECK, true);
	}
	
	public static boolean addHead(Player player, String skullOwner) {
		PlayerInventory inv = player.getInventory();
		int firstEmpty = inv.firstEmpty();
		if (firstEmpty == -1) {
			return false;
		} else {
			inv.setItem(firstEmpty, new Skull(skullOwner).getItemStack());
			return true;
		}
	}

	public void dropItemNaturally(CraftWorld world, Location loc, CraftItemStack item) { //inspired by org.bukkit.craftbukkit.CraftWorld
		double xs = loc.getX() + (prng.nextFloat() * 0.7F + (1.0F - 0.7F) * 0.5D);
		double ys = loc.getY() + (prng.nextFloat() * 0.7F + (1.0F - 0.7F) * 0.5D);
		double zs = loc.getZ() + (prng.nextFloat() * 0.7F + (1.0F - 0.7F) * 0.5D);
		
		EntityItem entity = new EntityItem(world.getHandle(), xs, ys, zs, item.getHandle());
		entity.pickupDelay = 10;
		world.getHandle().addEntity(entity);
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
}
