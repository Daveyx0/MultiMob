package net.daveyx0.multimob.spawn;

import net.daveyx0.multimob.config.MMConfigSpawns;
import net.daveyx0.multimob.core.MultiMob;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.entity.living.LivingPackSizeEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//Do custom event based spawning
public class MMSpawnerEventHandler {
	
    private MMWorldSpawner worldSpawner = null;

    public MMSpawnerEventHandler()
    {
    	if (MMConfigSpawns.getUseBetaSpawning())
    	{
            this.worldSpawner = new MMWorldSpawner();
    	}
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

        if (MMConfigSpawns.getUseBetaSpawning() && worldServer.getGameRules().getBoolean("doMobSpawning"))
        {
            WorldInfo worldInfo = worldServer.getWorldInfo();

            if (worldInfo.getWorldTotalTime() % MMConfigSpawns.getSpawnTickDelay() == 0)
            {
                this.worldSpawner.doCustomSpawning(worldServer);
            }
        }
    }

    //Do custom spawn checks before placing entity in the world.
    @SubscribeEvent
    public void onCheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
    	
    	if (!MMConfigSpawns.getUseBetaSpawning()) 
    	{
    	for(MMSpawnEntry entry: MMSpawnRegistry.getSpawnEntries())
    	{
    		if(entry.getEntityClass().equals(event.getEntityLiving().getClass()))
    		{
    			if(MMSpawnChecks.performSpawnChecks((WorldServer) event.getWorld(), new BlockPos(event.getX(), event.getY(),event.getZ()), entry))
    			{
    				if(MMSpawnChecks.canEntitySpawnHere(event.getEntityLiving(), entry))
    				{
    					//MultiMob.LOGGER.info(entry.getEntryName() + " spawned at " + event.getX() + " " + event.getY()  + " " + event.getZ());
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