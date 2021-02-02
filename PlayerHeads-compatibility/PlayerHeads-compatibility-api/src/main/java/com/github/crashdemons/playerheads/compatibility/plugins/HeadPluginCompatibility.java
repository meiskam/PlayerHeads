/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.plugins;

import com.github.crashdemons.playerheads.compatibility.Compatibility;
import com.github.crashdemons.playerheads.compatibility.CompatibleProfile;
import com.github.crashdemons.playerheads.compatibility.plugins.heads.HeadModificationHandling;
import com.github.crashdemons.playerheads.compatibility.plugins.heads.ExternalHeads;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class HeadPluginCompatibility extends CompatiblePlugin {
    
    public HeadPluginCompatibility(Plugin parentPlugin){
        super(parentPlugin,"");
    }
    
    public HeadPluginCompatibility(Plugin parentPlugin, ConfigurationSection config){
        super(parentPlugin,"",config);
    }
    
    
    @Override
    public void reloadConfig(){
        ExternalHeads.loadNamesFromConfig(config, "ignoredheadnames", HeadModificationHandling.NO_INTERACTION);
        ExternalHeads.loadIdsFromConfig(config, "ignoredheaduuids", HeadModificationHandling.NO_INTERACTION);
    }
    
    @NotNull
    public HeadModificationHandling getExternalHeadHandling(String ownerName, UUID ownerID){
       HeadModificationHandling handling = ExternalHeads.getHandling(ownerName);
       if(handling==null) handling=ExternalHeads.getHandling(ownerID);
       if(handling==null) handling=HeadModificationHandling.NORMAL;
       
       if(Compatibility.getProvider().isCustomHead(ownerName, ownerId)){
           return HeadModificationHandling.NO_INTERACTION;
       }

       return handling;
    }
    
    
    @NotNull
    public HeadModificationHandling getExternalHeadHandling(BlockState state){
       if(!(state instanceof Skull)) return HeadModificationHandling.NORMAL;
       String ownerName = Compatibility.getProvider().getOwnerDirect((Skull) state);
       OfflinePlayer owner = Compatibility.getProvider().getOwningPlayerDirect((Skull) state);
       UUID ownerID = null;
       if(owner!=null) ownerID = owner.getUniqueId();
       return getExternalHeadHandling( ownerName,  ownerID);
    }
    
    @NotNull
    public HeadModificationHandling getExternalHeadHandling(ItemStack stack){
       if(!stack.hasItemMeta()) return HeadModificationHandling.NORMAL;
       ItemMeta meta = stack.getItemMeta();
       if(!(meta instanceof SkullMeta)) return HeadModificationHandling.NORMAL;
       String ownerName = Compatibility.getProvider().getOwnerDirect((SkullMeta) meta);
       OfflinePlayer owner = Compatibility.getProvider().getOwningPlayerDirect((SkullMeta) meta);
       UUID ownerID = null;
       if(owner!=null) ownerID = owner.getUniqueId();
       return getExternalHeadHandling( ownerName,  ownerID);
    }
    
}
