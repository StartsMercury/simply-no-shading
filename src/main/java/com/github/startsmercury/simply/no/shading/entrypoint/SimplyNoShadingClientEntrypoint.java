package com.github.startsmercury.simply.no.shading.entrypoint;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;
import com.github.startsmercury.simply.no.shading.client.gui.screens.ConfigScreen;
import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.resources.ResourceLocation;

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
		final SimplyNoShading simplyNoShading = new SimplyNoShading();

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
		final FabricKeyBinding openConfigScreen = FabricKeyBinding.Builder.create(new ResourceLocation("simply-no-shading", "open_config_screen"),
		        InputConstants.Type.KEYSYM,
		        InputConstants.UNKNOWN.getValue(),
		        "simply-no-shading.key.categories.simply-no-shading").build();
		final FabricKeyBinding reloadConfig = FabricKeyBinding.Builder.create(new ResourceLocation("simply-no-shading", "reload_config"),
		        InputConstants.Type.KEYSYM,
		        InputConstants.UNKNOWN.getValue(),
		        "simply-no-shading.key.categories.simply-no-shading").build();
		final FabricKeyBinding toggleBlockShading = FabricKeyBinding.Builder.create(new ResourceLocation("simply-no-shading", "toggle_block_shading"),
		        InputConstants.Type.KEYSYM,
		        InputConstants.UNKNOWN.getValue(),
		        "simply-no-shading.key.categories.simply-no-shading").build();
		final FabricKeyBinding toggleCloudShading = FabricKeyBinding.Builder.create(new ResourceLocation("simply-no-shading", "toggle_cloud_shading"),
		        InputConstants.Type.KEYSYM,
		        InputConstants.UNKNOWN.getValue(),
		        "simply-no-shading.key.categories.simply-no-shading").build();


		KeyBindingRegistry.INSTANCE.addCategory("simply-no-shading.key.categories.simply-no-shading");
		KeyBindingRegistry.INSTANCE.register(openConfigScreen);
		KeyBindingRegistry.INSTANCE.register(reloadConfig);
		KeyBindingRegistry.INSTANCE.register(toggleBlockShading);
		KeyBindingRegistry.INSTANCE.register(toggleCloudShading);

		ClientTickCallback.EVENT.register(minecraft -> {
			if (reloadConfig.consumeClick()) {
				while (reloadConfig.consumeClick()) {}

				simplyNoShading.loadConfig();
			}

			if (openConfigScreen.consumeClick()) {
				while (openConfigScreen.consumeClick()) {}

				minecraft.setScreen(new ConfigScreen(null));
				return;
			}

			final Config.Builder builder = Config.builder(simplyNoShading.getConfig());

			while (toggleBlockShading.consumeClick())
				builder.setBlockShadingEnabled(!builder.isBlockShadingEnabled());
			while (toggleCloudShading.consumeClick())
				builder.setCloudShadingEnabled(!builder.isCloudShadingEnabled());

			simplyNoShading.setConfig(builder.build());
		});
	}

	/**
	 * Registers a shutdown thread with the name 'Simply No Shading Shutdown Thread'
	 *
	 * @param shutdownAction the shutdown action to run
	 */
	protected void setupShutdownHook(final Runnable shutdownAction) {
		final Thread shutdownThread = new Thread(shutdownAction);
		shutdownThread.setName("Simply No Shading Shutdown Thread");
		Runtime.getRuntime().addShutdownHook(shutdownThread);
	}
}
