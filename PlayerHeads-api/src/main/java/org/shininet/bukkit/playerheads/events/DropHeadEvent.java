/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.playerheads.events;

import org.bukkit.inventory.ItemStack;

/**
 * Interface for events that drop a plugin-supported head.
 * @author crashdemons (crashenator at gmail.com)
 */
public interface DropHeadEvent {
    public ItemStack getDrop();
}
