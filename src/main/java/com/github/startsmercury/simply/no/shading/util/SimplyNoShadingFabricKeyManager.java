package com.github.startsmercury.simply.no.shading.util;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingFabricClientConfig;
import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;

/**
 * The {@code SimplyNoShadingFabricKeyManager} class represents the key manager
 * for the fabric version of the mod Simply No Shading.
 *
 * @since 5.0.0
 */
@Environment(CLIENT)
public class SimplyNoShadingFabricKeyManager extends SimplyNoShadingKeyManager {
	/**
	 * Toggle enhanced block entity shading key mapping.
	 */
	public final ToggleKeyMapping toggleEnhancedBlockEntityShading;

	/**
	 * Create a key manager from a given config.
	 *
	 * @param config the config
	 */
	public SimplyNoShadingFabricKeyManager(final SimplyNoShadingFabricClientConfig<?> config) {
		super(config);

		this.toggleEnhancedBlockEntityShading = register("toggleEnhancedBlockEntityShading",
		                                                 new ToggleKeyMapping("simply-no-shading.key.toggleEnhancedBlockEntityShading",
		                                                                      InputConstants.UNKNOWN.getValue(),
		                                                                      CATEGORY,
		                                                                      config.shadingRules.enhancedBlockEntities::shouldShade));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void register(final KeyMapping keyMapping) {
		KeyBindingHelper.registerKeyBinding(keyMapping);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean shouldRegister(final String name, final KeyMapping keyMapping) {
		return switch (name) {
		case "toggleEnhancedBlockEntityShading" -> FabricLoader.getInstance().isModLoaded("enhancedblockentities");
		default -> super.shouldRegister(name, keyMapping);
		};
	}
}
