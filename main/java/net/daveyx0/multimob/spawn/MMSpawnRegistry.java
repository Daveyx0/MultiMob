package net.daveyx0.multimob.spawn;

import java.util.ArrayList;
import java.util.List;

import net.daveyx0.multimob.config.MMConfig;
import net.daveyx0.multimob.config.MMConfigSpawns;
import net.daveyx0.multimob.core.MultiMob;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class MMSpawnRegistry 	
{
	public static final List<MMSpawnEntry> SPAWNS = new ArrayList<MMSpawnEntry>();

	public static List<MMSpawnEntry> getSpawnEntries() {
		
		return SPAWNS;
	}

	public static void loadSpawns() {
		
		SPAWNS.clear();
		
		for(MMConfigSpawnEntry configEntry : MMConfig.CONFIGSPAWNS)
		{
			SPAWNS.add(getSpawnEntryFromConfig(configEntry));
		}
	}
	
	public static void registerSpawnEntry(MMConfigSpawnEntry entry)
	{
		MMConfigSpawns.addConfigSpawnEntry(entry);
	}
	
	public static MMSpawnEntry getSpawnEntryFromConfig(MMConfigSpawnEntry configEntry)
	{
		EntityEntry entry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(configEntry.entityName));
		if(entry != null)
		{
			return new MMSpawnEntry(configEntry.getEntryName(), entry.getEntityClass(), configEntry);
		}
		else
		{
			MultiMob.LOGGER.info("Was not able to make " + configEntry.getEntryName() + " entry for " + configEntry.entityName);
		}
		
		return null;
	}
}
