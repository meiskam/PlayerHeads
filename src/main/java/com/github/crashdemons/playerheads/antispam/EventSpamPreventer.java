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
public abstract class EventSpamPreventer {
    protected final static int RECORDS = 5;
    protected final EventSpamRecord[] records = new EventSpamRecord[RECORDS];
    private volatile int next = 0;
    
    protected synchronized void addRecord(EventSpamRecord record){
        records[next] = record;
        next = (next+1)%RECORDS;
    }
    public abstract SpamResult recordEvent(Event event);
}
