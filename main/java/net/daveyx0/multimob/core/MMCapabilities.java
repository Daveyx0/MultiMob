package net.daveyx0.multimob.core;

import net.daveyx0.multimob.common.capabilities.CapabilityTameableEntity;
import net.daveyx0.multimob.common.capabilities.CapabilityVariantEntity;

public class MMCapabilities {

	public static void preInit()
	{
		CapabilityTameableEntity.register();
		CapabilityVariantEntity.register();
	}
}
