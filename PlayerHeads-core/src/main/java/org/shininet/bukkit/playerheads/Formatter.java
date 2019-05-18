/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.playerheads;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Defines a collection of utility methods for the plugin message formatting
 *
 * @author meiskam
 */
public final class Formatter {
    private Formatter(){}

    /**
     * Formats a message with arguments with a list of parameters to use as
     * replacements for those arguments.
     * <p>
     * Note: this method also translates ampersand-encoded color-codes.
     *
     * @param text A string, potentially with arguments for replacement.
     * @param replacement a string or set of strings used to fill the
     * replacement arguments.
     * @return The formatted string after replacement.
     */
    public static String format(String text, String... replacement) {
        String output = text;
        for (int i = 0; i < replacement.length; i++) {
            if (output != null) {
                if (replacement[i] == null) {

                    replacement[i] = "<null:" + replacement.length + ":>";
                }
                output = output.replace("%" + (i + 1) + "%", replacement[i]);
            }
        }
        //noinspection ConstantConditions
        return ChatColor.translateAlternateColorCodes('&', output);
    }

    /**
     * Formats a message with replacement arguments and transmits the message to
     * a player.
     * <p>
     * See format() for more details.
     *
     * @param player The player to send the message to
     * @param text the string to be formatted
     * @param replacement the replacement string or strings used to fill the
     * replacement arguments.
     * @see #format(java.lang.String, java.lang.String...)
     */
    public static void formatMsg(CommandSender player, String text, String... replacement) {
        player.sendMessage(format(text, replacement));
    }

    /**
     * Formats a message with replacement arguments and then strips color-codes
     * from it.
     * <p>
     * See format() for more details.
     *
     * @param text the string to be formatted
     * @param replacement the replacement string or strings used to fill the
     * replacement arguments.
     * @return The formatted string after replacement and color-stripping.
     * @see #format(java.lang.String, java.lang.String...)
     * @see org.bukkit.ChatColor#stripColor(java.lang.String)
     */
    public static String formatStrip(String text, String... replacement) {
        return ChatColor.stripColor(format(text, replacement));
    }

}
