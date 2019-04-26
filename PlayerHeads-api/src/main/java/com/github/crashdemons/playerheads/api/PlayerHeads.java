/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads.api;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class PlayerHeads {
    private static PlayerHeadsAPI api = null;
    public static PlayerHeadsAPI getApiInstance(){return api;}
    @Deprecated
    public static void setApiInstance(PlayerHeadsAPI api){PlayerHeads.api=api;}
    
}
