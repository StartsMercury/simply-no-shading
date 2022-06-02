package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.config.ShadingRules.Observation.Context;
import com.github.startsmercury.simply.no.shading.util.Copyable;
import com.github.startsmercury.simply.no.shading.util.Observable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

/**
 * Simply No Shading client config.
 *
 * @param <R> the shading rules type
 * @since 5.0.0
 */
@Environment(CLIENT)
public class SimplyNoShadingClientConfig<R extends ShadingRules>
    implements Copyable<SimplyNoShadingClientConfig<?>>, Observable<SimplyNoShadingClientConfig<R>> {
	/**
	 * Observed changes to the config.
	 *
	 * @param <T> the config type
	 * @since 5.0.0
	 */
	public static class Observation<T extends SimplyNoShadingClientConfig<?>>
	    extends Observable.Observation<T, Minecraft> {
		/**
		 * Whether chunks were rebuilt smartly.
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
		public void react(final Minecraft context) {
			final var shadingRulesObservation = this.present.shadingRules.observe(this.past.shadingRules);
			shadingRulesObservation.react(new Context(context, this.present.smartReload));

			this.smartlyRebuiltChunks = shadingRulesObservation.smartlyRebuiltChunks();
		}

		/**
		 * @return whether chunks were rebuilt smartly.
		 * @since 5.0.0
		 */
		public boolean smartlyRebuiltChunks() {
			return this.smartlyRebuiltChunks;
		}
	}

	/**
	 * The shading rules.
	 *
	 * @since 5.0.0
	 */
	public final R shadingRules;

	/**
	 * Whether smart reloading is set.
	 *
	 * @since 5.0.0
	 */
	public boolean smartReload;

	/**
	 * Whether to display messages when chunks are rebuilt smartly.
	 *
	 * @since 5.0.0
	 */
	public boolean smartReloadMessage;

	/**
	 * Creates a new instance of {@code SimplyNoShadingClientConfig}.
	 *
	 * @param shadingRules the shading rules
	 * @since 5.0.0
	 */
	public SimplyNoShadingClientConfig(final R shadingRules) {
		this.shadingRules = shadingRules;
		this.smartReload = true;
		this.smartReloadMessage = true;
	}

	/**
	 * Creates a new instance of {@code SimplyNoShadingClientConfig}.
	 *
	 * @param shadingRules the shading rules
	 * @since 5.0.0
	 */
	@SuppressWarnings("unchecked")
	public SimplyNoShadingClientConfig(final SimplyNoShadingClientConfig<R> other) {
		this((R) other.shadingRules.copy());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public SimplyNoShadingClientConfig<R> copy() {
		return new SimplyNoShadingClientConfig<>(this);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public void copyFrom(final SimplyNoShadingClientConfig<?> other) {
		this.shadingRules.copyFrom(other.shadingRules);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public void copyTo(final SimplyNoShadingClientConfig<?> other) {
		this.shadingRules.copyTo(other.shadingRules);
	}

	/**
	 * @return the shading rules
	 * @since 5.0.0
	 */
	public final R getShadingRules() {
		return this.shadingRules;
	}

	/**
	 * @return whether smart reloading is set
	 * @since 5.0.0
	 */
	public final boolean isSmartReload() {
		return this.smartReload;
	}

	/**
	 * @return whether to display messages when chunks are rebuilt smartly
	 * @since 5.0.0
	 */
	public final boolean isSmartReloadMessage() {
		return this.smartReloadMessage;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public Observation<? extends SimplyNoShadingClientConfig<R>> observe() {
		return new Observation<>(this);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public Observation<? extends SimplyNoShadingClientConfig<R>> observe(final SimplyNoShadingClientConfig<R> past) {
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

		this.smartReload = !in.has("smartReload") || !(in.get("smartReload") instanceof final JsonPrimitive primitive)
		    || !primitive.isBoolean() || primitive.getAsBoolean();
	}

	/**
	 * Resets this {@code SimplyNoShadingClientConfig}.
	 *
	 * @since 5.0.0
	 */
	public void reset() {
		this.shadingRules.reset();
		this.smartReload = true;
	}

	/**
	 * Sets smart reload.
	 *
	 * @param smartReload the new value
	 */
	public final void setSmartReload(final boolean smartReload) {
		this.smartReload = smartReload;
	}

	/**
	 * Sets smart reload message.
	 *
	 * @param smartReloadMessage the new value
	 */
	public final void setSmartReloadMessage(final boolean smartReloadMessage) {
		this.smartReloadMessage = smartReloadMessage;
	}

	/**
	 * Writes to a {@code JsonObject}.
	 *
	 * @param out the source to write to
	 * @since 5.0.0
	 */
	public void write(final JsonObject out) {
		final var shadingRulesJson = out.has("shadingRules")
		    && out.get("shadingRules") instanceof final JsonObject object ? object : new JsonObject();
		this.shadingRules.write(shadingRulesJson);
		out.add("shadingRules", shadingRulesJson);

		out.addProperty("smartReload", this.smartReload);
	}
}
