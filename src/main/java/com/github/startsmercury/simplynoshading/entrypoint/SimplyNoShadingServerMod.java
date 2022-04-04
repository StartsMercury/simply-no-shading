package com.github.startsmercury.simplynoshading.entrypoint;

import static com.github.startsmercury.simplynoshading.SimplyNoShading.SERVER_LOGGER;
import static com.github.startsmercury.simplynoshading.SimplyNoShading.loadServerConfig;
import static net.fabricmc.api.EnvType.SERVER;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;

@Deprecated
@Environment(SERVER)
public final class SimplyNoShadingServerMod implements ClientModInitializer {
	private static SimplyNoShadingServerMod instance;

	public static SimplyNoShadingServerMod getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Accessed SimplyNoShadingServerMod too early");
		}

		return instance;
	}

	public SimplyNoShadingServerMod() {
		SERVER_LOGGER.debug("Initializer constructed.");
	}

	@Override
	public void onInitializeClient() {
		SERVER_LOGGER.debug("Mod initializing...");

		instance = this;

		loadServerConfig();

		SERVER_LOGGER.info("Mod initialized.");
		SERVER_LOGGER.warn("Servers are not supported.");
	}
}
