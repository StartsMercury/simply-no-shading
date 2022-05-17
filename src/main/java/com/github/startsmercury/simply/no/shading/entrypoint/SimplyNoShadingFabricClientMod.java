package com.github.startsmercury.simply.no.shading.entrypoint;

import java.util.function.Consumer;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig.ShadingRule;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingFabricClientConfig;
import com.github.startsmercury.simply.no.shading.impl.CloudRenderer;
import com.github.startsmercury.simply.no.shading.util.SimplyNoShadingFabricKeyManager;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class SimplyNoShadingFabricClientMod
    extends SimplyNoShadingClientMod<SimplyNoShadingFabricClientConfig, SimplyNoShadingFabricKeyManager>
    implements ClientModInitializer {
	private static SimplyNoShadingFabricClientMod instance;

	protected static void consumeClick(final KeyMapping keyMapping, final Minecraft client,
	    final Consumer<Minecraft> action) {
		if (keyMapping.consumeClick()) {
			action.accept(client);
		}
	}

	public static SimplyNoShadingFabricClientMod getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Accessed too early!");
		}

		return instance;
	}

	protected static boolean toggleShade(final KeyMapping keyMapping, final ShadingRule shadingRule) {
		if (keyMapping.consumeClick()) {
			return shadingRule.toggleShade();
		}

		return false;
	}

	public SimplyNoShadingFabricClientMod() {
		super(new SimplyNoShadingFabricClientConfig(),
		    FabricLoader.getInstance().getConfigDir().resolve("simply-no-shading+client.json"),
		    SimplyNoShadingFabricKeyManager::new);
	}

	@Override
	public void onInitializeClient() {
		registerKeyMappings();
		registerLifecycleEventListeners();

		instance = this;
	}

	protected void registerKeyMappings() {
		LOGGER.debug("Registering key mappings...");

		if (!FabricLoader.getInstance().isModLoaded("fabric-key-binding-api-v1")) {
			LOGGER.warn(
			    "Unable to register key mappings as the mod provided by 'fabric' (specifically 'fabric-key-binding-api-v1') is not present");

			return;
		}

		this.keyManager.registerAll();

		LOGGER.info("Registered key mappings");
	}

	protected void registerLifecycleEventListeners() {
		LOGGER.debug("Registering life cycle event listeners...");

		if (!FabricLoader.getInstance().isModLoaded("fabric-lifecycle-events-v1")) {
			LOGGER.warn(
			    "Unable to register life cycle event listeners as the mod provided by 'fabric' (specifically 'fabric-lifecycle-events-v1') is not present");

			registerShutdownHook();

			return;
		}

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			toggleShade(this.keyManager.toggleAllShading, this.config.allShading);

			final var blockShadingChanged = toggleShade(this.keyManager.toggleBlockShading, this.config.blockShading);
			final var cloudShadingChanged = toggleShade(this.keyManager.toggleCloudShading, this.config.cloudShading);
			final var enhancedBlockEntityShadingChanged = toggleShade(this.keyManager.toggleEnhancedBlockEntityShading,
			    this.config.enhancedBlockEntityShading);

			if (cloudShadingChanged && client.levelRenderer instanceof final CloudRenderer cloudRenderer) {
				cloudRenderer.generateClouds();
			}

			if (blockShadingChanged || enhancedBlockEntityShadingChanged) {
				client.levelRenderer.allChanged();
			}

			consumeClick(this.keyManager.openSettings, client, this::openSettings);
		});
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> saveConfig());

		LOGGER.info("Registered life cycle event listeners");
	}
}
