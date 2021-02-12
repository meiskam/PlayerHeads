/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import java.lang.reflect.Method;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

/**
 * Provides methods to perform runtime lookups of values by name (eg: enums)
 * @author crashdemons (crashenator at gmail.com)
 */
public final class RuntimeReferences {
    private RuntimeReferences(){}
    
    /**
     * Retrieves the 'CompatibleSkullMaterial' (a possibly-supported vanilla head material) associated with a name.
     * @param name The name of the CompatibleSkullMaterial (generally name-compatible with EntityType names)
     * @return the matching CompatibleSkullMaterial, or null.
     */
    public static CompatibleSkullMaterial getCompatibleMaterialByName(String name){
        try{
            return CompatibleSkullMaterial.valueOf(name);
        }catch(IllegalArgumentException e){
            return null;
        }
    }
    /**
     * Retrieves the entitytype from a name, or null
     * @param name
     * @return the entitytype, or null
     */
    public static EntityType getEntityTypeByName(String name){
        try{
            return EntityType.valueOf(name);
        }catch(IllegalArgumentException e){
            return null;
        }
    }
    /**
     * Retrieves the bukkit Material from a name, or null
     * @param name
     * @return the bukkit Material, or null
     */
    public static Material getMaterialByName(String name){
        try{
            return Material.valueOf(name);
        }catch(IllegalArgumentException e){
            return null;
        }
    }
    /**
     * Retrieves the SkullType (compatibility version) associated with a name
     * @param name
     * @return the SkullType, or null
     */
    public static SkullType getSkullTypeByName(String name){
        try{
            return SkullType.valueOf(name);
        }catch(IllegalArgumentException e){
            return null;
        }
    }
    
    /**
     * Retrieves a class associated with a name
     * @param classname
     * @return the class, or null
     */
    public static Class<?> getClass(String classname){
        try {
            return Class.forName(classname);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Retrieves a method of a class by name
     * @param classobj
     * @param methodname
     * @param parameterTypes
     * @return the method, or null
     */
    public static Method getMethod(Class<?> classobj, String methodname, Class<?>... parameterTypes){
        try{
            return classobj.getMethod(methodname, parameterTypes);
        }catch(Exception e){
            return null;
        }
    }
    
    /**
     * Retrieves a method by name from a class (also retrieved by name)
     * @param classname
     * @param methodname
     * @param parameterTypes
     * @return the method, or null
     */
    public static Method getMethod(String classname, String methodname,Class<?>... parameterTypes){
        Class<?> classobj = getClass(classname);
        if(classobj==null) return null;
        return getMethod(classobj,methodname,parameterTypes);
    }
    
    /**
     * Checks whether a class exists (by name)
     * @param classname
     * @return whether the class exists
     */
    public static boolean hasClass(String classname){
        try {
            Class<?> providerClass = Class.forName(classname);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
