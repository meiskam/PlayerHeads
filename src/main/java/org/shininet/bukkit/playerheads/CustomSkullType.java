/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.shininet.bukkit.playerheads;

import java.util.HashMap;

/**
 * @author meiskam
 */

public enum CustomSkullType {

    SPIDER("MHF_Spider", "Kelevra_V"),
    ENDERMAN("MHF_Enderman", "Violit"),
    BLAZE("MHF_Blaze", "Blaze_Head"),
    HORSE("gavertoso"), // Thanks Glompalici0us
    SQUID("MHF_Squid", "squidette8"),
    SILVERFISH("AlexVMiner"), // Thanks XlexerX
    ENDER_DRAGON("MHF_Enderman", "KingEndermen", "KingEnderman"),
    SLIME("MHF_SLIME", "HappyHappyMan", "Ex_PS3Zocker"),
    IRON_GOLEM("MHF_Golem", "zippie007"),
    MUSHROOM_COW("MHF_MushroomCow", "Mooshroom_Stew"),
    BAT("bozzobrain", "coolwhip101"), // Thanks incraftion.com
    PIG_ZOMBIE("MHF_PigZombie", "ManBearPigZombie"),
    SNOWMAN("Snowgolem", "Koebasti"), // Thanks MrLeikermoser
    GHAST("MHF_Ghast", "_QuBra_", "blaiden"),
    PIG("MHF_Pig", "XlexerX"),
    VILLAGER("MHF_Villager", "Kuvase", "Villager"),
    SHEEP("MHF_Sheep", "SGT_KICYORASS", "Eagle_Peak"),
    COW("MHF_Cow", "VerifiedBernard", "CarlosTheCow"),
    CHICKEN("MHF_Chicken"),
    OCELOT("MHF_Ocelot"),
    WITCH("MHF_Witch", "scrafbrothers4"), // Thanks SuperCraftBrothers.com
    MAGMA_CUBE("MHF_LavaSlime"),
    WOLF("MHF_Wolf", "Pablo_Penguin"),
    CAVE_SPIDER("MHF_CaveSpider"),
    RABBIT("MHF_Rabbit", "rabbit2077"), // Thanks IrParadox
    GUARDIAN("Guardian"), // Thanks lee3kfc
    POLAR_BEAR("ice_bear", "snowbear"),
    SHULKER("MHF_Shulker");

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
