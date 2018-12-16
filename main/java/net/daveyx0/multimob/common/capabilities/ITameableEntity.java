package net.daveyx0.multimob.common.capabilities;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;

/**
 * @author Daveyx0
 **/
public interface ITameableEntity
{
	
    /**
     * Returns if this is a tamed entity
     *
     *
     * @return boolean is tamed
     **/
	boolean isTamed();
	
    /**
     * Set this to a tamed entity
     *
     *
     * @param set true/false if this is a tamed entity
     **/
	void setTamed(boolean set);
	
    /**
     * Returns the owner of this tamed entity
     *
     * @param entity the entity that has been tamed (used to get entity world)
     * @return EntityLivingBase owner
     **/
	@Nullable
    EntityLivingBase getOwner(EntityLivingBase entity);
    
    /**
     * Returns the unique id of the owner
     *
     *
     * @return UUID ownerUUID
     **/
    UUID getOwnerId();
    
    /**
     * Sets the owner of this tamed entity
     *
     *
     * @param UUID the unique id of the owner
     **/
    void setOwner(UUID id);
    
    /**
     * Check if the tamed entity is following its owner
     *
     * 0 = sit, 1 = roam, 2 = follow, 3 = attack (not used by all)
     *
     *
     * @return boolean is following
     **/
    int getFollowState();
    
    /**
     * Sets if this tamed entity should follow owner
     *
     *
     * @param set true/false if this summon should follow
     **/
    void setFollowState(int set);

}