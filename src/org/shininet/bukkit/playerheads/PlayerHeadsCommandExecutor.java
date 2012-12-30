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
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SUBCOMMANDS+Lang.COLON_SPACE+Lang.CMD_CONFIG+Lang.COMMA_SPACE+Lang.CMD_SPAWN+Lang.COMMA_SPACE+Lang.CMD_RENAME+Lang.COMMA_SPACE+Lang.CMD_UPDATE));
			return true;
		}
		if (args[0].equalsIgnoreCase(Lang.CMD_CONFIG)) {
			if (args.length == 1) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SUBCOMMANDS+Lang.COLON_SPACE+Lang.CMD_GET+Lang.COMMA_SPACE+Lang.CMD_SET+Lang.COMMA_SPACE+Lang.CMD_RELOAD));
				return true;
			}
			if (args[1].equalsIgnoreCase(Lang.CMD_GET)) {
				if (!sender.hasPermission("playerheads.config.get")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_GET+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_PERMISSION));
					return true;
				}
				if (args.length == 3) {
					String key = args[2].toLowerCase();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_GET+Lang.BRACKET_RIGHT+Lang.SPACE+key+Lang.COLON_SPACE+plugin.configFile.get(key)));
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_GET+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SYNTAX+Lang.COLON_SPACE+label+Lang.SPACE+Lang.CMD_CONFIG+Lang.SPACE+Lang.CMD_GET+Lang.SPACE+Lang.OPT_VARIABLE_REQUIRED));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_GET+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.CONFIG_VARIABLES+Lang.COLON_SPACE+PlayerHeads.configKeysString));
				}
				return true;
			} else if (args[1].equalsIgnoreCase(Lang.CMD_SET)) {
				if (!sender.hasPermission("playerheads.config.set")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_SET+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_PERMISSION));
					return true;
				}
				if (args.length == 3) {
					String key = args[2].toLowerCase();
					plugin.configFile.set(key, null);
					plugin.saveConfig();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_SET+Lang.BRACKET_RIGHT+Lang.SPACE+key+Lang.COLON_SPACE+plugin.configFile.get(key)));
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
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_SET+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_NUMBERCONVERT.replace("%1%", value)));
								}
								break;
							default:
								plugin.logger.warning(ChatColor.translateAlternateColorCodes('&', Lang.ERROR_CONFIGTYPE.replace("%1%", PlayerHeads.configKeys.get(keySet.toLowerCase()).toString())));
								break;
							}
							break;
						}
					}
					if (!keyFound) {
						plugin.configFile.set(key, value);
					}
					plugin.saveConfig();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_SET+Lang.BRACKET_RIGHT+Lang.SPACE+key+Lang.COLON_SPACE+plugin.configFile.get(key)));
					return true;
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_SET+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SYNTAX+Lang.COLON_SPACE+label+Lang.SPACE+Lang.CMD_CONFIG+Lang.SPACE+Lang.CMD_SET+Lang.SPACE+Lang.OPT_VARIABLE_REQUIRED+Lang.SPACE+Lang.OPT_VALUE_OPTIONAL));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_SET+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.CONFIG_VARIABLES+Lang.COLON_SPACE+PlayerHeads.configKeysString));
					return true;
				}
			} else if (args[1].equalsIgnoreCase(Lang.CMD_RELOAD)) {
				if (!sender.hasPermission("playerheads.config.set")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_RELOAD+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_PERMISSION));
					return true;
				}
				plugin.reloadConfig();
				plugin.configFile = plugin.getConfig();
				Lang.reload();
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_RELOAD+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.CONFIG_RELOADED));
				return true;
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_UNKNOWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_INVALID_SUBCOMMAND));
				return true;
			}
		}
		else if (args[0].equalsIgnoreCase(Lang.CMD_SPAWN)) {
			String skullOwner;
			boolean haspermission = false;
			Player reciever = null;
			boolean isConsoleSender = !(sender instanceof Player);
			
			if (isConsoleSender) {
				if (args.length != 3) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_SPAWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SYNTAX+Lang.COLON_SPACE+label+Lang.SPACE+Lang.CMD_SPAWN+Lang.SPACE+Lang.OPT_HEADNAME_REQUIRED+Lang.SPACE+Lang.OPT_RECIEVER_REQUIRED));
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
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_SPAWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_NOT_ONLINE.replace("%1%", args[2])));
					return true;
				}
				skullOwner = args[1];
				haspermission = sender.hasPermission("playerheads.spawn.forother");
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_SPAWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SYNTAX+Lang.COLON_SPACE+label+Lang.SPACE+Lang.CMD_SPAWN+Lang.SPACE+Lang.OPT_HEADNAME_OPTIONAL+Lang.SPACE+Lang.OPT_RECIEVER_OPTIONAL));
				return true;
			}
			if (!haspermission) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_SPAWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_PERMISSION));
				return true;
			}
			if (plugin.configFile.getBoolean("fixcase")) {
				skullOwner = PlayerHeads.fixcase(skullOwner);
			}
			if (PlayerHeads.addHead(reciever, skullOwner)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_SPAWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SPAWNED_HEAD.replace("%1%", skullOwner)));
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_SPAWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_INV_FULL));
			}
			return true;
		} else if (args[0].equalsIgnoreCase(Lang.CMD_RENAME)) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_SPAWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_CONSOLE_SPAWN));
				return true;
			}
			if (!sender.hasPermission("playerheads.rename")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_RENAME+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_PERMISSION));
				return true;
			}
			if (!((args.length == 1) || (args.length == 2))) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_RENAME+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SYNTAX+Lang.COLON_SPACE+label+Lang.SPACE+Lang.CMD_RENAME+Lang.SPACE+Lang.OPT_HEADNAME_OPTIONAL));
				return true;
			}
			ItemStack skull = ((Player)sender).getItemInHand();
			if (!((skull.getType() == Material.SKULL_ITEM) && (skull.getDurability() == 3))) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_RENAME+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_NOT_A_HEAD));
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
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_RENAME+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.RENAMED_HEAD));
			return true;
		} else if (args[0].equalsIgnoreCase(Lang.CMD_UPDATE)) {
			if (!sender.hasPermission("playerheads.update")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_UPDATE+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_PERMISSION));
				return true;
			}
			if (!plugin.configFile.getBoolean("autoupdate")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_UPDATE+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_UPDATE_DISABLED));
				return true;
			}
			if (!plugin.getUpdateReady()) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_UPDATE+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_UPDATE_NOT_AVAILABLE));
				return true;
			}
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_UPDATE+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.UPDATE_STARTED));
			plugin.update();
			return true;
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_UNKNOWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_INVALID_SUBCOMMAND));
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
			if (Lang.CMD_CONFIG.startsWith(args[0])) {
				completions.add(Lang.CMD_CONFIG);
			}
			if (Lang.CMD_SPAWN.startsWith(args[0])) {
				completions.add(Lang.CMD_SPAWN);
			}
			if (Lang.CMD_RENAME.startsWith(args[0])) {
				completions.add(Lang.CMD_RENAME);
			}
			if (Lang.CMD_UPDATE.startsWith(args[0])) {
				completions.add(Lang.CMD_UPDATE);
			}
			return sort(completions);
		}
		if (args[0].equals(Lang.CMD_CONFIG)) {
			if (args.length == 2) {
				if (Lang.CMD_GET.startsWith(args[1])) {
					completions.add(Lang.CMD_GET);
				}
				if (Lang.CMD_SET.startsWith(args[1])) {
					completions.add(Lang.CMD_SET);
				}
				if (Lang.CMD_RELOAD.startsWith(args[1])) {
					completions.add(Lang.CMD_RELOAD);
				}
				return sort(completions);
			}

			if (args[1].equals(Lang.CMD_GET) || args[1].equals(Lang.CMD_SET)) {
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
		} else if (args[0].equals(Lang.CMD_SPAWN)) {
			if (args.length > 3) {
				return completions;
			}
		} else if (args[0].equals(Lang.CMD_RENAME)) {
			if (args.length > 2) {
				return completions;
			}
		} else if (args[0].equals(Lang.CMD_UPDATE)) {
			return completions;
		}
		return null;
	}
	
	public List<String> sort(List<String> completions) {
		Collections.sort(completions, String.CASE_INSENSITIVE_ORDER);
		return completions;
	}

}
