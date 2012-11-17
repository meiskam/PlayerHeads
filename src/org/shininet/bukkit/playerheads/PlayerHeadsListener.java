/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

/**
* @author meiskam
*/

public class PlayerHeadsListener implements Listener {

	private final Random prng = new Random();
	private PlayerHeads plugin;

	public PlayerHeadsListener(PlayerHeads plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("incomplete-switch")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		Player killer = event.getEntity().getKiller();
		double lootingrate = 1;
		
		if (killer != null) {
			ItemStack weapon = killer.getItemInHand();
			if (weapon != null) {
				lootingrate = 1+(plugin.configFile.getDouble("lootingrate")*weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS));
			}
		}

		//if (event.getEntityType() == EntityType.PLAYER) {
		switch (event.getEntityType()) {
		case PLAYER:
			//((Player)(event.getEntity())).sendMessage("Hehe, you died");
			Double dropchance = prng.nextDouble();
			Player player = (Player)event.getEntity();
			
			if (dropchance >= plugin.configFile.getDouble("droprate")*lootingrate) { return; }
			if (!player.hasPermission("playerheads.canloosehead")) { return; }
			if (plugin.configFile.getBoolean("pkonly") && ((killer == null) || (killer == player) || !killer.hasPermission("playerheads.canbehead"))) { return; }
			
			event.getDrops().add(new Skull(player.getName()).getItemStack()); // drop the precious player head
			if (plugin.configFile.getBoolean("broadcast")) {
				if (killer == null) {
					plugin.getServer().broadcastMessage(player.getDisplayName() + ChatColor.RESET + " was beheaded");
				} else if (killer == player) {
					plugin.getServer().broadcastMessage(player.getDisplayName() + ChatColor.RESET + " beheaded themselves");
				} else {
					plugin.getServer().broadcastMessage(player.getDisplayName() + ChatColor.RESET + " was beheaded by " + killer.getDisplayName() + ChatColor.RESET);
				}
			}
			break;
		case CREEPER:
			EntityDeathHelper(event, 4, plugin.configFile.getDouble("creeperdroprate")*lootingrate);
			break;
		case ZOMBIE:
			EntityDeathHelper(event, 2, plugin.configFile.getDouble("zombiedroprate")*lootingrate);
			break;
		case SKELETON:
			EntityDeathHelper(event, 0, plugin.configFile.getDouble("skeletondroprate")*lootingrate);
			break;
		}
	}
	
	public void EntityDeathHelper(EntityDeathEvent event, int damage, Double droprate) {
		Double dropchance = prng.nextDouble();
		Player killer = event.getEntity().getKiller();
		
		if (dropchance >= droprate) { return; }
		if (plugin.configFile.getBoolean("mobpkonly") && ((killer == null) || !killer.hasPermission("playerheads.canbeheadmob"))) { return; }
		
		event.getDrops().add(Skull.getItemStack(damage));
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		if (block != null && block.getType() == Material.SKULL && plugin.configFile.getBoolean("clickinfo")) {
			Location location = block.getLocation();
			CraftWorld world = (CraftWorld)block.getWorld();
			
			Skull skull = new Skull(world.getTileEntityAt(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
			
			if (skull.hasOwner()) {
				StringBuilder message = new StringBuilder();
				message.append("[PlayerHeads] That's ").append(skull.skullOwner).append("'s Head");
				if (skull.hasName()) {
					message.append(" (").append(skull.name).append(")");
				}
				event.getPlayer().sendMessage(message.toString());
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if(player.hasPermission("playerheads.update") && plugin.getUpdateReady())
		{
			player.sendMessage("[PlayerHeads] An update is available: " + plugin.getUpdateName() + " (" + plugin.getUpdateSize() + " bytes)");
			player.sendMessage("[PlayerHeads] Type \"/PlayerHeads update\" if you would like to update.");
		}
	}
}
