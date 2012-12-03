/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.ArrayList;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;

/**
* @author meiskam
*/

public class NBTUtils {
	public static ArrayList<String> NBTList2ArrayList(NBTTagList NBTList) {
		ArrayList<String> list = new ArrayList<String>();
		NBTBase line;
		
		for (int i = 0; i < NBTList.size(); i++) {
			if ((line = NBTList.get(i)) instanceof NBTTagString) {
				list.add(line.toString());
			}
		}
		
		return list;
	}
	
	public static NBTTagList ArrayList2NBTList(ArrayList<String> list) {
		NBTTagList NBTList = new NBTTagList();
		
		for (int i = 0; i < list.size(); i++) {
			NBTList.add(new NBTTagString(null, list.get(i)));
		}
		
		return NBTList;
	}
}
