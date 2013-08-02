/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.HashMap;

/**
 * @author meiskam
 */

public enum CustomSkullType {

    SPIDER("Kelevra_V"), // Thanks SethBling
    ENDERMAN("Violit"), // Thanks SethBling
    BLAZE("Blaze_Head"), // Thanks SethBling
    HORSE("gavertoso"), // Thanks Glompalici0us
    SQUID("squidette8"), // Thanks SethBling
    SILVERFISH("AlexVMiner"), // Thanks SethBling
    ENDER_DRAGON("KingEndermen", "KingEnderman"), // Thanks SethBling
    SLIME("HappyHappyMan", "Ex_PS3Zocker"), // Thanks SethBling
    IRON_GOLEM("zippie007"), // Thanks SethBling
    MUSHROOM_COW("Mooshroom_Stew"), // Thanks SethBling
    BAT("bozzobrain", "coolwhip101"), // Thanks incraftion.com
    PIG_ZOMBIE("ManBearPigZombie", "scraftbrothers5"), // Thanks cnaude of TrophyHeads
    SNOWMAN("Koebasti", "scraftbrothers2"), // Thanks MrLeikermoser
    GHAST("_QuBra_", "blaiden"), // Thanks MrLeikermoser
    PIG("XlexerX", "scrafbrothers7"), // Thanks XlexerX
    VILLAGER("Kuvase", "Villager", "scraftbrothers9"), // Thanks XlexerX
    SHEEP("SGT_KICYORASS", "Eagle_Peak"), // Thanks cowboys2317
    COW("VerifiedBernard", "CarlosTheCow"), // Thanks Jknies
    CHICKEN("scraftbrothers1"), // Thanks SuperCraftBrothers.com
    OCELOT("scraftbrothers3"), // Thanks SuperCraftBrothers.com
    WITCH("scrafbrothers4"); // Thanks SuperCraftBrothers.com

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
