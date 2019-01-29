package net.daveyx0.multimob.core;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.daveyx0.multimob.common.MMCommonProxy;
import net.daveyx0.multimob.config.MMConfig;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.multimob.entity.IMultiMobLava;
import net.daveyx0.multimob.entity.IMultiMobPassive;
import net.daveyx0.multimob.entity.IMultiMobWater;
import net.daveyx0.multimob.message.MMMessageRegistry;
import net.daveyx0.multimob.spawn.MMSpawnRegistry;
import net.daveyx0.multimob.spawn.MMSpawnerEventHandler;
import net.daveyx0.multimob.util.FileUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= MMReference.MODID, name = MMReference.NAME, version = MMReference.VERSION, acceptedMinecraftVersions = "[1.12]"
,guiFactory = "net.daveyx0.multimob.config.MMFactoryGui")
public class MultiMob 
{
	public static final Logger LOGGER = LogManager.getLogger(MMReference.MODID);

	@Instance(MMReference.MODID)
	public static MultiMob instance = new MultiMob();
	
	@SidedProxy(clientSide = "net.daveyx0.multimob.client.MMClientProxy", serverSide = "net.daveyx0.multimob.common.MMCommonProxy")
	public static MMCommonProxy proxy;
	private File directory;
	
	
	public final static SpawnPlacementType IN_LAVA = net.minecraftforge.common.util.EnumHelper.addSpawnPlacementType("IN_LAVA", new java.util.function.BiPredicate<net.minecraft.world.IBlockAccess, BlockPos>(){

		@Override
		public boolean test(IBlockAccess t, BlockPos u) {
			
			 IBlockState iblockstate = t.getBlockState(u);
			 return iblockstate.getMaterial() == Material.LAVA && t.getBlockState(u.down()).getMaterial() == Material.LAVA && !t.getBlockState(u.up()).isNormalCube();
		}}
		);
	;
	public final static EnumCreatureType MULTIMOB_MONSTER = net.minecraftforge.common.util.EnumHelper.addCreatureType("MULTIMOB_MONSTER", IMultiMob.class, 35, Material.AIR, false, false);
	public final static EnumCreatureType MULTIMOB_PASSIVE = net.minecraftforge.common.util.EnumHelper.addCreatureType("MULTIMOB_PASSIVE", IMultiMobPassive.class, 10, Material.AIR, false, false);
	public final static EnumCreatureType MULTIMOB_WATER = net.minecraftforge.common.util.EnumHelper.addCreatureType("MULTIMOB_WATER", IMultiMobWater.class, 10, Material.WATER, false, false);
	public final static EnumCreatureType MULTIMOB_LAVA = net.minecraftforge.common.util.EnumHelper.addCreatureType("MULTIMOB_LAVA", IMultiMobLava.class, 5, Material.LAVA, false, false);

	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new MultiMob());
		MinecraftForge.EVENT_BUS.register(new MMEvents.EntityEventHandler());

		MMMessageRegistry.preInit();
		MMCapabilities.preInit();

		MinecraftForge.EVENT_BUS.register(new MMSpawnerEventHandler());	
		
		directory = new File(event.getModConfigurationDirectory(), MMReference.MODID);
		
		if(!directory.exists())
		{
			directory.mkdirs();
		}
		MMConfig.preInit(directory, event);
		
		proxy.preInit(event);
	}

	@EventHandler
	public void Init(FMLInitializationEvent event)
	{
		MMTameableEntries.registerTameables();
		MMVariantEntries.registerVariants();
		MMSpawnRegistry.registerFillerSpawns();
		proxy.init(event);
	}

	@EventHandler 
	public void postInit(FMLPostInitializationEvent event)
	{
		File subDirectory = new File(directory, "modInformation");
		
		if(!subDirectory.exists())
		{
			subDirectory.mkdirs();
		}
		
		try
		{
			FileUtil.createTextFilesForModInfo(subDirectory);
		} 
		catch (IOException e) {

			e.printStackTrace();
		}
		
		MMConfig.postInit();
		proxy.postInit(event);
	}
	
	public File getDirectory()
	{
		return directory;
	}
}
