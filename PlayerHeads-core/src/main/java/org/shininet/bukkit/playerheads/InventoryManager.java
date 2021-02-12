/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.playerheads;

import com.github.crashdemons.playerheads.SkullConverter;
import com.github.crashdemons.playerheads.SkullManager;
import com.github.crashdemons.playerheads.TexturedSkullType;
import com.github.crashdemons.playerheads.compatibility.Compatibility;
import com.github.crashdemons.playerheads.compatibility.CompatibleProfile;
import com.github.crashdemons.playerheads.compatibility.SkullBlockAttachment;
import com.github.crashdemons.playerheads.compatibility.SkullDetails;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Defines a collection of utility methods for the plugin inventory management, item management
 * @author meiskam
 */
public final class InventoryManager {
    private InventoryManager(){}
    
    /**
     * Adds a head-item to a player's inventory.
     * 
     * The quantity of heads added to the inventory is controlled by Config.defaultStackSize (usually 1).
     * @param player the player receiving the head.
     * @param skullOwner the owner username of the head-item, or the "spawn" string for a supported skull.
     * @param usevanillaskulls whether vanilla mobheads are permitted (if relevant to the owner parameter).
     * @param addLore controls whether lore text can be added to the head by the plugin
     * @return true: the head was added successfully. false: there was no empty inventory slot to add the item.
     * @see Config#defaultStackSize
     */
    @SuppressWarnings("unused")
    public static boolean addHead(Player player, String skullOwner, boolean usevanillaskulls, boolean addLore) {
        return addHead(player, skullOwner, Config.defaultStackSize, usevanillaskulls, addLore);
    }

    /**
     * Adds a head-item to a player's inventory.
     * @param player the player receiving the head.
     * @param skullOwner the owner username of the head-item, or the "spawn" string for a supported skull.
     * @param quantity the number of this item to add
     * @param usevanillaskulls whether vanilla mobheads are permitted (if relevant to the owner parameter).
     * @param addLore controls whether lore text can be added to the head by the plugin
     * @return true: the head was added successfully. false: there was no empty inventory slot to add the item.
     */
    public static boolean addHead(Player player, String skullOwner, int quantity, boolean usevanillaskulls,boolean addLore) {
        PlayerInventory inv = player.getInventory();
        int firstEmpty = inv.firstEmpty();
        if (firstEmpty == -1) {
            return false;
        } else {
            inv.setItem(firstEmpty, SkullManager.spawnSkull(skullOwner, quantity, usevanillaskulls, addLore));
            return true;
        }
    }
    
    public static boolean setBlock(World w, int x, int y, int z, SkullBlockAttachment attachment, String skullOwner, BlockFace facing, boolean usevanillaskulls){
        ItemStack stack = SkullManager.spawnSkull(skullOwner, 1, usevanillaskulls, false);
        
        Location loc = new Location(w,x,y,z);
        
        
        PlayerHeads.instance.getLogger().info("setblock "+attachment+" "+facing);//TODO: debug
        
        ItemMeta meta = stack.getItemMeta();
        CompatibleProfile profile = null;
        if(meta instanceof SkullMeta) profile = Compatibility.getProvider().getCompatibleProfile(meta);
        
        if(CompatibleProfile.isValid(profile)){//TODO: DEBUG
            PlayerHeads.instance.getLogger().warning("setblock Profile from itemstack is null or invalid");//TODO:DEBUG
        }else{
            PlayerHeads.instance.getLogger().info("setblock profile "+profile.getName()+" "+profile.getId()+" "+profile.getTextures());//TODO: debug
        }
        
        
        TexturedSkullType skullType = SkullConverter.skullTypeFromItemStack(stack,false,false);
        if(skullType==null){//not a known type of skull - possibly internal error since we spawned it ourself?
            return false;
        }
        
        //TODO: set block facing direction???
        SkullDetails skullImplementation = skullType.getImplementationDetails();
        
        if(!SkullBlockAttachment.isValidOrientation(facing,attachment)){
            PlayerHeads.instance.getLogger().warning("setblock invalid orientation");//TODO:DEBUG
            return false;
        }
        
        Block block = skullImplementation.setBlock(loc, facing, attachment);
        if(block == null){//cannot get block for position
            PlayerHeads.instance.getLogger().warning("setblock block returned null");//TODO:DEBUG
            return false;
        }
        
        BlockState state = block.getState();
        if(state instanceof Skull && CompatibleProfile.isValid(profile)){
            boolean success = Compatibility.getProvider().setCompatibleProfile(state, profile);
            if(!success){//TODO: DEBUG
                PlayerHeads.instance.getLogger().warning("setblock Profile setting on block failed");//TODO:DEBUG
            }else{
                PlayerHeads.instance.getLogger().info("setblock setprofile succeeded");//TODO: debug
                state.update();
            }
        }else{
            PlayerHeads.instance.getLogger().warning("Blockstate wasn't a skull or profile wasn't valid "+(state instanceof Skull)+" "+CompatibleProfile.isValid(profile));//TODO: DEBUG
        }
        
        return true;
    }
}
