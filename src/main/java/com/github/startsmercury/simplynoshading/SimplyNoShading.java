package com.github.startsmercury.simplynoshading;

import static net.fabricmc.api.EnvType.CLIENT;
import static net.fabricmc.api.EnvType.SERVER;

import org.slf4j.Logger;

import com.github.startsmercury.simplynoshading.logging.SimplyNoShadingDefferedLogger;

import net.fabricmc.api.Environment;

public final class SimplyNoShading {
	public static final String MODID = "simply-no-shading";

	@Deprecated
	public static final Logger LOGGER = new SimplyNoShadingDefferedLogger(MODID);

	@Environment(CLIENT)
	public static final Logger CLIENT_LOGGER = new SimplyNoShadingDefferedLogger(MODID + "+client");

	@Deprecated
	@Environment(SERVER)
	public static final Logger SERVER_LOGGER = new SimplyNoShadingDefferedLogger(MODID + "+server");
}
