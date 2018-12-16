package net.daveyx0.multimob.client.particle;

import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.world.World;

public class ParticleLargeFlame extends ParticleFlame {

	public ParticleLargeFlame(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		this.particleScale = 3f;
	}

}
