package com.github.startsmercury.simplynoshading.entrypoint;

import static com.github.startsmercury.simplynoshading.SimplyNoShading.LOGGER;
import static com.github.startsmercury.simplynoshading.SimplyNoShading.loadConfig;

import net.fabricmc.api.ModInitializer;

@Deprecated
public final class SimplyNoShadingMod implements ModInitializer {
	private static SimplyNoShadingMod instance;

	public static SimplyNoShadingMod getInstance() {
		return instance;
	}

	public SimplyNoShadingMod() {
		LOGGER.debug("Initializer constructed.");
	}

	@Override
	public void onInitialize() {
		LOGGER.debug("Mod initializing...");

		instance = this;

		loadConfig();

		LOGGER.info("Mod initialized.");
		LOGGER.warn("Servers are not supported.");
	}
}
