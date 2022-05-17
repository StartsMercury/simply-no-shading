package com.github.startsmercury.simply.no.shading.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class SimplyNoShadingConstants {
	public static final boolean FABRIC;

	public static final Gson GSON;

	static {
		FABRIC = isClassLoaded("net.fabricmc.loader.api.FabricLoader");
		GSON = new GsonBuilder().setPrettyPrinting().create();
	}

	private static boolean isClassLoaded(final String className) {
		try {
			Class.forName(className);

			return true;
		} catch (final ClassNotFoundException cnfe) {
			return false;
		}
	}

	private SimplyNoShadingConstants() {
	}
}
