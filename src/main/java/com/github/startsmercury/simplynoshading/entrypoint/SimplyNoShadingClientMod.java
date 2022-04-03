package com.github.startsmercury.simplynoshading.entrypoint;

import static com.github.startsmercury.simplynoshading.SimplyNoShading.CLIENT_LOGGER;
import static net.fabricmc.api.EnvType.CLIENT;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;

@Environment(CLIENT)
public final class SimplyNoShadingClientMod implements ClientModInitializer {
	private static SimplyNoShadingClientMod instance;

	public static SimplyNoShadingClientMod getInstance() {
		return instance;
	}

	public SimplyNoShadingClientMod() {
		CLIENT_LOGGER.debug("Initializer constructed.");
	}

	@Override
	public void onInitializeClient() {
		CLIENT_LOGGER.debug("Mod initializing...");

		instance = this;

		CLIENT_LOGGER.info("Mod initialized.");
	}
}
