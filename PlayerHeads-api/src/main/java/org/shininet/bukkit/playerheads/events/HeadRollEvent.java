/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.playerheads.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event created by PlayerHeads (4.9.2+) to indicate that a head dropchance roll has occurred and the success/failure has been determined.
 * This event allows third-party plugin authors to analyze and modify drop chance success with all factors considered by PlayerHeads available.
 * If the success of this event is set to false, no head will be dropped. If it is set to true, a head will be dropped.
 * @author crashdemons (crashenator at gmail.com)
 */
public class HeadRollEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Entity killer;
    private final Entity target;

    private final boolean killerAlwaysBeheads;
    private final double lootingModifier;
    
    private final double originalDropRoll;
    private final double effectiveDropRoll;
    private final double originalDropRate;
    private final double effectiveDropRate;
    private boolean dropSuccess;

    /**
     * Creates the Head dropchance event for PlayerHeads.
     * @param killer the Entity beheading another
     * @param target the Entity being beheaded
     * @param killerAlwaysBeheads whether the killer has the always-behead permission
     * @param originalDropRoll the randomized PRNG double droproll value inclusively between 0 to 1.
     * @param lootingModifier the fractional probability modifier (greater than or equal to 1.0) of looting, as applied by PlayerHeads to the effective droprate.
     * @param effectiveDropRoll the modified droproll value after permission logic was applied (alwaysbehead sets to 0)
     * @param originalDropRate the configured droprate of the target as a fraction (0.01 = 1%)
     * @param effectiveDropRate the effective droprate of the target as a fraction (0.01 = 1%), as modified by looting.
     * @param dropSuccess whether the droproll was determined to be initially a successful roll.
     */
    public HeadRollEvent(Entity killer, Entity target, boolean killerAlwaysBeheads, double lootingModifier, double originalDropRoll, double effectiveDropRoll, double originalDropRate, double effectiveDropRate, boolean dropSuccess) {
        this.lootingModifier=lootingModifier;
        this.originalDropRate=originalDropRate;
        this.effectiveDropRate=effectiveDropRate;
        this.dropSuccess=dropSuccess;
        this.effectiveDropRoll=effectiveDropRoll;
        this.originalDropRoll=originalDropRoll;
        this.killerAlwaysBeheads=killerAlwaysBeheads;
        
        this.killer=killer;
        this.target=target;
    }

    /**
     * Gets the looting modifier (multiplier) that modified the effective droprate. Generally this is 1 (no effect) or greater.
     * @return the looting modifier
     */
    public double getLootingModifier(){
        return lootingModifier;
    }
    
    
    /**
     * Get the Killer's entity that may have done the beheading.
     * @return the entity of the killer, or null if the killer was a mob.
     */
    public Entity getKiller() {
        return killer;
    }

    /**
     * Get the Target's entity that may have been beheaded
     * @return the entity of the target
     */
    public Entity getTarget() {
        return target;
    }

    /**
     * Gets whether the killer was configured to always behead this type of target.
     * Note: this may differ on whether the target was a player or mob.
     * If this is true, the effective droproll may have been set to 0 to force success.
     * @return Whether the killer was configured to always behead
     */
    public boolean getKillerAlwaysBeheads() {
        return killerAlwaysBeheads;
    }

    /**
     * Gets the original PRNG-generated random value of the drop roll, uniform between 0 and 1 inclusively.
     * When this value is lower than the droprate value by chance, the roll is considered successful.
     * @return the drop roll value in the range [0,1]
     */
    public double getOriginalDropRoll() {
        return originalDropRoll;
    }
    
    /**
     * Gets the effective drop roll value after modification by PlayerHeads.
     * The droproll will normally be reflected by the original random droproll, except if the killer always beheads, then this may be 0.
     * If this is below the droprate, the roll would have been determined to be a success.
     * @return the effective drop roll.
     * @see #getOriginalDropRoll
     */
    public double getEffectiveDropRoll() {
        return effectiveDropRoll;
    }

    /**
     * Gets the configured droprate for the target as a fractional probability, unmodified.
     * @return the droprate
     */
    public double getOriginalDropRate() {
        return originalDropRate;
    }
    /**
     * Gets the configured droprate for the target as a fractional probability, after modification by looting.
     * @return the droprate
     */
    public double getEffectiveDropRate() {
        return effectiveDropRate;
    }

    /**
     * Whether the effective drop roll was determined to be a success.
     * @return the success of the drop roll
     */
    public boolean getDropSuccess() {
        return dropSuccess;
    }

    /**
     * Sets whether the drop roll should be considered a success.
     * @param value whether the head drop should succeed or fail.
     */
    public void setDropSuccess(boolean value) {
        dropSuccess = value;
    }

    /**
     * Whether the effective drop roll was determined to be a success.
     * Alias of getDropSuccess
     * @see #getDropSuccess() 
     * @return the success of the drop roll
     */
    public boolean succeeded() {
        return getDropSuccess();
    }

    /**
     * Get a list of handlers for the event.
     *
     * @return a list of handlers for the event
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Get a list of handlers for the event.
     *
     * @return a list of handlers for the event
     */
    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
