/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.plugins;

import static org.bukkit.Bukkit.getServer;
import org.bukkit.plugin.Plugin;

/**
 * Defines an abstract third-party-plugin compatibility class which can detect
 * and retrieves the plugin instance.
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public abstract class CompatiblePlugin {

    private String pluginName = "";
    private boolean present = false;

    protected Plugin parentPlugin = null;

    /**
     * Construct the plugin-compatibility object
     *
     * @param parentPlugin the current plugin requiring the compatibility (used
     * by child classes for events and logging)
     * @param pluginName the name of the third-party plugin to support
     */
    public CompatiblePlugin(Plugin parentPlugin, String pluginName) {
        this.parentPlugin = parentPlugin;
        this.pluginName = pluginName;
        present = this.getPlugin() != null; //if the plugin name lookup returns null, the plugin is not present.
        if (!pluginName.isEmpty()) {
            String presence=present ? "support" : "not";
            parentPlugin.getLogger().info(pluginName + " " + presence + " detected.");
        }
    }

    /**
     * Get the plugin instance for the third-party plugin being supported.
     * This method has Bukkit look-up the plugin name every time it is called,
     * unless the plugin name is blank - you should use get() instead.
     *
     * @return the plugin instance, or null of the plugin name is blank
     */
    protected final Plugin getPlugin() {
        //System.out.println("get pluginName: "+pluginName);
        if (pluginName.isEmpty()) {
            return null;
        }
        return getServer().getPluginManager().getPlugin(pluginName);
    }

    /**
     * Whether the plugin compatibility class is ready to be used.
     * (By default this is true unless overridden)
     *
     * @return whether the class is ready.
     */
    public boolean isReady() {
        return true;
    }

    /**
     * Whether the supported third-party plugin was detected to be present on
     * the server.
     *
     * @return whether the plugin is present.
     */
    public boolean isPresent() {
        return present;
    }

    /**
     * Gets the proper name of the third-party plugin this class attempts to
     * support.
     *
     * @return the plugin name
     */
    public String getName() {
        return pluginName;
    }

    /**
     * Get the plugin instance for the third-party plugin being supported.
     * This method only looks up the plugin name with Bukkit if it was initially
     * detected to be present.
     *
     * @return the plugin instance, or null if the plugin was not present at
     * startup.
     */
    public Plugin get() {
        if (present) {//save bukkit map lookup, marginal time save in benchmarks.
            return getPlugin();
        }
        return null;
    }
}
