/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.playerheads;

import org.shininet.bukkit.playerheads.CustomSkullType;

/**
 *
 * @author crash
 */
public class Test {
    public static void main(String[] args) {
        // Prints "Hello, World" to the terminal window.
        CustomSkullType sk = CustomSkullType.get("CarlosTheCow");
        System.out.println(sk);
        System.out.println(sk.getOwner());
        System.out.println(sk.getDisplayName());
        System.out.println(sk.getSpawnName());
    }

}
