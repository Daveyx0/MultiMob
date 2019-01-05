package net.daveyx0.multimob.spawn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.daveyx0.multimob.config.MMConfig;
import net.daveyx0.multimob.config.MMConfigSpawns;
import net.daveyx0.multimob.core.MultiMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeType;
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
			MMSpawnEntry entry = getSpawnEntryFromConfig(configEntry);
			if(entry != null && entry.getEntityClass() != null)
			{
				SPAWNS.add(getSpawnEntryFromConfig(configEntry));
			}
		}
		
		registerRegularSpawns();
	}
	
	public static void registerSpawnEntry(MMConfigSpawnEntry entry)
	{
		MMConfigSpawns.addConfigSpawnEntry(entry);
	}
	
	public static MMSpawnEntry getSpawnEntryFromEntityClass(Class<? extends Entity> entityClass)
	{
		for (MMSpawnEntry entry: SPAWNS)
		{
			if(entry.getEntityClass().equals(entityClass))
			{
				return entry;
			}
		}
		
		return null;
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
	
	public static void registerRegularSpawns()
	{
		for(MMSpawnEntry entry : SPAWNS)
		{
			addRegularSpawn(entry);
			setSpawnPlacementType(entry);
		}
	}
	
	public static void addRegularSpawn(MMSpawnEntry entry)
	{
		if(entry.getIsAllowedToSpawn() && entry.getSpawnLimit() != 0 && entry.getSpawnWeight() != 0)
		{
			boolean addToAllBiomes = false;

			
			if((entry.getBiomes() == null || entry.getBiomes().isEmpty()) && (entry.getBiomeTypes() == null ||  entry.getBiomeTypes().isEmpty()))
			{
				for(Biome biome : ForgeRegistries.BIOMES)
				{
					biome.getSpawnableList(entry.getCreatureType()).add(new SpawnListEntry(entry.getEntityClass(), entry.getSpawnWeight(), entry.getGroupSizeRange()[0], entry.getGroupSizeRange()[1]));
				}
			}
			else
			{
				
				final Set<Biome> biomes = new HashSet<>();
				
				if(entry.getBiomes() != null && !entry.getBiomes().isEmpty())
				{
					for(Biome biome: entry.getBiomes())
					{
						biomes.add(biome);
					}
				}
				
				if(entry.getBiomeTypes() != null && !entry.getBiomeTypes().isEmpty())
				{
					for(Biome biome : ForgeRegistries.BIOMES)
					{
						boolean typeCheck = true;
						
						for(BiomeDictionary.Type biomeType: entry.getBiomeTypes())
						{
							if(!BiomeDictionary.hasType(biome, biomeType))
							{
								typeCheck = false;
							}
						}
						
						if(typeCheck)
						{
							biomes.add(biome);
						}
						
					}
				}
				
				if(biomes != null && !biomes.isEmpty())
				{
					for(Biome biome : biomes)
					{
						biome.getSpawnableList(entry.getCreatureType()).add(new SpawnListEntry(entry.getEntityClass(), entry.getSpawnWeight(), entry.getGroupSizeRange()[0], entry.getGroupSizeRange()[1]));
					}
				}
			}
		}

	}
	
	public static void setSpawnPlacementType(MMSpawnEntry entry)
	{
		EntitySpawnPlacementRegistry.setPlacementType(entry.getEntityClass(), entry.getSpawnPlacementType());
	}
}
