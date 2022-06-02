package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import net.coderbot.iris.Iris;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Represents a collection of fabric shading rules.
 *
 * @since 5.0.0
 */
@Environment(CLIENT)
public class FabricShadingRules extends ShadingRules {
	/**
	 * Observed change to {@link FabricShadingRules}.
	 *
	 * @param <T> the shading rule type
	 * @since 5.0.0
	 */
	public static class Observation<T extends FabricShadingRules> extends ShadingRules.Observation<T> {
		/**
		 * Creates a new instance of {@link Observation} with the a reference point.
		 *
		 * @param past the past
		 * @since 5.0.0
		 */
		public Observation(final T point) {
			super(point);
		}

		/**
		 * Creates a new instance of {@link Observation} with the past and present.
		 *
		 * @param past    the past
		 * @param present the present
		 * @since 5.0.0
		 */
		public Observation(final T past, final T present) {
			super(past, present);
		}

		/**
		 * {@inheritDoc}
		 *
		 * @since 5.0.0
		 */
		@Override
		protected boolean shouldRebuild() {
			return super.shouldRebuild()
			    && (!FabricLoader.getInstance().isModLoaded("iris") || !Iris.getIrisConfig().areShadersEnabled());
		}

		/**
		 * {@inheritDoc}
		 *
		 * @since 5.0.0
		 */
		@Override
		public boolean shouldRebuildBlocks() {
			return super.shouldRebuildBlocks()
			    || !this.past.enhancedBlockEntities.wouldEquals(this.present.enhancedBlockEntities);
		}
	}

	/**
	 * Enhanced block entity shading.
	 */
	public final ShadingRule enhancedBlockEntities;

	/**
	 * Creates an instance of {@code FabricShadingRules}.
	 *
	 * @since 5.0.0
	 */
	public FabricShadingRules() {
		this.enhancedBlockEntities = register("enhancedBlockEntities", new ShadingRule(this.all, true));
	}

	/**
	 * Creates an instance of {@code FabricShadingRules} by copying from the other
	 * {@code ShadingRules}.
	 *
	 * @param other the other shading rule
	 * @since 5.0.0
	 */
	public FabricShadingRules(final ShadingRules other) {
		this();

		copyFrom(other);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public FabricShadingRules copy() {
		return new FabricShadingRules(this);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public Observation<? extends FabricShadingRules> observe() {
		return new Observation<>(this);
	}

	/**
	 * @param past the past
	 * @return the observation
	 * @since 5.0.0
	 */
	public Observation<? extends FabricShadingRules> observe(final FabricShadingRules past) {
		return new Observation<>(past, this);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Deprecated
	@Override
	public ShadingRules.Observation<? extends ShadingRules> observe(final ShadingRules past) {
		return past instanceof final FabricShadingRules fabricPast ? observe(fabricPast) : super.observe(past);
	}
}
