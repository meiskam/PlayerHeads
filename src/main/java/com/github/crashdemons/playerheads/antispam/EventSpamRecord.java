/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.antispam;

import org.bukkit.event.Event;

/**
 *
 * @author crash
 */
public abstract class EventSpamRecord {
    private final long timestamp;
    
    public boolean closeTo(EventSpamRecord record, long threshold_ms){
        long diff = Math.abs( record.timestamp - timestamp );
        return (diff < threshold_ms);
    }
    public long getTimestamp(){ return timestamp; }
    public EventSpamRecord(Event event){
            timestamp = System.currentTimeMillis();
    }
}
