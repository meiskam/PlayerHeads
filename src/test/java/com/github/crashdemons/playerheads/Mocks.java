/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.when;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author crash
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class})
abstract public class Mocks {
    
    public static void setupFakeServerVersion(){
        try{
            PowerMockito.mockStatic(Bukkit.class);
            when(Bukkit.getVersion()).thenReturn("git-SomeWackyServerFork-4454-4ad3bc (MC: 1.13.1)");
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    
    public static Location getMockLocation(double x, double y, double z){
        Location loc = PowerMockito.mock(Location.class);
        when(loc.getWorld()).thenReturn(null);
        when(loc.getX()).thenReturn(x);
        when(loc.getY()).thenReturn(y);
        when(loc.getZ()).thenReturn(z);
        return loc;
    }
    
    
    public static Block getMockBlock(Material mat, double x, double y, double z){
        Block block = PowerMockito.mock(Block.class);
        Location loc = getMockLocation(x,y,z);
        when(block.getLocation()).thenReturn(loc);
        when(block.getType()).thenReturn(mat);
        //block.getState()
        return block;
    }
    
    public static Player getMockPlayer(String id, String name, double x, double y, double z){
        Location loc = getMockLocation(x,y,z);
        Player player = PowerMockito.mock(Player.class);
        when(player.getUniqueId()).thenReturn(UUID.fromString(id));
        when(player.getName()).thenReturn(name);
        when(player.getLocation()).thenReturn(loc);
        return player;
    }
    public static OfflinePlayer getMockOfflinePlayer(String id, String name){
        OfflinePlayer op = PowerMockito.mock(OfflinePlayer.class);
        when(op.getName()).thenReturn(name);
        when(op.getUniqueId()).thenReturn(UUID.fromString(id));
        return op;
    }
    public static BlockState getMockBlockState_Stone(){
        BlockState state = PowerMockito.mock(BlockState.class);
        when(state.getType()).thenReturn(Material.STONE);
        return state;
    }
    public static BlockState getMockBlockState_PHead(OfflinePlayer owningPlayer){
        String name = null;
        if(owningPlayer!=null) name = owningPlayer.getName();
        Skull state = PowerMockito.mock(Skull.class);
        when(state.getType()).thenReturn(Material.PLAYER_HEAD);
        when(state.getOwningPlayer()).thenReturn(owningPlayer);
        when(state.getOwner()).thenReturn(name);
        
        return state;
    }
    public static BlockState getMockBlockState_Skull(){
        BlockState state = PowerMockito.mock(BlockState.class);
        when(state.getType()).thenReturn(Material.SKELETON_SKULL);
        return state;
    }
    
}
