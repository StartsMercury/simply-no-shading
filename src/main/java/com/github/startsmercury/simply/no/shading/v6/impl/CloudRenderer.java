package com.github.startsmercury.simply.no.shading.v6.impl;

import static net.fabricmc.api.EnvType.CLIENT;

import net.fabricmc.api.Environment;

/**
 * Represents a cloud renderer.
 *
 * @since 5.0.0
 * @see #generateClouds()
 */
@Environment(CLIENT)
public interface CloudRenderer {
	/**
	 * Hints the renderer that clouds are needed to be regenerated.
	 *
	 * @since 5.0.0
	 */
	void generateClouds();
}
