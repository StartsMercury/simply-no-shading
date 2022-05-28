package com.github.startsmercury.simply.no.shading.util;

import static java.lang.Math.max;
import static net.fabricmc.api.EnvType.CLIENT;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;

@Environment(CLIENT)
public class SimplyNoShadingKeyManager extends Values<KeyMapping> {
	public static final String CATEGORY = "simply-no-shading.key.categories.simply-no-shading";

	public final KeyMapping openSettings;

	public final ToggleKeyMapping toggleAllShading;

	public final ToggleKeyMapping toggleBlockShading;

	public final ToggleKeyMapping toggleCloudShading;

	public final ToggleKeyMapping toggleLiquidShading;

	protected SimplyNoShadingKeyManager(final int expected, final SimplyNoShadingClientConfig<?> config) {
		super(max(expected, 5));

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

	public SimplyNoShadingKeyManager(final SimplyNoShadingClientConfig<?> config) {
		this(5, config);
	}

	public void register(final BiPredicate<? super String, ? super KeyMapping> filter,
	    final Consumer<? super KeyMapping> registry) {
		forEach((name, keyMapping) -> {
			if (filter.test(name, keyMapping)) {
				registry.accept(keyMapping);
			}
		});
	}
}
