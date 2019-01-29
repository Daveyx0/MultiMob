package net.daveyx0.multimob.core;

import java.util.HashMap;

import net.daveyx0.multimob.spawn.MMConfigSpawnEntry;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class MMEntityRegistry {

		public static final HashMap<Class<? extends Entity>, Boolean> entities = new HashMap();
	    
	    public static void addEntities(String modID, Object modInstance, Class var1, String name1,  int entityid, int bkEggColor, int fgEggColor, boolean flag)
	    {	    	
	    	int trackingRange = 80;
	    	int updateFrequency = 3;
	    	boolean sendsVelocityUpdates = true;
	    	final ResourceLocation registryName = new ResourceLocation(modID, name1);
	    	
	    	if(flag)
	    	{
	    		EntityRegistry.registerModEntity(registryName, var1, modID + "." + name1, entityid,  modInstance, trackingRange, updateFrequency, sendsVelocityUpdates, bkEggColor, fgEggColor);
	    	}
	    	else
	    	{
	    		EntityRegistry.registerModEntity(registryName, var1, modID + "." + name1, entityid,  modInstance, trackingRange, updateFrequency, sendsVelocityUpdates);
	    	}
	    	
			entities.put(var1, flag);
	    }
	    
	    public static void addEntitiesWithoutEgg(String modID, Object modInstance, Class var1, String name1,  int entityid, boolean flag)
	    {  	
	    	int trackingRange = 80;
	    	int updateFrequency = 3;
	    	boolean sendsVelocityUpdates = true;
	    	final ResourceLocation registryName = new ResourceLocation(modID, name1);
			EntityRegistry.registerModEntity(registryName, var1, modID + "." + name1, entityid,  modInstance, trackingRange, updateFrequency, sendsVelocityUpdates);

			entities.put(var1, flag);
	    }
	    

	    public static void addCustomEntities(String modID, Object modInstance, Class var1, String name1,  int entityid, int track, int freq, boolean vel)
	    {
	    	int trackingRange = track;
	    	int updateFrequency = freq;
	    	boolean sendsVelocityUpdates = vel;
	    	final ResourceLocation registryName = new ResourceLocation(modID, name1);
			EntityRegistry.registerModEntity(registryName,var1, modID + "." + name1, entityid,  modInstance, trackingRange, updateFrequency, sendsVelocityUpdates);
	    }
	    
		 
	}