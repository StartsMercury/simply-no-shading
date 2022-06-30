package com.github.startsmercury.simply.no.shading.config;

import static com.github.startsmercury.simply.no.shading.config.ShadingRule.DUMMY;
import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.config.ShadingRules.Observation.Context;
import com.github.startsmercury.simply.no.shading.impl.CloudRenderer;
import com.github.startsmercury.simply.no.shading.util.Copyable;
import com.github.startsmercury.simply.no.shading.util.MultiValuedContainer;
import com.github.startsmercury.simply.no.shading.util.Observable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

/**
 * The {@code ShadingRules} class represents a collection of named shading
 * rules.
 *
 * @since 5.0.0
 */
@Environment(CLIENT)
public class ShadingRules extends MultiValuedContainer<ShadingRule>
        implements Copyable<ShadingRules>, Observable<ShadingRules> {
	/**
	 * The {@code Observation} class represents the observed changes between the
	 * past and the present state of a {@code ShadingRules} object.
	 *
	 * @param <T> the observed type
	 * @since 5.0.0
	 */
	public static class Observation<T extends ShadingRules> extends Observable.Observation<T, Context> {
		/**
		 * The {@code Context} class represents the context object used by the enclosing
		 * {@code Observation} class.
		 *
		 * @param client      the client
		 * @param smartReload was smartReload active
		 * @since 5.0.0
		 */
		public static record Context(Minecraft client, boolean smartReload) {}

		/**
		 * Whether an unnecessary chunk reload was avoided.
		 */
		private boolean smartlyRebuiltChunks;

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
		public void react(final Context context) {
			final var client = context.client();
			final var smartReload = context.smartReload();

			this.smartlyRebuiltChunks = !rebuildChunks(client, smartReload) && smartReload;
		}

		/**
		 * Rebuild blocks when necessary.
		 *
		 * @param client      the client
		 * @param smartReload was smartREload active
		 * @return whether a rebuild occurred
		 */
		protected boolean rebuildBlocks(final Minecraft client, final boolean smartReload) {
			if (!smartReload || shouldRebuild() && shouldRebuildBlocks()) {
				client.levelRenderer.allChanged();

				return true;
			} else
				return false;
		}

		/**
		 * Rebuild chunks when necessary.
		 *
		 * @param client      the client
		 * @param smartReload was smartREload active
		 * @return whether a rebuild occurred
		 */
		protected boolean rebuildChunks(final Minecraft client, final boolean smartReload) {
			return rebuildBlocks(client, smartReload) | rebuildClouds(client, smartReload);
		}

		/**
		 * Rebuild clouds when necessary.
		 *
		 * @param client      the client
		 * @param smartReload was smartReload active
		 * @return whether a rebuild occurred
		 */
		protected boolean rebuildClouds(final Minecraft client, final boolean smartReload) {
			if (!smartReload || shouldRebuild() && shouldRebuildClouds()) {
				((CloudRenderer) client.levelRenderer).generateClouds();

				return true;
			} else
				return false;
		}

		/**
		 * Returns {@code true} if rebuilds are to performed.
		 *
		 * @return {@code true} if rebuilds are to performed
		 */
		protected boolean shouldRebuild() {
			return true;
		}

		/**
		 * Returns {@code true} if blocks are to be rebuilt.
		 *
		 * @return {@code true} if blocks are to be rebuilt
		 * @since 5.0.0
		 */
		public boolean shouldRebuildBlocks() {
			return !this.past.blocks.wouldEquals(this.present.blocks)
			        || !this.past.liquids.wouldEquals(this.present.liquids);
		}

		/**
		 * Returns {@code true} if clouds are to be rebuilt.
		 *
		 * @return {@code true} if clouds are to be rebuilt
		 * @since 5.0.0
		 */
		public boolean shouldRebuildClouds() {
			return !this.past.clouds.wouldEquals(this.present.clouds);
		}

		/**
		 * Returns {@code true} if chunks were rebuilt smartly.
		 *
		 * @return {@code true} if chunks were rebuilt smartly
		 * @since 5.0.0
		 */
		public boolean smartlyRebuiltChunks() {
			return this.smartlyRebuiltChunks;
		}
	}

	/**
	 * All shading, the parent of all the other shading rules.
	 */
	public final ShadingRule all;

	/**
	 * Block shading.
	 */
	public final ShadingRule blocks;

	/**
	 * Cloud shading.
	 */
	public final ShadingRule clouds;

	/**
	 * Liquid shading.
	 */
	public final ShadingRule liquids;

	/**
	 * Creates a new {@code ShadingRules}.
	 */
	public ShadingRules() {
		this.all = register("all", new ShadingRule(false));
		this.blocks = register("blocks", new ShadingRule(this.all, false));
		this.clouds = register("clouds", new ShadingRule(this.all, true));
		this.liquids = register("liquids", new ShadingRule(this.all, false));
	}

	/**
	 * Creates a new {@code ShadingRules} by copying from the another
	 * {@code ShadingRules}.
	 *
	 * @param other the other shading rule
	 */
	public ShadingRules(final ShadingRules other) {
		this();

		copyFrom(other);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ShadingRules copy() {
		return new ShadingRules(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyFrom(final ShadingRules other) {
		forEach((name, shadingRule) -> {
			final var otherShadingRule = other.getOrDefault(name, DUMMY);

			shadingRule.copyFrom(otherShadingRule);
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyTo(final ShadingRules other) {
		forEach((name, shadingRule) -> {
			final var otherShadingRule = other.getOrDefault(name, DUMMY);

			shadingRule.copyTo(otherShadingRule);
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observation<? extends ShadingRules> observe() {
		return new Observation<>(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observation<? extends ShadingRules> observe(final ShadingRules past) {
		return new Observation<>(past, this);
	}

	/**
	 * Reads from a {@code JsonElement}.
	 *
	 * @param in the source to read from
	 * @since 5.0.0
	 */
	public void read(final JsonElement in) {
		if (!(in instanceof final JsonObject object)) { reset(); return; }

		read(object);
	}

	/**
	 * Reads from a {@code JsonObject}.
	 *
	 * @param in the source to read from
	 * @since 5.0.0
	 */
	public void read(final JsonObject in) {
		if (in == null) { reset(); return; }

		forEach((name, shadingRule) -> {
			if (!(in.get(name) instanceof final JsonPrimitive primitive))
				return;

			if (!primitive.isBoolean()) { shadingRule.resetShade(); return; }

			shadingRule.setShade(primitive.getAsBoolean());
		});
	}

	/**
	 * Resets this {@code ShadingRules}.
	 *
	 * @since 5.0.0
	 */
	public void reset() {
		forEach((name, shadingRule) -> shadingRule.resetShade());
	}

	/**
	 * Writes to a {@code JsonObject}.
	 *
	 * @param out the source to write to
	 * @since 5.0.0
	 */
	public void write(final JsonObject out) {
		forEach((name, shadingRule) -> out.addProperty(name, shadingRule.shouldShade()));
	}
}
