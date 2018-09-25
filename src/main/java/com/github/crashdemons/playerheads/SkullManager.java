
package com.github.crashdemons.playerheads;

import java.util.UUID;
import java.lang.reflect.Field;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import org.shininet.bukkit.playerheads.Config;

/**
 * Defines an abstract class of methods for creating, updating, and applying information to heads managed by the plugin.
 * @author crash
 * @author x7aSv
 */
public final class SkullManager {
    
    private SkullManager(){}
    
    /**
     * Applies Lore text (including the PlayerHeads plugin name) to a playerhead's meta.
     * @param headMeta The SkullMeta associated with the playerhead to modify
     * @param extra Extra lore text to display under the "PlayerHeads" line.
     */
    private static void applyLore(SkullMeta headMeta,String extra){
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(" ");
        lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"PlayerHeads");
        if(!extra.isEmpty()) lore.add(extra);
        headMeta.setLore(lore);
    }
    
    /**
     * Sets an owning player to a playerhead
     * @param headMeta  The SkullMeta associated with the playerhead to modify
     * @param owner The OfflinePlayer owning to own the head.
     */
    private static void applyOwningPlayer(SkullMeta headMeta,OfflinePlayer owner){
        headMeta.setOwningPlayer( owner );
    }
    /**
     * Sets a display name for the playerhead item's meta
     * @param headMeta The SkullMeta associated with the playerhead to modify
     * @param display The string containing the display name to set
     */
    private static void applyDisplayName(SkullMeta headMeta,String display){
        headMeta.setDisplayName(display);
    }
    
    /**
     * Applies a texture-url to a playerhead's meta.
     * @param headMeta The SkullMeta associated with the playerhead to modify
     * @param uuid A UUID to associate with the head and texture
     * @param texture The Base64-encoded Texture-URL tags.
     * @return true: the information was properly set on the playerhead; false: there was an error setting the profile field.
     * @author x7aSv
     */
    private static boolean applyTexture(SkullMeta headMeta, UUID uuid, String texture){//credit to x7aSv
        //System.out.println("Applying texture...");
        GameProfile profile = new GameProfile(uuid, null);
        profile.getProperties().put("textures", new Property("textures", texture));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
            return false;
        }
       // System.out.println("done applying.");
        return true;
    }
    
    /**
     * Creates a stack of heads for the specified Mob's SkullType.
     * 
     * The quantity of heads will be defined by Config.defaultStackSize (usually 1)
     * 
     * @param type The TexturedSkullType to create heads of.
     * @param useVanillaHeads Whether to permit vanilla head-items to be used in place of custom playerheads for supported mobs.
     * @return The ItemStack of heads desired.
     * @see org.shininet.bukkit.playerheads.Config#defaultStackSize
     */
    public static ItemStack MobSkull(TexturedSkullType type, boolean useVanillaHeads){
        return MobSkull(type,Config.defaultStackSize, useVanillaHeads);
    }
    
    /**
     * Creates a stack of heads for the specified Mob's SkullType
     * @param type The TexturedSkullType to create heads of.
     * @param quantity the number of heads to create in the stack
     * @param useVanillaHeads Whether to permit vanilla head-items to be used in place of custom playerheads for supported mobs.
     * @return The ItemStack of heads desired.
     */
    public static ItemStack MobSkull(TexturedSkullType type,int quantity,boolean useVanillaHeads){
        Material mat = type.getMaterial();
        
        
        if(type.hasDedicatedItem()){
            if(useVanillaHeads)
                return new ItemStack(mat,quantity);
            else mat=Material.PLAYER_HEAD;
        }
        
        //System.out.println("Player-head");
        ItemStack stack = new ItemStack(mat,quantity);
        SkullMeta headMeta = (SkullMeta) stack.getItemMeta();
        //applyOwningPlayer(headMeta,Bukkit.getOfflinePlayer(type.getOwner()));
        applyTexture(headMeta,type.getOwner(),type.getTexture());
        applyDisplayName(headMeta,ChatColor.RESET + "" + ChatColor.YELLOW + type.getDisplayName());
        applyLore(headMeta,ChatColor.GREEN+"Mob Drop");
        stack.setItemMeta(headMeta);
        return stack;
    }
    private static ItemStack PlayerSkull(OfflinePlayer owner){
        return PlayerSkull(owner,Config.defaultStackSize);
    }
    private static ItemStack PlayerSkull(OfflinePlayer owner, int quantity){
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD,quantity);
        SkullMeta headMeta = (SkullMeta) stack.getItemMeta();
        String name=null;
        if(owner!=null){
            applyOwningPlayer(headMeta,owner);
            name = owner.getName();
        }
        if(name==null) name="Unknown";//only used for display purposes.
        applyDisplayName(headMeta,ChatColor.RESET + "" + ChatColor.YELLOW + TexturedSkullType.getDisplayName(name));
        applyLore(headMeta,ChatColor.RED+"Player Drop");
        stack.setItemMeta(headMeta);
        return stack;
    }
    
    /**
     * Creates a stack of playerheads for the given username.
     * 
     * The username given is translated to an OfflinePlayer by bukkit at the time of creation.
     * 
     * Note: if the owner name is null or can't be translated to a player internally, a generic playerhead will be created with the name "Unknown".
     * 
     * The quantity of heads will be defined by Config.defaultStackSize (usually 1)
     * @param owner The username to create a head for.
     * @return The ItemStack of heads desired.
     * @see org.shininet.bukkit.playerheads.Config#defaultStackSize
     */
    public static ItemStack PlayerSkull(String owner){
        return PlayerSkull(owner,Config.defaultStackSize);
    }
    /**
     * Creates a stack of playerheads for the given username.
     * 
     * The username given is translated to an OfflinePlayer by bukkit at the time of creation.
     * 
     * @param owner The username to create a head for.
     * @param quantity The number of heads to create in the stack.
     * @return The ItemStack of heads desired.
     */
    public static ItemStack PlayerSkull(String owner, int quantity){
        OfflinePlayer op = Bukkit.getOfflinePlayer(owner);
        return PlayerSkull(op,quantity);
    }
    
    /**
     * Updates the blockstate of a head.
     * 
     * Originally this method also updated legacy username-based skulls to the correct owner - currently it only updates the blockstate.
     * Since these skulls are upgraded and textured-skulls have embedded textures, this method may not be necessary and may be removed in the future.
     * 
     * @param skullState the blockstate to update.
     */
    public static void updatePlayerSkullState(BlockState skullState){
        //for a skull belonging to a player drop, this shouldn't really be necessary to reset the owner.
        //and for textured mobheads, the texture is embedded, so shouldn't need updating...
        /*
        OfflinePlayer op = skullState.getOwningPlayer();
        String owner=null;
        if(op!=null) owner=op.getName();
        if(owner==null) owner=skullState.getOwner();//this is deprecated, but the above method does NOT get the name tag from the NBT.
        if(owner==null) return;

        skullState.setOwningPlayer(Bukkit.getOfflinePlayer(skullType.getOwner()));
        */
        skullState.update();
    }
    /*
    //TODO: reinvestigate these approaches as they're preferred to the deprecated username method.
    //these do not properly update head skin in server testing.
    public static ItemStack PlayerSkull(UUID owner){
        return PlayerSkull(owner,Config.defaultStackSize);
    }
    
    public static ItemStack PlayerSkull(UUID owner, int quantity){
        OfflinePlayer op = Bukkit.getOfflinePlayer(owner);
        //this is great but it doesn't update the texture
        return PlayerSkull(op,quantity);
    }
    */
}
