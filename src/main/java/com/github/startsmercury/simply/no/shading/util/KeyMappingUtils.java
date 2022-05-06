package com.github.startsmercury.simply.no.shading.util;

import static com.mojang.blaze3d.platform.InputConstants.UNKNOWN;

import java.util.function.BooleanSupplier;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;

public final class KeyMappingUtils {
	public static KeyMapping keyMapping(final String key, final int code, final String category) {
		return new KeyMapping(key, code, category);
	}

	public static KeyMapping keyMapping(final String key, final String category) {
		return keyMapping(key, UNKNOWN.getValue(), category);
	}

	public static ToggleKeyMapping toggleKeyMapping(final String key, final String category,
			final BooleanSupplier getter) {
		return toggleKeyMapping(key, UNKNOWN.getValue(), category, getter);
	}

	public static ToggleKeyMapping toggleKeyMapping(final String key, final int code, final String category,
			final BooleanSupplier getter) {
		return new ToggleKeyMapping(key, code, category, getter);
	}
}
