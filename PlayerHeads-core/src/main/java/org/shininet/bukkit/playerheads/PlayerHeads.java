/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import com.github.crashdemons.playerheads.api.ApiProvider;
import com.github.crashdemons.playerheads.compatibility.Compatibility;
import com.github.crashdemons.playerheads.compatibility.CompatiblePlugins;
import com.github.crashdemons.playerheads.compatibility.Version;
import com.github.crashdemons.playerheads.compatibility.exceptions.CompatibilityUnavailableException;
import com.github.crashdemons.playerheads.compatibility.exceptions.CompatibilityUnsupportedException;
import com.github.crashdemons.playerheads.compatibility.exceptions.UnknownVersionException;
import java.util.logging.Logger;
import org.bukkit.command.PluginCommand;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Defines the main plugin class.
 * 
 * <i>Note:</i> This documentation was inferred after the fact and may be inaccurate.
 * @author meiskam
 */

public final class PlayerHeads extends JavaPlugin implements Listener,PlayerHeadsPlugin {

    private PlayerHeadsListener listener;
    //private PlayerHeadsDraftListener draftListener=null;
    public Logger logger;
    public FileConfiguration configFile;
    public final ApiProvider api;
    private static boolean updateReady = false;
    private static String updateName = "";
    
    //private final boolean hasBlockDropItemSupport;//whether the server has draft-API support
    
    private boolean compatibilityFailed=false;
    
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
    
    private void initializeCompatibility(){
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
    }
    
    
    public PlayerHeads(){
        super();
        api=new ApiProvider(this);
        com.github.crashdemons.playerheads.api.PlayerHeads.setApiInstance(api);
        //hasBlockDropItemSupport = RuntimeReferences.hasClass("org.bukkit.event.block.BlockDropItemEvent");
        
    }
    
    @Override
    public void onLoad(){
        logger = getLogger();
        Lang.init(this);
        initializeCompatibility();
    }

    /**
     * Executed when the plugin is enabled by the server
     */
    @Override
    public void onEnable() {
        if(compatibilityFailed){
            logger.severe(Lang.ERROR_COMPATIBILITY);
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        CompatiblePlugins.init(this);
        
        configFile = getConfig();
        configFile.options().copyDefaults(true);
        saveDefaultConfig();

        initUpdater();

        listener = new PlayerHeadsListener(this);
        PlayerHeadsCommandExecutor commandExecutor = new PlayerHeadsCommandExecutor(this);
        listener.registerAll();
        /*if(hasBlockDropItemSupport){
            logger.info("BlockDropItem support detected - registering");
            draftListener = new PlayerHeadsDraftListener(listener,this);
            draftListener.registerAll();
        }
        else logger.info("BlockDropItem not supported by this server");*/
        PluginCommand command = getCommand("PlayerHeads");
        if(command==null){//the command for this very plugin couldn't be found - this should not happen.
            logger.severe(Lang.ERROR_PLUGIN_COMMAND);
        }else command.setExecutor(commandExecutor);
        
        
        logger.info(Lang.COMPATIBILITY_VERSION_CURRENT+Lang.COLON_SPACE+Compatibility.getProvider().getType()+" "+Compatibility.getProvider().getVersion());
    }
    
    /**
     * Executed when the plugin is disabled by the server
     */
    @Override
    public void onDisable() {
        //if(hasBlockDropItemSupport && draftListener!=null) draftListener.unregisterAll();
        listener.unregisterAll();
        logger.info("disabled");
    }
    
    public void onConfigReloaded(){
        listener.reloadConfig();
    }

    private void initUpdater() {
        //TODO: check for updated updater code without reliance on simple-json
        /*try {
            if (configFile.getBoolean("updatecheck") && !(updateReady)) {
                Updater updater = new Updater(this, Config.updateID, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
                updateReady = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
                updateName = updater.getLatestName(); // Get the latest version
            }
        } catch (Exception e) {
            logger.warning(Lang.ERROR_UPDATER);
        }*/
    }


    /**
     * Gets whether an update is available for the plugin.
     * @return whether an update is available for the plugin.
     */
    public boolean getUpdateReady() {
        return updateReady;
    }

    /**
     * Gets the version-name associated with the last update checked, if available
     * @return the version-name associated with the last update checked. If not available: "".
     */
    public String getUpdateName() {
        return updateName;
    }
}
