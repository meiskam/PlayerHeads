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
 * Compatibility class controlling implementation and version support.
 * 
 * This class forms the basis of most plugin access to the compatibility package methods through chaining.
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
    
    /**
     * Initialize compatibility support.
     * 
     * This method initializes server version-detection and selects compatibility providers (specific Bukkit implementations) for use later.
     * This method also makes a determination as to what the recommended implementation version is.
     * @return Whether the recommended implementation version was used. True: the best implementation version for your server that was supported was loaded. False: a fallback implementation was used - possibly because you loaded a backport implementation onto a newer server improperly.
     * @throws UnknownVersionException If the server version string could not be understood during detection.
     * @throws IncompatibleVersionException If the server version was lower than is supported by the compatibility package (minimum 1.8)
     * @throws CompatibilityUnavailableException If no implementation could be found that is compatible with your server. This happens when you load an implementation onto a server where the implementation is too new for the server - possibly because you didn't properly use a backport.
     * @throws CompatibilityConflictException If an implementation provider was already registered - this happens when there is more than one call to init and registerProvider.
     */
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
    
    /**
     * Registers an compatibility provider (a bukkit-version-specific implementation) with the compatibility class.
     * 
     * You should either use this or init(), but not both and not more than once - providers cannot be unregistered at this time.
     * @param obj The implementation to register
     * @throws CompatibilityConflictException If an implementation is already registered when you called this function.
     * @see CompatibilityProvider
     * @see #init() 
     */
    public static void registerProvider(CompatibilityProvider obj) throws CompatibilityConflictException{
        if(provider!=null) throw new CompatibilityConflictException("This project has been misconfigured because multiple compatibility-providers were registered - only one is supported at a time.");
        provider=obj;
    }
    
    /**
     * Gets the currently registered compatibility provider (bukkit-version-specific implementation of required methods)
     * @return the class object implementing the compatibility-provider methods.
     * @throws CompatibilityUnregisteredException If this method was called before registering an implementation (eg: with init() or registerProvider() )
     * @see CompatibilityProvider
     * @see #init() 
     * @see #registerProvider(com.github.crashdemons.playerheads.compatibility.CompatibilityProvider) 
     */
    public static CompatibilityProvider getProvider() throws CompatibilityUnregisteredException{
        if(provider==null) throw new CompatibilityUnregisteredException("Requested compatibility provider before any were registered - Compatibility.init must run first.");
        return provider;
    }
    
    /**
     * Gets the recommended bukkit-specific implementation version string for your server based on the supported implementations.
     * 
     * If you call this before init(), it will always return an empty string.
     * @return The version string, or an empty string if it is not yet available.
     */
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
