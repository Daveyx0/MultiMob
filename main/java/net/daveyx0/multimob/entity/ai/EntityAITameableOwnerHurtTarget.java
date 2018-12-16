package net.daveyx0.multimob.entity.ai;

import net.daveyx0.multimob.common.capabilities.CapabilityTameableEntity;
import net.daveyx0.multimob.common.capabilities.ITameableEntity;
import net.daveyx0.multimob.util.EntityUtil;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

public class EntityAITameableOwnerHurtTarget extends EntityAICustomTarget
{
	EntityLiving tamed;
    EntityLivingBase attacker;
    private int timestamp;

    public EntityAITameableOwnerHurtTarget(EntityLiving tameableEntity)
    {
        super(tameableEntity, false);
        this.tamed = tameableEntity;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.tamed.hasCapability(CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, null))
        {
            return false;
        }
        else
        {
        	ITameableEntity tameable = EntityUtil.getCapability(tamed, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, null);
        	EntityLivingBase entitylivingbase = tameable.getOwner(tamed);

            if (entitylivingbase == null)
            {
                return false;
            }
            else
            {
                this.attacker = entitylivingbase.getLastAttackedEntity();
                int i = entitylivingbase.getLastAttackedEntityTime();
                return i != this.timestamp && this.isSuitableTarget(this.attacker, false);
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.attacker);
        ITameableEntity tameable = EntityUtil.getCapability(tamed, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, null);
    	EntityLivingBase entitylivingbase = tameable.getOwner(tamed);

        if (entitylivingbase != null)
        {
            this.timestamp = entitylivingbase.getLastAttackedEntityTime();
        }

        super.startExecuting();
    }
}