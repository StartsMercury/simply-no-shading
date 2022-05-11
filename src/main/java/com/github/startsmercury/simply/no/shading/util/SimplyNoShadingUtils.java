package com.github.startsmercury.simply.no.shading.util;

import java.util.function.Consumer;

import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;

public final class SimplyNoShadingUtils {
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static FabricLoader fabricLoader() {
		return FabricLoader.getInstance();
	}

	public static void run(final Runnable runnable) {
		runWhenNotNull(runnable, Runnable::run);
	}

	public static void runWhenLoaded(final String id, final Runnable action) {
		runWhenLoaded(id, action, () -> {});
	}

	public static void runWhenLoaded(final String id, final Runnable action, final Logger logger,
	    final String message) {
		runWhenLoaded(id, action, logger, message, () -> {});
	}

	public static void runWhenLoaded(final String id, final Runnable action, final Logger logger, final String message,
	    final Runnable fallback) {
		runWhenLoaded(id, action, () -> {
			runWhenNotNull(message, logger::warn);

			run(fallback);
		});
	}

	public static void runWhenLoaded(final String id, final Runnable action, final Runnable fallback) {
		run(fabricLoader().isModLoaded(id) ? action : fallback);
	}

	public static <V> void runWhenNotNull(final V value, final Consumer<V> action) {
		runWhenNotNull(value, action, () -> {});
	}

	public static <V> void runWhenNotNull(final V value, final Consumer<V> action, final Runnable fallback) {
		if (value != null) {
			action.accept(value);
		} else {
			run(fallback);
		}
	}

	private SimplyNoShadingUtils() {
	}
}
