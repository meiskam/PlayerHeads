/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import com.github.crashdemons.playerheads.SkullConverter;
import com.github.crashdemons.playerheads.SkullManager;
import com.github.crashdemons.playerheads.TexturedSkullType;

import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.shininet.bukkit.playerheads.events.FakeBlockBreakEvent;
import org.shininet.bukkit.playerheads.events.MobDropHeadEvent;
import org.shininet.bukkit.playerheads.events.PlayerDropHeadEvent;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import org.bukkit.GameRule;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;

/**
 * @author meiskam
 */

class PlayerHeadsListener implements Listener {

    private final Random prng = new Random();
    private final PlayerHeads plugin;

    protected PlayerHeadsListener(PlayerHeads plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        double lootingrate = 1;

        if (killer != null) {
            ItemStack weapon = killer.getEquipment().getItemInMainHand();
            if (weapon != null) {
                lootingrate = 1 + (plugin.configFile.getDouble("lootingrate") * weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS));
            }
        }
        EntityType entityType = event.getEntityType();//entity type of the thing dying.
        TexturedSkullType skullType = SkullConverter.skullTypeFromEntityType(entityType);
        if(skullType==null) return;//entity type is one we don't support - don't attempt to handle heads for it.
        String mobDropConfig = SkullConverter.dropConfigFromSkullType(skullType);
        Double droprate = plugin.configFile.getDouble(mobDropConfig);
        //System.out.println(skullType);
        //System.out.println(mobDropConfig);
        switch (skullType) {
            case PLAYER:
                PlayerDeathHelper(event, skullType, droprate * lootingrate);
                break;
            case WITHER_SKELETON:
                if (droprate < 0) return;//if droprate is <0, don't modify drops
                event.getDrops().removeIf(
                        itemStack -> 
                                itemStack.getType() == Material.WITHER_SKELETON_SKULL
                );
                MobDeathHelper(event, skullType, droprate * lootingrate);
                break;
            default:
                MobDeathHelper(event, skullType, droprate * lootingrate);
                break;
        }
    }
    
    private void PlayerDeathHelper(EntityDeathEvent event, TexturedSkullType type, Double droprate){
        Double dropchance = prng.nextDouble();
        Player player = (Player) event.getEntity();
        Player killer = event.getEntity().getKiller();
        
        if(!player.hasPermission("playerheads.canlosehead")) return;
        if(killer!=null){//player was PK'd, so killer permissions apply.
            if(!killer.hasPermission("playerheads.canbehead")) return;//peronslly, I think canbehead should override alwaysbehead.
            if(killer.hasPermission("playerheads.alwaysbehead")) dropchance=0.0;//alwaysbehead just changes the chance in your favor - 0.0 is within all droprate ranges.
        }
        if(killer==player || killer==null){//self-kills and mob-kills are both handled as non-pk by the original plugin
            if(plugin.configFile.getBoolean("pkonly")) return;
        }
        if(dropchance >= droprate) return;
        //TODO: TEST
        
                
        String skullOwner;
        if (plugin.configFile.getBoolean("dropboringplayerheads")) {
            skullOwner = "";
        } else {
            skullOwner = player.getName();
        }
        
        ItemStack drop = SkullManager.PlayerSkull(skullOwner);
        PlayerDropHeadEvent dropHeadEvent = new PlayerDropHeadEvent(player, drop);
        plugin.getServer().getPluginManager().callEvent(dropHeadEvent);
        if (dropHeadEvent.isCancelled()) {
            return;
        }   
        
        //drop item naturally if the drops will be modified by another plugin or gamerule.
        if (plugin.configFile.getBoolean("antideathchest") || player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY)) {
            Location location = player.getLocation();
            location.getWorld().dropItemNaturally(location, drop);
        } else {
            event.getDrops().add(drop);
        }   
        
        //broadcast message about the beheading.
        if (plugin.configFile.getBoolean("broadcast")) {
            String message;
            if (killer == null) {
                message = Tools.format(Lang.BEHEAD_GENERIC, player.getDisplayName() + ChatColor.RESET);
            } else if (killer == player) {
                message = Tools.format(Lang.BEHEAD_SELF, player.getDisplayName() + ChatColor.RESET);
            } else {
                message = Tools.format(Lang.BEHEAD_OTHER, player.getDisplayName() + ChatColor.RESET, killer.getDisplayName() + ChatColor.RESET);
            }

            int broadcastRange = plugin.configFile.getInt("broadcastrange");
            if (broadcastRange > 0) {
                broadcastRange *= broadcastRange;
                Location location = player.getLocation();
                List<Player> players = player.getWorld().getPlayers();

                for (Player loopPlayer : players) {
                    if (location.distanceSquared(loopPlayer.getLocation()) <= broadcastRange) {
                        player.sendMessage(message);
                    }
                }
            } else {
                plugin.getServer().broadcastMessage(message);
            }
        }    
        
    }
    
    private void MobDeathHelper(EntityDeathEvent event, TexturedSkullType type, Double droprate) {
        Double dropchance = prng.nextDouble();
        Player killer = event.getEntity().getKiller();

        
        if(killer!=null){//mob was PK'd
            if(!killer.hasPermission("playerheads.canbeheadmob")) return;//killer does not have permission to behead mobs in any case
            if(killer.hasPermission("playerheads.alwaysbeheadmob")) dropchance=0.0;//alwaysbehead should only modify drop chances
        }else{//mob was killed by mob
            if(plugin.configFile.getBoolean("mobpkonly")) return;//mobs must only be beheaded by players
        }
        if(dropchance >= droprate) return;

        boolean usevanillaskull = plugin.configFile.getBoolean("dropvanillaheads");
        
        ItemStack drop = SkullManager.MobSkull(type,usevanillaskull);

        MobDropHeadEvent dropHeadEvent = new MobDropHeadEvent(event.getEntity(), drop);
        plugin.getServer().getPluginManager().callEvent(dropHeadEvent);

        if (dropHeadEvent.isCancelled()) {
            return;
        }

        if (plugin.configFile.getBoolean("antideathchest")) {
            Location location = event.getEntity().getLocation();
            location.getWorld().dropItemNaturally(location, drop);
        } else {
            event.getDrops().add(drop);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        if (block != null) {
            BlockState state = block.getState();
            TexturedSkullType skullType = SkullConverter.skullTypeFromBlockStateLegacy(state);
            if(skullType==null) return;
            //System.out.println(skullType.name());
            
            if (player.hasPermission("playerheads.clickinfo")) {
                switch (skullType) {
                    case PLAYER:
                        Skull skullState=(Skull) block.getState();
                        if (skullState.hasOwner()) {
                            String owner=null;
                            
                            OfflinePlayer op = skullState.getOwningPlayer();
                            if(op!=null) owner=op.getName();
                            if(owner==null) owner=skullState.getOwner();//this is deprecated, but the above method does NOT get the name tag from the NBT.
                            if(owner==null) owner="Unknown";
                            
                            //String ownerStrip = ChatColor.stripColor(owner); //Unnecessary?
                            Tools.formatMsg(player, Lang.CLICKINFO, owner);
                        } else {
                            //player.sendMessage("ClickInfo2 HEAD");
                            Tools.formatMsg(player, Lang.CLICKINFO2, Lang.HEAD);
                        }
                        break;
                    default:
                        Tools.formatMsg(player, Lang.CLICKINFO2, skullType.getDisplayName());
                        break;
                }
            }
            SkullManager.updatePlayerSkullState(state);
            
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event instanceof FakeBlockBreakEvent) {
            return;
        }
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE) {
            BlockState state = block.getState();
            TexturedSkullType skullType = SkullConverter.skullTypeFromBlockStateLegacy(state);
            if(skullType==null){
                
                //System.out.println("break null");
            }
            else{
                //System.out.println("break "+skullType.name());
                boolean isNotExempt = false;
                if (plugin.NCPHook) {
                    if (isNotExempt = !NCPExemptionManager.isExempted(player, CheckType.BLOCKBREAK_FASTBREAK)) {
                        NCPExemptionManager.exemptPermanently(player, CheckType.BLOCKBREAK_FASTBREAK);
                    }
                }

                plugin.getServer().getPluginManager().callEvent(new PlayerAnimationEvent(player));
                plugin.getServer().getPluginManager().callEvent(new BlockDamageEvent(player, block, player.getEquipment().getItemInMainHand(), true));

                FakeBlockBreakEvent fakebreak = new FakeBlockBreakEvent(block, player);
                plugin.getServer().getPluginManager().callEvent(fakebreak);

                if (plugin.NCPHook && isNotExempt) {
                    NCPExemptionManager.unexempt(player, CheckType.BLOCKBREAK_FASTBREAK);
                }

                if (fakebreak.isCancelled()) {
                    event.setCancelled(true);
                } else {
                    Location location = block.getLocation();
                    ItemStack item = null;
                    switch(skullType){
                        case PLAYER:
                            Skull skull = (Skull) block.getState();
                            item = SkullManager.PlayerSkull(skull.getOwner());
                            break;
                        default:
                            boolean blockIsSkinned = SkullConverter.isPlayerHead( block.getType() );
                            boolean usevanillaskull = plugin.configFile.getBoolean("dropvanillaheads");
                            boolean convertvanillahead = plugin.configFile.getBoolean("convertvanillaheads");
                            
                            //if the head is a skinned playerhead and usevanillaskull is set, then breaking it would convert it to a vanilla head
                            //if the head is a vanilla skull/head and usevanillaskull is unset, then breaking would convert it to a skinned head
                            boolean conversionWillHappen = (blockIsSkinned && usevanillaskull) || (!blockIsSkinned && !usevanillaskull);
                            if(conversionWillHappen && !convertvanillahead)
                                usevanillaskull=!usevanillaskull;//change the drop to the state that avoids converting it.
                            
                            item = SkullManager.MobSkull(skullType,usevanillaskull);
                            break;
                    }

                    event.setCancelled(true);
                    block.setType(Material.AIR);
                    location.getWorld().dropItemNaturally(location, item);
                }
            }
            
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("playerheads.update") && plugin.getUpdateReady()) {
            Tools.formatMsg(player, Lang.UPDATE1, plugin.getUpdateName());
            Tools.formatMsg(player, Lang.UPDATE3, "http://curse.com/bukkit-plugins/minecraft/" + Config.updateSlug);
        }
    }
}
