/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.shininet.bukkit.playerheads;

import com.github.crashdemons.playerheads.SkullConverter;
import com.github.crashdemons.playerheads.SkullManager;
import com.github.crashdemons.playerheads.TexturedSkullType;
import com.github.crashdemons.playerheads.compatibility.Compatibility;
import com.github.crashdemons.playerheads.compatibility.SkullBlockAttachment;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Defines a command handler for the plugin.
 *
 * @author meiskam
 */
class PlayerHeadsCommandExecutor implements CommandExecutor, TabCompleter {

    private final PlayerHeads plugin;

    public PlayerHeadsCommandExecutor(PlayerHeads plugin) {
        this.plugin = plugin;
    }
    
    private boolean onCommandSetblock(CommandSender sender, Command cmd, String label, String[] args, String scope){
        if (!sender.hasPermission("playerheads.setblock")) {
            formatMsg(sender, scope, Lang.ERROR_PERMISSION);
            return true;
        }
        
        String invalidSyntaxMsg = Lang.SYNTAX + Lang.COLON_SPACE + scope + Lang.SPACE + Lang.CMD_SETBLOCK  + Lang.SPACE + Lang.OPT_WORLD_REQUIRED + Lang.SPACE +  Lang.OPT_COORDS_REQUIRED + Lang.SPACE + Lang.OPT_HEADNAME_REQUIRED + Lang.SPACE + Lang.OPT_ATTACHMENT_OPTIONAL + Lang.SPACE + Lang.OPT_FACING_OPTIONAL;
        
        String skullOwner;
        boolean isConsoleSender = !(sender instanceof Player);


        boolean usevanillaskull = plugin.configFile.getBoolean("dropvanillaheads");
        
        //ph setblock <world> <x> <y> <z> <headname> [attachment] [facing]
        if(args.length<6){
            formatMsg(sender, scope, invalidSyntaxMsg);
            return false;
        }
        
        World w = null;
        int x = 0, y=0, z=0;
        
        if(!isConsoleSender && args[1].equals("~")){
            Player player = (Player) sender;
            w = player.getWorld();
        }else{
            w = Bukkit.getWorld(args[1]);
        }
        
        if(w==null){
            formatMsg(sender, scope, Lang.ERROR_INVALID_WORLD, args[1]);
            return true;
        }
        try{
            x = Integer.parseInt(args[2]);
            y = Integer.parseInt(args[3]);
            z = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) { 
            formatMsg(sender, scope, Lang.ERROR_INVALID_COORDS, args[2], args[3], args[4]);
            return true;
        }
        
        skullOwner = args[5];
        
        if (plugin.configFile.getBoolean("fixcase")) {
            skullOwner = fixcase(skullOwner);
        }
        
        Block block =w.getBlockAt(x, y, z);
        if(block==null){//make sure it's a valid block to change
            formatMsg(sender, scope, Lang.ERROR_CANNOT_SETBLOCK);
            return true;
        }
        
        
        SkullBlockAttachment attachment = SkullBlockAttachment.FLOOR;
        if(args.length>6){
            String attachmentName = args[6].toUpperCase();
            try{
                attachment = SkullBlockAttachment.valueOf(attachmentName);
            }catch(Exception e){
                formatMsg(sender, scope, Lang.ERROR_INVALID_ATTACHMENT, attachmentName);
                return true;
            }
        }
        
        BlockFace facing = BlockFace.NORTH;
        if(args.length>7){
            String facingName = args[7].toUpperCase();
            try{
                facing = BlockFace.valueOf(facingName);
            }catch(Exception e){
                formatMsg(sender, scope, Lang.ERROR_INVALID_FACING, facingName);
                return true;
            }
        }
        
        
        if(!SkullBlockAttachment.isValidOrientation(facing,attachment)){
            formatMsg(sender, scope, Lang.ERROR_INVALID_COMBINATION, attachment.name(), facing.name());
            return true;
        }
        
        
        //if (InventoryManager.addHead(receiver, skullOwner, quantity, usevanillaskull, addLore)) {
        
        if (InventoryManager.setBlock(w, x, y, z, attachment, skullOwner, facing, usevanillaskull)) {
            TexturedSkullType type = TexturedSkullType.getBySpawnName(skullOwner);
            String headName = (type==null) ? TexturedSkullType.getDisplayName(skullOwner) : type.getDisplayName();
            String forWhom = sender.getName();
            formatMsg(sender, scope, Lang.SET_BLOCK, headName);
        } else {
            formatMsg(sender, scope, Lang.ERROR_SETBLOCK_FAILED);
        }
        return true;
    }

    private void formatMsg(CommandSender sender, String commandscope, String message, String... replacements) {
        Formatter.formatMsg(sender, Lang.BRACKET_LEFT + commandscope + Lang.BRACKET_RIGHT + Lang.SPACE + message, replacements);
    }
    
    private String getConfigDisplay(String key){
        return key + Lang.COLON_SPACE + Config.getValueDisplayString(plugin.configFile, key);
    }

    private boolean onCommandConfigGet(CommandSender sender, Command cmd, String label, String[] args, String scope) {
        if (!sender.hasPermission("playerheads.config.get")) {
            formatMsg(sender, scope, Lang.ERROR_PERMISSION);
            return true;
        }
        if (args.length == 3) {
            String key = args[2].toLowerCase();
            formatMsg(sender, scope, getConfigDisplay(key));
        } else {
            formatMsg(sender, scope, Lang.SYNTAX + Lang.COLON_SPACE + scope + Lang.SPACE + Lang.OPT_VARIABLE_REQUIRED);//Syntax: ph config get <whatever>
            formatMsg(sender, scope, Lang.CONFIG_VARIABLES + Lang.COLON_SPACE + Config.configKeysString);//Config variables: x, y, z
        }
        return true;
    }

    private boolean onCommandConfigSet(CommandSender sender, Command cmd, String label, String[] args, String scope) {
        if (!sender.hasPermission("playerheads.config.set")) {
            formatMsg(sender, scope, Lang.ERROR_PERMISSION);
            return true;
        }
        if (args.length == 3) {
            String key = args[2].toLowerCase();
            plugin.configFile.set(key, null);
            plugin.saveConfig();
            formatMsg(sender, scope, getConfigDisplay(key));
            return true;
        } else if (args.length == 4) {
            String key = args[2].toLowerCase();
            String value = args[3];
            
            try{
                Config.setValue(plugin.configFile, key, value);
            }catch(NumberFormatException e){
                formatMsg(sender, scope, Lang.ERROR_NUMBERCONVERT, value);
            }catch(IllegalStateException e){
                plugin.logger.warning(Formatter.format(Lang.ERROR_CONFIGTYPE, Config.configKeys.get(key).toString()));
            }
            
            plugin.saveConfig();
            formatMsg(sender, scope, getConfigDisplay(key));
            return true;
        } else {
            formatMsg(sender, scope, Lang.SYNTAX + Lang.COLON_SPACE + scope + Lang.SPACE + Lang.OPT_VARIABLE_REQUIRED + Lang.SPACE + Lang.OPT_VALUE_OPTIONAL);
            formatMsg(sender, scope, Lang.CONFIG_VARIABLES + Lang.COLON_SPACE + Config.configKeysString);
            return true;
        }
    }

    private boolean onCommandConfigReload(CommandSender sender, Command cmd, String label, String[] args, String scope) {
        if (!sender.hasPermission("playerheads.config.set")) {
            formatMsg(sender, scope, Lang.ERROR_PERMISSION);
            return true;
        }
        plugin.reloadConfig();
        plugin.configFile = plugin.getConfig();
        Lang.reload();
        plugin.onConfigReloaded();
        formatMsg(sender, scope, Lang.CONFIG_RELOADED);
        return true;
    }

    private boolean onCommandConfig(CommandSender sender, Command cmd, String label, String[] args, String scope) {
        if (args.length == 1) {
            formatMsg(sender, scope, Lang.SUBCOMMANDS + Lang.COLON_SPACE
                    + Lang.CMD_GET + Lang.COMMA_SPACE
                    + Lang.CMD_SET + Lang.COMMA_SPACE
                    + Lang.CMD_RELOAD
            );
            //[ph:config] Subcommands: get, set, reload
            return true;
        }
        if (args[1].equalsIgnoreCase(Formatter.formatStrip(Lang.CMD_GET))) {
            return onCommandConfigGet(sender, cmd, label, args, scope + Lang.COLON + Lang.CMD_GET);
        } else if (args[1].equalsIgnoreCase(Formatter.formatStrip(Lang.CMD_SET))) {
            return onCommandConfigSet(sender, cmd, label, args, scope + Lang.COLON + Lang.CMD_SET);
        } else if (args[1].equalsIgnoreCase(Formatter.formatStrip(Lang.CMD_RELOAD))) {
            return onCommandConfigReload(sender, cmd, label, args, scope + Lang.COLON + Lang.CMD_RELOAD);
        } else {
            scope += Lang.COLON + Lang.CMD_UNKNOWN;
            formatMsg(sender, scope, Lang.ERROR_INVALID_SUBCOMMAND);
            return true;
        }
    }

    private boolean onCommandSpawn(CommandSender sender, Command cmd, String label, String[] args, String scope) {
        String skullOwner;
        boolean haspermission;
        Player receiver = null;
        int quantity = Config.defaultStackSize;
        boolean isConsoleSender = !(sender instanceof Player);

        boolean usevanillaskull = plugin.configFile.getBoolean("dropvanillaheads");

        if (isConsoleSender) {
            if ((args.length != 3) && (args.length != 4)) {
                formatMsg(sender, scope, Lang.SYNTAX + Lang.COLON_SPACE + scope + Lang.SPACE + Lang.OPT_HEADNAME_REQUIRED + Lang.SPACE + Lang.OPT_RECEIVER_REQUIRED + Lang.SPACE + Lang.OPT_AMOUNT_OPTIONAL);
                return true;
            }
        } else {
            receiver = (Player) sender;
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
            receiver = plugin.getServer().getPlayer(args[2]);
            if (receiver == null) {
                formatMsg(sender, scope, Lang.ERROR_NOT_ONLINE, args[2]);
                return true;
            }
            skullOwner = args[1];
            if (receiver.equals(sender)) {
                haspermission = sender.hasPermission("playerheads.spawn");
            } else {
                haspermission = sender.hasPermission("playerheads.spawn.forother");
            }
        } else {
            formatMsg(sender, scope, Lang.SYNTAX + Lang.COLON_SPACE + scope + Lang.SPACE + Lang.OPT_HEADNAME_OPTIONAL + Lang.SPACE + Lang.OPT_RECEIVER_OPTIONAL + Lang.SPACE + Lang.OPT_AMOUNT_OPTIONAL);
            return true;
        }
        if (!haspermission) {
            formatMsg(sender, scope, Lang.ERROR_PERMISSION);
            return true;
        }
        if (plugin.configFile.getBoolean("fixcase")) {
            skullOwner = fixcase(skullOwner);
        }
        boolean addLore = plugin.configFile.getBoolean("addlore");
        if (InventoryManager.addHead(receiver, skullOwner, quantity, usevanillaskull, addLore)) {
            TexturedSkullType type = TexturedSkullType.getBySpawnName(skullOwner);
            String headName = (type==null) ? TexturedSkullType.getDisplayName(skullOwner) : type.getDisplayName();
            String forWhom = receiver.getName();
            formatMsg(sender, scope, Lang.SPAWNED_HEAD2, headName, forWhom, ""+quantity);
        } else {
            formatMsg(sender, scope, Lang.ERROR_INV_FULL);
        }
        return true;
    }

    private boolean onCommandRename(CommandSender sender, Command cmd, String label, String[] args, String scope) {

        boolean usevanillaskull = plugin.configFile.getBoolean("dropvanillaheads");

        if (!(sender instanceof Player)) {
            formatMsg(sender, scope, Lang.ERROR_CONSOLE_SPAWN);
            return true;
        }
        if (!sender.hasPermission("playerheads.rename") && !sender.hasPermission("playerheads.rename.mob") && !sender.hasPermission("playerheads.rename.player")) {
            formatMsg(sender, scope, Lang.ERROR_PERMISSION);
            return true;
        }
        if (!((args.length == 1) || (args.length == 2))) {
            formatMsg(sender, scope, Lang.SYNTAX + Lang.COLON_SPACE + scope + Lang.SPACE + Lang.OPT_HEADNAME_OPTIONAL);
            return true;
        }

        //input item processing (from inventory)
        ItemStack skullInput = Compatibility.getProvider().getItemInMainHand((Player) sender);//.getEquipment().getItemInMainHand();

        TexturedSkullType inputSkullType = SkullConverter.skullTypeFromItemStack(skullInput,true,true);//here PLAYER means  playerhead or Player Mob head - only returns null on unknown material OR filtered by option
        if (inputSkullType == null) {
            formatMsg(sender, scope, Lang.ERROR_NOT_A_HEAD);
            return true;
        }

        //output item processing (to spawnname)
        ItemStack skullOutput;
        String spawnName = "";
        if (args.length >= 2) {
            spawnName = args[1];
            if (plugin.configFile.getBoolean("fixcase")) {
                spawnName = fixcase(spawnName);
            }
        }
        TexturedSkullType outputSkullType = TexturedSkullType.getBySpawnName(spawnName);//here null means it's not a mob head. #player->PLAYER can be a "mob head"

        boolean fromPlayerhead = inputSkullType == TexturedSkullType.PLAYER;//PLAYER here means steve "Player" Heads or XXXXX's Head
        boolean toPlayerhead = outputSkullType == TexturedSkullType.PLAYER || outputSkullType == null;//PLAYER here means steve "Player" Heads, null==XXXXX's head

        boolean mobRename = !fromPlayerhead && !toPlayerhead;
        boolean playerRename = fromPlayerhead && toPlayerhead;

        boolean hasPermission = false;

        /*String inName=(inputSkullType==null?"null":inputSkullType.name());
            String outName=(outputSkullType==null?"null":outputSkullType.name());
            sender.sendMessage("FSK: "+inName + " " + fromPlayerhead);
            sender.sendMessage("TSK: "+outName + " " + toPlayerhead);
            sender.sendMessage("RNM: "+mobRename);
            sender.sendMessage("RNP: "+playerRename);*/
        if (mobRename) {
            hasPermission = sender.hasPermission("playerheads.rename.mob");
            scope += Lang.COLON + Lang.CMD_RENAME_SCOPE_MOB;
        } else if (playerRename) {
            hasPermission = sender.hasPermission("playerheads.rename.player");
            scope += Lang.COLON + Lang.CMD_RENAME_SCOPE_PLAYER;
        } else {
            hasPermission = sender.hasPermission("playerheads.rename");
            scope += Lang.COLON + Lang.CMD_RENAME_SCOPE_ALL;
        }

        if (!hasPermission) {
            formatMsg(sender, scope, Lang.ERROR_PERMISSION);
            return true;
        }

        boolean addLore = plugin.configFile.getBoolean("addlore");
        skullOutput = SkullManager.spawnSkull(spawnName, usevanillaskull, addLore);
        skullOutput.setAmount(skullInput.getAmount());
        Compatibility.getProvider().setItemInMainHand((Player) sender, skullOutput);//.getEquipment().setItemInMainHand(skullOutput);
        formatMsg(sender, scope, Lang.RENAMED_HEAD);
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("PlayerHeads")) {
            return false;
        }

        /*if(args.length==1){
            if(args[0].equalsIgnoreCase("test")){
                Player p = (Player) sender;
                sender.sendMessage("RN "+sender.hasPermission("playerheads.rename"));
                sender.sendMessage("RN* "+sender.hasPermission("playerheads.rename.*"));
                sender.sendMessage("RNM "+sender.hasPermission("playerheads.rename.mob"));
                sender.sendMessage("RNP "+sender.hasPermission("playerheads.rename.player"));
                sender.sendMessage("SP "+sender.hasPermission("playerheads.spawn"));
                sender.sendMessage("SPO"+sender.hasPermission("playerheads.spawn.own"));
                sender.sendMessage("SPFO"+sender.hasPermission("playerheads.spawn.forothers"));
                return true;
            }
        }*/
        String scope = label;

        if (args.length == 0) {
            formatMsg(sender, scope, Lang.SUBCOMMANDS + Lang.COLON_SPACE
                    + Lang.CMD_CONFIG + Lang.COMMA_SPACE
                    + Lang.CMD_SPAWN + Lang.COMMA_SPACE
                    + Lang.CMD_RENAME
            );
            //[ph] Subcommands: config, spawn, rename
            return true;
        }
        if (args[0].equalsIgnoreCase(Formatter.formatStrip(Lang.CMD_CONFIG))) {
            return onCommandConfig(sender, cmd, label, args, scope + Lang.COLON + Lang.CMD_CONFIG);
        } else if (args[0].equalsIgnoreCase(Formatter.formatStrip(Lang.CMD_SPAWN))) {
            return onCommandSpawn(sender, cmd, label, args, scope + Lang.COLON + Lang.CMD_SPAWN);
        } else if (args[0].equalsIgnoreCase(Formatter.formatStrip(Lang.CMD_RENAME))) {
            return onCommandRename(sender, cmd, label, args, scope + Lang.COLON + Lang.CMD_RENAME);
        } else if (args[0].equalsIgnoreCase(Formatter.formatStrip(Lang.CMD_SETBLOCK))) {
            return onCommandSetblock(sender, cmd, label, args, scope + Lang.COLON + Lang.CMD_SETBLOCK);
        } else {
            scope += Lang.COLON + Lang.CMD_UNKNOWN;
            formatMsg(sender, scope, Lang.ERROR_INVALID_SUBCOMMAND);
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

        final String cmd_config = Formatter.formatStrip(Lang.CMD_CONFIG);
        final String cmd_get = Formatter.formatStrip(Lang.CMD_GET);
        final String cmd_reload = Formatter.formatStrip(Lang.CMD_RELOAD);
        final String cmd_rename = Formatter.formatStrip(Lang.CMD_RENAME);
        final String cmd_set = Formatter.formatStrip(Lang.CMD_SET);
        final String cmd_spawn = Formatter.formatStrip(Lang.CMD_SPAWN);
        final String cmd_setblock = Formatter.formatStrip(Lang.CMD_SETBLOCK);

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
            if (cmd_setblock.startsWith(args[0])) {
                completions.add(cmd_setblock);
            }
            return sort(completions);
        }
        if (args[0].equals(cmd_setblock)) {
            
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

    /**
     * Attempts to find the correct casing for the input username by searching
     * online players, then offline players.
     *
     * @param inputName the username to fix
     * @return the same username, but with the case changed, if there was a
     * match.
     */
    private static String fixcase(String inputName) {
        String inputNameLC = inputName.toLowerCase();
        Player player = Bukkit.getServer().getPlayerExact(inputNameLC);

        if (player != null) {
            return player.getName();
        }

        for (OfflinePlayer offPlayer : Bukkit.getServer().getOfflinePlayers()) {
            if ((offPlayer.getName() != null) && (offPlayer.getName().toLowerCase().equals(inputNameLC))) {
                return offPlayer.getName();
            }
        }

        return inputName;
    }
}
