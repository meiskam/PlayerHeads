package org.shininet.bukkit.playerheads;

public enum CustomSkullType {
	
	SPIDER("Kelevra_V", PlayerHeads.format(Lang.HEAD_SPIDER)), // Thanks SethBling
	ENDERMAN("Violit", PlayerHeads.format(Lang.HEAD_ENDERMAN)), // Thanks SethBling
	BLAZE("Blaze_Head", PlayerHeads.format(Lang.HEAD_BLAZE)), // Thanks SethBling
//	TEMP("gavertoso", PlayerHeads.format(Lang.HEAD)), // Thanks Glompalici0us //TODO
	SQUID("squidette8", PlayerHeads.format(Lang.HEAD_SQUID)), // Thanks SethBling
	SILVERFISH("AlexVMiner", PlayerHeads.format(Lang.HEAD_SILVERFISH)), // Thanks SethBling
	ENDER_DRAGON("KingEndermen", PlayerHeads.format(Lang.HEAD_ENDER_DRAGON)), // Thanks SethBling
	SLIME("HappyHappyMan", PlayerHeads.format(Lang.HEAD_SLIME)), // Thanks SethBling
//	GHAST("ghast1", PlayerHeads.format(Lang.HEAD_GHAST)), // Thanks SethBling
	IRON_GOLEM("zippie007", PlayerHeads.format(Lang.HEAD_IRON_GOLEM)), // Thanks SethBling
	MUSHROOM_COW("Mooshroom_Stew", PlayerHeads.format(Lang.HEAD_MUSHROOM_COW)), // Thanks SethBling
	BAT("coolwhip101", PlayerHeads.format(Lang.HEAD_BAT)); // Thanks SethBling
	
	private final String owner;
	private final String displayName;
	
	CustomSkullType(String owner, String displayName) {
		this.owner = owner;
		this.displayName = displayName;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
