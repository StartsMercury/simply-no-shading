package com.github.startsmercury.simply.no.shading.entrypoint;

import com.github.startsmercury.simply.no.shading.gui.FabricShadingSettingsScreen;
import com.github.startsmercury.simply.no.shading.gui.SimplyNoShadingFabricSettingsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.fabricmc.loader.api.FabricLoader;

public class SimplyNoShadingModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> FabricLoader.getInstance().isModLoaded("spruceui")
		    ? new SimplyNoShadingFabricSettingsScreen(parent)
		    : new FabricShadingSettingsScreen(parent);
	}
}
