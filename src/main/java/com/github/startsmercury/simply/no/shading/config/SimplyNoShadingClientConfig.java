package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import java.io.Serial;
import java.io.Serializable;

import net.fabricmc.api.Environment;

@Environment(CLIENT)
public final class SimplyNoShadingClientConfig implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private boolean shade = false;

	private boolean shadeBlocks = false;

	public SimplyNoShadingClientConfig set(final SimplyNoShadingClientConfig other) {
		if (other == null) {
			return this;
		}

		setShade(other.shade);
		setShadeBlocks(other.shadeBlocks);

		return this;
	}

	public SimplyNoShadingClientConfig setShade(final boolean shade) {
		this.shade = shade;

		return this;
	}

	public SimplyNoShadingClientConfig setShadeBlocks(final boolean shadeBlocks) {
		this.shadeBlocks = shadeBlocks;

		return this;
	}

	public boolean shouldShade() {
		return this.shade;
	}

	public boolean shouldShadeBlocks() {
		return this.shadeBlocks;
	}

	public void toggleBlockShading() {
		this.shadeBlocks = !this.shadeBlocks;
	}

	public void toggleShading() {
		this.shade = !this.shade;
	}

	public boolean wouldShadeBlocks() {
		return this.shade || this.shadeBlocks;
	}

	public boolean wouldShadeBlocks(final boolean shaded) {
		return shaded && wouldShadeBlocks();
	}
}
