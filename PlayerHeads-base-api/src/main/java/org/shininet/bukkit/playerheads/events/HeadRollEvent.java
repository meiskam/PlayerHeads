/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.playerheads.events;

import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shininet.bukkit.playerheads.events.modifiers.DropRateModifier;
import org.shininet.bukkit.playerheads.events.modifiers.DropRateModifierType;

/**
 * Event created by PlayerHeads (4.9.2+) to indicate that a head dropchance roll
 * has occurred and the success/failure has been determined. This event allows
 * third-party plugin authors to analyze and modify drop chance success with all
 * factors considered by PlayerHeads available. If the success of this event is
 * set to false, no head will be dropped. If it is set to true, a head will be
 * dropped.
 *
 * @since 4.9.2-SNAPSHOT
 * @author crashdemons (crashenator at gmail.com)
 */
public class HeadRollEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final LinkedHashMap<String, DropRateModifier> modifiers = new LinkedHashMap<>();

    private final Entity killer;
    private final Entity target;

    private final boolean killerAlwaysBeheads;

    private final double originalDropRoll;
    private double effectiveDropRoll;
    private final double originalDropRate;
    private double effectiveDropRate;

    private boolean dropSuccess;

    /**
     * Creates the Head dropchance event for PlayerHeads without precalculation,
     * allowing for event-based recalculation. Success is disabled by default.
     *
     * Note: this method does not add any modifier values by default.
     *
     * 5.2.2+ API
     *
     * @since 5.2.16-SNAPSHOT
     *
     * @param killer the Entity beheading another
     * @param target the Entity being beheaded
     * @param killerAlwaysBeheads whether the killer has the always-behead
     * permission
     * @param originalDropRoll the randomized PRNG double droproll value
     * inclusively between 0 to 1. logic was applied (alwaysbehead sets to 0)
     * @param originalDropRate the configured droprate of the target as a
     * fraction (0.01 = 1%)
     */
    public HeadRollEvent(final Entity killer, final Entity target, final boolean killerAlwaysBeheads, final double originalDropRoll, final double originalDropRate) {
        this.originalDropRate = originalDropRate;
        this.effectiveDropRate = originalDropRate;
        this.dropSuccess = false;
        this.effectiveDropRoll = originalDropRoll;
        this.originalDropRoll = originalDropRoll;
        this.killerAlwaysBeheads = killerAlwaysBeheads;

        this.killer = killer;
        this.target = target;
    }

    /**
     * Creates the Head dropchance event for PlayerHeads with values
     * precalaculated by the plugin.
     *
     * Note: this method does not add any modifier values by default.
     *
     * 5.2.2+ API
     *
     * @since 5.2.16-SNAPSHOT
     *
     * @param killer the Entity beheading another
     * @param target the Entity being beheaded
     * @param killerAlwaysBeheads whether the killer has the always-behead
     * permission
     * @param originalDropRoll the randomized PRNG double droproll value
     * inclusively between 0 to 1.
     * @param effectiveDropRoll the modified droproll value after permission
     * logic was applied (alwaysbehead sets to 0)
     * @param originalDropRate the configured droprate of the target as a
     * fraction (0.01 = 1%)
     * @param effectiveDropRate the effective droprate of the target as a
     * fraction (0.01 = 1%), as modified by looting.
     * @param dropSuccess whether the droproll was determined to be initially a
     * successful roll.
     */
    public HeadRollEvent(final Entity killer, final Entity target, final boolean killerAlwaysBeheads, final double originalDropRoll, final double effectiveDropRoll, final double originalDropRate, final double effectiveDropRate, final boolean dropSuccess) {
        this.originalDropRate = originalDropRate;
        this.effectiveDropRate = effectiveDropRate;
        this.dropSuccess = dropSuccess;
        this.effectiveDropRoll = effectiveDropRoll;
        this.originalDropRoll = originalDropRoll;
        this.killerAlwaysBeheads = killerAlwaysBeheads;

        this.killer = killer;
        this.target = target;
    }

    /**
     * Creates the Head dropchance event for PlayerHeads.
     *
     * 5.2+ API
     *
     * @since 5.2.0-SNAPSHOT
     *
     * @param killer the Entity beheading another
     * @param target the Entity being beheaded
     * @param killerAlwaysBeheads whether the killer has the always-behead
     * permission
     * @param originalDropRoll the randomized PRNG double droproll value
     * inclusively between 0 to 1.
     * @param slimeModifier the fraction of the slime/magmacube drop rate that
     * is applicable at this size, modifuing the effective droprate (0.5 is 50%
     * of the base rate). This should be 1.0 when there is no effect or the
     * entity is not a slime.
     * @param lootingModifier the fractional probability modifier (greater than
     * or equal to 1.0) of looting, as applied by PlayerHeads to the effective
     * droprate.
     * @param chargedCreeperModifier the multiplier effect that charged creepers
     * have on normal droprates
     * @param effectiveDropRoll the modified droproll value after permission
     * logic was applied (alwaysbehead sets to 0)
     * @param originalDropRate the configured droprate of the target as a
     * fraction (0.01 = 1%)
     * @param effectiveDropRate the effective droprate of the target as a
     * fraction (0.01 = 1%), as modified by looting.
     * @param dropSuccess whether the droproll was determined to be initially a
     * successful roll.
     */
    @Deprecated
    public HeadRollEvent(final Entity killer, final Entity target, final boolean killerAlwaysBeheads, final double lootingModifier, final double slimeModifier, final double chargedCreeperModifier, final double originalDropRoll, final double effectiveDropRoll, final double originalDropRate, final double effectiveDropRate, final boolean dropSuccess) {

        this.originalDropRate = originalDropRate;
        this.effectiveDropRate = effectiveDropRate;
        this.dropSuccess = dropSuccess;
        this.effectiveDropRoll = effectiveDropRoll;
        this.originalDropRoll = originalDropRoll;
        this.killerAlwaysBeheads = killerAlwaysBeheads;
        setModifier("looting", new DropRateModifier(DropRateModifierType.MULTIPLY, lootingModifier));
        setModifier("slime", new DropRateModifier(DropRateModifierType.MULTIPLY, slimeModifier));
        setModifier("chargedcreeper", new DropRateModifier(DropRateModifierType.MULTIPLY, chargedCreeperModifier));

        this.killer = killer;
        this.target = target;
    }

    /**
     * Creates the Head dropchance event for PlayerHeads.
     *
     * @since 5.1.0-SNAPSHOT
     * @param killer the Entity beheading another
     * @param target the Entity being beheaded
     * @param killerAlwaysBeheads whether the killer has the always-behead
     * permission
     * @param originalDropRoll the randomized PRNG double droproll value
     * inclusively between 0 to 1.
     * @param slimeModifier the fraction of the slime/magmacube drop rate that
     * is applicable at this size, modifuing the effective droprate (0.5 is 50%
     * of the base rate). This should be 1.0 when there is no effect or the
     * entity is not a slime.
     * @param lootingModifier the fractional probability modifier (greater than
     * or equal to 1.0) of looting, as applied by PlayerHeads to the effective
     * droprate.
     * @param effectiveDropRoll the modified droproll value after permission
     * logic was applied (alwaysbehead sets to 0)
     * @param originalDropRate the configured droprate of the target as a
     * fraction (0.01 = 1%)
     * @param effectiveDropRate the effective droprate of the target as a
     * fraction (0.01 = 1%), as modified by looting.
     * @param dropSuccess whether the droproll was determined to be initially a
     * successful roll.
     */
    @Deprecated
    public HeadRollEvent(final Entity killer, final Entity target, final boolean killerAlwaysBeheads, final double lootingModifier, final double slimeModifier, final double originalDropRoll, final double effectiveDropRoll, final double originalDropRate, final double effectiveDropRate, final boolean dropSuccess) {
        this.originalDropRate = originalDropRate;
        this.effectiveDropRate = effectiveDropRate;
        this.dropSuccess = dropSuccess;
        this.effectiveDropRoll = effectiveDropRoll;
        this.originalDropRoll = originalDropRoll;
        this.killerAlwaysBeheads = killerAlwaysBeheads;

        setModifier("looting", new DropRateModifier(DropRateModifierType.MULTIPLY, lootingModifier));
        setModifier("slime", new DropRateModifier(DropRateModifierType.MULTIPLY, slimeModifier));

        this.killer = killer;
        this.target = target;
    }

    /**
     * Creates the Head dropchance event for PlayerHeads.
     *
     * @param killer the Entity beheading another
     * @param target the Entity being beheaded
     * @param killerAlwaysBeheads whether the killer has the always-behead
     * permission
     * @param originalDropRoll the randomized PRNG double droproll value
     * inclusively between 0 to 1.
     * @param lootingModifier the fractional probability modifier (greater than
     * or equal to 1.0) of looting, as applied by PlayerHeads to the effective
     * droprate.
     * @param effectiveDropRoll the modified droproll value after permission
     * logic was applied (alwaysbehead sets to 0)
     * @param originalDropRate the configured droprate of the target as a
     * fraction (0.01 = 1%)
     * @param effectiveDropRate the effective droprate of the target as a
     * fraction (0.01 = 1%), as modified by looting.
     * @param dropSuccess whether the droproll was determined to be initially a
     * successful roll.
     */
    @Deprecated
    public HeadRollEvent(final Entity killer, final Entity target, final boolean killerAlwaysBeheads, final double lootingModifier, final double originalDropRoll, final double effectiveDropRoll, final double originalDropRate, final double effectiveDropRate, final boolean dropSuccess) {
        this.originalDropRate = originalDropRate;
        this.effectiveDropRate = effectiveDropRate;
        this.dropSuccess = dropSuccess;
        this.effectiveDropRoll = effectiveDropRoll;
        this.originalDropRoll = originalDropRoll;
        this.killerAlwaysBeheads = killerAlwaysBeheads;
        setModifier("looting", new DropRateModifier(DropRateModifierType.MULTIPLY, lootingModifier));

        this.killer = killer;
        this.target = target;
    }

    /**
     * Gets the list of modifiers to the effective droprate. This map will be in
     * order that the modifiers are applied.
     *
     * @since 5.2.16-SNAPSHOT
     * @return map containing the droprate modifiers by name.
     */
    @NotNull
    public Map<String, DropRateModifier> getModifiers() {
        return modifiers;
    }

    /**
     * Re-apply the current effective droproll and effective droprate values to
     * make a new determination of the head drop's success. Modifiers are not
     * considered by this method, only the two effective values. Note: if
     * killerAlwaysBeheads is enabled, the effective droproll will be set to 0.
     *
     * @since 5.2.16-SNAPSHOT
     */
    public void applyDropRate() {
        if (killerAlwaysBeheads) {
            effectiveDropRoll = 0;
        }
        this.setDropSuccess(effectiveDropRoll < effectiveDropRate);
    }

    /**
     * Re-apply all droprate modifiers to the original droprate and recalculate
     * the effective droprate. This method will discard the current effective
     * droprate, if you want to retain the original values, you should copy them
     * before calling this method. Success is not updated by this method.
     *
     * @since 5.2.16-SNAPSHOT
     */
    public void applyModifiers() {
        effectiveDropRate = originalDropRate;
        for (DropRateModifier modifier : modifiers.values()) {
            effectiveDropRate = modifier.apply(effectiveDropRate);
        }
    }

    /**
     * Re-apply all factors (droprate modifiers then effective values) to
     * determine suuccess. This method will discard the current effective
     * droprate, if you want to retain the original values, you should copy them
     * before calling this method.
     *
     * @since 5.2.16-SNAPSHOT
     */
    public void recalculateSuccess() {
        applyModifiers();
        applyDropRate();
    }

    /**
     * Retrieve the value of a modifier of the effective droprate. Note: this
     * value does not impact calculations or success and is for you to use as a
     * courtesy to other plugins at this point. This method can retrieve both
     * internal and custom plugin modifiers (if the prefix is included).
     *
     * @since 5.2.16-SNAPSHOT
     * @param modifierName the name of the modifier
     * @return the value of the modifier, or null if it is not present.
     */
    @Nullable
    public DropRateModifier getModifier(final String modifierName) {
        return modifiers.get(modifierName);
    }

    /**
     * Sets a note about an internal modifier of the effective droprate. Note:
     * this value does not impact calculations or success unless applyModifiers
     * is called Note: new modifies are generally applied AFTER other
     * modifiers<br>
     *
     * @deprecated using this method to modify existing modifiers should be
     * avoided - use setCustomModifier to note new ones.
     * @since 5.2.16-SNAPSHOT
     * @param modifierName the name of the modifier to set.
     * @param value the value of the modifier to set
     */
    public void setModifier(final String modifierName, final DropRateModifier value) {
        modifiers.put(modifierName, value);
    }

    /**
     * Replaces notes about an internal modifiers of the effective droprate.
     * Note: this value does not impact calculations or success unless
     * applyModifiers is called Note: new modifies are generally applied AFTER
     * other modifiers; this method will overwrite existing modifiers.<br>
     *
     * @deprecated using this method to modify existing modifiers should be
     * avoided - use setCustomModifier to note new ones.
     * @since 5.2.16-SNAPSHOT
     * @param entries the modifiers to set
     */
    public void setModifiers(final Map<String, DropRateModifier> entries) {
        modifiers.clear();
        modifiers.putAll(entries);
    }

    /**
     * Constructs the internal name of a custom droprate modifier, provided the
     * name of the plugin and modifier.
     *
     * @since 5.2.16-SNAPSHOT
     * @param pluginName The name of the plugin that added the modifier
     * @param modifierName The name of the modifier
     * @return the internal name of the modifier;
     */
    public static String getCustomModifierName(final String pluginName, final String modifierName) {
        return pluginName + ":" + modifierName;
    }

    /**
     * Add or change a note about your custom modifier to the head-roll event.
     * Note: this value does not impact calculations or success unless
     * applyModifiers is called.<br>
     * Note: the name of the modifier will be prepended with "PluginName:"
     * depending on your plugin's name.<br>
     * Note: new modifies are generally applied AFTER other modifiers<br>
     *
     * @since 5.2.16-SNAPSHOT
     * @param yourPlugin the plugin adding the modifier
     * @param modifierName the name of the modifier, excluding any prefix
     * @param modifierValue the value of the modifier
     */
    public void setCustomModifier(final Plugin yourPlugin, final String modifierName, final DropRateModifier modifierValue) {
        String customModifierName = getCustomModifierName(yourPlugin.getName(), modifierName);
        modifiers.put(customModifierName, modifierValue);
    }

    /**
     * Gets a custom (plugin-added) modifier to the head-roll event. Note: this
     * value does not impact calculations or success and is for you to use as a
     * courtesy to other plugins at this point. Note: the name of the modifier
     * will be prepended with "PluginName:" depending on your plugin's name.
     *
     * @since 5.2.16-SNAPSHOT
     * @param yourPlugin the plugin which added the modifier
     * @param modifierName the name of the modifier, excluding any prefix
     * @return the value of the modifier, or the null if it is not found.
     */
    public DropRateModifier getCustomModifier(final Plugin yourPlugin, final String modifierName) {
        return getCustomModifier(yourPlugin.getName(), modifierName);
    }

    /**
     * Gets a custom (plugin-added) modifier to the head-roll event. Note: this
     * value does not impact calculations or success and is for you to use as a
     * courtesy to other plugins at this point. Note: the name of the modifier
     * will be prepended with "PluginName:" depending on your plugin's name.
     *
     * @since 5.2.16-SNAPSHOT
     * @param yourPluginName the plugin name which added the modifier
     * @param modifierName the name of the modifier, excluding any prefix
     * @return the value of the modifier, or the null if it is not found.
     */
    public DropRateModifier getCustomModifier(final String yourPluginName, final String modifierName) {
        String customModifierName = getCustomModifierName(yourPluginName, modifierName);
        return getModifier(customModifierName);
    }

    /**
     * Gets the charged creeper modifier (multiplier) that modified the
     * effective droprate. Generally this is 1 (no effect) when the mob was not
     * detected to be killed by a charged creeper.
     *
     * 5.2+ API
     *
     * @since 5.2.0-SNAPSHOT
     * @deprecated use getModifier("chargedcreeper") instead
     * @see #getModifier(java.lang.String) 
     * @return the multiplier
     */
    @NotNull
    public double getChargedCreeperModifier() {
        DropRateModifier modifier = getModifier("chargedcreeper");
        if (modifier == null) {
            return 1;
        }
        return modifier.getMultiplierValue();
    }

    /**
     * Gets the slime/magmacube size modifier (multiplier) that modified the
     * effective droprate. Generally this is 1 (no effect) when not a slime.
     *
     * @since 5.1.0-SNAPSHOT
     * @deprecated use getModifier("slime") instead
     * @see #getModifier(java.lang.String)
     * @return the looting modifier
     */
    @NotNull
    public double getSlimeModifier() {
        DropRateModifier modifier = getModifier("slime");
        if (modifier == null) {
            return 1;
        }
        return modifier.getMultiplierValue();
    }

    /**
     * Gets the effective looting modifier (multiplier) that modified the
     * effective droprate. Generally this is 1 (no effect) or greater.
     *
     * Note: lootingmodifier = (1 + Config_lootingrate *
     * Entity_Looting_Enchantment_Level)
     *
     * @deprecated use getModifier("looting") instead
     * @see #getModifier(java.lang.String)
     * @return the looting modifier
     */
    public double getLootingModifier() {
        DropRateModifier modifier = getModifier("looting");
        if (modifier == null) {
            return 1;
        }
        return modifier.getMultiplierValue();
    }

    /**
     * Get the Killer's entity that may have done the beheading.
     *
     * @return the entity of the killer, or null if the killer was a mob.
     */
    public Entity getKiller() {
        return killer;
    }

    /**
     * Get the Target's entity that may have been beheaded
     *
     * @return the entity of the target
     */
    public Entity getTarget() {
        return target;
    }

    /**
     * Gets whether the killer was configured to always behead this type of
     * target. Note: this may differ on whether the target was a player or mob.
     * If this is true, the effective droproll may have been set to 0 to force
     * success.
     *
     * @return Whether the killer was configured to always behead
     */
    public boolean getKillerAlwaysBeheads() {
        return killerAlwaysBeheads;
    }

    /**
     * Gets the original PRNG-generated random value of the drop roll, uniform
     * between 0 and 1 inclusively. When this value is lower than the droprate
     * value by chance, the roll is considered successful.
     *
     * @return the drop roll value in the range [0,1]
     */
    public double getOriginalDropRoll() {
        return originalDropRoll;
    }

    /**
     * Sets the effective droproll value for the event. Note: this value will
     * not impact the success value or calculations unless applyDropRate() or
     * recalculateSuccess() is called.
     *
     * @since 5.2.16-SNAPSHOT
     * @param effectiveRoll the value between 0.0 and 1.0 to use as the drop
     * roll.
     */
    public void setEffectiveDropRoll(final double effectiveRoll) {
        this.effectiveDropRoll = effectiveRoll;
    }

    /**
     * Sets the effective droprate value for the event. Note: this value is only
     * for indication purposes to other plugins, and will be overwritten by
     * apply and recalculate methods.
     *
     * @since 5.2.16-SNAPSHOT
     * @param effectiveRate the effective droprate/fractional-chance value to set (0.0-1.0 inclusive)
     */
    public void setEffectiveDropRate(final double effectiveRate) {
        this.effectiveDropRate = effectiveRate;
    }

    /**
     * Gets the effective drop roll value after modification by PlayerHeads. The
     * droproll will normally be reflected by the original random droproll,
     * except if the killer always beheads, then this may be 0. If this is below
     * the droprate, the roll would have been determined to be a success.
     *
     * @return the effective drop roll.
     * @see #getOriginalDropRoll
     */
    public double getEffectiveDropRoll() {
        return effectiveDropRoll;
    }

    /**
     * Gets the configured droprate for the target as a fractional probability,
     * unmodified.
     *
     * @return the droprate
     */
    public double getOriginalDropRate() {
        return originalDropRate;
    }

    /**
     * Gets the configured droprate for the target as a fractional probability,
     * after modification by looting and slime size modifier.
     *
     * Note: effectiveDroprate = originalDropRate * lootingModifier *
     * slimeModifier.
     *
     * @return the droprate
     */
    public double getEffectiveDropRate() {
        return effectiveDropRate;
    }

    /**
     * Whether the effective drop roll was determined to be a success.
     *
     * @return the success of the drop roll
     */
    public boolean getDropSuccess() {
        return dropSuccess;
    }

    /**
     * Sets whether the drop roll should be considered a success.
     *
     * @param value whether the head drop should succeed or fail.
     */
    public void setDropSuccess(final boolean value) {
        dropSuccess = value;
    }

    /**
     * Whether the effective drop roll was determined to be a success. Alias of
     * getDropSuccess
     *
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
        return HANDLERS;
    }

    /**
     * Get a list of handlers for the event.
     *
     * @return a list of handlers for the event
     */
    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
