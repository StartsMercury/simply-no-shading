package com.github.startsmercury.simplynoshading.entrypoint;

import com.github.startsmercury.simplynoshading.client.gui.screens.ShadingSettingsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class SimplyNoShadingModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return ShadingSettingsScreen::new;
	}
}
