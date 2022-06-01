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

@Environment(CLIENT)
public class ShadingRules extends Values<ShadingRule> implements Copyable<ShadingRules>, Observable<ShadingRules> {
	public static class Observation<T extends ShadingRules> extends Observable.Observation<T, Context> {
		public static record Context(Minecraft client, boolean smartReload) {
		}

		private boolean smartlyRebuiltChunks;

		public Observation(final T point) {
			super(point);
		}

		public Observation(final T past, final T present) {
			super(past, present);
		}

		@Override
		public void react(final Context context) {
			final var client = context.client();
			final var smartReload = context.smartReload();

			this.smartlyRebuiltChunks = !rebuildChunks(client, smartReload) && smartReload;
		}

		protected boolean rebuildBlocks(final Minecraft client, final boolean smartReload) {
			if (!smartReload || shouldRebuild() && shouldRebuildBlocks()) {
				client.levelRenderer.allChanged();

				return true;
			} else {
				return false;
			}
		}

		protected boolean rebuildChunks(final Minecraft client, final boolean smartReload) {
			return rebuildBlocks(client, smartReload) | rebuildClouds(client, smartReload);
		}

		protected boolean rebuildClouds(final Minecraft client, final boolean smartReload) {
			if (!smartReload || shouldRebuild() && shouldRebuildClouds()) {
				((CloudRenderer) client.levelRenderer).generateClouds();

				return true;
			} else {
				return false;
			}
		}

		protected boolean shouldRebuild() {
			return true;
		}

		public boolean shouldRebuildBlocks() {
			return !this.past.blocks.wouldEquals(this.present.blocks)
			    || !this.past.liquids.wouldEquals(this.present.liquids);
		}

		public boolean shouldRebuildClouds() {
			return !this.past.clouds.wouldEquals(this.present.clouds);
		}

		public boolean smartlyRebuiltChunks() {
			return this.smartlyRebuiltChunks;
		}
	}

	public final ShadingRule all;

	public final ShadingRule blocks;

	public final ShadingRule clouds;

	public final ShadingRule liquids;

	public ShadingRules() {
		this.all = register("all", new ShadingRule(false));
		this.blocks = register("blocks", new ShadingRule(this.all, false));
		this.clouds = register("clouds", new ShadingRule(this.all, true));
		this.liquids = register("liquids", new ShadingRule(this.all, false));
	}

	public ShadingRules(final ShadingRules other) {
		this();

		copyFrom(other);
	}

	@Override
	public ShadingRules copy() {
		return new ShadingRules(this);
	}

	@Override
	public void copyFrom(final ShadingRules other) {
		forEach((name, shadingRule) -> {
			final var otherShadingRule = other.getOrDefault(name, DUMMY);

			shadingRule.copyFrom(otherShadingRule);
		});
	}

	@Override
	public void copyTo(final ShadingRules other) {
		forEach((name, shadingRule) -> {
			final var otherShadingRule = other.getOrDefault(name, DUMMY);

			shadingRule.copyTo(otherShadingRule);
		});
	}

	@Override
	public Observation<? extends ShadingRules> observe() {
		return new Observation<>(this);
	}

	@Override
	public Observation<? extends ShadingRules> observe(final ShadingRules past) {
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

	public void reset() {
		forEach((name, shadingRule) -> shadingRule.resetShade());
	}

	public void write(final JsonObject out) {
		forEach((name, shadingRule) -> out.addProperty(name, shadingRule.shouldShade()));
	}
}
