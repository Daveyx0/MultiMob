package net.daveyx0.multimob.entity.ai;

import net.daveyx0.multimob.entity.EntityMMFlyingCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAIFlyingAround extends EntityAIBase
{
    private EntityMMFlyingCreature flyingEntity;
    private double xPosition;
    private double yPosition;
    private double zPosition;

    public EntityAIFlyingAround(EntityMMFlyingCreature entity)
    {
        this.flyingEntity = entity;
        this.setMutexBits(4);
    }

    @Override
    public boolean shouldExecute()
    {
    	if(flyingEntity.isInWater())
    	{
    		return false;
    	}
    	
        Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.flyingEntity, 6, 4);

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
        return !this.flyingEntity.getNavigator().noPath();
    }
    

    @Override
    public void startExecuting()
    {
    	if(xPosition != 0 && yPosition != 0 && zPosition != 0)
    	{
            this.flyingEntity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, 1.0D);
    	}

    }
}