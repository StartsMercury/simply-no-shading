package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import java.io.Serial;
import java.io.Serializable;

import net.fabricmc.api.Environment;

@Environment(CLIENT)
public final class SimplyNoShadingClientConfig implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private boolean shadeAll;

	private boolean shadeBlocks;

	public SimplyNoShadingClientConfig setShadeAll(final boolean shadeAll) {
		this.shadeAll = shadeAll;

		return this;
	}

	public SimplyNoShadingClientConfig setShadeBlocks(final boolean shadeBlocks) {
		this.shadeBlocks = shadeBlocks;

		return this;
	}

	public boolean shouldShadeAll() {
		return this.shadeAll;
	}

	public boolean shouldShadeBlocks() {
		return this.shadeBlocks;
	}
}
