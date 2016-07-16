/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.HashMap;

/**
 * @author meiskam
 */

public enum CustomSkullType {

    SPIDER("MHF_Spider", "Kelevra_V"), // Thanks Marc Watson
    ENDERMAN("MHF_Enderman", "Violit"), // Thanks Marc Watson
    BLAZE("MHF_Blaze", "Blaze_Head"), // Thanks Marc Watson
    HORSE("gavertoso"), // Thanks Glompalici0us
    SQUID("MHF_Squid", "squidette8"), // Thanks Marc Watson
    SILVERFISH("Xzomag", "AlexVMiner"), // Thanks XlexerX
    ENDER_DRAGON("KingEndermen", "KingEnderman"), // Thanks SethBling
    SLIME("HappyHappyMan", "Ex_PS3Zocker"), // Thanks SethBling
    IRON_GOLEM("MHF_Golem", "zippie007"), // Thanks Marc Watson
    MUSHROOM_COW("MHF_MushroomCow", "Mooshroom_Stew"), // Thanks Marc Watson
    BAT("bozzobrain", "coolwhip101"), // Thanks incraftion.com
    PIG_ZOMBIE("MHF_PigZombie", "ManBearPigZombie", "scraftbrothers5"), // Thanks Marc Watson
    SNOWMAN("Koebasti", "scraftbrothers2"), // Thanks MrLeikermoser
    GHAST("MHF_Ghast", "_QuBra_", "blaiden"), // Thanks Marc Watson
    PIG("MHF_Pig", "XlexerX", "scrafbrothers7"), // Thanks Marc Watson
    VILLAGER("MHF_Villager", "Kuvase", "Villager", "scraftbrothers9"), // Thanks Marc Watson
    SHEEP("MHF_Sheep", "SGT_KICYORASS", "Eagle_Peak"), // Thanks Marc Watson
    COW("MHF_Cow", "VerifiedBernard", "CarlosTheCow"), // Thanks Marc Watson
    CHICKEN("MHF_Chicken", "scraftbrothers1"), // Thanks Marc Watson
    OCELOT("MHF_Ocelot", "scraftbrothers3"), // Thanks Marc Watson
    WITCH("scrafbrothers4"), // Thanks SuperCraftBrothers.com
    MAGMA_CUBE("MHF_LavaSlime"), // Thanks Marc Watson
    WOLF("Pablo_Penguin", "Budwolf"), // I still need an official wolf head if anyone wants to provide one
    CAVE_SPIDER("MHF_CaveSpider"), // Thanks Marc Watson
    RABBIT("rabbit2077"), // Thanks IrParadox
    GUARDIAN("Guardian"); // Thanks lee3kfc

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
