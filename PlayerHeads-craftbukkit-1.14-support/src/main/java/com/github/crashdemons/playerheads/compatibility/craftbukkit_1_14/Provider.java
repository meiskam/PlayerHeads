/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.craftbukkit_1_14;

import com.github.crashdemons.playerheads.compatibility.CompatibilityProvider;
import org.bukkit.entity.Entity;

/**
 * CompatibilityProvider Implementation for 1.13+ support
 * @author crashdemons (crashenator at gmail.com)
 */
@SuppressWarnings( "deprecation" )
public class Provider extends com.github.crashdemons.playerheads.compatibility.craftbukkit_1_13.Provider implements CompatibilityProvider {
    public Provider(){}
    @Override public String getType(){ return "craftbukkit"; }
    @Override public String getVersion(){ return "1.14"; }
    
    //note: I am overriding the Common provider, but not 1.13 provider and not the interface - I can't add @Override without an error
    protected boolean isLegacyCat(Entity e){//remove the necessity for this check in 1.14
        return false;
    }
}
