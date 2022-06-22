package com.github.startsmercury.simply.no.shading.entrypoint;

import static net.fabricmc.api.EnvType.CLIENT;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.startsmercury.simply.no.shading.config.FabricShadingRules;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingFabricClientConfig;
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
	 * The instance of this class.
	 */
	private static SimplyNoShadingFabricClientMod instance;

	/**
	 * Returns the instance of this class if there is one initialized, throws an
	 * {@link IllegalStateException} otherwise.
	 *
	 * @return the instance of this class if there is one initialized, throws an
	 *         {@link IllegalStateException} otherwise
	 * @throws IllegalStateException if there is no initialized instance of this
	 *                               class
	 * @since 5.0.0
	 * @see #isInitialized()
	 */
	public static SimplyNoShadingFabricClientMod getInstance() {
		if (isInitialized())
			throw new IllegalStateException("Accessed too early!");

		return instance;
	}

	/**
	 * Returns {@code true} if an instance of this class is initialized where
	 * accesses to {@link #getInstance()} are valid.
	 *
	 * @return {@code true} if an instance of this class is initialized where
	 *         accesses to {@link #getInstance()} are valid
	 * @since 5.0.0
	 */
	public static boolean isInitialized() { return instance == null; }

	/**
	 * Executes an action when an instance of this class {@link #isInitialized() was
	 * initialized}.
	 *
	 * @param whenInitialized the action ran when initialized
	 * @since 5.0.0
	 */
	public static void whenInitialized(final Consumer<? super SimplyNoShadingClientMod<?, ?>> whenInitialized) {
		Objects.requireNonNull(whenInitialized);

		if (isInitialized()) { whenInitialized.accept(instance); }
	}

	/**
	 * Executes an action when an instance of this class {@link #isInitialized() was
	 * initialized}, a fallback action is executed otherwise.
	 *
	 * @param <T>               the result type
	 * @param whenInitialized   the action ran when initialized
	 * @param whenUninitialized the fallback action
	 * @return the result of one of the given actions
	 * @since 5.0.0
	 */
	public static <T> T whenInitialized(final Function<? super SimplyNoShadingClientMod<?, ?>, ? extends T> whenInitialized,
	                                    final Supplier<? extends T> whenUninitialized) {
		Objects.requireNonNull(whenInitialized);
		Objects.requireNonNull(whenUninitialized);

		return isInitialized() ? whenInitialized.apply(instance) : whenUninitialized.get();
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
	public Screen createSettingsScreen(final Screen parent) {
		return new SimplyNoShadingFabricSettingsScreen(parent, this.config);
	}

	/**
	 * {@inheritDoc}
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
	 * Registers the key mappings to allow key presses to be consumed.
	 */
	protected void registerKeyMappings() {
		LOGGER.debug("Registering key mappings...");

		final var fabricLoader = FabricLoader.getInstance();

		if (!fabricLoader.isModLoaded("fabric-key-binding-api-v1")) {
			LOGGER.warn("Unable to register key mappings as the mod provided by 'fabric' (specifically 'fabric-key-binding-api-v1') is not present");

			return;
		}

		this.keyManager.register();

		LOGGER.info("Registered key mappings");
	}

	/**
	 * Register lifecycle event listeners to allow key mapping usage to take affect.
	 */
	protected void registerLifecycleEventListeners() {
		LOGGER.debug("Registering life cycle event listeners...");

		// Ineffective, this is bundled in spruceui
		if (!FabricLoader.getInstance().isModLoaded("fabric-lifecycle-events-v1")) {
			LOGGER.warn("Unable to register life cycle event listeners as the mod provided by 'fabric' (specifically 'fabric-lifecycle-events-v1') is not present");

			registerShutdownHook();

			return;
		}

		ClientTickEvents.END_CLIENT_TICK.register(new EndTick() {
			private boolean windowWasActive;

			@Override
			public void onEndTick(final Minecraft client) {
				final var windowActive = client.isWindowActive();

				if (this.windowWasActive == windowActive)
					return;

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

			if (openSettings) { openSettingsScreen(client); }

			final var observation = !openSettings ? this.config.observe() : null;
			final var toggled = toggleShade(this.keyManager.toggleAllShading, this.config.shadingRules.all)
			        | toggleShade(this.keyManager.toggleBlockShading, this.config.shadingRules.blocks)
			        | toggleShade(this.keyManager.toggleCloudShading, this.config.shadingRules.clouds)
			        | toggleShade(this.keyManager.toggleEnhancedBlockEntityShading,
			                      this.config.shadingRules.enhancedBlockEntities)
			        | toggleShade(this.keyManager.toggleLiquidShading, this.config.shadingRules.liquids);

			if (!toggled || openSettings)
				return;

			observation.react(client);

			if (!this.config.smartReloadMessage || !observation.smartlyRebuiltChunks() || client.player == null)
				return;

			client.player.displayClientMessage(SMART_RELOAD_COMPONENT, true);
		});
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> saveConfig());

		LOGGER.info("Registered life cycle event listeners");
	}
}
