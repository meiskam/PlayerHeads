
package com.github.crashdemons.playerheads.antispam;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Helper class that records and detects PlayerInteractEvent right-click spam for playerheads.
 * @author crash
 */
public class InteractSpamPreventer extends EventSpamPreventer{
            
    private class InteractRecord extends EventSpamRecord{
        private static final long TIME_THRESHOLD_MS=1000;
        Location location=null;
        UUID playerId;
        public InteractRecord(PlayerInteractEvent event){
            super(event);
            playerId = event.getPlayer().getUniqueId();
            Block block = event.getClickedBlock();
            if(block!=null) location=block.getLocation();
        }
        boolean closeTo(InteractRecord record){
            if(record==null) return false;
            if(record.playerId.equals(playerId)){
                if(record.location==null || location==null) return false;
                if(record.location.equals(location)){
                    if(super.closeTo(record,TIME_THRESHOLD_MS)) return true;
                }
            }
            return false;
        }
    }
    
    @Override
    public SpamResult recordEvent(org.bukkit.event.Event event){
        if(event instanceof PlayerInteractEvent)
            return recordEvent((PlayerInteractEvent) event);
        return new SpamResult(false);
    }
    
    /**
     * Records an interaction event internally and prepares a result after analyzing the event.
     * @param event The PlayerInteractEvent to send to the spam-preventer.
     * @return The Spam-detection Result object
     */
    public synchronized SpamResult recordEvent(PlayerInteractEvent event){
        SpamResult result = new SpamResult(false);
        InteractRecord record = new InteractRecord(event);
        for(EventSpamRecord otherRecordObj : records){
            InteractRecord otherRecord = (InteractRecord) otherRecordObj;
            if(record.closeTo(otherRecord)){
                result.toggle();
                break;
            }
        }
        addRecord(record);
        return result;
    }
}
    
    
