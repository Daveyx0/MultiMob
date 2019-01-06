package net.daveyx0.multimob.config;

import java.util.ArrayList;
import java.util.List;

import net.daveyx0.multimob.core.MultiMob;
import net.daveyx0.multimob.spawn.MMConfigSpawnEntry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.common.config.Configuration;

public class MMConfigSpawns {
	
	private static List<String> defaultEntitiesToSpawn = new ArrayList<String>();
	private static String[] entitiesToSpawn;
	private static int spawnTickDelay;
	private static boolean spawnInSpectate;
	private static boolean useAdditionalSpawning;
	
	private static int monsterSpawnLimit;
	private static int passiveSpawnLimit;
	private static int waterSpawnLimit;
	private static int lavaSpawnLimit;
	
	private static int vanillaMonsterSpawnLimit;
	private static int vanillaCreatureSpawnLimit;
	private static int vanillaAmbientSpawnLimit;
	private static int vanillaWaterSpawnLimit;
	
	private static int otherSpawnLimit;
	
	private static int[] dimensionWhiteList;
	private static int[] defaultWhiteList = new int[]{0};
	
	public static void loadGeneralOptions(Configuration config)
	{
		config.addCustomCategoryComment("additionalSpawningOptions", "These options only apply when using the Additional Spawning system, which can also be enabled here.");
		spawnTickDelay = config.get("additionalSpawningOptions", "Tick delay between spawns", 10, "The Tick delay between spawn attempts for MultiMob. Lower for more common spawns.").getInt();
		spawnInSpectate = config.get("additionalSpawningOptions", "Allow spawning in spectate mode", false, "Enable/Disable allowing the MultiMob system to spawn while in spectator mode.").getBoolean();
		useAdditionalSpawning = config.get("additionalSpawningOptions", "Use an additional world spawner to do extra spawns", false, "Enable/Disable to activate an additional spawner to add more mobs to the world.").getBoolean();
		monsterSpawnLimit = config.get("additionalSpawningOptions", "Spawn limit for MultiMob Monster type", 0, "Determines how many additional mobs can spawn of the creature type: MultiMob Monster.").getInt();
		passiveSpawnLimit = config.get("additionalSpawningOptions", "Spawn limit for MultiMob Passive type", 0, "Determines how many additional mobs can spawn of the creature type: MultiMob Passive.").getInt();
		waterSpawnLimit = config.get("additionalSpawningOptions", "Spawn limit for MultiMob Water type", 0, "Determines how many additional mobs can spawn of the creature type: MultiMob Water.").getInt();
		lavaSpawnLimit = config.get("additionalSpawningOptions", "Spawn limit for MultiMob Lava type", 0, "Determines how many additional mobs can spawn of the creature type: MultiMob Lava.").getInt();
		vanillaMonsterSpawnLimit = config.get("additionalSpawningOptions", "Spawn limit for Vanilla Monster type", 0, "Determines how many additional mobs can spawn of the creature type: Monster.").getInt();
		vanillaAmbientSpawnLimit = config.get("additionalSpawningOptions", "Spawn limit for Vanilla Ambient type", 0, "Determines how many additional mobs can spawn of the creature type: Ambient.").getInt();
		vanillaCreatureSpawnLimit = config.get("additionalSpawningOptions", "Spawn limit for Vanilla Creature type", 0, "Determines how many additional mobs can spawn of the creature type: Creature.").getInt();
		vanillaWaterSpawnLimit = config.get("additionalSpawningOptions", "Spawn limit for Vanilla Water type", 0, "Determines how many additional mobs can spawn of the creature type: Water.").getInt();
		otherSpawnLimit= config.get("additionalSpawningOptions", "Spawn limit for Modded type", 0, "Determines how many additional mobs can spawn of creature types from other mods.").getInt();
		dimensionWhiteList= config.get("additionalSpawningOptions", "Dimensions to apply additional spawns for", defaultWhiteList, "Determines which dimensions the additional spawns will count for.").getIntList();
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
	
	public static int getSpawnLimitIncrease(EnumCreatureType type)
	{
		if(type == EnumCreatureType.MONSTER)
		{
			return vanillaMonsterSpawnLimit;
		}
		else if(type == EnumCreatureType.AMBIENT)
		{
			return vanillaAmbientSpawnLimit;
		}
		else if(type == EnumCreatureType.CREATURE)
		{
			return vanillaCreatureSpawnLimit;
		}
		else if(type == EnumCreatureType.WATER_CREATURE)
		{
			return vanillaWaterSpawnLimit;
		}
		else if(type == MultiMob.MULTIMOB_MONSTER)
		{
			return monsterSpawnLimit;
		}
		else if(type == MultiMob.MULTIMOB_PASSIVE)
		{
			return passiveSpawnLimit;
		}
		else if(type == MultiMob.MULTIMOB_WATER)
		{
			return waterSpawnLimit;
		}
		else if(type == MultiMob.MULTIMOB_LAVA)
		{
			return lavaSpawnLimit;
		}
		else
		{
			return otherSpawnLimit;
		}
	}
	
	public static boolean getSpawnInSpectate()
	{
		return spawnInSpectate;
	}
	
	public static boolean getUseAdditionalSpawning()
	{
		return useAdditionalSpawning;
	}
	
	public static void addConfigSpawnEntry(MMConfigSpawnEntry entry)
	{
		//defaultEntitiesToSpawn.add(entry.getEntryName() + "#" + entry.entityName);
		MMConfig.EXTERNALCONFIGSPAWNS.add(entry);
	}
	
	public static int[] getDimensionWhiteList()
	{
		return dimensionWhiteList;
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