/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import com.github.crashdemons.playerheads.compatibility.faketestserver_1_0.Provider;
import com.github.crashdemons.playerheads.testutils.Mocks;
import com.github.crashdemons.playerheads.testutils.TestOutput;
import org.bukkit.Bukkit;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
@RunWith(PowerMockRunner.class)
public class CompatibilityTest {
    private final TestOutput out=new TestOutput(this);
    public CompatibilityTest() {
        Mocks.setupFakeServerVersion();
    }

    @Test
    @PrepareForTest({Compatibility.class,Bukkit.class})
    public void testInit() {
        out.println("init");
        assertEquals(false,Compatibility.init());
    }

    @Test
    @PrepareForTest({Compatibility.class,Bukkit.class})
    public void testIsProviderAvailable() {
        out.println("isProviderAvailable");
        assertEquals(false, Compatibility.isProviderAvailable());
        System.out.println(CompatibilitySupport.isFinalized());
        System.out.println(CompatibilitySupport.VERSIONS.toString());
        Compatibility.init();
        assertEquals(true, Compatibility.isProviderAvailable());
    }

    @Test
    @PrepareForTest({Compatibility.class,Bukkit.class})
    public void testGetProvider() {
        out.println("getProvider");
        Compatibility.init();
        assertEquals("faketestserver",Compatibility.getProvider().getType());
    }
    
    
    @Test
    @PrepareForTest({Compatibility.class,Bukkit.class})
    public void testRegisterProvider() {
        out.println("registerProvider");
        Provider pro = new Provider();
        Compatibility.registerProvider(pro);
        assertEquals("faketestserver",Compatibility.getProvider().getType());
    }


    @Test
    @PrepareForTest({Compatibility.class,Bukkit.class})
    public void testGetRecommendedProviderType() {
        out.println("getRecommendedProviderType");
        String expResult = "faketestserver";
        Compatibility.init();
        String result = Compatibility.getRecommendedProviderType();
        assertEquals(expResult, result);
    }

    @Test
    @PrepareForTest({Compatibility.class,Bukkit.class})
    public void testGetRecommendedProviderVersion() {
        out.println("getRecommendedProviderVersion");
        String expResult = "1.13";
        Compatibility.init();
        String result = Compatibility.getRecommendedProviderVersion();
        assertEquals(expResult, result);
        assertEquals("1.0",Compatibility.getProvider().getVersion());
    }
    
}
