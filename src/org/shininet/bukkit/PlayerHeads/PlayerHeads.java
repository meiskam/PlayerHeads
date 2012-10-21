package org.shininet.bukkit.PlayerHeads;

import net.minecraft.server.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerHeads extends JavaPlugin {
	
	public final PlayerDeathListener playerDeathListener = new PlayerDeathListener();
	
	@Override
	public void onEnable(){
		//getLogger().info(getDescription().getFullName()+" enabled");
		getServer().getPluginManager().registerEvents(playerDeathListener, this);
	}
 
	@Override
	public void onDisable() {
		//getLogger().info(getDescription().getFullName()+" disabled");
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

	public static CraftItemStack getHead(String playername) {
		CraftItemStack head = new CraftItemStack(Material.getMaterial(276),1,(short)3);
		NBTTagCompound headNBT = new NBTTagCompound();
		headNBT.setString("SkullOwner", playername);
		head.getHandle().tag = headNBT;
		return head;
	}

}
