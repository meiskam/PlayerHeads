/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.plugins;

import com.github.crashdemons.playerheads.compatibility.Compatibility;
import com.github.crashdemons.playerheads.compatibility.plugins.heads.ExternalHeadHandling;
import com.github.crashdemons.playerheads.compatibility.plugins.heads.ExternalHeadType;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class HeadPluginCompatibility extends CompatiblePlugin {
    public HeadPluginCompatibility(Plugin parentPlugin){
        super(parentPlugin,"");
    }
    
    @Nullable
    public ExternalHeadType getExternalHead(String ownerName, UUID ownerID){
        ExternalHeadType result = ExternalHeadType.get(ownerID);
        if(result!=null) return result;
        result = ExternalHeadType.get(ownerName);
        return result;
    }
    
    @NotNull
    public ExternalHeadHandling getExternalHeadHandling(String ownerName, UUID ownerID){
       ExternalHeadType head = getExternalHead(ownerName,ownerID);
       if(head==null) return ExternalHeadHandling.NORMAL;
       return head.getHandling();
    }
    
    @NotNull
    public ExternalHeadHandling getExternalHeadHandling(BlockState state){
       if(!(state instanceof Skull)) return ExternalHeadHandling.NORMAL;
       String ownerName = Compatibility.getProvider().getOwnerDirect((Skull) state);
       OfflinePlayer owner = Compatibility.getProvider().getOwningPlayerDirect((Skull) state);
       UUID ownerID = null;
       if(owner!=null) ownerID = owner.getUniqueId();
       return getExternalHeadHandling( ownerName,  ownerID);
    }
    
    @NotNull
    public ExternalHeadHandling getExternalHeadHandling(ItemStack stack){
       if(!stack.hasItemMeta()) return ExternalHeadHandling.NORMAL;
       ItemMeta meta = stack.getItemMeta();
       if(!(meta instanceof SkullMeta)) return ExternalHeadHandling.NORMAL;
       String ownerName = Compatibility.getProvider().getOwnerDirect((SkullMeta) meta);
       OfflinePlayer owner = Compatibility.getProvider().getOwningPlayerDirect((SkullMeta) meta);
       UUID ownerID = null;
       if(owner!=null) ownerID = owner.getUniqueId();
       return getExternalHeadHandling( ownerName,  ownerID);
    }
    
}
