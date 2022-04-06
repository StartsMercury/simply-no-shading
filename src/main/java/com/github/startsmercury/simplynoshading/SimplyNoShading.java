package com.github.startsmercury.simplynoshading;

import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static net.fabricmc.api.EnvType.CLIENT;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.startsmercury.simplynoshading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simplynoshading.config.SimplyNoShadingClientConfigJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Collection of identifiers, loggers, paths, and configurations used by Simply
 * No Shading.
 */
public final class SimplyNoShading {
	/**
	 * The identifier of Simply No Shading.
	 *
	 * @see #LOGGER
	 */
	public static final String MODID = "simply-no-shading";

	/**
	 * The identifier of Simply No Shading use by the client logger as name.
	 *
	 * @see #CLIENT_LOGGER
	 */
	private static final String CLIENT_MODID = MODID + "+client";

	/**
	 * The logger of Simply No Shading used in client-side environments.
	 *
	 * @see #CLIENT_MODID
	 */
	@Environment(CLIENT)
	public static final Logger CLIENT_LOGGER = LoggerFactory.getLogger(CLIENT_MODID);

	/**
	 * The path to Simply No Shading client configuration file.
	 *
	 * @see #getClientConfig()
	 * @see #setClientConfig(SimplyNoShadingClientConfig)
	 */
	public static final Path CLIENT_CONFIG_PATH;

	/**
	 * Simply No Shading configuration file paths' initializer.
	 */
	static {
		final Path configDir = FabricLoader.getInstance().getConfigDir();

		CLIENT_CONFIG_PATH = configDir.resolve(CLIENT_MODID + ".json");
	}

	/**
	 * {@link Gson} instance used in configuration serialization.
	 */
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * The client configuration for Simply No Shading.
	 */
	@Environment(CLIENT)
	private static SimplyNoShadingClientConfig clientConfig;

	/**
	 * Returns the client configuration for Simply No Shading
	 *
	 * @return the client config
	 */
	@Environment(CLIENT)
	public static SimplyNoShadingClientConfig getClientConfig() {
		if (clientConfig == null) {
			loadClientConfig();
		}

		return clientConfig;
	}

	/**
	 * Generically loads a json parsed object with complete logging and fallback.
	 *
	 * @param <T>      the type
	 * @param logger   the logger
	 * @param path     the path
	 * @param setter   the setter
	 * @param clazz    the class
	 * @param fallback the fallback
	 * @param getter   the getter
	 */
	private static <T> void loadAnyConfig(final Logger logger, final Path path, final Consumer<? super T> setter,
			final Class<T> clazz, final Supplier<? extends T> fallback, final Supplier<? extends T> getter) {
		logger.debug("Loading config...");

		try (final BufferedReader in = newBufferedReader(path)) {
			setter.accept(GSON.fromJson(in, clazz));

			logger.info("Config loaded.");
		} catch (final NoSuchFileException nsfe) {
			setter.accept(fallback.get());

			saveAnyConfig(logger, true, path, getter, clazz);
		} catch (final IOException ioe) {
			logger.warn("Unable to load config.", ioe);
			logger.debug("Loading default config...");

			setter.accept(fallback.get());

			logger.info("Default config loaded.");
		}
	}

	/**
	 * Loads the client configuration.
	 */
	@Environment(CLIENT)
	public static void loadClientConfig() {
		loadAnyConfig(CLIENT_LOGGER, CLIENT_CONFIG_PATH, SimplyNoShading::setClientConfig,
				SimplyNoShadingClientConfig.class, SimplyNoShadingClientConfigJson::new,
				SimplyNoShading::getClientConfig);
	}

	/**
	 * Generically saves a json parsed object with complete logging.
	 *
	 * @param <T>    the type
	 * @param logger the logger
	 * @param create
	 * @param path   the path
	 * @param getter the getter
	 * @param clazz  the class
	 */
	private static <T> void saveAnyConfig(final Logger logger, final boolean create, final Path path,
			final Supplier<? extends T> getter, final Class<T> clazz) {
		logger.debug((create ? "Creating" : "Saving") + " config...");

		try (final BufferedWriter out = newBufferedWriter(path, create ? CREATE_NEW : CREATE)) {
			GSON.toJson(getter.get(), clazz, out);

			logger.info("Config " + (create ? "created" : "saved") + '.');
		} catch (final IOException ioe) {
			logger.warn("Unable to " + (create ? "create" : "save") + " config.", ioe);
		}
	}

	/**
	 * Saves the client configuration.
	 */
	@Environment(CLIENT)
	public static void saveClientConfig() {
		saveAnyConfig(CLIENT_LOGGER, false, CLIENT_CONFIG_PATH, SimplyNoShading::getClientConfig,
				SimplyNoShadingClientConfig.class);
	}

	/**
	 * Sets the client configuration for Simply No Shading.
	 *
	 * @param clientConfig the new client config
	 */
	@Environment(CLIENT)
	public static void setClientConfig(final SimplyNoShadingClientConfig clientConfig) {
		SimplyNoShading.clientConfig = clientConfig;
	}
}
