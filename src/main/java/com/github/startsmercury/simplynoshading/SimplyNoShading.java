package com.github.startsmercury.simplynoshading;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.startsmercury.simplynoshading.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;

public final class SimplyNoShading {
	public record BakedConfig(boolean shading) {
		public BakedConfig(final JsonElement jsonElement) {
			this(JsonUtil.getBooleanOrDefault(jsonElement, "shading", false));
		}
	}

	private static BakedConfig bakedConfig;

	private static JsonElement config;

	private static final Path CONFIG_PATH;

	private static final Gson GSON;

	private static final Logger LOGGER;

	static {
		CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("simply-no-shading.json");
		GSON = new GsonBuilder().setPrettyPrinting().create();
		LOGGER = LogManager.getLogger("simply-no-shading");

		loadConfig();
		bakeConfig();

		Runtime.getRuntime().addShutdownHook(new Thread(SimplyNoShading::saveConfig));
	}

	public static void bakeConfig() {
		bakedConfig = new BakedConfig(config);
	}

	public static JsonObject defaultConfig() {
		final JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("enabled", true);

		return jsonObject;
	}

	public static BakedConfig getBakedConfig() {
		return bakedConfig;
	}

	public static JsonElement getConfig() {
		return config;
	}

	public static Logger getLogger() {
		return LOGGER;
	}

	public static void loadConfig() {
		try {
			config = GSON.fromJson(Files.readString(CONFIG_PATH), JsonElement.class);
		} catch (final IOException ioe) {
			config = defaultConfig();

			saveConfig();
		}
	}

	public static void saveConfig() {
		try {
			Files.writeString(CONFIG_PATH, GSON.toJson(config));
		} catch (final IOException ioe) {
			LOGGER.catching(Level.WARN, new RuntimeException("Unable to save config.", ioe));
		}
	}

	public static void setConfig(final JsonElement config) {
		SimplyNoShading.config = config == null ? defaultConfig() : config;
	}

	private SimplyNoShading() {
	}
}
