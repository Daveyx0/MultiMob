package net.daveyx0.multimob.config;

import java.util.List;
import java.util.stream.Collectors;

import net.daveyx0.multimob.core.MMReference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class MMGuiConfig extends GuiConfig {

	public MMGuiConfig(GuiScreen parentScreen) 
	{
		super(parentScreen, getConfigElements(), MMReference.MODID, false, false, "multimob.config.title");
	}

	private static List<IConfigElement> getConfigElements() 
	{
		return MMConfig.config.getCategoryNames().stream()
				.map(categoryName -> new ConfigElement(MMConfig.config.getCategory(categoryName).setLanguageKey("multimob.config." + categoryName)))
				.collect(Collectors.toList());
	}
}
