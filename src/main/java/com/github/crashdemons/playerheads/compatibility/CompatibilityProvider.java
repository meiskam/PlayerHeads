/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * An interface specifying all of the methods we need for our plugin that require differing Bukkit-specific implementations which we wish to abstract from our plugin code.
 * @author crashdemons (crashenator at gmail.com)
 */
public interface CompatibilityProvider {
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
    public void setItemInMainHand(Player p,ItemStack s);
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
    public String getCompatibleNameFromEntity(Entity e);//determine forward-portable name of entity even if they are variants.
}
