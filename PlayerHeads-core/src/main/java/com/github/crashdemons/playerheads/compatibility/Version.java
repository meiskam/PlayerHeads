/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import com.github.crashdemons.playerheads.compatibility.exceptions.CompatibilityUnsupportedException;
import com.github.crashdemons.playerheads.compatibility.exceptions.UnknownVersionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;

/**
 * A class providing methods related to the current server's version.
 * @author crashdemons (crashenator at gmail.com)
 */
public final class Version {
    private static String serverType ="";
    private static int versionMajor = 0;
    private static int versionMinor = 0;
    private static boolean isInit=false;
    
    private static final String[][] serverTypeByClass = {
        {"com.github.crashdemons.playerheads.compatibility.faketestserver_1_0.Provider","faketestserver"},
        {"net.glowstone.GlowServer","glowstone"},
        {"org.github.paperspigot.PaperSpigotConfig","paper"},
        {"com.destroystokyo.paper.PaperConfig","paper"},
        {"org.spigotmc.SpigotConfig","spigot"},
        {"org.bukkit.craftbukkit.Main","craftbukkit"}
    };
    
    
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
     * The type of server.
     * @return the server type string
     */
    public static String getType(){
        return serverType;
    }
    
    /**
     * Initialize the version class and detect the server version.
     * @throws UnknownVersionException If the version string supplied by the server could not be understood.
     * @throws CompatibilityUnsupportedException If the version supplied by the server is not supportable by this plugin
     */
    public static synchronized void init() throws UnknownVersionException,CompatibilityUnsupportedException{
        if(isInit) return;
        int[] mcver = getMCVersionParts();
        if(mcver==null) throw new UnknownVersionException("The current Bukkit build did not supply a version string that could be understood.");
        versionMajor=mcver[0];
        versionMinor=mcver[1];
        serverType=getServerType();
        isInit=true;
    }
    
    private static String getMCVersion(){
        String rawVersion = getRawServerVersion();

        String versionRegex=".*\\(MC: ([0-9.]+).*?";
        Pattern pattern = Pattern.compile(versionRegex);
        Matcher matcher = pattern.matcher(rawVersion);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return "";
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
    private static String getServerType(){
        //return (getRawServerVersion()+"  ").split(" ", 2)[0].toLowerCase();//this doesn't work because most servers don't return the full version string.
        for(int i=0;i<serverTypeByClass.length;i++){
            if(RuntimeReferences.hasClass(serverTypeByClass[i][0]))
                return serverTypeByClass[i][1];
        }
        return "unknown";
    }
}
