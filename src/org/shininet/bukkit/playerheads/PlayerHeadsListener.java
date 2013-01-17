/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.Iterator;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

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
			
			if (plugin.configFile.getBoolean("antideathchest")) {
				Location location = player.getLocation();
				location.getWorld().dropItemNaturally(location, PlayerHeads.Skull(player.getName()));
			} else {
				event.getDrops().add(PlayerHeads.Skull(player.getName())); // drop the precious player head	
			}
			
			if (plugin.configFile.getBoolean("broadcast")) {
				if (killer == null) {
					plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', Lang.BEHEAD_GENERIC.replace("%1%", player.getDisplayName() + ChatColor.RESET)));
				} else if (killer == player) {
					plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', Lang.BEHEAD_SELF.replace("%1%", player.getDisplayName() + ChatColor.RESET)));
				} else {
					plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', Lang.BEHEAD_OTHER.replace("%1%", player.getDisplayName() + ChatColor.RESET).replace("%2%", killer.getDisplayName() + ChatColor.RESET)));
				}
			}
			break;
		case CREEPER:
			EntityDeathHelper(event, SkullType.CREEPER, plugin.configFile.getDouble("creeperdroprate")*lootingrate);
			break;
		case ZOMBIE:
			EntityDeathHelper(event, SkullType.ZOMBIE, plugin.configFile.getDouble("zombiedroprate")*lootingrate);
			break;
		case SKELETON:
			if (((Skeleton)event.getEntity()).getSkeletonType() == Skeleton.SkeletonType.NORMAL) {
				EntityDeathHelper(event, SkullType.SKELETON, plugin.configFile.getDouble("skeletondroprate")*lootingrate);
			} else if (((Skeleton)event.getEntity()).getSkeletonType() == Skeleton.SkeletonType.WITHER) {
				for (Iterator<ItemStack> it = event.getDrops().iterator(); it.hasNext(); ) {
			    	if (it.next().getType() == Material.SKULL_ITEM) {
			    		it.remove();
			    	}
				}
				EntityDeathHelper(event, SkullType.WITHER, plugin.configFile.getDouble("witherdroprate")*lootingrate);
			}
			break;
		case SPIDER:
			EntityDeathHelper(event, PlayerHeads.customSpider, Lang.HEAD_SPIDER, plugin.configFile.getDouble("spiderdroprate")*lootingrate);
			break;
		case ENDERMAN:
			EntityDeathHelper(event, PlayerHeads.customEnderman, Lang.HEAD_ENDERMAN, plugin.configFile.getDouble("endermandroprate")*lootingrate);
			break;
		case BLAZE:
			EntityDeathHelper(event, PlayerHeads.customBlaze, Lang.HEAD_BLAZE, plugin.configFile.getDouble("blazedroprate")*lootingrate);
			break;
		}
	}
	
	public void EntityDeathHelper(EntityDeathEvent event, SkullType type, Double droprate) {
		EntityDeathHelper(event, type, null, null, droprate);
	}
	
	public void EntityDeathHelper(EntityDeathEvent event, String owner, String name, Double droprate) {
		EntityDeathHelper(event, null, owner, name, droprate);
	}
	
	public void EntityDeathHelper(EntityDeathEvent event, SkullType type, String owner, String name, Double droprate) {
		Double dropchance = prng.nextDouble();
		Player killer = event.getEntity().getKiller();
		
		if (dropchance >= droprate) { return; }
		if (plugin.configFile.getBoolean("mobpkonly") && ((killer == null) || !killer.hasPermission("playerheads.canbeheadmob"))) { return; }
		
		if (type != null) {
			event.getDrops().add(PlayerHeads.Skull(type));
		} else if (owner != null) {
			event.getDrops().add(PlayerHeads.Skull(owner, name));
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		if (block != null && block.getType() == Material.SKULL && plugin.configFile.getBoolean("clickinfo")) {
			for (ItemStack skull : block.getDrops()) {
				if (skull.getType() == Material.SKULL_ITEM) {
					short duribility = skull.getDurability();
					if (duribility == SkullType.PLAYER.ordinal()) {
						SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
						if (skullMeta.hasOwner()) {
							String owner = skullMeta.getOwner();
							if (ChatColor.stripColor(owner).equals(PlayerHeads.customBlaze)) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CLICKINFO2.replace("%1%", Lang.HEAD_BLAZE)));
							} else if (ChatColor.stripColor(owner).equals(PlayerHeads.customEnderman)) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CLICKINFO2.replace("%1%", Lang.HEAD_ENDERMAN)));
							} else if (ChatColor.stripColor(owner).equals(PlayerHeads.customSpider)) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CLICKINFO2.replace("%1%", Lang.HEAD_SPIDER)));
							} else {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CLICKINFO.replace("%1%", owner)));
							}
						} else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CLICKINFO2.replace("%1%", Lang.HEAD)));
						}
					} else if (duribility == SkullType.CREEPER.ordinal()) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CLICKINFO2.replace("%1%", Lang.HEAD_CREEPER)));
					} else if (duribility == SkullType.SKELETON.ordinal()) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CLICKINFO2.replace("%1%", Lang.HEAD_SKELETON)));
					} else if (duribility == SkullType.WITHER.ordinal()) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CLICKINFO2.replace("%1%", Lang.HEAD_WITHER)));
					} else if (duribility == SkullType.ZOMBIE.ordinal()) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CLICKINFO2.replace("%1%", Lang.HEAD_ZOMBIE)));
					}
				}
			}
		}
	}

/* TODO: make it so custom mob heads keep their name after placing/mining
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		event.
	}
*/
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if(player.hasPermission("playerheads.update") && plugin.getUpdateReady())
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.UPDATE1.replace("%1%", plugin.getUpdateName()).replace("%2%", String.valueOf(plugin.getUpdateSize()))));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.UPDATE2));
		}
	}
}
