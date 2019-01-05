package net.daveyx0.multimob.spawn;

import java.util.List;
import java.util.Set;

import net.daveyx0.multimob.config.MMConfigSpawns;
import net.daveyx0.multimob.core.MMEnums;
import net.daveyx0.multimob.core.MultiMob;
import net.daveyx0.multimob.spawn.MMSpawnEntry.WeatherCondition;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class MMSpawnChecks {

	private final static int distanceCheck = 10;
	private static boolean enableDebug = false;
	
	public static boolean performSpawnChecks(WorldServer worldIn, BlockPos pos, MMSpawnEntry entry)
	{
		//if(entry.getEntryName().equals("_FlameSpewer_Nether")){ enableDebug = true;} else{enableDebug = false;}
		
		if(!entry.getIsAllowedToSpawn()) {debug(entry, 0, pos, enableDebug); return false;}
		if(!isWithinWorldBorder(worldIn, pos)) {debug(entry, 1, pos, enableDebug);return false;}
		if(!entry.getIsAllowedOnPeaceful() && worldIn.getDifficulty() == EnumDifficulty.PEACEFUL) {debug(entry, 2, pos, enableDebug); return false;}
		if(!isLuckyEnoughToSpawn(worldIn, entry.getAdditionalRarity())){debug(entry, 3, pos, enableDebug); return false;}
		if(!isDimensionSuitable(worldIn, entry.getDimensions())){debug(entry, 4, pos, enableDebug); return false;}
		if(MMConfigSpawns.getUseBetaSpawning() && !isBiomeTypeSuitable(worldIn, pos, entry.getBiomeTypes())) {debug(entry, 5, pos, enableDebug); return false;}
		if(MMConfigSpawns.getUseBetaSpawning() && !isBiomeSuitable(worldIn, pos, entry.getBiomes())) {debug(entry, 6, pos, enableDebug); return false;}
		if(!isWeatherConditionSuitable(worldIn, pos, entry.getWeatherCondition())) {debug(entry, 19, pos, enableDebug); return false;}
		if(!isInsideSuitableStructure(worldIn, pos, entry.getStructures())) {debug(entry, 7, pos, enableDebug); return false;}
		if(MMConfigSpawns.getUseBetaSpawning() && !canCreatureTypeSpawnHere(worldIn, pos, entry.getSpawnPlacementType())) {debug(entry, 8, pos, enableDebug); return false;}
		if(entry.getNeedsToSeeSky() && !canPositionSeeSky(worldIn, pos)) {debug(entry, 18, pos, enableDebug); return false;}
		if(!isHeightLevelSuitable(pos, entry.getHeightLevelRange()[0], entry.getHeightLevelRange()[1])){debug(entry, 9, pos, enableDebug); return false;}
		if(!isLightLevelSuitable(worldIn, pos, entry.getLightLevelRange()[0], entry.getLightLevelRange()[1])){debug(entry, 10, pos, enableDebug); return false;}
		if(entry.getNeedsMoreSpace() && !hasLoadsOfSpaceAbove(worldIn, pos)) {debug(entry, 11, pos, enableDebug); return false;}
		if(!isNearEntity(worldIn, pos, entry.getEntitiesNearList(), distanceCheck)){debug(entry, 12, pos, enableDebug); return false;}
		if(!isNearBlock(worldIn, pos, entry.getBlocksNearList(), distanceCheck)){debug(entry, 13, pos, enableDebug); return false;}
		
		return true;
	}
	
	public static boolean canEntitySpawnHere(Entity entity, MMSpawnEntry entry)
	{
		if(entity == null || !(entity instanceof EntityLiving)){return false;}
		BlockPos pos = entity.getPosition();
		
		EntityLiving entityLiving = (EntityLiving)entity;
		if(!entityLiving.isNotColliding()){debug(entry, 14, pos, enableDebug); return false;}
		if(!entry.getOverrideCanGetSpawnHere()) {return entityLiving.getCanSpawnHere();}
		
		if(!canSpawnOnBlock(entity, entry.getSpawnBlocksList())) { debug(entry, 15, pos, enableDebug); return false;}
		if(!checkCanEntitySpawnOnBlockState(entity)){ debug(entry, 16, pos, enableDebug); return false;}
		if(entity instanceof EntityCreature && !checkBlockPathWeight((EntityCreature)entity)){ debug(entry, 17, pos, enableDebug); return false;}
		
		return true;
	}
	
	public static boolean isInsideSuitableStructure(WorldServer worldIn, BlockPos pos, List<String> structures)
	{
		if(structures == null || structures.isEmpty()){return true;}
		
		for(String entry : structures)
		{
			if(worldIn.getChunkProvider().isInsideStructure(worldIn, entry, pos))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isWeatherConditionSuitable(World worldIn, BlockPos pos, WeatherCondition condition)
	{
		if(condition == WeatherCondition.NONE)
		{
			return true;
		}
		if (condition == WeatherCondition.GENERAL_DOWNFALL)
       {
            return worldIn.isRaining();
       }
       else if (condition == WeatherCondition.THUNDERSTORM)
       {
       		return worldIn.isThundering();
       }
       else if (condition == WeatherCondition.RAIN || condition == WeatherCondition.SNOW)
       {
       		return isDownfallAtPosition(worldIn, pos, condition);
       }
		
       return false;
	}
	
	public static boolean isWithinWorldBorder(World worldIn, BlockPos pos)
	{
        return worldIn.getWorldBorder().contains(pos);
	}
	
	public static boolean isHeightLevelSuitable(BlockPos pos, int min, int max)
	{
		if(min == -1 && max == -1){ return true;}
		else if(min == -1){return pos.getY() <= max;}
		else if(max == -1){return pos.getY() >= min;}
		else{return pos.getY() >= min && pos.getY() <= max;}
	}
	
	public static boolean isLightLevelSuitable(World worldIn, BlockPos pos, int min, int max)
	{
		if(min == -1 && max == -1){ return true;}
		else if(min == -2 && max == -2){return isValidMobLightLevel(worldIn, pos);}
		else if(min == -1){return worldIn.getLight(pos) <= max;}
		else if(max == -1){return worldIn.getLight(pos) >= min;}
		else{return worldIn.getLight(pos) >= min && worldIn.getLight(pos) <= max;}
	}
	
	public static boolean isNearEntity(World worldIn, BlockPos pos, List<Class<? extends Entity>> entities, double distance)
	{
		if(entities == null || entities.isEmpty() ||distance == -1){ return true;}
		
		for(Entity entity : worldIn.getLoadedEntityList())
		{
			if(entity != null && entities.contains(entity.getClass()))
			{
				double d0 = entity.getDistanceSq(pos.getX(), pos.getY(), pos.getZ());
				if(d0 < 0 || d0 < distance * distance)
				{
					entities.remove(entity.getClass());
				}
			}
		}
		
        return entities.isEmpty();
	}
	
	public static boolean isNearBlock(World worldIn, BlockPos pos, List<IBlockState> blockstates, int searchLength)
	{
		if(blockstates == null || blockstates.isEmpty() || searchLength == -1){ return true;}
        int i = searchLength;
        int j = 1;

        for (int k = 0; k <= 1; k = k > 0 ? -k : 1 - k)
        {
            for (int l = 0; l < i; ++l)
            {
                for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1)
                {
                    for (int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1)
                    {
                        BlockPos blockpos1 = pos.add(i1, k, j1);

                        if (blockstates.contains(worldIn.getBlockState(blockpos1)))
                        {
                            blockstates.remove(worldIn.getBlockState(blockpos1));
                        }
                    }
                }
            }
        }
        
        return blockstates.isEmpty();
	}
	
	public static boolean isLuckyEnoughToSpawn(World worldIn, int chance)
	{
		if(chance == -1){return true;}
		return worldIn.rand.nextInt(chance) == 0;
	}
	
	public static boolean isBiomeSuitable(World worldIn, BlockPos pos, List<Biome> biomes)
	{
		if(biomes == null || biomes.isEmpty()){return true;}
		return biomes.contains(worldIn.getBiome(pos));
	}
	
	public static boolean isBiomeTypeSuitable(World worldIn, BlockPos pos, List<BiomeDictionary.Type> types)
	{
		if(types == null || types.isEmpty()){return true;}
		Biome biome = worldIn.getBiome(pos);
		if(!BiomeDictionary.hasAnyType(biome)){return false;}
		
		Set<BiomeDictionary.Type> biomeTypes = BiomeDictionary.getTypes(biome);
		for(BiomeDictionary.Type type : types)
		{
			if(!biomeTypes.contains(type))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean isDimensionSuitable(World worldIn, List<Integer> ids)
	{
		if(ids == null || ids.isEmpty()){return worldIn.provider.getDimension() == 0;}
		return ids.contains(worldIn.provider.getDimension());
	}
	
	public static boolean isAllowedToSpawnOnPeaceful(World worldIn, boolean check)
	{
		return check ? true : worldIn.getDifficulty() != EnumDifficulty.PEACEFUL;
	}
	
	public static boolean canCreatureTypeSpawnHere(World worldIn, BlockPos pos, EntityLiving.SpawnPlacementType spawnType)
	{
		 IBlockState iblockstate = worldIn.getBlockState(pos);

		 if(spawnType == MMEnums.IN_LAVA)
		 {
			 return iblockstate.getMaterial() == Material.LAVA && worldIn.getBlockState(pos.down()).getMaterial() == Material.LAVA && !worldIn.getBlockState(pos.up()).isNormalCube();
		 }
		 else if (spawnType == EntityLiving.SpawnPlacementType.IN_WATER)
         {
             return iblockstate.getMaterial() == Material.WATER && worldIn.getBlockState(pos.down()).getMaterial() == Material.WATER && !worldIn.getBlockState(pos.up()).isNormalCube();
         }
         else
         {
             BlockPos blockpos = pos.down();
             IBlockState state = worldIn.getBlockState(blockpos);

             if (!state.getBlock().canCreatureSpawn(state, worldIn, blockpos, spawnType))
             {
                 return false;
             }
             else
             {
                 Block block = worldIn.getBlockState(blockpos).getBlock();
                 boolean flag = block != Blocks.BEDROCK && block != Blocks.BARRIER;
                 return flag && WorldEntitySpawner.isValidEmptySpawnBlock(iblockstate) && WorldEntitySpawner.isValidEmptySpawnBlock(worldIn.getBlockState(pos.up()));
             }
         }
	}
	
	public static boolean hasLoadsOfSpaceAbove(World worldIn, BlockPos pos)
	{
		return check3x3IsAirBlock(worldIn, pos.up()) && check3x3IsAirBlock(worldIn, pos.up(2));
	}
	
    public static boolean check3x3IsAirBlock(World worldIn, BlockPos pos)
    {
    	return worldIn.isAirBlock(pos) && worldIn.isAirBlock(pos.west()) && worldIn.isAirBlock(pos.west().north())
    			&& worldIn.isAirBlock(pos.west().south()) && worldIn.isAirBlock(pos.east()) && worldIn.isAirBlock(pos.east().north())
    			&& worldIn.isAirBlock(pos.east().south()) && worldIn.isAirBlock(pos.north()) && worldIn.isAirBlock(pos.south());
    }
    
    protected static boolean isValidMobLightLevel(World worldIn, BlockPos pos)
    {
        if (worldIn.getLightFor(EnumSkyBlock.SKY, pos) > worldIn.rand.nextInt(32))
        {
            return false;
        }
        else
        {
            int i = worldIn.getLightFromNeighbors(pos);

            if (worldIn.isThundering())
            {
                int j = worldIn.getSkylightSubtracted();
                worldIn.setSkylightSubtracted(10);
                i = worldIn.getLightFromNeighbors(pos);
                worldIn.setSkylightSubtracted(j);
            }

            return i <= worldIn.rand.nextInt(8);
        }
    }
    
    public static boolean canPositionSeeSky(World worldIn, BlockPos pos)
    {
    	return worldIn.canBlockSeeSky(pos);
    }
    
	public static boolean checkCanEntitySpawnOnBlockState(Entity entity)
	{
        IBlockState iblockstate = entity.world.getBlockState((new BlockPos(entity)).down());
        return iblockstate.canEntitySpawn(entity);
	}
	
	public static boolean checkBlockPathWeight(EntityCreature creature)
	{
		return creature.getBlockPathWeight(new BlockPos(creature.posX, creature.getEntityBoundingBox().minY, creature.posZ)) >= 0.0F;
	}
	
	public static boolean canSpawnOnBlock(Entity entity, List<IBlockState> blockstates)
	{
		if(blockstates == null || blockstates.isEmpty()) {return true;}
		int i = MathHelper.floor(entity.posX);
        int j = MathHelper.floor(entity.getEntityBoundingBox().minY);
        int k = MathHelper.floor(entity.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        return blockstates.contains(entity.world.getBlockState(blockpos.down()));
	}
	
	public static boolean isDownfallAtPosition(World worldIn, BlockPos position, WeatherCondition condition)
	{
        if (!worldIn.isRaining())
        {
            return false;
        }
        else if (!worldIn.canSeeSky(position))
        {
            return false;
        }
        else if (worldIn.getPrecipitationHeight(position).getY() > position.getY())
        {
            return false;
        }
        else
        {
            Biome biome = worldIn.getBiome(position);
        	if(condition == WeatherCondition.RAIN)
        	{
        		return biome.canRain();
        	}
        	else if(condition == WeatherCondition.SNOW)
        	{
        		return biome.getEnableSnow() && worldIn.canSnowAt(position, false);
        	}
        	else
        	{
        		return true;
        	}
        }
	}
	
	private static void debug(MMSpawnEntry entry, int context, BlockPos pos, boolean allow)
	{
		if(!allow){return;}
		
		String message = entry.getEntryName() + " failed to spawn at " + pos.toString() +  " due to ";
		
		switch (context)
		{
			case 0: {message += "the entry not being allowed to spawn."; break;}
			case 1: {message += "the position being outside of the world border."; break;}
			case 2: {message += "the entry not being allowed to spawn on Peaceful."; break;}
			case 3: {message += "the entry not being lucky enough to spawn."; break;}
			case 4: {message += "the dimension not being suitable."; break;}
			case 5: {message += "the biome type not being suitable."; break;}
			case 6: {message += "the biome not being suitable."; break;}
			case 7: {message += "the structure not being at position or suitable."; break;}
			case 8: {message += "the spawn placement type preventing spawn."; break;}
			case 9: {message += "the height position not being suitable."; break;}
			case 10: {message += "the light level at position not being suitable."; break;}
			case 11: {message += "there not being enough available space."; break;}
			case 12: {message += "the appropriate entity not being nearby."; break;}
			case 13: {message += "the appropriate block not being nearby."; break;}
			case 14: {message += "the entity colliding."; break;}
			case 15: {message += "the entity not being on the correct block."; break;}
			case 16: {message += "the blockstate below not allowing entities to spawn."; break;}
			case 17: {message += "the block path weight being too low."; break;}
			case 18: {message += "the position could not see the sky."; break;}
			case 19: {message += "the position does not have a suitable weather condition."; break;}
		}
		
		MultiMob.LOGGER.info(message);
	}

}
