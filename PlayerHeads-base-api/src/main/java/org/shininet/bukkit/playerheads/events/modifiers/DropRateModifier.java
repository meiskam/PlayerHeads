/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.playerheads.events.modifiers;

/**
 * A modifier for the droprate including type and value information
 *
 * @since 5.2.16-SNAPSHOT
 * @author crashdemons (crashenator at gmail.com)
 */
public class DropRateModifier {

    private final DropRateModifierType type;
    private final double value;
    private final int level;

    /**
     * Constructs a modifier with a value that is multiplied by a level of
     * effectiveness
     *
     * @param type the type of modifier
     * @param value the base value of the modifier
     * @param level the level to be multiplied against the modifier
     */
    public DropRateModifier(final DropRateModifierType type, final double value, final int level) {
        this.type = type;
        this.value = value;
        this.level = level;
    }
    
    /**
     * Constructs a modifier of a given type and value
     *
     * @param type the type of modifier
     * @param value the value of the modifier
     */
    public DropRateModifier(final DropRateModifierType type, final double value) {
        this(type, value, 1);
    }
    
    /**
     * Gets the effectiveness level of the modifier. Note: this only provides a
     * useful result with modifiers of type ADD_MULTIPLE_PER_LEVEL, otherwise it
     * will return 1.
     *
     * @return the effectiveness level of the modifier, or 1.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the type of modifier
     * @return the type of modifier
     */
    public DropRateModifierType getType() {
        return type;
    }

    /**
     * Returns the value or rate applied by the modifier
     * @return the value or rate applied by the modifier
     */
    public double getValue() {
        return value;
    }

    /**
     * Converts the modifier into an equivalent add-multiple value where
     * droprate*(1+newvalue) is equivalent to the current modifier effect. Note:
     * only other multipliers can be converted.
     *
     * @return the equivalent additive multiplier value, or null.
     */
    public Double getAdditiveMultiplierValue() {
        if (type == DropRateModifierType.MULTIPLY) {
            return value - 1.0;
        }
        if (type == DropRateModifierType.ADD_MULTIPLE) {
            return value;
        }
        if (type == DropRateModifierType.ADD_MULTIPLE_PER_LEVEL) {
            return value * level;
        }
        return null;
    }

    /**
     * Converts the modifier value to a multiplier (1.0=no effect) if possible.
     * Note: only additive multipliers can be converted to multipliers.
     *
     * @return the equivalent multiplier value, or null.
     */
    public Double getMultiplierValue() {
        if (type == DropRateModifierType.MULTIPLY) {
            return value;
        }
        if (type == DropRateModifierType.ADD_MULTIPLE) {
            return 1.0 + value;
        }
        if (type == DropRateModifierType.ADD_MULTIPLE_PER_LEVEL) {
            return 1.0 + value * level;
        }
        return null;
    }

    /**
     * Apply the droprate modifier to the droprate value
     *
     * @param droprate a droprate between 0 (0% chance) and 1.0 (100% chance).
     * Higher values also indicate 100% chance.
     * @return the effective droprate after modification
     */
    public double apply(final double droprate) {
        switch (type) {
            case MULTIPLY:
                return droprate * value;
            case ADD_MULTIPLE:
                return droprate * (1.0 + value);
            case ADD_MULTIPLE_PER_LEVEL:
                return droprate * (1.0 + value * level);
            case ADD_CONSTANT:
                return droprate + value;
            default:
                throw new IllegalArgumentException("An unsupported droprate modifier type was detected.");
        }
    }
}
