
package com.github.crashdemons.playerheads;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author crash
 */
public class InteractSpamPreventer {
    private final static int RECORDS = 5;
    private final InteractRecord[] records = new InteractRecord[RECORDS];
    private volatile int next = 0;
            
    private class InteractRecord{
        private static final long TIME_THRESHOLD_MS=1000;
        Location location=null;
        UUID playerId;
        long timestamp;
        public InteractRecord(PlayerInteractEvent event){
            timestamp = System.currentTimeMillis();
            playerId = event.getPlayer().getUniqueId();
            Block block = event.getClickedBlock();
            if(block!=null) location=block.getLocation();
        }
        boolean closeTo(InteractRecord record){
            if(record==null) return false;
            if(record.playerId.equals(playerId)){
                if(record.location==null || location==null) return false;
                if(record.location.equals(location)){
                    long diff = Math.abs( record.timestamp - timestamp );
                    if(diff < TIME_THRESHOLD_MS) return true;
                }
            }
            return false;
        }
    }
    public class InteractSpamResult{
        private boolean spam;
        public InteractSpamResult(boolean spam){this.spam=spam;}
        public boolean isSpam(){return spam;}
        public void toggle(){ spam=!spam; }
    }
    public synchronized InteractSpamResult recordEvent(PlayerInteractEvent event){
        InteractSpamResult result = new InteractSpamResult(false);
        InteractRecord record = new InteractRecord(event);
        for(InteractRecord otherRecord : records){
            if(record.closeTo(otherRecord)){
                result.toggle();
                break;
            }
        }
        records[next] = record;
        next = (next+1)%RECORDS;
        return result;
    }
}
    
    
