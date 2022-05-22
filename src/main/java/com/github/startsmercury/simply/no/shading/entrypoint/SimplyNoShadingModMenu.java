package com.github.startsmercury.simply.no.shading.entrypoint;

import com.github.startsmercury.simply.no.shading.gui.FabricShadingSettingsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class SimplyNoShadingModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return FabricShadingSettingsScreen::new;
	}
}
