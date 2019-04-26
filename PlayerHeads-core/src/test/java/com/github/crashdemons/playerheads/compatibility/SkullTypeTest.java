/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility;

import com.github.crashdemons.playerheads.testutils.TestOutput;
import org.bukkit.Material;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class SkullTypeTest {
    private final TestOutput out=new TestOutput(this);
    
    public SkullTypeTest() {
    }
    
    @Test
    public void test_isSkull_invalid(){
        out.println("isSkull");
        assertEquals(false,SkullType.CREEPER.isSkull);
    }
    @Test
    public void test_isSkull_valid(){
        out.println("isSkull");
        assertEquals(true,SkullType.SKELETON.isSkull);
    }
    @Test
    public void testSkullNameMaterialCorrespondence(){
        out.println("SkullNameMaterialCorrespondence");
        for(SkullType st : SkullType.values()){
            Material mat,matw;
            if(st.isSkull){
                mat = RuntimeReferences.getMaterialByName(st.name()+"_SKULL");
                matw = RuntimeReferences.getMaterialByName(st.name()+"_WALL_SKULL");
                
            }else{
                mat = RuntimeReferences.getMaterialByName(st.name()+"_HEAD");
                matw = RuntimeReferences.getMaterialByName(st.name()+"_WALL_HEAD");
            }
            out.println("   "+st.name()+" => "+mat.name()+" "+matw.name());
        }
    }
    @Test
    public void testOrdinalCompatibility(){
        out.println("OrdinalCompatibility");
        for(SkullType st : SkullType.values()){
            org.bukkit.SkullType st_legacy = org.bukkit.SkullType.values()[ st.ordinal() ];
            out.println("   "+st.name()+" => "+st_legacy.name());
        }
    }
    
}
