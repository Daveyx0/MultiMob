package net.daveyx0.multimob.client.renderer.entity.layer;

import net.daveyx0.multimob.common.capabilities.CapabilityVariantEntity;
import net.daveyx0.multimob.common.capabilities.IVariantEntity;
import net.daveyx0.multimob.core.MMVariantEntries;
import net.daveyx0.multimob.core.MultiMob;
import net.daveyx0.multimob.util.EntityUtil;
import net.daveyx0.multimob.variant.MMVariantEntityEntry;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LayerVariantOverlay implements LayerRenderer {

	private final RenderLivingBase livingRenderer;
	private final ModelBase model;
	
	public LayerVariantOverlay(RenderLivingBase renderLivingBase)
	{
		livingRenderer = renderLivingBase;
		model = renderLivingBase.getMainModel();
	}
	
	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
    	if(CapabilityVariantEntity.EventHandler.hasVariant(entitylivingbaseIn))
    	{
    		IVariantEntity variant = EntityUtil.getCapability(entitylivingbaseIn, CapabilityVariantEntity.VARIANT_ENTITY_CAPABILITY, null);
    		if(variant != null && variant.getVariant() != 0)
    		{
    			MMVariantEntityEntry entry = MMVariantEntries.getVariantEntry(entitylivingbaseIn.getClass(), variant.getVariant());
    			if(entry != null)
    			{
    			GlStateManager.pushMatrix();
    			GlStateManager.depthMask(true);
    			this.livingRenderer.bindTexture(new ResourceLocation("multimob", "textures/entity/variants/" + entry.getVariantName() + ".png"));
    			this.model.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale * 1.025f);
    			GlStateManager.depthMask(false);
    			GlStateManager.popMatrix();
    			}
    		}
    	}
	}

	@Override
	public boolean shouldCombineTextures() {

		return true;
	}

}
