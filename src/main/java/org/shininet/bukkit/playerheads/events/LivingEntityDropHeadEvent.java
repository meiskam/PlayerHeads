/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author meiskam
 */

public class LivingEntityDropHeadEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled = false;
    private ItemStack drop;

    public LivingEntityDropHeadEvent(LivingEntity entity, ItemStack drop) {
        super(entity);
        this.drop = drop;
    }

    /**
     * @return mutable ItemStack that will drop into the world once this event is over
     */
    public ItemStack getDrop() {
        return drop;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) entity;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
