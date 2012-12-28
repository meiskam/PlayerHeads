/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

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
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+"]"+"Subcommands"+": "+"config"+", "+"spawn"+", "+"rename"+", "+"update"));
			return true;
		}
		if (args[0].equalsIgnoreCase("config")) {
			if (args.length == 1) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"config"+"]"+" "+"Subcommands"+": "+"get"+", "+"set"+", "+"reload"));
				return true;
			}
			if (args[1].equalsIgnoreCase("get")) {
				if (!sender.hasPermission("playerheads.config.get")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"config"+":"+"get"+"]"+" "+"You don't have permission to use that command"));
					return true;
				}
				if (args.length == 3) {
					String key = args[2].toLowerCase();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"config"+":"+"get"+"]"+" "+key+": "+plugin.configFile.get(key)));
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"config"+":"+"get"+"]"+" "+"Syntax"+": "+label+" "+"config"+" "+"get"+" "+"<variable>"));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"config"+":"+"get"+"]"+" "+"Config variables"+": "+PlayerHeads.configKeysString));
				}
				return true;
			} else if (args[1].equalsIgnoreCase("set")) {
				if (!sender.hasPermission("playerheads.config.set")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"config"+":"+"set"+"]"+" "+"You don't have permission to use that command"));
					return true;
				}
				if (args.length == 3) {
					String key = args[2].toLowerCase();
					plugin.configFile.set(key, null);
					plugin.saveConfig();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"config"+":"+"set"+"]"+" "+key+": "+plugin.configFile.get(key)));
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
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"config"+":"+"set"+"]"+" "+"ERROR: Can not convert %1% to a number".replace("%1%", value)));
								}
								break;
							default:
								plugin.logger.warning(ChatColor.translateAlternateColorCodes('&', "configType \"%1%\" unrecognised - this is a bug".replace("%1%", PlayerHeads.configKeys.get(keySet.toLowerCase()).toString())));
								break;
							}
							break;
						}
					}
					if (!keyFound) {
						plugin.configFile.set(key, value);
					}
					plugin.saveConfig();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"config"+":"+"set"+"]"+" "+key+": "+plugin.configFile.get(key)));
					return true;
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"config"+":"+"set"+"]"+" "+"Syntax"+": "+label+" "+"config"+" "+"set"+" "+"<variable>"+" "+"[value]"));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"config"+":"+"set"+"]"+" "+"Config variables"+": "+PlayerHeads.configKeysString));
					return true;
				}
			} else if (args[1].equalsIgnoreCase("reload")) {
				if (!sender.hasPermission("playerheads.config.set")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"config"+":"+"reload"+"]"+" "+"You don't have permission to use that command"));
					return true;
				}
				plugin.reloadConfig();
				plugin.configFile = plugin.getConfig();
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"config"+":"+"reload"+"]"+" "+"Config reloaded"));
				return true;
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"config"+":"+"??"+"]"+" "+"Invalid subcommand"));
				return true;
			}
		}
		else if (args[0].equalsIgnoreCase("spawn")) {
			String skullOwner;
			boolean haspermission = false;
			Player reciever = null;
			boolean isConsoleSender = !(sender instanceof Player);
			
			if (isConsoleSender) {
				if (args.length != 3) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"spawn"+"]"+" "+"Syntax"+": "+label+" "+"spawn"+" "+"<headname>"+" "+"<reciever>"));
					return true;
				}
			} else {
				reciever = (Player)sender;
			}
			if ((args.length == 1) || ((args.length == 2) && !isConsoleSender && ((Player)sender).getName().equalsIgnoreCase(args[1]))) {
				skullOwner = ((Player)sender).getName();
				haspermission = sender.hasPermission("playerheads.spawn.own");
			} else if ((args.length == 2) && !isConsoleSender) {
				skullOwner = args[1];
				haspermission = sender.hasPermission("playerheads.spawn");
			} else if (args.length == 3) {
				if ((reciever = plugin.getServer().getPlayer(args[2])) == null) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"spawn"+"]"+" "+"Can not find %1% online".replace("%1%", args[2])));
					return true;
				}
				skullOwner = args[1];
				haspermission = sender.hasPermission("playerheads.spawn.forother");
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"spawn"+"]"+" "+"Syntax"+": "+label+" "+"spawn"+" "+"[headname]"+" "+"[reciever]"));
				return true;
			}
			if (!haspermission) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"spawn"+"]"+" "+"You don't have permission to use that command"));
				return true;
			}
			if (plugin.configFile.getBoolean("fixcase")) {
				skullOwner = PlayerHeads.fixcase(skullOwner);
			}
			if (PlayerHeads.addHead(reciever, skullOwner)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"spawn"+"]"+" "+"Spawned %1%'s Head".replace("%1%", skullOwner)));
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"spawn"+"]"+" "+"Well I can't very well spawn a head if the inventory is full"));
			}
			return true;
		} else if (args[0].equalsIgnoreCase("rename")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"spawn"+"]"+" "+"Sorry console, heads are for players!"));
				return true;
			}
			if (!sender.hasPermission("playerheads.rename")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"rename"+"]"+" "+"You don't have permission to use that command"));
				return true;
			}
			if (!((args.length == 1) || (args.length == 2))) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"rename"+"]"+" "+"Syntax"+": "+label+" "+"rename"+" "+"[headname]"));
				return true;
			}
			ItemStack skull = ((Player)sender).getItemInHand();
			if (!((skull.getType() == Material.SKULL_ITEM) && (skull.getDurability() == 3))) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"rename"+"]"+" "+"That's not a player head"));
				return true;
			}
			SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
			if (args.length >= 2) {
				if (plugin.configFile.getBoolean("fixcase")) {
					skullMeta.setOwner(PlayerHeads.fixcase(args[1]));
				} else {
					skullMeta.setOwner(args[1]);
				}
			} else {
				skullMeta.setOwner("");
			}
			skull.setItemMeta(skullMeta);
			((Player)sender).setItemInHand(skull);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"rename"+"]"+" "+"Successfully renamed Head"));
			return true;
		} else if (args[0].equalsIgnoreCase("update")) {
			if (!sender.hasPermission("playerheads.update")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"update"+"]"+" "+"You don't have permission to use that command"));
				return true;
			}
			if (!plugin.configFile.getBoolean("autoupdate")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"update"+"]"+" "+"Updater is disabled"));
				return true;
			}
			if (!plugin.getUpdateReady()) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"update"+"]"+" "+"There is no update available"));
				return true;
			}
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"update"+"]"+" "+"Update started, check console for info"));
			plugin.update();
			return true;
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "["+label+":"+"??"+"]"+" "+"Invalid subcommand"));
			return true;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){
		if (!cmd.getName().equalsIgnoreCase("PlayerHeads")) {
			return null;
		}
		
		ArrayList<String> completions = new ArrayList<String>();
		
		for (int i = 0; i < args.length; i++) {
			args[i] = args[i].toLowerCase();
		}
		
		if (args.length == 1) {
			if ("config".startsWith(args[0])) {
				completions.add("config");
			}
			if ("spawn".startsWith(args[0])) {
				completions.add("spawn");
			}
			if ("rename".startsWith(args[0])) {
				completions.add("rename");
			}
			if ("update".startsWith(args[0])) {
				completions.add("update");
			}
			return sort(completions);
		}
		if (args[0].equals("config")) {
			if (args.length == 2) {
				if ("get".startsWith(args[1])) {
					completions.add("get");
				}
				if ("set".startsWith(args[1])) {
					completions.add("set");
				}
				if ("reload".startsWith(args[1])) {
					completions.add("reload");
				}
				return sort(completions);
			}

			if (args[1].equals("get") || args[1].equals("set")) {
				if (args.length == 3) {
					for (String keySet : PlayerHeads.configKeys.keySet()) {
						if (keySet.startsWith(args[2])) {
							completions.add(keySet);
						}
					}
					return sort(completions);
				}
			}
			return completions;
		} else if (args[0].equals("spawn")) {
			if (args.length > 3) {
				return completions;
			}
		} else if (args[0].equals("rename")) {
			if (args.length > 2) {
				return completions;
			}
		} else if (args[0].equals("update")) {
			return completions;
		}
		return null;
	}
	
	public List<String> sort(List<String> completions) {
		Collections.sort(completions, String.CASE_INSENSITIVE_ORDER);
		return completions;
	}

}
