package net.daveyx0.multimob.spawn;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.daveyx0.multimob.config.MMConfigSpawns;
import net.daveyx0.multimob.core.MMEntityRegistry;
import net.daveyx0.multimob.core.MultiMob;
import net.daveyx0.primitivemobs.entity.passive.EntityChameleon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event;

//Entity spawning logic
public class MMWorldSpawner
{
    private final Set<ChunkPos> chunksForSpawning = Sets.<ChunkPos>newHashSet();

    public int doCustomSpawning(WorldServer worldServerIn)
    {
        this.chunksForSpawning.clear();
        int chunkCount = 0;
        int successCount = 0;

        for (EntityPlayer entityplayer : worldServerIn.playerEntities)
        {
            if (!entityplayer.isSpectator() || MMConfigSpawns.getSpawnInSpectate())
            {
                int playerX = MathHelper.floor(entityplayer.posX / 16.0D);
                int playerY = MathHelper.floor(entityplayer.posZ / 16.0D);
                final int center = 8;

                for (int x = -center; x <= center; ++x)
                {
                    for (int z = -center; z <= center; ++z)
                    {
                        boolean border = x == -center || x == center || z == -center || z == center;
                        ChunkPos chunkpos = new ChunkPos(x + playerX, z + playerY);

                        if (!this.chunksForSpawning.contains(chunkpos))
                        {
                            ++chunkCount;

                            if (!border && worldServerIn.getWorldBorder().contains(chunkpos))
                            {
                                PlayerChunkMapEntry entry = worldServerIn.getPlayerChunkMap().getEntry(chunkpos.x, chunkpos.z);

                                if (entry != null && entry.isSentToPlayers())
                                {
                                    this.chunksForSpawning.add(chunkpos);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (MMSpawnEntry entry : MMSpawnRegistry.getSpawnEntries())
        {
        	if(entry == null || entry.getEntityClass() == null || MMEntityRegistry.entities.get(entry.getEntityClass())
        			 != null && !MMEntityRegistry.entities.get(entry.getEntityClass())){/*MultiMob.LOGGER.info("Invalid spawn entry found");*/ continue; }
        	
            int entityCount = worldServerIn.countEntities( entry.getEntityClass());
            
            int max = (int)(entry.getSpawnFrequency() * chunkCount/ (int)Math.pow(17.0D, 2.0D));

            //MultiMob.LOGGER.info(entry.getEntryName() + ": " + max + " (" + chunkCount + ")");
            if (max != 0 && entityCount <= max)
            {
                List<ChunkPos> shuffled = Lists.newArrayList(this.chunksForSpawning);
                Collections.shuffle(shuffled);

                for (ChunkPos chunk : shuffled)
                {
                    BlockPos pos = getRandomChunkPosition(worldServerIn, chunk.x, chunk.z);
                    int x = pos.getX();
                    int y = pos.getY();
                    int z = pos.getZ();
                    IBlockState state = worldServerIn.getBlockState(pos);
                    float x1 = (float)x + 0.5F;
                    float z1 = (float)z + 0.5F;

                    if (!state.isNormalCube() && !worldServerIn.isAnyPlayerWithinRangeAt((double)x1, (double)y, (double)z1, 24.0D))
                    {
                        if (MMSpawnChecks.performSpawnChecks(worldServerIn, pos, entry))
                        {
                            EntityLiving entity;

                            try
                            {
                                entity = (EntityLiving)entry.getEntityClass().getConstructor(new Class[] {World.class}).newInstance(new Object[] {worldServerIn});
                            }
                            catch (Exception exception)
                            {
                                exception.printStackTrace();
                                return successCount;
                            }

                            entity.setLocationAndAngles((double)x1, (double)y, (double)z1, worldServerIn.rand.nextFloat() * 360.0F, 0.0F);
                            Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(entity, worldServerIn, x1, (float)y, z1, null);

                            if (canSpawn == Event.Result.ALLOW || (canSpawn == Event.Result.DEFAULT && MMSpawnChecks.canEntitySpawnHere(entity, entry)))
                            {
                                entity.onInitialSpawn(worldServerIn.getDifficultyForLocation(new BlockPos(entity)), null);
                                
                                if (entity.isNotColliding())
                                {
                                	//MultiMob.LOGGER.info(entity.getName() + " spawned at " + x1 + " " + y + " " + z1); 
                                    ++successCount;
                                    worldServerIn.spawnEntity(entity);
                                }
                                else
                                {
                                    entity.setDead();
                                }
                            }
                        }
                    }
                }
            }
        }

        return successCount;
    }

    private static BlockPos getRandomChunkPosition(World worldIn, int x, int z)
    {
        Chunk chunk = worldIn.getChunkFromChunkCoords(x, z);
        int i = x * 16 + worldIn.rand.nextInt(16);
        int j = z * 16 + worldIn.rand.nextInt(16);
        int k = MathHelper.roundUp(chunk.getHeight(new BlockPos(i, 0, j)) + 1, 16);
        int l = worldIn.rand.nextInt(k > 0 ? k : chunk.getTopFilledSegment() + 16 - 1);
        return new BlockPos(i, l, j);
        
        //Cubic Chunk support ((ICubicWorld)world).findTopBlock(new BlockPos(randX, blockY + sizeY + ICube.SIZE / 2, randZ),
        //blockY, blockY + sizeY - 1, ICubicWorld.SurfaceType.SOLID);
    }
}