/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import org.bukkit.Material;

/**
 * Provides methods to perform runtime lookups of values by name (eg: enums)
 * @author crashdemons (crashenator at gmail.com)
 */
public class RuntimeReferences {
    private RuntimeReferences(){}
    public static CompatibleSkullMaterial getCompatibleMaterialByName(String name){
        try{
            return CompatibleSkullMaterial.valueOf(name);
        }catch(IllegalArgumentException e){
            return null;
        }
    }
    public static Material getMaterialByName(String name){
        try{
            return Material.valueOf(name);
        }catch(IllegalArgumentException e){
            return null;
        }
    }
    public static SkullType getSkullTypeByName(String name){
        try{
            return SkullType.valueOf(name);
        }catch(IllegalArgumentException e){
            return null;
        }
    }
}
