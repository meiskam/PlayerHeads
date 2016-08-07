/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
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

import java.util.List;
import java.util.Random;

/**
 * @author meiskam
 */

public class PlayerHeadsListener implements Listener {

    private final Random prng = new Random();
    private PlayerHeads plugin;

    public PlayerHeadsListener(PlayerHeads plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        double lootingrate = 1;

        if (killer != null) {
            ItemStack weapon = killer.getItemInHand();
            if (weapon != null) {
                lootingrate = 1 + (plugin.configFile.getDouble("lootingrate") * weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS));
            }
        }

        EntityType entityType = event.getEntityType();
        if (entityType == EntityType.PLAYER) {
            Double dropchance = prng.nextDouble();
            Player player = (Player) event.getEntity();

            if ((dropchance >= plugin.configFile.getDouble("droprate") * lootingrate) && ((killer == null) || !killer.hasPermission("playerheads.alwaysbehead"))) {
                return;
            }
            if (!player.hasPermission("playerheads.canlosehead")) {
                return;
            }
            if (plugin.configFile.getBoolean("pkonly") && ((killer == null) || (killer == player) || !killer.hasPermission("playerheads.canbehead"))) {
                return;
            }

            String skullOwner;
            if (plugin.configFile.getBoolean("dropboringplayerheads")) {
                skullOwner = "";
            } else {
                skullOwner = player.getName();
            }

            ItemStack drop = Tools.Skull(skullOwner);

            PlayerDropHeadEvent dropHeadEvent = new PlayerDropHeadEvent(player, drop);
            plugin.getServer().getPluginManager().callEvent(dropHeadEvent);

            if (dropHeadEvent.isCancelled()) {
                return;
            }

            if (plugin.configFile.getBoolean("antideathchest")) {
                Location location = player.getLocation();
                location.getWorld().dropItemNaturally(location, drop);
            } else {
                event.getDrops().add(drop);
            }

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
        } else if (entityType == EntityType.CREEPER) {
            EntityDeathHelper(event, SkullType.CREEPER, plugin.configFile.getDouble("creeperdroprate") * lootingrate);
        } else if (entityType == EntityType.ZOMBIE) {
            EntityDeathHelper(event, SkullType.ZOMBIE, plugin.configFile.getDouble("zombiedroprate") * lootingrate);
        } else if (entityType == EntityType.SKELETON) {
            if (((Skeleton) event.getEntity()).getSkeletonType() == Skeleton.SkeletonType.NORMAL) {
                EntityDeathHelper(event, SkullType.SKELETON, plugin.configFile.getDouble("skeletondroprate") * lootingrate);
            }
//            } else if (((Skeleton) event.getEntity()).getSkeletonType() == Skeleton.SkeletonType.WITHER) {
//                for (Iterator<ItemStack> it = event.getDrops().iterator(); it.hasNext();) {
//                    if (it.next().getType() == Material.SKULL_ITEM) {
//                        it.remove();
//                    }
//                }
//                EntityDeathHelper(event, SkullType.WITHER, plugin.configFile.getDouble("witherdroprate") * lootingrate);
//            }
        } else if (entityType == EntityType.SLIME) {
            if (((Slime) event.getEntity()).getSize() == 1) {
                EntityDeathHelper(event, CustomSkullType.SLIME, plugin.configFile.getDouble("slimedroprate") * lootingrate);
            }
        } else if (entityType == EntityType.ENDER_DRAGON) {
            EntityDeathHelper(event, SkullType.DRAGON, plugin.configFile.getDouble("enderdragondroprate") * lootingrate);
        } else {
            try {
                CustomSkullType customSkullType = CustomSkullType.valueOf(entityType.name());
                EntityDeathHelper(event, customSkullType, plugin.configFile.getDouble(customSkullType.name().replace("_", "").toLowerCase() + "droprate") * lootingrate);
            } catch (IllegalArgumentException e) {
            }
        }
    }

    public void EntityDeathHelper(EntityDeathEvent event, Enum<?> type, Double droprate) {
        Double dropchance = prng.nextDouble();
        Player killer = event.getEntity().getKiller();

        if ((dropchance >= droprate) && ((killer == null) || !killer.hasPermission("playerheads.alwaysbeheadmob"))) {
            return;
        }
        if (plugin.configFile.getBoolean("mobpkonly") && ((killer == null) || !killer.hasPermission("playerheads.canbeheadmob"))) {
            return;
        }

        ItemStack drop;

        if (type instanceof SkullType) {
            drop = Tools.Skull((SkullType) type);
        } else if (type instanceof CustomSkullType) {
            drop = Tools.Skull((CustomSkullType) type);
        } else {
            return;
        }

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
        if (block != null && block.getType() == Material.SKULL) {
            Skull skullState = (Skull) block.getState();
            if (player.hasPermission("playerheads.clickinfo")) {
                switch (skullState.getSkullType()) {
                    case PLAYER:
                        String owner = skullState.getOwner();
                        if (skullState.hasOwner() && owner != null) {
                            //String ownerStrip = ChatColor.stripColor(owner); //Unnecessary?
                            CustomSkullType skullType = CustomSkullType.get(owner);
                            if (skullType != null) {
                                Tools.formatMsg(player, Lang.CLICKINFO2, skullType.getDisplayName());
                                if (!owner.equals(skullType.getOwner())) {
                                    skullState.setOwner(skullType.getOwner());
                                    skullState.update();
                                }
                            } else {
                                Tools.formatMsg(player, Lang.CLICKINFO, owner);
                            }
                        } else {
                            Tools.formatMsg(player, Lang.CLICKINFO2, Lang.HEAD);
                        }
                        break;
                    case CREEPER:
                        Tools.formatMsg(player, Lang.CLICKINFO2, Tools.format(Lang.HEAD_CREEPER));
                        break;
                    case SKELETON:
                        Tools.formatMsg(player, Lang.CLICKINFO2, Tools.format(Lang.HEAD_SKELETON));
                        break;
                    case WITHER:
                        Tools.formatMsg(player, Lang.CLICKINFO2, Tools.format(Lang.HEAD_WITHER));
                        break;
                    case ZOMBIE:
                        Tools.formatMsg(player, Lang.CLICKINFO2, Tools.format(Lang.HEAD_ZOMBIE));
                        break;
                }
            } else if ((skullState.getSkullType() == SkullType.PLAYER) && (skullState.hasOwner())) {
                String owner = skullState.getOwner();
                CustomSkullType skullType = CustomSkullType.get(owner);
                if ((skullType != null) && (!owner.equals(skullType.getOwner()))) {
                    skullState.setOwner(skullType.getOwner());
                    skullState.update();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event instanceof FakeBlockBreakEvent) {
            return;
        }
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if ((player.getGameMode() != GameMode.CREATIVE) && (block.getType() == Material.SKULL)) {
            Skull skull = (Skull) block.getState();
            if (skull.hasOwner()) {
                //String owner = ChatColor.stripColor(skull.getOwner()); //Unnecessary?
                CustomSkullType skullType = CustomSkullType.get(skull.getOwner());
                if (skullType != null) {
                    boolean isNotExempt = false;
                    if (plugin.NCPHook) {
                        if (isNotExempt = !NCPExemptionManager.isExempted(player, CheckType.BLOCKBREAK_FASTBREAK)) {
                            NCPExemptionManager.exemptPermanently(player, CheckType.BLOCKBREAK_FASTBREAK);
                        }
                    }

                    plugin.getServer().getPluginManager().callEvent(new PlayerAnimationEvent(player));
                    plugin.getServer().getPluginManager().callEvent(new BlockDamageEvent(player, block, player.getItemInHand(), true));

                    FakeBlockBreakEvent fakebreak = new FakeBlockBreakEvent(block, player);
                    plugin.getServer().getPluginManager().callEvent(fakebreak);

                    if (plugin.NCPHook && isNotExempt) {
                        NCPExemptionManager.unexempt(player, CheckType.BLOCKBREAK_FASTBREAK);
                    }

                    if (fakebreak.isCancelled()) {
                        event.setCancelled(true);
                    } else {
                        Location location = block.getLocation();
                        ItemStack item = Tools.Skull(skullType);

                        event.setCancelled(true);
                        block.setType(Material.AIR);
                        location.getWorld().dropItemNaturally(location, item);
                    }
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
