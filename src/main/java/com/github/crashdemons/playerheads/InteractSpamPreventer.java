
package com.github.crashdemons.playerheads;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Helper class that records and detects PlayerInteractEvent right-click spam for playerheads.
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
    
    /**
     * Defines a spam-detection result object.
     * 
     * Used as semantic-sugar to make code self-documenting (eg:  `if(spampreventer.recordEvent(evt).isSpam())`  is more descriptive than `if(spampreventer.recordEvent(evt))`)
     * 
     */
    public class InteractSpamResult{
        private boolean spam;
        /**
         * <b><i>[INTERNAL - DO NOT USE]</i></b> Constructs a spam result
         * @param spam the internal boolean state to abstract
         */
        public InteractSpamResult(boolean spam){this.spam=spam;}
        /**
         * Reports whether the spam result indicates that the record was spam or not.
         * 
         * Reports the internal boolean state this class abstracts.
         * @return true: it was detected as spam. false: it was not
         */
        public boolean isSpam(){return spam;}
        /**
         * <b><i>[INTERNAL - DO NOT USE]</i></b> Toggles the internal boolean state the class abstracts.
         */
        public void toggle(){ spam=!spam; }
    }
    /**
     * Records an interaction event internally and prepares a result after analyzing the event.
     * @param event The PlayerInteractEvent to send to the spam-preventer.
     * @return The Spam-detection Result object
     */
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
    
    
