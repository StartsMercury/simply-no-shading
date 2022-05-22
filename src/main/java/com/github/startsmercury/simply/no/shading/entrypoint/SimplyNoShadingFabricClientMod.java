package com.github.startsmercury.simply.no.shading.entrypoint;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig.ShadingRule;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingFabricClientConfig;
import com.github.startsmercury.simply.no.shading.gui.FabricShadingSettingsScreen;
import com.github.startsmercury.simply.no.shading.util.SimplyNoShadingFabricKeyManager;

import net.coderbot.iris.Iris;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;

@Environment(CLIENT)
public class SimplyNoShadingFabricClientMod
    extends SimplyNoShadingClientMod<SimplyNoShadingFabricClientConfig, SimplyNoShadingFabricKeyManager>
    implements ClientModInitializer {
	private static SimplyNoShadingFabricClientMod instance;

	public static SimplyNoShadingFabricClientMod getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Accessed too early!");
		}

		return instance;
	}

	protected static void toggleShade(final KeyMapping keyMapping, final ShadingRule shadingRule) {
		if (keyMapping.consumeClick()) {
			shadingRule.toggleShade();
		}
	}

	public SimplyNoShadingFabricClientMod() {
		super(new SimplyNoShadingFabricClientConfig(),
		    FabricLoader.getInstance().getConfigDir().resolve("simply-no-shading+client.json"),
		    SimplyNoShadingFabricKeyManager::new);
	}

	@Override
	protected Screen createSettingsScreen(final Screen screen, final SimplyNoShadingClientConfig config) {
		return new FabricShadingSettingsScreen(screen, config);
	}

	@Override
	public void onInitializeClient() {
		LOGGER.debug("Initializing mod...");

		registerKeyMappings();
		registerLifecycleEventListeners();

		instance = this;

		LOGGER.info("Initialized mod");
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
			final var openSettings = this.keyManager.openSettings.consumeClick();
			final var refresh = !openSettings
			    && (!FabricLoader.getInstance().isModLoaded("iris") || !Iris.getIrisConfig().areShadersEnabled());

			if (openSettings) {
				openSettingsScreen(client);
			}

			final var observation = this.config.observe();

			toggleShade(this.keyManager.toggleAllShading, this.config.allShading);
			toggleShade(this.keyManager.toggleBlockShading, this.config.blockShading);
			toggleShade(this.keyManager.toggleCloudShading, this.config.cloudShading);
			toggleShade(this.keyManager.toggleEnhancedBlockEntityShading, this.config.enhancedBlockEntityShading);
			toggleShade(this.keyManager.toggleLiquidShading, this.config.liquidShading);

			if (refresh) {
				observation.consume(client);
			}
		});
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> saveConfig());

		LOGGER.info("Registered life cycle event listeners");
	}
}
