package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import net.fabricmc.api.Environment;

@Environment(CLIENT)
public class SimplyNoShadingFabricClientConfig<R extends FabricShadingRules> extends SimplyNoShadingClientConfig<R> {
	public SimplyNoShadingFabricClientConfig(final R shadingRules) {
		super(shadingRules);
	}

	public SimplyNoShadingFabricClientConfig(final SimplyNoShadingClientConfig<R> other) {
		super(other);
	}

	@Override
	public SimplyNoShadingFabricClientConfig<R> copy() {
		return new SimplyNoShadingFabricClientConfig<>(this);
	}
}
