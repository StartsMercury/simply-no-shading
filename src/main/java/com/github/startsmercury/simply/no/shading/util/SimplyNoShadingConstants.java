package com.github.startsmercury.simply.no.shading.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Holds constants used throughout elsewhere.
 *
 * @since 5.0.0
 */
public final class SimplyNoShadingConstants {
	/**
	 * The {@link Gson gson}.
	 *
	 * @since 5.0.0
	 */
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * It is not allowed to instantiate {@code SimplyNoShadingConstants}.
	 */
	private SimplyNoShadingConstants() {
	}
}
