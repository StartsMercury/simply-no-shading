package com.github.startsmercury.simply.no.shading.util;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;

@Environment(CLIENT)
public abstract class SimplyNoShadingKeyManager {
	public static final String CATEGORY = "simply-no-shading.key.categories.simply-no-shading";

	public final KeyMapping openSettings;

	public final ToggleKeyMapping toggleAllShading;

	public final ToggleKeyMapping toggleBlockShading;

	public final ToggleKeyMapping toggleCloudShading;

	protected SimplyNoShadingKeyManager(final SimplyNoShadingClientConfig config) {
		this.openSettings = new KeyMapping("simply-no-shading.key.openSettings", InputConstants.UNKNOWN.getValue(),
		    CATEGORY);
		this.toggleAllShading = new ToggleKeyMapping("simply-no-shading.key.toggleAllShading", InputConstants.KEY_F6,
		    CATEGORY, config.allShading::shouldShade);
		this.toggleBlockShading = new ToggleKeyMapping("simply-no-shading.key.toggleBlockShading",
		    InputConstants.UNKNOWN.getValue(), CATEGORY, config.blockShading::shouldShade);
		this.toggleCloudShading = new ToggleKeyMapping("simply-no-shading.key.toggleCloudShading",
		    InputConstants.UNKNOWN.getValue(), CATEGORY, config.cloudShading::shouldShade);
	}

	protected abstract void register(KeyMapping key);

	public void registerAll() {
		register(this.openSettings);
		register(this.toggleAllShading);
		register(this.toggleBlockShading);
		register(this.toggleCloudShading);
	}
}
