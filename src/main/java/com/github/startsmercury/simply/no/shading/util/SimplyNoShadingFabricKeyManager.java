package com.github.startsmercury.simply.no.shading.util;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingFabricClientConfig;
import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.Environment;
import net.minecraft.client.ToggleKeyMapping;

@Environment(CLIENT)
public class SimplyNoShadingFabricKeyManager extends SimplyNoShadingKeyManager {
	public final ToggleKeyMapping toggleEnhancedBlockEntityShading;

	public SimplyNoShadingFabricKeyManager(final SimplyNoShadingFabricClientConfig<?> config) {
		super(config);

		this.toggleEnhancedBlockEntityShading = register("toggleEnhancedBlockEntityShading",
		    new ToggleKeyMapping("simply-no-shading.key.toggleEnhancedBlockEntityShading",
		        InputConstants.UNKNOWN.getValue(), CATEGORY, config.shadingRules.enhancedBlockEntities::shouldShade));
	}
}
