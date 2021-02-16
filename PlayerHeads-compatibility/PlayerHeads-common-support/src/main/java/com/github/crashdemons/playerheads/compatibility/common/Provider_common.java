/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.common;

import com.github.crashdemons.playerheads.compatibility.Compatibility;
import com.github.crashdemons.playerheads.compatibility.CompatibilityProvider;
import com.github.crashdemons.playerheads.compatibility.CompatibleProfile;
import com.github.crashdemons.playerheads.compatibility.CompatibleSkullMaterial;
import com.github.crashdemons.playerheads.compatibility.RuntimeReferences;
import com.github.crashdemons.playerheads.compatibility.SkullType;
import com.github.crashdemons.playerheads.compatibility.Version;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import static org.bukkit.Warning.WarningState.value;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;

/**
 * CompatibilityProvider Implementation for 1.8-1.12.2 support.
 *
 * @author crashdemons (crashenator at gmail.com)
 */
@SuppressWarnings("deprecation")
public abstract class Provider_common implements CompatibilityProvider {

    @Override
    public String getOwnerDirect(SkullMeta skullItemMeta) {
        return skullItemMeta.getOwner();
    }

    @Override
    public String getOwnerDirect(Skull skullBlockState) {
        return skullBlockState.getOwner();
    }

    @Override
    public boolean setOwner(SkullMeta skullItemMeta, String owner) {
        return skullItemMeta.setOwner(owner);
    }

    @Override
    public boolean setOwner(Skull skullBlockState, String owner) {
        return skullBlockState.setOwner(owner);
    }

    @Override
    public boolean isHead(ItemStack s) {
        return getSkullType(s) != null;
    }

    @Override
    public boolean isHead(BlockState s) {
        return getSkullType(s) != null;
    }

    @Override
    public boolean isPlayerhead(ItemStack s) {
        return getSkullType(s) == SkullType.PLAYER;
    }

    @Override
    public boolean isPlayerhead(BlockState s) {
        return getSkullType(s) == SkullType.PLAYER;
    }

    @Override
    public boolean isMobhead(ItemStack s) {
        SkullType t = getSkullType(s);
        return (t != null && t != SkullType.PLAYER);
    }

    @Override
    public boolean isMobhead(BlockState s) {
        SkullType t = getSkullType(s);
        return (t != null && t != SkullType.PLAYER);
    }

    @Override
    public String getCompatibleNameFromEntity(Entity e) {
        if(isZombiePigman(e)) return "ZOMBIFIED_PIGLIN";
        if (isLegacyCat(e)) {
            return "CAT";
        }
        return e.getType().name().toUpperCase();
    }

    @Override
    public OfflinePlayer getOfflinePlayerByName(String username) {
        return Bukkit.getOfflinePlayer(username);
    }
    
    //default implementation without version-specific name checking
    @Override
    public EntityType getEntityTypeFromTypename(String typename){
        try{
            return EntityType.valueOf(typename.toUpperCase());
        }catch(Exception e){
            return null;
        }
    }
    
    protected static final String ETYPE_ZOMBIE_PIGMAN_PRE116 = "PIG_ZOMBIE";
    protected static final String ETYPE_ZOMBIE_PIGMAN_POST116 = "ZOMBIFIED_PIGLIN";
    
    protected boolean isZombiePigmanTypename(String typename){
        typename=typename.toUpperCase();
        return typename!=null && (typename.equals(ETYPE_ZOMBIE_PIGMAN_PRE116) || typename.equals(ETYPE_ZOMBIE_PIGMAN_POST116));
    }
    
    
    private EntityType currentZombiePigmanType = null;
    protected EntityType getCurrentZombiePigmanType(){
        if(currentZombiePigmanType==null){
            if(Version.checkAtLeast(1, 16)) currentZombiePigmanType = RuntimeReferences.getEntityTypeByName(ETYPE_ZOMBIE_PIGMAN_POST116);
            else currentZombiePigmanType = RuntimeReferences.getEntityTypeByName(ETYPE_ZOMBIE_PIGMAN_PRE116);
        }
        return currentZombiePigmanType;
    }
    
    
    
    
    protected boolean isZombiePigman(Entity e){
        return (e instanceof PigZombie);
    }

    protected boolean isLegacyCat(Entity e) {
        if (e instanceof Ocelot && e instanceof Tameable) {
            return ((Tameable) e).isTamed();
        }

        return false;
    }
    
    
    
    //-----------5.2.12 providers-----------//
    @Override
    public Optional<Object> getOptionalProfile(ItemMeta skullMeta){
        try{
            return Optional.of(Compatibility.getProvider().getProfile(skullMeta));
        }catch(Exception e){
            return Optional.empty();
        }
    }
    @Override
    public Optional<Object> getOptionalProfile(Skull skullState){
        try{
            return Optional.of(Compatibility.getProvider().getProfile(skullState));
        }catch(Exception e){
            return Optional.empty();
        }
    }
    @Override
    public boolean setOptionalProfile(Skull skullState, Optional<Object> profile){
        if(!profile.isPresent()) return false;
        try{
            return Compatibility.getProvider().setProfile(skullState,profile.get());
        }catch(Exception e){
            return false;
        }
    }
    @Override
    public boolean setOptionalProfile(ItemMeta skullMeta, Optional<Object> profile){
        if(!profile.isPresent()) return false;
        try{
            return Compatibility.getProvider().setProfile(skullMeta,profile.get());
        }catch(Exception e){
            return false;
        }
    }
    
    @Override
    public ItemStack getCompatibleHeadItem(CompatibleSkullMaterial material, int amount){
        return material.getDetails().createItemStack(amount);
    }
    
    
    @Override
    public boolean isCustomHead(String username, UUID id){
        if(id!=null) if( (username==null || username.isEmpty()) ) return true;//custom head would have a valid ID, but no username (no user+no ID = boring steve head / Player head)
        if(username!=null) if(username.contains(":")) return true;//Invalid char for names, but used by a few large plugins (HeadDB, DropHeads)
        return false;
    }
    
    @Override
    public boolean isCustomHead(CompatibleProfile profile){
        if(profile==null) throw new IllegalArgumentException("profile is null");
        return isCustomHead(profile.getName(), profile.getId());
    }
    @Override
    public boolean isCustomHead(Object skull){
        if(skull==null) throw new IllegalArgumentException("skull is null");
        if(skull instanceof Skull || skull instanceof SkullMeta){
            CompatibleProfile profile = Compatibility.getProvider().getCompatibleProfile(skull);
            if(profile==null) return false;
            return isCustomHead(profile);
        }else throw new IllegalArgumentException("skull provided is not of type Skull or SkullMeta");
    }
    
    
    protected Entity getEntityOwningEntity(EntityDamageByEntityEvent event, boolean considertameowners){
        Entity entity = event.getDamager();
        if(entity instanceof Projectile){
            //System.out.println("   damager entity projectile");
            Projectile projectile = (Projectile) entity;
            ProjectileSource shooter = projectile.getShooter();
            if(shooter instanceof Entity){
                entity=(Entity) shooter;
                //if(entity!=null) System.out.println("   arrow shooter: "+entity.getType().name()+" "+entity.getName());
            }
        }else if(entity instanceof Tameable && considertameowners){
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
    
    public LivingEntity getKillerEntity(EntityDeathEvent event, boolean considermobkillers, boolean considertameowners){
        LivingEntity victim = event.getEntity();
        //if(victim!=null) System.out.println("victim: "+victim.getType().name()+" "+victim.getName());
        LivingEntity killer = victim.getKiller();
        //if(killer!=null) System.out.println("original killer: "+killer.getType().name()+" "+killer.getName());
        
        if(killer==null && considermobkillers){
            EntityDamageEvent dmgEvent = event.getEntity().getLastDamageCause();
            if(dmgEvent instanceof EntityDamageByEntityEvent){
                Entity killerEntity = getEntityOwningEntity((EntityDamageByEntityEvent)dmgEvent, considertameowners);
                //if(killerEntity!=null) System.out.println(" parent killer: "+killerEntity.getType().name()+" "+killerEntity.getName());
                if(killerEntity instanceof LivingEntity) killer=(LivingEntity)killerEntity;
                //what if the entity isn't living (eg: arrow?)
            }
        }
        //if(killer!=null) System.out.println(" final killer: "+killer.getType().name()+" "+killer.getName());
        return killer;
    }
    
    protected boolean setTemporaryTag(Entity ent, Plugin plugin, String key, String value){
        if(ent instanceof Metadatable){
            ent.setMetadata(key, new FixedMetadataValue(plugin,value));
            return true;
        }
        return false;
    }
    protected String getTemporaryTag(Entity ent, Plugin plugin, String key){
        if(ent instanceof Metadatable){
            List<MetadataValue> values = ent.getMetadata(key);
            for(MetadataValue value : values){
                Plugin valuePlugin = value.getOwningPlugin();
                if(valuePlugin==plugin || (valuePlugin!=null && valuePlugin.equals(plugin))){
                    return value.asString();
                }
            }
        }
        return null;
    }
    
    protected boolean setPersistentTag(Entity entity, Plugin plugin, String key, String value){
        return false;
    }
    protected String getPersistentTag(Entity entity, Plugin plugin, String key){
        return null;
    }
    
    @Override
    public boolean supportsEntityTagType(boolean persistent){
        return !persistent;
    }
    
    @Override
    public boolean setEntityTag(Entity entity, Plugin plugin, String key, String value, boolean persistent){
        if(persistent) return setPersistentTag(entity,plugin,key,value);
        return setTemporaryTag(entity,plugin,key,value);
    }
    
    @Override
    public String getEntityTag(Entity entity, Plugin plugin, String key, boolean persistent){
        if(persistent) return getPersistentTag(entity,plugin,key);
        return getTemporaryTag(entity,plugin,key);
    }
    
}
