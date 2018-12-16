package net.daveyx0.multimob.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MMParticles {
	
	@SideOnly(Side.CLIENT)
	public static Particle spawnParticle(String par1Str, World theWorld, double par2, double par4, double par6, double par8, double par10, double par12, float[] rgb)
    {
		Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null)
        {
            int i = mc.gameSettings.particleSetting;
            
            if (i == 1 && theWorld.rand.nextInt(3) == 0)
            {
                i = 2;
            }

            double d6 = mc.getRenderViewEntity().posX - par2;
            double d7 = mc.getRenderViewEntity().posY - par4;
            double d8 = mc.getRenderViewEntity().posZ - par6;
            Particle particle = null;

                double d9 = 16.0D;

                if (d6 * d6 + d7 * d7 + d8 * d8 > d9 * d9)
                {
                    return null;
                }
                if (i > 1)
                {
                    return null;
                }
                if (par1Str.equals("slime"))
                    {
                        particle = new ParticleBreakingColored(theWorld, par2, par4, par6, par8, par10, par12, Items.SNOWBALL, 0, rgb[0], rgb[1], rgb[2]);
                    }
                else if (par1Str.equals("flame"))
                {
                    particle = new ParticleLargeFlame(theWorld, par2, par4, par6, par8, par10, par12);
                }

                if (particle != null)
                    {
                        mc.effectRenderer.addEffect(particle);
                    }

                return (Particle)particle;
        }
        else
        {
            return null;
        }
    }
}