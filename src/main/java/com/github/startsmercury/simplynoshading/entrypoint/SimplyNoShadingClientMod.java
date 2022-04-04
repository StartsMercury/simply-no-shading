package com.github.startsmercury.simplynoshading.entrypoint;

import static com.github.startsmercury.simplynoshading.SimplyNoShading.CLIENT_LOGGER;
import static com.github.startsmercury.simplynoshading.SimplyNoShading.loadClientConfig;
import static net.fabricmc.api.EnvType.CLIENT;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;

@Environment(CLIENT)
public final class SimplyNoShadingClientMod implements ClientModInitializer {
	private static SimplyNoShadingClientMod instance;

	public static SimplyNoShadingClientMod getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Accessed SimplyNoShadingClientMod too early");
		}

		return instance;
	}

	public SimplyNoShadingClientMod() {
		CLIENT_LOGGER.debug("Constructing mod initializer...");
		CLIENT_LOGGER.debug("Mod initializer constructed.");
	}

	@Override
	public void onInitializeClient() {
		CLIENT_LOGGER.debug("Initializing mod...");

		instance = this;

		loadClientConfig();

		CLIENT_LOGGER.info("Mod initialized.");
	}
}
