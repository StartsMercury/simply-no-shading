package com.github.startsmercury.simply.no.shading.config;

import static com.github.startsmercury.simply.no.shading.config.ShadingRule.DUMMY;
import static com.google.gson.stream.JsonToken.BOOLEAN;
import static com.google.gson.stream.JsonToken.NULL;
import static net.fabricmc.api.EnvType.CLIENT;

import java.io.IOException;

import com.github.startsmercury.simply.no.shading.config.ShadingRules.Observation.Context;
import com.github.startsmercury.simply.no.shading.impl.CloudRenderer;
import com.github.startsmercury.simply.no.shading.util.Copyable;
import com.github.startsmercury.simply.no.shading.util.Observable;
import com.github.startsmercury.simply.no.shading.util.Values;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;

@Environment(CLIENT)
public class ShadingRules extends Values<ShadingRule> implements Copyable<ShadingRules>, Observable<ShadingRules> {
	public static class JsonAdapter extends TypeAdapter<ShadingRules> {
		@Override
		public ShadingRules read(final JsonReader in) throws IOException {
			if (in.peek() == NULL) {
				return null;
			}

			final var shadingRules = new ShadingRules();

			in.beginObject();

			while (switch (in.peek()) {
			case END_DOCUMENT, END_OBJECT -> false;
			default -> true;
			}) {
				final var name = in.nextName();
				final var shadingRule = shadingRules.getOrDefault(name, DUMMY);

				if (in.peek() == BOOLEAN) {
					final var shade = in.nextBoolean();

					shadingRule.setShade(shade);
				} else {
					in.skipValue();
				}
			}

			in.endObject();

			return shadingRules;
		}

		@Override
		public void write(final JsonWriter out, final ShadingRules shadingRules) throws IOException {
			out.beginObject();

			for (final var shadingRuleEntry : shadingRules.mapView().entrySet()) {
				final var name = shadingRuleEntry.getKey();
				final var shadingRule = shadingRuleEntry.getValue();
				final var shade = shadingRule.shouldShade();

				out.name(name);
				out.value(shade);
			}

			out.endObject();
		}
	}

	public static class Observation<T extends ShadingRules> extends Observable.Observation<T, Context> {
		public static record Context(Minecraft client, boolean smartReload) {
		}

		public Observation(final T point) {
			super(point);
		}

		public Observation(final T past, final T present) {
			super(past, present);
		}

		protected void evaluate(final boolean smartReload, final Player player, final boolean rebuiltChunks) {
			if (player != null && smartReload && rebuiltChunks) {
				player.displayClientMessage(
				    new TranslatableComponent("simply-no-shading.option.shadingRules.smartReload"), true);
			}
		}

		@Override
		public void react(final Context context) {
			final var client = context.client();
			final var smartReload = context.smartReload();

			final var rebuiltChunks = rebuildChunks(client, smartReload);

			evaluate(smartReload, client.player, rebuiltChunks);
		}

		protected boolean rebuildBlocks(final Minecraft client, final boolean smartReload) {
			if (!smartReload || shouldRebuildBlocks()) {
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
			if (!smartReload || shouldRebuildClouds()) {
				((CloudRenderer) client.levelRenderer).generateClouds();

				return true;
			} else {
				return false;
			}
		}

		public boolean shouldRebuildBlocks() {
			return !this.past.blocks.wouldEquals(this.present.blocks)
			    || !this.past.liquids.wouldEquals(this.present.liquids);
		}

		public boolean shouldRebuildClouds() {
			return !this.past.clouds.wouldEquals(this.present.clouds);
		}
	}

	public final ShadingRule all;

	public final ShadingRule blocks;

	public final ShadingRule clouds;

	public final ShadingRule liquids;

	public ShadingRules() {
		this(4);
	}

	protected ShadingRules(final int expected) {
		super(expected);

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
}
