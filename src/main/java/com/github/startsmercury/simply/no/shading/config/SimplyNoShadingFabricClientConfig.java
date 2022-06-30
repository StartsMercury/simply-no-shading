package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import net.fabricmc.api.Environment;

/**
 * The {@code SimplyNoShadingFabricClientConfig} class represents the Simply No
 * Shading config that includes additional features with fabric.
 *
 * @param <R> the shading rules type
 * @since 5.0.0
 */
@Environment(CLIENT)
public class SimplyNoShadingFabricClientConfig<R extends FabricShadingRules> extends SimplyNoShadingClientConfig<R> {
	/**
	 * Returns a new instance of {@code SimplyNoShadingFabricClientConfig} with the
	 * default shading rules type.
	 *
	 * @return a new instance of {@code SimplyNoShadingFabricClientConfig} with the
	 *         default shading rules type
	 * @since 5.0.0
	 * @see FabricShadingRules
	 */
	public static SimplyNoShadingFabricClientConfig<?> identity() {
		return new SimplyNoShadingFabricClientConfig<>(new FabricShadingRules());
	}

	/**
	 * Creates a new SimplyNoShadingFabricClientConfig.
	 *
	 * @param shadingRules the shading rules
	 */
	public SimplyNoShadingFabricClientConfig(final R shadingRules) {
		super(shadingRules);
	}

	/**
	 * Creates a new SimplyNoShadingFabricClientConfig by copying another's state.
	 *
	 * @param other the other config
	 */
	public SimplyNoShadingFabricClientConfig(final SimplyNoShadingClientConfig<R> other) {
		super(other);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimplyNoShadingFabricClientConfig<R> copy() {
		return new SimplyNoShadingFabricClientConfig<>(this);
	}
}
