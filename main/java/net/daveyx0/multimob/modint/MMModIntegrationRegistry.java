package net.daveyx0.multimob.modint;

import net.minecraftforge.fml.common.Loader;

public class MMModIntegrationRegistry {

	public static void registerModIntegration(IModIntegration integration)
	{
		if (Loader.isModLoaded("jeresources") && integration instanceof JustEnoughResourcesIntegration) 
		{
			integration.init();
		}
	}
}
