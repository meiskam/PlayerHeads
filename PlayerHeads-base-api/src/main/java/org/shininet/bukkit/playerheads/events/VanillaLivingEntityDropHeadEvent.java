/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shininet.bukkit.playerheads.events;

import java.util.List;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Event created by PlayerHeads when a [living] entity drops a head from a source outside of PlayerHeads.
 * Note: this event does not currently check if the entity dropped the item from their inventory/equipment, or from an actual head drop.
 * @author crashdemons (crashenator at gmail.com)
 * @since 5.2.14-SNAPSHOT
 */
public class VanillaLivingEntityDropHeadEvent extends EntityEvent implements Cancellable{
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean canceled = false;
    private final List<ItemStack> itemDrops;
    private final Event eventCause;
    private final LivingEntity killerEntity;
    
    /**
     * Creates the event
     * @param cause the event that caused this one, or null.
     * @param entity the entity that dropped a head
     * @param killer the entity that caused the beheading, as determined by PlayerHeads.
     * @param drops the head items dropped.
     */
    public VanillaLivingEntityDropHeadEvent(Event cause, LivingEntity entity, LivingEntity killer, List<ItemStack> drops){
        super(entity);
        eventCause = cause;
        itemDrops = drops;
        killerEntity = killer;
    }
    
    /**
     * Whether the event has been cancelled.
     *
     * @return Whether the event has been cancelled.
     */
    @Override
    public boolean isCancelled() {
        return canceled;
    }

    /**
     * Sets whether the event should be cancelled.
     *
     * @param cancel whether the event should be cancelled.
     */
    @Override
    public void setCancelled(final boolean cancel) {
        canceled = cancel;
    }
    
    /**
     * The event which inevitably triggered the beheading event (usually EntityDeathEvent)
     * @return the event, or null.
     * @since 5.2.14-SNAPSHOT
     */
    @Nullable
    public Event getCause(){
        return eventCause;
    }
    
    /**
     * Gets the items that will drop from the beheading.
     * Note: changing this list will not impact dropped items, you must interact with the event through getCause().
     * @return ItemStacks that will drop into the world once this event is over
     */
    @SuppressWarnings("unused")
    public List<ItemStack> getDrops() {
        return itemDrops;
    }
    
    /**
     * Gets the entity that was beheaded
     *
     * @return the beheaded entity
     */
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) entity;
    }
    
    /**
     * The entity that is responsible for the beheading, as determined by PlayerHeads.
     * It is possible that this differs from getEntity().getKiller() because it can identify non-player killers
     * and search projectile and tame ownership, if configured.
     * 
     * Use getEntity().getKiller() if you want the killer as determined by Minecraft.
     * @return the killerEntity's entity
     * @since 5.2.14-SNAPSHOT
     */
    @Nullable
    public LivingEntity getKillerEntity(){
        return killerEntity;
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
