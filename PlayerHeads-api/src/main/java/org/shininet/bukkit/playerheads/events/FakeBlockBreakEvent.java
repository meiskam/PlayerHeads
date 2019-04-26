/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Event used internally by the plugin to detect if a hypothetical BlockBreak would be cancelled by another plugin.
 * 
 * Used in determining whether drops should occur when heads might be broken by a player.
 * This is useful when using plugins such as WorldGuard that cancel blockbreak in protected areas.
 * 
 * <i>Note:</i> This documentation was inferred after the fact and may be inaccurate.
 * 
 * @author meiskam
 * @deprecated you should use SimulatedBlockBreakEvent instead, this class may be removed in the future
 * @see com.github.crashdemons.playerheads.compatibility.plugins.SimulatedBlockBreakEvent
 */
@Deprecated
public class FakeBlockBreakEvent extends BlockBreakEvent {

    /**
     * Constructs a simulated block break event
     * @param theBlock the block to "break"
     * @param player the player doing the breaking
     */
    public FakeBlockBreakEvent(Block theBlock, Player player) {
        super(theBlock, player);
    }

}
