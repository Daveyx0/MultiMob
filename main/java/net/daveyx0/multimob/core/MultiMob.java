package net.daveyx0.multimob.core;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.daveyx0.multimob.common.MMCommonProxy;
import net.daveyx0.multimob.config.MMConfig;
import net.daveyx0.multimob.message.MMMessageRegistry;
import net.daveyx0.multimob.spawn.MMSpawnerEventHandler;
import net.daveyx0.multimob.util.FileUtil;
import net.minecraftforge.common.MinecraftForge;
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
