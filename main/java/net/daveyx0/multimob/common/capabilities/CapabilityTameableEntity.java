package net.daveyx0.multimob.common.capabilities;

import java.util.UUID;

import net.daveyx0.multimob.capabilities.CapabilityProviderSerializable;
import net.daveyx0.multimob.core.MMReference;
import net.daveyx0.multimob.core.MMTameableEntries;
import net.daveyx0.multimob.entity.ai.EntityAITameableFollowOwner;
import net.daveyx0.multimob.entity.ai.EntityAITameableOwnerHurtByTarget;
import net.daveyx0.multimob.entity.ai.EntityAITameableOwnerHurtTarget;
import net.daveyx0.multimob.message.MMMessageRegistry;
import net.daveyx0.multimob.message.MessageMMParticle;
import net.daveyx0.multimob.message.MessageMMTameable;
import net.daveyx0.multimob.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.AllowDespawn;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

/**
 * @author Daveyx0
 **/
public class CapabilityTameableEntity {
	
	    @CapabilityInject(ITameableEntity.class)
	    public static Capability<ITameableEntity> TAMEABLE_ENTITY_CAPABILITY = null;

	    public static final ResourceLocation capabilityID = new ResourceLocation(MMReference.MODID, "Tameable");
	    
	    public static void register()
	    {
	        CapabilityManager.INSTANCE.register(ITameableEntity.class, new Capability.IStorage<ITameableEntity>()
	        {
	            @Override
	            public NBTBase writeNBT(Capability<ITameableEntity> capability, ITameableEntity instance, EnumFacing side)
	            {
	            	NBTTagCompound compound = new NBTTagCompound();
	            	UUID owner = instance.getOwnerId();
		            	
	            	if (owner == null)
	                {
	                    compound.setString("OwnerUUID", "");
	                }
	                else
	                {
	                	compound.setString("OwnerUUID", owner.toString());
	                }
	            	
	            	compound.setInteger("FollowState", instance.getFollowState());
	            	
	                return compound;
	            }

	            @Override
	            public void readNBT(Capability<ITameableEntity> capability, ITameableEntity instance, EnumFacing side, NBTBase base)
	            {
	            	NBTTagCompound compound = (NBTTagCompound)base;
	                String s = "";

	                if (compound.hasKey("OwnerUUID", 8))
	                {
	                    s = compound.getString("OwnerUUID");
	                }

	                if (!s.isEmpty())
	                {
	                    try
	                    {
	                        instance.setOwner((UUID.fromString(s)));
	                        instance.setTamed(true);
	                    }
	                    catch (Throwable var4)
	                    {
	                    	instance.setTamed(false);
	                    }
	                }
	                
	                instance.setFollowState(compound.getInteger("FollowState"));

	            }
	        }, TameableEntityHandler::new);
	    }
	    
	    
	//Most stuff for the tameable Entities is done through this event handler
	@Mod.EventBusSubscriber(modid = MMReference.MODID)
	public static class EventHandler
	{
		//Attach tameable Entity capability
		@SubscribeEvent
		public static void AttachEntityCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event)
		{
			if(event.getObject() != null && MMTameableEntries.tameableEntries.containsKey(event.getObject().getClass()))
			{	
				event.addCapability(capabilityID, new CapabilityProviderSerializable(TAMEABLE_ENTITY_CAPABILITY));
			}
		}
		
		//Play death message
		@SubscribeEvent
		public static void EntityLivingDeathEvent(LivingDeathEvent event)
		{
			if(isTameableEntity(event.getEntityLiving()))
			{
				ITameableEntity tameable = EntityUtil.getCapability(event.getEntity(), TAMEABLE_ENTITY_CAPABILITY, null);
				EntityLiving entity = (EntityLiving)event.getEntityLiving();
		        if (!entity.world.isRemote && entity.world.getGameRules().getBoolean("showDeathMessages") && tameable.getOwner(entity) != null && tameable.getOwner(entity) instanceof EntityPlayerMP)
		        {
		        	tameable.getOwner(entity).sendMessage(entity.getCombatTracker().getDeathMessage());
		        }
			}
		}
		
		//Update the tameable entity AI once the entity joins the world
		@SubscribeEvent
		public static void JoinWorldEvent(EntityJoinWorldEvent event)
		{
			if(isTameableEntity(event.getEntity()))
			{
				EntityLiving entity = (EntityLiving)event.getEntity();
				ITameableEntity tameable = EntityUtil.getCapability(event.getEntity(), TAMEABLE_ENTITY_CAPABILITY, null);
				if(tameable != null && tameable.isTamed())
				{
					if(tameable.getFollowState() == 0 || tameable.getFollowState() == 0)
					{
						resetEntityTargetAI(entity);
					}
					else
					{
						updateEntityTargetAI(entity);
					}
					
					entity.tasks.taskEntries.stream().filter(taskEntry -> taskEntry.action instanceof EntityAITameableFollowOwner)
					.findFirst().ifPresent(taskEntry -> entity.tasks.removeTask(taskEntry.action));
					
					if(tameable.getFollowState() == 2)
					{
						entity.tasks.addTask(3, new EntityAITameableFollowOwner(entity, 1.2D, 8.0f, 2.0f));
					}
					
					if(MMTameableEntries.tameableEntries.containsKey(entity))
					{
						TameableEntityEntry entry = MMTameableEntries.tameableEntries.get(entity);
						entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(entry.getTamedHealth());		
					}

				}
			}
		}
		
		//When a tamed entity does not follow, constantly clear the path entity, which is essentially what the sit AI does
		@SubscribeEvent
		public static void EntityUpdateEvent(LivingUpdateEvent event)
		{
			if(isTameableEntity(event.getEntityLiving()))
			{
				EntityLiving entity = (EntityLiving)event.getEntity();
				ITameableEntity tameable = EntityUtil.getCapability(event.getEntity(), TAMEABLE_ENTITY_CAPABILITY, null);
				if(tameable != null && tameable.isTamed())
				{
					if(tameable.getFollowState() == 0)
					{
						entity.getNavigator().clearPath();
						entity.setAIMoveSpeed(0);
						entity.setAttackTarget(null);
					}
					
		            if (entity.world.rand.nextInt(200) == 0)
		            {
		            	entity.getEntityWorld().spawnParticle(EnumParticleTypes.HEART, entity.posX + (entity.world.rand.nextFloat() - entity.world.rand.nextFloat()), entity.posY + entity.world.rand.nextFloat() + 1D, entity.posZ + (entity.world.rand.nextFloat() - entity.world.rand.nextFloat()), 1, 1, 1);
		            }
				}
			}
		}
		
		@SubscribeEvent
		public static void EntityDamageEvent(LivingDamageEvent event)
		{
			if(isTameableEntity(event.getEntityLiving()))
			{
				EntityLiving entity = (EntityLiving)event.getEntity();
				ITameableEntity tameable = EntityUtil.getCapability(event.getEntity(), TAMEABLE_ENTITY_CAPABILITY, null);
				
				if(tameable != null && tameable.isTamed())
				{
					if(event.getSource() == DamageSource.FALL || event.getSource() == DamageSource.DROWN || event.getSource() == DamageSource.IN_WALL)
					{
						event.setResult(Result.DENY);
					}
				}
			}
		}
		
		//Make sure the tamed entity gets updated on client when a new player starts tracking it
		@SubscribeEvent
		public static void PlayerStartsTrackingEvent(StartTracking event)
		{
			if(!event.getEntityPlayer().getEntityWorld().isRemote && 
					isTameableEntity(event.getTarget()))
			{
				ITameableEntity tameable = EntityUtil.getCapability(event.getTarget(), TAMEABLE_ENTITY_CAPABILITY, null);
				if(tameable.getOwnerId() != null)
				{
					MMMessageRegistry.getNetwork().sendToAllAround(new MessageMMTameable(event.getTarget().getUniqueID().toString(), tameable.getOwnerId().toString(), tameable.getFollowState()), 
						new TargetPoint(event.getEntityPlayer().dimension, event.getTarget().posX, event.getTarget().posY, event.getTarget().posZ, 255D));
				}
			}
		}
		
		//Tameable entity interaction; sit, heal and tame
		@SubscribeEvent
		public static void PlayerInteractEvent(EntityInteract event)
		{
			if(isTameableEntity(event.getTarget()) && event.getHand() == EnumHand.MAIN_HAND)
			{

				EntityLiving entity = (EntityLiving)event.getTarget();
				ITameableEntity tameable = EntityUtil.getCapability(event.getTarget(), TAMEABLE_ENTITY_CAPABILITY, null);
				if(tameable != null)
				{
				if(tameable.isTamed() && tameable.getOwner(entity) == event.getEntityPlayer())
				{
					if(event.getItemStack() == ItemStack.EMPTY)
					{
					if(event.getEntityPlayer().isSneaking() )
					{

						tameable.setFollowState(tameable.getFollowState() + 1);
						if(tameable.getFollowState() == 3)
						{
							tameable.setFollowState(0);
						}
						MMMessageRegistry.getNetwork().sendToAllAround(new MessageMMTameable(event.getTarget().getUniqueID().toString(), tameable.getOwnerId().toString(), tameable.getFollowState()), 
						new TargetPoint(event.getEntityPlayer().dimension, event.getTarget().posX, event.getTarget().posY, event.getTarget().posZ, 255D));
						
						if(tameable.getFollowState() == 2)
						{
							entity.tasks.addTask(3, new EntityAITameableFollowOwner(entity, 1.2D, 8.0f, 2.0f));
						}
						else
						{
							entity.tasks.taskEntries.stream().filter(taskEntry -> taskEntry.action instanceof EntityAITameableFollowOwner)
							.findFirst().ifPresent(taskEntry -> entity.tasks.removeTask(taskEntry.action));
						}
						
						if(tameable.getFollowState() == 0)
						{
							resetEntityTargetAI(entity);
						}
						else
						{
							updateEntityTargetAI(entity);
						}
						//MultiMob.LOGGER.info(tameable.getFollowState());
						
						if(!event.getEntity().getEntityWorld().isRemote)
						{
							int particleID = 0;
							
							if(tameable.getFollowState() == 0) {particleID = EnumParticleTypes.VILLAGER_HAPPY.getParticleID(); event.getEntityPlayer().sendMessage(new TextComponentTranslation("%1$s is now sitting.",new Object[] {event.getTarget().getDisplayName()}));}
							else if(tameable.getFollowState() == 1) {particleID = EnumParticleTypes.NOTE.getParticleID(); event.getEntityPlayer().sendMessage(new TextComponentTranslation("%1$s is now wandering.",new Object[] {event.getTarget().getDisplayName()}));}
							else if(tameable.getFollowState() == 2) {particleID = EnumParticleTypes.HEART.getParticleID(); event.getEntityPlayer().sendMessage(new TextComponentTranslation("%1$s is now following.",new Object[] {event.getTarget().getDisplayName()}));}
							
							MMMessageRegistry.getNetwork().sendToAll(new MessageMMParticle(particleID, 15, (float)entity.posX + 0.5f, (float)entity.posY + 0.5F, (float)entity.posZ + 0.5f, 0D, 0.0D,0.0D, 0));
						}
					}
					else if(entity.canBeSteered())
					{
						if(!event.getEntity().getEntityWorld().isRemote)
						{
							event.getEntityPlayer().startRiding(entity);
						}
					}
					}
					else
					{
						if(MMTameableEntries.tameableEntries.containsKey(entity.getClass()))
						{
							TameableEntityEntry entry = MMTameableEntries.tameableEntries.get(entity.getClass());
							
							if(entry.getHealItems() != null && entry.getHealItems().length > 0)
							{
								for(Item item : entry.getHealItems())
								{
									if(event.getItemStack().getItem() == item)
									{
					                   if (!event.getEntityPlayer().capabilities.isCreativeMode)
					                   {
					                	   event.getItemStack().shrink(1);
					                   }
					                   
										entity.heal(10f);
										playHealEffect(entity);
										break;
									}
								}
							}

						}
					}
				}else if(!tameable.isTamed())
				{
					if(event.getItemStack() != null && MMTameableEntries.tameableEntries.containsKey(entity.getClass()))
					{
						TameableEntityEntry entry = MMTameableEntries.tameableEntries.get(entity.getClass());
						//MultiMob.LOGGER.info(isTameableEntity("got here"));
						if(entry.getTameItems() != null && entry.getCanBeTamedWithItem() &&  entry.getTameItems().length > 0)
						{
							for(Item item : entry.getTameItems())
							{
								if(event.getItemStack().getItem() == item)
								{
				                   if (!event.getEntityPlayer().capabilities.isCreativeMode)
				                   {
				                	   event.getItemStack().shrink(1);
				                   }
				                   setUpTameable(tameable, entity, event.getEntityPlayer());

									break;
								}
							}
						}

					}
				}
				}
			}
		}
		
		public static void setUpTameable(ITameableEntity tameable, EntityLiving entity, EntityLivingBase owner)
		{
			tameable.setOwner(owner.getUniqueID());
			tameable.setTamed(true);
			tameable.setFollowState(2);
			MMMessageRegistry.getNetwork().sendToAllAround(new MessageMMTameable(entity.getUniqueID().toString(), tameable.getOwnerId().toString(), tameable.getFollowState()), 
					new TargetPoint(owner.dimension, entity.posX, entity.posY, entity.posZ, 255D));
			CapabilityTameableEntity.EventHandler.updateEntityTargetAI(entity);
			entity.tasks.addTask(3, new EntityAITameableFollowOwner(entity, 1.2D, 8.0f, 2.0f));
			playHealEffect(entity);
		}
	
		
		@SubscribeEvent
		public static void EntityDespawnEvent(AllowDespawn event)
		{
			if(isTameableEntity(event.getEntity()))
			{
				ITameableEntity tameable = EntityUtil.getCapability(event.getEntity(), TAMEABLE_ENTITY_CAPABILITY, null);
				if(tameable != null && tameable.isTamed())
				{
					event.setResult(Result.DENY);
				}
			}
		}
		
		public static void updateEntityTargetAI(EntityLiving base)
		{
			while(base.targetTasks.taskEntries.stream()
			.filter(taskEntry -> taskEntry.action instanceof EntityAIBase).findFirst().isPresent())
			{
				base.targetTasks.taskEntries.stream().filter(taskEntry -> taskEntry.action instanceof EntityAIBase)
				.findFirst().ifPresent(taskEntry -> base.targetTasks.removeTask(taskEntry.action));
			}
			
			base.targetTasks.addTask(0, new EntityAITameableOwnerHurtByTarget(base));
			base.targetTasks.addTask(1, new EntityAITameableOwnerHurtTarget(base));
		}
		
		public static void resetEntityTargetAI(EntityLiving base)
		{
			while(base.targetTasks.taskEntries.stream()
			.filter(taskEntry -> taskEntry.action instanceof EntityAIBase).findFirst().isPresent())
			{
				base.targetTasks.taskEntries.stream().filter(taskEntry -> taskEntry.action instanceof EntityAIBase)
				.findFirst().ifPresent(taskEntry -> base.targetTasks.removeTask(taskEntry.action));
			}
		}
		
		public static boolean isTameableEntity(Entity entity)
		{
			return entity != null && entity instanceof EntityLiving && entity.hasCapability(TAMEABLE_ENTITY_CAPABILITY, null);
		}
		
	    /**
	     * Play the taming effect, will either be hearts or smoke depending on status
	     */
	    protected static void playHealEffect(Entity entity)
	    {
	        EnumParticleTypes enumparticletypes = EnumParticleTypes.HEART;
	        for (int i = 0; i < 7; ++i)
	        {
	            double d0 = entity.world.rand.nextGaussian() * 0.02D;
	            double d1 = entity.world.rand.nextGaussian() * 0.02D;
	            double d2 = entity.world.rand.nextGaussian() * 0.02D;
	            entity.getEntityWorld().spawnParticle(enumparticletypes, entity.posX + (double)(entity.world.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width, entity.posY + 0.5D + (double)(entity.world.rand.nextFloat() * entity.height), entity.posZ + (double)(entity.world.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width, d0, d1, d2, new int[0]);
	        }
	    }
	    
	}




}
