package com.github.startsmercury.simply.no.shading.util;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingFabricClientConfig;
import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;

public class SimplyNoShadingFabricKeyManager extends SimplyNoShadingKeyManager {
	public final ToggleKeyMapping toggleEnhancedBlockEntityShading;

	public SimplyNoShadingFabricKeyManager(final SimplyNoShadingFabricClientConfig config) {
		super(config);

		this.toggleEnhancedBlockEntityShading = new ToggleKeyMapping(
		    "simply-no-shading.key.toggleEnhancedBlockEntityShading", InputConstants.UNKNOWN.getValue(), CATEGORY,
		    config.enhancedBlockEntityShading::shouldShade);
	}

	@Override
	protected void register(final KeyMapping key) {
		KeyBindingHelper.registerKeyBinding(key);
	}

	@Override
	public void registerAll() {
		register(this.openSettings);
		register(this.toggleAllShading);
		register(this.toggleBlockShading);
		register(this.toggleCloudShading);

		if (FabricLoader.getInstance().isModLoaded("enhancedblockentities")) {
			register(this.toggleEnhancedBlockEntityShading);
		}
	}
}
