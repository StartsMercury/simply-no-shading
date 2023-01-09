package com.github.startsmercury.simply.no.shading.v6.config;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.v6.util.Copyable;

import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

/**
 * The {@code FabricShadingRules} class represents a collection of named shading
 * rules including additional ones available with fabric.
 *
 * @since 5.0.0
 */
@Environment(CLIENT)
public class FabricShadingRules extends ShadingRules {
	/**
	 * The {@code Observation} class represents the observed changes between the
	 * past and the present state of a {@code FabricShadingRules} object.
	 *
	 * @param <T> the observed type
	 * @since 5.0.0
	 */
	public static class Observation<T extends FabricShadingRules> extends ShadingRules.Observation<T> {
		/**
		 * Creates a new observation using the "copy" of the present as the past.
		 * Providing a present state that is not an instance of {@link Copyable} will
		 * throw an {@link IllegalArgumentException}.
		 *
		 * @param present the present
		 * @throws IllegalArgumentException if construction is unable to copy the
		 *                                  present
		 */
		public Observation(final T present) {
			super(present);
		}

		/**
		 * Creates a new observation with complete information.
		 *
		 * @param past    the past
		 * @param present the present
		 */
		public Observation(final T past, final T present) {
			super(past, present);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected boolean shouldRebuild() {
			return super.shouldRebuild() && !FabricLoader.getInstance().isModLoaded("iris");
		}

		/**
		 * {@inheritDoc}
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
	 * Creates a new {@code FabricShadingRules}.
	 */
	public FabricShadingRules() {
		this.enhancedBlockEntities = register("enhancedBlockEntities", new ShadingRule(this.all, true));
	}

	/**
	 * Creates a new {@code ShadingRules} by copying from the another
	 * {@code ShadingRules}.
	 *
	 * @param other the other shading rule
	 */
	public FabricShadingRules(final ShadingRules other) {
		this();

		copyFrom(other);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FabricShadingRules copy() {
		return new FabricShadingRules(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observation<? extends FabricShadingRules> observe() {
		return new Observation<>(this);
	}

	/**
	 * Creates an observation with a provided point of the past as reference.
	 *
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
	 * @see #observe(FabricShadingRules)
	 */
	@Deprecated
	@Override
	public ShadingRules.Observation<? extends ShadingRules> observe(final ShadingRules past) {
		return past instanceof final FabricShadingRules fabricPast ? observe(fabricPast) : super.observe(past);
	}
}
