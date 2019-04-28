/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.api;

import com.github.crashdemons.playerheads.SkullConverter;
import com.github.crashdemons.playerheads.SkullManager;
import com.github.crashdemons.playerheads.TexturedSkullType;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.shininet.bukkit.playerheads.PlayerHeadsPlugin;
import org.shininet.bukkit.playerheads.PlayerHeads;

/**
 * Implements the API by wrapping internal methods
 * @author crashdemons (crashenator at gmail.com)
 */
public class ApiProvider implements PlayerHeadsAPI {
    final private PlayerHeads plugin;
    public ApiProvider(PlayerHeadsPlugin plugin){this.plugin=(PlayerHeads) plugin;}
    
    @Override
    public PlayerHeadsPlugin getPlugin(){
        return plugin;
    }
    
    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }
    
    @Override
    public HeadType getHeadFrom(ItemStack s){
        return SkullConverter.skullTypeFromItemStackLegacy(s);
    }
    
    @Override
    public HeadType getHeadFrom(BlockState s){
        return SkullConverter.skullTypeFromBlockStateLegacy(s);
    }
    @Override
    public HeadType getHeadOf(Entity e){
        return SkullConverter.skullTypeFromEntity(e);
    }
    @Override
    public HeadType getHeadOf(EntityType t){
        try{
            return TexturedSkullType.valueOf(t.name());
        }catch(Exception e){
            return null;
        }
    }
    @Override
    public ItemStack getHeadItem(HeadType h, int num){
        TexturedSkullType type = headFromApiHead(h);
        if(type==null) return null;
        
        boolean addLore = plugin.configFile.getBoolean("addlore");
        boolean usevanillaskull = plugin.configFile.getBoolean("dropvanillaheads");
        if(type==TexturedSkullType.PLAYER) return SkullManager.PlayerSkull(num, true);
        return SkullManager.MobSkull(type, num, usevanillaskull, addLore);
    }
    
    @Override
    public ItemStack getHeadDrop(Entity e){
        TexturedSkullType type = SkullConverter.skullTypeFromEntity(e);
        if(type==null) return null;
        boolean addLore = plugin.configFile.getBoolean("addlore");
        ItemStack drop;
        if(e instanceof Player){
            Player player = (Player) e;
            String skullOwner;
            if (plugin.configFile.getBoolean("dropboringplayerheads")) {
                skullOwner = "";
            } else {
                skullOwner = player.getName();
            }
            if(skullOwner==null || skullOwner.isEmpty()){
                drop = SkullManager.PlayerSkull(addLore);
            }else{
                drop = SkullManager.PlayerSkull(skullOwner,addLore);
            }
        }else{
            boolean usevanillaskull = plugin.configFile.getBoolean("dropvanillaheads");
            drop = SkullManager.MobSkull(type,usevanillaskull,addLore);
        }
        return drop;
    }
    
    private TexturedSkullType headFromApiHead(HeadType h){
        return TexturedSkullType.get( h.getOwner() );
    }
}
