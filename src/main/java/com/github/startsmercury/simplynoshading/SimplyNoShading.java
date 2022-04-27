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
import com.github.startsmercury.simplynoshading.config.SimplyNoShadingMixinConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Encapsulates Simply No Shading's name spaces, loggers, config, config paths,
 * and config operations.
 */
public final class SimplyNoShading {
	/**
	 * Name space of Simply No Shading used as logger name and in resource
	 * locations.
	 *
	 * @see #LOGGER
	 */
	public static final String MODID = "simply-no-shading";

	/**
	 * Name space of Simply No Shading (Client) used as logger name and in resource
	 * locations.
	 *
	 * @see #CLIENT_LOGGER
	 */
	@Environment(CLIENT)
	public static final String CLIENT_MODID = MODID + "+client";

	/**
	 * Name space of Simply No Shading (Mixin) used as logger name and in resource
	 * locations.
	 */
	public static final String MIXIN_MODID = "simply-no-shading";

	/**
	 * Simply No Shading (Client)'s logger used to print messages.
	 *
	 * @see #CLIENT_MODID
	 */
	@Environment(CLIENT)
	public static final Logger CLIENT_LOGGER = LoggerFactory.getLogger(CLIENT_MODID);

	/**
	 * Simply No Shading (Mixin)'s logger used to print messages.
	 *
	 * @see #MIXIN_MODID
	 */
	public static final Logger MIXIN_LOGGER = LoggerFactory.getLogger(MIXIN_MODID);

	/**
	 * Simply No Shading (Client)'s config path.
	 *
	 * @see #getClientConfig()
	 * @see #setClientConfig(SimplyNoShadingClientConfig)
	 */
	@Environment(CLIENT)
	public static final Path CLIENT_CONFIG_PATH;

	/**
	 * Simply No Shading (Mixin)'s config path.
	 *
	 * @see #getClientConfig()
	 * @see #setClientConfig(SimplyNoShadingClientConfig)
	 */
	public static final Path MIXIN_CONFIG_PATH;

	/**
	 * Simply No Shading configuration file paths' initializer.
	 */
	static {
		final Path configDir = FabricLoader.getInstance().getConfigDir();

		CLIENT_CONFIG_PATH = configDir.resolve(CLIENT_MODID + ".json");
		MIXIN_CONFIG_PATH = configDir.resolve(MIXIN_MODID + ".json");
	}

	/**
	 * {@link Gson} instance used in configuration serialization.
	 */
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * Simply No Shading (Client)'s config.
	 */
	@Environment(CLIENT)
	private static SimplyNoShadingClientConfig clientConfig;

	/**
	 * Simply No Shading (Mixin)'s config.
	 */
	private static SimplyNoShadingMixinConfig mixinConfig;

	/**
	 * Returns Simply No Shading (Client)'s config.
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
	 * Returns Simply No Shading (Client)'s config.
	 *
	 * @return the client config
	 */
	public static SimplyNoShadingMixinConfig getMixinConfig() {
		if (mixinConfig == null) {
			loadMixinConfig();
		}

		return mixinConfig;
	}

	/**
	 * Loads any config.
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
	 * Loads Simply No Shading (Client)'s config.
	 */
	@Environment(CLIENT)
	public static void loadClientConfig() {
		loadAnyConfig(CLIENT_LOGGER, CLIENT_CONFIG_PATH, SimplyNoShading::setClientConfig,
				SimplyNoShadingClientConfig.class, SimplyNoShadingClientConfig::new, SimplyNoShading::getClientConfig);
	}

	/**
	 * Loads Simply No Shading (Mixin)'s config.
	 */
	public static void loadMixinConfig() {
		loadAnyConfig(MIXIN_LOGGER, MIXIN_CONFIG_PATH, SimplyNoShading::setMixinConfig,
				SimplyNoShadingMixinConfig.class, SimplyNoShadingMixinConfig::new, SimplyNoShading::getMixinConfig);
	}

	/**
	 * Saves any config.
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
	 * Saves Simply No Shading (Mixin)'s config.
	 */
	@Environment(CLIENT)
	public static void saveClientConfig() {
		saveAnyConfig(CLIENT_LOGGER, false, CLIENT_CONFIG_PATH, SimplyNoShading::getClientConfig,
				SimplyNoShadingClientConfig.class);
	}

	/**
	 * Saves Simply No Shading (Mixin)'s config.
	 */
	public static void saveMixinConfig() {
		saveAnyConfig(MIXIN_LOGGER, false, MIXIN_CONFIG_PATH, SimplyNoShading::getMixinConfig,
				SimplyNoShadingMixinConfig.class);
	}

	/**
	 * Sets Simply No Shading (Client)'s config.
	 *
	 * @param clientConfig the new client config
	 */
	@Environment(CLIENT)
	public static void setClientConfig(final SimplyNoShadingClientConfig clientConfig) {
		SimplyNoShading.clientConfig = clientConfig;
	}

	/**
	 * Sets Simply No Shading (Mixin)'s config.
	 *
	 * @param mixinConfig the new mixin config
	 */
	public static void setMixinConfig(final SimplyNoShadingMixinConfig mixinConfig) {
		SimplyNoShading.mixinConfig = mixinConfig;
	}
}
