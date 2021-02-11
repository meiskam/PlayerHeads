/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.shininet.bukkit.playerheads.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Event created by the PlayerHeads plugin when a Player is beheaded.
 *
 * Cancellable.
 * @since 3.11
 * @author meiskam
 */
public class PlayerDropHeadEvent extends LivingEntityDropHeadEvent {

    /**
     * Constructs the event
     *
     * @param player the player that was beheaded
     * @param drop the head item to be dropped
     */
    public PlayerDropHeadEvent(final Player player, final ItemStack drop) {
        super(player, drop);
    }
    /**
     * Constructs the event
     *
     * @param cause the event which caused the beheading event, or null.
     * @param player the player that was beheaded
     * @param killer the killer responsible for the death of the mob. As determined by the plugin (may differ from entity.getKiller())
     * @param drop the head item to be dropped
     * @since 5.2.14-SNAPSHOT
     */
    public PlayerDropHeadEvent(@Nullable final Event cause, final Player player, LivingEntity killer, final ItemStack drop) {
        super(cause, player, killer, drop);
    }

    /**
     * Gets the player that was beheaded.
     *
     * @return the player that was beheaded.
     */
    @Override
    public Player getEntity() {
        return (Player) entity;
    }
}
