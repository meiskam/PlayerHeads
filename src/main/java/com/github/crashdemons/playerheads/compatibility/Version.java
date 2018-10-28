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
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class Version {
    private static int versionMajor = 0;
    private static int versionMinor = 0;
    private static boolean isInit=false;
    
    private Version(){}
    
    public static boolean checkAtLeast(int major, int minor){
        init();
        return (versionMajor>major) || (versionMajor==major && versionMinor>=minor);
    }
    public static boolean checkUnder(int major, int minor){
        init();
        return (versionMajor<major) || (versionMajor==major && versionMinor<minor);
    }
    public static boolean checkEquals(int major, int minor){
        init();
        return (versionMajor==major && versionMinor==minor);
    }
    
    public static String getRawServerVersion(){
        return Bukkit.getVersion();
    }
    
    public static String getString(){
        return versionMajor + "." + versionMinor;
    }
    
    public static synchronized void init() throws UnknownVersionException,IncompatibleVersionException{
        if(isInit) return;
        int[] mcver = getMCVersionParts();
        if(mcver==null) throw new UnknownVersionException("The current Bukkit build did not supply a version string that could be understood.");
        versionMajor=mcver[0];
        versionMinor=mcver[1];
        if(versionMajor<1 || (versionMajor==1 && versionMinor<8)) throw new IncompatibleVersionException("Server versions under 1.8 are not supported.");
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
