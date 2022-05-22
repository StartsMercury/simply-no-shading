package com.github.startsmercury.simply.no.shading.gui;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;

import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;

@Environment(CLIENT)
public class FabricShadingSettingsScreen extends ShadingSettingsScreen {
	public FabricShadingSettingsScreen(final Screen screen) {
		super(screen);
	}

	public FabricShadingSettingsScreen(final Screen screen, final SimplyNoShadingClientConfig config) {
		super(screen, config);
	}

	@Override
	protected boolean applyOption(final String name) {
		return switch (name) {
		case "enhancedBlockEntityShading" -> FabricLoader.getInstance().isModLoaded("enhancedblockentities");
		default -> super.applyOption(name);
		};
	}
}
