package com.github.startsmercury.simply.no.shading.util;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;

/**
 * The {@code SimplyNoShadingKeyManager} class represents the key manager of the
 * mod Simply No Shading.
 *
 * @since 5.0.0
 */
@Environment(CLIENT)
public class SimplyNoShadingKeyManager extends MultiValuedContainer<KeyMapping> {
	/**
	 * Simply No Shading key category.
	 */
	public static final String CATEGORY = "simply-no-shading.key.categories.simply-no-shading";

	/**
	 * Open settings key mapping.
	 */
	public final KeyMapping openSettings;

	/**
	 * Toggle all shading key mapping.
	 */
	public final ToggleKeyMapping toggleAllShading;

	/**
	 * Toggle block shading key mapping.
	 */
	public final ToggleKeyMapping toggleBlockShading;

	/**
	 * Toggle cloud shading key mapping.
	 */
	public final ToggleKeyMapping toggleCloudShading;

	/**
	 * Toggle liquid shading key mapping.
	 */
	public final ToggleKeyMapping toggleLiquidShading;

	/**
	 * Create a key manager from a given config.
	 *
	 * @param config the config
	 */
	protected SimplyNoShadingKeyManager(final SimplyNoShadingClientConfig<?> config) {
		this.openSettings = register("openSettings",
		        new KeyMapping("simply-no-shading.key.openSettings", InputConstants.UNKNOWN.getValue(), CATEGORY));
		this.toggleAllShading = register("toggleAllShading",
		        new ToggleKeyMapping("simply-no-shading.key.toggleAllShading", InputConstants.KEY_F6, CATEGORY,
		                config.shadingRules.all::shouldShade));
		this.toggleBlockShading = register("toggleBlockShading",
		        new ToggleKeyMapping("simply-no-shading.key.toggleBlockShading", InputConstants.UNKNOWN.getValue(),
		                CATEGORY, config.shadingRules.blocks::shouldShade));
		this.toggleCloudShading = register("toggleCloudShading",
		        new ToggleKeyMapping("simply-no-shading.key.toggleCloudShading", InputConstants.UNKNOWN.getValue(),
		                CATEGORY, config.shadingRules.clouds::shouldShade));
		this.toggleLiquidShading = register("toggleLiquidShading",
		        new ToggleKeyMapping("simply-no-shading.key.toggleLiquidShading", InputConstants.UNKNOWN.getValue(),
		                CATEGORY, config.shadingRules.liquids::shouldShade));
	}

	/**
	 * Register all key mapping.
	 *
	 * @since 5.0.0
	 */
	public void register() {
		forEach((name, keyMapping) -> {
			if (shouldRegister(name, keyMapping))
				register(keyMapping);
		});
	}

	/**
	 * Registers a key mapping.
	 *
	 * @param keyMapping the key mapping
	 */
	protected void register(final KeyMapping keyMapping) {
	}

	/**
	 * Filters which key mapping should be applied given the name and the key
	 * mapping.
	 *
	 * @param name       the name
	 * @param keyMapping the key mapping
	 * @return a {@code boolean} value
	 */
	protected boolean shouldRegister(final String name, final KeyMapping keyMapping) {
		return true;
	}
}
