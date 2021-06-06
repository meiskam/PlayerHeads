/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.playerheads.events.modifiers;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class DropRateModifierTest {
    
    private static final double TOLERANCE = 0.0001;
    
    public DropRateModifierTest() {
    }

    @Test
    public void testGetLevel() {
        System.out.println("getLevel");
        assertEquals(1, new DropRateModifier(DropRateModifierType.ADD_CONSTANT,0.035).getLevel());
        assertEquals(7, new DropRateModifier(DropRateModifierType.ADD_CONSTANT,0.035,7).getLevel());
        assertEquals(8, new DropRateModifier(DropRateModifierType.ADD_MULTIPLE_PER_LEVEL,0.035,8).getLevel());
    }
/*
    @Test
    public void testGetType() {
        System.out.println("getType");
        DropRateModifier instance = null;
        DropRateModifierType expResult = null;
        DropRateModifierType result = instance.getType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetValue() {
        System.out.println("getValue");
        DropRateModifier instance = null;
        double expResult = 0.0;
        double result = instance.getValue();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
    @Test
    public void testGetAdditiveMultiplierValue() {
        System.out.println("getAdditiveMultiplierValue");
        assertEquals(null, new DropRateModifier(DropRateModifierType.ADD_CONSTANT,0.035).getAdditiveMultiplierValue());
        assertEquals(0.5, (double) new DropRateModifier(DropRateModifierType.MULTIPLY,1.5).getAdditiveMultiplierValue(),TOLERANCE);
        assertEquals( (-0.5), (double) new DropRateModifier(DropRateModifierType.MULTIPLY,0.5).getAdditiveMultiplierValue(),TOLERANCE);
        assertEquals(0.02, (double) new DropRateModifier(DropRateModifierType.ADD_MULTIPLE,0.02).getAdditiveMultiplierValue(),TOLERANCE);
        assertEquals(0.013, (double) new DropRateModifier(DropRateModifierType.ADD_MULTIPLE_PER_LEVEL,0.013).getAdditiveMultiplierValue(),TOLERANCE);
        assertEquals( (0.014*3), (double) new DropRateModifier(DropRateModifierType.ADD_MULTIPLE_PER_LEVEL,0.014,3).getAdditiveMultiplierValue(),TOLERANCE);
    }

    @Test
    public void testGetMultiplierValue() {
        System.out.println("getMultiplierValue");
        assertEquals(null, new DropRateModifier(DropRateModifierType.ADD_CONSTANT,0.035).getMultiplierValue());
        assertEquals((Double) 1.5, new DropRateModifier(DropRateModifierType.MULTIPLY,1.5).getMultiplierValue(),TOLERANCE);
        assertEquals((Double) 0.5, new DropRateModifier(DropRateModifierType.MULTIPLY,0.5).getMultiplierValue(),TOLERANCE);
        assertEquals((Double) 1.02, new DropRateModifier(DropRateModifierType.ADD_MULTIPLE,0.02).getMultiplierValue(),TOLERANCE);
        assertEquals((Double) 1.013, new DropRateModifier(DropRateModifierType.ADD_MULTIPLE_PER_LEVEL,0.013).getMultiplierValue(),TOLERANCE);
        assertEquals((Double) (1+(0.014*3)), new DropRateModifier(DropRateModifierType.ADD_MULTIPLE_PER_LEVEL,0.014,3).getMultiplierValue(),TOLERANCE);
    }

    @Test
    public void testApply() {
        double droprate = 0.01;
        assertEquals((Double) 0.045, (Double) new DropRateModifier(DropRateModifierType.ADD_CONSTANT,0.035).apply(droprate),TOLERANCE);
        assertEquals((Double) 0.015, (Double) new DropRateModifier(DropRateModifierType.MULTIPLY,1.5).apply(droprate),TOLERANCE);
        assertEquals((Double) 0.005, (Double) new DropRateModifier(DropRateModifierType.MULTIPLY,0.5).apply(droprate),TOLERANCE);
        assertEquals((Double) 0.0102, (Double) new DropRateModifier(DropRateModifierType.ADD_MULTIPLE,0.02).apply(droprate),TOLERANCE);
        assertEquals((Double) 0.01013, (Double) new DropRateModifier(DropRateModifierType.ADD_MULTIPLE_PER_LEVEL,0.013).apply(droprate),TOLERANCE);
        assertEquals((Double) ((1+(0.014*3))*0.01),(Double) new DropRateModifier(DropRateModifierType.ADD_MULTIPLE_PER_LEVEL,0.014,3).apply(droprate),TOLERANCE);
    
        assertEquals(0.12974400000000003, new DropRateModifier(DropRateModifierType.MULTIPLY,1.5).apply(0.08649600000000002),TOLERANCE);
    }
    
}
