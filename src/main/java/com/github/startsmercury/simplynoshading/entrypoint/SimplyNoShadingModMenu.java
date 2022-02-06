package com.github.startsmercury.simplynoshading.entrypoint;

import com.github.startsmercury.simplynoshading.client.gui.screens.ShadingSettingsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * This class contains the method {@link #getModConfigScreenFactory()} which is
 * ran during modmenu initialization.
 *
 * @see ModMenuApi
 */
public class SimplyNoShadingModMenu implements ModMenuApi {
	/**
	 * Returns a factory creating {@link ShadingSettingsScreen}s which is the config
	 * screen.
	 */
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return ShadingSettingsScreen::new;
	}
}
