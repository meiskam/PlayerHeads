/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.plugins.heads;

/**
 * The Handling (or modifiability) that is recommended for a head from another plugin.
 * This currently just indicates whether the head should be treated as a supported player-owned head, or be avoided entirely to prevent plugin conflicts.
 * @author crashdemons (crashenator at gmail.com)
 */
public enum HeadModificationHandling {
    /**
     * Support the head as a normal player-owned or inbuilt head.
     */
    NORMAL,
    /**
     * Avoid any interaction or modification of this head to prevent plugin conflicts.
     */
    NO_INTERACTION,
}
