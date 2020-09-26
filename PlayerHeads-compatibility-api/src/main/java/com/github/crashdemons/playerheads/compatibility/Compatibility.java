/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import com.github.crashdemons.playerheads.compatibility.exceptions.CompatibilityConflictException;
import com.github.crashdemons.playerheads.compatibility.exceptions.CompatibilityMisconfiguredException;
import com.github.crashdemons.playerheads.compatibility.exceptions.CompatibilityUnavailableException;
import com.github.crashdemons.playerheads.compatibility.exceptions.CompatibilityUnregisteredException;
import com.github.crashdemons.playerheads.compatibility.exceptions.CompatibilityUnsupportedException;
import com.github.crashdemons.playerheads.compatibility.exceptions.UnknownVersionException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Compatibility class controlling implementation and version support.
 * <p>
 * This class forms the basis of most plugin access to the compatibility package
 * methods through chaining.
 * <p>
 *
 * Note: some methods of this class may not be thread-safe. Generally you should only change provider registrations on the main thread during plugin load or enable,
 * then only afterwards use isProviderAvailable / getProvider methods freely.
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public final class Compatibility {

    private Compatibility() {
    }

    private static CompatibilityProvider provider = null;

    private static final String FALLBACK_PROVIDER_TYPE = "craftbukkit";

    private static Integer[] recommendedVersion = null;
    private static String recommendedType = "";

    private static final int REQUIRED_VERSION_MAJOR = 1;
    private static final int REQUIRED_VERSION_MINOR = 8;

    /**
     * Initialize compatibility support.
     * <p>
     * This method initializes server version-detection and selects
     * compatibility providers (specific Bukkit implementations) for use later.
     * This method also makes a determination as to what the recommended
     * implementation version is.
     * <p>
     * The "recommended" version is the highest implementation version available
     * (for your server's type) that is under or equal to your server version.
     * If no provider with a matching type can be found, this method will
     * look for one with the 'fallback' server type (which at this time defaults
     * to "craftbukkit").
     * <p>
     * Provider implementations are expected to exist in the same package as the compatibility library (com.github.crashdemons.playerheads.compatibility by default).
     * followed by the server type name and major/minor version.  For example: com.github.crashdemons.playerheads.compatibility.craftbukkit_1_16
     * the class in this package must be named "Provider" and must not be abstract.
     *
     * @return Whether the recommended implementation version was used. True:
     * the best implementation version for your server that was supported was
     * loaded. False: a fallback implementation was used - possibly because you
     * loaded a backport implementation onto a newer server improperly.
     * @throws UnknownVersionException If the server version string could not be
     * understood during detection.
     * @throws CompatibilityUnsupportedException If the server version was lower
     * than is supported by the compatibility package (minimum 1.8)
     * @throws CompatibilityUnavailableException If no implementation could be
     * found that is compatible with your server. This may happen if all 
     * available providers (of a compatible type) are newer than your server 
     * version, or no matches can be found for your server type or fallback type.
     * @throws CompatibilityConflictException If an implementation provider was
     * already registered - this happens when there is more than one call to
     * init and registerProvider.
     * @throws CompatibilityMisconfiguredException If the compatibility library 
     * was not properly built (with supported versions finalized).
     */
    public static synchronized boolean init() throws UnknownVersionException, CompatibilityUnsupportedException, CompatibilityUnavailableException, CompatibilityConflictException {
        Version.init();
        //CompatiblePlugins.init();
        if (Version.checkUnder(REQUIRED_VERSION_MAJOR, REQUIRED_VERSION_MINOR)) {
            throw new CompatibilityUnsupportedException("Server versions under " + REQUIRED_VERSION_MAJOR + "." + REQUIRED_VERSION_MINOR + " are not supported.");
        }

        if (!CompatibilitySupport.isFinalized()) {
            throw new CompatibilityMisconfiguredException("This project has been misconfigured - compatibility support was not present.");
        }

        boolean isUsingFallback = false;
        CompatibilityProvider bestprovider = loadRecommendedProvider();//load provider on best available version and matching type
        if (bestprovider == null) {//if that provider isn't available, try any lower/equal version (with the matching type)
            isUsingFallback = true;
            bestprovider = loadFallbackProvider(recommendedType);
        }
        if (bestprovider == null && !recommendedType.equals(FALLBACK_PROVIDER_TYPE)) {//if THAT provider isn't available, try any lower/equal version (with "craftbukkit" implementation)
            bestprovider = loadFallbackProvider(FALLBACK_PROVIDER_TYPE);
        }
        if (bestprovider == null) {
            throw new CompatibilityUnavailableException("No suitable compatibility provider could be found.");
        }
        registerProvider(bestprovider);
        return !isUsingFallback;
    }

    /**
     * Determine if a compatibility implementation has been registered yet.
     *
     * @return whether a provider is available yet
     */
    public static boolean isProviderAvailable() {
        return provider != null;
    }

    /**
     * Registers an compatibility provider (a bukkit-version-specific
     * implementation) for the session with the compatibility class.
     * <p>
     * You should either use this or init(), but not both and not more than once
     * - providers cannot be unregistered at this time.
     *
     * @param obj The implementation to register
     * @throws CompatibilityConflictException If an implementation is already
     * registered when you called this function.
     * @see CompatibilityProvider
     * @see #init()
     */
    public static void registerProvider(CompatibilityProvider obj) throws CompatibilityConflictException {
        if (provider != null) {
            throw new CompatibilityConflictException("Multiple compatibility-providers were registered - only one is supported at a time.");
        }
        provider = obj;
    }
    
    /**
     * Unregisters the currently registered Compatibility Provider for the
     * session.
     * @return whether there was a provider to unregister (same result as isProviderAvailable())
     * @deprecated registering an unregistering multiple providers is not recommended since it means loading unnecessary classes into memory and changing the state of compatibility.
     */
    @Deprecated
    public static boolean unregisterProvider(){
        if(provider == null) return false;
        provider = null;
        return true;
    }

    /**
     * Gets the currently registered compatibility provider for the session.
     * (bukkit-version-specific implementation of required methods)
     *
     * @return the class object implementing the compatibility-provider methods.
     * @throws CompatibilityUnregisteredException If this method was called
     * before registering an implementation (eg: with init() or
     * registerProvider() )
     * @see CompatibilityProvider
     * @see #init()
     * @see
     * #registerProvider(com.github.crashdemons.playerheads.compatibility.CompatibilityProvider)
     */
    public static CompatibilityProvider getProvider() throws CompatibilityUnregisteredException {
        if (provider == null) {
            throw new CompatibilityUnregisteredException("Requested compatibility provider before any were registered - Compatibility.init must run first.");
        }
        return provider;
    }

    /**
     * Gets the current recommended implementation type name for your server
     * based on the supported implementations.
     * <p>
     * If you call this before init(), it will always return an empty string.
     *
     * @return the implementation type name, or an empty string if it is not yet
     * available.
     */
    public static String getRecommendedProviderType() {
        return recommendedType;
    }

    /**
     * Gets the current recommended bukkit-specific implementation version 
     * string for your server based on the supported implementations.
     * <p>
     * If you call this before init(), it will always return an empty string.
     *
     * @return The version string, or an empty string if it is not yet
     * available.
     */
    public static String getRecommendedProviderVersion() {
        if (recommendedVersion == null) {
            return "";
        }
        return recommendedVersion[0] + "." + recommendedVersion[1];
    }

    /**
     * gets the server's type, or the fallback type if not explicitly supported
     * @return the server type string.
     */
    private static String determineRecommendedType() {//MUST return a supported type key
        String nativeType = Version.getType();
        if (CompatibilitySupport.VERSIONS.keySet().contains(nativeType)) {
            return nativeType;
        }
        switch (nativeType) {
            //case "glowstone":
            //    throw new CompatibilityUnsupportedException("Glowstone servers are not supported by this build (missing authlib).");
            default:
                return FALLBACK_PROVIDER_TYPE;
        }
    }

    /**
     * Retrieves the highest applicable provider for currently recommended type
     * @return the provider
     */
    private static CompatibilityProvider loadRecommendedProvider() {
        recommendedType = determineRecommendedType();
        Integer[][] supportedVersions = CompatibilitySupport.VERSIONS.get(recommendedType);
        if (supportedVersions == null) {
            return null;
        }
        for (int i = 0; i < supportedVersions.length; i++) {
            Integer[] ver = supportedVersions[i];
            if (Version.checkAtLeast(ver[0], ver[1])) {
                recommendedVersion = ver;
                try {
                    return loadProviderByVersion(recommendedType, ver[0], ver[1]);
                } catch (CompatibilityUnavailableException e) {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves the highest applicable server version for the provided fallback
     * server type string.
     * @param type
     * @return the provider
     */
    private static CompatibilityProvider loadFallbackProvider(String type) {
        Integer[][] supportedVersions = CompatibilitySupport.VERSIONS.get(type);
        if (supportedVersions == null) {
            return null;
        }
        for (int i = 0; i < supportedVersions.length; i++) {
            Integer[] ver = supportedVersions[i];
            if (Version.checkAtLeast(ver[0], ver[1])) {
                try {
                    return loadProviderByVersion(type, ver[0], ver[1]);
                } catch (CompatibilityUnavailableException e) {
                    //do nothing - continue instead
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a compatibility provider (implementation) by the given server
     * type string and version.
     * @param type
     * @param major
     * @param minor
     * @return the provider
     * @throws CompatibilityUnavailableException if the provider was missing, 
     * inaccessible, or could not be instantiated.
     */
    private static CompatibilityProvider loadProviderByVersion(String type, int major, int minor) throws CompatibilityUnavailableException {
        System.out.println("Trying provider: " + type + "_" + major + "_" + minor);
        String pkg = Compatibility.class.getPackage().getName();
        String classname = pkg + "." + type + "_" + major + "_" + minor + ".Provider";
        try {
            Class<?> providerClass = Class.forName(classname);
            Constructor<?> ctor = providerClass.getConstructor();
            Object potentialProvider = ctor.newInstance();
            return (CompatibilityProvider) potentialProvider;
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException | ClassNotFoundException e) {
            throw new CompatibilityUnavailableException("Missing compatibility provider: " + classname, e);
        }
    }
}
