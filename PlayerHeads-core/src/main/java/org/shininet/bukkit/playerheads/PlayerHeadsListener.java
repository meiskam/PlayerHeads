/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.shininet.bukkit.playerheads;

import com.github.crashdemons.playerheads.antispam.InteractSpamPreventer;
import com.github.crashdemons.playerheads.SkullConverter;
import com.github.crashdemons.playerheads.SkullManager;
import com.github.crashdemons.playerheads.TexturedSkullType;
import com.github.crashdemons.playerheads.antispam.PlayerDeathSpamPreventer;
import com.github.crashdemons.playerheads.compatibility.Compatibility;
import com.github.crashdemons.playerheads.compatibility.CompatiblePlugins;
import com.github.crashdemons.playerheads.compatibility.plugins.SimulatedBlockBreakEvent;

import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.shininet.bukkit.playerheads.events.MobDropHeadEvent;
import org.shininet.bukkit.playerheads.events.PlayerDropHeadEvent;

import java.util.function.Predicate;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Tameable;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.projectiles.ProjectileSource;
import org.shininet.bukkit.playerheads.events.BlockDropHeadEvent;
import org.shininet.bukkit.playerheads.events.HeadRollEvent;

/**
 * Defines a listener for playerheads events.
 * <p>
 * <i>Note:</i> This documentation was inferred after the fact and may be
 * inaccurate.
 *
 * @author meiskam
 */
class PlayerHeadsListener implements Listener {

    private final Random prng = new Random();
    private final PlayerHeads plugin;
    private volatile InteractSpamPreventer clickSpamPreventer;
    private volatile PlayerDeathSpamPreventer deathSpamPreventer;

    public void registerAll() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void unregisterAll() {
        EntityDeathEvent.getHandlerList().unregister(this);
        PlayerInteractEvent.getHandlerList().unregister(this);
        PlayerJoinEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
    }

    private final Predicate<ItemStack> isVanillaHead = new Predicate<ItemStack>() { //we only need this because of java 7 support
        @Override
        public boolean test(ItemStack itemStack) {
            return Compatibility.getProvider().isHead(itemStack);
        }
    };

    protected PlayerHeadsListener(PlayerHeads plugin) {
        this.plugin = plugin;
        reloadConfig();
    }

    public void reloadConfig() {
        org.bukkit.configuration.file.FileConfiguration cfg = plugin.configFile; //just to make the following more readable
        clickSpamPreventer = new InteractSpamPreventer(cfg.getInt("clickspamcount"), cfg.getLong("clickspamthreshold"));
        deathSpamPreventer = new PlayerDeathSpamPreventer(cfg.getInt("deathspamcount"), cfg.getLong("deathspamthreshold"));
    }
    
    private Entity getEntityOwningEntity(EntityDamageByEntityEvent event){
        Entity entity = event.getDamager();
        if(entity instanceof Projectile){
            //System.out.println("   damager entity projectile");
            Projectile projectile = (Projectile) entity;
            ProjectileSource shooter = projectile.getShooter();
            if(shooter instanceof Entity){
                entity=(Entity) shooter;
                //if(entity!=null) System.out.println("   arrow shooter: "+entity.getType().name()+" "+entity.getName());
            }
        }else if(entity instanceof Tameable && plugin.configFile.getBoolean("considertameowner")){
            //System.out.println("   damager entity wolf");
            Tameable animal = (Tameable) entity;
            if(animal.isTamed()){
                AnimalTamer tamer = animal.getOwner();
                if(tamer instanceof Entity){
                    entity=(Entity) tamer;
                    //if(entity!=null) System.out.println("   wolf tamer: "+entity.getType().name()+" "+entity.getName());
                }
            }
        }
        return entity;
    }
    
    private LivingEntity getKillerEntity(EntityDeathEvent event){
        LivingEntity victim = event.getEntity();
        //if(victim!=null) System.out.println("victim: "+victim.getType().name()+" "+victim.getName());
        LivingEntity killer = victim.getKiller();
        //if(killer!=null) System.out.println("original killer: "+killer.getType().name()+" "+killer.getName());
        
        if(killer==null && plugin.configFile.getBoolean("considermobkillers")){
            EntityDamageEvent dmgEvent = event.getEntity().getLastDamageCause();
            if(dmgEvent instanceof EntityDamageByEntityEvent){
                Entity killerEntity = getEntityOwningEntity((EntityDamageByEntityEvent)dmgEvent);
                //if(killerEntity!=null) System.out.println(" parent killer: "+killerEntity.getType().name()+" "+killerEntity.getName());
                if(killerEntity instanceof LivingEntity) killer=(LivingEntity)killerEntity;
                //what if the entity isn't living (eg: arrow?)
            }
        }
        //if(killer!=null) System.out.println(" final killer: "+killer.getType().name()+" "+killer.getName());
        return killer;
    }
    

    /**
     * Event handler for entity deaths.
     * <p>
     * Used to determine when heads should be dropped.
     *
     * @param event the event received
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity killer = getKillerEntity(event);

        double lootingrate = 1;

        if (killer != null) {
            ItemStack weapon = Compatibility.getProvider().getItemInMainHand(killer);//killer.getEquipment().getItemInMainHand();
            if(weapon!=null){
                if(plugin.configFile.getBoolean("requireitem")){
                    String weaponType = weapon.getType().name().toLowerCase();
                    if(!plugin.configFile.getStringList("requireditems").contains(weaponType)) return;
                }
                lootingrate = 1 + (plugin.configFile.getDouble("lootingrate") * weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS));
            }
        }

        TexturedSkullType skullType = SkullConverter.skullTypeFromEntity(event.getEntity());
        if (skullType == null) {
            return;//entity type is one we don't support - don't attempt to handle heads for it.
        }
        String mobDropConfig = skullType.getConfigName();
        Double droprate = plugin.configFile.getDouble(mobDropConfig);
        if (droprate < 0) {
            return;//if droprate is <0, don't modify drops
        }
        switch (skullType) {
            case PLAYER:
                if (plugin.configFile.getBoolean("nerfdeathspam")) {
                    if (deathSpamPreventer.recordEvent(event).isSpam()) {
                        return;
                    }
                }
                PlayerDeathHelper(event, skullType, droprate, lootingrate);
                break;
            case WITHER_SKELETON:
                event.getDrops().removeIf(isVanillaHead);
                MobDeathHelper(event, skullType, droprate, lootingrate, 1.0);
                break;
            case SLIME:
            case MAGMA_CUBE:
                Double slimemodifier=1.0;
                Entity entity = event.getEntity();
                if(entity instanceof Slime){
                    int slimeSize = ((Slime) entity).getSize();// 0, 1, 2, 3  (0,1,3 natual with 0 the smallest)
                    slimemodifier=plugin.configFile.getDouble("slimemodifier."+slimeSize);
                }
                MobDeathHelper(event, skullType, droprate, lootingrate, slimemodifier);
                break;
            default:
                MobDeathHelper(event, skullType, droprate, lootingrate, 1.0);
                break;
        }
    }

    private void PlayerDeathHelper(EntityDeathEvent event, TexturedSkullType type, Double droprateOriginal, Double lootingModifier) {
        Double droprate = droprateOriginal * lootingModifier;
        Double dropchanceRand = prng.nextDouble();
        Double dropchance = dropchanceRand;
        Player player = (Player) event.getEntity();
        Player killer = event.getEntity().getKiller();

        boolean killerAlwaysBeheads = false;

        if (!player.hasPermission("playerheads.canlosehead")) {
            return;
        }
        if (killer != null) {//player was PK'd, so killer permissions apply.
            if (!killer.hasPermission("playerheads.canbehead")) {
                return;//peronslly, I think canbehead should override alwaysbehead.
            }
            killerAlwaysBeheads = killer.hasPermission("playerheads.alwaysbehead");
            if (killerAlwaysBeheads) {
                dropchance = 0.0;//alwaysbehead just changes the chance in your favor - 0.0 is within all droprate ranges.
            }
        }
        if (killer == player || killer == null) {//self-kills and mob-kills are both handled as non-pk by the original plugin
            if (plugin.configFile.getBoolean("pkonly")) {
                return;
            }
        }
        boolean headDropSuccess = dropchance < droprate;

        HeadRollEvent rollEvent = new HeadRollEvent(killer, player, killerAlwaysBeheads, lootingModifier, dropchanceRand, dropchance, droprateOriginal, droprate, headDropSuccess);
        plugin.getServer().getPluginManager().callEvent(rollEvent);
        if (!rollEvent.succeeded()) {
            return;//allow plugins a chance to modify the success
        }

        ItemStack drop = plugin.api.getHeadDrop(player);

        PlayerDropHeadEvent dropHeadEvent = new PlayerDropHeadEvent(player, drop);
        plugin.getServer().getPluginManager().callEvent(dropHeadEvent);
        if (dropHeadEvent.isCancelled()) {
            return;
        }

        //drop item naturally if the drops will be modified by another plugin or gamerule.
        if (plugin.configFile.getBoolean("antideathchest") || Compatibility.getProvider().getKeepInventory(player.getWorld())) {
            Location location = player.getLocation();
            location.getWorld().dropItemNaturally(location, drop);
        } else {
            event.getDrops().add(drop);
        }

        //broadcast message about the beheading.
        if (plugin.configFile.getBoolean("broadcast")) {
            String message;
            if (killer == null) {
                message = Formatter.format(Lang.BEHEAD_GENERIC, player.getDisplayName() + ChatColor.RESET);
            } else if (killer == player) {
                message = Formatter.format(Lang.BEHEAD_SELF, player.getDisplayName() + ChatColor.RESET);
            } else {
                message = Formatter.format(Lang.BEHEAD_OTHER, player.getDisplayName() + ChatColor.RESET, killer.getDisplayName() + ChatColor.RESET);
            }

            int broadcastRange = plugin.configFile.getInt("broadcastrange");
            if (broadcastRange > 0) {
                broadcastRange *= broadcastRange;
                Location location = player.getLocation();
                List<Player> players = player.getWorld().getPlayers();

                for (Player loopPlayer : players) {
                    try{
                        if (location.distanceSquared(loopPlayer.getLocation()) <= broadcastRange) {
                            loopPlayer.sendMessage(message);
                        }
                    }catch(IllegalArgumentException e){
                        //entities are in different worlds
                    }
                }
            } else {
                plugin.getServer().broadcastMessage(message);
            }
        }

    }

    private void MobDeathHelper(EntityDeathEvent event, TexturedSkullType type, Double droprateOriginal, Double lootingModifier, Double slimeModifier) {
        Double droprate = droprateOriginal * lootingModifier * slimeModifier;
        Double dropchanceRand = prng.nextDouble();
        Double dropchance = dropchanceRand;
        Entity entity = event.getEntity();
        Player killer = event.getEntity().getKiller();
        
        boolean killerAlwaysBeheads = false;
        if (killer != null) {//mob was PK'd
            if (!killer.hasPermission("playerheads.canbeheadmob")) {
                return;//killer does not have permission to behead mobs in any case
            }
            killerAlwaysBeheads = killer.hasPermission("playerheads.alwaysbeheadmob");
            if (killerAlwaysBeheads) {
                dropchance = 0.0;//alwaysbehead should only modify drop chances
            }
        } else {//mob was killed by mob
            if (plugin.configFile.getBoolean("mobpkonly")) {
                return;//mobs must only be beheaded by players
            }
        }
        boolean headDropSuccess = dropchance < droprate;

        HeadRollEvent rollEvent = new HeadRollEvent(killer, event.getEntity(), killerAlwaysBeheads, lootingModifier, slimeModifier, dropchanceRand, dropchance, droprateOriginal, droprate, headDropSuccess);
        plugin.getServer().getPluginManager().callEvent(rollEvent);
        if (!rollEvent.succeeded()) {
            return;//allow plugins a chance to modify the success
        }

        ItemStack drop = plugin.api.getHeadDrop(entity);

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
        //broadcast message about the beheading.
        if (plugin.configFile.getBoolean("broadcastmob") && killer!=null) { //mob-on-mob broadcasts would be extremely annoying!
            String entityName = entity.getCustomName​();
            if (entityName==null) entityName = entity.getName(); //notnull
            
            
            String message = Formatter.format(Lang.BEHEAD_OTHER, entityName + ChatColor.RESET, killer.getDisplayName() + ChatColor.RESET);

            int broadcastRange = plugin.configFile.getInt("broadcastmobrange");
            if (broadcastRange > 0) {
                broadcastRange *= broadcastRange;
                Location location = entity.getLocation();
                List<Player> players = entity.getWorld().getPlayers();

                for (Player loopPlayer : players) {
                    try{
                        if (location.distanceSquared(loopPlayer.getLocation()) <= broadcastRange) {
                            loopPlayer.sendMessage(message);
                        }
                    }catch(IllegalArgumentException e){
                        //entities are in different worlds
                    }
                }
            } else {
                plugin.getServer().broadcastMessage(message);
            }
        }
    }

    /**
     * Event handler for player-block interactions.
     * <p>
     * Used to determine when information should be sent to the user, or
     * head-blocks need updating.
     *
     * @param event the event received
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        if (block != null) {
            BlockState state = block.getState();
            TexturedSkullType skullType = SkullConverter.skullTypeFromBlockState(state);
            if (skullType == null) {
                return;
            }
            //System.out.println(skullType.name());

            if (clickSpamPreventer.recordEvent(event).isSpam()) {
                return;
            }

            if (player.hasPermission("playerheads.clickinfo")) {
                switch (skullType) {
                    case PLAYER:
                        Skull skullState = (Skull) block.getState();
                        if (skullState.hasOwner()) {
                            String owner = Compatibility.getProvider().getOwner(skullState);//SkullConverter.getSkullOwner(skullState);
                            if (owner == null) {
                                return;//this is an unsupported custom-texture head. don't print anything.
                            }
                            //String ownerStrip = ChatColor.stripColor(owner); //Unnecessary?
                            Formatter.formatMsg(player, Lang.CLICKINFO, owner);
                        } else {
                            //player.sendMessage("ClickInfo2 HEAD");
                            Formatter.formatMsg(player, Lang.CLICKINFO2, Lang.HEAD);
                        }
                        break;
                    default:
                        Formatter.formatMsg(player, Lang.CLICKINFO2, skullType.getDisplayName());
                        break;
                }
            }
            SkullManager.updatePlayerSkullState(state);

        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemSpawnEvent(ItemSpawnEvent event) {
        ItemStack stack = event.getEntity().getItemStack();
        //Location location = event.getEntity().getLocation();
        boolean fixDroppedHeads = plugin.configFile.getBoolean("fixdroppedheads");
        if (!fixDroppedHeads) {
            return;
        }
        TexturedSkullType skullType = SkullConverter.skullTypeFromItemStack(stack);
        if (skullType == null) {
            return;
        }
        ItemStack newstack = null;
        boolean addLore = plugin.configFile.getBoolean("addlore");
        switch (skullType) {
            case PLAYER:
                SkullMeta skull = (SkullMeta) stack.getItemMeta();
                String owner = Compatibility.getProvider().getOwner(skull);//SkullConverter.getSkullOwner(skull);
                if (owner == null) {
                    return;//you broke an unsupported custom-textured head. Question: should we instead just return to avoid modifying behavior?
                }
                newstack = SkullManager.PlayerSkull(owner, stack.getAmount(), addLore);
                break;
            default:
                boolean blockIsSkinnable = Compatibility.getProvider().isPlayerhead(stack);
                newstack = createConvertedMobhead(skullType, blockIsSkinnable, addLore, stack.getAmount());
                break;
        }
        if (newstack == null) {
            return;
        }
        event.getEntity().setItemStack(newstack);
    }

    //can conversion of an item occur depending on if the skull was skinned and dropvanillahead flag (if passed directly)?
    private static boolean canConversionHappen(boolean dropVanillaHeads, boolean isSourceSkinnable) {
        //if the head is a skinned playerhead and usevanillaskull is set, then breaking it would convert it to a vanilla head
        //if the head is a vanilla skull/head and usevanillaskull is unset, then breaking would convert it to a skinned head
        return (isSourceSkinnable && dropVanillaHeads) || (!isSourceSkinnable && !dropVanillaHeads);
    }

    private ItemStack createConvertedMobhead(TexturedSkullType skullType, boolean isSourceSkinnable, boolean addLore, int quantity) {
        boolean usevanillaskull = plugin.configFile.getBoolean("dropvanillaheads");
        boolean convertvanillahead = plugin.configFile.getBoolean("convertvanillaheads");

        //if the head is a skinned playerhead and usevanillaskull is set, then breaking it would convert it to a vanilla head
        //if the head is a vanilla skull/head and usevanillaskull is unset, then breaking would convert it to a skinned head
        boolean conversionCanHappen = canConversionHappen(usevanillaskull, isSourceSkinnable);
        if (conversionCanHappen && !convertvanillahead) {
            usevanillaskull = !usevanillaskull;//change the drop to the state that avoids converting it.
        }
        return SkullManager.MobSkull(skullType, quantity, usevanillaskull, addLore);
    }

    //drop a head based on a block being broken in some fashion
    //NOTE: the blockbreak handler expects this to unconditionally drop the item unless the new event is cancelled.
    private BlockDropResult blockDrop(BlockEvent event, Block block, BlockState state) {
        TexturedSkullType skullType = SkullConverter.skullTypeFromBlockState(state);
        Location location = block.getLocation();
        ItemStack item = null;
        boolean addLore = plugin.configFile.getBoolean("addlore");
        switch (skullType) {
            case PLAYER:
                Skull skull = (Skull) block.getState();
                String owner = Compatibility.getProvider().getOwner(skull);//SkullConverter.getSkullOwner(skull);
                if (owner == null) {
                    return BlockDropResult.FAILED_CUSTOM_HEAD;//you broke an unsupported custom-textured head.
                }
                item = SkullManager.PlayerSkull(owner, addLore);
                break;
            default:
                boolean blockIsSkinnable = Compatibility.getProvider().isPlayerhead(block.getState());
                item = createConvertedMobhead(skullType, blockIsSkinnable, addLore, Config.defaultStackSize);
                break;
        }
        block.setType(Material.AIR);
        BlockDropHeadEvent newEvent = new BlockDropHeadEvent(block, item);
        plugin.getServer().getPluginManager().callEvent(newEvent);
        if (newEvent.isCancelled()) {
            return BlockDropResult.FAILED_EVENT_CANCELLED;
        }
        location.getWorld().dropItemNaturally(location, item);
        return BlockDropResult.SUCCESS;
    }

    /**
     * Event handler for player block-break events.
     * <p>
     * Used to determine when the associated head item for a head-block needs to
     * be dropped, if it is broken by a player.
     *
     * @param event the event received
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event instanceof SimulatedBlockBreakEvent) {
            return;
        }
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE) {
            BlockState state = block.getState();
            TexturedSkullType skullType = SkullConverter.skullTypeFromBlockState(state);
            if (skullType != null) {

                boolean canBreak = true;
                if (CompatiblePlugins.isReady()) {
                    canBreak = CompatiblePlugins.testBlockBreak(block, player);
                }

                if (!canBreak) {
                    event.setCancelled(true);
                } else {
                    event.setCancelled(true);
                    BlockDropResult result = blockDrop(event, block, state);
                    if (result == BlockDropResult.FAILED_CUSTOM_HEAD) {
                        event.setCancelled(false);//uncancel the event if we can't drop it accurately - attempted fix for issue crashdemons/PlayerHeads#12
                    }
                }
            }

        }
    }

    /**
     * Event handler for player server join events
     * <p>
     * Used to send updater information to appropriate players on join.
     *
     * @param event the event received
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("playerheads.update") && plugin.getUpdateReady()) {
            Formatter.formatMsg(player, Lang.UPDATE1, plugin.getUpdateName());
            Formatter.formatMsg(player, Lang.UPDATE3, "http://curse.com/bukkit-plugins/minecraft/" + Config.updateSlug);
        }
    }
}
