package com.github.startsmercury.simply.no.shading.config;

import static com.github.startsmercury.simply.no.shading.config.ShadingRule.DUMMY;
import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.config.ShadingRules.Observation.Context;
import com.github.startsmercury.simply.no.shading.impl.CloudRenderer;
import com.github.startsmercury.simply.no.shading.util.Copyable;
import com.github.startsmercury.simply.no.shading.util.Observable;
import com.github.startsmercury.simply.no.shading.util.Values;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

/**
 * Represents a collection of shading rules.
 *
 * @since 5.0.0
 */
@Environment(CLIENT)
public class ShadingRules extends Values<ShadingRule> implements Copyable<ShadingRules>, Observable<ShadingRules> {
	/**
	 * Observed change to {@link ShadingRules}.
	 *
	 * @param <T> the shading rule type
	 * @since 5.0.0
	 */
	public static class Observation<T extends ShadingRules> extends Observable.Observation<T, Context> {
		/**
		 * {@link Observation Observation's} {@link Observation#react(Context) react}
		 * context.
		 *
		 * @param client      the client
		 * @param smartReload was smartReload active
		 * @since 5.0.0
		 */
		public static record Context(Minecraft client, boolean smartReload) {
		}

		/**
		 * Whether an unnecessary chunk reload was avoided.
		 */
		private boolean smartlyRebuiltChunks;

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
			} else {
				return false;
			}
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
		 * @param smartReload was smartREload active
		 * @return whether a rebuild occurred
		 */
		protected boolean rebuildClouds(final Minecraft client, final boolean smartReload) {
			if (!smartReload || shouldRebuild() && shouldRebuildClouds()) {
				((CloudRenderer) client.levelRenderer).generateClouds();

				return true;
			} else {
				return false;
			}
		}

		/**
		 * {@code AND} composed check whether a rebuild is necessary.
		 *
		 * @return a {@code boolean} value
		 */
		protected boolean shouldRebuild() {
			return true;
		}

		/**
		 * Checks whether block rebuild is necessary.
		 *
		 * @return a {@code boolean} value
		 * @since 5.0.0
		 */
		public boolean shouldRebuildBlocks() {
			return !this.past.blocks.wouldEquals(this.present.blocks)
			    || !this.past.liquids.wouldEquals(this.present.liquids);
		}

		/**
		 * Checks whether clouds rebuild is necessary.
		 *
		 * @return a {@code boolean} value
		 * @since 5.0.0
		 */
		public boolean shouldRebuildClouds() {
			return !this.past.clouds.wouldEquals(this.present.clouds);
		}

		/**
		 * @return whether chunks were rebuilt smartly
		 * @since 5.0.0
		 */
		public boolean smartlyRebuiltChunks() {
			return this.smartlyRebuiltChunks;
		}
	}

	/**
	 * All shading, is the parent of the other shading rules.
	 *
	 * @since 5.0.0
	 */
	public final ShadingRule all;

	/**
	 * Block shading.
	 *
	 * @since 5.0.0
	 */
	public final ShadingRule blocks;

	/**
	 * Cloud shading.
	 *
	 * @since 5.0.0
	 */
	public final ShadingRule clouds;

	/**
	 * Liquid shading.
	 *
	 * @since 5.0.0
	 */
	public final ShadingRule liquids;

	/**
	 * Creates an instance of {@code ShadingRules}.
	 *
	 * @since 5.0.0
	 */
	public ShadingRules() {
		this.all = register("all", new ShadingRule(false));
		this.blocks = register("blocks", new ShadingRule(this.all, false));
		this.clouds = register("clouds", new ShadingRule(this.all, true));
		this.liquids = register("liquids", new ShadingRule(this.all, false));
	}

	/**
	 * Creates an instance of {@code ShadingRules} by copying from the other
	 * {@code ShadingRules}.
	 *
	 * @param other the other shading rule
	 * @since 5.0.0
	 */
	public ShadingRules(final ShadingRules other) {
		this();

		copyFrom(other);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public ShadingRules copy() {
		return new ShadingRules(this);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
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
	 *
	 * @since 5.0.0
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
	 *
	 * @since 5.0.0
	 */
	@Override
	public Observation<? extends ShadingRules> observe() {
		return new Observation<>(this);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
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
		if (!(in instanceof final JsonObject object)) {
			reset();
			return;
		}

		read(object);
	}

	/**
	 * Reads from a {@code JsonObject}.
	 *
	 * @param in the source to read from
	 * @since 5.0.0
	 */
	public void read(final JsonObject in) {
		if (in == null) {
			reset();
			return;
		}

		forEach((name, shadingRule) -> {
			if (!(in.get(name) instanceof final JsonPrimitive primitive)) {
				return;
			}

			if (!primitive.isBoolean()) {
				shadingRule.resetShade();
				return;
			}

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
