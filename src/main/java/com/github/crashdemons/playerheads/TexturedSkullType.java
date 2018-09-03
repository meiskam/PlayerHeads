/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import java.util.UUID;
import org.shininet.bukkit.playerheads.Lang;
import org.shininet.bukkit.playerheads.Tools;
import java.util.HashMap;

/**
 * Enumeration of skulls with associated UUID (randomly assigned) and texture string.
 * 
 * Inspired by shininet-CustomSkullType, presumably by meiskam
 * @author crash
 */
public enum TexturedSkullType {
    EXAMPLE("fc1d495d-a5a1-4f5e-b85d-5583c3d5d622","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19")
    
    ;
    private final UUID owner;
    private String texture;
    private static class Mappings{
        public static final HashMap<UUID,TexturedSkullType> skullsById = new HashMap<>();
    }
            
    TexturedSkullType(String ownerUUID, String texture){
        this(UUID.fromString(ownerUUID),texture);
    }
    TexturedSkullType(UUID owner, String texture){
        this.owner=owner;
        this.texture = texture;
        Mappings.skullsById.put(owner, this);
    }
    public UUID getOwner() {
        return owner;
    }
    public String getTexture(){
        return texture;
    }
    public static TexturedSkullType get(UUID owner) {
        return Mappings.skullsById.get(owner);
    }
    
    public String getDisplayName() {
        return Tools.format(Lang.getString("HEAD_" + name()));
    }
    public String getSpawnName() {
        return Lang.getString("HEAD_SPAWN_" + name());
    }
}
