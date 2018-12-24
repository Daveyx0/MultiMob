package net.daveyx0.multimob.spawn;

import net.daveyx0.multimob.config.MMConfigSpawns;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//Do custom event based spawning
public class MMSpawnerEventHandler {
	
    private MMWorldSpawner worldSpawner;

    public MMSpawnerEventHandler()
    {
        this.worldSpawner = new MMWorldSpawner();
    }

    @SubscribeEvent
    public void onWorldTickEvent(TickEvent.WorldTickEvent event)
    {
        if (event.phase != TickEvent.WorldTickEvent.Phase.START || !(event.world instanceof WorldServer))
        {
            return;
        }

        WorldServer worldServer = (WorldServer)event.world;

        if (worldServer.getGameRules().getBoolean("doMobSpawning"))
        {
            WorldInfo worldInfo = worldServer.getWorldInfo();

            if (worldInfo.getWorldTotalTime() % MMConfigSpawns.getSpawnTickDelay() == 0)
            {
                this.worldSpawner.doCustomSpawning(worldServer);
            }
        }
    }

    public MMWorldSpawner getWorldSpawner()
    {
        return this.worldSpawner;
    }
}