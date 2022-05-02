package com.github.startsmercury.simply.no.shading.entrypoint;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.minecraft.client.gui.screens.Screen;

public class SimplyNoShadingModMenu implements ModMenuApi {
	public SimplyNoShadingModMenu() {
	}

	protected <S extends Screen> S createModConfigScreen(final Screen parent) {
		return null;
	}

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return this::createModConfigScreen;
	}
}
