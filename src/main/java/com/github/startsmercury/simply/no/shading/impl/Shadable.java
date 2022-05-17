package com.github.startsmercury.simply.no.shading.impl;

import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig.ShadingRule;

public interface Shadable {
	ShadingRule getShadingRule();

	boolean isShade();

	void setShadingRule(ShadingRule shadingRule);
}
