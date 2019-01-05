package net.daveyx0.multimob.config;

import java.util.ArrayList;
import java.util.List;

import net.daveyx0.multimob.spawn.MMConfigSpawnEntry;
import net.minecraftforge.common.config.Configuration;

public class MMConfigSpawns {
	
	private static List<String> defaultEntitiesToSpawn = new ArrayList<String>();
	private static String[] entitiesToSpawn;
	private static int spawnTickDelay;
	private static boolean spawnInSpectate;
	private static boolean useBetaSpawning;
	
	private static int monsterSpawnLimit;
	private static int passiveSpawnLimit;
	private static int waterSpawnLimit;
	private static int lavaSpawnLimit;
	
	public static void loadGeneralOptions(Configuration config)
	{
		config.addCustomCategoryComment("generalOptions", "Some general MultiMob config options");
		config.getCategory("generalOptions").setRequiresMcRestart(true);
		spawnTickDelay = config.get("generalOptions", "Tick delay between spawns", 40, " (Only for Beta spawning) The Tick delay between spawn attempts for MultiMob. Lower for more common spawns.").getInt();
		spawnInSpectate = config.get("generalOptions", "Allow spawning in spectate mode", false, " (Only for Beta spawning) Enable/Disable allowing the MultiMob system to spawn while in spectator mode.").getBoolean();
		useBetaSpawning = config.get("generalOptions", "Use the Unique MultiMob spawning (experimental)", false, "Enable/Disable MultiMob spawning; a system seperate from normal Minecraft spawning. Not recommended right now.").getBoolean();
		monsterSpawnLimit = config.get("generalOptions", "Spawn limit for MultiMob Monster type", 30, "Determines how many mobs can spawn of the creature type: MultiMob Monster.").getInt();
		passiveSpawnLimit = config.get("generalOptions", "Spawn limit for MultiMob Passive type", 10, "Determines how many mobs can spawn of the creature type: MultiMob Passive.").getInt();
		waterSpawnLimit = config.get("generalOptions", "Spawn limit for MultiMob Water type", 10, "Determines how many mobs can spawn of the creature type: MultiMob Water.").getInt();
		lavaSpawnLimit = config.get("generalOptions", "Spawn limit for MultiMob Lava type", 5, "Determines how many mobs can spawn of the creature type: MultiMob Lava.").getInt();
	}

	public static void load(Configuration config) {
		
		loadGeneralOptions(config);
		
		if(defaultEntitiesToSpawn.isEmpty()) {defaultEntitiesToSpawn.add("");}
		config.addCustomCategoryComment("extraEntries", "Add additional spawn entries. Simply add a name for the entry, then '#' followed by the mob resource name (like minecraft:zombie) and it generates a new entry (go back to the main menu to refresh)");
		entitiesToSpawn = config.get("extraEntries", "Mob spawns", convertListToArray(defaultEntitiesToSpawn)).getStringList();
	}
	
	public static String[] getConfigSpawnEntries()
	{
		return entitiesToSpawn;
	}
	
	public static int getSpawnTickDelay()
	{
		return spawnTickDelay;
	}
	
	public static int getMonsterSpawnLimit()
	{
		return monsterSpawnLimit;
	}
	
	public static int getPassiveSpawnLimit()
	{
		return passiveSpawnLimit;
	}
	
	public static int getWaterSpawnLimit()
	{
		return waterSpawnLimit;
	}
	
	public static int getLavaSpawnLimit()
	{
		return lavaSpawnLimit;
	}
	
	public static boolean getSpawnInSpectate()
	{
		return spawnInSpectate;
	}
	
	public static boolean getUseBetaSpawning()
	{
		return useBetaSpawning;
	}
	
	public static void addConfigSpawnEntry(MMConfigSpawnEntry entry)
	{
		//defaultEntitiesToSpawn.add(entry.getEntryName() + "#" + entry.entityName);
		MMConfig.EXTERNALCONFIGSPAWNS.add(entry);
	}
	
	public static String[] convertListToArray(List<String> list)
	{
		String[] array = new String[list.size()];
		
		for(int i = 0; i < array.length ; i++)
		{
			array[i] = list.get(i);
		}
		
		return array;
	}
}