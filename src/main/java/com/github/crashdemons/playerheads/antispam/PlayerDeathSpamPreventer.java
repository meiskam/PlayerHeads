/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.antispam;

import java.util.UUID;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 *
 * @author crash
 */
public class PlayerDeathSpamPreventer extends EventSpamPreventer{
    
    private class PlayerDeathRecord extends EventSpamRecord{
        private static final long TIME_THRESHOLD_MS=300000;
        UUID victimId;
        UUID killerId;
        public PlayerDeathRecord(EntityDeathEvent event){
            super(event);
            Entity victimEntity = event.getEntity();
            if(victimEntity instanceof Player){
                Player victim = (Player) victimEntity;
                victimId = victim.getUniqueId();
                Player killer = victim.getKiller();
                if(killer==null)
                    killerId=null;
                else
                    killerId=killer.getUniqueId();
            }
        }
        boolean sameKiller(PlayerDeathRecord record){ 
            if(killerId==null && record.killerId==null) return true;
            return killerId.equals(record.killerId);
        }
        boolean closeTo(PlayerDeathRecord record){
            if(record==null) return false;
            if(victimId.equals(record.victimId) && sameKiller(record)){
                return super.closeTo(record, TIME_THRESHOLD_MS);
            }
            return false;
        }
    }
    
    @Override
    public SpamResult recordEvent(org.bukkit.event.Event event){
        if(event instanceof EntityDeathEvent)
            return recordEvent((EntityDeathEvent) event);
        return new SpamResult(false);
    }
    
    public synchronized SpamResult recordEvent(EntityDeathEvent event){
        SpamResult result = new SpamResult(false);
        PlayerDeathRecord record = new PlayerDeathRecord(event);
        for(EventSpamRecord otherRecordObj : records){
            PlayerDeathRecord otherRecord = (PlayerDeathRecord) otherRecordObj;
            if(record.closeTo(otherRecord)){
                result.toggle();
                break;
            }
        }
        addRecord(record);
        return result;
    }
}
