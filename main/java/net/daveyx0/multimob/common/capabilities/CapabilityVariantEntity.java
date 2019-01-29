package net.daveyx0.multimob.common.capabilities;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import net.daveyx0.atmosmobs.renderer.entity.layer.LayerCustomEntityOnShoulder;
import net.daveyx0.multimob.capabilities.CapabilityProviderSerializable;
import net.daveyx0.multimob.client.renderer.entity.layer.LayerVariantOverlay;
import net.daveyx0.multimob.core.MMReference;
import net.daveyx0.multimob.core.MMVariantEntries;
import net.daveyx0.multimob.core.MultiMob;
import net.daveyx0.multimob.message.MMMessageRegistry;
import net.daveyx0.multimob.message.MessageMMVariant;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Daveyx0
 **/
public class CapabilityVariantEntity {
	
	    @CapabilityInject(IVariantEntity.class)
	    public static Capability<IVariantEntity> VARIANT_ENTITY_CAPABILITY = null;

	    public static final ResourceLocation capabilityID = new ResourceLocation(MMReference.MODID, "Variant");
	    
	    public static void register()
	    {
	        CapabilityManager.INSTANCE.register(IVariantEntity.class, new Capability.IStorage<IVariantEntity>()
	        {
	            @Override
	            public NBTBase writeNBT(Capability<IVariantEntity> capability, IVariantEntity instance, EnumFacing side)
	            {
	            	NBTTagCompound compound = new NBTTagCompound();
	            	compound.setInteger("VariantID", instance.getVariant());
	            	
	                return compound;
	            }

	            @Override
	            public void readNBT(Capability<IVariantEntity> capability, IVariantEntity instance, EnumFacing side, NBTBase base)
	            {
	            	NBTTagCompound compound = (NBTTagCompound)base;
	                instance.setVariant(compound.getInteger("VariantID"));

	            }
	        }, VariantEntityHandler::new);
	    }
	    
	    
	//Most stuff for the variant Entities is done through this event handler
	@Mod.EventBusSubscriber(modid = MMReference.MODID)
	public static class EventHandler
	{
		//Attach Variant Entity capability
		@SubscribeEvent
		public static void AttachEntityCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event)
		{
			if(event.getObject() != null && MMVariantEntries.variantEntries.containsKey(event.getObject().getClass()))
			{	
				event.addCapability(capabilityID, new CapabilityProviderSerializable(VARIANT_ENTITY_CAPABILITY));
			}
		}
		
		@SubscribeEvent
		public static void LivingEntityEvent(LivingUpdateEvent event)
		{
			if(hasVariant(event.getEntity()) && event.getEntity().getCapability(VARIANT_ENTITY_CAPABILITY, null).getVariant() != 0 && event.getEntity().ticksExisted % 10 == 0)
			{
				MMMessageRegistry.getNetwork().sendToAll(new MessageMMVariant(event.getEntity().getUniqueID().toString(), event.getEntity().getCapability(VARIANT_ENTITY_CAPABILITY, null).getVariant()));
				//MultiMob.LOGGER.info(event.getEntity().getCapability(VARIANT_ENTITY_CAPABILITY, null).getVariant());
			}
		}
		
		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public static void LivingLayerRenderEvent(RenderLivingEvent.Pre event)
		{
			if(hasVariant(event.getEntity()) && event.getEntity().getCapability(VARIANT_ENTITY_CAPABILITY, null).getVariant() != 0 )
			{
				if(event.getRenderer() != null)
				{
				Object objectRenderLayers = ReflectionHelper.getPrivateValue(RenderLivingBase.class, (RenderLivingBase)event.getRenderer(), ObfuscationReflectionHelper.remapFieldNames(RenderLivingBase.class.getName(), new String[]{"layerRenderers", "layerRenderers"}));
				List<LayerRenderer> layerRenderers = null;
				try 
				{
					Field modifier = Field.class.getDeclaredField("modifiers");
					modifier.setAccessible(true);
					layerRenderers = (List<LayerRenderer>) objectRenderLayers;
					
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
				
				if(layerRenderers != null)
				{
					Optional<LayerRenderer> layerEntity = layerRenderers.stream().filter(layerRenderer -> layerRenderer instanceof LayerVariantOverlay).findFirst();
					if(layerEntity == null || !layerEntity.isPresent())
					{
						event.getRenderer().addLayer(new LayerVariantOverlay(event.getRenderer()));
					}
					
				}
				}
				
				//GlStateManager.enableBlend();
				//GlStateManager.blendFunc(SourceFactor.ONE, DestFactor.ONE);
				GlStateManager.depthMask(false);
			}
			
		}
		
		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public static void LivingLayerRenderEvent(RenderLivingEvent.Post event)
		{
			if(hasVariant(event.getEntity())  && event.getEntity().getCapability(VARIANT_ENTITY_CAPABILITY, null).getVariant() != 0 )
			{
				GlStateManager.depthMask(true);
				//GlStateManager.disableBlend();
			}
		}

		
		public static boolean hasVariant(Entity entity)
		{
			return entity != null && entity instanceof EntityLiving && entity.hasCapability(VARIANT_ENTITY_CAPABILITY, null);
		}
	}
	

}
