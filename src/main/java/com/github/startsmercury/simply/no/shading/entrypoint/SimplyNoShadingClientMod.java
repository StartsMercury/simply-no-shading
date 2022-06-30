package com.github.startsmercury.simply.no.shading.entrypoint;

import static com.github.startsmercury.simply.no.shading.util.SimplyNoShadingConstants.GSON;
import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newBufferedWriter;
import static net.fabricmc.api.EnvType.CLIENT;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.startsmercury.simply.no.shading.config.ShadingRule;
import com.github.startsmercury.simply.no.shading.config.ShadingRules;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.gui.ShadingSettingsScreen;
import com.github.startsmercury.simply.no.shading.util.SimplyNoShadingKeyManager;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;

import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * The base mod class of Simply No Shading.
 *
 * @param <C> The config type
 * @param <K> The key manager type
 * @since 5.0.0
 */
@Environment(CLIENT)
public abstract class SimplyNoShadingClientMod<C extends SimplyNoShadingClientConfig<? extends ShadingRules>, K extends SimplyNoShadingKeyManager> {
	/**
	 * The instance of this class.
	 */
	private static SimplyNoShadingClientMod<?, ?> instance;

	/**
	 * The logger used for debugging and sending messages to the console.
	 *
	 * @since 5.0.0
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger("simply-no-shading/client");

	/**
	 * The message shown in-game to the player when a smart reload was performed.
	 *
	 * @since 5.0.0
	 */
	public static final TranslatableComponent SMART_RELOAD_COMPONENT = new TranslatableComponent("simply-no-shading.option.shadingRules.smartReload");

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
	public static SimplyNoShadingClientMod<?, ?> getInstance() {
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
	 * Returns {@code true} if the given shading rule was toggled for the given key
	 * mapping being pressed.
	 *
	 * @param keyMapping  the key mapping
	 * @param shadingRule the shading rule
	 * @return {@code true} if the given shading rule was toggled for the given key
	 *         mapping being pressed
	 */
	protected static boolean toggleShade(final KeyMapping keyMapping, final ShadingRule shadingRule) {
		if (keyMapping.consumeClick()) {
			shadingRule.toggleShade();

			return true;
		} else
			return false;
	}

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
	 * The config.
	 *
	 * @since 5.0.0
	 */
	public final C config;

	/**
	 * The config path.
	 *
	 * @since 5.0.0
	 */
	public final Path configPath;

	/**
	 * The key manager.
	 *
	 * @since 5.0.0
	 */
	public final K keyManager;

	/**
	 * Creates a new instance of SimplyNoShadingClientMod.
	 *
	 * @param config             the config
	 * @param configPath         the config path
	 * @param keyManagerProvider the key manager provider
	 */
	protected SimplyNoShadingClientMod(final C config,
	                                   final Path configPath,
	                                   final Function<? super C, ? extends K> keyManagerProvider) {
		this.config = config;
		this.configPath = configPath;
		this.keyManager = keyManagerProvider.apply(this.config);

		loadConfig();

		instance = this;
	}

	/**
	 * Creates the config, writing it to disk.
	 *
	 * @see #loadConfig()
	 * @since 5.0.0
	 */
	public void createConfig() {
		LOGGER.debug("Creating config...");

		final var configJson = new JsonObject();

		this.config.write(configJson);

		try (final var buffer = newBufferedWriter(this.configPath); final var out = GSON.newJsonWriter(buffer)) {
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
	 */
	public Screen createSettingsScreen(final Screen parent) {
		return new ShadingSettingsScreen(parent, this.config);
	}

	/**
	 * Returns the config.
	 *
	 * @return the config
	 * @since 5.0.0
	 */
	public C getConfig() { return this.config; }

	/**
	 * Returns the config path.
	 *
	 * @return the config path
	 * @since 5.0.0
	 */
	public Path getConfigPath() { return this.configPath; }

	/**
	 * Returns the config type.
	 *
	 * @return the config type
	 */
	protected Type getConfigType() {
		return TypeToken.getParameterized(this.config.getClass(), this.config.shadingRules.getClass()).getType();
	}

	/**
	 * Returns the key manager.
	 *
	 * @return the key manager
	 * @since 5.0.0
	 */
	public K getKeyManager() { return this.keyManager; }

	/**
	 * Loads the config from disk, it will {@link #createConfig() create} a new one
	 * if its absent.
	 *
	 * @see #createConfig()
	 * @see #saveConfig()
	 * @since 5.0.0
	 */
	public void loadConfig() {
		try (final var buffer = newBufferedReader(this.configPath); var in = GSON.newJsonReader(buffer)) {
			LOGGER.debug("Loading config...");

			this.config.read(Streams.parse(in));

			LOGGER.info("Loaded config");
		} catch (final NoSuchFileException nsfe) {
			createConfig();
		} catch (final IOException ioe) {
			LOGGER.debug("Loading config...");

			LOGGER.warn("Unable to load config", ioe);
		}
	}

	/**
	 * Opens the settings screen.
	 *
	 * @param client the client
	 * @see #createSettingsScreen(Screen)
	 */
	protected void openSettingsScreen(final Minecraft client) {
		LOGGER.debug("Opening settings screen...");

		if (!(client.screen instanceof ShadingSettingsScreen)) {
			final var settingsScreen = createSettingsScreen(client);

			client.setScreen(settingsScreen);

			LOGGER.info("Opened settings screen");
		} else {
			LOGGER.warn("Unable to open settings screen as it's already open!");
		}
	}

	/**
	 * Registers a shutdown hook to run {@link #saveConfig()}.
	 */
	protected void registerShutdownHook() {
		LOGGER.debug("Registering shutdown hook...");

		Runtime.getRuntime().addShutdownHook(new Thread(this::saveConfig));

		LOGGER.info("Registered shutdown hook");
	}

	/**
	 * Saves the config, writing it to disk.
	 *
	 * @see #loadConfig()
	 * @since 5.0.0
	 */
	public void saveConfig() {
		LOGGER.debug("Saving config...");

		final JsonObject configJson;

		try (final var reader = newBufferedReader(this.configPath); var in = GSON.newJsonReader(reader)) {
			configJson = Streams.parse(in) instanceof final JsonObject object ? object : new JsonObject();
		} catch (final IOException ioe) {
			LOGGER.warn("Unable to save config", ioe);
			return;
		}

		this.config.write(configJson);

		try (final var writer = newBufferedWriter(this.configPath); final var out = GSON.newJsonWriter(writer)) {
			Streams.write(configJson, out);

			LOGGER.info("Saved config");
		} catch (final IOException ioe) {
			LOGGER.warn("Unable to save config", ioe);
		}
	}
}
