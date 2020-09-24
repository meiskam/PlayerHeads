/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import java.util.Optional;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

/**
 * An interface specifying all of the methods we need for our plugin that require differing Bukkit-specific implementations which we wish to abstract from our plugin code.
 * CompatibilityProviders not only must implement this interface, but in order to be automatically selected by the Compatibility class,
 * they must exist in a specific package name.
 * 
 * Providers (or a class extending them) are expected to exist in the same package as the compatibility library (com.github.crashdemons.playerheads.compatibility)
 * followed by the server type name and major/minor version.  For example: com.github.crashdemons.playerheads.compatibility.craftbukkit_1_16
 * the class in this package must be named "Provider" and must not be abstract.
 * 
 * Secondly, to be automatically selected by the Compatibility class, the server type and version must be listed in the CompatibilitySupport class' VERSIONS map.
 * This is typically accomplished by replacing it in the compatibility-library module when shading in all relevant support classes (ie: the original is excluded from shading).
 * 
 * @author crashdemons (crashenator at gmail.com)
 */
public interface CompatibilityProvider {
    /**
     * Retrieve the server type the provider implements code for.
     * @return the server type string
     */
    public String getType();
    
    /**
     * Retrieve the version string associated with the specific Compatibility Provider implementation.
     * @return the version string
     */
    public String getVersion();
    /**
     * Gets the owning player of a skull as direct as possible from the relevant API - this may not always reliably get the player.
     * 
     * This method may not result in the same information as getOwner, you should check both.
     * @param skullItemMeta the ItemMeta of the skull
     * @return the player owning the skull, or null if none could be retrieved.
     * @deprecated This method does not include extended checking, you probably dont want the direct method.
     */
    @Deprecated
    public OfflinePlayer getOwningPlayerDirect(SkullMeta skullItemMeta);
    /**
     * Gets the owning player of a skull as direct as possible from the relevant API - this may not always reliably get the player.
     * 
     * This method may not result in the same information as getOwner, you should check both.
     * @param skullBlockState the BlockState of the skull
     * @return the player owning the skull, or null if none could be retrieved.
     * @deprecated This method does not include extended checking, you probably dont want the direct method.
     */
    @Deprecated
    public OfflinePlayer getOwningPlayerDirect(Skull skullBlockState);
    
    /**
     * Gets the owning player of a skull, with an additional attempt to derive the player from Profile uuid.
     * 
     * This method may not result in the same information as getOwner, you should check both.
     * This method does not exhaustively attempt to derive players from usernames.
     * @param skullItemMeta the ItemMeta of the skull
     * @return the player owning the skull, or null if none could be retrieved.
     */
    public OfflinePlayer getOwningPlayer(SkullMeta skullItemMeta);
    /**
     * Gets the owning player of a skull, with an additional attempt to derive the player from Profile uuid.
     * 
     * This method may not result in the same information as getOwner, you should check both.
     * This method does not exhaustively attempt to derive players from usernames.
     * @param skullBlockState the BlockState of the skull
     * @return the player owning the skull, or null if none could be retrieved.
     */
    public OfflinePlayer getOwningPlayer(Skull skullBlockState);
    
    
    /**
     * Gets the owner username of a skull as direct as possible from the relevant API - this may not always reliably get the username.
     * 
     * This method may not result in the same information as getOwningPlayerDirect, you should check both.
     * @param skullItemMeta the ItemMeta of the skull
     * @return the owner name
     * @deprecated This method does not include extended checking, you probably dont want the direct method.
     */
    @Deprecated
    public String getOwnerDirect(SkullMeta skullItemMeta);
    /**
     * Gets the owner username of a skull as direct as possible from the relevant API - this may not always reliably get the username.
     * 
     * This method may not result in the same information as getOwningPlayerDirect, you should check both.
     * @param skullBlockState the BlockState of the skull
     * @return the owner name
     * @deprecated This method does not include extended checking, you probably dont want the direct method.
     */
    @Deprecated
    public String getOwnerDirect(Skull skullBlockState);
    /**
     * Gets the owner username of a skull by any means necessary.
     * 
     * Because of differing results between getOwningPlayer().getName() and getOwnerDirect(), it may be necessary to use both or check the profile field, which this method should do.
     * This method must be implemented with the following defined order: getOwningPlayer and getProfilePlayer if necessary to check name, then getOwner to check name.
     * @param skullItemMeta the itemmeta of the skull to check
     * @return the owner name or null if none could be found.
     */
    public String getOwner(SkullMeta skullItemMeta);
    /**
     * Gets the owner username of a skull by any means necessary.
     * 
     * Because of differing results between getOwningPlayer().getName() and getOwnerDirect(), it may be necessary to use both or check the profile field, which this method should do.
     * This method must be implemented with the following defined order: getOwningPlayer and getProfilePlayer if necessary to check name, then getOwner to check name.
     * @param skullBlockState the blockstate of the skull to check
     * @return the owner name or null if none could be found.
     */
    public String getOwner(Skull skullBlockState);
    /**
     * Sets the player owning a skull
     * @param skullItemMeta the ItemMeta of a skull
     * @param op the owning player
     * @return whether the process succeeded
     */
    public boolean setOwningPlayer(SkullMeta skullItemMeta, OfflinePlayer op);

    /**
     * Sets the player owning a skull
     * @param skullBlockState the BlockState of a skull
     * @param op the owning player
     */
    public void    setOwningPlayer(Skull skullBlockState, OfflinePlayer op);
    /**
     * Sets the owner username of a skull
     * @param skullItemMeta the ItemMeta of a skull
     * @param owner the owner username to set
     * @return whether the process succeeded
     */
    public boolean setOwner(SkullMeta skullItemMeta, String owner);
    /**
     * Sets the owner username of a skull
     * @param skullBlockState the BlockState of a skull
     * @param owner the owner username to set
     * @return whether the process succeeded
     */
    public boolean setOwner(Skull skullBlockState, String owner);
    /**
     * Gets the itemstack in the [main] hand of a player
     * @param p the player to check
     * @return The ItemStack if found, or null
     */
    public ItemStack getItemInMainHand(Player p);
    /**
     * Sets the itemstack in the [main] hand of a player
     * @param p the player to change
     * @param s the itemstack to set in their hand
     */
    public void setItemInMainHand(Player p, ItemStack s);
    /**
     * Gets a class describing implementation-specific details about a vanilla skull type
     * @param type the type of vanilla head or skull to check
     * @return the object containing details about the skull type
     * @see SkullType
     */
    public SkullDetails getSkullDetails(SkullType type);
    /**
     * Checks whether the keepinventory gamerule is enabled for a given world
     * @param world the world to check in
     * @return whether the gamerule is enabled
     */
    public boolean getKeepInventory(World world);
    /**
     * Gets the vanilla skulltype best associated with the ItemStack provided.
     * 
     * Note: this method does not perform any username, UUID, or texture checks, so any skull that is not directly supported in the current server (such as a dragon head in 1.8) will return SkullType.PLAYER instead.
     * It only determines what skulltype is associated with the vanilla types available.
     * @param s The itemstack to check
     * @return the skulltype associated with the object.
     */
    public SkullType getSkullType(ItemStack s); 
    /**
     * Gets the vanilla skulltype best associated with the BlockState provided.
     * 
     * Note: this method does not perform any username, UUID, or texture checks, so any skull that is not directly supported in the current server (such as a dragon head in 1.8) will return SkullType.PLAYER instead.
     * It only determines what skulltype is associated with the vanilla types available.
     * @param s The blockstate to check
     * @return the skulltype associated with the object.
     */
    public SkullType getSkullType(BlockState s);
    /**
     * Checks whether the itemstack corresponds a vanilla head or skull of some type.
     * @param s the itemstack to check
     * @return whether the object is a head
     */
    public boolean isHead(ItemStack s);
    /**
     * Checks whether the blockstate corresponds a vanilla head or skull of some type.
     * @param s the blockstate to check
     * @return whether the object is a head
     */
    public boolean isHead(BlockState s);
    /**
     * Checks whether the itemstack corresponds to a vanilla player-head type
     * @param s the itemstack to check
     * @return whether the object is a playerhead
     */
    public boolean isPlayerhead(ItemStack s);
    /**
     * Checks whether the blockstate corresponds to a vanilla player-head type
     * @param s the blockstate to check
     * @return whether the object is a playerhead
     */
    public boolean isPlayerhead(BlockState s);
    /**
     * Checks whether the itemstack corresponds to a vanilla head or skull and is not a player-head type
     * @param s the itemstack to check
     * @return whether the object is a mobhead
     */
    public boolean isMobhead(ItemStack s);
    /**
     * Checks whether the blockstate corresponds to a vanilla head or skull and is not a player-head type
     * @param s the blockstate to check
     * @return whether the object is a mobhead
     */
    public boolean isMobhead(BlockState s);
    /**
     * Gets a forward-portable name of an entity.
     * 
     * This gets the name of the entity as it appears in the EntityType enum of newer bukkit versions, even if the mob is a variant in the current version.
     * This value can be used to correspond entities to their heads.
     * @param e the entity to check
     * @return the portable name of the string.
     * @see org.bukkit.entity.EntityType
     */
    public String getCompatibleNameFromEntity(Entity e); //determine forward-portable name of entity even if they are variants.
    /**
     * Gets an entity type from the Typename of an entity.
     * This exists because EntityType names can change between Spigot-API versions.
     * @param ename the entity-type name requested.
     * @return the corresponding entity-type, or null if it doesn't exist.
     */
    public EntityType getEntityTypeFromTypename(String ename); //determine forward-portable name of entity even if they are variants.
    /**
     * Set a profile field in the supplied item meta using a UUID and Texture string
     * @param headMeta the item meta to apply the profile on
     * @param uuid A UUID to be associated with this profile and texture
     * @param texture The Base64-encoded Texture-URL tags.
     * @return True: the profile was successfully set. False: the profile could not be set.
     */
    public boolean setProfile(ItemMeta headMeta, UUID uuid, String texture);
    /**
     * Set a profile field in the supplied block state using a UUID and Texture string
     * @param headBlockState the block state to apply the profile on
     * @param uuid A UUID to be associated with this profile and texture
     * @param texture The Base64-encoded Texture-URL tags.
     * @return True: the profile was successfully set. False: the profile could not be set.
     */
    public boolean setProfile(Skull headBlockState, UUID uuid, String texture);
    /**
     * Gets a player by their username
     * @param username the username of the player
     * @return the offline-player
     */
    public OfflinePlayer getOfflinePlayerByName(String username);
    
    //----------- 5.0 providers -----------//
    public ItemStack getItemInMainHand(LivingEntity p);
    
    
    //-----------5.2.12 providers-----------//
    /**
     * Gets the Profile object associated with a head, if possible.
     * The return type is offered as an Object to remove reliance on authlib.
     * NOTE: depending on server implementation, the Profile is not guaranteed to be a GameProfile - you should not act on this object directly, but only get/set it.
     * Providers that are not capable of retrieving this should throw an IllegalStateException.
     * @param headMeta the meta of the head item
     * @return the Profile object object for the head, or null.
     * @throws IllegalStateException when the provider does not support GameProfiile access.
     * @deprecated This method should be avoided entirely or used only for acceptable-failure situations because of server support limitations.
     * @since 5.2.12
     */
    @Deprecated
    @Nullable
    public Object getProfile(ItemMeta headMeta) throws IllegalStateException;
    
    /**
     * Gets the Profile object associated with a head, if possible.
     * The return type is offered as an Object to remove reliance on authlib.
     * NOTE: depending on server implementation, the Profile is not guaranteed to be a GameProfile - you should not act on this object directly, but only get/set it.
     * @param headBlockState the blockstate of the head block
     * @return the Profile object object for the head, or null.
     * @throws IllegalStateException when the provider does not support GameProfiile access.
     * @deprecated This method should be avoided entirely or used only for acceptable-failure situations because of server support limitations.
     * @since 5.2.12
     */
    @Deprecated
    @Nullable
    public Object getProfile(Skull headBlockState) throws IllegalStateException;
    
    
    
    /**
     * Sets the Profile object on a head, if possible.
     * Providers that are not capable of retrieving this should throw an IllegalStateException.
     * IllegalArgumentException should be thrown if the input is not null and also not a Profile object type.
     * NOTE: depending on server implementation, the Profile is not guaranteed to be a GameProfile - you should not act on this object directly, but only get/set it.
     * @param headMeta the meta of the head item
     * @param profile the Profile object object to set in the head
     * @return whether setting the profile field succeeded
     * @throws IllegalStateException when the provider does not support GameProfiile access.
     * @throws IllegalArgumentException when the the input profile was not am acceptable Profile object type and not null
     * @deprecated This method should be avoided entirely or used only for acceptable-failure situations because of server support limitations.
     * @since 5.2.12
     */
    @Deprecated
    public boolean setProfile(ItemMeta headMeta, Object profile) throws IllegalStateException, IllegalArgumentException;
    
    /**
     * Sets the Profile object on a head, if possible.
     * Providers that are not capable of retrieving this should throw an IllegalStateException.
     * IllegalArgumentException should be thrown if the input is not null and also not a Profile object type.
     * NOTE: depending on server implementation, the Profile is not guaranteed to be a GameProfile - you should not act on this object directly, but only get/set it.
     * @param headBlockState the blockstate of the head block
     * @param profile the Profile object object to set in the head
     * @return whether setting the profile field succeeded
     * @throws IllegalStateException when the provider does not support GameProfiile access.
     * @throws IllegalArgumentException when the the input profile was not am acceptable Profile object type and not null
     * @deprecated This method should be avoided entirely or used only for acceptable-failure situations because of server support limitations.
     * @since 5.2.12
     */
    @Deprecated
    public boolean setProfile(Skull headBlockState, Object profile) throws IllegalStateException, IllegalArgumentException;
    
    /**
     * Gets the Optional Profile object for a head.
     * This method must return an Optional object which 'is present' if the profile has a retrievable value (including null).
     * Otherwise, the Optional must be 'empty'.
     * @param skullMeta the meta for a head item
     * @return The optional profile object
     */
    public Optional<Object> getOptionalProfile(ItemMeta skullMeta);
    
    /**
     * Gets the Optional Profile object for a head.
     * This method must return an Optional object which 'is present' if the profile has a retrievable value (including null).
     * Otherwise, the Optional must be 'empty'.
     * @param skullState the blockstate for a head item
     * @return The optional profile object
     */
    public Optional<Object> getOptionalProfile(Skull skullState);
    
    /**
     * Sets the Optional Profile object for a head.
     * If the Optional value is 'empty' (not present), then nothing happens.
     * If the Optional value is present, then it is set (even if it is null).
     * @param skullState the blockstate for a head item
     * @param profile the Optional profile object to set
     * @return whether setting the profile succeeded. (nothing happening is considered failure).
     */
    public boolean setOptionalProfile(Skull skullState, Optional<Object> profile);
    
    /**
     * Sets the Optional Profile object for a head.
     * If the Optional value is 'empty' (not present), then nothing happens.
     * If the Optional value is present, then it is set (even if it is null).
     * @param skullMeta the meta for a head item
     * @param profile the Optional profile object to set
     * @return whether setting the profile succeeded. (nothing happening is considered failure).
     */
    public boolean setOptionalProfile(ItemMeta skullMeta, Optional<Object> profile);
    

}
