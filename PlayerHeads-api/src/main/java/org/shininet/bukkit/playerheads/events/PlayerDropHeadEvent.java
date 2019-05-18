/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.shininet.bukkit.playerheads.events;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Event created by the PlayerHeads plugin when a Player is beheaded.
 *
 * Cancellable.
 *
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
     * Gets the player that was beheaded.
     *
     * @return the player that was beheaded.
     */
    @Override
    public Player getEntity() {
        return (Player) entity;
    }
}
