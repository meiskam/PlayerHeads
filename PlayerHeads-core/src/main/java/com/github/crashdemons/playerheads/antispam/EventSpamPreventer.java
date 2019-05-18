/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.antispam;

import org.bukkit.event.Event;

/**
 * Defines an abstract spam preventer that defines methods common to all spam-preventers.
 * 
 * Implements basic record storage for spam preventers.
 * Child classes are expected to create their own EventSpamRecord implementation and handle adding internal records through this class.
 * @author crash
 */
public abstract class EventSpamPreventer {
    /**
     * The number of internal records to keep for spam preventers. Default is 5.
     */
    protected final int recordCount; // = 5;
    /**
     * The buffer of internal spam records held by the spam preventer instance.
     * 
     * These are generally filled circularly by addRecord()
     * @see #addRecord(com.github.crashdemons.playerheads.antispam.EventSpamRecord) 
     */
    protected final EventSpamRecord[] records; // = new EventSpamRecord[RECORDS];
    private volatile int next = 0;
    
    public EventSpamPreventer(int numRecords){
        recordCount=numRecords;
        records=new EventSpamRecord[recordCount];
    }
    
    /**
     * Adds a record to internal (circular) storage.
     * @param record the record to add.
     */
    protected synchronized void addRecord(EventSpamRecord record){
        records[next] = record;
        next = (next+1)%recordCount;
    }
    
    /**
     * Records an event in internal storage and returns a spam check result.
     * 
     * Classes implementing this method should use addRecord to add internal records and methods of their implementation of EventSpamRecord to 
     * @param event the event to record and check.
     * @return the SpamResult associated with spam checks done by this method.
     * @see #addRecord(com.github.crashdemons.playerheads.antispam.EventSpamRecord) 
     */
    public abstract SpamResult recordEvent(Event event);
}
