/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import com.github.crashdemons.playerheads.testutils.TestOutput;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class CompatibilitySupportTest {
    TestOutput out = new TestOutput(this);
    
    
    public CompatibilitySupportTest() {
    }

    @Test
    public void testIsFinalized() {
        out.println("check support was added correctly");
        boolean expResult = true;
        boolean result = CompatibilitySupport.isFinalized();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSupport(){
        out.println("check support actually exists for each implementation");
        for(Map.Entry<String,Integer[][]> serverSupport : CompatibilitySupport.versions.entrySet()){
            String serverType = serverSupport.getKey();
            Integer[][] serverVers = serverSupport.getValue();
            for (Integer[] serverVer : serverVers) {
                String packagename=serverType + "_" + serverVer[0]+"_"+serverVer[1];
                String classPath="com.github.crashdemons.playerheads.compatibility."+packagename;
                String classCanonical=classPath+".Provider";
                out.println("   testing for: "+classCanonical);
                assertEquals(true,RuntimeReferences.hasClass(classCanonical));
            }
        }
    }
    
}
