package net.daveyx0.multimob.spawn;

import net.daveyx0.multimob.common.capabilities.CapabilityVariantEntity;
import net.daveyx0.multimob.common.capabilities.IVariantEntity;
import net.daveyx0.multimob.config.MMConfigSpawns;
import net.daveyx0.multimob.core.MultiMob;
import net.daveyx0.multimob.message.MMMessageRegistry;
import net.daveyx0.multimob.message.MessageMMTameable;
import net.daveyx0.multimob.message.MessageMMVariant;
import net.daveyx0.multimob.util.EntityUtil;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.entity.living.LivingPackSizeEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

//Do custom event based spawning
public class MMSpawnerEventHandler {
	
    private MMWorldSpawner worldSpawner = null;

    public MMSpawnerEventHandler()
    {
    }

    //Custom spawning; not used for now. Needs to be improved. Can still be used with config changes.
    @SubscribeEvent
    public void onWorldTickEvent(TickEvent.WorldTickEvent event)
    {
        if (event.phase != TickEvent.WorldTickEvent.Phase.START || !(event.world instanceof WorldServer))
        {
            return;
        }

        WorldServer worldServer = (WorldServer)event.world;

        if (MMConfigSpawns.getUseAdditionalSpawning())
        {
        	if(worldSpawner == null)
        	{
            	this.worldSpawner = new MMWorldSpawner();
        	}
            WorldInfo worldInfo = worldServer.getWorldInfo();

            if (worldInfo.getWorldTotalTime() % MMConfigSpawns.getSpawnTickDelay() == 0)
            {
                this.worldSpawner.findChunksForSpawning(worldServer, worldServer.getGameRules().getBoolean("doMobSpawning"), true, true);
            }
        }
        else
        {
        	worldSpawner = null;
        }
    }

    //Do custom spawn checks before placing entity in the world.
    @SubscribeEvent
    public void onCheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
    	if(event.getResult() == Result.DENY) {return;}
    	
    	for(MMSpawnEntry entry: MMSpawnRegistry.getSpawnEntries())
    	{
    		if(entry.getEntityClass().equals(event.getEntityLiving().getClass()))
    		{
    	    	if(entry.getVariantID() != 0 && CapabilityVariantEntity.EventHandler.hasVariant(event.getEntity()))
    	    	{
    	    		IVariantEntity variant = EntityUtil.getCapability(event.getEntity(), CapabilityVariantEntity.VARIANT_ENTITY_CAPABILITY, null);
	    			
    	    		if(variant != null)
    	    		{
    	    			variant.setVariant(entry.getVariantID());
    	    		}
    	    	}
    	    	
    			if(MMSpawnChecks.performSpawnChecks((WorldServer) event.getWorld(), new BlockPos(event.getX(), event.getY(),event.getZ()), entry))
    			{
					if(entry.getEntryName().equals("_GreyZombie")){MultiMob.LOGGER.info(entry.getEntryName() + " spawned at " + event.getX() + " " + event.getY()  + " " + event.getZ());}
    				if(!entry.getOverrideCanGetSpawnHere()){event.setResult(Result.DEFAULT); return;}
    				
    				if(MMSpawnChecks.canEntitySpawnHere(event.getEntityLiving(), entry))
    				{

    					event.setResult(Result.ALLOW);
    				}
        			else
        			{
        				event.setResult(Result.DENY);
        			}
    			}
    			else
    			{
    				event.setResult(Result.DENY);
    			}
    		}
    	}
    }
    
    
    @SubscribeEvent
    public void onLivingPackSizeEvent(LivingPackSizeEvent event)
    {
    	MMSpawnEntry entry = MMSpawnRegistry.getSpawnEntryFromEntityClass(event.getEntityLiving().getClass());
    	if(entry != null && entry.getGroupSizeRange() != null && entry.getGroupSizeRange()[1] > 0)
    	{
    		event.setMaxPackSize(entry.getGroupSizeRange()[1]);
    		event.setResult(Result.ALLOW);
    	}
    }
    
    
    public MMWorldSpawner getWorldSpawner()
    {
        return this.worldSpawner;
    }
}