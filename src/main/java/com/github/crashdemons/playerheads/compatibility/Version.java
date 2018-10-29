/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import com.github.crashdemons.playerheads.compatibility.exceptions.IncompatibleVersionException;
import com.github.crashdemons.playerheads.compatibility.exceptions.UnknownVersionException;
import org.bukkit.Bukkit;

/**
 * A class providing methods related to the current server's version.
 * @author crashdemons (crashenator at gmail.com)
 */
public class Version {
    private static int versionMajor = 0;
    private static int versionMinor = 0;
    private static boolean isInit=false;
    
    private Version(){}
    
    /**
     * Checks whether the current server version is at least the version supplied
     * @param major the major version number to check
     * @param minor the minor version number to check
     * @return whether the check is true
     */
    public static boolean checkAtLeast(int major, int minor){
        init();
        return (versionMajor>major) || (versionMajor==major && versionMinor>=minor);
    }
    /**
     * Checks whether the current server version is less than the version supplied
     * @param major the major version number to check
     * @param minor the minor version number to check
     * @return whether the check is true
     */
    public static boolean checkUnder(int major, int minor){
        init();
        return (versionMajor<major) || (versionMajor==major && versionMinor<minor);
    }
    /**
     * Checks whether the current server version is exactly the version supplied
     * @param major the major version number to check
     * @param minor the minor version number to check
     * @return whether the check is true
     */
    public static boolean checkEquals(int major, int minor){
        init();
        return (versionMajor==major && versionMinor==minor);
    }
    
    /**
     * Gets the raw version string supplied by the server
     * @return the version string
     */
    public static String getRawServerVersion(){
        return Bukkit.getVersion();
    }
    
    /**
     * Gets the detected server version string in the format Major.Minor
     * @return the version string
     */
    public static String getString(){
        return versionMajor + "." + versionMinor;
    }
    
    /**
     * Initialize the version class and detect the server version.
     * @throws UnknownVersionException If the version string supplied by the server could not be understood.
     * @throws IncompatibleVersionException If the version supplied by the server is not supportable by this plugin
     */
    public static synchronized void init() throws UnknownVersionException,IncompatibleVersionException{
        if(isInit) return;
        int[] mcver = getMCVersionParts();
        if(mcver==null) throw new UnknownVersionException("The current Bukkit build did not supply a version string that could be understood.");
        versionMajor=mcver[0];
        versionMinor=mcver[1];
        if(versionMajor<1 || (versionMajor==1 && versionMinor<8)) throw new IncompatibleVersionException("Server versions under 1.8 are not supported.");// this exception may need to be moved to a more relevant class like Compatibility - it's not the version class's job to decide what is compatible.
        isInit=true;
    }
    
    private static String getMCVersion(){
        String ver = getRawServerVersion();
        int pos = ver.indexOf("MC: ");
        if(pos==-1) return "";
        String ver_mc_untrimmed=ver.substring(pos+"MC: ".length());
        int pos_paren=ver_mc_untrimmed.indexOf(")");
        if(pos_paren==-1) return "";
        return ver_mc_untrimmed.substring(0, pos_paren);
    }
    private static int[] getMCVersionParts(){
        String ver = getMCVersion();
        if(ver.isEmpty()) return null;
        String[] parts = (ver+".0.0").split("\\.");
        try{
            return new int[]{Integer.parseInt(parts[0]),Integer.parseInt(parts[1])};
        }catch(NumberFormatException e){
            return null;
        }
    }
}
