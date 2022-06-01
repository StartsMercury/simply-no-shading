package com.github.startsmercury.simply.no.shading.entrypoint;

import com.github.startsmercury.simply.no.shading.gui.FabricShadingSettingsScreen;
import com.github.startsmercury.simply.no.shading.gui.SimplyNoShadingFabricSettingsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.fabricmc.loader.api.FabricLoader;

/**
 * Provides support for {@link ModMenuApi modmenu}.
 *
 * In {@code fabric.mod.json}, the entrypoint is defined with {@code modmenu}
 * key.
 *
 * @since 5.0.0
 */
public class SimplyNoShadingModMenu implements ModMenuApi {
	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> FabricLoader.getInstance().isModLoaded("spruceui")
		    ? new SimplyNoShadingFabricSettingsScreen(parent)
		    : new FabricShadingSettingsScreen(parent);
	}
}
