package net.daveyx0.multimob.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MMItem extends Item{

		public MMItem(String itemName, CreativeTabs tabs) 
		{
			setItemName(this, itemName);
			setCreativeTab(tabs);
		}

		public static void setItemName(Item item, String itemName) 
		{
			item.setRegistryName(itemName);
			item.setUnlocalizedName(item.getRegistryName().toString());
		}
	}
