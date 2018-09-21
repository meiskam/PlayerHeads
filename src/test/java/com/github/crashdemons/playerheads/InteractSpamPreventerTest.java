/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author crash
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PlayerInteractEvent.class)
public class InteractSpamPreventerTest {
    
    public InteractSpamPreventerTest() {
    }
    

    @Test
    public void testRecordEvent_SameUserSpam() {
        System.out.println("testRecordEvent SameUserSpam");
        PlayerInteractEvent mockEvent = PowerMockito.mock(PlayerInteractEvent.class);
        
        Player playerA = Mocks.getMockPlayer("3437cf83-c9b0-4709-a686-b8632b8d6172", "crashdemons", 1, 2, 3);
        
        Block blockA = Mocks.getMockBlock(Material.PLAYER_HEAD, 7, 8, 9);
        
        when(mockEvent.getPlayer()).thenReturn(playerA);
        when(mockEvent.getClickedBlock()).thenReturn(blockA);
        
        InteractSpamPreventer antispam = new InteractSpamPreventer();
        
        assertEquals(antispam.recordEvent(mockEvent).isSpam(),false);
        for(int i=0;i<100;i++)
            assertEquals(antispam.recordEvent(mockEvent).isSpam(),true);
        
    }
    @Test
    public void testRecordEvent_SameUserNotSpam() {
        System.out.println("testRecordEvent SameUserNotSpam");
        
        Player playerA = Mocks.getMockPlayer("3437cf83-c9b0-4709-a686-b8632b8d6172", "crashdemons", 1, 2, 3);
        
        InteractSpamPreventer antispam = new InteractSpamPreventer();
        for(int i=0;i<100;i++){
            PlayerInteractEvent mockEvent = PowerMockito.mock(PlayerInteractEvent.class);
            Block blockA = Mocks.getMockBlock(Material.PLAYER_HEAD, i, i+1, i+2);
            when(mockEvent.getPlayer()).thenReturn(playerA);
            when(mockEvent.getClickedBlock()).thenReturn(blockA);
            assertEquals(antispam.recordEvent(mockEvent).isSpam(),false);
        }
    }
    @Test
    public void testRecordEvent_MultipleUsersSameBlock_NotSpam() {
        System.out.println("testRecordEvent MultipleUsersSameBlock_NotSpam");
        
        
        InteractSpamPreventer antispam = new InteractSpamPreventer();
        for(int i=100;i<200;i++){
            PlayerInteractEvent mockEvent = PowerMockito.mock(PlayerInteractEvent.class);
            Player playerA = Mocks.getMockPlayer("3437cf83-c9b0-4709-a686-b8632b8d6"+i, "user"+i, i, i+1, i+2);
            Block blockA = Mocks.getMockBlock(Material.PLAYER_HEAD, 1, 2, 3);
            when(mockEvent.getPlayer()).thenReturn(playerA);
            when(mockEvent.getClickedBlock()).thenReturn(blockA);
            assertEquals(antispam.recordEvent(mockEvent).isSpam(),false);
        }
    }
    
}
