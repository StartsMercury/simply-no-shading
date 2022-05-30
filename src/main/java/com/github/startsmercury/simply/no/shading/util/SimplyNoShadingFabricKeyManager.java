package com.github.startsmercury.simply.no.shading.util;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingFabricClientConfig;
import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
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

	@Override
	protected void register(final KeyMapping keyMapping) {
		KeyBindingHelper.registerKeyBinding(keyMapping);
	}

	@Override
	protected boolean shouldRegister(final String name, final KeyMapping keyMapping) {
		return switch (name) {
		case "toggleEnhancedBlockEntityShading" -> FabricLoader.getInstance().isModLoaded("enhancedblockentities");
		default -> super.shouldRegister(name, keyMapping);
		};
	}
}
