package net.daveyx0.multimob.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityMMFlyingCreature extends EntityCreature implements EntityFlying{


public EntityMMFlyingCreature(World worldIn) {
		super(worldIn);
		this.moveHelper = new FlyingMoveHelper();
	}

/**
 * Called when the entity is attacked.
 */
public boolean attackEntityFrom(DamageSource source, float amount)
{
    return this.isEntityInvulnerable(source) ? false : super.attackEntityFrom(source, amount);
}

public boolean attackEntityAsMob(Entity entityIn)
{
    float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
    int i = 0;

    if (entityIn instanceof EntityLivingBase)
    {
        f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
        i += EnchantmentHelper.getKnockbackModifier(this);
    }

    boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

    if (flag)
    {
        if (i > 0 && entityIn instanceof EntityLivingBase)
        {
            ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
            this.motionX *= 0.6D;
            this.motionZ *= 0.6D;
        }

        int j = EnchantmentHelper.getFireAspectModifier(this);

        if (j > 0)
        {
            entityIn.setFire(j * 4);
        }

        if (entityIn instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityIn;
            ItemStack itemstack = this.getHeldItemMainhand();
            ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

            if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() instanceof ItemShield)
            {
                float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                if (this.rand.nextFloat() < f1)
                {
                    entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                    this.getEntityWorld().setEntityState(entityplayer, (byte)30);
                }
            }
        }

        this.applyEnchantments(this, entityIn);
    }

    return flag;
}

public void onUpdate()
{
	super.onUpdate();
	this.fallDistance = 0f;
}

protected void applyEntityAttributes()
{
    super.applyEntityAttributes();
    this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
}

@Override
protected PathNavigate createNavigator(World worldIn)
{
    return new PathNavigateFlying(this, worldIn);
}

@Override
public void fall(float distance, float damageMultiplier)
{
	
}

/**
 * Returns true if this entity should push and be pushed by other entities when colliding.
 */
public boolean canBePushed()
{
    return false;
}

/**
 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
 * prevent them from trampling crops
 */
protected boolean canTriggerWalking()
{
    return false;
}
protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
{
}

/**
 * Return whether this entity should NOT trigger a pressure plate or a tripwire.
 */
public boolean doesEntityNotTriggerPressurePlate()
{
    return true;
}

public class FlyingMoveHelper extends EntityMoveHelper
{
    private EntityMMFlyingCreature flyingEntity = EntityMMFlyingCreature.this;

    public FlyingMoveHelper()
    {
        super(EntityMMFlyingCreature.this);
    }

    @Override
    public void onUpdateMoveHelper()
    {
    	
        if (this.action == EntityMoveHelper.Action.MOVE_TO)
        {
            double d0 = (double)this.posX + 0.5D - this.flyingEntity.posX;
            if(!flyingEntity.getEntityWorld().isAirBlock(new BlockPos(this.posX, this.posY - 1D, this.posZ)))
            {
            	this.posY += 1.0D;
            }
            double d1 = (double)this.posY + 1D - this.flyingEntity.posY;
            double d2 = (double)this.posZ + 0.5D - this.flyingEntity.posZ;
            double d3 = d0 * d0 + d2 * d2;
            float f = (float)(MathHelper.atan2(this.flyingEntity.motionZ, this.flyingEntity.motionX) * (180D / Math.PI)) - 90.0F;
            float f1 = MathHelper.wrapDegrees(f - this.flyingEntity.rotationYaw);

            if (d3 > 1D)
            {
                this.flyingEntity.motionX += (Math.signum(d0) * 0.25D - this.flyingEntity.motionX) * 0.10000000149011612D;
                this.flyingEntity.motionZ += (Math.signum(d2) * 0.25D - this.flyingEntity.motionZ) * 0.10000000149011612D;
                this.flyingEntity.rotationYaw += f1;
                this.flyingEntity.moveForward = 0.5F;
            }

            //this.flyingEntity.motionY += (Math.signum(d1) * 0.699999988079071D - this.flyingEntity.motionY) * 0.20000000149011612D;
            this.flyingEntity.setAIMoveSpeed((float)(this.speed * this.flyingEntity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 0.6));
            this.flyingEntity.motionY += (double)this.flyingEntity.getAIMoveSpeed() * d1 * 0.1D;
        }
        else
        {
        	this.flyingEntity.setAIMoveSpeed(0.0F);
        	this.flyingEntity.motionX = 0F;
        	this.flyingEntity.motionY = 0F;
        	this.flyingEntity.motionZ = 0F;
        }
    }
}
}
