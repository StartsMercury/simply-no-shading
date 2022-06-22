package com.github.startsmercury.simply.no.shading.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Holds constants used elsewhere by the mod Simply No Shading.
 *
 * @since 5.0.0
 */
public final class SimplyNoShadingConstants {
	/**
	 * The {@link Gson gson} used in serializing and deserializing json files.
	 *
	 * @since 5.0.0
	 */
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * It is forbidden to create a new {@code SimplyNoShadingConstants}.
	 */
	private SimplyNoShadingConstants() {}
}
