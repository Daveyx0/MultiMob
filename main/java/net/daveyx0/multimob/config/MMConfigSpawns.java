package net.daveyx0.multimob.config;

import java.util.ArrayList;
import java.util.List;

import net.daveyx0.multimob.spawn.MMConfigSpawnEntry;
import net.minecraftforge.common.config.Configuration;

public class MMConfigSpawns {
	
	private static List<String> defaultEntitiesToSpawn = new ArrayList<String>();
	private static String[] entitiesToSpawn;

	public static void load(Configuration config) {
		
		if(defaultEntitiesToSpawn.isEmpty()) {defaultEntitiesToSpawn.add("");}
		config.addCustomCategoryComment("extraEntries", "Add additional spawn entries. Simply add a name for the entry, then '#' followed by the mob resource name (like minecraft:zombie) and it generates a new entry (go back to the main menu to refresh)");
		entitiesToSpawn = config.get("extraEntries", "Mob spawns", convertListToArray(defaultEntitiesToSpawn)).getStringList();
	}
	
	public static String[] getConfigSpawnEntries()
	{
		return entitiesToSpawn;
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