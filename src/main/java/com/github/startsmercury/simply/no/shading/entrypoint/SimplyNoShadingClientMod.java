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

import com.github.startsmercury.simply.no.shading.config.ShadingRules;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.gui.ShadingSettingsScreen;
import com.github.startsmercury.simply.no.shading.util.SimplyNoShadingKeyManager;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;

import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

@Environment(CLIENT)
public abstract class SimplyNoShadingClientMod<C extends SimplyNoShadingClientConfig<? extends ShadingRules>, K extends SimplyNoShadingKeyManager> {
	private static SimplyNoShadingClientMod<?, ?> instance;

	public static final Logger LOGGER = LoggerFactory.getLogger("simply-no-shading/client");

	public static SimplyNoShadingClientMod<?, ?> getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Accessed too early!");
		}

		return instance;
	}

	public final C config;

	public final Path configPath;

	public final K keyManager;

	protected SimplyNoShadingClientMod(final C config, final Path configPath,
	    final Function<? super C, ? extends K> keyManagerProvider) {
		this.config = config;
		this.configPath = configPath;
		this.keyManager = keyManagerProvider.apply(this.config);

		loadConfig();

		instance = this;
	}

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

	private Screen createSettingsScreen(final Minecraft client) {
		LOGGER.debug("Creating settings screen...");

		final var settingsScreen = createSettingsScreen(client.screen, this.config);

		LOGGER.info("Created settings screen");
		return settingsScreen;
	}

	protected Screen createSettingsScreen(final Screen parent, final C config) {
		return new ShadingSettingsScreen(parent, this.config);
	}

	protected Type getConfigType() {
		return TypeToken.getParameterized(this.config.getClass(), this.config.shadingRules.getClass()).getType();
	}

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

	protected void registerShutdownHook() {
		LOGGER.debug("Registering shutdown hook...");

		Runtime.getRuntime().addShutdownHook(new Thread(this::saveConfig));

		LOGGER.info("Registered shutdown hook");
	}

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
