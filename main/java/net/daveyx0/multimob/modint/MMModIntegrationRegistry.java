package net.daveyx0.multimob.modint;

import net.daveyx0.multimob.core.MultiMob;
import net.minecraftforge.fml.common.Loader;

public class MMModIntegrationRegistry {

	public static IModIntegration registerModIntegration(IModIntegration integration)
	{
		if (Loader.isModLoaded("jeresources") && integration instanceof JustEnoughResourcesIntegration) 
		{
			integration.init();
		}
		
		if (Loader.isModLoaded("dynamictrees") && integration instanceof DynamicTreesIntegration) 
		{
			integration.init();
		}
		
		return integration;
	}
}
