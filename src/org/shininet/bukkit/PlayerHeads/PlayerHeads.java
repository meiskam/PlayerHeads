package org.shininet.bukkit.PlayerHeads;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerHeads extends JavaPlugin {
	
	public final PlayerDeathListener playerDeathListener = new PlayerDeathListener();
	
	@Override
	public void onEnable(){
		getLogger().info(getDescription().getFullName()+" enabled");
		getServer().getPluginManager().registerEvents(playerDeathListener, this);
	}
 
	@Override
	public void onDisable() {
		getLogger().info(getDescription().getFullName()+" disabled");
		EntityDeathEvent.getHandlerList().unregister(playerDeathListener);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("PlayerHeads")) {
			sender.sendMessage("I see that you typed /"+label+", but this command currently does nothing.");
			return true;
		}
		return false; 
	}


}
