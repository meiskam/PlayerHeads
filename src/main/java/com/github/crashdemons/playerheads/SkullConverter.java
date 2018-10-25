
package com.github.crashdemons.playerheads;

import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.shininet.bukkit.playerheads.LegacySkullType;

/**
 * Abstract class defining methods for converting between entities, custom skullState type, and legacy username-skulls, etc.
 * @author crashdemons
 */
public final class SkullConverter {
    
    private SkullConverter(){}
    
    /**
     * Get the droprate config name (key) for the given skulltype.
     * @param skullType the TexturedSkullType
     * @return A string containing the config entry name (key) for the skulltype
     * @see TexturedSkullType#getConfigName() 
     * @deprecated use TexturedSkullType.getConfigName() instead
     */
    @Deprecated
    public static String dropConfigFromSkullType(TexturedSkullType skullType){
        return skullType.getConfigName();
    }
    
    /**
     * Convert an entity type to a TexturedSkullType.  Most mobs (and the player) have a 1:1 mapping.
     * 
     * Note: At the time of writing in 1.13.1 all entities with isAlive() have associated TexturedSkullType's with the exception of GIANT and ARMOR_STAND.
     * Not all entities will have an associated skulltype, not all living entities will have an associated skulltype.
     * 
     * @param entityType The type of entity to get a skull for
     * @return The associated TexturedSkullType, if one exists. Otherwise, null.
     */
    public static TexturedSkullType skullTypeFromEntityType(EntityType entityType){
        String entityName = entityType.name().toUpperCase();
        try{
            return TexturedSkullType.valueOf(entityName);
        }catch(IllegalArgumentException e){
            return null;
        }
    }
    
    /**
     * Checks if the material provided is a player-head type of any description.
     * 
     * Checks whether the material is a Player Head or a Player Wall-Head.
     * @param mat The material to check.
     * @return true: the material is a playerhead. false: the material is not a playerhead.
     */
    public static boolean isPlayerHead(Material mat){
        return (mat==Material.PLAYER_HEAD || mat==Material.PLAYER_WALL_HEAD);
    }
    
    /**
     * Gets the owner username from a playerhead.
     * @param skullMeta ItemMeta for a playerhead item
     * @return the username of the head's owner
     */
    public static String getSkullOwner(SkullMeta skullMeta){
        String owner=null;
        OfflinePlayer op = skullMeta.getOwningPlayer();
        if(op!=null) owner=op.getName();
        if(owner==null) owner=skullMeta.getOwner();
        return owner;
    }
    /**
     * Gets the owner username from a playerhead.
     * @param skullBlockState BlockState for a playerhead block
     * @return the username of the head's owner
     */
    public static String getSkullOwner(Skull skullBlockState){
        String owner=null;
        OfflinePlayer op = skullBlockState.getOwningPlayer();
        if(op!=null) owner=op.getName();
        if(owner==null) owner=skullBlockState.getOwner();
        return owner;
    }
    
    /**
     * Attempts to determine a TexturedSkullType from an itemstack's information.
     * 
     * This first attempts to guess the skulltype from the stacks's Material (whether it is a player or vanilla head drop).
     * If it is a player-head, then it checks whether the associated UUID matches a TexturedSkullType entry.
     * 
     * 
     * @param stack The stack to determine the skulltype of.
     * @return <ul><li>A TexturedSkullType associated with the mob (if material or UUID matched),</li>
     *         <li>null (if the material is unsupported)</li>
     *         <li>TexturedSkullType.PLAYER (if a playerhead UUID was not associated with any mob)</li></ul>
     */
    public static TexturedSkullType skullTypeFromItemStack(ItemStack stack){
        TexturedSkullType type = TexturedSkullType.get(stack.getType());//guess skullState by material
        if(type==null){
            //System.out.println("Material not found "+state.getType().name());
            return null;
        }
        if(type.hasDedicatedItem() && type!=TexturedSkullType.PLAYER) return type;//if it's not a player then it's a dedicated skullState item reserved for the mob
        //if it's a playerhead, then we need to resolve further
        SkullMeta skullState = (SkullMeta) stack.getItemMeta();
        OfflinePlayer op =skullState.getOwningPlayer();
        if(op==null) return TexturedSkullType.PLAYER;
        UUID owner = op.getUniqueId();
        if(owner==null) return TexturedSkullType.PLAYER;
        TexturedSkullType match = TexturedSkullType.get(owner);//check if the UUID matches any in our textured skullState list
        if(match==null) return TexturedSkullType.PLAYER;
        return match;//if match was not null
    }
    
    /**
     * Attempts to determine a TexturedSkullType from a itemstack's information, with support for legacy username-based mobheads.
     * 
     * This method first checks skullTypeFromItemStack() for skulltype.
     * If the skulltype returned is TexturedSkullType.PLAYER (unknown playerhead), 
     * then this method attempts to associate the head's username with a LegacySkullType and upgrade it to the respective TexturedSkullType.
     * 
     * @param stack The stack to determine the skulltype of.
     * @return <ul><li>A TexturedSkullType associated with the mob (if material or UUID matched),</li>
     *         <li>null (if the material is unsupported)</li>
     *         <li>TexturedSkullType.PLAYER (if a playerhead UUID and username was not associated with any mob)</li></ul>
     * @see #skullTypeFromItemStack(org.bukkit.inventory.ItemStack) 
     * @see org.shininet.bukkit.playerheads.LegacySkullType
     * @deprecated Legacy username-based mobheads are supported for backwards-compatibility reasons but are deprecated in the long term, skullTypeFromBlockState should be used if legacy support is not needed.
     */
    @Deprecated
    public static TexturedSkullType skullTypeFromItemStackLegacy(ItemStack stack){//with legacy name matching support
        TexturedSkullType type = skullTypeFromItemStack(stack);
        if(type==null || type!=TexturedSkullType.PLAYER) return type;//don't really need to check null here, but it's more explicit this way.
        //now we're checking legacy player skulls
        Material mat = stack.getType();
        if(mat!=Material.PLAYER_HEAD && mat!=Material.PLAYER_WALL_HEAD) return null;
        SkullMeta skullState = (SkullMeta) stack.getItemMeta();
        String owner=getSkullOwner(skullState);
        if(owner==null) return TexturedSkullType.PLAYER;//we cannot resolve an owner name for this playerhead, so it can only be considered a Player
        
        LegacySkullType oldtype = LegacySkullType.get(owner);
        if(oldtype==null) return TexturedSkullType.PLAYER;//we can't resolve a legacy type for this playerhead so...
        
        return upgradeSkullTypeLegacy(oldtype);
        
    }
    
    /**
     * Attempts to determine a TexturedSkullType from a block's blockstate information.
     * 
     * This first attempts to guess the skulltype from the block's Material (whether it is a player or vanilla head drop).
     * If it is a player-head, then it checks whether the associated UUID matches a TexturedSkullType entry.
     * 
     * 
     * @param state The state associated with a block to determine the skulltype of.
     * @return <ul><li>A TexturedSkullType associated with the mob (if material or UUID matched),</li>
     *         <li>null (if the material is unsupported)</li>
     *         <li>TexturedSkullType.PLAYER (if a playerhead UUID was not associated with any mob)</li></ul>
     */
    public static TexturedSkullType skullTypeFromBlockState(BlockState state){
        TexturedSkullType type = TexturedSkullType.get(state.getType());//guess skullState by material
        if(type==null){
            //System.out.println("Material not found "+state.getType().name());
            return null;
        }
        if(type.hasDedicatedItem() && type!=TexturedSkullType.PLAYER) return type;//if it's not a player then it's a dedicated skullState item reserved for the mob
        //if it's a playerhead, then we need to resolve further
        Skull skullState = (Skull) state;
        OfflinePlayer op =skullState.getOwningPlayer();
        if(op==null) return TexturedSkullType.PLAYER;
        UUID owner = op.getUniqueId();
        if(owner==null) return TexturedSkullType.PLAYER;
        TexturedSkullType match = TexturedSkullType.get(owner);//check if the UUID matches any in our textured skullState list
        if(match==null) return TexturedSkullType.PLAYER;
        return match;//if match was not null
    }
    
    /**
     * Attempts to determine a TexturedSkullType from a block's blockstate information, with support for legacy username-based mobheads.
     * 
     * This method first checks skullTypeFromBlockState() for skulltype.
     * If the skulltype returned is TexturedSkullType.PLAYER (unknown playerhead), 
     * then this method attempts to associate the head's username with a LegacySkullType and upgrade it to the respective TexturedSkullType.
     * 
     * @param state The state associated with a block to determine the skulltype of.
     * @return <ul><li>A TexturedSkullType associated with the mob (if material or UUID matched),</li>
     *         <li>null (if the material is unsupported)</li>
     *         <li>TexturedSkullType.PLAYER (if a playerhead UUID and username was not associated with any mob)</li></ul>
     * @see #skullTypeFromBlockState(org.bukkit.block.BlockState) 
     * @see org.shininet.bukkit.playerheads.LegacySkullType
     * @deprecated Legacy username-based mobheads are supported for backwards-compatibility reasons but are deprecated in the long term, skullTypeFromBlockState should be used if legacy support is not needed.
     */
    @Deprecated
    public static TexturedSkullType skullTypeFromBlockStateLegacy(BlockState state){//with legacy name matching support
        TexturedSkullType type = skullTypeFromBlockState(state);
        if(type==null || type!=TexturedSkullType.PLAYER) return type;//don't really need to check null here, but it's more explicit this way.
        //now we're checking legacy player skulls
        Material mat = state.getType();
        if(mat!=Material.PLAYER_HEAD && mat!=Material.PLAYER_WALL_HEAD) return null;
        Skull skullState = (Skull) state;
        String owner=getSkullOwner(skullState);
        if(owner==null) return TexturedSkullType.PLAYER;//we cannot resolve an owner name for this playerhead, so it can only be considered a Player
        
        LegacySkullType oldtype = LegacySkullType.get(owner);
        if(oldtype==null) return TexturedSkullType.PLAYER;//we can't resolve a legacy type for this playerhead so...
        
        return upgradeSkullTypeLegacy(oldtype);
        
    }
    
    /**
     * Attempts to convert a LegacySkullType (username-based mobhead) to TexturedSkullType (texture-based mobhead).
     * 
     * Simple enum name conversion is used in this method.
     * 
     * Note: since 3.x changes making CustomSkullType (now LegacySkullType) uniform with bukkit's EntityType naming scheme, 
     * all enum names in LegacySkullType should be present in TexturedSkullType.
     * 
     * @param oldType The LegacySkullType to convert
     * @return The associated TexturedSkullType for the mobhead, or TexturedSkullType.PLAYER if no conversion was supported.
     * @see org.shininet.bukkit.playerheads.LegacySkullType
     * @see com.github.crashdemons.playerheads.TexturedSkullType
     * @deprecated Legacy username-based mobheads are supported for backwards-compatibility reasons but are deprecated in the long term.
     */
    @Deprecated
    public static TexturedSkullType upgradeSkullTypeLegacy(LegacySkullType oldType){
        try{
            return TexturedSkullType.valueOf(oldType.name().toUpperCase());
        }catch(IllegalArgumentException e){
            System.out.println("ERROR - Could not upgrade head: "+oldType.name());
            return TexturedSkullType.PLAYER;
        }
    }
    
    /**
     * Attempts to get a bukkit EntityType from the given TexturedSkullType.
     * 
     * Simple enum name conversion is used in this method - TexturedSkullType is intended to have all entries be in the same format as EntityType.
     * Any entry in TexturedSkullType that doesn't exist in EntityType should be considered a bug or breaking changes in the bukkit API between major versions.
     * 
     * 
     * 
     * @param skullType The TexturedSkullType to find the associated entity-type for.
     * @return The EntityType associated with the skulltype if one is found. Otherwise, in case of breaking API changes, returns null.
     */
    public static EntityType entityTypeFromSkullType(TexturedSkullType skullType){
        String skullName = skullType.name().toUpperCase();
        try{
            return EntityType.valueOf(skullName);
        }catch(IllegalArgumentException e){
            return null;
        }
    }


}
