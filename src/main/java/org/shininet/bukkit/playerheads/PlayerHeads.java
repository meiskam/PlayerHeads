/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import com.github.crashdemons.playerheads.compatibility.Compatibility;
import com.github.crashdemons.playerheads.compatibility.Version;
import com.github.crashdemons.playerheads.compatibility.exceptions.CompatibilityConflictException;
import com.github.crashdemons.playerheads.compatibility.exceptions.CompatibilityUnavailableException;
import com.github.crashdemons.playerheads.compatibility.exceptions.IncompatibleVersionException;
import com.github.crashdemons.playerheads.compatibility.exceptions.UnknownVersionException;
import net.gravitydevelopment.updater.Updater;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Defines the main plugin class.
 * 
 * <i>Note:</i> This documentation was inferred after the fact and may be inaccurate.
 * @author meiskam
 */

public final class PlayerHeads extends JavaPlugin implements Listener {

    private PlayerHeadsListener listener;
    public Logger logger;
    public FileConfiguration configFile;
    private static boolean updateReady = false;
    private static String updateName = "";
    /**
     * State indicating whether NoCheatPlus is enabled on the server.
     * 
     * Used internally to enable or disable behavior in helper classes to improve compatibility with NCP.
     */
    public boolean NCPHook = false;
    
    public boolean compatibilityFailed=false;
    
    private void logCompatibilityIssue(String description, String reportcomment){
        logger.severe(description);
        logger.severe("  Raw server version string: "+Version.getRawServerVersion());
        logger.severe("  Detected server version: "+Version.getString());
        logger.severe(reportcomment);
        compatibilityFailed=true;
    }
    private void logCompatibilityBug(String description){
        logCompatibilityIssue(
                description,
                "Please report this as a bug at https://dev.bukkit.org/projects/player-heads/issues with your log file."
        );
    }
    private void logCompatibilityError(String description){
        logCompatibilityIssue(
                description,
                "(If you believe this is incorrect, please report this as a bug at https://dev.bukkit.org/projects/player-heads/issues with your log)"
        );
    }
    
    private void initializeCompatibility(){
        boolean isUsingRecommendedVersion=true;
        try{
            isUsingRecommendedVersion = Compatibility.init();
        }catch(UnknownVersionException e){
            logCompatibilityBug("The server supplied a version string the plugin could not understand.");
            throw e;//ensure the plugin is not loaded
        }catch(IncompatibleVersionException e){
            logCompatibilityError("Your server version is not supported for this plugin build.");
            throw e;
        }catch(CompatibilityUnavailableException e){
            logCompatibilityError("No compatibility support available for your server version - this plugin may have been made for a newer server version. Did you mean to use a backport instead?");
            throw e;
        }
        
        if(!isUsingRecommendedVersion){ 
            logger.warning("This plugin was made for different server version and may not operate properly:");
            logger.warning("  Your server version: "+Version.getString());
            logger.warning("  Recommended plugin version: "+Compatibility.getRecommendedProviderVersion()+" (or better)");
            logger.warning("  Current plugin version: "+Compatibility.getProvider().getVersion());
        }
    }
    
    
    public PlayerHeads(){
        super();
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
            logger.severe("Incompatible plugin - disabling...");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        configFile = getConfig();
        configFile.options().copyDefaults(true);
        saveDefaultConfig();

        initUpdater();
        initNCPHook();

        listener = new PlayerHeadsListener(this);
        PlayerHeadsCommandExecutor commandExecutor = new PlayerHeadsCommandExecutor(this);
        getServer().getPluginManager().registerEvents(listener, this);
        getCommand("PlayerHeads").setExecutor(commandExecutor);
        
        
        logger.info("Compatibility-Provider: "+Compatibility.getProvider().getVersion());
    }
    
    /**
     * Executed when the plugin is disabled by the server
     */
    @Override
    public void onDisable() {
        EntityDeathEvent.getHandlerList().unregister(listener);
        PlayerInteractEvent.getHandlerList().unregister(listener);
        PlayerJoinEvent.getHandlerList().unregister(listener);
        BlockBreakEvent.getHandlerList().unregister(listener);
    }

    private void initUpdater() {
        try {
            if (configFile.getBoolean("updatecheck") && !(updateReady)) {
                Updater updater = new Updater(this, Config.updateID, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
                updateReady = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
                updateName = updater.getLatestName(); // Get the latest version
            }
        } catch (Exception e) {
            logger.warning(Lang.ERROR_UPDATER);
        }
    }

    private void initNCPHook() {
        if (getServer().getPluginManager().getPlugin("NoCheatPlus") != null) {
            NCPHook = true;
        }
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
