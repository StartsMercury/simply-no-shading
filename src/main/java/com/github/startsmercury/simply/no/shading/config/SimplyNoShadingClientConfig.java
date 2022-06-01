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

@Environment(CLIENT)
public class SimplyNoShadingClientConfig<R extends ShadingRules>
    implements Copyable<SimplyNoShadingClientConfig<?>>, Observable<SimplyNoShadingClientConfig<R>> {
	public static class Observation<T extends SimplyNoShadingClientConfig<?>>
	    extends Observable.Observation<T, Minecraft> {
		private boolean smartlyRebuiltChunks;

		public Observation(final T point) {
			super(point);
		}

		public Observation(final T past, final T present) {
			super(past, present);
		}

		@Override
		public void react(final Minecraft context) {
			final var shadingRulesObservation = this.present.shadingRules.observe(this.past.shadingRules);
			shadingRulesObservation.react(new Context(context, this.present.smartReload));

			this.smartlyRebuiltChunks = shadingRulesObservation.smartlyRebuiltChunks();
		}

		public boolean smartlyRebuiltChunks() {
			return this.smartlyRebuiltChunks;
		}
	}

	public final R shadingRules;

	public boolean smartReload;

	public boolean smartReloadMessage;

	public SimplyNoShadingClientConfig(final R shadingRules) {
		this.shadingRules = shadingRules;
		this.smartReload = true;
		this.smartReloadMessage = true;
	}

	@SuppressWarnings("unchecked")
	public SimplyNoShadingClientConfig(final SimplyNoShadingClientConfig<R> other) {
		this((R) other.shadingRules.copy());
	}

	@Override
	public SimplyNoShadingClientConfig<R> copy() {
		return new SimplyNoShadingClientConfig<>(this);
	}

	@Override
	public void copyFrom(final SimplyNoShadingClientConfig<?> other) {
		this.shadingRules.copyFrom(other.shadingRules);
	}

	@Override
	public void copyTo(final SimplyNoShadingClientConfig<?> other) {
		this.shadingRules.copyTo(other.shadingRules);
	}

	public final R getShadingRules() {
		return this.shadingRules;
	}

	public final boolean isSmartReload() {
		return this.smartReload;
	}

	public final boolean isSmartReloadMessage() {
		return this.smartReloadMessage;
	}

	@Override
	public Observation<? extends SimplyNoShadingClientConfig<R>> observe() {
		return new Observation<>(this);
	}

	@Override
	public Observation<? extends SimplyNoShadingClientConfig<R>> observe(final SimplyNoShadingClientConfig<R> past) {
		return new Observation<>(past, this);
	}

	public void read(final JsonElement in) {
		if (!(in instanceof final JsonObject object)) {
			reset();
			return;
		}

		read(object);
	}

	public void read(final JsonObject in) {
		if (in == null) {
			reset();
			return;
		}

		this.smartReload = !in.has("smartReload") || !(in.get("smartReload") instanceof final JsonPrimitive primitive)
		    || !primitive.isBoolean() || primitive.getAsBoolean();
	}

	public void reset() {
		this.shadingRules.reset();
		this.smartReload = true;
	}

	public final void setSmartReload(final boolean smartReload) {
		this.smartReload = smartReload;
	}

	public final void setSmartReloadMessage(final boolean smartReloadMessage) {
		this.smartReloadMessage = smartReloadMessage;
	}

	public void write(final JsonObject out) {
		final var shadingRulesJson = out.has("shadingRules")
		    && out.get("shadingRules") instanceof final JsonObject object ? object : new JsonObject();
		this.shadingRules.write(shadingRulesJson);
		out.add("shadingRules", shadingRulesJson);

		out.addProperty("smartReload", this.smartReload);
	}
}
