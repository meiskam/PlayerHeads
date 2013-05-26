/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * @author meiskam
 */

public class FakeBlockBreakEvent extends BlockBreakEvent {

    public FakeBlockBreakEvent(Block theBlock, Player player) {
        super(theBlock, player);
    }

}
