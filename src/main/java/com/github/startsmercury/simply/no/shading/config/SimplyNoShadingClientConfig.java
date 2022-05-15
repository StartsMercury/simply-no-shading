package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import java.io.Serial;
import java.io.Serializable;

import net.fabricmc.api.Environment;

@Environment(CLIENT)
public final class SimplyNoShadingClientConfig implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private boolean shadeAll = false;

	private boolean shadeBlocks = false;

	private boolean shadeEnhancedBlockEntities = true;

	public SimplyNoShadingClientConfig set(final SimplyNoShadingClientConfig other) {
		if (other == null) {
			return this;
		}

		setShadeAll(other.shadeAll);
		setShadeBlocks(other.shadeBlocks);
		setShadeEnhancedBlockEntities(other.shadeEnhancedBlockEntities);

		return this;
	}

	public SimplyNoShadingClientConfig setShadeAll(final boolean shadeAll) {
		this.shadeAll = shadeAll;

		return this;
	}

	public SimplyNoShadingClientConfig setShadeBlocks(final boolean shadeBlocks) {
		this.shadeBlocks = shadeBlocks;

		return this;
	}

	public SimplyNoShadingClientConfig setShadeEnhancedBlockEntities(final boolean shadeEnhancedBlockEntities) {
		this.shadeEnhancedBlockEntities = shadeEnhancedBlockEntities;

		return this;
	}

	public boolean shouldShadeAll() {
		return this.shadeAll;
	}

	public boolean shouldShadeBlocks() {
		return this.shadeBlocks;
	}

	public boolean shouldShadeEnhancedBlockEntities() {
		return this.shadeEnhancedBlockEntities;
	}

	public SimplyNoShadingClientConfig toggleAllShading() {
		return setShadeAll(!this.shadeAll);
	}

	public SimplyNoShadingClientConfig toggleBlockShading() {
		return setShadeBlocks(!this.shadeBlocks);
	}

	public SimplyNoShadingClientConfig toggleEnhancedBlockEntityShading() {
		return setShadeEnhancedBlockEntities(!this.shadeEnhancedBlockEntities);
	}

	public boolean wouldShadeAll() {
		return this.shadeAll || this.shadeBlocks && this.shadeEnhancedBlockEntities;
	}

	public boolean wouldShadeAll(final boolean shaded) {
		return shaded && wouldShadeAll();
	}

	public boolean wouldShadeBlocks() {
		return this.shadeAll || this.shadeBlocks;
	}

	public boolean wouldShadeBlocks(final boolean shaded) {
		return shaded && wouldShadeBlocks();
	}

	public boolean wouldShadeEnhancedBlockEntities() {
		return this.shadeAll || this.shadeEnhancedBlockEntities;
	}

	public boolean wouldShadeEnhancedBlockEntities(final boolean shaded) {
		return shaded && wouldShadeEnhancedBlockEntities();
	}
}
