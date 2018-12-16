package net.daveyx0.multimob.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.daveyx0.multimob.common.capabilities.VariantEntityEntry;
import net.minecraft.entity.Entity;

public class MMVariantEntries {

	public static final Map<Class<? extends Entity>, List<VariantEntityEntry> > variantEntries = new HashMap();
	
	public static void registerVariants()
	{
		//addVariant(EntityButterfly.class, 0, "Blue");
	}
	
	public static void addVariant(Class<? extends Entity> entityClass, int variantID, String variantName)
	{
		VariantEntityEntry entry = new VariantEntityEntry(variantID, variantName);
		List<VariantEntityEntry> variantList = new ArrayList();
		if(variantEntries.containsKey(entityClass))
		{
			variantList = variantEntries.get(entityClass);
		}
		
		variantList.add(entry);
		variantEntries.put(entityClass, variantList);
	}
	
	public VariantEntityEntry getVariantByName(String name)
	{
		for(List<VariantEntityEntry> list : variantEntries.values())
		{
			for(VariantEntityEntry entry : list)
			{
				if(entry.getVariantName().equals(name))
				{
					return entry;
				}
			}
		}

		return null;
	}
}
