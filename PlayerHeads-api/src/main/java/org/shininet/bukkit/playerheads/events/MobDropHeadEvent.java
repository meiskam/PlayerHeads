/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.shininet.bukkit.playerheads.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

/**
 * Event created by the PlayerHeads plugin when a Mob is beheaded.
 *
 * Cancellable.
 *
 * @author meiskam
 */
public class MobDropHeadEvent extends LivingEntityDropHeadEvent {

    /**
     * Constructs the event
     *
     * @param mob the mob that was beheaded
     * @param drop the head item to be dropped.
     */
    public MobDropHeadEvent(LivingEntity mob, ItemStack drop) {
        super(mob, drop);
    }
}
