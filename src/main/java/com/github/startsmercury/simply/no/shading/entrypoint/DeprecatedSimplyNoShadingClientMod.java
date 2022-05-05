package com.github.startsmercury.simply.no.shading.entrypoint;

import static com.github.startsmercury.simply.no.shading.SimplyNoShading.CLIENT_CONFIG;
import static com.github.startsmercury.simply.no.shading.SimplyNoShading.CLIENT_LOGGER;
import static com.github.startsmercury.simply.no.shading.SimplyNoShading.loadClientConfig;
import static net.fabricmc.api.EnvType.CLIENT;

import java.util.function.BooleanSupplier;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;

@Environment(CLIENT)
public final class DeprecatedSimplyNoShadingClientMod implements ClientModInitializer {
	private static DeprecatedSimplyNoShadingClientMod instance;

	public static final String KEY_CATEGORY = "simply-no-shading.key.categories.simply-no-shading";

	public static DeprecatedSimplyNoShadingClientMod getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Accessed SimplyNoShadingClientMod too early");
		}

		return instance;
	}

	private static KeyMapping keyMapping(final String name, final int value) {
		return new KeyMapping("simply-no-shading.key." + name, value, KEY_CATEGORY);
	}

	private static ToggleKeyMapping toggleKeyMapping(final String name, final int value, final BooleanSupplier getter) {
		return new ToggleKeyMapping("simply-no-shading.key." + name, value, KEY_CATEGORY, getter);
	}

	public final KeyMapping openShadingSettingsKey;

	public final ToggleKeyMapping toggleBlockShadingKey;

	public DeprecatedSimplyNoShadingClientMod() {
		CLIENT_LOGGER.debug("Constructing mod initializer...");

		final var unknownValue = InputConstants.UNKNOWN.getValue();

		CLIENT_LOGGER.debug("Creating key mappings...");

		this.openShadingSettingsKey = keyMapping("open_shading_settings", unknownValue);
		this.toggleBlockShadingKey = toggleKeyMapping("toggle_block_shading", unknownValue,
				CLIENT_CONFIG::shouldShadeBlocks);

		CLIENT_LOGGER.debug("Created key mappings");
		CLIENT_LOGGER.debug("Mod initializer constructed");
	}

	@Override
	public void onInitializeClient() {
		CLIENT_LOGGER.debug("Initializing mod...");

		instance = this;

		loadClientConfig();

		if (FabricLoader.getInstance().isModLoaded("fabric-key-binding-api-v1")) {
			registerKeyMappings();
		} else {
			CLIENT_LOGGER.warn(
					"Unable to register key mappings as a module of Fabric API is missing, specifically fabric-key-binding-v1");
		}

		CLIENT_LOGGER.info("Mod initialized");
	}

	private void registerKeyMappings() {
		CLIENT_LOGGER.debug("Registering key mappings...");

		KeyBindingHelper.registerKeyBinding(this.openShadingSettingsKey);
		KeyBindingHelper.registerKeyBinding(this.toggleBlockShadingKey);

		CLIENT_LOGGER.info("Registered key mappings");
	}
}
