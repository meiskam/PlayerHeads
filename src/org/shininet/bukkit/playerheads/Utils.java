/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.ArrayList;

import org.bukkit.Bukkit;

/**
* @author meiskam
*/

public class Utils {
	
	public static String CBBase;
	public static String NMSBase;
	public static Class<?> NBTBase;
	public static Class<?> NBTTagList;
	public static Class<?> NBTTagString;
	public static Class<?> NBTTagCompound;
	public static Class<?> TileEntity;
	public static Class<?> ItemStack;
	public static Class<?> CraftWorld;
	public static Class<?> CraftItemStack;
	
	public static void init() {
		try {
			CBBase = Bukkit.getServer().getClass().getName();
			CBBase = CBBase.substring(0, CBBase.lastIndexOf('.')+1);
			
			NMSBase = Bukkit.getServer().getClass().getDeclaredMethod("getServer", (Class<?>)null).invoke(Bukkit.getServer(), (Object)null).getClass().getName();
			NMSBase = NMSBase.substring(0, NMSBase.lastIndexOf('.')+1);
	
			NBTBase = Class.forName(NMSBase + "NBTBase");
			NBTTagList = Class.forName(NMSBase + "NBTTagList");
			NBTTagString = Class.forName(NMSBase + "NBTTagString");
			NBTTagCompound = Class.forName(NMSBase + "NBTTagCompound");
			TileEntity = Class.forName(NMSBase + "TileEntity");
			ItemStack = Class.forName(NMSBase + "ItemStack");
			CraftWorld = Class.forName(CBBase + "CraftWorld");
			CraftItemStack = Class.forName(CBBase + "inventory.CraftItemStack");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object invoke(Class<?> type, Object var, String method) {
		return invoke(type, var, method, (Object)null);
	}
	
	public static Object invoke(Class<?> type, Object var, String method, Object... args) {
		try {
			return type.getMethod(method, (Class<?>)null).invoke(var, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<String> NBTList2ArrayList(Object NBTList) {
		if (!NBTTagList.isInstance(NBTList)) {
			return null;
		}
		ArrayList<String> list = new ArrayList<String>();
		Object line;
		
		int listSize = (Integer) invoke(NBTTagList, NBTList, "size");
		for (int i = 0; i < listSize; i++) {
			
			if (NBTTagString.isInstance(line = invoke(NBTTagList, NBTList, "get", i))) {
				list.add(line.toString());
			}
		}
		
		return list;
	}
	
	public static Object ArrayList2NBTList(ArrayList<String> list) {
		try {
			Object NBTList = NBTTagList.newInstance();
			
			for (int i = 0; i < list.size(); i++) {
				invoke(NBTTagList, NBTList, "add", NBTTagString.getConstructor(String.class, String.class).newInstance(null, list.get(i)));
			}
			
			return NBTList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
