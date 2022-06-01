package com.github.startsmercury.simply.no.shading.entrypoint;

import static com.github.startsmercury.simply.no.shading.util.SimplyNoShadingConstants.GSON;
import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newBufferedWriter;
import static net.fabricmc.api.EnvType.CLIENT;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.function.Function;

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
	 * The instance.
	 */
	private static SimplyNoShadingClientMod<?, ?> instance;

	/**
	 * The logger.
	 *
	 * @since 5.0.0
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger("simply-no-shading/client");

	/**
	 * Returns the initialized instance, throws {@link IllegalStateException} if
	 * there is none.
	 *
	 * @return the initialized instance
	 * @since 5.0.0
	 */
	public static SimplyNoShadingClientMod<?, ?> getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Accessed too early!");
		}

		return instance;
	}

	/**
	 * @param keyMapping  the key mapping
	 * @param shadingRule the shading rule
	 * @return {@code true} if there was a consumed click; {@code false} otherwise
	 */
	protected static boolean toggleShade(final KeyMapping keyMapping, final ShadingRule shadingRule) {
		if (keyMapping.consumeClick()) {
			shadingRule.toggleShade();

			return true;
		} else {
			return false;
		}
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
	 * @since 5.0.0
	 */
	protected SimplyNoShadingClientMod(final C config, final Path configPath,
	    final Function<? super C, ? extends K> keyManagerProvider) {
		this.config = config;
		this.configPath = configPath;
		this.keyManager = keyManagerProvider.apply(this.config);

		loadConfig();

		instance = this;
	}

	/**
	 * Creates the config in disk.
	 *
	 * @see #loadConfig()
	 * @since 5.0.0
	 */
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
	 * Creates the settings screen.
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
	 * Creates the settings screen.
	 *
	 * @param parent the parent screen
	 * @return the settings screen
	 */
	protected Screen createSettingsScreen(final Screen parent) {
		return new ShadingSettingsScreen(parent, this.config);
	}

	/**
	 * @return the config type.
	 */
	protected Type getConfigType() {
		return TypeToken.getParameterized(this.config.getClass(), this.config.shadingRules.getClass()).getType();
	}

	/**
	 * Loads the config from disk, it will {@link #createConfig() create} a new one
	 * if not present.
	 *
	 * @see #createConfig()
	 * @see #saveConfig()
	 * @since 5.0.0
	 */
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
		}
	}

	/**
	 * Opens the settings screen
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
	 * Registers a shutdown hook to {@link #saveConfig() save the config}.
	 */
	protected void registerShutdownHook() {
		LOGGER.debug("Registering shutdown hook...");

		Runtime.getRuntime().addShutdownHook(new Thread(this::saveConfig));

		LOGGER.info("Registered shutdown hook");
	}

	/**
	 * Saves the config to disk.
	 *
	 * @see #loadConfig()
	 * @since 5.0.0
	 */
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
