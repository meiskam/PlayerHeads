/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
* @author meiskam
*/

public class Skull {
	public String skullOwner;
	public String name;
	public ArrayList<String> lore;
	public Object ench;
	public Integer repairCost;
	public Integer skullType; //skull item's damage value
	public Integer rotation1; //skull block's damage value
	public Integer rotation2; //tile entities rotation value 

	public Skull(String skullOwner) {
		this.skullOwner = skullOwner;
		skullType = 3;
	}

	public Skull(Object skull) {
		if (Utils.NBTTagCompound.isInstance(skull)) { //skullNBT
			fakeConstructor(skull);
		} else if (Utils.CraftItemStack.isInstance(skull)) { //skull
			try {
				Object skullNBT = Utils.ItemStack.getField("tag").get(Utils.invoke(Utils.CraftItemStack, skull, "getHandle")); //NBTTagCompound
				if (skullNBT != null) {
					fakeConstructor(skullNBT);
				}
				this.skullType = (Integer)(Utils.invoke(Utils.CraftItemStack, skull, "getDurability"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (Utils.TileEntity.isInstance(skull)) { //TileEntity skullTE
			try {
				if (skull != null) {
					Object skullNBT = Utils.NBTTagCompound.newInstance(); //NBTTagCompound
					
					Utils.invoke(Utils.TileEntity, skull, "b", Utils.NBTTagCompound, skullNBT); // copies the TE's NBT data into blockNBT
					fakeConstructor(skullNBT);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Skull(Object skullNBT, int skullType) { //NBTTagCompound
		this(skullNBT);
		this.skullType = skullType;
	}
	
	public Skull(Location location) {

		Utils.invoke(Utils.CraftWorld, location.getWorld(), "getTileEntityAt", Location.class, location.getBlockX(), Location.class, location.getBlockY(), Location.class, location.getBlockZ());
		rotation1 = (int)location.getBlock().getData();
	}
	
	private void fakeConstructor(Object skullNBT) { //NBTTagCompound		
		if ((Boolean) Utils.invoke(Utils.NBTTagCompound, skullNBT, "hasKey", String.class, "ench")) {
			ench = Utils.invoke(Utils.NBTTagCompound, skullNBT, "getList", String.class, "ench");
		}
		if ((Boolean) Utils.invoke(Utils.NBTTagCompound, skullNBT, "hasKey", String.class, "display")) {
			Object skullNBTdisplay = Utils.invoke(Utils.NBTTagCompound, skullNBT, "getCompound", String.class, "display"); //NBTTagCompound
			
			if ((Boolean) Utils.invoke(Utils.NBTTagCompound, skullNBTdisplay, "hasKey", String.class, "Name")) {
				name = (String) Utils.invoke(Utils.NBTTagCompound, skullNBTdisplay, "getString", String.class, "Name");
			}
			if ((Boolean) Utils.invoke(Utils.NBTTagCompound, skullNBTdisplay, "hasKey", String.class, "Lore")) {
				lore = Utils.NBTList2ArrayList(Utils.invoke(Utils.NBTTagCompound, skullNBTdisplay, "getList", String.class, "Lore"));
			}
		}
		if ((Boolean) Utils.invoke(Utils.NBTTagCompound, skullNBT, "hasKey", String.class, "SkullOwner")) {
			skullOwner = (String) Utils.invoke(Utils.NBTTagCompound, skullNBT, "getString", String.class, "SkullOwner");
		} else if ((Boolean) Utils.invoke(Utils.NBTTagCompound, skullNBT, "hasKey", String.class, "ExtraType")) {
			skullOwner = (String) Utils.invoke(Utils.NBTTagCompound, skullNBT, "getString", String.class, "ExtraType");
		}
		if ((Boolean) Utils.invoke(Utils.NBTTagCompound, skullNBT, "hasKey", String.class, "SkullType")) {
			skullType = (Integer) Utils.invoke(Utils.NBTTagCompound, skullNBT, "getByte", String.class, "SkullType");
		}
		if ((Boolean) Utils.invoke(Utils.NBTTagCompound, skullNBT, "hasKey", String.class, "Rot")) {
			rotation2 = (Integer) Utils.invoke(Utils.NBTTagCompound, skullNBT, "getByte", String.class, "Rot");
		}
		if ((Boolean) Utils.invoke(Utils.NBTTagCompound, skullNBT, "hasKey", String.class, "RepairCost")) {
			repairCost = (Integer) Utils.invoke(Utils.NBTTagCompound, skullNBT, "getInt", String.class, "RepairCost");
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
		if (ench == null || (Integer)Utils.invoke(Utils.NBTTagList, ench, "size") == 0) {
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
	
	public ItemStack getItemStack() {
		Object skull;
		try {
			skull = Utils.CraftItemStack.getConstructor(Material.class, int.class, short.class).newInstance(Material.SKULL_ITEM, 1, (short)3);
		} catch (Exception e) {
			return null;
		}
		Object skullNBT = getNBT(false);
		if (skullNBT != null) {
			try {
				Utils.ItemStack.getField("tag").set(Utils.invoke(Utils.CraftItemStack, skull, "getHandle"), skullNBT);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return (ItemStack) skull;
	}
	
	public Object getNBT() {
		return getNBT(false);
	}

	public Object getNBT(boolean isTileEntity) {
		if (hasTag()) {
			Object skullNBT;
			try {
				skullNBT = Utils.NBTTagCompound.newInstance();
				return addNBT(skullNBT, isTileEntity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Object addNBT(Object skullNBT) {
		return addNBT(skullNBT, false);
	}

	public Object addNBT(Object skullNBT, boolean isTileEntity) {
		if (hasEnch()) {
			Utils.invoke(Utils.NBTTagCompound, skullNBT, "set", String.class, "ench", Utils.NBTBase, ench);
		}
		if (hasName() || hasLore()) {
			Object skullNBTdisplay;
			try {
				skullNBTdisplay = Utils.NBTTagCompound.newInstance();
				if (hasName()) {
					Utils.invoke(Utils.NBTTagCompound, skullNBTdisplay, "setString", String.class, "Name", String.class, name);
				}
				if (hasLore()) {
					Utils.invoke(Utils.NBTTagCompound, skullNBTdisplay, "set", String.class, "Lore", Utils.NBTBase, Utils.ArrayList2NBTList(lore));
				}
				Utils.invoke(Utils.NBTTagCompound, skullNBT, "setCompound", String.class, "display", Utils.NBTTagCompound, skullNBTdisplay);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (hasRepairCost()) {
			Utils.invoke(Utils.NBTTagCompound, skullNBT, "setInt", String.class, "RepairCost", int.class, repairCost);
		}
		if (isTileEntity) {
			if (hasOwner()) {
				Utils.invoke(Utils.NBTTagCompound, skullNBT, "setString", String.class, "ExtraType", String.class, skullOwner);
			}
			if (skullType != null) {
				Utils.invoke(Utils.NBTTagCompound, skullNBT, "setByte", String.class, "SkullType", byte.class, (byte)(int)skullType);
			}
			if (rotation2 != null) {
				Utils.invoke(Utils.NBTTagCompound, skullNBT, "setByte", String.class, "Rot", byte.class, (byte)(int)rotation2);
			}
		} else {
			if (hasOwner()) {
				Utils.invoke(Utils.NBTTagCompound, skullNBT, "setString", String.class, "SkullOwner", String.class, skullOwner);
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
		Object blockTE = Utils.invoke(Utils.CraftWorld, location.getWorld(), "getTileEntityAt", int.class, location.getBlockX(), int.class, location.getBlockY(), int.class, location.getBlockZ());
		//Utils.TileEntity
		if (blockTE != null) {
			Object blockNBT;
			try {
				blockNBT = Utils.NBTTagCompound.newInstance();
				Utils.invoke(Utils.TileEntity, blockTE, "b", Utils.NBTTagCompound, blockNBT); //copy the block's TE into blockNBT
				Utils.invoke(Utils.TileEntity, blockTE, "a", Utils.NBTTagCompound, addNBT(blockNBT, true)); //add pertaining details to blockNBT then set the block's TE to blockNBT
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static ItemStack getItemStack(int damage) {
		try {
			return (ItemStack) Utils.CraftItemStack.getConstructor(Material.class, int.class, short.class).newInstance(Material.SKULL_ITEM, 1, (short)damage);
		} catch (Exception e) {
			return null;
		}
	}

}