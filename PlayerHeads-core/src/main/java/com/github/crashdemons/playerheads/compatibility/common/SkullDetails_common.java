/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.common;

import com.github.crashdemons.playerheads.compatibility.SkullDetails;
import com.github.crashdemons.playerheads.compatibility.SkullType;
import org.bukkit.Material;

/**
 * SkullDetails implementation for 1.8+ support
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public abstract class SkullDetails_common implements SkullDetails {

    protected Material materialItem;
    protected SkullType skullType;

    @Override
    public boolean isSkinnable() {
        return isBackedByPlayerhead();
    }

    @Override
    public Material getItemMaterial() {
        return materialItem;
    }
}
