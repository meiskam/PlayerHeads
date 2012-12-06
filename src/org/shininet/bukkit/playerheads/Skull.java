/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.TileEntity;

/**
* @author meiskam
*/

public class Skull {
	public String skullOwner;
	public String name;
	public ArrayList<String> lore;
	public NBTTagList ench;
	public Integer repairCost;
	public Integer skullType; //skull item's damage value
	public Integer rotation1; //skull block's damage value
	public Integer rotation2; //tile entities rotation value 

	public Skull(String skullOwner) {
		this.skullOwner = skullOwner;
		skullType = 3;
	}

	public Skull(NBTTagCompound skullNBT) {
		fakeConstructor(skullNBT);
	}
	
	public Skull(NBTTagCompound skullNBT, int skullType) {
		this(skullNBT);
		this.skullType = skullType;
	}
	
	public Skull(CraftItemStack skull) {
		NBTTagCompound skullNBT = skull.getHandle().tag;
		if (skullNBT != null) {
			fakeConstructor(skullNBT);
		}
		this.skullType = (int)(skull.getDurability());
	}
	
	public Skull(ItemStack skull) {
		this((CraftItemStack)skull);
	}
	
	public Skull(TileEntity skullTE) {
		if (skullTE != null) {
			NBTTagCompound skullNBT = new NBTTagCompound();
			skullTE.b(skullNBT); // copies the TE's NBT data into blockNBT
			fakeConstructor(skullNBT);
		}
	}
	
	@Deprecated
	public Skull(TileEntity skullTE, int skullType) {
		this(skullTE);
		this.skullType = skullType;
	}
	
	public Skull(Location location) {
		this(((CraftWorld)location.getWorld()).getTileEntityAt(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
		rotation1 = (int)location.getBlock().getData();
	}
	
	private void fakeConstructor(NBTTagCompound skullNBT) {
		if (skullNBT.hasKey("ench")) {
			ench = skullNBT.getList("ench");
		}
		if (skullNBT.hasKey("display")) {
			NBTTagCompound skullNBTdisplay = skullNBT.getCompound("display");
			if (skullNBTdisplay.hasKey("Name")) {
				name = skullNBTdisplay.getString("Name");
			}
			if (skullNBTdisplay.hasKey("Lore")) {
				lore = Utils.NBTList2ArrayList(skullNBTdisplay.getList("Lore"));
			}
		}
		if (skullNBT.hasKey("SkullOwner")) {
			skullOwner = skullNBT.getString("SkullOwner");
		} else if (skullNBT.hasKey("ExtraType")) {
			skullOwner = skullNBT.getString("ExtraType");
		}
		if (skullNBT.hasKey("SkullType")) {
			skullType = (int)skullNBT.getByte("SkullType");
		}
		if (skullNBT.hasKey("Rot")) {
			rotation2 = (int)skullNBT.getByte("Rot");
		}
		if (skullNBT.hasKey("RepairCost")) {
			repairCost = skullNBT.getInt("RepairCost");
		}
	}
	
	public boolean hasTag() {
		return (hasOwner() || hasName() || hasLore() || hasEnch() || hasRepairCost());
	}

	public boolean hasOwner() {
		if (skullOwner == null || skullOwner.equals("")) {
			return false;
		}
		return true;
	}
	
	public boolean hasName() {
		if (name == null || name.equals("")) {
			return false;
		}
		return true;
	}
	
	public boolean hasLore() {
		if (lore == null || lore.size() == 0) {
			return false;
		}
		return true;
	}

	public boolean hasEnch() {
		if (ench == null || ench.size() == 0) {
			return false;
		}
		return true;
	}
	
	public boolean hasRepairCost() {
		if (repairCost == null) {
			return false;
		}
		return true;
	}
	
	public CraftItemStack getItemStack() {
		CraftItemStack skull;
		try {
			skull = new CraftItemStack(Material.SKULL_ITEM,1,(short)3);
		} catch (NullPointerException e) {
			//getLogger().warning("It seems you're not using CraftBukkit 1.4 or above .. falling back to a leather helm");
			//head = new CraftItemStack(Material.LEATHER_HELMET,1,(short)55);
			return null;
		}
		NBTTagCompound skullNBT = getNBT(false);
		if (skullNBT != null) {
			skull.getHandle().tag = skullNBT;
		}
		return skull;
	}
	
	public NBTTagCompound getNBT() {
		return getNBT(false);
	}

	public NBTTagCompound getNBT(boolean isTileEntity) {
		if (hasTag()) {
			NBTTagCompound skullNBT = new NBTTagCompound();
			return addNBT(skullNBT, isTileEntity);
		}
		return null;
	}
	
	public NBTTagCompound addNBT(NBTTagCompound skullNBT) {
		return addNBT(skullNBT, false);
	}

	public NBTTagCompound addNBT(NBTTagCompound skullNBT, boolean isTileEntity) {
		if (hasEnch()) {
			skullNBT.set("ench", ench);
		}
		if (hasName() || hasLore()) {
			NBTTagCompound skullNBTdisplay = new NBTTagCompound();
			if (hasName()) {
				skullNBTdisplay.setString("Name", name);
			}
			if (hasLore()) {
				skullNBTdisplay.set("Lore", Utils.ArrayList2NBTList(lore));
			}
			skullNBT.setCompound("display", skullNBTdisplay);
		}
		if (hasRepairCost()) {
			skullNBT.setInt("RepairCost", repairCost);
		}
		if (isTileEntity) {
			if (hasOwner()) {
				skullNBT.setString("ExtraType", skullOwner);
			}
			if (skullType != null) {
				skullNBT.setByte("SkullType", (byte)(int)skullType);
			}
			if (rotation2 != null) {
				skullNBT.setByte("Rot", (byte)(int)rotation2);
			}
		} else {
			if (hasOwner()) {
				skullNBT.setString("SkullOwner", skullOwner);
			}
		}
		return skullNBT;
	}
	
	public void place(Location location) {
		Block block = location.getWorld().getBlockAt(location);
		block.setType(Material.SKULL);
		if (rotation1 != null) {
			block.setData((byte)(int)rotation1);
		}
		TileEntity blockTE = ((CraftWorld)location.getWorld()).getTileEntityAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		if (blockTE != null) {
			NBTTagCompound blockNBT = new NBTTagCompound();
			blockTE.b(blockNBT); //copy the block's TE into blockNBT
			blockTE.a(addNBT(blockNBT,true)); //add pertaining details to blockNBT then set the block's TE to blockNBT
		}
	}
	
	public static CraftItemStack getItemStack(int damage) {
		try {
			return new CraftItemStack(Material.SKULL_ITEM,1,(short)damage);
		} catch (NullPointerException e) {
			return null;
		}
	}

}