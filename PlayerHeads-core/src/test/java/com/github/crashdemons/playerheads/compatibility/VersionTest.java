/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import com.github.crashdemons.playerheads.testutils.Mocks;
import com.github.crashdemons.playerheads.testutils.TestOutput;
import org.bukkit.Bukkit;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
@RunWith(PowerMockRunner.class)
public class VersionTest {
    
    private final TestOutput out=new TestOutput(this);
    public VersionTest() {
        Mocks.setupFakeServerVersion();
    }
    
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testInit() {
        out.println("init");
        Version.init();
    }
    
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testCheckAtLeast_equal() {
        out.println("checkAtLeast");
        int major = 1;
        int minor = 13;
        boolean expResult = true;
        boolean result = Version.checkAtLeast(major, minor);
        assertEquals(expResult, result);
    }
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testCheckAtLeast_over() {
        out.println("checkAtLeast");
        int major = 1;
        int minor = 14;
        boolean expResult = false;
        boolean result = Version.checkAtLeast(major, minor);
        assertEquals(expResult, result);
    }
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testCheckAtLeast_over2() {
        out.println("checkAtLeast");
        int major = 2;
        int minor = 13;
        boolean expResult = false;
        boolean result = Version.checkAtLeast(major, minor);
        assertEquals(expResult, result);
    }
    
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testCheckAtLeast_under() {
        out.println("checkAtLeast");
        Version.init();
        int major = 0;
        int minor = 9;
        boolean expResult = true;
        boolean result = Version.checkAtLeast(major, minor);
        assertEquals(expResult, result);
    }
    
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testCheckAtLeast_under2() {
        out.println("checkAtLeast");
        Version.init();
        int major = 0;
        int minor = 14;
        boolean expResult = true;
        boolean result = Version.checkAtLeast(major, minor);
        assertEquals(expResult, result);
    }
    
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testCheckUnder_under() {
        out.println("checkUnder");
        Version.init();
        int major = 0;
        int minor = 13;
        boolean expResult = false;
        boolean result = Version.checkUnder(major, minor);
        assertEquals(expResult, result);
    }
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testCheckUnder_under2() {
        out.println("checkUnder");
        Version.init();
        int major = 1;
        int minor = 12;
        boolean expResult = false;
        boolean result = Version.checkUnder(major, minor);
        assertEquals(expResult, result);
    }
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testCheckUnder_equal() {
        out.println("checkUnder");
        Version.init();
        int major = 0;
        int minor = 13;
        boolean expResult = false;
        boolean result = Version.checkUnder(major, minor);
        assertEquals(expResult, result);
    }
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testCheckUnder_over() {
        out.println("checkUnder");
        Version.init();
        int major = 1;
        int minor = 14;
        boolean expResult = true;
        boolean result = Version.checkUnder(major, minor);
        assertEquals(expResult, result);
    }
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testCheckUnder_over2() {
        out.println("checkUnder");
        Version.init();
        int major = 2;
        int minor = 13;
        boolean expResult = true;
        boolean result = Version.checkUnder(major, minor);
        assertEquals(expResult, result);
    }
    
    
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testCheckEquals_over() {
        out.println("checkEquals");
        Version.init();
        assertEquals(false,Version.checkEquals(1, 14));
    }
    
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testCheckEquals_over2() {
        out.println("checkEquals");
        Version.init();
        assertEquals(false,Version.checkEquals(2, 13));
    }
    
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testCheckEquals_equal() {
        out.println("checkEquals");
        Version.init();
        assertEquals(true,Version.checkEquals(1, 13));
    }
    
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testCheckEquals_under() {
        out.println("checkEquals");
        Version.init();
        assertEquals(false,Version.checkEquals(0, 13));
    }
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testCheckEquals_under2() {
        out.println("checkEquals");
        Version.init();
        assertEquals(false,Version.checkEquals(1, 12));
    }
    
    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testGetRawServerVersion() {
        out.println("getRawServerVersion");
        Version.init();
        Version.getRawServerVersion();
    }

    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testGetString() {
        out.println("getString");
        Version.init();
        String expResult = "1.13";
        String result = Version.getString();
        assertEquals(expResult, result);
    }

    @Test
    @PrepareForTest({Version.class,Bukkit.class})
    public void testGetType() {
        out.println("getType");
        Version.init();
        String expResult = "faketestserver";
        String result = Version.getType();
        assertEquals(expResult, result);
    }
    
}
