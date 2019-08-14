/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.playerheads.events;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for events that drop a plugin-supported head.
 * @since 4.10.0-SNAPSHOT
 * @author crashdemons (crashenator at gmail.com)
 */
public interface DropHeadEvent {

    public ItemStack getDrop();
    
    /**
     * Set the item dropped by the event.
     * 5.2+ API
     * @since 5.2.0-SNAPSHOT
     * @param stack 
     */
    public void setDrop(@Nullable final ItemStack stack);
}
