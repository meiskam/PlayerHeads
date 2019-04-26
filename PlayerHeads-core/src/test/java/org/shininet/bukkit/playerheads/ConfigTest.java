/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.playerheads;

import com.github.crashdemons.playerheads.testutils.Mocks;
import com.github.crashdemons.playerheads.testutils.TestOutput;
import com.github.crashdemons.playerheads.TexturedSkullType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author crash
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class})
public class ConfigTest {
    
    private final TestOutput out=new TestOutput(this);
    public ConfigTest() {
        Mocks.setupFakeServerSupport();
    }

    @Test
    public void testSkullConfigChanges() {
        out.println("testSkullConfigChanges");
        for (TexturedSkullType skullType : TexturedSkullType.values()) {
                if(skullType==TexturedSkullType.PLAYER) continue;
                String oldconfig = skullType.name().replace("_", "").toLowerCase() + "droprate";
                if(Config.configKeys.get(oldconfig)==null){
                    fail("skull drop config entry mismatch: "+oldconfig+" -> "+skullType.getConfigName());
                }
        }
    }
    
    @Test
    public void testConfigExistence() throws IOException,InvalidConfigurationException,URISyntaxException,FileNotFoundException{
        out.println("testConfigExistence");
        YamlConfiguration config = new YamlConfiguration();
        
        String path = new File( getClass().getProtectionDomain().getCodeSource().getLocation().toURI() ).getPath();
        out.println(path);
        File resource = new File(path+"/config.yml");

        config.load(resource);
        for (TexturedSkullType skullType : TexturedSkullType.values()) {
            String conf_entry = skullType.getConfigName();
            Object value = config.get(conf_entry);
            if(value==null){
                fail(" no config entry for "+conf_entry);
            }
        }
    }
}
