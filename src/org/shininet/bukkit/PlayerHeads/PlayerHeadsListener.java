/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class PlayerHeadsListener implements Listener {

	private final Random prng = new Random();
	private PlayerHeads plugin;

	public PlayerHeadsListener(PlayerHeads plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("incomplete-switch")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		//if (event.getEntityType() == EntityType.PLAYER) {
		switch (event.getEntityType()) {
		case PLAYER:
			//((Player)(event.getEntity())).sendMessage("Hehe, you died");
			Double dropchance = prng.nextDouble();
			Player player = (Player)event.getEntity();
			Player killer = player.getKiller();

			if ((dropchance <= plugin.configFile.getDouble("droprate", 0.05)) && // check if it should drop via droprate
			 (!(plugin.configFile.getBoolean("pkonly", true)) // if pkonly's off, continue, don't check next 2 if lines
			 || ((plugin.configFile.getBoolean("pkonly", true) && (killer != null) 
				 && (killer != player) && killer.hasPermission("playerheads.canbehead")))) // if pkonly's on AND killer is a Player AND killer != player, AND killer has permission to lop heads, continue
			 && (player.hasPermission("playerheads.canloosehead"))) { // if player has permission to drop his head when dies
				event.getDrops().add(new Skull(player.getName()).getItemStack()); //getHead(player.getName())); // drop the precious player head
			}
			break;
		case CREEPER:
			EntityDeathHelper(event, 4, plugin.configFile.getDouble("creeperdroprate", 0.005));
			break;
		case ZOMBIE:
			EntityDeathHelper(event, 2, plugin.configFile.getDouble("zombiedroprate", 0.005));
			break;
		case SKELETON:
			EntityDeathHelper(event, 0, plugin.configFile.getDouble("skeletondroprate", 0.005));
			break;
		}
	}
	
	public void EntityDeathHelper(EntityDeathEvent event, int damage, Double droprate) {
		Double dropchance = prng.nextDouble();
		Player killer = event.getEntity().getKiller();
		
		if ((dropchance <= droprate) && (!(plugin.configFile.getBoolean("mobpkonly", true)) || 
				((plugin.configFile.getBoolean("mobpkonly", true) && (killer != null) && killer.hasPermission("playerheads.canbeheadmob")))) ) {
			event.getDrops().add(Skull.getItemStack(damage));
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		if (!(event.isCancelled()) && event.getBlock().getType() == Material.SKULL && event.getPlayer().getGameMode() == GameMode.SURVIVAL && plugin.configFile.getBoolean("hookbreak", true)) {
			Block block = event.getBlock();
			Location location = block.getLocation();
			CraftWorld world = (CraftWorld)block.getWorld();
			
			Skull skull = new Skull(world.getTileEntityAt(location.getBlockX(), location.getBlockY(), location.getBlockZ()));

			if (skull.hasTag()) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				plugin.dropItemNaturally(world, location, skull.getItemStack());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockDamage(BlockDamageEvent event) {
		if (!(event.isCancelled()) && event.getBlock().getType() == Material.SKULL && event.getPlayer().getGameMode() == GameMode.SURVIVAL && plugin.configFile.getBoolean("clickinfo", true)) {
			Block block = event.getBlock();
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
	
/*	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!(event.isCancelled()) && event.getBlock().getType() == Material.SKULL && plugin.configFile.getBoolean("extradata", true)) {
			CraftBlock block = (CraftBlock)event.getBlock();
			Location location = block.getLocation();
			
			
			Skull skull = new Skull((CraftItemStack)event.getItemInHand());
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new ExtraTileData(plugin, skull, location));

		}
	}
*/	
}
