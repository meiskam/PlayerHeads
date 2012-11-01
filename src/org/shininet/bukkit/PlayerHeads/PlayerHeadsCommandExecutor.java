/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class PlayerHeadsCommandExecutor implements CommandExecutor, TabCompleter {
	
	private PlayerHeads plugin;
	
	public PlayerHeadsCommandExecutor(PlayerHeads plugin) {
		this.plugin = plugin;
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
									sender.sendMessage("["+label+":config:get] Config variables: "+PlayerHeads.configKeysString);
								} else if (args.length == 3) {
									String key = args[2].toLowerCase();
									sender.sendMessage("["+label+":config:get] "+key+": "+plugin.configFile.get(key));
								} else {
									sender.sendMessage("["+label+":config:get] Too many params!");
								}
							} else {
								sender.sendMessage("["+label+":config:get] You don't have permission to use that command");
							}
						} else if (args[1].equalsIgnoreCase("set")) {
							if (sender.hasPermission("playerheads.config.set")) {
								if (args.length == 2 || args.length == 3) {
									sender.sendMessage("["+label+":config:set] Config variables: "+PlayerHeads.configKeysString);
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
										}
										if (keyFound) {
											break;
										}
									}
									if (!keyFound) {
										plugin.configFile.set(key, value);
									}
									plugin.saveConfig();
									sender.sendMessage("["+label+":config:set] "+key+": "+plugin.configFile.get(key));
								} else {
									sender.sendMessage("["+label+":config:set] Too many params!");
								}
							} else {
								sender.sendMessage("["+label+":config:set] You don't have permission to use that command");
							}
						} else if (args[1].equalsIgnoreCase("reload")) {
							if (sender.hasPermission("playerheads.config.set")) {
								plugin.reloadConfig();
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
						String skullOwner;
						if (args.length == 1) {
							skullOwner = ((Player)sender).getName();
						} else if (args.length == 2) {
							skullOwner = args[1];
						} else {
							sender.sendMessage("["+label+":spawn] Too many params!");
							return true;
						}
						if ((skullOwner.equals(((Player)sender).getName()) && sender.hasPermission("playerheads.spawn.own")) || sender.hasPermission("playerheads.spawn")) {
							if (plugin.addHead((Player)sender, skullOwner)) {
								sender.sendMessage("["+label+":spawn] Spawned you "+skullOwner+"'s head");
							} else {
								sender.sendMessage("["+label+":spawn] Well I can't very well give you an item if your inventory is full");
							}
						} else {
							sender.sendMessage("["+label+":spawn] You don't have permission to use that command");
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

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
