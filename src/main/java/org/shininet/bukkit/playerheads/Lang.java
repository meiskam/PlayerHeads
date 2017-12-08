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
 * @author meiskam
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class Lang {
    private static final String BUNDLE_NAME = "lang";
    private static Plugin plugin;
    private static ResourceBundle RESOURCE_BUNDLE;
    private static ResourceBundle RESOURCE_BUNDLE0;

    public static String BEHEAD_GENERIC;
    public static String BEHEAD_OTHER;
    public static String BEHEAD_SELF;
    public static String BRACKET_LEFT;
    public static String BRACKET_RIGHT;
    public static String CLICKINFO;
    public static String CLICKINFO2;
    public static String CMD_CONFIG;
    public static String CMD_GET;
    public static String CMD_RELOAD;
    public static String CMD_RENAME;
    public static String CMD_SET;
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
    public static String HEAD_BAT;
    public static String HEAD_BLAZE;
    public static String HEAD_CAVE_SPIDER;
    public static String HEAD_CHICKEN;
    public static String HEAD_COW;
    public static String HEAD_CREEPER;
    public static String HEAD_ENDER_DRAGON;
    public static String HEAD_ENDERMAN;
    public static String HEAD_GHAST;
    public static String HEAD_GUARDIAN;
    public static String HEAD_ELDER_GUARDIAN;
    public static String HEAD_HORSE;
    public static String HEAD_DONKEY;
    public static String HEAD_IRON_GOLEM;
    public static String HEAD_MAGMA_CUBE;
    public static String HEAD_MUSHROOM_COW;
    public static String HEAD_OCELOT;
    public static String HEAD_PIG;
    public static String HEAD_PIG_ZOMBIE;
    public static String HEAD_RABBIT;
    public static String HEAD_SHEEP;
    public static String HEAD_SILVERFISH;
    public static String HEAD_SKELETON;
    public static String HEAD_SLIME;
    public static String HEAD_SNOWMAN;
    public static String HEAD_SPIDER;
    public static String HEAD_SQUID;
    public static String HEAD_VILLAGER;
    public static String HEAD_ZOMBIE_VILLAGER;
    public static String HEAD_WITCH;
    public static String HEAD_WITHER;
    public static String HEAD_WOLF;
    public static String HEAD_ZOMBIE;
    public static String HEAD_POLAR_BEAR;
    public static String HEAD_SHULKER;
    public static String HEAD_ENDERMITE;
    public static String HEAD_STRAY;
    public static String HEAD_EVOKER;
    public static String HEAD_ILLUSIONER;
    public static String HEAD_VINDICATOR;
    public static String HEAD_WITHER_BOSS;
    public static String HEAD_VEX;
    public static String HEAD_LLAMA;
    public static String HEAD_PARROT;
    public static String HEAD_SPAWN_BAT;
    public static String HEAD_SPAWN_BLAZE;
    public static String HEAD_SPAWN_CAVE_SPIDER;
    public static String HEAD_SPAWN_CHICKEN;
    public static String HEAD_SPAWN_COW;
    public static String HEAD_SPAWN_CREEPER;
    public static String HEAD_SPAWN_ENDER_DRAGON;
    public static String HEAD_SPAWN_ENDERMAN;
    public static String HEAD_SPAWN_GHAST;
    public static String HEAD_SPAWN_GUARDIAN;
    public static String HEAD_SPAWN_ELDER_GUARDIAN;
    public static String HEAD_SPAWN_HORSE;    
    public static String HEAD_SPAWN_DONKEY;
    public static String HEAD_SPAWN_IRON_GOLEM;
    public static String HEAD_SPAWN_MAGMA_CUBE;
    public static String HEAD_SPAWN_MUSHROOM_COW;
    public static String HEAD_SPAWN_OCELOT;
    public static String HEAD_SPAWN_PIG;
    public static String HEAD_SPAWN_PIG_ZOMBIE;
    public static String HEAD_SPAWN_RABBIT;
    public static String HEAD_SPAWN_SHEEP;
    public static String HEAD_SPAWN_SILVERFISH;
    public static String HEAD_SPAWN_SKELETON;
    public static String HEAD_SPAWN_SLIME;
    public static String HEAD_SPAWN_SNOWMAN;
    public static String HEAD_SPAWN_SPIDER;
    public static String HEAD_SPAWN_SQUID;
    public static String HEAD_SPAWN_VILLAGER;
    public static String HEAD_SPAWN_ZOMBIE_VILLAGER;
    public static String HEAD_SPAWN_WITCH;
    public static String HEAD_SPAWN_WITHER;
    public static String HEAD_SPAWN_WOLF;
    public static String HEAD_SPAWN_ZOMBIE;
    public static String HEAD_SPAWN_POLAR_BEAR;
    public static String HEAD_SPAWN_SHULKER;
    public static String HEAD_SPAWN_ENDERMITE;
    public static String HEAD_SPAWN_STRAY;    
    public static String HEAD_SPAWN_EVOKER;
    public static String HEAD_SPAWN_ILLUSIONER;
    public static String HEAD_SPAWN_VINDICATOR;
    public static String HEAD_SPAWN_WITHER_BOSS;
    public static String HEAD_SPAWN_VEX;
    public static String HEAD_SPAWN_LLAMA;
    public static String HEAD_SPAWN_PARROT;
    public static String HEAD;
    public static String OPT_AMOUNT_OPTIONAL;
    public static String OPT_HEADNAME_OPTIONAL;
    public static String OPT_HEADNAME_REQUIRED;
    public static String OPT_RECEIVER_OPTIONAL;
    public static String OPT_RECEIVER_REQUIRED;
    public static String OPT_VALUE_OPTIONAL;
    public static String OPT_VARIABLE_REQUIRED;
    public static String RENAMED_HEAD;
    public static String SPACE;
    public static String SPAWNED_HEAD;
    public static String SUBCOMMANDS;
    public static String SYNTAX;
    public static String UPDATE1;
    public static String UPDATE3;

    private Lang() {
    }

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

    public static void init(Plugin instance) {
        plugin = instance;
        reload();
    }

    public static void reload() {
        String locale = "";
        try {
            RESOURCE_BUNDLE0 = ResourceBundle.getBundle(BUNDLE_NAME, new UTF8Control());
            locale = RESOURCE_BUNDLE0.getLocale().toString();
            if (!(locale.equals(""))) {
                locale = "_".concat(locale);
            }
        } catch (MissingResourceException ignored) {
        }

        try {
            URL[] urls = {plugin.getDataFolder().toURI().toURL()};
            RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault(), new URLClassLoader(urls), new UTF8Control());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (MissingResourceException e) {
            plugin.saveResource(BUNDLE_NAME.concat(locale).replace('.', '/').concat(".properties"), false);
            try {
                URL[] urls = {plugin.getDataFolder().toURI().toURL()};
                RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault(), new URLClassLoader(urls), new UTF8Control());
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
