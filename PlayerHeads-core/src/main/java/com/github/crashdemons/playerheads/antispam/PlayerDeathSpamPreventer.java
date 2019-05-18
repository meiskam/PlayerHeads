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
 * Helper class that records and detects player deaths being spammed repeatedly
 * (to farm heads, etc).
 *
 * @author crash
 */
public class PlayerDeathSpamPreventer extends EventSpamPreventer {

    private final long deathThresholdMs;

    public PlayerDeathSpamPreventer(int numRecords, long timeMS) {
        super(numRecords);
        deathThresholdMs = timeMS;
    }

    private class PlayerDeathRecord extends EventSpamRecord {

        private UUID victimId = null;
        private UUID killerId = null;

        public PlayerDeathRecord(EntityDeathEvent event) {
            super(event);
            Entity victimEntity = event.getEntity();
            if (victimEntity instanceof Player) {
                Player victim = (Player) victimEntity;
                victimId = victim.getUniqueId();
                if (victimId != null) {
                    Player killer = victim.getKiller();
                    if (killer == null) {
                        killerId = null;
                    } else {
                        killerId = killer.getUniqueId();
                    }
                }
            }
        }

        boolean sameKiller(PlayerDeathRecord record) {
            if (record == null) {
                return false; //can't match against null record - NPE protection
            }
            if (killerId == null && record.killerId == null) {
                return true; //if both null (mob death) then they're considered to match.
            }
            if (killerId == null || record.killerId == null) {
                return false; //killerid null but other killer wasn't null above, then they don't match - prevent NPE on either object below
            }
            return killerId.equals(record.killerId); //ceck if UUD's match
        }

        boolean closeTo(PlayerDeathRecord record) {
            if (record == null) {
                return false;
            }
            if (victimId == null) {
                return false; //this shouldn't be null if the event was properly from a player
            }
            if (victimId.equals(record.victimId) && sameKiller(record)) {
                return super.closeTo(record, deathThresholdMs);
            }
            return false;
        }
    }

    @Override
    public SpamResult recordEvent(org.bukkit.event.Event event) {
        if (event instanceof EntityDeathEvent) {
            return recordEvent((EntityDeathEvent) event);
        }
        return new SpamResult(false);
    }

    /**
     * Records an interaction event internally and prepares a result after
     * analyzing the event.
     * <p>
     * For the current implementation, a death by the same person to the same
     * killer (player or null) within 5 minutes is considered spam (within 5
     * death records).
     *
     * @param event The EntityDeathEvent to send to the spam-preventer, this is
     * expected to be a player death.
     * @return The Spam-detection Result object
     * @see EventSpamPreventer#recordEvent(org.bukkit.event.Event)
     */
    public synchronized SpamResult recordEvent(EntityDeathEvent event) {
        SpamResult result = new SpamResult(false);
        PlayerDeathRecord record = new PlayerDeathRecord(event);
        for (EventSpamRecord otherRecordObj : records) {
            PlayerDeathRecord otherRecord = (PlayerDeathRecord) otherRecordObj;
            if (record.closeTo(otherRecord)) {
                result.toggle();
                break;
            }
        }
        addRecord(record);
        return result;
    }
}
