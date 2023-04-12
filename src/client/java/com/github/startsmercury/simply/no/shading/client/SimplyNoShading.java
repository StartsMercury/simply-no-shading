package com.github.startsmercury.simply.no.shading.client;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Objects;

import org.slf4j.Logger;

import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientEntrypoint;
import com.github.startsmercury.simply.no.shading.util.PrefixedLogger;
import com.github.startsmercury.simply.no.shading.util.storage.JsonPathStorage;
import com.github.startsmercury.simply.no.shading.util.storage.Storage;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

/**
 * The {@code SimplyNoShading} class models the Simply No Shading mod. It
 * contains the config, allows changing the config, and loading and saving of
 * the config. The model does not directly interact with the game, aside from
 * {@link #setConfig(Config)} which reloads the level when a change is detected.
 * Coupling this class with the base game is the responsibility of
 * {@link SimplyNoShadingClientEntrypoint} (and the mixins).
 *
 * @since 6.0.0
 */
public class SimplyNoShading {
	/**
	 * The first instance of the {@code SimplyNoShading} class.
	 */
	private static SimplyNoShading firstInstance;

	/**
	 * The category key for key mappings.
	 */
	public static final String KEY_CATEGORY = "key.categories.simply-no-shading";

	/**
	 * This mod's logger.
	 */
	public static final Logger LOGGER = PrefixedLogger.named("simply-no-shading", "[SimplyNoShading] ");

	/**
	 * Sets the first instance if there's not one set.
	 *
	 * @param instance the instance to try set as first
	 */
	private static void computeFirstInstanceIfAbsent(final SimplyNoShading instance) {
		if (firstInstance == null)
			firstInstance = instance;
	}

	/**
	 * Tries to get the config directory by using the {@linkplain FabricLoader
	 * fabric loader}. When called before the fabric loader is initialized, it'll
	 * instead return the current working directory.
	 *
	 * @return the config directory
	 */
	private static Path getConfigDirectory() {
		try {
			final var fabricLoader = FabricLoader.getInstance();
			final var configDirectory = fabricLoader.getConfigDir();

			return configDirectory;
		} catch (final RuntimeException re) { // FabricLoader is not yet loaded
			return Path.of("/");
		}
	}

	/**
	 * Returns the default config path.
	 *
	 * @return the default config path
	 * @see #getConfigDirectory()
	 */
	private static Path getDefaultConfigPath() {
		final var configDirectory = SimplyNoShading.getConfigDirectory();
		final var configPath = configDirectory.resolve("simply-no-shading.json");

		return configPath;
	}

	/**
	 * Returns the first instance of the {@code SimplyNoShading} class.
	 *
	 * @return the first instance of the {@code SimplyNoShading} class
	 */
	public static SimplyNoShading getFirstInstance() {
		return firstInstance;
	}

	/**
	 * The config is responsible in storing the states that may modify the behavior
	 * of the mod.
	 */
	private Config config;

	/**
	 * The config storage dictates where the {@link #config} should be stored, most
	 * likely in a persistent file.
	 */
	private Storage<Config> configStorage;

	/**
	 * Creates a new {@code SimplyNoShading} instance.
	 */
	public SimplyNoShading() {
		this.config = Config.INTERNAL_SHADERS;
		this.configStorage = new JsonPathStorage<>(getDefaultConfigPath(),
		        new GsonBuilder().setPrettyPrinting().create(),
		        Config.class);

		computeFirstInstanceIfAbsent(this);
	}

	/**
	 * Returns the config. It is responsible in storing the states that may modify
	 * the behavior of the mod
	 *
	 * @return the config
	 */
	public Config getConfig() {
		return this.config;
	}

	/**
	 * Returns the config storage. It dictates where the {@link #getConfig() config}
	 * should be stored, most likely in a persistent file.
	 *
	 * @return the config storage
	 */
	public Storage<Config> getConfigStorage() {
		return this.configStorage;
	}

	/**
	 * Loads the config from the {@link #getConfigStorage() config storage} logging
	 * any errors caught.
	 */
	public void loadConfig() {
		try {
			setConfig(getConfigStorage().load());
		} catch (final NoSuchFileException nsfe) {
			saveConfig();
		} catch (final Exception e) {
			LOGGER.warn("Unable to load config", e);
		}
	}

	/**
	 * Saves the config to the {@link #getConfigStorage() config storage} logging
	 * any errors caught.
	 */
	public void saveConfig() {
		try {
			getConfigStorage().save(getConfig());
		} catch (final Exception e) {
			LOGGER.warn("Unable to save config", e);
			e.printStackTrace();
		}
	}

	/**
	 * Sets a new config. It is responsible in storing the states that may modify
	 * the behavior of the mod
	 *
	 * @param config the new config
	 */
	public void setConfig(final Config config) {
		Objects.requireNonNull(config, "Parameter config was null");

		if (this.config.equals(config))
			return;

		this.config = config;

		final var minecraft = Minecraft.getInstance();
		if (minecraft.levelRenderer != null)
			minecraft.levelRenderer.allChanged();
	}

	/**
	 * Sets a new config storage. It dictates where the {@link #getConfig() config}
	 * should be stored, most likely in a persistent file.
	 *
	 * @param configStorage the new config storage
	 */
	public void setConfigStorage(final Storage<Config> configStorage) {
		Objects.requireNonNull(configStorage, "Parameter configStorage was null");

		this.configStorage = configStorage;
	}
}
