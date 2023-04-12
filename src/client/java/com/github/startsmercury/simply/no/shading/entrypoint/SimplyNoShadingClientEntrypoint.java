package com.github.startsmercury.simply.no.shading.entrypoint;

import static net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;
import com.github.startsmercury.simply.no.shading.client.gui.screens.ConfigScreen;
import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;

/**
 * The {@code SimplyNoShadingClientEntrypoint} class is an implementation of
 * {@link ClientModInitializer} and is an entrypoint defined with the
 * {@code client} key in the {@code fabric.mod.json}. This allows Simply No
 * Shading to be initialized and configured for the minecraft client.
 */
public class SimplyNoShadingClientEntrypoint implements ClientModInitializer {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onInitializeClient() {
		final var simplyNoShading = new SimplyNoShading();

		simplyNoShading.loadConfig();

		setupKeyMappings(simplyNoShading);
		setupShutdownHook(simplyNoShading::saveConfig);
	}

	/**
	 * Registers key mappings and key event listeners.
	 *
	 * @param simplyNoShading the simply no shading instance
	 */
	protected void setupKeyMappings(final SimplyNoShading simplyNoShading) {
		final var openConfigScreen = new KeyMapping("simply-no-shading.key.openConfigScreen",
		        InputConstants.UNKNOWN.getValue(),
		        SimplyNoShading.KEY_CATEGORY);
		final var reloadConfig = new KeyMapping("simply-no-shading.key.reloadConfig",
		        InputConstants.UNKNOWN.getValue(),
		        SimplyNoShading.KEY_CATEGORY);
		final var toggleBlockShading = new ToggleKeyMapping("simply-no-shading.key.toggleBlockShading",
		        InputConstants.UNKNOWN.getValue(),
		        SimplyNoShading.KEY_CATEGORY,
		        () -> simplyNoShading.getConfig().blockShadingEnabled);
		final var toggleCloudShading = new ToggleKeyMapping("simply-no-shading.key.toggleCloudShading",
		        InputConstants.UNKNOWN.getValue(),
		        SimplyNoShading.KEY_CATEGORY,
		        () -> simplyNoShading.getConfig().cloudShadingEnabled);

		registerKeyBinding(openConfigScreen);
		registerKeyBinding(reloadConfig);
		registerKeyBinding(toggleBlockShading);
		registerKeyBinding(toggleCloudShading);

		ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
			if (reloadConfig.consumeClick()) {
				while (reloadConfig.consumeClick()) {}

				simplyNoShading.loadConfig();
			}

			if (openConfigScreen.consumeClick()) {
				while (openConfigScreen.consumeClick()) {}

				minecraft.setScreen(new ConfigScreen(null));
				return;
			}

			final var builder = Config.builder(simplyNoShading.getConfig());

			builder.setBlockShadingEnabled(toggleBlockShading.isDown());
			builder.setCloudShadingEnabled(toggleCloudShading.isDown());

			simplyNoShading.setConfig(builder.build());
		});
	}

	/**
	 * Registers a shutdown thread with the name 'Simply No Shading Shutdown Thread'
	 *
	 * @param shutdownAction the shutdown action to run
	 */
	protected void setupShutdownHook(final Runnable shutdownAction) {
		final var shutdownThread = new Thread(shutdownAction);
		shutdownThread.setName("Simply No Shading Shutdown Thread");
		Runtime.getRuntime().addShutdownHook(shutdownThread);
	}
}
