package com.github.startsmercury.simply.no.shading.entrypoint;

import static com.github.startsmercury.simply.no.shading.util.SimplyNoShadingConstants.GSON;
import static com.github.startsmercury.simply.no.shading.util.SimplyNoShadingConstants.LOGGER;
import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newBufferedWriter;
import static net.fabricmc.api.EnvType.CLIENT;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.startsmercury.simply.no.shading.config.FabricShadingRules;
import com.github.startsmercury.simply.no.shading.config.ShadingRule;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingFabricClientConfig;
import com.github.startsmercury.simply.no.shading.gui.SimplyNoShadingFabricSettingsScreen;
import com.github.startsmercury.simply.no.shading.util.SimplyNoShadingFabricKeyManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * The Simply No Shading mod class exposes the configuration object and its IO
 * accesses. This class is a {@linkplain ClientModInitializer fabric client mod
 * initializer} and may run {@link #registerKeyMappings()},
 * {@link #registerLifecycleEventListeners()}, and
 * {@link #registerShutdownHook()}.
 *
 * @since 5.0.0
 */
@Environment(CLIENT)
@SuppressWarnings("deprecation")
public class SimplyNoShadingFabricClientMod extends
        SimplyNoShadingClientMod<SimplyNoShadingFabricClientConfig<FabricShadingRules>, SimplyNoShadingFabricKeyManager>
        implements ClientModInitializer {
	/**
	 * The latest initialized instance of this class.
	 *
	 * @see #onInitializeClient()
	 */
	private static SimplyNoShadingFabricClientMod instance;

	/**
	 * The message shown in-game to the player when a smart reload was performed.
	 *
	 * @since 5.0.0
	 */
	public static final Component SMART_RELOAD_COMPONENT = Component.translatable(
	        "simply-no-shading.option.shadingRules.smartReload");

	/**
	 * Returns the latest initialized instance of this class when present, throws an
	 * {@link IllegalStateException} otherwise.
	 *
	 * @return the latest initialized instance of this class when present
	 * @throws IllegalStateException if there is no initialized instance of this
	 *                               class
	 * @since 5.0.0
	 * @see #isInitialized()
	 */
	public static SimplyNoShadingFabricClientMod getInstance() {
		if (isInitialized())
			throw new NoSuchElementException("An initialized instance of "
			        + SimplyNoShadingFabricClientMod.class.getName() + " is not yet present");

		return instance;
	}

	/**
	 * Returns {@code true} if an initialized instance of this class is present.
	 *
	 * @return {@code true} if an initialized instance of this class is present
	 * @since 5.0.0
	 */
	public static boolean isInitialized() {
		return instance != null;
	}

	/**
	 * Returns {@code true} when a key press is consumed and the given shading rule
	 * is toggled; {@code false} otherwise.
	 *
	 * @param keyMapping  the key mapping to consume click events from
	 * @param shadingRule the shading rule to toggle on click events
	 * @return {@code true} when a key press is consumed and the given shading rule
	 *         is toggled; {@code false} otherwise
	 */
	protected static boolean toggleShade(final KeyMapping keyMapping, final ShadingRule shadingRule) {
		if (!keyMapping.consumeClick())
			return false;

		shadingRule.toggleShade();

		return true;
	}

	/**
	 * Executes an action when an instance of this class {@link #isInitialized() was
	 * initialized}.
	 *
	 * @param whenInitialized the action ran when initialized
	 * @since 5.0.0
	 */
	@Deprecated
	public static void whenInitialized(final Consumer<? super SimplyNoShadingClientMod<?, ?>> whenInitialized) {
		Objects.requireNonNull(whenInitialized);

		if (isInitialized())
			whenInitialized.accept(instance);
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
	@Deprecated
	public static <T> T whenInitialized(
	        final Function<? super SimplyNoShadingClientMod<?, ?>, ? extends T> whenInitialized,
	        final Supplier<? extends T> whenUninitialized) {
		Objects.requireNonNull(whenInitialized);
		Objects.requireNonNull(whenUninitialized);

		return isInitialized() ? whenInitialized.apply(instance) : whenUninitialized.get();
	}

	/**
	 * The configuration object.
	 *
	 * @since 5.0.0
	 */
	public final SimplyNoShadingFabricClientConfig<FabricShadingRules> config;

	/**
	 * The configuration file path.
	 *
	 * @since 5.0.0
	 */
	public final Path configPath;

	/**
	 * The key manager.
	 *
	 * @since 5.0.0
	 */
	public final SimplyNoShadingFabricKeyManager keyManager;

	/**
	 * Creates a new instance of {@code SimplyNoShadingFabricClientMod}.
	 */
	public SimplyNoShadingFabricClientMod() {
		super(new SimplyNoShadingFabricClientConfig<>(new FabricShadingRules()),
		        FabricLoader.getInstance().getConfigDir().resolve("simply-no-shading+client.json"),
		        SimplyNoShadingFabricKeyManager::new);

		this.config = super.config;
		this.configPath = super.configPath;
		this.keyManager = super.keyManager;
	}

	/**
	 * Creates the configuration file, usually when the file is absent at load time.
	 *
	 * @see #loadConfig()
	 * @since 5.0.0
	 */
	@Override
	public void createConfig() {
		LOGGER.debug("Creating config...");

		final var configJson = new JsonObject();

		this.config.write(configJson);

		try (final var buffer = newBufferedWriter(this.configPath);
		     final var out = GSON.newJsonWriter(buffer)) {
			Streams.write(configJson, out);

			LOGGER.info("Created config");
		} catch (final IOException ioe) {
			LOGGER.warn("Unable to create config", ioe);
		}
	}

	/**
	 * Creates the settings screen given the client.
	 *
	 * @param client the client
	 * @return the settings screen
	 * @see #createSettingsScreen(Screen)
	 */
	private Screen createSettingsScreen(final Minecraft client) {
		LOGGER.debug("Creating settings screen...");

		final var settingsScreen = createSettingsScreen(client.screen);

		LOGGER.info("Created settings screen");
		return settingsScreen;
	}

	/**
	 * Creates the settings screen given the parent screen.
	 *
	 * @param parent the parent screen
	 * @return the settings screen
	 * @since 5.0.0
	 */
	@Override
	public Screen createSettingsScreen(final Screen parent) {
		return new SimplyNoShadingFabricSettingsScreen(parent, this.config);
	}

	/**
	 * Returns the configuration object.
	 *
	 * @return the configuration object
	 * @since 5.0.0
	 */
	@Override
	public SimplyNoShadingFabricClientConfig<FabricShadingRules> getConfig() {
		return this.config;
	}

	/**
	 * Returns the configuration file path.
	 *
	 * @return the configuration file path
	 * @since 5.0.0
	 */
	@Override
	public Path getConfigPath() {
		return this.configPath;
	}

	/**
	 * Returns the configuration type.
	 *
	 * @return the configuration type
	 */
	@Override
	protected Type getConfigType() {
		return TypeToken.getParameterized(this.config.getClass(), this.config.shadingRules.getClass()).getType();
	}

	/**
	 * Returns the key manager.
	 *
	 * @return the key manager
	 * @since 5.0.0
	 */
	@Override
	public SimplyNoShadingFabricKeyManager getKeyManager() {
		return this.keyManager;
	}

	/**
	 * Loads the configuration file when present; {@link #createConfig() creates} a
	 * new one otherwise. On rare occasions, the configuration is file is
	 * regenerated when corrupted.
	 *
	 * @see #createConfig()
	 * @see #saveConfig()
	 * @since 5.0.0
	 */
	@Override
	public void loadConfig() {
		try (final var buffer = newBufferedReader(this.configPath);
		     var in = GSON.newJsonReader(buffer)) {
			LOGGER.debug("Loading config...");

			this.config.read(Streams.parse(in));

			LOGGER.info("Loaded config");
		} catch (final NoSuchFileException nsfe) {
			createConfig();
		} catch (final IOException ioe) {
			LOGGER.debug("Loading config...");

			LOGGER.warn("Unable to load config", ioe);
		} catch (final JsonSyntaxException jse) {
			LOGGER.warn("Malformed json", jse);

			saveConfig();
		}
	}

	/**
	 * Initializes Simply No Shading for the Fabric Mod Loader.
	 *
	 * - Key mappings are registered - Life-cycle events are registered - Shutdown
	 * hooks may be registered
	 *
	 * @see #registerKeyMappings()
	 * @see #registerLifecycleEventListeners()
	 * @see #registerShutdownHook()
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
	 * Sets the currently opened screen to the settings screen.
	 *
	 * @param client the client
	 * @see #createSettingsScreen(Screen)
	 */
	@Override
	protected void openSettingsScreen(final Minecraft client) {
		LOGGER.debug("Opening settings screen...");

		if (!(client.screen instanceof SimplyNoShadingFabricSettingsScreen)) {
			final var settingsScreen = createSettingsScreen(client);

			client.setScreen(settingsScreen);

			LOGGER.info("Opened settings screen");
		} else
			LOGGER.warn("Unable to open settings screen as it's already open!");
	}

	/**
	 * Registers key mappings present in {@link #getKeyManager() keyManager}.
	 */
	protected void registerKeyMappings() {
		LOGGER.debug("Registering key mappings...");

		if (!FabricLoader.getInstance().isModLoaded("fabric-key-binding-api-v1")) {
			LOGGER.warn(
			        "Unable to register key mappings as the mod provided by 'fabric-api' (specifically 'fabric-key-binding-api-v1') is not present");

			return;
		}

		this.keyManager.register();

		LOGGER.info("Registered key mappings");
	}

	/**
	 * Registers life-cycle event listeners.
	 */
	protected void registerLifecycleEventListeners() {
		LOGGER.debug("Registering life cycle event listeners...");

		// Maybe redundant, this is embedded in spruceui
		if (!FabricLoader.getInstance().isModLoaded("fabric-lifecycle-events-v1")) {
			LOGGER.warn(
			        "Unable to register life cycle event listeners as the mod provided by 'fabric-api' (specifically 'fabric-lifecycle-events-v1') is not present");

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

				if (windowActive)
					loadConfig();
				else
					saveConfig();

				this.windowWasActive = windowActive;
			}
		});
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			final var openSettings = this.keyManager.openSettings.consumeClick();

			if (openSettings)
				openSettingsScreen(client);

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

	/**
	 * Registers shutdown hooks.
	 */
	@Override
	protected void registerShutdownHook() {
		LOGGER.debug("Registering shutdown hook...");

		Runtime.getRuntime().addShutdownHook(new Thread(this::saveConfig));

		LOGGER.info("Registered shutdown hook");
	}

	/**
	 * Saves the configuration file by appending the contents, though it is
	 * completely overwritten when the original file gets corrupted.
	 *
	 * @see #loadConfig()
	 * @since 5.0.0
	 */
	@Override
	public void saveConfig() {
		LOGGER.debug("Saving config...");

		final JsonObject configJson;

		try (final var reader = newBufferedReader(this.configPath);
		     var in = GSON.newJsonReader(reader)) {
			configJson = Streams.parse(in) instanceof final JsonObject object ? object : new JsonObject();
		} catch (final IOException ioe) {
			LOGGER.warn("Unable to save config", ioe);
			return;
		}

		this.config.write(configJson);

		try (final var writer = newBufferedWriter(this.configPath);
		     final var out = GSON.newJsonWriter(writer)) {
			Streams.write(configJson, out);

			LOGGER.info("Saved config");
		} catch (final IOException ioe) {
			LOGGER.warn("Unable to save config", ioe);
		}
	}
}
