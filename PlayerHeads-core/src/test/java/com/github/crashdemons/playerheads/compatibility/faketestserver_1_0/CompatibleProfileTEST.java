/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.faketestserver_1_0;

import com.github.crashdemons.playerheads.compatibility.CompatibleProfile;
import java.util.UUID;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class CompatibleProfileTEST extends CompatibleProfile {
    
    public CompatibleProfileTEST(){
        super();
    }
    
    public CompatibleProfileTEST(UUID id, String name){
        super(id,name);
    }
    public CompatibleProfileTEST(Object internal){
        setFromInternalObject(internal);
    }
    
    public Object toInternalObject(){//use this class as he basic type, no lower object.
        return this;
    }
    
    public void setFromInternalObject(Object test){
        if(!(test instanceof CompatibleProfileTEST)) throw new IllegalArgumentException("invalid profile type");
        CompatibleProfileTEST profile = (CompatibleProfileTEST) test;
        this.id = profile.id;
        this.name = profile.name;
        this.textures = profile.textures;
    }
}
