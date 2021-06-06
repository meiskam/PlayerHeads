/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shininet.bukkit.playerheads.events;

import java.util.Random;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class PHListenerDummy {
    
    private final Random prng = new Random();
    
    /**
     * A copy of PHListener drop helper code from 5.2.15, modified to generate two objects: the old 5.2.15 HeadRollEvent, and a new 5.3/5.2.16 HeadRollEvent.
     * This is for the purpose of testing output.
     * @param event
     * @param killerEntity
     * @param type
     * @param droprateOriginal
     * @param lootingModifier
     * @param chargedcreeperModifier
     * @return an array containing two elements: HeadRollEvent_old (5.2.15 event), HeadRollEvent (5.2.16/5.3 code)
     */
     public Object[] MobDeathHelper(Object event, LivingEntity killerEntity, Object type, Double droprateOriginal, Double lootingModifier, Double slimeModifier, Double chargedcreeperModifier) {
        Double droprate = droprateOriginal * lootingModifier * slimeModifier * chargedcreeperModifier;
        Double dropchanceRand = prng.nextDouble();
        Double dropchance = dropchanceRand;
        Entity entity = null;//event.getEntity();
        Player killer = killerEntity instanceof Player ? (Player)killerEntity : null;
        
        boolean killerAlwaysBeheads = false;
        /*if (killer != null) {//mob was PK'd
            if (!killer.hasPermission("playerheads.canbeheadmob")) {
                return null;//killer does not have permission to behead mobs in any case
            }
            killerAlwaysBeheads = killer.hasPermission("playerheads.alwaysbeheadmob");
            if (killerAlwaysBeheads) {
                dropchance = 0.0;//alwaysbehead should only modify drop chances
            }
        } else {//mob was killed by mob
            if (plugin.configFile.getBoolean("mobpkonly")) {
                return;//mobs must only be beheaded by players
            }
        }*/
        boolean headDropSuccess = dropchance < droprate;

         HeadRollEvent_old old_rollEvent = new HeadRollEvent_old(killer, null, killerAlwaysBeheads, lootingModifier, slimeModifier, chargedcreeperModifier, dropchanceRand, dropchance, droprateOriginal, droprate, headDropSuccess);
         HeadRollEvent new_rollEvent = new HeadRollEvent(killer, null, killerAlwaysBeheads, lootingModifier, slimeModifier, chargedcreeperModifier, dropchanceRand, dropchance, droprateOriginal, droprate, headDropSuccess);
        
            Object[] arr = new Object[]{ old_rollEvent, new_rollEvent };
            return arr;

//plugin.getServer().getPluginManager().callEvent(rollEvent);
            //if (!rollEvent.succeeded()) {
            //    return;//allow plugins a chance to modify the success
            //}
            
    }
}
