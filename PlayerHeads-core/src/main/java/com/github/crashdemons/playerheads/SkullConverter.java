
package com.github.crashdemons.playerheads;

import com.github.crashdemons.playerheads.compatibility.Compatibility;
import com.github.crashdemons.playerheads.compatibility.CompatibleSkullMaterial;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Abstract class defining methods for converting between entities, custom skullState type, and legacy username-skulls, etc.
 * @author crashdemons
 */
public final class SkullConverter {
    
    
    private SkullConverter(){}
    
    /**
     * Determine the TexturedSkullType best corresponding to an entity.  Most mobs (and the player) have a 1:1 mapping by their name.
     * 
     * Note: At the time of writing in 1.13.1 all entities with isAlive() have associated TexturedSkullType's with the exception of GIANT and ARMOR_STAND.
     * Not all entities will have an associated skulltype, not all living entities will have an associated skulltype.
     * 
     * @param entity The entity to get a skull for
     * @return The associated TexturedSkullType, if one exists. Otherwise, null.
     */
    public static TexturedSkullType skullTypeFromEntity(Entity entity){
        String entityName = Compatibility.getProvider().getCompatibleNameFromEntity(entity);
        try{
            return TexturedSkullType.valueOf(entityName);
        }catch(IllegalArgumentException e){
            return null;
        }
    }
    
    /**
     * Gets the owner player from a playerhead.
     * 
     * If there is no OwningPlayer result, this infers one from head's "profile" field.
     * @param skullMeta ItemMeta for a playerhead item
     * @return the owning player of the skull
     * @deprecated use Compatibility.getProvider().getOwningPlayerExtended(...)
     * @see com.github.crashdemons.playerheads.compatibility.CompatibilityProvider#getOwner(org.bukkit.inventory.meta.SkullMeta) 
     */
    @Deprecated
    public static OfflinePlayer getSkullOwningPlayer(SkullMeta skullMeta){
        return Compatibility.getProvider().getOwningPlayer(skullMeta);
    }
    /**
     * Gets the owner username from a playerhead.
     * 
     * If there is no OwningPlayer result, this infers one from head's "profile" field.
     * @param skullBlockState BlockState for a playerhead block
     * @return the username of the head's owner
     * @deprecated use Compatibility.getProvider().getOwningPlayerExtended(...)
     * @see com.github.crashdemons.playerheads.compatibility.CompatibilityProvider#getOwningPlayer(org.bukkit.block.Skull) 
     */
    @Deprecated
    public static OfflinePlayer getSkullOwningPlayer(Skull skullBlockState){
        return Compatibility.getProvider().getOwningPlayer(skullBlockState);
    }
    
    /**
     * Gets the owner username from a playerhead.
     * @param skullMeta ItemMeta for a playerhead item
     * @return the username of the head's owner
     * @deprecated use Compatibility.getProvider().getOwnerExhaustive(...)
     * @see com.github.crashdemons.playerheads.compatibility.CompatibilityProvider#getOwner(org.bukkit.inventory.meta.SkullMeta) 
     */
    public static String getSkullOwner(SkullMeta skullMeta){
        return Compatibility.getProvider().getOwner(skullMeta);
    }
    /**
     * Gets the owner username from a playerhead.
     * @param skullBlockState BlockState for a playerhead block
     * @return the username of the head's owner
     * @deprecated use Compatibility.getProvider().getOwnerExhaustive(...)
     * @see com.github.crashdemons.playerheads.compatibility.CompatibilityProvider#getOwner(org.bukkit.block.Skull) 
     */
    public static String getSkullOwner(Skull skullBlockState){
        return Compatibility.getProvider().getOwner(skullBlockState);
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
        CompatibleSkullMaterial mat = CompatibleSkullMaterial.get(stack);
        if(mat==null) return null;
        if(!mat.getDetails().isBackedByPlayerhead()) return TexturedSkullType.get(mat);
        SkullMeta skullState = (SkullMeta) stack.getItemMeta();
        OfflinePlayer op =Compatibility.getProvider().getOwningPlayer(skullState);//getSkullOwningPlayer(skullState);
        if(op==null) return TexturedSkullType.PLAYER;
        UUID owner = op.getUniqueId();
        if(owner==null) return TexturedSkullType.PLAYER;
        TexturedSkullType match = TexturedSkullType.get(owner);//check if the UUID matches any in our textured skullState list
        if(match==null) return TexturedSkullType.PLAYER;
        return match;//if match was not null
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
        CompatibleSkullMaterial mat = CompatibleSkullMaterial.get(state);
        if(mat==null) return null;
        if(!mat.getDetails().isBackedByPlayerhead()) return TexturedSkullType.get(mat);
        Skull skullState = (Skull) state;
        OfflinePlayer op =Compatibility.getProvider().getOwningPlayer(skullState);//getSkullOwningPlayer(skullState);
        if(op==null) return TexturedSkullType.PLAYER;
        UUID owner = op.getUniqueId();
        if(owner==null) return TexturedSkullType.PLAYER;
        TexturedSkullType match = TexturedSkullType.get(owner);//check if the UUID matches any in our textured skullState list
        if(match==null) return TexturedSkullType.PLAYER;
        return match;//if match was not null
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
    public static EntityType entityTypeFromSkullType(TexturedSkullType skullType){ //TODO: use new compatibility method instead
        String skullName = skullType.name().toUpperCase();
        return Compatibility.getProvider().getEntityTypeFromTypename(skullName);
    }


}
