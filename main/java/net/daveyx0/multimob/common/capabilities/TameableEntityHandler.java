package net.daveyx0.multimob.common.capabilities;

import java.util.UUID;
import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import net.daveyx0.multimob.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Daveyx0
 **/
public class TameableEntityHandler implements ITameableEntity {

	protected UUID ownerID;
	protected boolean isTamed;
	protected int followState;
	
	public TameableEntityHandler()
	{
		ownerID = null;
		isTamed = false;
		followState = 0;
	}
	
	public TameableEntityHandler(UUID id)
	{
		ownerID = id;
		isTamed = true;
		followState = 0;
	}
	
	@Override
	@Nullable
	public EntityLivingBase getOwner(EntityLivingBase entityIn) {
		try
        {
            UUID uuid = this.getOwnerId();
            if(uuid != null)
            	{
            	EntityPlayer player = entityIn.world.getPlayerEntityByUUID(uuid);
            		if(player != null){return player;}else { 
            			EntityLivingBase entity = EntityUtil.getLoadedEntityByUUID(uuid, entityIn.world);
            			if(entity != null) {return entity;} else {return null;}
            		}
            	}
            else {return null;}
        }
        catch (IllegalArgumentException var2)
        {
            return null;
        }
	}
	
    public boolean isOwner(EntityLivingBase thisEntity, EntityLivingBase entityIn)
    {
        return entityIn == this.getOwner(thisEntity);
    }

	@Override
	public void setOwner(UUID id) {
		ownerID = id;
	}

	@Override
	public boolean isTamed() {

		return isTamed;
	}

	@Override
	public void setTamed(boolean set) {
		isTamed = set;
	}
	
	@Override
	public UUID getOwnerId() {

		return ownerID;
	}
	
	@Override
	public int getFollowState() {

		return followState;
	}

	@Override
	public void setFollowState(int set) {

		followState = set;
	}
	
	
	private static class Factory implements Callable<ITameableEntity> {

		  @Override
		  public ITameableEntity call() throws Exception {
		    return new TameableEntityHandler();
		  }
	}

}
