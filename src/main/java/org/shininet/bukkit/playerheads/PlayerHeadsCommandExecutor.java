/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import com.github.crashdemons.playerheads.Shim;
import com.github.crashdemons.playerheads.SkullManager;
import com.github.crashdemons.playerheads.TexturedSkullType;
import java.util.ArrayList;
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

class PlayerHeadsCommandExecutor implements CommandExecutor, TabCompleter {

    private final PlayerHeads plugin;

    public PlayerHeadsCommandExecutor(PlayerHeads plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("PlayerHeads")) {
            return false;
        }
        if(args.length==1){
            if(args[0].equalsIgnoreCase("test")){
                Player p = (Player) sender;
                
                p.getInventory().addItem(SkullManager.MobSkull(TexturedSkullType.HUSK, 3));
                return true;
            }
        }
        
        
        if (args.length == 0) {
            Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.SUBCOMMANDS + Lang.COLON_SPACE + Lang.CMD_CONFIG + Lang.COMMA_SPACE + Lang.CMD_SPAWN + Lang.COMMA_SPACE + Lang.CMD_RENAME);
            return true;
        }
        if (args[0].equalsIgnoreCase(Tools.formatStrip(Lang.CMD_CONFIG))) {
            if (args.length == 1) {
                Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_CONFIG + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.SUBCOMMANDS + Lang.COLON_SPACE + Lang.CMD_GET + Lang.COMMA_SPACE + Lang.CMD_SET + Lang.COMMA_SPACE + Lang.CMD_RELOAD);
                return true;
            }
            if (args[1].equalsIgnoreCase(Tools.formatStrip(Lang.CMD_GET))) {
                if (!sender.hasPermission("playerheads.config.get")) {
                    Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_CONFIG + Lang.COLON + Lang.CMD_GET + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.ERROR_PERMISSION);
                    return true;
                }
                if (args.length == 3) {
                    String key = args[2].toLowerCase();
                    Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_CONFIG + Lang.COLON + Lang.CMD_GET + Lang.BRACKET_RIGHT + Lang.SPACE + key + Lang.COLON_SPACE + plugin.configFile.get(key));
                } else {
                    Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_CONFIG + Lang.COLON + Lang.CMD_GET + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.SYNTAX + Lang.COLON_SPACE + label + Lang.SPACE + Lang.CMD_CONFIG + Lang.SPACE + Lang.CMD_GET + Lang.SPACE + Lang.OPT_VARIABLE_REQUIRED);
                    Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_CONFIG + Lang.COLON + Lang.CMD_GET + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.CONFIG_VARIABLES + Lang.COLON_SPACE + Config.configKeysString);
                }
                return true;
            } else if (args[1].equalsIgnoreCase(Tools.formatStrip(Lang.CMD_SET))) {
                if (!sender.hasPermission("playerheads.config.set")) {
                    Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_CONFIG + Lang.COLON + Lang.CMD_SET + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.ERROR_PERMISSION);
                    return true;
                }
                if (args.length == 3) {
                    String key = args[2].toLowerCase();
                    plugin.configFile.set(key, null);
                    plugin.saveConfig();
                    Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_CONFIG + Lang.COLON + Lang.CMD_SET + Lang.BRACKET_RIGHT + Lang.SPACE + key + Lang.COLON_SPACE + plugin.configFile.get(key));
                    return true;
                } else if (args.length == 4) {
                    String key = args[2].toLowerCase();
                    String value = args[3].toLowerCase();
                    boolean keyFound = false;

                    for (String keySet : Config.configKeys.keySet()) {
                        if (key.equals(keySet.toLowerCase())) {
                            keyFound = true;
                            switch (Config.configKeys.get(keySet.toLowerCase())) {
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
                                        Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_CONFIG + Lang.COLON + Lang.CMD_SET + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.ERROR_NUMBERCONVERT, value);
                                    }
                                    break;
                                case INT:
                                    try {
                                        plugin.configFile.set(key, Integer.parseInt(value));
                                    } catch (NumberFormatException e) {
                                        Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_CONFIG + Lang.COLON + Lang.CMD_SET + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.ERROR_NUMBERCONVERT, value);
                                    }
                                    break;
                                default:
                                    plugin.logger.warning(Tools.format(Lang.ERROR_CONFIGTYPE, Config.configKeys.get(keySet.toLowerCase()).toString()));
                                    break;
                            }
                            break;
                        }
                    }
                    if (!keyFound) {
                        plugin.configFile.set(key, value);
                    }
                    plugin.saveConfig();
                    Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_CONFIG + Lang.COLON + Lang.CMD_SET + Lang.BRACKET_RIGHT + Lang.SPACE + key + Lang.COLON_SPACE + plugin.configFile.get(key));
                    return true;
                } else {
                    Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_CONFIG + Lang.COLON + Lang.CMD_SET + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.SYNTAX + Lang.COLON_SPACE + label + Lang.SPACE + Lang.CMD_CONFIG + Lang.SPACE + Lang.CMD_SET + Lang.SPACE + Lang.OPT_VARIABLE_REQUIRED + Lang.SPACE + Lang.OPT_VALUE_OPTIONAL);
                    Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_CONFIG + Lang.COLON + Lang.CMD_SET + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.CONFIG_VARIABLES + Lang.COLON_SPACE + Config.configKeysString);
                    return true;
                }
            } else if (args[1].equalsIgnoreCase(Tools.formatStrip(Lang.CMD_RELOAD))) {
                if (!sender.hasPermission("playerheads.config.set")) {
                    Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_CONFIG + Lang.COLON + Lang.CMD_RELOAD + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.ERROR_PERMISSION);
                    return true;
                }
                plugin.reloadConfig();
                plugin.configFile = plugin.getConfig();
                Lang.reload();
                Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_CONFIG + Lang.COLON + Lang.CMD_RELOAD + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.CONFIG_RELOADED);
                return true;
            } else {
                Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_CONFIG + Lang.COLON + Lang.CMD_UNKNOWN + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.ERROR_INVALID_SUBCOMMAND);
                return true;
            }
        } else if (args[0].equalsIgnoreCase(Tools.formatStrip(Lang.CMD_SPAWN))) {
            String skullOwner;
            boolean haspermission;
            Player reciever = null;
            int quantity = Config.defaultStackSize;
            boolean isConsoleSender = !(sender instanceof Player);

            if (isConsoleSender) {
                if ((args.length != 3) && (args.length != 4)) {
                    Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_SPAWN + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.SYNTAX + Lang.COLON_SPACE + label + Lang.SPACE + Lang.CMD_SPAWN + Lang.SPACE + Lang.OPT_HEADNAME_REQUIRED + Lang.SPACE + Lang.OPT_RECEIVER_REQUIRED + Lang.SPACE + Lang.OPT_AMOUNT_OPTIONAL);
                    return true;
                }
            } else {
                reciever = (Player) sender;
            }
            if (args.length == 1 || args.length == 2 && sender.getName().equalsIgnoreCase(args[1])) {
                skullOwner = sender.getName();
                haspermission = sender.hasPermission("playerheads.spawn.own");
            } else if (args.length == 2) {
                skullOwner = args[1];
                haspermission = sender.hasPermission("playerheads.spawn");
            } else if ((args.length == 3) || (args.length == 4)) {
                if (args.length == 4) {
                    try {
                        quantity = Integer.parseInt(args[3]);
                    } catch (NumberFormatException ignored) {
                    }
                }
                if ((reciever = plugin.getServer().getPlayer(args[2])) == null) {
                    Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_SPAWN + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.ERROR_NOT_ONLINE, args[2]);
                    return true;
                }
                skullOwner = args[1];
                if (reciever.equals(sender)) {
                    haspermission = sender.hasPermission("playerheads.spawn");
                } else {
                    haspermission = sender.hasPermission("playerheads.spawn.forother");
                }
            } else {
                Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_SPAWN + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.SYNTAX + Lang.COLON_SPACE + label + Lang.SPACE + Lang.CMD_SPAWN + Lang.SPACE + Lang.OPT_HEADNAME_OPTIONAL + Lang.SPACE + Lang.OPT_RECEIVER_OPTIONAL + Lang.SPACE + Lang.OPT_AMOUNT_OPTIONAL);
                return true;
            }
            if (!haspermission) {
                Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_SPAWN + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.ERROR_PERMISSION);
                return true;
            }
            if (plugin.configFile.getBoolean("fixcase")) {
                skullOwner = Tools.fixcase(skullOwner);
            }
            if (Tools.addHead(reciever, skullOwner, quantity)) {
                Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_SPAWN + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.SPAWNED_HEAD, skullOwner);
            } else {
                Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_SPAWN + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.ERROR_INV_FULL);
            }
            return true;
        } else if (args[0].equalsIgnoreCase(Tools.formatStrip(Lang.CMD_RENAME))) {
            if (!(sender instanceof Player)) {
                Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_SPAWN + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.ERROR_CONSOLE_SPAWN);
                return true;
            }
            if (!sender.hasPermission("playerheads.rename")) {
                Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_RENAME + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.ERROR_PERMISSION);
                return true;
            }
            if (!((args.length == 1) || (args.length == 2))) {
                Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_RENAME + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.SYNTAX + Lang.COLON_SPACE + label + Lang.SPACE + Lang.CMD_RENAME + Lang.SPACE + Lang.OPT_HEADNAME_OPTIONAL);
                return true;
            }
            ItemStack skullInput = ((Player) sender).getEquipment().getItemInMainHand();
            if ( Shim.isSkull(skullInput.getType()) ) {
                Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_RENAME + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.ERROR_NOT_A_HEAD);
                return true;
            }
            ItemStack skullOutput;
            if (args.length >= 2) {
                if (plugin.configFile.getBoolean("fixcase")) {
                    skullOutput = Tools.Skull(Tools.fixcase(args[1]));
                } else {
                    skullOutput = Tools.Skull(args[1]);
                }
            } else {
                skullOutput = Tools.Skull("");
            }
            skullOutput.setAmount(skullInput.getAmount());
            ((Player) sender).getEquipment().setItemInMainHand(skullOutput);
            Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_RENAME + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.RENAMED_HEAD);
            return true;
        } else {
            Tools.formatMsg(sender, Lang.BRACKET_LEFT + label + Lang.COLON + Lang.CMD_UNKNOWN + Lang.BRACKET_RIGHT + Lang.SPACE + Lang.ERROR_INVALID_SUBCOMMAND);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("PlayerHeads")) {
            return null;
        }

        ArrayList<String> completions = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].toLowerCase();
        }

        final String cmd_config = Tools.formatStrip(Lang.CMD_CONFIG);
        final String cmd_get = Tools.formatStrip(Lang.CMD_GET);
        final String cmd_reload = Tools.formatStrip(Lang.CMD_RELOAD);
        final String cmd_rename = Tools.formatStrip(Lang.CMD_RENAME);
        final String cmd_set = Tools.formatStrip(Lang.CMD_SET);
        final String cmd_spawn = Tools.formatStrip(Lang.CMD_SPAWN);

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
                    for (String keySet : Config.configKeys.keySet()) {
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

    private List<String> sort(List<String> completions) {
        completions.sort(String.CASE_INSENSITIVE_ORDER);
        return completions;
    }

}
