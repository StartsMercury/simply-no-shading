package com.github.startsmercury.simply.no.shading;

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

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingMixinConfig;
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
	 * Simply No Shading (Client)'s config path.
	 *
	 * @see #getClientConfig()
	 * @see #setClientConfig(SimplyNoShadingClientConfig)
	 */
	@Environment(CLIENT)
	public static final Path CLIENT_CONFIG_PATH;

	/**
	 * Simply No Shading (Client)'s logger used to print messages.
	 *
	 * @see #CLIENT_MODID
	 */
	@Environment(CLIENT)
	public static final Logger CLIENT_LOGGER;

	/**
	 * Name space of Simply No Shading (Client) used as logger name and in resource
	 * locations.
	 *
	 * @see #CLIENT_LOGGER
	 */
	@Environment(CLIENT)
	public static final String CLIENT_MODID;

	/**
	 * Simply No Shading (Client)'s config.
	 */
	@Environment(CLIENT)
	public static final SimplyNoShadingClientConfig CLIENT_CONFIG;

	/**
	 * {@link Gson} instance used in configuration serialization.
	 */
	public static final Gson GSON;

	/**
	 * Simply No Shading (Mixin)'s config path.
	 *
	 * @see #getClientConfig()
	 * @see #setClientConfig(SimplyNoShadingClientConfig)
	 */
	public static final Path MIXIN_CONFIG_PATH;

	/**
	 * Simply No Shading (Mixin)'s logger used to print messages.
	 *
	 * @see #MIXIN_MODID
	 */
	public static final Logger MIXIN_LOGGER;

	/**
	 * Name space of Simply No Shading (Mixin) used as logger name and in resource
	 * locations.
	 */
	public static final String MIXIN_MODID;

	/**
	 * Simply No Shading (Mixin)'s config.
	 */
	public static final SimplyNoShadingMixinConfig MIXIN_CONFIG;

	/**
	 * Name space of Simply No Shading used as logger name and in resource
	 * locations.
	 *
	 * @see #LOGGER
	 */
	public static final String MODID;

	static {
		final var fabricLoader = FabricLoader.getInstance();

		CLIENT_CONFIG = new SimplyNoShadingClientConfig();
		GSON = new GsonBuilder().setPrettyPrinting().create();
		MODID = "simply-no-shading";
		MIXIN_CONFIG = new SimplyNoShadingMixinConfig();
		final var configDir = fabricLoader.getConfigDir();

		CLIENT_MODID = MODID + "+client";
		MIXIN_MODID = MODID + "+mixin";

		CLIENT_CONFIG_PATH = configDir.resolve(CLIENT_MODID + ".json");
		CLIENT_LOGGER = LoggerFactory.getLogger(CLIENT_MODID);
		MIXIN_CONFIG_PATH = configDir.resolve(MIXIN_MODID + ".json");
		MIXIN_LOGGER = LoggerFactory.getLogger(MIXIN_MODID);
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
			final Class<T> clazz, final Supplier<? extends T> fallback, final T config) {
		logger.debug("Loading config...");

		try (final BufferedReader in = newBufferedReader(path)) {
			setter.accept(GSON.fromJson(in, clazz));

			logger.info("Config loaded.");
		} catch (final NoSuchFileException nsfe) {
			setter.accept(fallback.get());

			saveAnyConfig(logger, true, path, config, clazz);
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
		loadAnyConfig(CLIENT_LOGGER, CLIENT_CONFIG_PATH, CLIENT_CONFIG::set, SimplyNoShadingClientConfig.class,
				SimplyNoShadingClientConfig::new, CLIENT_CONFIG);
	}

	/**
	 * Loads Simply No Shading (Mixin)'s config.
	 */
	public static void loadMixinConfig() {
		loadAnyConfig(MIXIN_LOGGER, MIXIN_CONFIG_PATH, MIXIN_CONFIG::set, SimplyNoShadingMixinConfig.class,
				SimplyNoShadingMixinConfig::new, MIXIN_CONFIG);
	}

	/**
	 * Saves any config.
	 *
	 * @param <T>    the type
	 * @param logger the logger
	 * @param create
	 * @param path   the path
	 * @param config the getter
	 * @param clazz  the class
	 */
	private static <T> void saveAnyConfig(final Logger logger, final boolean create, final Path path, final T config,
			final Class<T> clazz) {
		logger.debug((create ? "Creating" : "Saving") + " config...");

		try (final BufferedWriter out = newBufferedWriter(path, create ? CREATE_NEW : CREATE)) {
			GSON.toJson(config, clazz, out);

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
		saveAnyConfig(CLIENT_LOGGER, false, CLIENT_CONFIG_PATH, CLIENT_CONFIG, SimplyNoShadingClientConfig.class);
	}

	/**
	 * Saves Simply No Shading (Mixin)'s config.
	 */
	public static void saveMixinConfig() {
		saveAnyConfig(MIXIN_LOGGER, false, MIXIN_CONFIG_PATH, MIXIN_CONFIG, SimplyNoShadingMixinConfig.class);
	}
}
