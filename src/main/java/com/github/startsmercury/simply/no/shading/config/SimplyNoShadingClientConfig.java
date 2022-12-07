package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.config.ShadingRules.Observation.Context;
import com.github.startsmercury.simply.no.shading.util.Copyable;
import com.github.startsmercury.simply.no.shading.util.JsonUtils;
import com.github.startsmercury.simply.no.shading.util.Observable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

/**
 * The {@code SimplyNoShadingClientConfig} class represents the Simply No
 * Shading config.
 *
 * @param <R> the shading rules type
 * @since 5.0.0
 */
@Environment(CLIENT)
public class SimplyNoShadingClientConfig<R extends ShadingRules>
        implements Copyable<SimplyNoShadingClientConfig<?>>, Observable<SimplyNoShadingClientConfig<R>> {
	/**
	 * The {@code Observation} class represents the observed changes between the
	 * past and the present state of a {@code SimplyNoShadingClientConfig} object.
	 *
	 * @param <T> the observed type
	 * @since 5.0.0
	 */
	public static class Observation<T extends SimplyNoShadingClientConfig<?>>
	        extends Observable.Observation<T, Minecraft> {
		/**
		 * Whether chunks were rebuilt smartly.
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
		public void react(final Minecraft context) {
			final var shadingRulesObservation = this.present.shadingRules.observe(this.past.shadingRules);
			shadingRulesObservation.react(new Context(context, this.present.smartReload));

			this.smartlyRebuiltChunks = shadingRulesObservation.smartlyRebuiltChunks();
		}

		/**
		 * Returns whether chunks were rebuilt smartly.
		 *
		 * @return whether chunks were rebuilt smartly
		 * @since 5.0.0
		 */
		public boolean smartlyRebuiltChunks() {
			return this.smartlyRebuiltChunks;
		}
	}

	/**
	 * Returns a new instance of {@code SimplyNoShadingClientConfig} with the
	 * default shading rules type.
	 *
	 * @return a new instance of {@code SimplyNoShadingClientConfig} with the
	 *         default shading rules type
	 * @since 5.0.0
	 * @see ShadingRules
	 */
	public static SimplyNoShadingClientConfig<?> identity() {
		return new SimplyNoShadingClientConfig<>(new ShadingRules());
	}

	/**
	 * The shading rules.
	 */
	public final R shadingRules;

	/**
	 * Whether smart reloading is set.
	 */
	public boolean smartReload;

	/**
	 * Whether to display messages when chunks are rebuilt smartly.
	 */
	public boolean smartReloadMessage;

	/**
	 * Creates a new SimplyNoShadingClientConfig.
	 *
	 * @param shadingRules the shading rules
	 */
	public SimplyNoShadingClientConfig(final R shadingRules) {
		this.shadingRules = shadingRules;
		this.smartReload = true;
		this.smartReloadMessage = true;
	}

	/**
	 * Creates a new SimplyNoShadingClientConfig by copying another's state.
	 *
	 * @param other the other config
	 */
	@SuppressWarnings("unchecked")
	public SimplyNoShadingClientConfig(final SimplyNoShadingClientConfig<? extends R> other) {
		this((R) other.shadingRules.copy());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimplyNoShadingClientConfig<R> copy() {
		return new SimplyNoShadingClientConfig<>(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyFrom(final SimplyNoShadingClientConfig<?> other) {
		this.shadingRules.copyFrom(other.shadingRules);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyTo(final SimplyNoShadingClientConfig<?> other) {
		this.shadingRules.copyTo(other.shadingRules);
	}

	/**
	 * Returns the shading rules.
	 *
	 * @return the shading rules
	 * @since 5.0.0
	 */
	public final R getShadingRules() {
		return this.shadingRules;
	}

	/**
	 * Returns whether smart reloading is set.
	 *
	 * @return whether smart reloading is set
	 * @since 5.0.0
	 */
	public final boolean isSmartReload() {
		return this.smartReload;
	}

	/**
	 * Returns whether to display messages when chunks are rebuilt smartly.
	 *
	 * @return whether to display messages when chunks are rebuilt smartly
	 * @since 5.0.0
	 */
	public final boolean isSmartReloadMessage() {
		return this.smartReloadMessage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observation<? extends SimplyNoShadingClientConfig<R>> observe() {
		return new Observation<>(this);
	}

	/**
	 * {@inheritDoc}
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

		this.shadingRules.read(in.get("shadingRules"));
		this.smartReload = JsonUtils.getBooleanNonNull(in, "smartReload", true);
		this.smartReloadMessage = JsonUtils.getBooleanNonNull(in, "smartReloadMessage", true);
	}

	/**
	 * Resets this {@code SimplyNoShadingClientConfig}.
	 *
	 * @since 5.0.0
	 */
	public void reset() {
		this.shadingRules.reset();
		this.smartReload = true;
		this.smartReloadMessage = true;
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
		out.addProperty("smartReloadMessage", this.smartReloadMessage);
	}
}
