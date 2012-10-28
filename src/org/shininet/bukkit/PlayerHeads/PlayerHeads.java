/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.PlayerHeads;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.server.EntityItem;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerHeads extends JavaPlugin implements Listener {
	
	private Random prng = new Random();
	private ArrayList<Location> breaklist;
	
	@Override
	public void onEnable(){
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		breaklist = new ArrayList<Location>();
		getServer().getPluginManager().registerEvents((Listener)this, this);
	}
 
	@Override
	public void onDisable() {
		EntityDeathEvent.getHandlerList().unregister((Listener)this);
		BlockBreakEvent.getHandlerList().unregister((Listener)this);
		ItemSpawnEvent.getHandlerList().unregister((Listener)this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("PlayerHeads")) {
			if (args.length == 0) {
				sender.sendMessage("["+label+"] Subcommands: config, spawn");
			} else {
				if (args[0].equalsIgnoreCase("config")) {
					if (args.length == 1) {
						sender.sendMessage("["+label+":config] Subcommands: get, set, reload");
					} else {
						if (args[1].equalsIgnoreCase("get") || args[1].equalsIgnoreCase("view")) {
							if (sender.hasPermission("playerheads.config.get")) {
								if (args.length == 2) {
									sender.sendMessage("["+label+":config:get] Config variables: DropRate, PKOnly, HookBreak");
								} else if (args.length == 3) {
									sender.sendMessage("["+label+":config:get] "+args[2].toLowerCase()+": "+getConfig().get(args[2].toLowerCase()));
								} else {
									sender.sendMessage("["+label+":config:get] Too many params!");
								}
							} else {
								sender.sendMessage("["+label+":config:get] You don't have permission to use that command");
							}
						} else if (args[1].equalsIgnoreCase("set")) {
							if (sender.hasPermission("playerheads.config.set")) {
								if (args.length == 2 || args.length == 3) {
									sender.sendMessage("["+label+":config:set] Config variables: DropRate, PKOnly, HookBreak");
								} else if (args.length == 4) {
									if (args[2].equalsIgnoreCase("droprate")) {
										try {
											getConfig().set("droprate", Double.parseDouble(args[3]));
										} catch (NumberFormatException e) {
											sender.sendMessage("["+label+":config:set] ERROR: Can not convert "+args[3].toLowerCase()+" to a number");
										}
									} else if (args[2].equalsIgnoreCase("pkonly")) {
										if (args[3].equalsIgnoreCase("false") || args[3].equalsIgnoreCase("no") || args[3].equalsIgnoreCase("0")) {
											getConfig().set("pkonly", false);
										} else {
											getConfig().set("pkonly", true);
										}
									} else if (args[2].equalsIgnoreCase("HookBreak")) {
										if (args[3].equalsIgnoreCase("false") || args[3].equalsIgnoreCase("no") || args[3].equalsIgnoreCase("0")) {
											getConfig().set("hookbreak", false);
										} else {
											getConfig().set("hookbreak", true);
										}
									}
									else {
										getConfig().set(args[2].toLowerCase(), args[3]);
									}
									saveConfig();
									sender.sendMessage("["+label+":config:set] "+args[2].toLowerCase()+": "+getConfig().get(args[2].toLowerCase()));
								} else {
									sender.sendMessage("["+label+":config:set] Too many params!");
								}
							} else {
								sender.sendMessage("["+label+":config:set] You don't have permission to use that command");
							}
/*						} else if (args[1].equalsIgnoreCase("save")) {
							if (((Player)sender).hasPermission("playerheads.config.set")) {
								saveConfig();
							} else {
								sender.sendMessage("["+label+":config:save] You don't have permission to use that command");
							}
*/						} else if (args[1].equalsIgnoreCase("reload")) {
							if (sender.hasPermission("playerheads.config.set")) {
								reloadConfig();
								sender.sendMessage("["+label+":config:reload] Config reloaded");
							} else {
								sender.sendMessage("["+label+":config:reload] You don't have permission to use that command");
							}
						} else {
							sender.sendMessage("["+label+":config:??] Invalid subcommand");
						}
					}
				}
				else if (args[0].equalsIgnoreCase("spawn")) {
					if (!(sender instanceof Player)) {
						sender.sendMessage("["+label+":spawn] Sorry console, heads are for players!");
					}
					else {
						if (args.length == 1) {
							//spawn them their head
							if (sender.hasPermission("playerheads.spawn.own")) {
								if (addHead((Player)sender, ((Player)sender).getName())) {
									sender.sendMessage("["+label+":spawn] Spawned you "+((Player)sender).getName()+"'s head");
								} else {
									sender.sendMessage("["+label+":spawn] Well I can't very well give you an item if your inventory is full");
								}
							} else {
								sender.sendMessage("["+label+":spawn] You don't have permission to use that command");
							}
						} else if (args.length == 2) {
							//spawn them args[1]'s head
							if (sender.hasPermission("playerheads.spawn")) {
								if (addHead((Player)sender, args[1])) {
									sender.sendMessage("["+label+":spawn] Spawned you "+args[1]+"'s head");
								} else {
									sender.sendMessage("["+label+":spawn] Well I can't very well give you an item if your inventory is full");
								}
							} else {
								sender.sendMessage("["+label+":spawn] You don't have permission to use that command");
							}
						} else {
							sender.sendMessage("["+label+":spawn] Too many params!");
						}
					}
/*				} else if (args[0].equalsIgnoreCase("somethingelse")) {
					sender.sendMessage("["+label+":??] moo");
*/				} else {
					sender.sendMessage("["+label+":??] Invalid subcommand");
				}
			}
			return true;
		}
		return false; 
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			//((Player)(event.getEntity())).sendMessage("Hehe, you died");
			Double dropchance = prng.nextDouble();
			Player player = (Player)event.getEntity();
			LivingEntity killer = player.getKiller();

			if ((!(getConfig().getBoolean("pkonly", true)) // if pkonly's off, continue, don't check next 2 if lines
			 || (getConfig().getBoolean("pkonly", true) && (killer instanceof Player) 
				 && (killer != player) && ((Player)killer).hasPermission("playerheads.canbehead"))) // if pkonly's on AND killer is a Player AND killer != player, AND killer has permission to lop heads, continue
			 && (player.hasPermission("playerheads.canloosehead")) // if player has permission to drop his head when dies
			 && (dropchance <= getConfig().getDouble("droprate", 0.05))) { // check if it should drop via droprate
				event.getDrops().add(getHead(player.getName())); // drop the precious player head
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		if (!(event.isCancelled()) && event.getBlock().getType() == Material.SKULL && getConfig().getBoolean("hookbreak", true)) { // skull=144, sign=63
			Block block = event.getBlock();
			Location location = block.getLocation();
			CraftWorld world = (CraftWorld)block.getWorld();
			
			TileEntity tileEntity = world.getTileEntityAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
			NBTTagCompound blockNBT = new NBTTagCompound();
			
			tileEntity.b(blockNBT); // copies the TE's NBT data into blockNBT
			String skullname = blockNBT.getString("ExtraType");
			//String skullname = blockNBT.getString("Text1");
			if (!(skullname.equals(""))) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				dropItemNaturally(world, location, getHead(skullname));
			}
		}
	}
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onItemSpawn(ItemSpawnEvent event) { // workaround for the dupe bug
		if (!(event.isCancelled()) && getConfig().getBoolean("hookbreak", true)) {
			CraftItemStack itemStack = (CraftItemStack)event.getEntity().getItemStack();
			if (itemStack.getType() == Material.SKULL_ITEM && itemStack.getDurability() == (short)3 && 
					itemStack.getHandle().tag.getString("ExtraType").equals("")) {
				Location itemLocation = event.getEntity().getLocation();
				
				//breaklist.iterator()
				for (int i = 0; i < breaklist.size(); i++) {
					if (breaklist.get(i).distanceSquared(itemLocation) <= 2) {
						event.setCancelled(true);
						breaklist.remove(event.getEntity().getLocation());
						break;
					}
				}
			}
		}
	}
	//ItemSpawnEvent
	
	public CraftItemStack getHead(String headname) {
		CraftItemStack head;
		try {
			head = new CraftItemStack(Material.SKULL_ITEM,1,(short)3);
		} catch (NullPointerException e) {
			getLogger().warning("It seems you're not using CraftBukkit 1.4 or above .. falling back to a leather helm");
			head = new CraftItemStack(Material.LEATHER_HELMET,1,(short)55);
		}		
		NBTTagCompound headNBT = new NBTTagCompound();
		headNBT.setString("SkullOwner", headname);
		head.getHandle().tag = headNBT;
		return head;
	}
	
	public boolean addHead(Player player, String headname) {
		PlayerInventory inv = player.getInventory();
		int firstEmpty = inv.firstEmpty();
		if (firstEmpty == -1) {
			return false;
		} else {
			ItemStack[] invContents = inv.getContents();
			invContents[firstEmpty] = getHead(headname);
			inv.setContents(invContents);
			return true;
		}
	}

	public void dropItemNaturally(CraftWorld world, Location loc, CraftItemStack item) { //inspired by org.bukkit.craftbukkit.CraftWorld
		double xs = loc.getX() + (prng.nextFloat() * 0.7F + (1.0F - 0.7F) * 0.5D);
		double ys = loc.getY() + (prng.nextFloat() * 0.7F + (1.0F - 0.7F) * 0.5D);
		double zs = loc.getZ() + (prng.nextFloat() * 0.7F + (1.0F - 0.7F) * 0.5D);
		
		EntityItem entity = new EntityItem(world.getHandle(), xs, ys, zs, item.getHandle());
		entity.pickupDelay = 10;
		world.getHandle().addEntity(entity);
	}
}
