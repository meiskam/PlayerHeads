/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class Compatibility {
    private Compatibility(){}
    
    private static CompatibilityProvider provider=null;
    
    private final static int[][] supportedVersions = new int[][]{
        {1,13},
        {1,8}
    };
    
    public static synchronized void init(){ 
        Version.init(); 
        CompatibilityProvider bestprovider = getBestProvider();
        if(bestprovider==null) throw new IllegalStateException("No suitable compatibility provider could be found.");
        registerProvider(bestprovider);
    }
    
    public static void registerProvider(CompatibilityProvider obj){
        if(provider!=null) throw new IllegalStateException("This project has been misconfigured because it contains multiple compatibility-providers.");
        provider=obj;
    }
    public static CompatibilityProvider getProvider(){
        if(provider==null) throw new IllegalStateException("Requested compatibility provider before any were registered.");
        return provider;
    }
    
    private static CompatibilityProvider getBestProvider(){
        for(int i=0;i<supportedVersions.length;i++){
            int[] ver=supportedVersions[i];
            if(Version.checkAtLeast(ver[0], ver[1])) return getProviderByVersion(ver[0],ver[1]);
        }
        return null;
    }
    private static CompatibilityProvider getProviderByVersion(int major,int minor){
        String classname = "com.github.crashdemons.playerheads.compatibility.bukkit_"+major+"_"+minor+".Provider";
        try {
            Class<?> providerClass = Class.forName(classname);
            Constructor<?> ctor = providerClass.getConstructor();
            Object provider = ctor.newInstance();
            return (CompatibilityProvider) provider;
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException | ClassNotFoundException e) {
            throw new IllegalStateException("Missing compatibility provider: "+classname,e);
        }
    }
}
