package net.daveyx0.multimob.spawn;

import java.util.ArrayList;
import java.util.List;

import net.daveyx0.multimob.core.MultiMob;
import net.daveyx0.multimob.spawn.MMSpawnEntry.WeatherCondition;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class MMConfigSpawnEntry {
	
	private static String category1 = "spawnLimit", category2 = "spawnAllowed", category3 = "peacefulAllowed", category4 = "addedRarity", category5 = "spawnBiomeTypes"
			, category6 = "spawnBiomes", category7 = "spawnDimensions", category8 = "heightLevels", category9 = "lightLevels", category10 = "spawnType",
			category11 = "weatherCondition", category12 = "spawnBlocks", category13 = "blocksNearSpawn", category14 = "spawnStructures", category15 = "entitiesNearSpawn",
			category16 = "needsMoreSpace", category17 = "needsToSeeSky", category18 = "canOverrideSpawnChecks", category19 = "creatureTypes", category20= "spawnWeights"
					, category21 = "spawnGroupSizes";

	private String entryName;
	public String entityName;
	
	private final int defaultSpawnWeight;
	private final boolean defaultIsAllowedToSpawn;

	private String spawnType, weatherCondition, creatureType;
	private String[] entitiesNear, spawnBlocks, blocksNear, biomes, biomeTypes, structures;
	private int[] dimensions, heightLevels, lightLevels, groupSize;
	private boolean allowedOnPeaceful, overrideEntityCanSpawnHere, isAllowedToSpawn, needsLoadsOfSpace, needsToSeeSky;
	private int spawnFrequency, additionalRarity, spawnWeight;
	
	public MMConfigSpawnEntry(String entryName, String entityName)
	{
		this(entryName, entityName, 100, true);
		this.entryName = entryName;
		this.entityName = entityName;
	}
	
	public MMConfigSpawnEntry(String entryName, String entityName, int spawnWeight, boolean isAllowedToSpawn)
	{
		this.entryName = entryName;
		this.entityName = entityName;
		this.defaultSpawnWeight = spawnWeight;
		this.defaultIsAllowedToSpawn = isAllowedToSpawn;
	}
	
	public void load(Configuration config) {
		
		spawnFrequency = config.get(category1, entryName, defaultSpawnFrequency).getInt();
		isAllowedToSpawn = config.get(category2, entryName, defaultIsAllowedToSpawn).getBoolean();
		allowedOnPeaceful = config.get(category3, entryName, defaultAllowedOnPeaceful).getBoolean();		
		additionalRarity = config.get(category4, entryName, defaultAdditionalRarity).getInt();
		biomeTypes = config.get(category5, entryName , defaultBiomeTypes).getStringList();
		biomes = config.get(category6, entryName , defaultBiomes).getStringList();
		dimensions = config.get(category7, entryName, defaultDimensions).getIntList();
		heightLevels = config.get(category8, entryName , defaultHeightLevels).getIntList();
		lightLevels = config.get(category9, entryName , defaultLightLevels).getIntList();
		spawnType = config.get(category10, entryName , defaultSpawnType).getString();
		weatherCondition = config.get(category11, entryName , defaultWeatherType).getString();
		spawnBlocks = config.get(category12, entryName, defaultSpawnBlocks).getStringList();
		blocksNear = config.get(category13, entryName, defaultBlocksNear).getStringList();
		structures = config.get(category14, entryName, defaultStructures).getStringList();
		entitiesNear = config.get(category15, entryName , defaultEntitiesNear).getStringList();
		needsLoadsOfSpace = config.get(category16, entryName, defaultNeedsLoadsOfSpace).getBoolean();
		needsToSeeSky = config.get(category17, entryName, defaultNeedsToSeeSky).getBoolean();
		overrideEntityCanSpawnHere = config.get(category18, entryName, defaultOverrideEntityCanSpawnHere).getBoolean();
		creatureType = config.get(category19, entryName , defaultCreatureType).getString();
		spawnWeight = config.get(category20, entryName, defaultSpawnWeight).getInt();
		groupSize = config.get(category21, entryName , defaultGroupSize).getIntList();
	}
	
	public static void setupCategoryDescriptions(Configuration config)
	{
		config.addCustomCategoryComment(category1, "Spawn limit of this mob; if this limit is reached, the mob will no longer spawn until another one despawns.");
		config.addCustomCategoryComment(category2, "Determines if this spawn setting is enabled; if false the mob will not spawn");
		config.addCustomCategoryComment(category3, "Determines if the mob is allowed to spawn on Peaceful difficulty; if false the mob will not spawn on peaceful");
		config.addCustomCategoryComment(category4, "Adds an additional rarity check when spawning; the higher this number the lower the spawn successrate");
		config.addCustomCategoryComment(category5, "Biome Types this mob will spawn in; for a list of all different types check, the multimob config folder; requires capital letters and adding multiple will require the spawn biome to have all these types present! Example: FOREST");
		config.addCustomCategoryComment(category6, "Biomes this mob will spawn in; for a list of all different biomes present, check the multimob config folder; adding multiple will allow the mob to spawn in each of those biomes.");
		config.addCustomCategoryComment(category7, "Dimensions this mob will spawn in; they go by ID. Example: 0, -1 (This is the Overworld and the Nether)");
		config.addCustomCategoryComment(category8, "Height levels this mob will spawn in between; first number is min height, second is max. Putting -1 will ignore that height limit");
		config.addCustomCategoryComment(category9, "Light levels this mob will spawn in between; first number is min light, second is max. Putting -1 will ignore the light limit and putting -2 for both will make the check the same as normal hostile mobs.");
		config.addCustomCategoryComment(category10, "The spawn type this mob will use to check if they can spawn at a certain location; self explanatory. Valid entries: GROUND, WATER, AIR, LAVA");
		config.addCustomCategoryComment(category11, "The weather conditions this mob will spawn at; self explanatory. Valid entries: THUNDER, RAIN, SNOW, DOWNFALL (this means generally when there is downfall)");
		config.addCustomCategoryComment(category12, "Blocks or BlockStates this mob can spawn on; for a list of all different blockstates present, check the multimob config folder. This is NOT checked if the regular spawn checks are not overridden.");
		config.addCustomCategoryComment(category13, "Blocks or BlockStates that need to be near in order for this mob to spawn; for a list of all different blockstates present, check the multimob config folder. Adding multiple will require all those blocks to be near.");
		config.addCustomCategoryComment(category15, "Entities that need to be near in order for this mob to spawn; for a list of all different entities present, check the multimob config folder. Adding multiple will require all those entities to be near.");
		config.addCustomCategoryComment(category14, "Structures this mob can spawn inside of; for a list of all different vanilla structures present, check the multimob config folder. Adding multiple will allow the mob to spawn in each structure.");
		config.addCustomCategoryComment(category16, "Determines if the mob needs more space to spawn; if true the mob will need more air blocks around it to spawn");
		config.addCustomCategoryComment(category17, "Determines if the mob needs to be under the sky to spawn; if true the mob needs to be able to see the sky in order to spawn");
		config.addCustomCategoryComment(category18, "Determines if the spawn should use the mob's inherent spawn checks; if false it will not use them and instead use the spawn blocks setting to see which block to spawn on.");
		config.addCustomCategoryComment(category19, "Determine the creature type, which is used to determine the limit of each type that can spawn in the world. Only really useful for mod makers. Valid entries: MULTIMOBMONSTER, MULTIMOBPASSIVE, MULTIMOBWATER, MULTIMOBLAVA, MONSTER, CREATURE, AMBIENT, WATERCREATURE.");
		config.addCustomCategoryComment(category20, "Spawn weights are used to determine the frequency of the mob being chosen by the spawner. The higher this number, the more chance the mob has to spawn");
		config.addCustomCategoryComment(category21, "Spawn group size; determines the minimum and maximum amount of mobs that will appear for each spawn.");
		
	}
	
	public String getEntryName()
	{
		return this.entryName;
	}
	
	public SpawnPlacementType getSpawnPlacementType()
	{
		if(spawnType.equals("LAVA"))
		{
			return MultiMob.IN_LAVA;
		}
		else if(spawnType.equals("AIR"))
		{
			return SpawnPlacementType.IN_AIR;
		}
		else if(spawnType.equals("WATER"))
		{
			return SpawnPlacementType.IN_WATER;
		}
		else
		{
			return SpawnPlacementType.ON_GROUND;
		}
	}
	
	public WeatherCondition getWeatherCondition()
	{
		if(weatherCondition.equals("DOWNFALL"))
		{
			return WeatherCondition.GENERAL_DOWNFALL;
		}
		else if(weatherCondition.equals("THUNDER"))
		{
			return WeatherCondition.THUNDERSTORM;
		}
		else if(weatherCondition.equals("RAIN"))
		{
			return WeatherCondition.RAIN;
		}
		else if(weatherCondition.equals("SNOW"))
		{
			return WeatherCondition.SNOW;
		}
		else
		{
			return WeatherCondition.NONE;
		}
	}
	
	public List<Class<? extends Entity>> getEntitiesNearList()
	{
		if(entitiesNear != null && !entitiesNear[0].equals(""))
		{
		List<Class<? extends Entity>> entryList = new ArrayList<Class<? extends Entity>>();
		for(String entry : entitiesNear)
		{
			EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entry));
			if(entityEntry != null)
			{
				entryList.add(entityEntry.getEntityClass());
			}
		}

			return entryList;
		}
		
		return null;
	}
	
	public List<Biome> getBiomeList()
	{
		if(biomes != null && !biomes[0].equals(""))
		{
		
		List<Biome> entryList = new ArrayList<Biome>();
		for(String entry : biomes)
		{
			Biome biomeEntry = ForgeRegistries.BIOMES.getValue(new ResourceLocation(entry));
			if(biomeEntry != null)
			{
				entryList.add(biomeEntry);
			}
		}

		return entryList;
		}
		return null;
	}
	
	public List<Type> getBiomeTypeList()
	{
		if(biomeTypes != null && !biomeTypes[0].equals(""))
		{
		
		List<Type> entryList = new ArrayList<Type>();
		for(String entry : biomeTypes)
		{
			Type biomeTypeEntry = BiomeDictionary.Type.getType(entry);
			if(biomeTypeEntry != null)
			{
				entryList.add(biomeTypeEntry);
			}
		}

		return entryList;
		}
		return null;
	}

	public int[] getHeightLevelRange() {

		return heightLevels;
	}

	public int[] getLightLevelRange() {
		
		return lightLevels;
	}

	public List<IBlockState> getBlocksNearList() {

		if(blocksNear != null && !blocksNear[0].equals(""))
		{
			return createBlockStateList(blocksNear);
		}
		return null;
	}

	public List<IBlockState> getSpawnBlockList() {

		if(spawnBlocks != null && !spawnBlocks[0].equals(""))
		{
			return createBlockStateList(spawnBlocks);
		}
		return null;
	}
	
	public List<IBlockState> createBlockStateList(String[] array)
	{
		List<IBlockState> entryList = new ArrayList<IBlockState>();
		for(String entry : array)
		{
			Block blockEntry = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(entry));
			if(blockEntry != null)
			{
				entryList.add(blockEntry.getDefaultState());
			}
			else
			{
				for(Block block : ForgeRegistries.BLOCKS.getValuesCollection())
				{
					for(IBlockState state: block.getBlockState().getValidStates())
					{
						if(entry.equals(state.toString()))
						{
							entryList.add(state);
						}
					}
				}
			}
		}
		
		return entryList;
	}

	public List<Integer> getDimensionIdList() {
		
		if(dimensions != null)
		{
		List<Integer> entryList = new ArrayList<Integer>();
		for(int entry : dimensions)
		{
			entryList.add(entry);
		}
		return entryList;
		}
		return null;
	}

	public boolean getAllowedOnPeaceful() {
		return this.allowedOnPeaceful;
	}

	public boolean getOverridesEntityCanSpawnHere() {
		return this.overrideEntityCanSpawnHere;
	}

	public boolean getIsAllowedToSpawn() {
		return this.isAllowedToSpawn;
	}

	public boolean getNeedsLoadsOfSpace() {
		return this.needsLoadsOfSpace;
	}
	
	public boolean getNeedsToSeeSky() {
		return this.needsToSeeSky;
	}

	public int getSpawnLimit() {
		return this.spawnFrequency;
	}

	public int getAdditionalRarity() {
		return this.additionalRarity;
	}
	
	public int getSpawnWeight() {
		return this.spawnWeight;
	}
	
	public EnumCreatureType getCreatureType()
	{
		if(creatureType.equals("MULTIMOBMONSTER"))
		{
			return MultiMob.MULTIMOB_MONSTER;
		}
		else if(creatureType.equals("MULTIMOBPASSIVE"))
		{
			return MultiMob.MULTIMOB_PASSIVE;
		}
		else if(creatureType.equals("MULTIMOBWATER"))
		{
			return MultiMob.MULTIMOB_WATER;
		}
		else if(creatureType.equals("MULTIMOBLAVA"))
		{
			return MultiMob.MULTIMOB_LAVA;
		}
		else if(creatureType.equals("CREATURE"))
		{
			return EnumCreatureType.CREATURE;
		}
		else if(creatureType.equals("AMBIENT"))
		{
			return EnumCreatureType.AMBIENT;
		}
		else if(creatureType.equals("WATERCREATURE"))
		{
			return EnumCreatureType.WATER_CREATURE;
		}
		else
		{
			return EnumCreatureType.MONSTER;
		}
	}
	
	public int[] getGroupSizeRange() {

		return groupSize;
	}


	public List<String> getStructureList() {

		if(structures != null && !structures[0].equals(""))
		{
		List<String> entryList = new ArrayList<String>();
		for(String entry : structures)
		{
			entryList.add(entry);
		}
			return entryList;
		}
		return null;
	}
	
	private String defaultSpawnType = "GROUND", defaultWeatherType = "NONE", defaultCreatureType = "MULTIMOBMONSTER";
	private String[] defaultEntitiesNear = new String[]{""}, defaultSpawnBlocks = new String[]{""}, defaultBlocksNear = new String[]{""}, defaultBiomes = new String[]{""}, defaultBiomeTypes = new String[]{""}, defaultStructures = new String[]{""};
	private int[] defaultDimensions = new int[]{0}, defaultHeightLevels = new int[]{-1, -1}, defaultLightLevels = new int[]{-1, -1}, defaultGroupSize = new int[]{1,1};
	private boolean defaultAllowedOnPeaceful = false, defaultOverrideEntityCanSpawnHere = false, defaultNeedsLoadsOfSpace = false, defaultNeedsToSeeSky = false;
	private int defaultAdditionalRarity = -1, defaultSpawnFrequency = -1;
	
	
	public MMConfigSpawnEntry setSpawnType(String i) {defaultSpawnType = i;return this;}
	public MMConfigSpawnEntry setWeatherCondition(String i) {defaultWeatherType = i;return this;}
	public MMConfigSpawnEntry setEntitiesNear(String[] i){defaultEntitiesNear = i;return this;}
	public MMConfigSpawnEntry setSpawnBlocks(String[] i){defaultSpawnBlocks = i;return this;}
	public MMConfigSpawnEntry setBlocksNear(String[] i){defaultBlocksNear = i;return this;}
	public MMConfigSpawnEntry setBiomes(String[] i){defaultBiomes = i;return this;}
	public MMConfigSpawnEntry setBiomeTypes(String[] i){defaultBiomeTypes = i;return this;}
	public MMConfigSpawnEntry setStructures(String[] i){defaultStructures = i;return this;}
	public MMConfigSpawnEntry setDimensions(int[] i){defaultDimensions = i;return this;}
	public MMConfigSpawnEntry setAllowedOnPeacful(boolean i){defaultAllowedOnPeaceful = i;return this;}
	public MMConfigSpawnEntry setOverrideCanSpawnHere(boolean i){defaultOverrideEntityCanSpawnHere = i;return this;}
	public MMConfigSpawnEntry setNeedsMoreSpace(boolean i){defaultNeedsLoadsOfSpace = i;return this;}
	public MMConfigSpawnEntry setNeedsToSeeSky(boolean i){defaultNeedsToSeeSky = i;return this;}
	public MMConfigSpawnEntry setAdditionalRarity(int i){defaultAdditionalRarity = i;return this;}
	public MMConfigSpawnEntry setHeightLevel(int i, int j){defaultHeightLevels[0] = i; defaultHeightLevels[1] = j;return this;}
	public MMConfigSpawnEntry setLightLevel(int i, int j){defaultLightLevels[0] = i; defaultLightLevels[1] = j;return this;}
	public MMConfigSpawnEntry setSpawnLimit(int i){defaultSpawnFrequency = i; return this;}
	public MMConfigSpawnEntry setCreatureType(String i) {defaultCreatureType = i;return this;}
	public MMConfigSpawnEntry setGroupSize(int i, int j){defaultGroupSize[0] = i; defaultGroupSize[1] = j;return this;}
	
	public MMConfigSpawnEntry setupBaseAnimalSpawnEntry(boolean overrideSpawnChecks)
	{
		this.defaultSpawnBlocks = new String[]{"minecraft:grass"};
		this.defaultLightLevels[0] = 9;
		this.defaultAllowedOnPeaceful = true;
		this.overrideEntityCanSpawnHere = overrideSpawnChecks;
		return this;
	}
	
	public MMConfigSpawnEntry setupBaseMobSpawnEntry(boolean overrideSpawnChecks)
	{
		this.defaultLightLevels[0] = -2;
		this.defaultLightLevels[1] = -2;
		this.overrideEntityCanSpawnHere = overrideSpawnChecks;
		return this;
	}
}
