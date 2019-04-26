/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.api;

import com.github.crashdemons.playerheads.SkullManager;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.shininet.bukkit.playerheads.PlayerHeadsPlugin;

/**
 * Implements the API by wrapping internal methods
 * @author crashdemons (crashenator at gmail.com)
 */
public class ApiWrapper {//implements PlayerHeadsAPI {
    public ApiWrapper(){}
    public PlayerHeadsPlugin getPlugin(){
        return (PlayerHeadsPlugin) Bukkit.getServer().getPluginManager().getPlugin("PlayerHeads");
    }
    /*public Head getHeadFrom(ItemStack s){ SkullManager. }
    public Head getHeadFrom(BlockState s);
    public Head getHeadOf(Entity e);
    public Head getHeadOf(EntityType t);
    public ItemStack getHeadItem(Head h, int num);*/
}
