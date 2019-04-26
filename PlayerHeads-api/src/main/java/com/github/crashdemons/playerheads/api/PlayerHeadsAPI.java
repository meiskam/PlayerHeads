/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads.api;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.shininet.bukkit.playerheads.PlayerHeadsPlugin;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public interface PlayerHeadsAPI {
    public PlayerHeadsPlugin getPlugin();
    public Head getHeadFrom(ItemStack s);
    public Head getHeadFrom(BlockState s);
    public Head getHeadOf(Entity e);
    public Head getHeadOf(EntityType t);//note: not reliable!
    public ItemStack getHeadItem(Head h, int num);
}
