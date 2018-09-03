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
    DONKEY("Donkeyes"), // Still looking for a better alternative
    // Also looking for a Mule skull.
    SQUID("MHF_Squid"), // Thanks Marc Watson
    SILVERFISH("AlexVMiner","MHF_Silverfish"), // Thanks XlexerX  //20180903-secondary name no longer has this skin
    SLIME("MHF_Slime", "HappyHappyMan"), // Thanks SethBling
    IRON_GOLEM("MHF_Golem", "zippie007"), // Thanks Marc Watson
    MUSHROOM_COW("MHF_MushroomCow"), // Thanks Marc Watson
    BAT("bozzobrain"), // Thanks incraftion.com
    PIG_ZOMBIE("MHF_PigZombie", "ManBearPigZombie"), // Thanks Marc Watson
    SNOWMAN("MHF_SnowGolem", "Koebasti"), // Thanks MrLeikermoser
    GHAST("MHF_Ghast", "_QuBra_"), // Thanks Marc Watson
    PIG("MHF_Pig"), // Thanks Marc Watson
    VILLAGER("MHF_Villager", "Kuvase", "Villager", "scraftbrothers9"), // Thanks Marc Watson
    ZOMBIE_VILLAGER("Geartooth_","Clubjustin"), // Need to find a secondary just to be sure. 20180903-original name changed
    SHEEP("MHF_Sheep"), // Thanks Marc Watson
    COW("MHF_Cow", "VerifiedBernard", "CarlosTheCow"), // Thanks Marc Watson
    CHICKEN("MHF_Chicken"), // Thanks Marc Watson
    OCELOT("MHF_Ocelot"), // Thanks Marc Watson
    WITCH("MHF_Witch"),
    MAGMA_CUBE("MHF_LavaSlime"), // Thanks Marc Watson
    WOLF("MHF_Wolf", "Pablo_Penguin", "Budwolf"),
    CAVE_SPIDER("MHF_CaveSpider"), // Thanks Marc Watson
    RABBIT("MHF_Rabbit","rabbit2077"), // Thanks IrParadox
    GUARDIAN("MHF_Guardian", "Guardian"), // Thanks lee3kfc
    ELDER_GUARDIAN("MHF_EGuardian"),
    POLAR_BEAR("ice_bear", "snowbear"),
    SHULKER("MHF_Shulker"),
    STRAY("MHF_Stray"),
    ENDERMITE("MHF_Endermite"),
    EVOKER("MHF_Illager","MHF_Evoker"),//20180903-secondary name no longer has this skin
    ILLUSIONER("Illager","MHF_Illusioner"),//20180903-name no longer exists
    VINDICATOR("Vindicator"),
    WITHER("MHF_Wither"),
    VEX("MHF_Vex"),
    LLAMA("LilLlama","MHF_Llama"),//20180903-name no longer exists
    PARROT("ParrotBot","MHF_Parrot");//20180903-better options available
    // Still have to find a skull for Husk Zombie.

    private final String owner;

    private static class Holder {
        static final HashMap<String, CustomSkullType> map = new HashMap<>();
    }

    CustomSkullType(String owner) {
        this.owner = owner;
        Holder.map.put(owner.toLowerCase(), this);
    }

    CustomSkullType(String owner, String... toConvert) {
        this(owner);
        for (String key : toConvert) {
            Holder.map.put(key.toLowerCase(), this);
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
        return Holder.map.get(owner.toLowerCase());
    }
}
