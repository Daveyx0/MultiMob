package net.daveyx0.multimob.common.capabilities;

import net.daveyx0.multimob.capabilities.CapabilityProviderSerializable;
import net.daveyx0.multimob.core.MMReference;
import net.daveyx0.multimob.core.MMVariantEntries;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
	}

}
