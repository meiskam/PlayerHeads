/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.playerheads;

import com.github.crashdemons.playerheads.Mocks;
import com.github.crashdemons.playerheads.TexturedSkullType;
import org.bukkit.Bukkit;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author crash
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class})
@Ignore
public class ConfigTest {
    
    public ConfigTest() {
        Mocks.setupFakeServerVersion();
    }

    @Test
    public void testSkullConfigChanges() {
        System.out.println("testSkullConfigChanges");
        for (TexturedSkullType skullType : TexturedSkullType.values()) {
                if(skullType==TexturedSkullType.PLAYER) continue;
                String oldconfig = skullType.name().replace("_", "").toLowerCase() + "droprate";
                if(Config.configKeys.get(oldconfig)==null){
                    fail("skull drop config entry mismatch: "+oldconfig+" -> "+skullType.getConfigName());
                }
        }
    }
    
}
