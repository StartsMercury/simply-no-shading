package com.github.startsmercury.simply.no.shading.api;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newBufferedWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.client.Minecraft;

public interface CommonMod {
	final class Utils {
		public static <T> T loadJsonConfig(final Class<T> clazz, final T currentConfig, final Gson gson, final Logger logger,
				final Path path) {
			logger.debug("Loading config...");

			try (var in = newBufferedReader(path)) {
				final var newConfig = GSON.fromJson(in, clazz);

				logger.debug("Loaded Config");

				return newConfig;
			} catch (final NoSuchFileException nsfe) {
				saveJsonConfig(true, gson, logger, currentConfig, path);
			} catch (final IOException ioe) {
				logger.warn("Unable to read config", ioe);
			}

			return currentConfig;
		}

		public static void saveJsonConfig(final boolean create, final Gson gson, final Logger logger, final Object obj,
				final Path path) {
			logger.debug(create ? "Creating config..." : "Saving config...");

			try (var out = newBufferedWriter(path)) {
				GSON.toJson(obj, out);

				logger.info(create ? "Config created" : "Config saved");
			} catch (final IOException ioe) {
				logger.warn(create ? "Unable to create config" : "Unable to save config", ioe);
			}
		}
	}

	Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	Object getConfig();

	default Path getConfigPath() {
		@SuppressWarnings("resource")
		final var configDirectory = Minecraft.getInstance().gameDirectory.toPath().resolve("config");

		if (!exists(configDirectory)) {
			try {
				Files.createDirectories(configDirectory);
			} catch (final IOException ioe) {
				getLogger().warn("Unable to create config directory", ioe);
			}
		}

		return configDirectory.resolve(getName() + ".json");
	}

	String getIdentifier();

	Object getInitializer();

	default Logger getLogger() {
		return LoggerFactory.getLogger(getName());
	}

	String getName();

	boolean isClientSided();

	boolean isServerSided();

	void loadConfig();

	default void saveConfig() {
		Utils.saveJsonConfig(false, GSON, getLogger(), getConfig(), getConfigPath());
	}
}
