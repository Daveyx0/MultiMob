package net.daveyx0.multimob.spawn;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class MMSpawnEntry
{
	private String entryName;
    private Class<? extends Entity> entityClass;
    
    private SpawnPlacementType spawnType;
    private WeatherCondition weatherCondition;
    private EnumCreatureType creatureType;
    
    private int[] heightLevelRange;
    private int[] lightLevelRange;
    private int[] groupSizeRange;
    
    private List<Class<? extends Entity>> entitiesNear;
    private List<IBlockState> blocksNear;
    private List<IBlockState> spawnBlocks;
    private List<Biome> biomes;
    private List<Type> biomeTypes;
    private List<String> structures;
    private List<Integer> dimensions;
    
    private boolean allowedOnPeaceful;
    private boolean overrideEntityCanSpawnHere;
    private boolean isAllowedToSpawn;
    private boolean needsLoadsOfSpace;
    private boolean needsToSeeSky;
    
    private int spawnFrequency;
    private int additionalRarity;
    private int spawnWeight;
    private int variantID;

    public MMSpawnEntry(String entryName, Class<? extends Entity> class1, MMConfigSpawnEntry config)
    {
    	this.entryName = entryName;
        this.entityClass = class1;
        this.reloadInfoFromConfig(config);
    }
    
    public String toString()
    {
    	return entryName + " " + entityClass.getName();
    }
    
    public void reloadInfoFromConfig(MMConfigSpawnEntry config)
    {
        spawnType = config.getSpawnPlacementType();
        heightLevelRange = config.getHeightLevelRange();
        lightLevelRange = config.getLightLevelRange();
        entitiesNear = config.getEntitiesNearList();
        blocksNear = config.getBlocksNearList();
        spawnBlocks = config.getSpawnBlockList();
        biomes = config.getBiomeList();
        biomeTypes = config.getBiomeTypeList();
        structures = config.getStructureList();
        dimensions = config.getDimensionIdList();
        allowedOnPeaceful = config.getAllowedOnPeaceful();
        overrideEntityCanSpawnHere = config.getOverridesEntityCanSpawnHere();
        isAllowedToSpawn = config.getIsAllowedToSpawn();
        needsLoadsOfSpace = config.getNeedsLoadsOfSpace();
        needsToSeeSky = config.getNeedsToSeeSky();
        spawnFrequency = config.getSpawnLimit();
        additionalRarity = config.getAdditionalRarity();
        weatherCondition = config.getWeatherCondition();
        spawnWeight = config.getSpawnWeight();
        creatureType = config.getCreatureType();
        groupSizeRange = config.getGroupSizeRange();
        variantID = config.getVariantID();
    }
    
    //Get Methods
    public String getEntryName()
    {
    	return entryName;
    }
    
    public Class getEntityClass()
    {
    	return entityClass;
    }
    
    public int getSpawnLimit()
    {
    	return spawnFrequency;
    }
    
    public int getSpawnWeight()
    {
    	return spawnWeight;
    }
    
    public int getAdditionalRarity()
    {
    	return additionalRarity;
    }
    
    public SpawnPlacementType getSpawnPlacementType()
    {
    	return spawnType;
    }
    
    public WeatherCondition getWeatherCondition()
    {
    	return weatherCondition;
    }
    
    public EnumCreatureType getCreatureType()
    {
    	return creatureType;
    }
    
    public int[] getHeightLevelRange()
    {
    	return heightLevelRange;
    }
    
    public int[] getLightLevelRange()
    {
    	return lightLevelRange;
    }
    
	public int[] getGroupSizeRange() {

		return groupSizeRange;
	}
    
    public List<Class<? extends Entity>> getEntitiesNearList()
    {
    	return entitiesNear;
    }
    
    public List<IBlockState> getSpawnBlocksList()
    {
    	return spawnBlocks;
    }
    
    public List<IBlockState> getBlocksNearList()
    {
    	return blocksNear;
    }
    
    public List<Biome> getBiomes()
    {
    	return biomes;
    }
    
    public List<Type> getBiomeTypes()
    {
    	return biomeTypes;
    }
    
    public List<Integer> getDimensions()
    {
    	return dimensions;
    }
    
    public List<String> getStructures()
    {
    	return structures;
    }
    
    public boolean getIsAllowedOnPeaceful()
    {
    	return allowedOnPeaceful;
    }
    
    public boolean getOverrideCanGetSpawnHere()
    {
    	return overrideEntityCanSpawnHere;
    }
    
    public boolean getIsAllowedToSpawn()
    {
    	return isAllowedToSpawn;
    }
    
    public boolean getNeedsMoreSpace()
    {
    	return needsLoadsOfSpace;
    }
    
    public boolean getNeedsToSeeSky()
    {
    	return needsToSeeSky;
    }
    
    public int getVariantID()
    {
    	return variantID;
    }
    
    static enum WeatherCondition
    {
    	NONE,
    	GENERAL_DOWNFALL,
    	THUNDERSTORM,
    	RAIN,
    	SNOW
    }
}