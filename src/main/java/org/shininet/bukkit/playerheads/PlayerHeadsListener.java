/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.Iterator;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
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
            if (!player.hasPermission("playerheads.canloosehead")) {
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

            if (plugin.configFile.getBoolean("antideathchest")) {
                Location location = player.getLocation();
                location.getWorld().dropItemNaturally(location, Tools.Skull(skullOwner));
            } else {
                event.getDrops().add(Tools.Skull(skullOwner)); // drop the precious player head
            }

            if (plugin.configFile.getBoolean("broadcast")) {
                if (killer == null) {
                    plugin.getServer().broadcastMessage(Tools.format(Lang.BEHEAD_GENERIC, player.getDisplayName() + ChatColor.RESET));
                } else if (killer == player) {
                    plugin.getServer().broadcastMessage(Tools.format(Lang.BEHEAD_SELF, player.getDisplayName() + ChatColor.RESET));
                } else {
                    plugin.getServer().broadcastMessage(Tools.format(Lang.BEHEAD_OTHER, player.getDisplayName() + ChatColor.RESET, killer.getDisplayName() + ChatColor.RESET));
                }
            }
        } else if (entityType == EntityType.CREEPER) {
            EntityDeathHelper(event, SkullType.CREEPER, plugin.configFile.getDouble("creeperdroprate") * lootingrate);
        } else if (entityType == EntityType.ZOMBIE) {
            EntityDeathHelper(event, SkullType.ZOMBIE, plugin.configFile.getDouble("zombiedroprate") * lootingrate);
        } else if (entityType == EntityType.SKELETON) {
            if (((Skeleton) event.getEntity()).getSkeletonType() == Skeleton.SkeletonType.NORMAL) {
                EntityDeathHelper(event, SkullType.SKELETON, plugin.configFile.getDouble("skeletondroprate") * lootingrate);
            } else if (((Skeleton) event.getEntity()).getSkeletonType() == Skeleton.SkeletonType.WITHER) {
                for (Iterator<ItemStack> it = event.getDrops().iterator(); it.hasNext();) {
                    if (it.next().getType() == Material.SKULL_ITEM) {
                        it.remove();
                    }
                }
                EntityDeathHelper(event, SkullType.WITHER, plugin.configFile.getDouble("witherdroprate") * lootingrate);
            }
        } else if (entityType == EntityType.SLIME) {
            if (((Slime) event.getEntity()).getSize() == 1) {
                EntityDeathHelper(event, CustomSkullType.SLIME, plugin.configFile.getDouble("slimedroprate") * lootingrate);
            }
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

        if (type instanceof SkullType) {
            event.getDrops().add(Tools.Skull((SkullType) type));
        } else if (type instanceof CustomSkullType) {
            event.getDrops().add(Tools.Skull((CustomSkullType) type));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        if (block != null && block.getType() == Material.SKULL && plugin.configFile.getBoolean("clickinfo")) {
            Skull skullState = (Skull) block.getState();
            switch (skullState.getSkullType()) {
            case PLAYER:
                if (skullState.hasOwner()) {
                    String owner = skullState.getOwner();
                    //String ownerStrip = ChatColor.stripColor(owner); //Unnecessary?
                    CustomSkullType skullType = CustomSkullType.get(owner);
                    if (owner != null) {
                        Tools.formatMsg(player, Lang.CLICKINFO2, skullType.getDisplayName());
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
                    plugin.getServer().getPluginManager().callEvent(new PlayerAnimationEvent(player));
                    plugin.getServer().getPluginManager().callEvent(new BlockDamageEvent(player, block, player.getItemInHand(), true));

                    FakeBlockBreakEvent fakebreak = new FakeBlockBreakEvent(block, player);
                    plugin.getServer().getPluginManager().callEvent(fakebreak);

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
            Tools.formatMsg(player, Lang.UPDATE1, plugin.getUpdateName(), String.valueOf(plugin.getUpdateSize()));
            Tools.formatMsg(player, Lang.UPDATE3, "http://curse.com/server-mods/minecraft/" + Config.updateSlug);
        }
    }
}
