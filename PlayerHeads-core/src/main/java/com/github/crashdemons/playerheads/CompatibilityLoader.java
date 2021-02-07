/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads;

import com.github.crashdemons.playerheads.compatibility.Compatibility;
import com.github.crashdemons.playerheads.compatibility.Version;
import com.github.crashdemons.playerheads.compatibility.exceptions.CompatibilityUnavailableException;
import com.github.crashdemons.playerheads.compatibility.exceptions.CompatibilityUnsupportedException;
import com.github.crashdemons.playerheads.compatibility.exceptions.UnknownVersionException;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;
import org.shininet.bukkit.playerheads.Lang;

/**
 * Provides logging mechanisms when version-Compatibility is initialized by PlayerHeads.
 * This is not strictly necessary over Compatibility.init(), but it provides useful log information in different cases
 * and serves as an example for catching Compatibility Exceptions.
 * @author crashdemons (crashenator at gmail.com)
 */
public class CompatibilityLoader {
    public Plugin plugin;
    public Logger logger;
    private boolean compatibilityFailed=false;
    private boolean completedInit = false;
    
    public CompatibilityLoader(Plugin plugin){
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    
    
    
    private void logCompatibilityIssue(String description, String reportcomment){
        logger.severe(description);
        logger.severe("  "+Lang.COMPATIBILITY_VERSION_RAW+Lang.COLON_SPACE+Version.getRawServerVersion());
        logger.severe("  "+Lang.COMPATIBILITY_VERSION_DETECTED+Lang.COLON_SPACE+Version.getType()+" "+Version.getString());
        logger.severe(reportcomment);
        compatibilityFailed=true;
    }
    private void logCompatibilityBug(String description){
        logCompatibilityIssue(
                description,
                Lang.COMPATIBILITY_REPORT_BUG
        );
    }
    private void logCompatibilityError(String description){
        logCompatibilityIssue(
                description,
                Lang.COMPATIBILITY_REPORT_ERROR
        );
    }
    
    public void logCompatibilityFailed(){
        logger.severe(Lang.ERROR_COMPATIBILITY);
    }
    public void logCompatibilityDetails(){
        logger.info(Lang.COMPATIBILITY_VERSION_CURRENT+Lang.COLON_SPACE+Compatibility.getProvider().getType()+" "+Compatibility.getProvider().getVersion());
    }
    
    public boolean failed(){
        return compatibilityFailed;
    }
    public boolean completedInit(){
        return completedInit;
    }
    
    public void initializeCompatibility(){
        boolean isUsingRecommendedVersion=true;
        try{
            isUsingRecommendedVersion = Compatibility.init();
        }catch(UnknownVersionException e){
            logCompatibilityBug(Lang.ERROR_COMPATIBILITY_UNKNOWN_VERSION);
            throw e;//ensure the plugin is not loaded
        }catch(CompatibilityUnsupportedException e){
            logCompatibilityError(Lang.ERROR_COMPATIBILITY_SERVER_VERSION);
            throw e;
        }catch(CompatibilityUnavailableException e){
            logCompatibilityError(Lang.ERROR_COMPATIBILITY_NOT_FOUND);
            throw e;
        }
        
        if(!isUsingRecommendedVersion){ 
            logger.warning(Lang.WARNING_COMPATIBILITY_DIFFERENT);
            logger.warning("  "+Lang.COMPATIBILITY_VERSION_DETECTED+Lang.COLON_SPACE+Version.getType()+" "+Version.getString());
            logger.warning("  "+Lang.COMPATIBILITY_VERSION_RECOMMENDED+Lang.COLON_SPACE+Compatibility.getRecommendedProviderType()+" "+Compatibility.getRecommendedProviderVersion()+" (or better)");
            logger.warning("  "+Lang.COMPATIBILITY_VERSION_CURRENT+Lang.COLON_SPACE+Compatibility.getProvider().getType()+" "+Compatibility.getProvider().getVersion());
        }
        completedInit = true;
    }
    
    

}
