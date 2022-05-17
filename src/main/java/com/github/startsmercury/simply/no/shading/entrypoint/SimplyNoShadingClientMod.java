package com.github.startsmercury.simply.no.shading.entrypoint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.gui.ShadingSettingsScreen;
import com.github.startsmercury.simply.no.shading.util.SimplyNoShadingKeyManager;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.minecraft.client.Minecraft;

public abstract class SimplyNoShadingClientMod<C extends SimplyNoShadingClientConfig, K extends SimplyNoShadingKeyManager> {
	private static SimplyNoShadingClientMod<?, ?> instance;

	public static final Logger LOGGER = LoggerFactory.getLogger("simply-no-shading+client");

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

		instance = this;
	}

	public void createConfig() {
		LOGGER.debug("Creating config...");

		try (final var buffer = Files.newBufferedWriter(this.configPath);
		     final var out = new JsonWriter(buffer)) {
			out.beginObject();

			this.config.write(out);

			out.endObject();

			LOGGER.info("Created config");
		} catch (final IOException ioe) {
			LOGGER.warn("Unable to create config", ioe);
		}
	}

	public void loadConfig() {
		try (final var buffer = Files.newBufferedReader(this.configPath);
		     final var in = new JsonReader(buffer)) {
			LOGGER.debug("Loading config...");

			in.beginObject();

			this.config.read(in);

			in.endObject();

			LOGGER.info("Loaded config");
		} catch (final NoSuchFileException nsfe) {
			createConfig();
		} catch (final IOException ioe) {
			LOGGER.debug("Loading config...");

			LOGGER.warn("Unable to load config", ioe);
		}
	}

	protected void openSettings(final Minecraft client) {
		if (!(client.screen instanceof ShadingSettingsScreen)) {
			client.setScreen(new ShadingSettingsScreen(client.screen));
		}
	}

	protected void registerShutdownHook() {
		LOGGER.debug("Registering shutdown hook...");

		Runtime.getRuntime().addShutdownHook(new Thread(this::saveConfig));

		LOGGER.info("Registered shutdown hook");
	}

	public void saveConfig() {
		LOGGER.debug("Saving config...");

		try (final var buffer = Files.newBufferedWriter(this.configPath);
		     final var out = new JsonWriter(buffer)) {
			out.beginObject();

			this.config.write(out);

			out.endObject();

			LOGGER.info("Saved config");
		} catch (final IOException ioe) {
			LOGGER.warn("Unable to save config", ioe);
		}
	}
}
