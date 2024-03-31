package com.github.startsmercury.simply.no.shading.entrypoint;

import com.github.startsmercury.simply.no.shading.client.gui.screens.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * The {@code SimplyNoShadingModMenuEntrypoint} class is an implementation of
 * {@link ModMenuApi} and is an entrypoint defined with the {@code modmenu} key
 * in the {@code fabric.mod.json}. This acts as compatibility between Simply No
 * Shading and ModMenu.
 */
@Deprecated(since = "6.2.0", forRemoval = true)
public class SimplyNoShadingModMenuEntrypoint implements ModMenuApi {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return ConfigScreen::new;
	}
}
