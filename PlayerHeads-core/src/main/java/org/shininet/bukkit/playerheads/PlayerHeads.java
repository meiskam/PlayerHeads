/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import com.github.crashdemons.playerheads.CompatibilityLoader;
import com.github.crashdemons.playerheads.api.ApiProvider;
import com.github.crashdemons.playerheads.compatibility.CompatiblePlugins;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
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
    public static PlayerHeads instance;
    private PlayerHeadsListener listener;
    private CompatibilityLoader compatibilityLoader; 
    //private PlayerHeadsDraftListener draftListener=null;
    public Logger logger;
    public FileConfiguration configFile;
    public final ApiProvider api;
    private static boolean updateReady = false;
    private static String updateName = "";
    
    //private final boolean hasBlockDropItemSupport;//whether the server has draft-API support
    
    public void scheduleSync(Runnable task, long tick_delay){
        int tasknum = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, task, tick_delay);
        //System.out.println(" head drop task #"+tasknum+" @ "+tick_delay+" ticks ("+(tick_delay/20.0)+"s");
    }
    
    public PlayerHeads(){
        super();
        instance=this;
        api=new ApiProvider(this);
        com.github.crashdemons.playerheads.api.PlayerHeads.setApiInstance(api);
        //hasBlockDropItemSupport = RuntimeReferences.hasClass("org.bukkit.event.block.BlockDropItemEvent");
        
    }
    
    @Override
    public void onLoad(){
        logger = getLogger();
        Lang.init(this);
        compatibilityLoader.initializeCompatibility();
    }

    /**
     * Executed when the plugin is enabled by the server
     */
    @Override
    public void onEnable() {
        if(compatibilityLoader.failed()){
            compatibilityLoader.logCompatibilityFailed();
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        configFile = getConfig();
        configFile.options().copyDefaults(true);
        saveDefaultConfig();
        
        CompatiblePlugins.init(this);

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
        
        compatibilityLoader.logCompatibilityDetails();
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
        CompatiblePlugins.reloadConfig();
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
