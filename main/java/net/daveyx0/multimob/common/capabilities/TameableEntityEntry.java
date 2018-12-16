package net.daveyx0.multimob.common.capabilities;

import net.minecraft.item.Item;

public class TameableEntityEntry
{
	Item[] itemsUsedToTame = null;
	Item[] itemsUsedToHeal = null;
	float tamedNewHealth = 20f;
	int chanceToTame = 100;
	boolean canBeTamedWithItem = true;
	
	public TameableEntityEntry(Item[] tameItems, Item[] healItems, float newHealth, int tameChance, boolean conventionalTame)
	{
		itemsUsedToTame = tameItems;
		itemsUsedToHeal = healItems;
		tamedNewHealth = newHealth;
		chanceToTame = tameChance;
		canBeTamedWithItem = conventionalTame;
	}
	
	public Item[] getTameItems()
	{
		return itemsUsedToTame;
	}
	
	public Item[] getHealItems()
	{
		return itemsUsedToHeal;
	}
	
	public float getTamedHealth()
	{
		return tamedNewHealth;
	}
	
	public int getTameChance()
	{
		return chanceToTame;
	}
	
	public boolean getCanBeTamedWithItem()
	{
		return canBeTamedWithItem;
	}
}
