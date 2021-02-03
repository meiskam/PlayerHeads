/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.bukkit.plugin.Plugin;

/**
 * Defines localization support for the plugin in way of messages and item names.
 * 
 * The values of keys defined here can be changed in lang.properties. `HEAD_SPAWN_*` entries are strings used for spawning heads with the spawn command, `HEAD_*` entries are used to determine the display-name for a given head.
 * 
 * <i>Note:</i> This documentation was inferred after the fact and may be inaccurate.
 * @author meiskam
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public final class Lang {
    private static final String BUNDLE_NAME = "lang";
    private static Plugin plugin;
    private static ResourceBundle RESOURCE_BUNDLE;
    private static ResourceBundle RESOURCE_BUNDLE0;

    public static String ERROR_PLUGIN_COMMAND;
    public static String ERROR_COMPATIBILITY;
    public static String ERROR_COMPATIBILITY_UNKNOWN_VERSION;
    public static String ERROR_COMPATIBILITY_SERVER_VERSION;
    public static String ERROR_COMPATIBILITY_NOT_FOUND;
    public static String WARNING_COMPATIBILITY_DIFFERENT;
    public static String COMPATIBILITY_REPORT_BUG;
    public static String COMPATIBILITY_REPORT_ERROR;
    public static String COMPATIBILITY_VERSION_RAW;
    public static String COMPATIBILITY_VERSION_DETECTED;
    public static String COMPATIBILITY_VERSION_RECOMMENDED;
    public static String COMPATIBILITY_VERSION_CURRENT;
    
    public static String LORE_PLUGIN_NAME;
    public static String LORE_HEAD_MOB;
    public static String LORE_HEAD_PLAYER;
    
    /**
     * The message displayed when a player is beheaded by anything besides a player.
     * 
     * Formatting arguments(1): player name.
     */
    public static String BEHEAD_GENERIC;
    /**
     * The message displayed when a player is beheaded by another player.
     * 
     * Formatting arguments(2): victim name, killer name
     */
    public static String BEHEAD_OTHER;
    /**
     * The message displayed when a player is beheaded by themself.
     * 
     * Formatting arguments(1): player name
     */
    public static String BEHEAD_SELF;
    public static String BRACKET_LEFT;
    public static String BRACKET_RIGHT;
    /**
     * Message displayed when a player clicks a head belonging to another player.
     * 
     * Formatting arguments(1): victim name
     */
    public static String CLICKINFO;
    /**
     * Message displayed when a player clicks a head beloning to an entity or mob.
     * 
     * Formatting arguments(1): The display-name for the head item
     */
    public static String CLICKINFO2;
    public static String CMD_CONFIG;
    public static String CMD_GET;
    public static String CMD_RELOAD;
    public static String CMD_RENAME;
    public static String CMD_RENAME_SCOPE_MOB;
    public static String CMD_RENAME_SCOPE_PLAYER;
    public static String CMD_RENAME_SCOPE_ALL;
    public static String CMD_SET;
    public static String CMD_SETBLOCK;
    public static String CMD_SPAWN;
    public static String CMD_UNKNOWN;
    public static String COLON;
    public static String COLON_SPACE;
    public static String COMMA_SPACE;
    public static String CONFIG_RELOADED;
    public static String CONFIG_VARIABLES;
    public static String ERROR_CONFIGTYPE;
    public static String ERROR_CONSOLE_SPAWN;
    public static String ERROR_INV_FULL;
    public static String ERROR_INVALID_SUBCOMMAND;
    public static String ERROR_METRICS;
    public static String ERROR_NOT_A_HEAD;
    public static String ERROR_NOT_ONLINE;
    public static String ERROR_NUMBERCONVERT;
    public static String ERROR_PERMISSION;
    public static String ERROR_UPDATER;
    
    
    public static String ERROR_INVALID_WORLD;
    public static String ERROR_INVALID_COORDS;
    public static String ERROR_CANNOT_SETBLOCK;
    public static String ERROR_INVALID_ATTACHMENT;
    public static String ERROR_INVALID_FACING;
    public static String ERROR_INVALID_COMBINATION;
    public static String ERROR_SETBLOCK_FAILED;
    public static String SET_BLOCK;
    
    /**
     * Name for a generic head item.
     */
    public static String HEAD;
    /**
     * Name for a player's head item.
     * 
     * Formatting arguments(1): player name
     */
    public static String HEAD_PLAYER;
    /**
     * String for spawning a generic player head item.
     */
    public static String HEAD_SPAWN_PLAYER;
    
    public static String HEAD_ELDER_GUARDIAN;
    public static String HEAD_SPAWN_ELDER_GUARDIAN;
    public static String HEAD_WITHER_SKELETON;
    public static String HEAD_SPAWN_WITHER_SKELETON;
    public static String HEAD_STRAY;
    public static String HEAD_SPAWN_STRAY;
    public static String HEAD_HUSK;
    public static String HEAD_SPAWN_HUSK;
    public static String HEAD_ZOMBIE_VILLAGER;
    public static String HEAD_SPAWN_ZOMBIE_VILLAGER;
    public static String HEAD_SKELETON_HORSE;
    public static String HEAD_SPAWN_SKELETON_HORSE;
    public static String HEAD_ZOMBIE_HORSE;
    public static String HEAD_SPAWN_ZOMBIE_HORSE;
    public static String HEAD_DONKEY;
    public static String HEAD_SPAWN_DONKEY;
    public static String HEAD_MULE;
    public static String HEAD_SPAWN_MULE;
    public static String HEAD_EVOKER;
    public static String HEAD_SPAWN_EVOKER;
    public static String HEAD_VEX;
    public static String HEAD_SPAWN_VEX;
    public static String HEAD_VINDICATOR;
    public static String HEAD_SPAWN_VINDICATOR;
    public static String HEAD_ILLUSIONER;
    public static String HEAD_SPAWN_ILLUSIONER;
    public static String HEAD_CREEPER;
    public static String HEAD_SPAWN_CREEPER;
    public static String HEAD_SKELETON;
    public static String HEAD_SPAWN_SKELETON;
    public static String HEAD_SPIDER;
    public static String HEAD_SPAWN_SPIDER;
    public static String HEAD_ZOMBIE;
    public static String HEAD_SPAWN_ZOMBIE;
    public static String HEAD_SLIME;
    public static String HEAD_SPAWN_SLIME;
    public static String HEAD_GHAST;
    public static String HEAD_SPAWN_GHAST;
    public static String HEAD_PIG_ZOMBIE;
    public static String HEAD_SPAWN_PIG_ZOMBIE;
    public static String HEAD_ENDERMAN;
    public static String HEAD_SPAWN_ENDERMAN;
    public static String HEAD_CAVE_SPIDER;
    public static String HEAD_SPAWN_CAVE_SPIDER;
    public static String HEAD_SILVERFISH;
    public static String HEAD_SPAWN_SILVERFISH;
    public static String HEAD_BLAZE;
    public static String HEAD_SPAWN_BLAZE;
    public static String HEAD_MAGMA_CUBE;
    public static String HEAD_SPAWN_MAGMA_CUBE;
    public static String HEAD_ENDER_DRAGON;
    public static String HEAD_SPAWN_ENDER_DRAGON;
    public static String HEAD_WITHER;
    public static String HEAD_SPAWN_WITHER;
    public static String HEAD_BAT;
    public static String HEAD_SPAWN_BAT;
    public static String HEAD_WITCH;
    public static String HEAD_SPAWN_WITCH;
    public static String HEAD_ENDERMITE;
    public static String HEAD_SPAWN_ENDERMITE;
    public static String HEAD_GUARDIAN;
    public static String HEAD_SPAWN_GUARDIAN;
    public static String HEAD_SHULKER;
    public static String HEAD_SPAWN_SHULKER;
    public static String HEAD_PIG;
    public static String HEAD_SPAWN_PIG;
    public static String HEAD_SHEEP;
    public static String HEAD_SPAWN_SHEEP;
    public static String HEAD_COW;
    public static String HEAD_SPAWN_COW;
    public static String HEAD_CHICKEN;
    public static String HEAD_SPAWN_CHICKEN;
    public static String HEAD_SQUID;
    public static String HEAD_SPAWN_SQUID;
    public static String HEAD_WOLF;
    public static String HEAD_SPAWN_WOLF;
    public static String HEAD_MUSHROOM_COW;
    public static String HEAD_SPAWN_MUSHROOM_COW;
    public static String HEAD_SNOWMAN;
    public static String HEAD_SPAWN_SNOWMAN;
    public static String HEAD_OCELOT;
    public static String HEAD_SPAWN_OCELOT;
    public static String HEAD_IRON_GOLEM;
    public static String HEAD_SPAWN_IRON_GOLEM;
    public static String HEAD_HORSE;
    public static String HEAD_SPAWN_HORSE;
    public static String HEAD_RABBIT;
    public static String HEAD_SPAWN_RABBIT;
    public static String HEAD_POLAR_BEAR;
    public static String HEAD_SPAWN_POLAR_BEAR;
    public static String HEAD_LLAMA;
    public static String HEAD_SPAWN_LLAMA;
    public static String HEAD_PARROT;
    public static String HEAD_SPAWN_PARROT;
    public static String HEAD_VILLAGER;
    public static String HEAD_SPAWN_VILLAGER;
    public static String HEAD_TURTLE;
    public static String HEAD_SPAWN_TURTLE;
    public static String HEAD_PHANTOM;
    public static String HEAD_SPAWN_PHANTOM;
    public static String HEAD_COD;
    public static String HEAD_SPAWN_COD;
    public static String HEAD_SALMON;
    public static String HEAD_SPAWN_SALMON;
    public static String HEAD_PUFFERFISH;
    public static String HEAD_SPAWN_PUFFERFISH;
    public static String HEAD_TROPICAL_FISH;
    public static String HEAD_SPAWN_TROPICAL_FISH;
    public static String HEAD_DROWNED;
    public static String HEAD_SPAWN_DROWNED;
    public static String HEAD_DOLPHIN;
    public static String HEAD_SPAWN_DOLPHIN;
    
    public static String HEAD_BEE;
    public static String HEAD_SPAWN_BEE;
    
    public static String HEAD_FOX;
    public static String HEAD_CAT;
    public static String HEAD_PILLAGER;
    public static String HEAD_PANDA;
    public static String HEAD_RAVAGER;
    
    public static String HEAD_SPAWN_FOX;
    public static String HEAD_SPAWN_CAT;
    public static String HEAD_SPAWN_PILLAGER;
    public static String HEAD_SPAWN_PANDA;
    public static String HEAD_SPAWN_RAVAGER;
    
    public static String HEAD_ARMOR_STAND;
    public static String HEAD_GIANT;
    public static String HEAD_SPAWN_ARMOR_STAND;
    public static String HEAD_SPAWN_GIANT;
    
    
    public static String OPT_AMOUNT_OPTIONAL;
    public static String OPT_HEADNAME_OPTIONAL;
    public static String OPT_HEADNAME_REQUIRED;
    public static String OPT_RECEIVER_OPTIONAL;
    public static String OPT_RECEIVER_REQUIRED;
    public static String OPT_VALUE_OPTIONAL;
    public static String OPT_VARIABLE_REQUIRED;
    
    public static String OPT_WORLD_REQUIRED;
    public static String OPT_COORDS_REQUIRED;
    public static String OPT_ATTACHMENT_OPTIONAL;
    public static String OPT_FACING_OPTIONAL;

    /**
     * Message displayed when a head is renamed.
     */
    public static String RENAMED_HEAD;
    public static String SPACE;
    /**
     * Message displayed when a head is spawned.
     * 
     * Formatting arguments(1): name of head's owner.
     */
    public static String SPAWNED_HEAD;
    public static String SPAWNED_HEAD2;
    public static String SUBCOMMANDS;
    public static String SYNTAX;

    /**
     * Message displayed when an update is available.
     * 
     * Formatting arguments(1): Name associated with the update.
     */
    public static String UPDATE1;
    /**
     * Message containing any additional information about the update.
     * 
     * Formatting arguments(1): Additional update information (usually a link).
     */
    public static String UPDATE3;

    private Lang() {}

    /**
     * Gets the string value associated with a given language-file (localization) key.
     * 
     * Note: localization strings will first be loaded from the plugin config path, and then from the default (internal) lang resource if the key is missing.
     * @param key the key to get the message string for
     * @return the localized message string for the key
     */
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (Exception e) {
            try {
                return RESOURCE_BUNDLE0.getString(key);
            } catch (Exception f) {
                return '!' + key + '!';
            }
        }
    }

    /**
     * Associates the class with a plugin and reloads all localized strings.
     * @param instance the plugin instance being used.
     */
    public static void init(Plugin instance) {
        plugin = instance;
        reload();
    }

    /**
     * Reloads localization resources and reloads the values of all keys.
     * 
     * Note: localization strings will first be loaded from the plugin config path, and then from the default lang resource if missing.
     * @see #reload() 
     */
    public static void reload() {
        String internalLocale = "";
        try {
            RESOURCE_BUNDLE0 = ResourceBundle.getBundle(BUNDLE_NAME, new UTF8Control());
            internalLocale = RESOURCE_BUNDLE0.getLocale().toString();
            if (!(internalLocale.equals(""))) {
                internalLocale = "_".concat(internalLocale);
            }
        } catch (MissingResourceException ignored) {
            if(plugin!=null) plugin.getLogger().warning("Internal language resource bundle missing");
        }

        Locale environmentLocale = Locale.getDefault();
        if(plugin!=null){
            plugin.getLogger().info("Internal plugin locale: "+internalLocale);
            plugin.getLogger().info("Environment locale: "+environmentLocale.toString()+" / "+environmentLocale.toLanguageTag());
        }
        
        try {
            URL[] urls = {plugin.getDataFolder().toURI().toURL()};
            RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, environmentLocale, new URLClassLoader(urls), new UTF8Control());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (MissingResourceException e) {
            plugin.saveResource(BUNDLE_NAME.concat(internalLocale).replace('.', '/').concat(".properties"), false);
            try {
                URL[] urls = {plugin.getDataFolder().toURI().toURL()};
                RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, environmentLocale, new URLClassLoader(urls), new UTF8Control());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        for (Field field : Lang.class.getFields()) {
            if (field.getType().equals(String.class) && Modifier.isStatic(field.getModifiers())) {
                try {
                    field.set(null, getString(field.getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
