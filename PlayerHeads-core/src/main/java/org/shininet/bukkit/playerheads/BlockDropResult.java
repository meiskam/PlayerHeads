/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.playerheads;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
enum BlockDropResult {
    FAILED_EVENT_CANCELLED(true,true),
    FAILED_CUSTOM_HEAD(true,false),
    FAILED_BLOCKED_HEAD(true,false),
    FAILED_DEFERRED_TO_VANILLA(true,false),
    SUCCESS(false,false),
;    
    public final boolean isFailure;
    public final boolean eventCancelled;
    
    private BlockDropResult(boolean failure, boolean eventCancelled){
        this.isFailure=failure;
        this.eventCancelled=eventCancelled;
    }
}
