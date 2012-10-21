package org.shininet.bukkit.PlayerHeads;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class PlayerDeathListener implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			((Player)(event.getEntity())).sendMessage("Hehe, you died");
			//PlayerHeads.getHead(((Player)(event.getEntity())).getName());
			event.getDrops().add(PlayerHeads.getHead(((Player)(event.getEntity())).getName()));
		}
	}
}
