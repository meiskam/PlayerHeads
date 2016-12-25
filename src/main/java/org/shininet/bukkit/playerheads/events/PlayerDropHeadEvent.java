/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads.events;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author meiskam
 */

public class PlayerDropHeadEvent extends LivingEntityDropHeadEvent {
    public PlayerDropHeadEvent(Player player, ItemStack drop) {
        super(player, drop);
    }

    @Override
    public Player getEntity() {
        return (Player) entity;
    }
}
