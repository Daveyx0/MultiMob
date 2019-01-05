package net.daveyx0.multimob.config;


import java.io.File;
import java.util.HashSet;

import net.daveyx0.multimob.core.MMReference;
import net.daveyx0.multimob.spawn.MMConfigSpawnEntry;
import net.daveyx0.multimob.spawn.MMSpawnRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MMConfig {

	static Configuration config;
	public static final HashSet<MMConfigSpawnEntry> CONFIGSPAWNS = new HashSet<MMConfigSpawnEntry>();
	public static final HashSet<MMConfigSpawnEntry> EXTERNALCONFIGSPAWNS = new HashSet<MMConfigSpawnEntry>();

	public static void preInit(File dir, FMLPreInitializationEvent event) {
		config = new Configuration(new File(dir, "multimob_spawns.cfg"));
		MMConfigSpawns.loadGeneralOptions(config);
		
		MMConfigSpawnEntry.setupCategoryDescriptions(config);
		MinecraftForge.EVENT_BUS.register(new MMConfig());
	}
	
	public static Configuration getConfig()
	{
		return config;
	}
	
	public static void postInit()
	{
		reloadConfig();
	}

	private static void reloadConfig() {
		
		MMConfigSpawns.load(config);
		refreshConfigSpawns();
		
		for(MMConfigSpawnEntry entry: CONFIGSPAWNS)
		{
			entry.load(config);
			//MultiMob.LOGGER.info(entry.getEntryName());
		}
		
		MMSpawnRegistry.loadSpawns();

		if (config.hasChanged()) {
			config.save();
		}
	}
	
	private static void refreshConfigSpawns()
	{
		HashSet<MMConfigSpawnEntry> oldConfigSpawns = CONFIGSPAWNS;
		
		CONFIGSPAWNS.clear(); 
		
		for(MMConfigSpawnEntry entry : EXTERNALCONFIGSPAWNS)
		{
			MMConfigSpawnEntry newEntry = entry;
			for(MMConfigSpawnEntry oldEntry : oldConfigSpawns)
			{
				if(oldEntry.getEntryName().equals(entry.getEntryName()))
				{
					newEntry = oldEntry;
				}
			}
			
			CONFIGSPAWNS.add(newEntry);
		}
		
		for(String configEntry : MMConfigSpawns.getConfigSpawnEntries())
		{
			if(configEntry.equals("")) {continue;}
			
			String[] names = configEntry.split("#");
			if(names != null && names.length < 2) {continue;}
			
			MMConfigSpawnEntry newEntry = new MMConfigSpawnEntry(names[0], names[1]);
			for(MMConfigSpawnEntry oldEntry : oldConfigSpawns)
			{
				if(oldEntry.getEntryName().equals(names[0]))
				{
					newEntry = oldEntry;
				}
			}
			CONFIGSPAWNS.add(newEntry);
		}

	}


	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(MMReference.MODID)) {
			reloadConfig();
		}
	}
}