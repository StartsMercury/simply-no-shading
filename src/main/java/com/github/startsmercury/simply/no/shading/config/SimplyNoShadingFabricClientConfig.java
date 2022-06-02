package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import net.fabricmc.api.Environment;

/**
 * Simply No Shading fabric client config.
 *
 * @param <R> the shading rules type
 * @since 5.0.0
 */
@Environment(CLIENT)
public class SimplyNoShadingFabricClientConfig<R extends FabricShadingRules> extends SimplyNoShadingClientConfig<R> {
	/**
	 * Creates a new instance of {@code SimplyNoShadingClientConfig}.
	 *
	 * @param shadingRules the shading rules
	 * @since 5.0.0
	 */
	public SimplyNoShadingFabricClientConfig(final R shadingRules) {
		super(shadingRules);
	}

	/**
	 * Creates a new instance of {@code SimplyNoShadingClientConfig}.
	 *
	 * @param other the other config
	 * @since 5.0.0
	 */
	public SimplyNoShadingFabricClientConfig(final SimplyNoShadingClientConfig<R> other) {
		super(other);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public SimplyNoShadingFabricClientConfig<R> copy() {
		return new SimplyNoShadingFabricClientConfig<>(this);
	}
}
