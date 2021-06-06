/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.playerheads.events.modifiers;

/**
 * Indicates the type of modifier to the effective droprate.
 * 
 * @since 5.2.16-SNAPSHOT
 * @author crashdemons (crashenator at gmail.com)
 */
public enum DropRateModifierType {
    /**
     * adds a constant value to the droprate
     */
    ADD_CONSTANT,
    
    /**
     * adds a VALUE*droprate to the droprate. This is equivalent to a multiplier of 1+VALUE. Positive values increase droprate by a fraction, negative values will reduuce the droprate by a fraction.
     * In this way, additive multipliers can be thought of as a delta value.
     */
    ADD_MULTIPLE,
    
    /**
     * adds a value to the droprate where the value is a multiple of a provided effectiveness level. this is equivalent to a multiplier of (1+VALUE*LEVEL)
     */
    ADD_MULTIPLE_PER_LEVEL,
    
    /**
     * Multiplies the droprate against a value. Values under 1 reduce the droprate, values above 1 are equivalent to additive multipliers 
     */
    MULTIPLY,
    
}
