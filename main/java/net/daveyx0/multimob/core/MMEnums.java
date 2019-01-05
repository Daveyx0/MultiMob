package net.daveyx0.multimob.core;

import net.daveyx0.multimob.config.MMConfigSpawns;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.multimob.entity.IMultiMobLava;
import net.daveyx0.multimob.entity.IMultiMobPassive;
import net.daveyx0.multimob.entity.IMultiMobWater;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.EnumHelper;

public class MMEnums {

	public static SpawnPlacementType IN_LAVA;
	public static EnumCreatureType MULTIMOB_MONSTER;
	public static EnumCreatureType MULTIMOB_PASSIVE;
	public static EnumCreatureType MULTIMOB_WATER;
	public static EnumCreatureType MULTIMOB_LAVA;
	
	public static void preInit()
	{
		IN_LAVA = EnumHelper.addSpawnPlacementType("IN_LAVA", new java.util.function.BiPredicate<net.minecraft.world.IBlockAccess, BlockPos>(){

			@Override
			public boolean test(IBlockAccess t, BlockPos u) {
				
				 IBlockState iblockstate = t.getBlockState(u);
				 return iblockstate.getMaterial() == Material.LAVA && t.getBlockState(u.down()).getMaterial() == Material.LAVA && !t.getBlockState(u.up()).isNormalCube();
			}}
			);
		
		MULTIMOB_MONSTER = EnumHelper.addCreatureType("MULTIMOB_MONSTER", IMultiMob.class, MMConfigSpawns.getMonsterSpawnLimit(), Material.AIR, false, false);
		MULTIMOB_PASSIVE = EnumHelper.addCreatureType("MULTIMOB_PASSIVE", IMultiMobPassive.class, MMConfigSpawns.getPassiveSpawnLimit(), Material.AIR, false, false);
		MULTIMOB_WATER = EnumHelper.addCreatureType("MULTIMOB_WATER", IMultiMobWater.class, MMConfigSpawns.getWaterSpawnLimit(), Material.WATER, false, false);
		MULTIMOB_LAVA = EnumHelper.addCreatureType("MULTIMOB_LAVA", IMultiMobLava.class, MMConfigSpawns.getLavaSpawnLimit(), Material.LAVA, false, false);
	}
}
