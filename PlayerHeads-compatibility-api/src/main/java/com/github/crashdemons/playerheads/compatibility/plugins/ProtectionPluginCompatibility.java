/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.plugins;

import com.github.crashdemons.playerheads.compatibility.Compatibility;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;

/**
 * Defines required operations performed for generic block/region protection plugins for compatibility reasons.
 * @author crashdemons (crashenator at gmail.com)
 */
public class ProtectionPluginCompatibility extends CompatiblePlugin{
    /**
     * Create the protection-plugin compatibility class
     * @param parentPlugin the plugin requesting the compatibility support (provides logging/events)
     */
    public ProtectionPluginCompatibility(Plugin parentPlugin){
        super(parentPlugin,"");
    }
    private SimulatedBlockBreakEvent simulateBlockBreak(Block block, Player player){
        parentPlugin.getServer().getPluginManager().callEvent(new PlayerAnimationEvent(player));
        parentPlugin.getServer().getPluginManager().callEvent(new BlockDamageEvent(player, block, Compatibility.getProvider().getItemInMainHand(player), true));//player.getEquipment().getItemInMainHand()

        SimulatedBlockBreakEvent fakebreak = new SimulatedBlockBreakEvent(block, player);
        parentPlugin.getServer().getPluginManager().callEvent(fakebreak);
        return fakebreak;
    }
    /**
     * Simulates a block-break event from a user in order to check if it would normally be blocked by a protection-plugin.
     * @param block the block allegedly being broken.
     * @param player the player allegedly breaking the block.
     * @return true: the block break event was allowed, false: the block-break event was blocked (cancelled).
     */
    public boolean testBlockBreak(Block block, Player player){
        return !simulateBlockBreak(block,player).isCancelled();
    }
}
