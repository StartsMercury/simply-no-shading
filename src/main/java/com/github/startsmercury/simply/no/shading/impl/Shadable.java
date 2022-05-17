package com.github.startsmercury.simply.no.shading.impl;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig.ShadingRule;

import net.fabricmc.api.Environment;

@Environment(CLIENT)
public interface Shadable {
	ShadingRule getShadingRule();

	boolean isShade();

	void setShadingRule(ShadingRule shadingRule);
}
