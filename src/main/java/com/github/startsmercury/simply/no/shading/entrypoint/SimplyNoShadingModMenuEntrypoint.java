package com.github.startsmercury.simply.no.shading.entrypoint;

import java.util.function.Function;

import com.github.startsmercury.simply.no.shading.client.gui.screens.ConfigScreen;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

/**
 * The {@code SimplyNoShadingModMenuEntrypoint} class is an implementation of
 * {@link ModMenuApi} and is an entrypoint defined with the {@code modmenu} key
 * in the {@code fabric.mod.json}. This acts as compatibility between Simply No
 * Shading and ModMenu.
 */
public class SimplyNoShadingModMenuEntrypoint implements ModMenuApi {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Function<Screen, ? extends Screen> getConfigScreenFactory() {
		return ConfigScreen::new;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getModId() {
		return "simply-no-shading";
	}
}
