package net.daveyx0.multimob.message;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.daveyx0.multimob.common.capabilities.CapabilityTameableEntity;
import net.daveyx0.multimob.common.capabilities.ITameableEntity;
import net.daveyx0.multimob.core.MultiMob;
import net.daveyx0.multimob.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageMMTameable implements IMessage 
{
    private String entityId;
    private String ownerId;
    private int followState;

    public MessageMMTameable() { }

    public MessageMMTameable(String entityInID, String summonerInID, int followState) {
        this.entityId = entityInID;
        this.ownerId = summonerInID;
        this.followState = followState;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    	entityId = ByteBufUtils.readUTF8String(buf);
    	ownerId = ByteBufUtils.readUTF8String(buf);
    	followState = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, entityId);
        ByteBufUtils.writeUTF8String(buf, ownerId);
        buf.writeInt(followState);
    }
    public static class Handler implements IMessageHandler<MessageMMTameable, IMessage> {
        
        @Override
        public IMessage onMessage(MessageMMTameable message, MessageContext ctx) 
        {
        	MultiMob.proxy.getThreadListener(ctx).addScheduledTask(() -> {
        		
        		if(!message.entityId.isEmpty() && !message.ownerId.isEmpty())
        		{
        			EntityLivingBase entity = EntityUtil.getLoadedEntityByUUID((UUID.fromString(message.entityId)), MultiMob.proxy.getClientWorld());
        			if(entity != null && entity.hasCapability(CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, null))
        			{
        				ITameableEntity tameable = EntityUtil.getCapability(entity, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, null);
        				tameable.setTamed(true);
        				tameable.setOwner((UUID.fromString(message.ownerId)));
        				tameable.setFollowState(message.followState);
        				NBTTagCompound nbttagcompound = entity.writeToNBT(new NBTTagCompound());
        				nbttagcompound.setString("Owner", message.ownerId);
        				nbttagcompound.setString("OwnerUUID", message.ownerId);
        				nbttagcompound.setBoolean("Tame", true);
        				nbttagcompound.setBoolean("Tamed", true);
        				entity.readFromNBT(nbttagcompound);
        			}
        		}
        	});
            return null;
        }
    }

}