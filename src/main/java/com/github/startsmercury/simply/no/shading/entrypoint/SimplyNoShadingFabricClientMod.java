package com.github.startsmercury.simply.no.shading.entrypoint;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.config.FabricShadingRules;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingFabricClientConfig;
import com.github.startsmercury.simply.no.shading.gui.FabricShadingSettingsScreen;
import com.github.startsmercury.simply.no.shading.gui.SimplyNoShadingFabricSettingsScreen;
import com.github.startsmercury.simply.no.shading.util.SimplyNoShadingFabricKeyManager;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * Simply No Shading {@link ClientModInitializer fabric client mod initializer}.
 *
 * @since 5.0.0
 */
@Environment(CLIENT)
public class SimplyNoShadingFabricClientMod extends
    SimplyNoShadingClientMod<SimplyNoShadingFabricClientConfig<FabricShadingRules>, SimplyNoShadingFabricKeyManager>
    implements ClientModInitializer {
	/**
	 * The initialized instance.
	 */
	private static SimplyNoShadingFabricClientMod instance;

	/**
	 * Returns the initialized instance, throws {@link IllegalStateException} if
	 * there is none.
	 *
	 * @return the initialized instance
	 * @since 5.0.0
	 */
	public static SimplyNoShadingFabricClientMod getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Accessed too early!");
		}

		return instance;
	}

	/**
	 * Creates a new instance of {@code SimplyNoShadingFabricClientMod}.
	 *
	 * @since 5.0.0
	 */
	public SimplyNoShadingFabricClientMod() {
		super(new SimplyNoShadingFabricClientConfig<>(new FabricShadingRules()),
		    FabricLoader.getInstance().getConfigDir().resolve("simply-no-shading+client.json"),
		    SimplyNoShadingFabricKeyManager::new);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Screen createSettingsScreen(final Screen parent) {
		return FabricLoader.getInstance().isModLoaded("spruceui") ? new SimplyNoShadingFabricSettingsScreen(parent)
		    : new FabricShadingSettingsScreen(parent, this.config);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public void onInitializeClient() {
		LOGGER.debug("Initializing mod...");

		registerKeyMappings();
		registerLifecycleEventListeners();

		instance = this;

		LOGGER.info("Initialized mod");
	}

	/**
	 * Registers key mappings.
	 */
	protected void registerKeyMappings() {
		LOGGER.debug("Registering key mappings...");

		final var fabricLoader = FabricLoader.getInstance();

		if (!fabricLoader.isModLoaded("fabric-key-binding-api-v1")) {
			LOGGER.warn(
			    "Unable to register key mappings as the mod provided by 'fabric' (specifically 'fabric-key-binding-api-v1') is not present");

			return;
		}

		this.keyManager.register();

		LOGGER.info("Registered key mappings");
	}

	/**
	 * Register lifecycle event listeners.
	 */
	protected void registerLifecycleEventListeners() {
		LOGGER.debug("Registering life cycle event listeners...");

		if (!FabricLoader.getInstance().isModLoaded("fabric-lifecycle-events-v1")) {
			LOGGER.warn(
			    "Unable to register life cycle event listeners as the mod provided by 'fabric' (specifically 'fabric-lifecycle-events-v1') is not present");

			registerShutdownHook();

			return;
		}

		ClientTickEvents.END_CLIENT_TICK.register(new EndTick() {
			private boolean windowWasActive;

			@Override
			public void onEndTick(final Minecraft client) {
				final var windowActive = client.isWindowActive();

				if (this.windowWasActive == windowActive) {
					return;
				}

				if (windowActive) {
					loadConfig();
				} else {
					saveConfig();
				}

				this.windowWasActive = windowActive;
			}
		});
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			final var openSettings = this.keyManager.openSettings.consumeClick();

			if (openSettings) {
				openSettingsScreen(client);
			}

			final var observation = !openSettings ? this.config.observe() : null;
			final var toggled = toggleShade(this.keyManager.toggleAllShading, this.config.shadingRules.all)
			    | toggleShade(this.keyManager.toggleBlockShading, this.config.shadingRules.blocks)
			    | toggleShade(this.keyManager.toggleCloudShading, this.config.shadingRules.clouds)
			    | toggleShade(this.keyManager.toggleEnhancedBlockEntityShading,
			        this.config.shadingRules.enhancedBlockEntities)
			    | toggleShade(this.keyManager.toggleLiquidShading, this.config.shadingRules.liquids);

			if (!toggled || openSettings) {
				return;
			}

			observation.react(client);

			if (!this.config.smartReloadMessage || !observation.smartlyRebuiltChunks() || client.player == null) {
				return;
			}

			client.player.displayClientMessage(
			    new TranslatableComponent("simply-no-shading.option.shadingRules.smartReload"), true);
		});
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> saveConfig());

		LOGGER.info("Registered life cycle event listeners");
	}
}
