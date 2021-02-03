
package com.github.crashdemons.playerheads;

import com.github.crashdemons.playerheads.compatibility.Compatibility;
import com.github.crashdemons.playerheads.compatibility.CompatibleProfile;
import com.github.crashdemons.playerheads.compatibility.CompatibleSkullMaterial;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import org.shininet.bukkit.playerheads.Config;
import org.shininet.bukkit.playerheads.Lang;

/**
 * Defines an abstract class of methods for creating, updating, and applying information to heads managed by the plugin.
 * @author crash
 */
public final class SkullManager {
    
    private SkullManager(){}
    
    /**
     * Applies Lore text (including the PlayerHeads plugin name) to a playerhead's meta.
     * @param headMeta The SkullMeta associated with the playerhead to modify
     * @param extra Extra lore text to display under the "PlayerHeads" line.
     */
    private static void applyLore(SkullMeta headMeta,String extra){
        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");
        if(!Lang.LORE_PLUGIN_NAME.isEmpty()) lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+Lang.LORE_PLUGIN_NAME);
        if(!extra.isEmpty()) lore.add(extra);
        headMeta.setLore(lore);
    }
    
    /**
     * Sets an owning player to a playerhead
     * @param headMeta  The SkullMeta associated with the playerhead to modify
     * @param owner The OfflinePlayer owning to own the head.
     */
    private static void applyOwningPlayer(SkullMeta headMeta,OfflinePlayer owner){
        Compatibility.getProvider().setOwningPlayer(headMeta, owner);//headMeta.setOwningPlayer( owner );
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
    private static boolean applyTexture(SkullMeta headMeta, UUID uuid, String texture){
        return Compatibility.getProvider().setProfile(headMeta, uuid, texture);
    }
    
    /**
     * Creates a stack of heads for the specified Mob's SkullType.
     * 
     * The quantity of heads will be defined by Config.defaultStackSize (usually 1)
     * 
     * @param type The TexturedSkullType to create heads of.
     * @param useVanillaHeads Whether to permit vanilla head-items to be used in place of custom playerheads for supported mobs.
     * @param addLore controls whether any lore text should be added to the head (is currently applied only to custom heads).
     * @return The ItemStack of heads desired.
     * @see org.shininet.bukkit.playerheads.Config#defaultStackSize
     */
    public static ItemStack MobSkull(TexturedSkullType type, boolean useVanillaHeads, boolean addLore, CompatibleProfile profile){
        return MobSkull(type,Config.defaultStackSize, useVanillaHeads, addLore, profile);
    }
    
    /**
     * Creates a stack of heads for the specified Mob's SkullType
     * @param type The TexturedSkullType to create heads of.
     * @param quantity the number of heads to create in the stack
     * @param useVanillaHeads Whether to permit vanilla head-items to be used in place of custom playerheads for supported mobs.
     * @param addLore controls whether any lore text should be added to the head (is currently applied only to custom heads).
     * @return The ItemStack of heads desired.
     */
    public static ItemStack MobSkull(TexturedSkullType type,int quantity,boolean useVanillaHeads, boolean addLore, CompatibleProfile profile){
        CompatibleSkullMaterial mat = type.getCompatibleMaterial();
        
        
        if(type.hasDedicatedItem()){
            if(useVanillaHeads)
                return mat.getDetails().createItemStack(quantity);//new ItemStack(mat,quantity);
            else mat=CompatibleSkullMaterial.PLAYER;
        }
        
        //System.out.println("Player-head");
        ItemStack stack = mat.getDetails().createItemStack(quantity);//new ItemStack(mat,quantity);
        SkullMeta headMeta = (SkullMeta) stack.getItemMeta();
        //applyOwningPlayer(headMeta,Bukkit.getOfflinePlayer(type.getOwner()));
        applyTexture(headMeta,type.getOwner(),type.getTexture());
        applyDisplayName(headMeta,ChatColor.RESET + "" + ChatColor.YELLOW + type.getDisplayName());
        //System.out.println("DEBUG: addlore "+addLore);
        if(addLore) applyLore(headMeta,ChatColor.GREEN+Lang.LORE_HEAD_MOB);
        
        if(profile!=null && (profile.hasId() || profile.hasName())) Compatibility.getProvider().setCompatibleProfile(headMeta, profile);
        
        stack.setItemMeta(headMeta);
        return stack;
    }
    private static ItemStack PlayerSkull(OfflinePlayer owner, boolean addLore){
        return PlayerSkull(owner,Config.defaultStackSize, addLore, null);
    }
    private static ItemStack PlayerSkull(OfflinePlayer owner, int quantity, boolean addLore, CompatibleProfile profile){
        ItemStack stack = CompatibleSkullMaterial.PLAYER.getDetails().createItemStack(quantity);//new ItemStack(Material.PLAYER_HEAD,quantity);
        SkullMeta headMeta = (SkullMeta) stack.getItemMeta();
        String name=null;
        if(owner!=null){
            applyOwningPlayer(headMeta,owner);
            name = owner.getName();
        }
        if(name==null) name="Player";//only used for display purposes.
        applyDisplayName(headMeta,ChatColor.RESET + "" + ChatColor.YELLOW + TexturedSkullType.getDisplayName(name));
        //System.out.println("DEBUG: addlore "+addLore);
        if(addLore) applyLore(headMeta,ChatColor.RED+Lang.LORE_HEAD_PLAYER);
        if(profile!=null && (profile.hasId() || profile.hasName())) Compatibility.getProvider().setCompatibleProfile(headMeta, profile);//restore profile, if necessary
        stack.setItemMeta(headMeta);
        return stack;
    }
    
    /**
     * Creates a stack of playerheads for no owner
     *  
     * @param addLore controls whether any lore text should be added to the head.
     * @return The ItemStack of heads desired.
     */
    public static ItemStack PlayerSkull(boolean addLore){
        return PlayerSkull(Config.defaultStackSize,addLore);
    }
    
    /**
     * Creates a stack of playerheads for no owner
     *  
     * @param quantity The number of heads to create in the stack.
     * @param addLore controls whether any lore text should be added to the head.
     * @return The ItemStack of heads desired.
     */
    public static ItemStack PlayerSkull(int quantity, boolean addLore){
        return PlayerSkull((OfflinePlayer) null,quantity, addLore, null);
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
     * @param addLore controls whether any lore text should be added to the head.
     * @return The ItemStack of heads desired.
     * @see org.shininet.bukkit.playerheads.Config#defaultStackSize
     */
    public static ItemStack PlayerSkull(String owner, boolean addLore){
        return PlayerSkull(owner,Config.defaultStackSize, addLore,null);
    }
    /**
     * Creates a stack of playerheads for the given username.The username given is translated to an OfflinePlayer by bukkit at the time of creation.
     * 
     * NOTE: null or empty usernames will be rejected with an IllegalArgumentException
     * 
     * @param owner The username to create a head for.
     * @param quantity The number of heads to create in the stack.
     * @param addLore controls whether any lore text should be added to the head.
     * @param profile the profile to set on the new head (if the optional itself is null or empty, nothing happens)
     * @return The ItemStack of heads desired.
     * @throws IllegalArgumentException passed a null or empty username.
     */
    public static ItemStack PlayerSkull(String owner, int quantity, boolean addLore, CompatibleProfile profile){
        if(owner==null || owner.isEmpty()) throw new IllegalArgumentException("Creating a playerhead with a null or empty username is not possible with this method.");
        OfflinePlayer op = Compatibility.getProvider().getOfflinePlayerByName(owner);
        return PlayerSkull(op,quantity, addLore,profile);
    }
    
    /**
     * Spawns a skull itemstack using an input spawn string or username.
     * 
     * The quantity of heads will be defined by Config.defaultStackSize (usually 1)
     * @param spawnString the spawn string indicating the type of skull or username indicating the owner of the skull.
     * @param usevanillaheads Whether to permit vanilla head-items to be used in place of custom playerheads for supported mobs. (if the spawn string is recognized)
     * @param addLore controls whether any lore text should be added to the head. (currently only applied to custom mobheads and player heads)
     * @return The skull itemstack desired. If the spawn string is recognized, this will be the corresponding entity's head, otherwise it will be a playerhead for the name supplied.
     */
    public static ItemStack spawnSkull(String spawnString, boolean usevanillaheads, boolean addLore){
        return spawnSkull(spawnString,Config.defaultStackSize,usevanillaheads,addLore);
    }
    
    /**
     * Spawns a skull itemstack using an input spawn string or username.
     * @param spawnString the spawn string indicating the type of skull or username indicating the owner of the skull.
     * @param quantity the number of items to spawn in this stack.
     * @param usevanillaheads Whether to permit vanilla head-items to be used in place of custom playerheads for supported mobs. (if the spawn string is recognized)
     * @param addLore controls whether any lore text should be added to the head. (currently only applied to custom mobheads and player heads)
     * @return The skull itemstack desired. If the spawn string is recognized, this will be the corresponding entity's head, otherwise it will be a playerhead for the name supplied.
     */
    public static ItemStack spawnSkull(String spawnString, int quantity, boolean usevanillaheads,boolean addLore){
        TexturedSkullType type;
        if(spawnString.isEmpty()) type=TexturedSkullType.PLAYER;
        else type = TexturedSkullType.getBySpawnName(spawnString);
        if(type==null){
            return PlayerSkull(spawnString,quantity,addLore,null);
        }else{
            return MobSkull(type,quantity,usevanillaheads,addLore,null);
        }
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
    //reinvestigate these approaches as they're preferred to the deprecated username method.
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
