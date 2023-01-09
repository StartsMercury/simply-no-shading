package com.github.startsmercury.simply.no.shading.v6.gui;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.v6.config.FabricShadingRules;
import com.github.startsmercury.simply.no.shading.v6.config.SimplyNoShadingFabricClientConfig;

import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;

/**
 * The fabric shading settings screen covering the {@link FabricShadingRules
 * shading rules} in the {@link SimplyNoShadingFabricClientConfig config}.
 *
 * @since 5.0.0
 */
@Deprecated(forRemoval = true)
@Environment(CLIENT)
public class FabricShadingSettingsScreen extends ShadingSettingsScreen {
	/**
	 * Creates a new instance of {@code FabricShadingSettingsScreen} with the parent
	 * screen.
	 *
	 * @param parent the parent screen
	 * @since 5.0.0
	 */
	public FabricShadingSettingsScreen(final Screen parent) {
		super(parent);
	}

	/**
	 * Creates a new instance of {@code FabricShadingSettingsScreen} with the parent
	 * screen and the config.
	 *
	 * @param parent the parent screen
	 * @param config the config
	 * @since 5.0.0
	 */
	public FabricShadingSettingsScreen(final Screen parent, final SimplyNoShadingFabricClientConfig<?> config) {
		super(parent, config);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean applyOption(final String name) {
		return switch (name) {
		case "enhancedBlockEntities" -> FabricLoader.getInstance().isModLoaded("enhancedblockentities");
		default -> super.applyOption(name);
		};
	}
}
