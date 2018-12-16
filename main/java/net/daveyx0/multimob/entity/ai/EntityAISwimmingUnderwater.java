package net.daveyx0.multimob.entity.ai;

import net.daveyx0.multimob.entity.EntityMMSwimmingCreature;
import net.daveyx0.primitivemobs.entity.monster.EntityLilyLurker;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAISwimmingUnderwater extends EntityAIBase
{
    private EntityMMSwimmingCreature swimmingEntity;
    private double xPosition;
    private double yPosition;
    private double zPosition;

    public EntityAISwimmingUnderwater(EntityMMSwimmingCreature entity)
    {
        this.swimmingEntity = entity;
        this.setMutexBits(4);
    }

    @Override
    public boolean shouldExecute()
    {
    	if(!swimmingEntity.isInWater() || swimmingEntity.isInFlowingWater())
    	{
    		return false;
    	}
        Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.swimmingEntity, 6, 2);

        if(swimmingEntity instanceof EntityLilyLurker)
        {
        	EntityLilyLurker lurker = (EntityLilyLurker)swimmingEntity;
        	if(lurker.isCamouflaged())
        	{
        		return false;
        	}
        }
        if (vec3d == null)
        {
            return false;
        }
        else
        {
            this.xPosition = vec3d.x;
            this.yPosition = vec3d.y;
            this.zPosition = vec3d.z;
            return true;
        }
    }
    
    public void resetTask()
    {
        this.xPosition = 0;
        this.yPosition = 0;
        this.zPosition = 0;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
    	if(!swimmingEntity.isInWater() || swimmingEntity.isInFlowingWater())
    	{
    		return false;
    	}
        return !this.swimmingEntity.getNavigator().noPath();
    }
    

    @Override
    public void startExecuting()
    {
    	if(xPosition != 0 && yPosition != 0 && zPosition != 0)
    	{
            this.swimmingEntity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, 1.0D);
    	}

    }
}