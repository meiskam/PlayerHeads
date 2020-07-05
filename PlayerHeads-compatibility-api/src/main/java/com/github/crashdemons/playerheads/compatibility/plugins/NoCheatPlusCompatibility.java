/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.plugins;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Defines required operations performed with the NoCheatPlus plugin for compatibility reasons.
 * @author crashdemons (crashenator at gmail.com)
 */
public class NoCheatPlusCompatibility extends CompatiblePlugin {
    /**
     * Create the NCP plugin-compatibility class
     * @param parentPlugin the plugin requesting the compatibility support (provides logging/events)
     */
    public NoCheatPlusCompatibility(Plugin parentPlugin) {
        super(parentPlugin,"NoCheatPlus");
    }
    
    /**
     * Checks if a user is exempt from fast-break checks
     * @param player the user
     * @return whether the user is exempt
     */
    public boolean isExemptFastbreak(Player player){
        return NCPExemptionManager.isExempted(player, CheckType.BLOCKBREAK_FASTBREAK);
    }
    
    /**
     * Adds an exemption for a user from fast-break checks
     * @param player the user
     */
    public void exemptFastbreak(Player player){
        NCPExemptionManager.exemptPermanently(player, CheckType.BLOCKBREAK_FASTBREAK);
    }
    /**
     * Removes exemptions for a user for fast-break checks
     * @param player the user
     */
    public void unexemptFastbreak(Player player){
        NCPExemptionManager.unexempt(player, CheckType.BLOCKBREAK_FASTBREAK);
    }
    
}


/*
                    if (isNotExempt = !NCPExemptionManager.isExempted(player, CheckType.BLOCKBREAK_FASTBREAK)) {
                        NCPExemptionManager.exemptPermanently(player, CheckType.BLOCKBREAK_FASTBREAK);
                    }
*/