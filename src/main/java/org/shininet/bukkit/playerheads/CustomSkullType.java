/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.HashMap;

/**
 * @author meiskam
 */

public enum CustomSkullType {

    SPIDER("MHF_Spider", "Kelevra_V"), // Thanks SethBling
    ENDERMAN("MHF_Enderman", "Violit", "Troax"), // Thanks SethBling
    BLAZE("MHF_Blaze", "Blaze_Head"), // Thanks SethBling
    HORSE("gavertoso"), // Thanks Glompalici0us
    SQUID("MHF_Squid", "squidette8"), // Thanks SethBling
    SILVERFISH("AlexVMiner"), // Thanks SethBling
    ENDER_DRAGON("KingEndermen", "KingEnderman"), // Thanks SethBling
    SLIME("MHF_Slime", "HappyHappyMan", "Ex_PS3Zocker", "BlackJake"), // Thanks SethBling
    IRON_GOLEM("zippie007"), // Thanks SethBling
    MUSHROOM_COW("MHF_MushroomCow", "Mooshroom_Stew", "BaileeBitch"), // Thanks SethBling
    BAT("bozzobrain", "coolwhip101"), // Thanks incraftion.com
    PIG_ZOMBIE("MHF_PigZombie", "ManBearPigZombie", "scraftbrothers5"), // Thanks cnaude of TrophyHeads
    SNOWMAN("Koebasti", "scraftbrothers2", "GLaDOS"), // Thanks MrLeikermoser
    GHAST("MHF_Ghast", "_QuBra_", "blaiden"), // Thanks MrLeikermoser
    PIG("XlexerX", "scrafbrothers7"), // Thanks XlexerX
    VILLAGER("MHF_Villager", "Kuvase", "Villager", "scraftbrothers9"), // Thanks XlexerX
    SHEEP("MHF_Sheep", "SGT_KICYORASS", "Eagle_Peak", "HyperBeam567"), // Thanks cowboys2317
    COW("MHF_Cow", "VerifiedBernard", "CarlosTheCow", "Seann_G"), // Thanks Jknies
    CHICKEN("MHF_Chicken", "scraftbrothers1", "Gafloff"), // Thanks SuperCraftBrothers.com
    OCELOT("scraftbrothers3"), // Thanks SuperCraftBrothers.com
    WITCH("scrafbrothers4"), // Thanks SuperCraftBrothers.com
    MAGMA_CUBE("MHF_LavaSlime", "brinkley26"), // Thanks Marc_IRL of Mojang
    WITHER("Mad_Miner63"); // Thanks Mad_Miner63

    private final String owner;
    private static class Holder {
        static HashMap<String, CustomSkullType> map = new HashMap<String, CustomSkullType>();
    }

    CustomSkullType(String owner) {
        this.owner = owner;
        Holder.map.put(owner, this);
    }
    
    CustomSkullType(String owner, String... toConvert) {
        this(owner);
        for (String key : toConvert) {
            Holder.map.put(key, this);
        }
    }

    public String getOwner() {
        return owner;
    }

    public String getDisplayName() {
        return Tools.format(Lang.getString("HEAD_" + name()));
    }

    public String getSpawnName() {
        return Lang.getString("HEAD_SPAWN_" + name());
    }

    public static CustomSkullType get(String owner) {
        return Holder.map.get(owner);
    }
}
