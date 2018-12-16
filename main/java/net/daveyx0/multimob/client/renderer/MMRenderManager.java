package net.daveyx0.multimob.client.renderer;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//Credit to Alex-the-666 for reflection reference
@SideOnly(Side.CLIENT)
public class MMRenderManager {
		
	private static RenderingRegistry renderingRegistry;
	
	public static void init()
	{
		Field renderingRegistryField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), new String[]{"INSTANCE", "INSTANCE"}));
		try 
		{
			Field modifier = Field.class.getDeclaredField("modifiers");
			modifier.setAccessible(true);
			renderingRegistry = (RenderingRegistry) renderingRegistryField.get(null);
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	public static void addRenderLayers(Class<LayerRenderer> layerClass)
	{
		for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : Minecraft.getMinecraft().getRenderManager().entityRenderMap.entrySet()) {
				Render render = entry.getValue();
				
				if (render instanceof RenderLivingBase && EntityLiving.class.isAssignableFrom(entry.getKey())) 
				{
					RenderLivingBase renderLivingBase = (RenderLivingBase) render;
					LayerRenderer layer = null;
					try
					{					
						layer = (LayerRenderer)layerClass.getConstructor(new Class[] {RenderLivingBase.class}).newInstance(new Object[] {render});
					}
			        catch (Exception exception)
	                {
	                    exception.printStackTrace();
	                }
					
					if(layer != null)
					{
						renderLivingBase.addLayer(layer);
					}
				}
			}
	}
	
	public static void addRenderLayersOldMethods(Class<LayerRenderer> layerClass)
	{
		Field entityRenderFactoriesField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), new String[]{"entityRenderers", "entityRenderers"}));
		Field entityRendersOldField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), new String[]{"entityRenderersOld", "entityRenderersOld"}));
		Map<Class<? extends Entity>, IRenderFactory<? extends Entity>> entityRenderFactories = null;
		Map<Class<? extends Entity>, Render<? extends Entity>> entityRendersOld = null;
		
		try 
		{
			Field modifier1 = Field.class.getDeclaredField("modifiers");
			modifier1.setAccessible(true);
			entityRenderFactories = (Map<Class<? extends Entity>, IRenderFactory<? extends Entity>>) entityRenderFactoriesField.get(renderingRegistry);
			entityRendersOld = (Map<Class<? extends Entity>, Render<? extends Entity>>) entityRendersOldField.get(renderingRegistry);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		if (entityRenderFactories != null) 
		{
			for (Map.Entry<Class<? extends Entity>, IRenderFactory<? extends Entity>> entry : entityRenderFactories.entrySet()) 
			{
				Render render = null;
				LayerRenderer layer = null;
				
				try 
				{
					render = entry.getValue().createRenderFor(Minecraft.getMinecraft().getRenderManager());
					if (render != null && render instanceof RenderLivingBase && EntityLiving.class.isAssignableFrom(entry.getKey())) 
					{
						layer = (LayerRenderer)layerClass.getConstructor(new Class[] {RenderLivingBase.class}).newInstance(new Object[] {render});
					}
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				
				if (layer != null) 
				{
					((RenderLivingBase) render).addLayer(layer);
				}
			}
		}
		
		if (entityRendersOld != null) 
		{
			for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : entityRendersOld.entrySet()) 
			{
				Render render = entry.getValue();
				
				if (render != null && render instanceof RenderLivingBase && EntityLiving.class.isAssignableFrom(entry.getKey())) 
				{
					LayerRenderer layer = null;
					
					try
					{					
						layer = (LayerRenderer)layerClass.getConstructor(new Class[] {RenderLivingBase.class}).newInstance(new Object[] {render});
					}
			        catch (Exception exception)
	                {
	                    exception.printStackTrace();
	                }
					
					if(layer != null)
					{
						((RenderLivingBase) render).addLayer(layer);
					}
				}
			}
		}
	}
	
}