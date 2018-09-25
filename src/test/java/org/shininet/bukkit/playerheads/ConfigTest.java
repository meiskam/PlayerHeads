/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.playerheads;

import com.github.crashdemons.playerheads.SkullConverter;
import com.github.crashdemons.playerheads.TexturedSkullType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author crash
 */
public class ConfigTest {
    
    public ConfigTest() {
    }

    @Test
    public void testSkullConfigChanges() {
        System.out.println("testSkullConfigChanges");
        for (TexturedSkullType skullType : TexturedSkullType.values()) {
                if(skullType==TexturedSkullType.PLAYER) continue;
                String oldconfig = skullType.name().replace("_", "").toLowerCase() + "droprate";
                if(Config.configKeys.get(oldconfig)==null){
                    fail("skull drop config entry mismatch: "+oldconfig+" -> "+SkullConverter.dropConfigFromSkullType(skullType));
                }
        }
    }
    
}
