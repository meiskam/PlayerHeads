/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import com.github.crashdemons.playerheads.compatibility.exceptions.*;
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
    private static int[] recommendedVersion=null;
    
    public static synchronized boolean init() throws UnknownVersionException,IncompatibleVersionException,CompatibilityUnavailableException,CompatibilityConflictException{ 
        Version.init(); 
        boolean isUsingFallback = false;
        
        CompatibilityProvider bestprovider = loadRecommendedProvider();
        if(bestprovider==null){
            isUsingFallback = true;
            bestprovider = loadFallbackProvider();
        }
        if(bestprovider==null) throw new CompatibilityUnavailableException("No suitable compatibility provider could be found.");
        registerProvider(bestprovider);
        return !isUsingFallback;
    }
    
    public static void registerProvider(CompatibilityProvider obj) throws CompatibilityConflictException{
        if(provider!=null) throw new CompatibilityConflictException("This project has been misconfigured because multiple compatibility-providers were registered - only one is supported at a time.");
        provider=obj;
    }
    public static CompatibilityProvider getProvider() throws CompatibilityUnregisteredException{
        if(provider==null) throw new CompatibilityUnregisteredException("Requested compatibility provider before any were registered - Compatibility.init must run first.");
        return provider;
    }
    
    public static String getRecommendedProviderVersion(){
        if(recommendedVersion==null) return "";
        return recommendedVersion[0]+"."+recommendedVersion[1];
    }
    
    private static CompatibilityProvider loadRecommendedProvider(){
        for(int i=0;i<supportedVersions.length;i++){
            int[] ver=supportedVersions[i];
            if(Version.checkAtLeast(ver[0], ver[1])){
                recommendedVersion=ver;
                try{
                    return loadProviderByVersion(ver[0],ver[1]);
                }catch(CompatibilityUnavailableException e){
                    return null;
                }
            }
        }
        return null;
    }
    
    
    private static CompatibilityProvider loadFallbackProvider(){
        for(int i=0;i<supportedVersions.length;i++){
            int[] ver=supportedVersions[i];
            if(Version.checkAtLeast(ver[0], ver[1])){
                try{
                    return loadProviderByVersion(ver[0],ver[1]);
                }catch(CompatibilityUnavailableException e){
                    //do nothing - continue instead
                }
            }
        }
        return null;
    }
    private static CompatibilityProvider loadProviderByVersion(int major,int minor) throws CompatibilityUnavailableException{
        String classname = "com.github.crashdemons.playerheads.compatibility.bukkit_"+major+"_"+minor+".Provider";
        try {
            Class<?> providerClass = Class.forName(classname);
            Constructor<?> ctor = providerClass.getConstructor();
            Object provider = ctor.newInstance();
            return (CompatibilityProvider) provider;
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException | ClassNotFoundException e) {
            throw new CompatibilityUnavailableException("Missing compatibility provider: "+classname,e);
        }
    }
}
