/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.antispam;

import com.github.crashdemons.playerheads.testutils.TestOutput;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class SpamResultTest {
    
    private final TestOutput out=new TestOutput(this);
    public SpamResultTest() {
    }

    @Test
    public void testIsSpam() {
        out.println("testSpamResult isSpam");
        SpamResult instance = new SpamResult(false);
        boolean expResult = false;
        boolean result = instance.isSpam();
        assertEquals(expResult, result);
    }

    @Test
    public void testToggle() {
        out.println("testSpamResult toggle");
        SpamResult instance = new SpamResult(false);
        instance.toggle();
        assertEquals(true, instance.isSpam());
        instance.toggle();
        assertEquals(false, instance.isSpam());
    }
    
}
