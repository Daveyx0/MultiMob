package net.daveyx0.multimob.message;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.daveyx0.multimob.common.capabilities.CapabilityTameableEntity;
import net.daveyx0.multimob.common.capabilities.CapabilityVariantEntity;
import net.daveyx0.multimob.common.capabilities.ITameableEntity;
import net.daveyx0.multimob.common.capabilities.IVariantEntity;
import net.daveyx0.multimob.core.MultiMob;
import net.daveyx0.multimob.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageMMVariant implements IMessage 
{
    private String entityId;
    private int variantId;

    public MessageMMVariant() { }

    public MessageMMVariant(String entityInID, int variantId) {
        this.entityId = entityInID;
        this.variantId = variantId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    	entityId = ByteBufUtils.readUTF8String(buf);
    	variantId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, entityId);
        buf.writeInt(variantId);
    }
    public static class Handler implements IMessageHandler<MessageMMVariant, IMessage> {
        
        @Override
        public IMessage onMessage(MessageMMVariant message, MessageContext ctx) 
        {
        	MultiMob.proxy.getThreadListener(ctx).addScheduledTask(() -> {
        		
        		if(!message.entityId.isEmpty())
        		{
        			EntityLivingBase entity = EntityUtil.getLoadedEntityByUUID((UUID.fromString(message.entityId)), MultiMob.proxy.getClientWorld());
        			if(entity != null && entity.hasCapability(CapabilityVariantEntity.VARIANT_ENTITY_CAPABILITY, null))
        			{
        				IVariantEntity variant = EntityUtil.getCapability(entity, CapabilityVariantEntity.VARIANT_ENTITY_CAPABILITY, null);

        				variant.setVariant(message.variantId);
        			}
        		}
        	});
            return null;
        }
    }

}