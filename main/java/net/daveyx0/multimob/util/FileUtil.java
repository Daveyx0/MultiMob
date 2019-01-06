package net.daveyx0.multimob.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class FileUtil {
	
	public static void createTextFilesForModInfo(File directory) throws IOException
	{
		listResourcesForRegistry(directory, "allEntities", ForgeRegistries.ENTITIES.getKeys());
		listResourcesForRegistry(directory, "allItems", ForgeRegistries.ITEMS.getKeys());
		listResourcesForRegistry(directory, "allBlocks", ForgeRegistries.BLOCKS.getKeys());
		listResourcesForRegistry(directory, "allPotions", ForgeRegistries.POTIONS.getKeys());
		listResourcesForRegistry(directory, "allPotionTypes", ForgeRegistries.POTION_TYPES.getKeys());
		listResourcesForRegistry(directory, "allEnchantments", ForgeRegistries.ENCHANTMENTS.getKeys());
		listBiomeResources(directory, "allBiomes", "allBiomeTypes");
		listBlockStateResources(directory, "allBlockStates");
		listStructureResources(directory, "allVanillaStructures");
	}
	
	public static void listResourcesForRegistry(File directory, String fileName, Set<ResourceLocation> registry) throws IOException
	{
		List<String> arrayList = new ArrayList<String>();
		for(ResourceLocation loc : registry)
		{
			arrayList.add(loc.toString());
		}
		
		createTextFile(directory, fileName, arrayList);
	}
	
	public static void listBlockStateResources(File directory, String fileName) throws IOException
	{
		List<Block> blockList = new ArrayList<Block>();
		List<String> arrayList = new ArrayList<String>();
		for(Block loc : ForgeRegistries.BLOCKS.getValuesCollection())
		{
			for(IBlockState state: loc.getBlockState().getValidStates())
			{
				arrayList.add(state.toString());
			}
		}
		
		createTextFile(directory, fileName, arrayList);
	}
	
	public static void listBiomeResources(File directory, String fileName, String fileName2) throws IOException
	{
		List<String> arrayList = new ArrayList<String>();
		List<String> arrayListTypes = new ArrayList<String>();
		
		for(ResourceLocation loc : ForgeRegistries.BIOMES.getKeys())
		{
			arrayList.add(loc.toString());
			
			Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(ForgeRegistries.BIOMES.getValue(loc));
			if(types != null && !types.isEmpty())
			{
				for(BiomeDictionary.Type type : types)
				{
					if(!arrayListTypes.contains(type.getName()))
					{
						arrayListTypes.add(type.getName());
					}
				}
			}
		}
		
		createTextFile(directory, fileName, arrayList);
		createTextFile(directory, fileName2, arrayListTypes);
	}
	
	public static void listStructureResources(File directory, String fileName) throws IOException
	{
		List<String> arrayList = new ArrayList<String>();
		arrayList.add("Stronghold");
		arrayList.add("Mansion");
		arrayList.add("Monument");
		arrayList.add("Village");
		arrayList.add("Mineshaft");
		arrayList.add("Temple");
		arrayList.add("Fortress");
		arrayList.add("EndCity");
		
		createTextFile(directory, fileName, arrayList);
	}
	
	public static void createTextFile(File directory, String fileName, List<String> list) throws IOException
	{
		File textFile = new File(directory, fileName + ".txt");
		
		if(textFile.exists())
		{
			textFile.delete();
		}
		
		FileUtils.writeLines(textFile, list);
	}
}
