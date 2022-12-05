package com.github.startsmercury.simply.no.shading.entrypoint;

import com.github.startsmercury.simply.no.shading.gui.SimplyNoShadingFabricSettingsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Provides support for {@link ModMenuApi modmenu}. In {@code fabric.mod.json},
 * the entrypoint is defined with the {@code modmenu} key.
 *
 * @since 5.0.0
 */
public class SimplyNoShadingModMenu implements ModMenuApi {
	/**
	 * Creates a new SimplyNoShadingModMenu.
	 */
	public SimplyNoShadingModMenu() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return SimplyNoShadingFabricSettingsScreen::new;
	}
}
