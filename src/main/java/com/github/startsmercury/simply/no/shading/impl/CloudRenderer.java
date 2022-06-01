package com.github.startsmercury.simply.no.shading.impl;

import static net.fabricmc.api.EnvType.CLIENT;

import org.jetbrains.annotations.ApiStatus;

import net.fabricmc.api.Environment;

/**
 * Represents a cloud renderer.
 *
 * @since 5.0.0
 */
@ApiStatus.Internal
@Environment(CLIENT)
public interface CloudRenderer {
	/**
	 * Generate the clouds.
	 */
	void generateClouds();
}
