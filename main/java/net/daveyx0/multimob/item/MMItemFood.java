package net.daveyx0.multimob.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;

public class MMItemFood extends ItemFood {

	public MMItemFood(String itemName, int amount, float saturation, boolean isWolfFood, CreativeTabs tabs) 
	{
		super(amount, saturation, isWolfFood);
		setItemName(this, itemName);
		setCreativeTab(tabs);
	}

	public static void setItemName(Item item, String itemName) {
		item.setRegistryName(itemName);
		item.setUnlocalizedName(item.getRegistryName().toString());
	}
}
