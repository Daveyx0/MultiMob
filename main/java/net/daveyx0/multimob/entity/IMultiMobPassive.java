package net.daveyx0.multimob.entity;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;

public interface IMultiMobPassive extends IAnimals {
	
	    /** Entity selector for IMob types. */
	    Predicate<Entity>MULTIMOB_SELECTOR = new Predicate<Entity>()
	    {
	        public boolean apply(@Nullable Entity p_apply_1_)
	        {
	            return p_apply_1_ instanceof IMultiMobPassive;
	        }
	    };
}
