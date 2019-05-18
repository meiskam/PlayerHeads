/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.antispam;

import org.bukkit.event.Event;

/**
 * Defines an abstract event record for antispam features.
 * <p>
 * Implements basing record timestamp recording and comparison common to all
 * records.
 *
 * @author crash
 */
public abstract class EventSpamRecord {

    private final long timestamp;

    /**
     * Determines if the record is "close to" another record by comparing
     * timestamps against a time threshold.
     *
     * @param record the record to compare with
     * @param thresholdMs the threshold of milliseconds within which the record
     * should be considered recent / spam.
     * @return Whether the record is close to the other record, given the
     * parameters.
     */
    public boolean closeTo(EventSpamRecord record, long thresholdMs) {
        long diff = Math.abs(record.timestamp - timestamp);
        return (diff < thresholdMs);
    }

    /**
     * Gets the timestamp associated with this record's creation.
     *
     * @return the timestamp as a long integer.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Constructs the event record about an event.
     * <p>
     * Note: this base class does not store any event-specific information
     * except the time, that is up to child classes to do.
     *
     * @param event the bukkit event to record information about.
     */
    public EventSpamRecord(Event event) {
        timestamp = System.currentTimeMillis();
    }
}
