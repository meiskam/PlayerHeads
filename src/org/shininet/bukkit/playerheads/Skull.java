/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import org.bukkit.Material;
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
	public NBTTagList lore;
	public NBTTagList ench;
	public Integer repairCost;
	public Integer damage = 3;

	public Skull(String skullOwner) {
		this.skullOwner = skullOwner;
	}

	public Skull(NBTTagCompound skullNBT) {
		fakeConstructor(skullNBT);
	}
	
	public Skull(NBTTagCompound skullNBT, int damage) {
		this(skullNBT);
		this.damage = damage;
	}
	
	public Skull(CraftItemStack skull) {
		NBTTagCompound skullNBT = skull.getHandle().tag;
		if (skullNBT != null) {
			fakeConstructor(skullNBT);
		}
		this.damage = (int)(skull.getDurability());
	}
	
	public Skull(ItemStack skull) {
		this((CraftItemStack)skull);
	}
	
	public Skull(TileEntity skullTE) {
		NBTTagCompound skullNBT = new NBTTagCompound();
		skullTE.b(skullNBT); // copies the TE's NBT data into blockNBT
		fakeConstructor(skullNBT);
	}
	
	public Skull(TileEntity skullTE, int damage) {
		this(skullTE);
		this.damage = damage;
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
				lore = skullNBTdisplay.getList("Lore");
			}
		}
		if (skullNBT.hasKey("SkullOwner")) {
			skullOwner = skullNBT.getString("SkullOwner");
		} else if (skullNBT.hasKey("ExtraType")) {
			skullOwner = skullNBT.getString("ExtraType");
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
		NBTTagCompound skullNBT = getNBT();
		if (skullNBT != null) {
			skull.getHandle().tag = skullNBT;
		}
		return skull;
	}

	public NBTTagCompound getNBT() {
		if (hasTag()) {
			NBTTagCompound skullNBT = new NBTTagCompound();
			return addNBT(skullNBT);
		}
		return null;
	}
	
	public NBTTagCompound addNBT(NBTTagCompound skullNBT) {
		if (hasEnch()) {
			skullNBT.set("ench", ench);
		}
		if (hasName() || hasLore()) {
			NBTTagCompound skullNBTdisplay = new NBTTagCompound();
			if (hasName()) {
				skullNBTdisplay.setString("Name", name);
			}
			if (hasLore()) {
				skullNBTdisplay.set("Lore", lore);
			}
			skullNBT.setCompound("display", skullNBTdisplay);
		}
		if (hasOwner()) {
			skullNBT.setString("SkullOwner", skullOwner);
		}
		if (hasRepairCost()) {
			skullNBT.setInt("RepairCost", repairCost);
		}
		return skullNBT;
	}
	
	public static CraftItemStack getItemStack(int damage) {
		try {
			return new CraftItemStack(Material.SKULL_ITEM,1,(short)damage);
		} catch (NullPointerException e) {
			return null;
		}
	}

}