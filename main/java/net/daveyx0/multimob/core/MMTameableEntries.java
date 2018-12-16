package net.daveyx0.multimob.core;

import java.util.HashMap;

import net.daveyx0.multimob.common.capabilities.TameableEntityEntry;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class MMTameableEntries {

	public static final HashMap<Class<? extends Entity>, TameableEntityEntry> tameableEntries = new HashMap();
	
	public static void registerTameables()
	{
	}
	
	public static void addTameable(Class<? extends Entity> entityClass, Item[] tameItems, Item[] healItems, float newHealth, int tameChance, boolean conventionalTaming)
	{
		TameableEntityEntry entry = new TameableEntityEntry(tameItems, healItems, newHealth, tameChance, conventionalTaming);
		tameableEntries.put(entityClass, entry);
	}
}
