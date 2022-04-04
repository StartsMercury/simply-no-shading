package com.github.startsmercury.simplynoshading;

import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static net.fabricmc.api.EnvType.CLIENT;
import static net.fabricmc.api.EnvType.SERVER;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;

import com.github.startsmercury.simplynoshading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simplynoshading.config.SimplyNoShadingClientConfigJson;
import com.github.startsmercury.simplynoshading.config.SimplyNoShadingConfig;
import com.github.startsmercury.simplynoshading.config.SimplyNoShadingConfigJson;
import com.github.startsmercury.simplynoshading.config.SimplyNoShadingServerConfig;
import com.github.startsmercury.simplynoshading.config.SimplyNoShadingServerConfigJson;
import com.github.startsmercury.simplynoshading.util.SimplyNoShadingDefferedLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Collection of identifiers, loggers, paths, and configurations used by Simply
 * No Shading.
 */
@SuppressWarnings("deprecation")
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
	 * The identifier of Simply No Shading use by the server logger as name.
	 *
	 * @see #SERVER_LOGGER
	 */
	private static final String SERVER_MODID = MODID + "+server";

	/**
	 * The logger of Simply No Shading.
	 *
	 * @see #MODID
	 */
	@Deprecated
	public static final Logger LOGGER = new SimplyNoShadingDefferedLogger(MODID);

	/**
	 * The logger of Simply No Shading used in client-side environments.
	 *
	 * @see #CLIENT_MODID
	 */
	@Environment(CLIENT)
	public static final Logger CLIENT_LOGGER = new SimplyNoShadingDefferedLogger(CLIENT_MODID);

	/**
	 * The logger of Simply No Shading used in server-side environments.
	 *
	 * @see #SERVER_MODID
	 */
	@Deprecated
	@Environment(SERVER)
	public static final Logger SERVER_LOGGER = new SimplyNoShadingDefferedLogger(SERVER_MODID);

	/**
	 * The path to Simply No Shading configuration file.
	 *
	 * @see #getConfig()
	 * @see #setConfig(SimplyNoShadingConfig)
	 */
	public static final Path CONFIG_PATH;

	/**
	 * The path to Simply No Shading client configuration file.
	 *
	 * @see #getClientConfig()
	 * @see #setClientConfig(SimplyNoShadingClientConfig)
	 */
	public static final Path CLIENT_CONFIG_PATH;

	/**
	 * The path to Simply No Shading server configuration file.
	 *
	 * @see #getServerConfig()
	 * @see #setServerConfig(SimplyNoShadingServerConfig)
	 */
	public static final Path SERVER_CONFIG_PATH;

	/**
	 * Simply No Shading configuration file paths' initializer.
	 */
	static {
		final Path configDir = FabricLoader.getInstance().getConfigDir();

		CONFIG_PATH = configDir.resolve(MODID + ".json");
		CLIENT_CONFIG_PATH = configDir.resolve(CLIENT_MODID + ".json");
		SERVER_CONFIG_PATH = configDir.resolve(SERVER_MODID + ".json");
	}

	/**
	 * {@link Gson} instance used in configuration serialization.
	 */
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * The configuration for Simply No Shading.
	 */
	@Deprecated
	private static SimplyNoShadingConfig config;

	/**
	 * The client configuration for Simply No Shading.
	 */
	@Environment(CLIENT)
	private static SimplyNoShadingClientConfig clientConfig;

	/**
	 * The server configuration for Simply No Shading.
	 */
	@Deprecated
	@Environment(SERVER)
	private static SimplyNoShadingServerConfig serverConfig;

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
	 * Returns the configuration for Simply No Shading
	 *
	 * @return the config
	 */
	@Deprecated
	public static SimplyNoShadingConfig getConfig() {
		if (config == null) {
			loadConfig();
		}

		return config;
	}

	/**
	 * Returns the server configuration for Simply No Shading
	 *
	 * @return the server config
	 */
	@Deprecated
	@Environment(SERVER)
	public static SimplyNoShadingServerConfig getServerConfig() {
		if (serverConfig == null) {
			loadServerConfig();
		}

		return serverConfig;
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
	 * Loads the configuration.
	 */
	@Deprecated
	public static void loadConfig() {
		loadAnyConfig(LOGGER, CONFIG_PATH, SimplyNoShading::setConfig, SimplyNoShadingConfig.class,
				SimplyNoShadingConfigJson::new, SimplyNoShading::getConfig);
	}

	/**
	 * Loads the server configuration.
	 */
	@Deprecated
	@Environment(SERVER)
	public static void loadServerConfig() {
		loadAnyConfig(SERVER_LOGGER, SERVER_CONFIG_PATH, SimplyNoShading::setServerConfig,
				SimplyNoShadingServerConfig.class, SimplyNoShadingServerConfigJson::new,
				SimplyNoShading::getServerConfig);
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
	 * Saves the configuration.
	 */
	@Deprecated
	public static void saveConfig() {
		saveAnyConfig(LOGGER, false, CONFIG_PATH, SimplyNoShading::getConfig, SimplyNoShadingConfig.class);
	}

	/**
	 * Saves the server configuration.
	 */
	@Deprecated
	@Environment(SERVER)
	public static void saveServerConfig() {
		saveAnyConfig(SERVER_LOGGER, false, SERVER_CONFIG_PATH, SimplyNoShading::getServerConfig,
				SimplyNoShadingServerConfig.class);
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

	/**
	 * Sets the client configuration for Simply No Shading.
	 *
	 * @param config the new config
	 */
	@Deprecated
	public static void setConfig(final SimplyNoShadingConfig config) {
		SimplyNoShading.config = config;
	}

	/**
	 * Sets the client configuration for Simply No Shading.
	 *
	 * @param serverConfig the new server config
	 */
	@Deprecated
	@Environment(SERVER)
	public static void setServerConfig(final SimplyNoShadingServerConfig serverConfig) {
		SimplyNoShading.serverConfig = serverConfig;
	}
}
