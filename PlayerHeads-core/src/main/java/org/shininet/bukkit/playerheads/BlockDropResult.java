/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.playerheads;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
enum BlockDropResult {
    FAILED_EVENT_CANCELLED,
    FAILED_CUSTOM_HEAD,
    FAILED_BLOCKED_HEAD,
    FAILED_DEFERRED_TO_VANILLA,
    SUCCESS,
}
