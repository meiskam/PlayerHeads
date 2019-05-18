/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads.api;

import org.jetbrains.annotations.Nullable;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.shininet.bukkit.playerheads.PlayerHeadsPlugin;

/**
 * Interface for the PlayerHeads API. This interface defines each method that
 * the PlayerHeads API provides.
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public interface PlayerHeadsAPI {

    /**
     * Get the main PlayerHeads Plugin instance
     *
     * @return the PlayerHeads plugin object, or null if unavailable.
     */
    @NotNull
    public PlayerHeadsPlugin getPlugin();

    /**
     * Get the version of the PlayerHeads plugin in use
     *
     * @return the version of the PlayerHeads plugin in use
     */
    @NotNull
    public String getVersion();

    /**
     * Gets the type of head associated with an itemstack.
     *
     * @param s the itemstack to check
     * @return the type of head, or null if there is none
     */
    @Nullable
    public HeadType getHeadFrom(ItemStack s);

    /**
     * Gets the type of head associated with the Block
     *
     * @param s the blockstate to check
     * @return the type of head, or null if the block wasn't a head.
     */
    @Nullable
    public HeadType getHeadFrom(BlockState s);

    /**
     * Gets the type of head associated with an entity
     *
     * @param e the entity to check
     * @return the type of head, or null if there is no viable head.
     */
    @Nullable
    public HeadType getHeadOf(Entity e);

    /**
     * Gets the type of head associated with an entity-type. See deprecation
     * note for limitations.
     *
     * @param t the entity type to check
     * @deprecated this may not do exactly what you want because between MC
     * 1.10-1.13 many entities were separated from variants to their own
     * entity-type. Using this method will introduce version-dependent behavior
     * (For example: this may not be able to tell a stray from a skeleton in
     * certain server versions) and should be avoided by using
     * getHeadFrom(Entity) instead.
     * @see #getHeadDrop(org.bukkit.entity.Entity)
     * @return the type of head, or null if there is no viable head.
     */
    @Nullable
    public HeadType getHeadOf(EntityType t);

    /**
     * Gets a stack of head items for the given type and amount. Note:
     * player-type heads will be untextured and have no set owner with this
     * method.
     *
     * @param h the head type to create items of
     * @param num the number of items to create
     * @return The item stack
     */
    @NotNull
    public ItemStack getHeadItem(HeadType h, int num);

    /**
     * Gets a stack of head items as they would normally be dropped from the
     * given entity. Note: this method takes into full account plugin
     * configuration settings, player's name, and preset item drop amount
     *
     * @param e the entity to create items from
     * @return The item stack
     */
    public ItemStack getHeadDrop(Entity e);
}
