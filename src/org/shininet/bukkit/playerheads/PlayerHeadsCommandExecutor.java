/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.ArrayList;
import java.util.Collections;
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
			PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SUBCOMMANDS+Lang.COLON_SPACE+Lang.CMD_CONFIG+Lang.COMMA_SPACE+Lang.CMD_SPAWN+Lang.COMMA_SPACE+Lang.CMD_RENAME);
			return true;
		}
		if (args[0].equalsIgnoreCase(PlayerHeads.formatStrip(Lang.CMD_CONFIG))) {
			if (args.length == 1) {
				PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SUBCOMMANDS+Lang.COLON_SPACE+Lang.CMD_GET+Lang.COMMA_SPACE+Lang.CMD_SET+Lang.COMMA_SPACE+Lang.CMD_RELOAD);
				return true;
			}
			if (args[1].equalsIgnoreCase(PlayerHeads.formatStrip(Lang.CMD_GET))) {
				if (!sender.hasPermission("playerheads.config.get")) {
					PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_GET+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_PERMISSION);
					return true;
				}
				if (args.length == 3) {
					String key = args[2].toLowerCase();
					PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_GET+Lang.BRACKET_RIGHT+Lang.SPACE+key+Lang.COLON_SPACE+plugin.configFile.get(key));
				} else {
					PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_GET+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SYNTAX+Lang.COLON_SPACE+label+Lang.SPACE+Lang.CMD_CONFIG+Lang.SPACE+Lang.CMD_GET+Lang.SPACE+Lang.OPT_VARIABLE_REQUIRED);
					PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_GET+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.CONFIG_VARIABLES+Lang.COLON_SPACE+PlayerHeads.configKeysString);
				}
				return true;
			} else if (args[1].equalsIgnoreCase(PlayerHeads.formatStrip(Lang.CMD_SET))) {
				if (!sender.hasPermission("playerheads.config.set")) {
					PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_SET+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_PERMISSION);
					return true;
				}
				if (args.length == 3) {
					String key = args[2].toLowerCase();
					plugin.configFile.set(key, null);
					plugin.saveConfig();
					PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_SET+Lang.BRACKET_RIGHT+Lang.SPACE+key+Lang.COLON_SPACE+plugin.configFile.get(key));
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
									PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_SET+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_NUMBERCONVERT, value);
								}
								break;
							default:
								plugin.logger.warning(PlayerHeads.format(Lang.ERROR_CONFIGTYPE, PlayerHeads.configKeys.get(keySet.toLowerCase()).toString()));
								break;
							}
							break;
						}
					}
					if (!keyFound) {
						plugin.configFile.set(key, value);
					}
					plugin.saveConfig();
					PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_SET+Lang.BRACKET_RIGHT+Lang.SPACE+key+Lang.COLON_SPACE+plugin.configFile.get(key));
					return true;
				} else {
					PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_SET+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SYNTAX+Lang.COLON_SPACE+label+Lang.SPACE+Lang.CMD_CONFIG+Lang.SPACE+Lang.CMD_SET+Lang.SPACE+Lang.OPT_VARIABLE_REQUIRED+Lang.SPACE+Lang.OPT_VALUE_OPTIONAL);
					PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_SET+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.CONFIG_VARIABLES+Lang.COLON_SPACE+PlayerHeads.configKeysString);
					return true;
				}
			} else if (args[1].equalsIgnoreCase(PlayerHeads.formatStrip(Lang.CMD_RELOAD))) {
				if (!sender.hasPermission("playerheads.config.set")) {
					PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_RELOAD+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_PERMISSION);
					return true;
				}
				plugin.reloadConfig();
				plugin.configFile = plugin.getConfig();
				Lang.reload();
				PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_RELOAD+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.CONFIG_RELOADED);
				return true;
			} else {
				PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_CONFIG+Lang.COLON+Lang.CMD_UNKNOWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_INVALID_SUBCOMMAND);
				return true;
			}
		}
		else if (args[0].equalsIgnoreCase(PlayerHeads.formatStrip(Lang.CMD_SPAWN))) {
			String skullOwner;
			boolean haspermission = false;
			Player reciever = null;
			int quantity = PlayerHeads.defaultStackSize;
			boolean isConsoleSender = !(sender instanceof Player);
			
			if (isConsoleSender) {
				if ((args.length != 3) && (args.length != 4)) {
					PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_SPAWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SYNTAX+Lang.COLON_SPACE+label+Lang.SPACE+Lang.CMD_SPAWN+Lang.SPACE+Lang.OPT_HEADNAME_REQUIRED+Lang.SPACE+Lang.OPT_RECIEVER_REQUIRED+Lang.SPACE+Lang.OPT_AMOUNT_OPTIONAL);
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
			} else if ((args.length == 3) || (args.length == 4)) {
				if (args.length == 4) {
					try {
						quantity = Integer.parseInt(args[3]);
					} catch (NumberFormatException e) {
					}
				}
				if ((reciever = plugin.getServer().getPlayer(args[2])) == null) {
					PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_SPAWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_NOT_ONLINE, args[2]);
					return true;
				}
				skullOwner = args[1];
				haspermission = sender.hasPermission("playerheads.spawn.forother");
			} else {
				PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_SPAWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SYNTAX+Lang.COLON_SPACE+label+Lang.SPACE+Lang.CMD_SPAWN+Lang.SPACE+Lang.OPT_HEADNAME_OPTIONAL+Lang.SPACE+Lang.OPT_RECIEVER_OPTIONAL+Lang.SPACE+Lang.OPT_AMOUNT_OPTIONAL);
				return true;
			}
			if (!haspermission) {
				PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_SPAWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_PERMISSION);
				return true;
			}
			if (plugin.configFile.getBoolean("fixcase")) {
				skullOwner = PlayerHeads.fixcase(skullOwner);
			}
			if (PlayerHeads.addHead(reciever, skullOwner, quantity)) {
				PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_SPAWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SPAWNED_HEAD, skullOwner);
			} else {
				PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_SPAWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_INV_FULL);
			}
			return true;
		} else if (args[0].equalsIgnoreCase(PlayerHeads.formatStrip(Lang.CMD_RENAME))) {
			if (!(sender instanceof Player)) {
				PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_SPAWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_CONSOLE_SPAWN);
				return true;
			}
			if (!sender.hasPermission("playerheads.rename")) {
				PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_RENAME+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_PERMISSION);
				return true;
			}
			if (!((args.length == 1) || (args.length == 2))) {
				PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_RENAME+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.SYNTAX+Lang.COLON_SPACE+label+Lang.SPACE+Lang.CMD_RENAME+Lang.SPACE+Lang.OPT_HEADNAME_OPTIONAL);
				return true;
			}
			ItemStack skullInput = ((Player)sender).getItemInHand();
			if (skullInput.getType() != Material.SKULL_ITEM) {
				PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_RENAME+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_NOT_A_HEAD);
				return true;
			}
			ItemStack skullOutput;
			if (args.length >= 2) {
				if (plugin.configFile.getBoolean("fixcase")) {
					skullOutput = PlayerHeads.Skull(PlayerHeads.fixcase(args[1]));
				} else {
					skullOutput = PlayerHeads.Skull(args[1]);
				}
			} else {
				skullOutput = PlayerHeads.Skull("");
			}
			skullOutput.setAmount(skullInput.getAmount());
			((Player)sender).setItemInHand(skullOutput);
			PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_RENAME+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.RENAMED_HEAD);
			return true;
		} else {
			PlayerHeads.formatMsg(sender, Lang.BRACKET_LEFT+label+Lang.COLON+Lang.CMD_UNKNOWN+Lang.BRACKET_RIGHT+Lang.SPACE+Lang.ERROR_INVALID_SUBCOMMAND);
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
		
		final String cmd_config = PlayerHeads.formatStrip(Lang.CMD_CONFIG);
		final String cmd_get = PlayerHeads.formatStrip(Lang.CMD_GET);
		final String cmd_reload = PlayerHeads.formatStrip(Lang.CMD_RELOAD);
		final String cmd_rename = PlayerHeads.formatStrip(Lang.CMD_RENAME);
		final String cmd_set = PlayerHeads.formatStrip(Lang.CMD_SET);
		final String cmd_spawn = PlayerHeads.formatStrip(Lang.CMD_SPAWN);
		
		if (args.length == 1) {
			if (cmd_config.startsWith(args[0])) {
				completions.add(cmd_config);
			}
			if (cmd_spawn.startsWith(args[0])) {
				completions.add(cmd_spawn);
			}
			if (cmd_rename.startsWith(args[0])) {
				completions.add(cmd_rename);
			}
			return sort(completions);
		}
		if (args[0].equals(cmd_config)) {
			if (args.length == 2) {
				if (cmd_get.startsWith(args[1])) {
					completions.add(cmd_get);
				}
				if (cmd_set.startsWith(args[1])) {
					completions.add(cmd_set);
				}
				if (cmd_reload.startsWith(args[1])) {
					completions.add(cmd_reload);
				}
				return sort(completions);
			}

			if (args[1].equals(cmd_get) || args[1].equals(cmd_set)) {
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
		} else if (args[0].equals(cmd_spawn)) {
			if (args.length > 3) {
				return completions;
			}
		} else if (args[0].equals(cmd_rename)) {
			if (args.length > 2) {
				return completions;
			}
		}
		return null;
	}
	
	public List<String> sort(List<String> completions) {
		Collections.sort(completions, String.CASE_INSENSITIVE_ORDER);
		return completions;
	}

}
