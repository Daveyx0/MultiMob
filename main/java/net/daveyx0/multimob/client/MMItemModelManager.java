package net.daveyx0.multimob.client;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import net.daveyx0.multimob.core.MMItemRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//Credit to Choonster for reference
//Class that registers item models and colors
@SideOnly(Side.CLIENT)
public class MMItemModelManager
{
	public static final MMItemModelManager INSTANCE = new MMItemModelManager();

	public static final Set<Item> ITEMS = new HashSet<>();
	 
	private MMItemModelManager() {
	}
	
	@SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
		
		for(Item item : MMItemRegistry.ITEMS)
		{
			ITEMS.add(item);
		}
		registerItemModels();
	}

	private final Set<Item> itemsRegistered = new HashSet<>();

	private void registerItemModels() {
		
		ITEMS.stream().filter(item -> !itemsRegistered.contains(item)).forEach(this::registerItemModel);
	}
	
	public void registerItemColors(Item[] items) {
		
		registerItemColor(new IItemColor()
        {
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex)
            {
                return tintIndex > 0 ? -1 : getColor(stack);
            }
        }, items);
	}
	
	/**
	 * A {@link StateMapperBase} used to create property strings.
	 */
	private final StateMapperBase propertyStringMapper = new StateMapperBase() {
		@Override
		protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
			return new ModelResourceLocation("minecraft:air");
		}
	};
	
	private void registerItemColor(IItemColor itemcolor, Item... itemsIn)
	{
		FMLClientHandler.instance().getClient().getItemColors().registerItemColorHandler(itemcolor, itemsIn);
	}
	
	private void registerItemModel(final Item item) {
		final ResourceLocation registryName = Objects.requireNonNull(item.getRegistryName());
		registerItemModel(item, registryName.toString());
	}

	private void registerItemModel(final Item item, final String modelLocation) {
		final ModelResourceLocation fullModelLocation = new ModelResourceLocation(modelLocation, "inventory");
		registerItemModel(item, fullModelLocation);
	}

	private void registerItemModel(final Item item, final ModelResourceLocation fullModelLocation) {
		ModelBakery.registerItemVariants(item, fullModelLocation); // Ensure the custom model is loaded and prevent the default model from being loaded
		registerItemModel(item, stack -> fullModelLocation);
	}

	private void registerItemModel(final Item item, final ItemMeshDefinition meshDefinition) {
		itemsRegistered.add(item);
		ModelLoader.setCustomMeshDefinition(item, meshDefinition);
	}
	
    /**
     * Return the color for the specified ItemStack.
     */ 
    public static int getColor(ItemStack stack)
    {
            NBTTagCompound nbttagcompound = stack.getTagCompound();

            if (nbttagcompound != null)
            {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

                if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3))
                {
                    return nbttagcompound1.getInteger("color");
                }
            }

            return 16777215;
    }
}