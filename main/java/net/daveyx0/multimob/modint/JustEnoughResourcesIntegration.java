package net.daveyx0.multimob.modint;

import jeresources.api.IJERAPI;
import jeresources.api.JERPlugin;
import net.minecraft.world.World;

public class JustEnoughResourcesIntegration implements IModIntegration {

	@JERPlugin
	public static IJERAPI jerAPI;
	public static World world;

	@Override
	public void init() 
	{
		world = jerAPI.getWorld();
	}
}