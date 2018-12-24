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

	public static void load(Configuration config) {
		
		if(defaultEntitiesToSpawn.isEmpty()) {defaultEntitiesToSpawn.add("");}
		config.addCustomCategoryComment("extraEntries", "Add additional spawn entries. Simply add a name for the entry, then '#' followed by the mob resource name (like minecraft:zombie) and it generates a new entry (go back to the main menu to refresh)");
		entitiesToSpawn = config.get("extraEntries", "Mob spawns", convertListToArray(defaultEntitiesToSpawn)).getStringList();
		config.addCustomCategoryComment("generalOptions", "Some general MultiMob config options");
		spawnTickDelay = config.get("generalOptions", "Tick delay between spawns", 40, "The Tick delay between spawn attempts for MultiMob. Lower for more common spawns.").getInt();
		spawnInSpectate = config.get("generalOptions", "Allow spawning in spectate mode", false, "Enable/Disable allowing the MultiMob system to spawn while in spectator mode.").getBoolean();
		
	}
	
	public static String[] getConfigSpawnEntries()
	{
		return entitiesToSpawn;
	}
	
	public static int getSpawnTickDelay()
	{
		return spawnTickDelay;
	}
	
	public static boolean getSpawnInSpectate()
	{
		return spawnInSpectate;
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