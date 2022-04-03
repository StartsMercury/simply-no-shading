package com.github.startsmercury.simplynoshading;

import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newBufferedWriter;
import static net.fabricmc.api.EnvType.CLIENT;
import static net.fabricmc.api.EnvType.SERVER;

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
import com.github.startsmercury.simplynoshading.logging.SimplyNoShadingDefferedLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@SuppressWarnings("deprecation")
public final class SimplyNoShading {
	public static final String MODID = "simply-no-shading";

	private static final String CLIENT_MODID = MODID + "+client";

	private static final String SERVER_MODID = MODID + "+server";

	@Deprecated
	public static final Logger LOGGER = new SimplyNoShadingDefferedLogger(MODID);

	@Environment(CLIENT)
	public static final Logger CLIENT_LOGGER = new SimplyNoShadingDefferedLogger(CLIENT_MODID);

	@Deprecated
	@Environment(SERVER)
	public static final Logger SERVER_LOGGER = new SimplyNoShadingDefferedLogger(SERVER_MODID);

	public static final Path CONFIG_PATH;

	public static final Path CLIENT_CONFIG_PATH;

	public static final Path SERVER_CONFIG_PATH;

	static {
		final Path configDir = FabricLoader.getInstance().getConfigDir();

		CONFIG_PATH = configDir.resolve(MODID + ".json");
		CLIENT_CONFIG_PATH = configDir.resolve(CLIENT_MODID + ".json");
		SERVER_CONFIG_PATH = configDir.resolve(SERVER_MODID + ".json");
	}

	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	@Deprecated
	private static SimplyNoShadingConfig config;

	@Environment(CLIENT)
	private static SimplyNoShadingClientConfig clientConfig;

	@Deprecated
	@Environment(SERVER)
	private static SimplyNoShadingServerConfig serverConfig;

	@Environment(CLIENT)
	public static SimplyNoShadingClientConfig getClientConfig() {
		if (clientConfig == null) {
			loadClientConfig();
		}

		return clientConfig;
	}

	@Deprecated
	public static SimplyNoShadingConfig getConfig() {
		if (config == null) {
			loadConfig();
		}

		return config;
	}

	@Deprecated
	@Environment(SERVER)
	public static SimplyNoShadingServerConfig getServerConfig() {
		if (serverConfig == null) {
			loadServerConfig();
		}

		return serverConfig;
	}

	private static <T> void loadAnyConfig(final Logger logger, final Consumer<? super T> setter, final Path path,
			final Class<T> clazz, final Supplier<? extends T> fallback, final Supplier<? extends T> getter) {
		logger.debug("Config loading...");

		try {
			setter.accept(GSON.fromJson(newBufferedReader(path), clazz));

			logger.info("Config loaded.");
		} catch (final NoSuchFileException nsfe) {
			logger.info("Config does not exist, creating one...");

			setter.accept(fallback.get());

			saveAnyConfig(logger, getter, path);
		} catch (final IOException ioe) {
			logger.warn("Config loading halted.", ioe);
		}
	}

	@Environment(CLIENT)
	public static void loadClientConfig() {
		loadAnyConfig(CLIENT_LOGGER, SimplyNoShading::setClientConfig, CLIENT_CONFIG_PATH,
				SimplyNoShadingClientConfig.class, SimplyNoShadingClientConfigJson::new,
				SimplyNoShading::getClientConfig);
	}

	@Deprecated
	public static void loadConfig() {
		loadAnyConfig(LOGGER, SimplyNoShading::setConfig, CONFIG_PATH, SimplyNoShadingConfig.class,
				SimplyNoShadingConfigJson::new, SimplyNoShading::getConfig);
	}

	@Deprecated
	@Environment(SERVER)
	public static void loadServerConfig() {
		loadAnyConfig(SERVER_LOGGER, SimplyNoShading::setServerConfig, SERVER_CONFIG_PATH,
				SimplyNoShadingServerConfig.class, SimplyNoShadingServerConfigJson::new,
				SimplyNoShading::getServerConfig);
	}

	private static void saveAnyConfig(final Logger logger, final Supplier<?> getter, final Path path) {
		logger.debug("Config saving...");

		try {
			GSON.toJson(getter.get(), newBufferedWriter(path));

			logger.info("Config saved.");
		} catch (final IOException ioe) {
			logger.warn("Config saving halted.", ioe);
		}
	}

	@Environment(CLIENT)
	public static void saveClientConfig() {
		saveAnyConfig(CLIENT_LOGGER, SimplyNoShading::getClientConfig, CLIENT_CONFIG_PATH);
	}

	@Deprecated
	public static void saveConfig() {
		saveAnyConfig(LOGGER, SimplyNoShading::getConfig, CONFIG_PATH);
	}

	@Deprecated
	@Environment(SERVER)
	public static void saveServerConfig() {
		saveAnyConfig(SERVER_LOGGER, SimplyNoShading::getServerConfig, SERVER_CONFIG_PATH);
	}

	@Environment(CLIENT)
	public static void setClientConfig(final SimplyNoShadingClientConfig clientConfig) {
		SimplyNoShading.clientConfig = clientConfig;
	}

	@Deprecated
	public static void setConfig(final SimplyNoShadingConfig config) {
		SimplyNoShading.config = config;
	}

	@Deprecated
	@Environment(SERVER)
	public static void setServerConfig(final SimplyNoShadingServerConfig serverConfig) {
		SimplyNoShading.serverConfig = serverConfig;
	}
}
