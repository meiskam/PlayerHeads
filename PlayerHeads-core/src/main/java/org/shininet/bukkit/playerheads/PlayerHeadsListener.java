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
import com.github.crashdemons.playerheads.compatibility.CompatibleProfile;
import com.github.crashdemons.playerheads.compatibility.SkullType;
import com.github.crashdemons.playerheads.compatibility.plugins.SimulatedBlockBreakEvent;
import com.github.crashdemons.playerheads.compatibility.plugins.heads.HeadModificationHandling;
import java.util.ArrayList;

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
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.shininet.bukkit.playerheads.events.MobDropHeadEvent;
import org.shininet.bukkit.playerheads.events.PlayerDropHeadEvent;

import java.util.function.Predicate;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;
import org.shininet.bukkit.playerheads.events.BlockDropHeadEvent;
import org.shininet.bukkit.playerheads.events.HeadRollEvent;
import org.shininet.bukkit.playerheads.events.VanillaLivingEntityDropHeadEvent;

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
    
    private final static long TICKS_PER_SECOND = 20;
    private final static long MS_PER_TICK = 1000/TICKS_PER_SECOND; //50
    

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
    
    private LivingEntity getKillerEntity(EntityDeathEvent event){
        return Compatibility.getProvider().getKillerEntity(event, plugin.configFile.getBoolean("considermobkillers"), plugin.configFile.getBoolean("considertameowner"));
    }
    
    private enum VanillDropBehavior{
        IGNORE,
        REPLACE,
        VANILLA,
        BLOCK;
        
        public boolean allowsVanillaBehavior(){
            return this==VANILLA || this==IGNORE;
        }
        public boolean allowsPhBehavior(){
            return this==REPLACE || this==IGNORE;
        }
        
        public static VanillDropBehavior fromString(String value){
            try{
                return VanillDropBehavior.valueOf(value.toUpperCase());
            }catch(Exception e){
                return IGNORE;
            }
        }
        
        public VanillDropBehavior apply(VanillDropBehavior newBehavior){
            if(newBehavior==BLOCK || this==BLOCK) return BLOCK;//can't override a block / it always supercedes everything.
            if(this==IGNORE) return newBehavior;//any behavior can supercede ignore.
            if(newBehavior==IGNORE) return this;//current behavior will always be at least as restrictive as Ignore.
            
            //new behavior is NOT BLOCK (Vanilla,replace)
            //current behavior is NOT IGNORE or BLOCK (vanilla,replace)
            
            if(newBehavior==VANILLA || this==VANILLA) return VANILLA;//vanilla supercedes replace,vanilla
            return REPLACE;
        }
        
    }
    private class DeathParameters{
        public boolean cancelledEarly;
        public VanillaLivingEntityDropHeadEvent vanillaBeheadEvent;
        public VanillDropBehavior vanillaBehavior;
        public boolean isPlayerDeath;
        public EntityDeathEvent event;
        public LivingEntity killer;
        public TexturedSkullType skullType;
        public double droprate;
        public double lootingrate;
        public double slimemodifier;
        public double chargedcreeperModifier;

        public DeathParameters(){
            
        }
        
        public DeathParameters cancel(){
            this.cancelledEarly=true;
            return this;
        }
        public DeathParameters finish(){
            this.cancelledEarly=false;
            return this;
        }
        
        public DeathParameters(boolean cancelledEarly, VanillaLivingEntityDropHeadEvent vanillaBeheadEvent,VanillDropBehavior vanillaBehavior, boolean isPlayerDeath, EntityDeathEvent event, LivingEntity killer, TexturedSkullType skullType, double droprate, double lootingrate, double slimemodifier, double chargedcreeperModifier) {
            this.cancelledEarly = cancelledEarly;
            this.vanillaBeheadEvent = vanillaBeheadEvent;
            this.vanillaBehavior = vanillaBehavior;
            this.isPlayerDeath = isPlayerDeath;
            this.event = event;
            this.killer = killer;
            this.skullType = skullType;
            this.droprate = droprate;
            this.lootingrate = lootingrate;
            this.slimemodifier = slimemodifier;
            this.chargedcreeperModifier = chargedcreeperModifier;
        }

        @Override
        public String toString() {
            return "DeathParameters{" + "cancelledEarly=" + cancelledEarly + ", vanillaBeheadEvent=" + vanillaBeheadEvent + ", vanillaBehavior=" + vanillaBehavior + ", isPlayerDeath=" + isPlayerDeath + ", event=" + event + ", killer=" + killer + ", skullType=" + skullType + ", droprate=" + droprate + ", lootingrate=" + lootingrate + ", slimemodifier=" + slimemodifier + ", chargedcreeperModifier=" + chargedcreeperModifier + '}';
        }
        
        
        
    }
    
    private DeathParameters getDeathParameters(EntityDeathEvent event) {
        DeathParameters params = new DeathParameters();
        params.event=event;
        params.cancelledEarly=false;
        params.vanillaBeheadEvent=null;
        params.vanillaBehavior = VanillDropBehavior.IGNORE;
        params.skullType=null;
        params.droprate=-1;
        params.lootingrate=1;
        params.slimemodifier=1.0;
        params.chargedcreeperModifier=1.0;
        params.killer = getKillerEntity(event);
        //---
        LivingEntity victim = event.getEntity();
        params.isPlayerDeath = victim instanceof Player;
        
        // vanilla head event
        SkullType legacySkullType = SkullType.getFromEntity(victim);
        final List<ItemStack> droppedVanillaHeads = new ArrayList<>();
        boolean hasDroppedVanillaHead = false;
        if(legacySkullType!=null && legacySkullType.canDropInVanilla){
            for(ItemStack stack : event.getDrops()){//I would use a stream, but in Java 7 it converts to List<Object> without an easy way to convert.
                if(isVanillaHead.test(stack)) droppedVanillaHeads.add(stack);
            }
            hasDroppedVanillaHead = droppedVanillaHeads.size() > 0;
        }
        if(hasDroppedVanillaHead){
            params.vanillaBeheadEvent = new VanillaLivingEntityDropHeadEvent(event,victim, params.killer, droppedVanillaHeads);
        }
        //end vanilla head event

        if(plugin.getConfig().getBoolean("trackspawnermobs")){
            if(isSpawnerMob(event.getEntity())){
                VanillDropBehavior spawnerMobBehavior = VanillDropBehavior.fromString( plugin.configFile.getString("spawnermobbehavior") );
                params.vanillaBehavior = params.vanillaBehavior.apply(spawnerMobBehavior);
                if(!params.vanillaBehavior.allowsPhBehavior()) return params.cancel();
            }
        }

        VanillDropBehavior chargedcreeperBehavior=VanillDropBehavior.IGNORE;
        if (params.killer != null) {
            
            if(params.killer instanceof Creeper && !(victim instanceof Player)){
                if(((Creeper) params.killer).isPowered()){
                    chargedcreeperBehavior = VanillDropBehavior.fromString( plugin.configFile.getString("chargedcreeperbehavior") );
                    params.chargedcreeperModifier = plugin.configFile.getDouble("chargedcreepermodifier");
                    params.vanillaBehavior = params.vanillaBehavior.apply(chargedcreeperBehavior);
                    if(!params.vanillaBehavior.allowsPhBehavior()) return params.cancel();
                        
                }
            }
            
            ItemStack weapon = Compatibility.getProvider().getItemInMainHand(params.killer);//killer.getEquipment().getItemInMainHand();
            if(weapon!=null){
                if(plugin.configFile.getBoolean("requireitem")){
                    String weaponType = weapon.getType().name().toLowerCase();
                    if(!plugin.configFile.getStringList("requireditems").contains(weaponType)) return params.cancel();
                }
                params.lootingrate = 1 + (plugin.configFile.getDouble("lootingrate") * weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS));
            }
        }

        params.skullType = SkullConverter.skullTypeFromEntity(victim);
        if (params.skullType == null) {
            return params.cancel();
        }
        String mobDropConfig = params.skullType.getConfigName();
        params.droprate = plugin.configFile.getDouble(mobDropConfig);
        if (params.droprate < 0) {
            return params.cancel();
        }
        switch (params.skullType) {
            case PLAYER:
                params.isPlayerDeath=true;
                if (plugin.configFile.getBoolean("nerfdeathspam")) {
                    if (deathSpamPreventer.recordEvent(event).isSpam()) {
                        return params.cancel();
                    }
                }
                break;
            case WITHER_SKELETON: //note: Wither Skeletons are currently the only mob where a player can directly cause their death...
                params.isPlayerDeath=false;
                VanillDropBehavior witherskeletonbehavior = VanillDropBehavior.fromString( plugin.getConfig().getString("witherskeletonbehavior") );
                params.vanillaBehavior = params.vanillaBehavior.apply(witherskeletonbehavior);
                if(!params.vanillaBehavior.allowsPhBehavior()) return params.cancel();
                break;
            case SLIME:
            case MAGMA_CUBE:
                params.isPlayerDeath=false;
                Entity entity = event.getEntity();
                if(entity instanceof Slime){
                    int slimeSize = ((Slime) entity).getSize();// 1, 2, 3, 4  (1,2,4 natual with 1 the smallest)
                    params.slimemodifier=plugin.configFile.getDouble("slimemodifier."+slimeSize);
                }
                break;
            default:
                params.isPlayerDeath=false;
                break;
        }
        return params.finish();
    }
    
    
    
    /**
     * Event handler for entity deaths.
     * <p>
     * Used to determine when heads should be dropped.
     *
     * @param event the event received
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled=true)
    public void onEntityDeath(EntityDeathEvent event) {
        DeathParameters params = getDeathParameters(event);
        //System.out.println(params);//TODO: debug
        boolean hasDroppedVanillaHead = params.vanillaBeheadEvent!=null;
        boolean doVanillaBehavior = params.vanillaBehavior.allowsVanillaBehavior();
        boolean doPHbehavior = (!params.cancelledEarly) && params.vanillaBehavior.allowsPhBehavior();
        //System.out.println("hasDroppedVanillaHead,doVanillaBehavior,doPHbehavior,allowsPHBehavior "+hasDroppedVanillaHead+" "+doVanillaBehavior+" "+doPHbehavior+" "+params.vanillaBehavior.allowsPhBehavior());//TODO: debug
        
        if(hasDroppedVanillaHead){
            if(doVanillaBehavior){
                //send vanilla behead event
                plugin.getServer().getPluginManager().callEvent(params.vanillaBeheadEvent);
            }
            if(!doVanillaBehavior || params.vanillaBeheadEvent.isCancelled()){
                //remove vanilla drops
                event.getDrops().removeIf(isVanillaHead);
            }
        }
        if(doPHbehavior){
            if(params.isPlayerDeath){
                PlayerDeathHelper(event, params.killer, params.skullType, params.droprate, params.lootingrate,params.chargedcreeperModifier);
            }else{
                MobDeathHelper(event, params.killer, params.skullType, params.droprate, params.lootingrate, params.slimemodifier, params.chargedcreeperModifier);
            }
        }
        
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled=true)
    public void onEntitySpawn(CreatureSpawnEvent event){
        if(!plugin.getConfig().getBoolean("trackspawnermobs")) return;
        LivingEntity entity = event.getEntity();
        boolean persistenceSupported = Compatibility.getProvider().supportsEntityTagType(true);
        Compatibility.getProvider().setEntityTag(entity, plugin, "phspawnreason", event.getSpawnReason().name().toUpperCase(), persistenceSupported);
    }
    
    private CreatureSpawnEvent.SpawnReason getMobSpawnReason(Entity entity){
        boolean persistenceSupported = Compatibility.getProvider().supportsEntityTagType(true);
        String reason = Compatibility.getProvider().getEntityTag(entity, plugin, "phspawnreason", persistenceSupported);
        try{
            return CreatureSpawnEvent.SpawnReason.valueOf(reason);
        }catch(Exception e){
            return null;
        }
    }
    
    private boolean isSpawnerMob(Entity entity){
        return getMobSpawnReason(entity)==CreatureSpawnEvent.SpawnReason.SPAWNER;
    }
    
    
    /**
     * Event handler for entity deaths.
     * <p>
     * Used to determine when heads should be dropped.
     *
     * @param event the event received
     */
    //@EventHandler(priority = EventPriority.HIGH, ignoreCancelled=true)
    private void onEntityDeath_UNUSED(EntityDeathEvent event) {
        LivingEntity victim = event.getEntity();
        LivingEntity killer = getKillerEntity(event);
        
        // vanilla head event
        SkullType legacySkullType = SkullType.getFromEntity(victim);
        final List<ItemStack> droppedVanillaHeads = new ArrayList<>();
        boolean hasDroppedVanillaHead = false;
        if(legacySkullType!=null && legacySkullType.canDropInVanilla){
            for(ItemStack stack : event.getDrops()){//I would use a stream, but in Java 7 it converts to List<Object> without an easy way to convert.
                if(isVanillaHead.test(stack)) droppedVanillaHeads.add(stack);
            }
            hasDroppedVanillaHead = droppedVanillaHeads.size() > 0;
        }
        if(hasDroppedVanillaHead){
            VanillaLivingEntityDropHeadEvent vanillaBeheadEvent = new VanillaLivingEntityDropHeadEvent(event,victim, killer, droppedVanillaHeads);
            plugin.getServer().getPluginManager().callEvent(vanillaBeheadEvent);
        }
        //end vanilla head event
        
        double lootingrate = 1;

        String chargedcreeperBehavior="ignore";
        Double chargedcreeperModifier=1.0; //not implemented yet
        if (killer != null) {
            
            if(killer instanceof Creeper && !(victim instanceof Player)){
                if(((Creeper) killer).isPowered()){
                    chargedcreeperBehavior = plugin.configFile.getString("chargedcreeperbehavior");
                    chargedcreeperModifier = plugin.configFile.getDouble("chargedcreepermodifier");
                    
                    if(chargedcreeperBehavior.equals("block") || chargedcreeperBehavior.equals("replace"))
                        event.getDrops().removeIf(isVanillaHead);
                    if(chargedcreeperBehavior.equals("block") || chargedcreeperBehavior.equals("vanilla"))
                        return;
                }
            }
            
            ItemStack weapon = Compatibility.getProvider().getItemInMainHand(killer);//killer.getEquipment().getItemInMainHand();
            if(weapon!=null){
                if(plugin.configFile.getBoolean("requireitem")){
                    String weaponType = weapon.getType().name().toLowerCase();
                    if(!plugin.configFile.getStringList("requireditems").contains(weaponType)) return;
                }
                lootingrate = 1 + (plugin.configFile.getDouble("lootingrate") * weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS));
            }
        }

        TexturedSkullType skullType = SkullConverter.skullTypeFromEntity(victim);
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
                PlayerDeathHelper(event, killer, skullType, droprate, lootingrate,chargedcreeperModifier);
                break;
            case WITHER_SKELETON: //note: Wither Skeletons are currently the only mob where a player can directly cause their death...
                String witherskeletonbehavior = plugin.getConfig().getString("witherskeletonbehavior");
                
                if(witherskeletonbehavior.equals("block") || witherskeletonbehavior.equals("replace")) 
                    if(hasDroppedVanillaHead) event.getDrops().removeIf(isVanillaHead);
                if(witherskeletonbehavior.equals("block") || witherskeletonbehavior.equals("vanilla"))
                    return;
                
                MobDeathHelper(event, killer, skullType, droprate, lootingrate, 1.0, chargedcreeperModifier);
                break;
            case SLIME:
            case MAGMA_CUBE:
                Double slimemodifier=1.0;
                Entity entity = event.getEntity();
                if(entity instanceof Slime){
                    int slimeSize = ((Slime) entity).getSize();// 1, 2, 3, 4  (1,2,4 natual with 1 the smallest)
                    slimemodifier=plugin.configFile.getDouble("slimemodifier."+slimeSize);
                }
                MobDeathHelper(event, killer, skullType, droprate, lootingrate, slimemodifier,chargedcreeperModifier);
                break;
            default:
                MobDeathHelper(event, killer, skullType, droprate, lootingrate, 1.0,chargedcreeperModifier);
                break;
        }
    }

    private void PlayerDeathHelper(EntityDeathEvent event, LivingEntity killerEntity, TexturedSkullType type, Double droprateOriginal, Double lootingModifier, Double chargedcreeperModifier) {
        Double droprate = droprateOriginal * lootingModifier * chargedcreeperModifier;
        Double dropchanceRand = prng.nextDouble();
        Double dropchance = dropchanceRand;
        Player player = (Player) event.getEntity();
        Player killer = killerEntity instanceof Player ? (Player)killerEntity : event.getEntity().getKiller();

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

        HeadRollEvent rollEvent = new HeadRollEvent(killer, player, killerAlwaysBeheads, lootingModifier, 1.0, chargedcreeperModifier, dropchanceRand, dropchance, droprateOriginal, droprate, headDropSuccess);
        plugin.getServer().getPluginManager().callEvent(rollEvent);
        if (!rollEvent.succeeded()) {
            return;//allow plugins a chance to modify the success
        }

        ItemStack drop = plugin.api.getHeadDrop(player);

        PlayerDropHeadEvent dropHeadEvent = new PlayerDropHeadEvent(event, player, killerEntity, drop);
        plugin.getServer().getPluginManager().callEvent(dropHeadEvent);
        if (dropHeadEvent.isCancelled()) {
            return;
        }
        drop=dropHeadEvent.getDrop();

        //drop item naturally if the drops will be modified by another plugin or gamerule.
        if(drop!=null){
            if (plugin.configFile.getBoolean("antideathchest") || Compatibility.getProvider().getKeepInventory(player.getWorld())) {
                Location location = player.getLocation();
                location.getWorld().dropItemNaturally(location, drop);
            } else {
                event.getDrops().add(drop);
            }
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

    private void MobDeathHelper(EntityDeathEvent event, LivingEntity killerEntity, TexturedSkullType type, Double droprateOriginal, Double lootingModifier, Double slimeModifier, Double chargedcreeperModifier) {
        Double droprate = droprateOriginal * lootingModifier * slimeModifier * chargedcreeperModifier;
        Double dropchanceRand = prng.nextDouble();
        Double dropchance = dropchanceRand;
        Entity entity = event.getEntity();
        Player killer = killerEntity instanceof Player ? (Player)killerEntity : event.getEntity().getKiller();
        
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

        HeadRollEvent rollEvent = new HeadRollEvent(killer, event.getEntity(), killerAlwaysBeheads, lootingModifier, slimeModifier, chargedcreeperModifier, dropchanceRand, dropchance, droprateOriginal, droprate, headDropSuccess);
        plugin.getServer().getPluginManager().callEvent(rollEvent);
        if (!rollEvent.succeeded()) {
            return;//allow plugins a chance to modify the success
        }

        ItemStack drop = plugin.api.getHeadDrop(entity);

        MobDropHeadEvent dropHeadEvent = new MobDropHeadEvent(event, event.getEntity(),killerEntity, drop);
        plugin.getServer().getPluginManager().callEvent(dropHeadEvent);
        if (dropHeadEvent.isCancelled()) {
            return;
        }
        drop=dropHeadEvent.getDrop();
        
        
        final ItemStack finalDrop = drop;//the inner-class requires a final object;
        if(finalDrop!=null){
            if(type==TexturedSkullType.WITHER && plugin.configFile.getBoolean("delaywitherdrop")){
                int delay = plugin.configFile.getInt("delaywitherdropms");
                long ticks =  delay / MS_PER_TICK;
                final Location location = event.getEntity().getLocation();
                plugin.scheduleSync(new Runnable(){
                    @Override
                    public void run(){
                        //System.out.println(" delayed head drop running");
                        location.getWorld().dropItemNaturally(location, finalDrop);
                    }
                },ticks);
                //System.out.println("scheduled head drop for "+ticks+" ticks");
            }else if (plugin.configFile.getBoolean("antideathchest")) {
                Location location = event.getEntity().getLocation();
                location.getWorld().dropItemNaturally(location, finalDrop);
            } else {
                event.getDrops().add(finalDrop);
            }
        }
        
        //broadcast message about the beheading.
        if (plugin.configFile.getBoolean("broadcastmob") && killer!=null) { //mob-on-mob broadcasts would be extremely annoying!
            String entityName = entity.getCustomName();
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
            if(CompatiblePlugins.heads.getExternalHeadHandling(state)==HeadModificationHandling.NO_INTERACTION) return; //TODO: redundant code - SkullConverter checks custom & external heads now
            TexturedSkullType skullType = SkullConverter.skullTypeFromBlockState(state,true,true);
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
                        if(Compatibility.getProvider().isCustomHead(skullState)) return;//TODO: redundant code - SkullConverter checks custom & external heads now  - OLD Comment: this is an unsupported custom-texture head. don't print anything.
                        if (skullState.hasOwner()) {
                            String owner = Compatibility.getProvider().getOwner(skullState);//SkullConverter.getSkullOwner(skullState);
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
        if(CompatiblePlugins.heads.getExternalHeadHandling(stack)==HeadModificationHandling.NO_INTERACTION) return;//TODO: redundant code - SkullConverter checks custom & external heads now
        boolean fixDroppedHeads = plugin.configFile.getBoolean("fixdroppedheads");
        if (!fixDroppedHeads) {
            return;
        }
        TexturedSkullType skullType = SkullConverter.skullTypeFromItemStack(stack,true,true);
        if (skullType == null) {
            return;
        }
        onHeadItemSpawn(event, stack, skullType);
        
    }
    private void onHeadItemSpawn(ItemSpawnEvent event, ItemStack stack, TexturedSkullType skullType){
        boolean restoreprofile = plugin.configFile.getBoolean("restoreprofile");
        boolean isSkinnable = Compatibility.getProvider().isPlayerhead(stack);
        CompatibleProfile savedProfile = null;
        if(isSkinnable && restoreprofile){
            SkullMeta skull = (SkullMeta) stack.getItemMeta();
            savedProfile = Compatibility.getProvider().getCompatibleProfile(skull);
        }
        
        ItemStack newstack = null;
        boolean addLore = plugin.configFile.getBoolean("addlore");
        switch (skullType) {
            case PLAYER:
                SkullMeta skull = (SkullMeta) stack.getItemMeta();
                if(Compatibility.getProvider().isCustomHead(skull)) return;//TODO: redundant code - SkullConverter checks custom & external heads now
                String owner = Compatibility.getProvider().getOwner(skull);//SkullConverter.getSkullOwner(skull);
                newstack = SkullManager.PlayerSkull(owner, stack.getAmount(), addLore, savedProfile); //override with the profile if possible instead of the username
                break;
            default:
                newstack = createConvertedMobhead(skullType, isSkinnable, addLore, stack.getAmount(), true, savedProfile);    
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

    @Nullable
    private ItemStack createConvertedMobhead(TexturedSkullType skullType, boolean isSourceSkinnable, boolean addLore, int quantity, boolean avoidVanillaReplacement, CompatibleProfile oldProfile) {
        boolean dropvanillaheads = plugin.configFile.getBoolean("dropvanillaheads");
        boolean convertvanillaheads = plugin.configFile.getBoolean("convertvanillaheads");

        //if the head is a skinned playerhead and usevanillaskull is set, then breaking it would convert it to a vanilla head
        //if the head is a vanilla skull/head and usevanillaskull is unset, then breaking would convert it to a skinned head
        boolean conversionCanHappen = canConversionHappen(dropvanillaheads, isSourceSkinnable);
        if (conversionCanHappen && !convertvanillaheads) {
            dropvanillaheads = !dropvanillaheads;//change the drop to the state that avoids converting it.
        }
        if(avoidVanillaReplacement && (!isSourceSkinnable && dropvanillaheads)) return null;//if the head is not skinnable (and so, vanilla) and we want the vanilla head - do not drop a replacement.
        return SkullManager.MobSkull(skullType, quantity, dropvanillaheads, addLore, oldProfile);
    }

    //drop a head based on a block being broken in some fashion
    //NOTE: the blockbreak handler expects this to unconditionally drop the item unless the new event is cancelled.
    private BlockDropResult blockDrop(BlockEvent event, Block block, BlockState state, CompatibleProfile oldProfile) {
        if(CompatiblePlugins.heads.getExternalHeadHandling(state)==HeadModificationHandling.NO_INTERACTION) return BlockDropResult.FAILED_BLOCKED_HEAD;//TODO: redundant code - SkullConverter checks custom & external heads now
        TexturedSkullType skullType = SkullConverter.skullTypeFromBlockState(state,true,true);
        if(skullType==null) return BlockDropResult.FAILED_BLOCKED_HEAD;
        Location location = block.getLocation();
        ItemStack item = null;
        boolean addLore = plugin.configFile.getBoolean("addlore");
        switch (skullType) {
            case PLAYER:
                Skull skull = (Skull) block.getState();
                if(Compatibility.getProvider().isCustomHead(skull)) return BlockDropResult.FAILED_CUSTOM_HEAD;//TODO: redundant code - SkullConverter checks custom & external heads now
                String owner = Compatibility.getProvider().getOwner(skull);//SkullConverter.getSkullOwner(skull);
                item = SkullManager.PlayerSkull(owner, addLore);
                break;
            default:
                boolean blockIsSkinnable = Compatibility.getProvider().isPlayerhead(block.getState());
                item = createConvertedMobhead(skullType, blockIsSkinnable, addLore, Config.defaultStackSize, true, oldProfile);
                if(item == null) return BlockDropResult.FAILED_DEFERRED_TO_VANILLA;
                break;
        }
        block.setType(Material.AIR);
        BlockDropHeadEvent eventDropHead = new BlockDropHeadEvent(block, item);
        plugin.getServer().getPluginManager().callEvent(eventDropHead);
        if (eventDropHead.isCancelled()) {
            return BlockDropResult.FAILED_EVENT_CANCELLED;
        }
        item=eventDropHead.getDrop();
        if(item!=null) location.getWorld().dropItemNaturally(location, item);
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
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event instanceof SimulatedBlockBreakEvent) {
            return;
        }
        
        boolean fixbrokenheads = plugin.configFile.getBoolean("fixbrokenheads");
        if(!fixbrokenheads){
            return;
        }
        
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if(CompatiblePlugins.heads.getExternalHeadHandling(block.getState())==HeadModificationHandling.NO_INTERACTION) return;//TODO: redundant code - SkullConverter checks custom & external heads now
        if (player.getGameMode() != GameMode.CREATIVE) {
            BlockState state = block.getState();
            TexturedSkullType skullType = SkullConverter.skullTypeFromBlockState(state,true,true);
            if (skullType != null) {

                boolean canBreak = true;
                boolean pretestblockbreak = plugin.getConfig().getBoolean("pretestblockbreak");
                if (pretestblockbreak && CompatiblePlugins.isReady()) {
                    canBreak = CompatiblePlugins.testBlockBreak(block, player);
                }

                if (!canBreak) {
                    event.setCancelled(true);
                    return;
                } else {
                    onHeadBreak(event,block,state,skullType);
                }
            }

        }
    }
    
    private void onHeadBreak(BlockBreakEvent event, Block block, BlockState state, TexturedSkullType skullType){
        boolean restoreprofile = plugin.configFile.getBoolean("restoreprofile");
        boolean isSkinnable = Compatibility.getProvider().isPlayerhead(state);
        CompatibleProfile savedProfile = null;
        if(isSkinnable && restoreprofile){
            Skull skull = (Skull) state;
            savedProfile = Compatibility.getProvider().getCompatibleProfile(skull);
        }
        
        event.setCancelled(true);
        BlockDropResult result = blockDrop(event, block, state, savedProfile);
        
        if(result.isFailure && !result.eventCancelled){//failed BUT the drop wasn't cancelled!
            event.setCancelled(false);//uncancel the event if we can't drop it accurately - attempted fix for issue crashdemons/PlayerHeads#12
        }
    }

    /**
     * Event handler for player server join events
     * <p>
     * Used to send updater information to appropriate players on join.
     *
     * @param event the event received
     */
    @EventHandler(ignoreCancelled=true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("playerheads.update") && plugin.getUpdateReady()) {
            Formatter.formatMsg(player, Lang.UPDATE1, plugin.getUpdateName());
            Formatter.formatMsg(player, Lang.UPDATE3, "http://curse.com/bukkit-plugins/minecraft/" + Config.updateSlug);
        }
    }
}
