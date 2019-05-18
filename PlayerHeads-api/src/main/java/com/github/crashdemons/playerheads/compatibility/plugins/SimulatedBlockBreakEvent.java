/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.github.crashdemons.playerheads.compatibility.plugins;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.shininet.bukkit.playerheads.events.FakeBlockBreakEvent;

/**
 * Event used internally by the plugin to detect if a hypothetical BlockBreak
 * would be cancelled by another plugin.
 *
 * Used in determining whether drops should occur when heads might be broken by
 * a player. This is useful when using plugins such as WorldGuard that cancel
 * blockbreak in protected areas.
 *
 * This version of the class currently extends FakeBlockBreakEvent so that
 * external plugin compatibility is not broken, but this is intended to be the
 * new location of the class in the future.
 *
 * (Previously FakeBlockBreakEvent)
 *
 * <i>Note:</i> This documentation was inferred after the fact and may be
 * inaccurate.
 *
 * @author meiskam
 */
public class SimulatedBlockBreakEvent extends FakeBlockBreakEvent {

    /**
     * Constructs a simulated block break event
     *
     * @param theBlock the block to "break"
     * @param player the player doing the breaking
     */
    public SimulatedBlockBreakEvent(Block theBlock, Player player) {
        super(theBlock, player);
    }

}
