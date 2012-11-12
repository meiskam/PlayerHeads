/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
* @author meiskam
*/

public class PlayerHeadsCommandExecutor implements CommandExecutor, TabCompleter {
	
	private PlayerHeads plugin;
	
	public PlayerHeadsCommandExecutor(PlayerHeads plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (!cmd.getName().equalsIgnoreCase("PlayerHeads")) {
			return false;
		}
		if (args.length == 0) {
			sender.sendMessage("["+label+"] Subcommands: config, spawn, rename");
			return true;
		}
		if (args[0].equalsIgnoreCase("config")) {
			if (args.length == 1) {
				sender.sendMessage("["+label+":config] Subcommands: get, set, reload");
				return true;
			}
			if (args[1].equalsIgnoreCase("get") || args[1].equalsIgnoreCase("view")) {
				if (!sender.hasPermission("playerheads.config.get")) {
					sender.sendMessage("["+label+":config:get] You don't have permission to use that command");
					return true;
				}
				if (args.length == 2) {
					sender.sendMessage("["+label+":config:get] Config variables: "+PlayerHeads.configKeysString);
				} else if (args.length == 3) {
					String key = args[2].toLowerCase();
					sender.sendMessage("["+label+":config:get] "+key+": "+plugin.configFile.get(key));
				} else {
					sender.sendMessage("["+label+":config:get] Syntax: "+label+" config get [variable]");
				}
				return true;
			} else if (args[1].equalsIgnoreCase("set")) {
				if (!sender.hasPermission("playerheads.config.set")) {
					sender.sendMessage("["+label+":config:set] You don't have permission to use that command");
					return true;
				}
				if (args.length == 2 || args.length == 3) {
					sender.sendMessage("["+label+":config:set] Config variables: "+PlayerHeads.configKeysString);
					return true;
				} else if (args.length == 4) {
					String key = args[2].toLowerCase();
					String value = args[3].toLowerCase();
					boolean keyFound = false;
					
					for (String keySet : PlayerHeads.configKeys.keySet()) {
						if (key.equals(keySet.toLowerCase())) {
							keyFound = true;
							switch (PlayerHeads.configKeys.get(keySet.toLowerCase())) {
							case BOOLEAN:
								if (value.equals("false") || value.equals("no") || value.equals("0")) {
									plugin.configFile.set(key, false);
								} else {
									plugin.configFile.set(key, true);
								}
								break;
							case DOUBLE:
								try {
									plugin.configFile.set(key, Double.parseDouble(value));
								} catch (NumberFormatException e) {
									sender.sendMessage("["+label+":config:set] ERROR: Can not convert "+value+" to a number");
								}
								break;
							default:
								plugin.logger.warning("configType \""+PlayerHeads.configKeys.get(keySet.toLowerCase())+"\" unrecognised - this is a bug");
								break;
							}
							break;
						}
					}
					if (!keyFound) {
						plugin.configFile.set(key, value);
					}
					plugin.saveConfig();
					sender.sendMessage("["+label+":config:set] "+key+": "+plugin.configFile.get(key));
					return true;
				} else {
					sender.sendMessage("["+label+":config:set] Syntax: "+label+" config set [variable] [value]");
					return true;
				}
			} else if (args[1].equalsIgnoreCase("reload")) {
				if (!sender.hasPermission("playerheads.config.set")) {
					sender.sendMessage("["+label+":config:reload] You don't have permission to use that command");
					return true;
				}
				plugin.reloadConfig();
				plugin.configFile = plugin.getConfig();
				sender.sendMessage("["+label+":config:reload] Config reloaded");
				return true;
			} else {
				sender.sendMessage("["+label+":config:??] Invalid subcommand");
				return true;
			}
		}
		else if (args[0].equalsIgnoreCase("spawn")) {
			String skullOwner;
			boolean haspermission = false;
			Player reciever;
			
			if (!(sender instanceof Player) && (args.length != 3)) {
				sender.sendMessage("["+label+":spawn] Syntax: "+label+" spawn <headname> <reciever>");
				return true;
			}
			reciever = (Player)sender;
			if ((args.length == 1) || ((args.length == 2) && (sender instanceof Player) && ((Player)sender).getName().equalsIgnoreCase(args[1]))) {
				skullOwner = ((Player)sender).getName();
				haspermission = sender.hasPermission("playerheads.spawn.own");
			} else if ((args.length == 2) && (sender instanceof Player)) {
				skullOwner = args[1];
				haspermission = sender.hasPermission("playerheads.spawn");
			} else if (args.length == 3) {
				if ((reciever = plugin.getServer().getPlayer(args[2])) == null) {
					sender.sendMessage("["+label+":spawn] Can not find "+args[2]+" online");
					return true;
				}
				skullOwner = args[1];
				if (sender instanceof Player) {
					haspermission = true;
				} else {
					haspermission = sender.hasPermission("playerheads.spawn");
				}
			} else {
				sender.sendMessage("["+label+":spawn] Syntax: "+label+" spawn [headname] [reciever]");
				return true;
			}
			if (!haspermission) {
				sender.sendMessage("["+label+":spawn] You don't have permission to use that command");
				return true;
			}
			if (plugin.configFile.getBoolean("fixcase", true)) {
				skullOwner = PlayerHeads.fixcase(skullOwner);
			}
			if (PlayerHeads.addHead(reciever, skullOwner)) {
				sender.sendMessage("["+label+":spawn] Spawned "+skullOwner+"'s Head");
			} else {
				sender.sendMessage("["+label+":spawn] Well I can't very well spawn a head if the inventory is full");
			}
			return true;
		} else if (args[0].equalsIgnoreCase("rename")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("["+label+":spawn] Sorry console, heads are for players!");
				return true;
			}
			if (!sender.hasPermission("playerheads.rename")) {
				sender.sendMessage("["+label+":rename] You don't have permission to use that command");
				return true;
			}
			if (!((args.length == 1) || (args.length == 2))) {
				sender.sendMessage("["+label+":rename] Syntax: "+label+" rename [headname]");
				return true;
			}
			ItemStack hand = ((Player)sender).getItemInHand();
			if (hand.getData().getItemType() != Material.SKULL_ITEM) {
				sender.sendMessage("["+label+":rename] That's not a player head");
				return true;
			}
			Skull skull = new Skull (((Player)sender).getItemInHand());
			if (skull.damage != 3) {
				sender.sendMessage("["+label+":rename] That's not a player head");
				return true;
			}
			if (args.length >= 2) {
				if (plugin.configFile.getBoolean("fixcase", true)) {
					skull.skullOwner = PlayerHeads.fixcase(args[1]);
				} else {
					skull.skullOwner = args[1];
				}
			} else {
				skull.skullOwner = "";
			}
			((Player)sender).setItemInHand(skull.getItemStack());
			sender.sendMessage("["+label+":rename] Successfully renamed Head");
			return true;
/*		} else if (args[0].equalsIgnoreCase("somethingelse")) {
			sender.sendMessage("["+label+":??] moo");
			return true;
*/		} else {
			sender.sendMessage("["+label+":??] Invalid subcommand");
			return true;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
