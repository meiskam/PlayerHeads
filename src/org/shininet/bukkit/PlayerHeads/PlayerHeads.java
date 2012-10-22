/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.PlayerHeads;

import java.util.Random;

import net.minecraft.server.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerHeads extends JavaPlugin implements Listener {
	
	private Random generator = new Random();
	
	@Override
	public void onEnable(){
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents((Listener)this, this);
	}
 
	@Override
	public void onDisable() {
		EntityDeathEvent.getHandlerList().unregister((Listener)this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("PlayerHeads")) {
			if (args.length == 0) {
				sender.sendMessage("["+label+"] Subcommands: config, spawn");
			} else {
				if (args[0].equalsIgnoreCase("config")) {
					if (args.length == 1) {
						sender.sendMessage("["+label+":config] Subcommands: get, set, reload");
					} else {
						if (args[1].equalsIgnoreCase("get") || args[1].equalsIgnoreCase("view")) {
							if (sender.hasPermission("playerheads.config.get")) {
								if (args.length == 2) {
									sender.sendMessage("["+label+":config:get] Config variables: DropRate, PKOnly, HookBreak");
								} else if (args.length == 3) {
									sender.sendMessage("["+label+":config:get] "+args[2].toLowerCase()+": "+getConfig().get(args[2].toLowerCase()));
								} else {
									sender.sendMessage("["+label+":config:get] Too many params!");
								}
							} else {
								sender.sendMessage("["+label+":config:get] You don't have permission to use that command");
							}
						} else if (args[1].equalsIgnoreCase("set")) {
							if (sender.hasPermission("playerheads.config.set")) {
								if (args.length == 2 || args.length == 3) {
									sender.sendMessage("["+label+":config:set] Config variables: DropRate, PKOnly, HookBreak");
								} else if (args.length == 4) {
									if (args[2].equalsIgnoreCase("droprate")) {
										try {
											getConfig().set("droprate", Double.parseDouble(args[3]));
										} catch (NumberFormatException e) {
											sender.sendMessage("["+label+":config:set] ERROR: Can not convert "+args[3].toLowerCase()+" to a number");
										}
									} else if (args[2].equalsIgnoreCase("pkonly")) {
										if (args[3].equalsIgnoreCase("false") || args[3].equalsIgnoreCase("no") || args[3].equalsIgnoreCase("0")) {
											getConfig().set("pkonly", false);
										} else {
											getConfig().set("pkonly", true);
										}
									} else if (args[2].equalsIgnoreCase("HookBreak")) {
										if (args[3].equalsIgnoreCase("false") || args[3].equalsIgnoreCase("no") || args[3].equalsIgnoreCase("0")) {
											getConfig().set("hookbreak", false);
										} else {
											getConfig().set("hookbreak", true);
										}
									}
									else {
										getConfig().set(args[2].toLowerCase(), args[3]);
									}
									saveConfig();
									sender.sendMessage("["+label+":config:set] "+args[2].toLowerCase()+": "+getConfig().get(args[2].toLowerCase()));
								} else {
									sender.sendMessage("["+label+":config:set] Too many params!");
								}
							} else {
								sender.sendMessage("["+label+":config:set] You don't have permission to use that command");
							}
/*						} else if (args[1].equalsIgnoreCase("save")) {
							if (((Player)sender).hasPermission("playerheads.config.set")) {
								saveConfig();
							} else {
								sender.sendMessage("["+label+":config:save] You don't have permission to use that command");
							}
*/						} else if (args[1].equalsIgnoreCase("reload")) {
							if (sender.hasPermission("playerheads.config.set")) {
								reloadConfig();
								sender.sendMessage("["+label+":config:reload] Config reloaded");
							} else {
								sender.sendMessage("["+label+":config:reload] You don't have permission to use that command");
							}
						} else {
							sender.sendMessage("["+label+":config:??] Invalid subcommand");
						}
					}
				}
				else if (args[0].equalsIgnoreCase("spawn")) {
					if (!(sender instanceof Player)) {
						sender.sendMessage("["+label+":spawn] Sorry console, heads are for players!");
					}
					else {
						if (args.length == 1) {
							//spawn them their head
							if (sender.hasPermission("playerheads.spawn.own")) {
								if (((Player)sender).getInventory().addItem(getHead(((Player)sender).getName())).isEmpty()) {
									sender.sendMessage("["+label+":spawn] Spawned you "+((Player)sender).getName()+"'s head");
								} else {
									sender.sendMessage("["+label+":spawn] Well I can't very well give you an item if your inventory is full");
								}
							} else {
								sender.sendMessage("["+label+":spawn] You don't have permission to use that command");
							}
						} else if (args.length == 2) {
							//spawn them args[1]'s head
							if (sender.hasPermission("playerheads.spawn")) {
								if (((Player)sender).getInventory().addItem(getHead(args[1])).isEmpty()) {
									sender.sendMessage("["+label+":spawn] Spawned you "+args[1]+"'s head");
								} else {
									sender.sendMessage("["+label+":spawn] Well I can't very well give you an item if your inventory is full");
								}
							} else {
								sender.sendMessage("["+label+":spawn] You don't have permission to use that command");
							}
						} else {
							sender.sendMessage("["+label+":spawn] Too many params!");
						}
					}
/*				} else if (args[0].equalsIgnoreCase("somethingelse")) {
					sender.sendMessage("["+label+":??] moo");
*/				} else {
					sender.sendMessage("["+label+":??] Invalid subcommand");
				}
			}
			return true;
		}
		return false; 
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			//((Player)(event.getEntity())).sendMessage("Hehe, you died");
			Double dropchance = generator.nextDouble();
/*			((Player)(event.getEntity())).sendMessage(new StringBuilder("[ph] ").append(getConfig().getBoolean("pkonly", true))
					.append(", ").append(event.getEntity().getKiller() instanceof Player).append(", ")
					.append(dropchance).append(", ").append(dropchance <= getConfig().getDouble("droprate", 0.05)).toString());
*/			if ((!(getConfig().getBoolean("pkonly", true)) // if pkonly's off, continue, don't check next if line
			 || (getConfig().getBoolean("pkonly", true) && (event.getEntity().getKiller() instanceof Player))) // if pkonly's on AND killer is player, continue 
			 && (dropchance <= getConfig().getDouble("droprate", 0.05))) { // check if it should drop via droprate
				event.getDrops().add(getHead(((Player)(event.getEntity())).getName())); // drop the precious player head
			}
		}
	}
	
/*	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		
	}
*/	
	public CraftItemStack getHead(String playername) { //TODO: temp = 298 .. Skull item = 144 .. block = 397
		CraftItemStack head;
		try {
			head = new CraftItemStack(Material.getMaterial(397),1,(short)3);
		} catch (NullPointerException e) {
			getLogger().warning("It seems you're not yet using version 1.4 .. falling back to a leather helm");
			head = new CraftItemStack(Material.getMaterial(298),1,(short)55);
		}		
		NBTTagCompound headNBT = new NBTTagCompound();
		headNBT.setString("SkullOwner", playername);
		head.getHandle().tag = headNBT;
		return head;
	}
//NullPointerException
}
