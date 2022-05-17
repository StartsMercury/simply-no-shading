package com.github.startsmercury.simply.no.shading.impl;

import static net.fabricmc.api.EnvType.CLIENT;

import net.fabricmc.api.Environment;

@Environment(CLIENT)
public interface CloudRenderer {
	void generateClouds();
}
