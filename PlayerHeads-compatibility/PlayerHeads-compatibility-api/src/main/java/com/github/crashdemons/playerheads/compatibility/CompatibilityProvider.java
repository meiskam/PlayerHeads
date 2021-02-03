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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An interface specifying all of the methods we need for our plugin that require differing Bukkit-specific implementations which we wish to abstract from our plugin code.
 * CompatibilityProviders not only must implement this interface, but in order to be automatically selected by the Compatibility class,
 * they must exist in a specific package name.
 * 
 * Providers (or a class extending them) are expected to exist in the same package as the compatibility library (com.github.crashdemons.playerheads.compatibility by default).
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
     * @deprecated Use CompatibleProfile or profile methods instead. This API may be removed in the future.
     */
    @Deprecated
    public Optional<Object> getOptionalProfile(ItemMeta skullMeta);
    
    /**
     * Gets the Optional Profile object for a head.
     * This method must return an Optional object which 'is present' if the profile has a retrievable value (including null).
     * Otherwise, the Optional must be 'empty'.
     * NOTE: depending on server implementation, the Profile is not guaranteed to be a GameProfile or even present - you should not act on this object directly, but only get/set it.
     * @param skullState the blockstate for a head item
     * @return The optional profile object
     * @deprecated Use CompatibleProfile or profile methods instead. This API may be removed in the future.
     */
    @Deprecated
    public Optional<Object> getOptionalProfile(Skull skullState);
    
    /**
     * Sets the Optional Profile object for a head.
     * If the Optional value is 'empty' (not present), then nothing happens.
     * If the Optional value is present, then it is set (even if it is null).
     * NOTE: depending on server implementation, the Profile is not guaranteed to be a GameProfile or even present - you should not act on this object directly, but only get/set it.
     * @param skullState the blockstate for a head item
     * @param profile the Optional profile object to set
     * @return whether setting the profile succeeded. (nothing happening is considered failure).
     * @deprecated Use CompatibleProfile or profile methods instead. This API may be removed in the future.
     */
    @Deprecated
    public boolean setOptionalProfile(Skull skullState, Optional<Object> profile);
    
    /**
     * Sets the Optional Profile object for a head.
     * If the Optional value is 'empty' (not present), then nothing happens.
     * If the Optional value is present, then it is set (even if it is null).
     * NOTE: depending on server implementation, the Profile is not guaranteed to be a GameProfile or even present - you should not act on this object directly, but only get/set it.
     * @param skullMeta the meta for a head item
     * @param profile the Optional profile object to set
     * @return whether setting the profile succeeded. (nothing happening is considered failure).
     * @deprecated Use CompatibleProfile or profile methods instead. This API may be removed in the future.
     */
    @Deprecated
    public boolean setOptionalProfile(ItemMeta skullMeta, Optional<Object> profile);
    
    
    
    /**
     * Sets a profile on a block or item.
     * If the skull parameter is not the proper type, an except may be thrown.
     * @param skull an object of type Skull (BlockState) or SkullMeta
     * @param profile the compatible profile object, or specific implementation child object.
     * @return whether setting a profile succeeded.
     * @throws IllegalArgumentException if the skull parameter was not the specified type
     * @since 5.2.13-SNAPSHOT
     */
    public boolean setCompatibleProfile(Object skull, CompatibleProfile profile) throws IllegalArgumentException;
    /**
     * Gets a profile on a block or item.
     * If the skull parameter is not 
     * @param skull an object of type Skull (BlockState) or SkullMeta
     * @return the profile from the head if it exists, or null
     * @throws IllegalArgumentException if the skull parameter was not the specified type
     * @since 5.2.13-SNAPSHOT
     */
    public CompatibleProfile getCompatibleProfile(Object skull) throws IllegalArgumentException;
    
    /**
     * Create a compatible profile object with the provided parameters.
     * Note: name and id cannot both be null, only one can be null.
     * @param name the owner username of the head (this should not be a custom name - use null instead)
     * @param id A UUID to be associated with this profile and texture (this may be a custom/unique value that you manage - you are strongly recommended to choose a static but randomly-generated ID)
     * @param texture The Base64-encoded Texture-URL tags. (this may be null to set no texture)
     * @return the CompatibleProfile object
     * @throws IllegalArgumentException if both the name and id are null.
     * @since 5.2.13-SNAPSHOT
     */
    public CompatibleProfile createCompatibleProfile(@Nullable String name, @Nullable UUID id, @Nullable String texture) throws IllegalArgumentException;
    
    
    /**
     * Gets the head used as a base for a displaying a given vanilla skull type.
     * Note: if the particular skull-type is not supported in your server version, this will create a Player-type head
     * for you to use for skinning/placeholding.
     * If you want more control over head happens in these cases, consider using CompatibleSkullMaterial directly.
     * @param material the supported compatible skull material/type
     * @param amount amount of items to create in the stack
     * @return an ItemStack of the head
     * @since 5.2.13-SNAPSHOT
     */
    
    @NotNull
    public ItemStack getCompatibleHeadItem(@NotNull CompatibleSkullMaterial material, int amount);
    
    
    
    /**
     * Determine if a head profile is a custom head or not, created by a plugin or for decoration.
     * This is a best-guess determination based on whether a username is set and possibly other details.
     * You should check head information further in addition to this step (eg: check plugin-reserved UUIDs and names)
     * @since 5.2.14-SNAPSHOT
     * @param username username associated with the head
     * @param id the UUID associated with the head
     * @return whether it is assumed to be a custom head.
     * @throws IllegalArgumentException if profile is null
     */
    public boolean isCustomHead(String username, UUID id);
    /**
     * Determine if a head profile is a custom head or not, created by a plugin or for decoration.
     * This is a best-guess determination based on whether a username is set and possibly other details.
     * You should check head information further in addition to this step (eg: check plugin-reserved UUIDs and names)
     * @since 5.2.14-SNAPSHOT
     * @param profile the profile information for the head
     * @return whether it is assumed to be a custom head.
     * @throws IllegalArgumentException if profile is null
     */
    public boolean isCustomHead(CompatibleProfile profile);
    /**
     * Determine if a head profile is a custom head or not, created by a plugin or for decoration.
     * This is a best-guess determination based on whether a username is set and possibly other details.
     * You should check head information further in addition to this step (eg: check plugin-reserved UUIDs and names)
     * @since 5.2.14-SNAPSHOT
     * @param skull the profile information for the head
     * @return whether it is assumed to be a custom head.
     * @throws IllegalArgumentException if profile is null or not a skull type (SkullMeta, Skull block)
     */
    public boolean isCustomHead(Object skull);
    
    /**
     * Clears internal profile information from a skull.
     * @param skull a Skull blockstate or SkullMeta object to clear the profile from
     * @return false if clearing the profile failed or was unsupported, otherwise true.
     * @since 5.2.14-SNAPSHOT
     */
    public boolean clearProfile(Object skull) throws IllegalArgumentException;

}
