package com.github.startsmercury.simply_no_shading.entrypoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

/**
 * Simply No Shading Mod's client-side initializer.
 *
 * @see com.github.startsmercury.simply_no_shading.entrypoint
 * @see ClientModInitialize
 * @see #onInitializeClient()
 */
@Environment(EnvType.CLIENT)
public class SimplyNoShadingClient implements ClientModInitializer {
	/**
	 * Simply No Shading Mod's {@link Logger logger}.
	 *
	 * @see #MOD_NAME
	 * @see Logger
	 * @see LogManager#getLogger(String)
	 */
	public static final Logger LOGGER;

	/**
	 * Latest created instance of this class.
	 *
	 * @see #onInitializeClient()
	 */
	private static SimplyNoShadingClient instance;

	/**
	 * Simply No Shading Mod's {@link ModContainer Container} loaded by
	 * {@link FabricLoader fabric}.
	 *
	 * @see #MOD_METADATA
	 * @see ModContainer
	 * @see FabricLoader#getModContainer(String)
	 */
	public static final ModContainer MOD_CONTAINER;

	/**
	 * Simply No Shading Mod's id.
	 *
	 * @see #MOD_METADATA
	 * @see ModMetadata#getId()
	 */
	public static final String MOD_ID;

	/**
	 * Simply No Shading Mod's {@link ModMetadata metadata}.
	 *
	 * @see #MOD_CONTAINER
	 * @see #MOD_ID
	 * @see #MOD_NAME
	 * @see ModContainer#getMetadata()
	 */
	public static final ModMetadata MOD_METADATA;

	/**
	 * Simply No Shading Mod's display name.
	 *
	 * @see #LOGGER
	 * @see #MOD_METADATA
	 * @see ModMetadata#getName()
	 */
	public static final String MOD_NAME;

	/**
	 * Simply No Shading Mod's constant initializer.
	 */
	static {
		MOD_ID = "simply-no-shading";
		MOD_CONTAINER = FabricLoader.getInstance().getModContainer(MOD_ID)
				.orElseThrow(() -> new IllegalStateException("Mod of id '" + MOD_ID + "' must be present."));
		MOD_METADATA = MOD_CONTAINER.getMetadata();
		MOD_NAME = MOD_METADATA.getName();
		LOGGER = LogManager.getLogger(MOD_NAME);
	}

	/**
	 * @return {@link #instance}
	 */
	public static SimplyNoShadingClient getInstance() {
		return instance;
	}

	/**
	 * @return {@link #LOGGER}
	 */
	public static Logger getLogger() {
		return LOGGER;
	}

	/**
	 * @return {@link #MOD_CONTAINER}
	 */
	public static ModContainer getModContainer() {
		return MOD_CONTAINER;
	}

	/**
	 * @return {@link #MOD_ID}
	 */
	public static String getModId() {
		return MOD_ID;
	}

	/**
	 * @return {@link #MOD_METADATE}
	 */
	public static ModMetadata getModMetadata() {
		return MOD_METADATA;
	}

	/**
	 * @return {@link #MOD_NAME}
	 */
	public static String getModName() {
		return MOD_NAME;
	}

	/**
	 * Initializes Simply No Shading Mod.
	 *
	 * @see #instance
	 * @see #LOGGER
	 */
	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing...");

		if (instance != null) {
			LOGGER.debug("Multi-instanced!");
		}

		instance = this;

		LOGGER.debug("Initialized.");
	}
}
