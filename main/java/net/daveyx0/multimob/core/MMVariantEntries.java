package net.daveyx0.multimob.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.daveyx0.atmosmobs.entity.passive.EntityButterfly;
import net.daveyx0.multimob.variant.MMVariantEntityEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;

public class MMVariantEntries {

	public static final Map<Class<? extends Entity>, List<MMVariantEntityEntry> > variantEntries = new HashMap();
	
	public static void registerVariants()
	{
		//addVariant(EntityZombie.class, 1, "zombie_grey");
	}
	
	public static void addVariant(Class<? extends Entity> entityClass, int variantID, String variantName)
	{
		MMVariantEntityEntry entry = new MMVariantEntityEntry(variantID, variantName);
		List<MMVariantEntityEntry> variantList = new ArrayList();
		if(variantEntries.containsKey(entityClass))
		{
			variantList = variantEntries.get(entityClass);
		}
		
		variantList.add(entry);
		variantEntries.put(entityClass, variantList);
	}
	
	public static MMVariantEntityEntry getVariantEntry(Class<? extends Entity> entityClass, int id)
	{
		 List<MMVariantEntityEntry> variants = variantEntries.get(entityClass);
		 
		 if(variants != null && !variants.isEmpty())
		 {
			 for(MMVariantEntityEntry variant: variants)
			 {
				 if(variant.getVariantIndex() == id)
				 {
					 return variant;
				 }
			 }
		 }

		return null;
	}
}
