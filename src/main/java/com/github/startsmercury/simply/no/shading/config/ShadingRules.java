package com.github.startsmercury.simply.no.shading.config;

import static com.github.startsmercury.simply.no.shading.config.ShadingRule.DUMMY;
import static com.google.gson.stream.JsonToken.BOOLEAN;
import static net.fabricmc.api.EnvType.CLIENT;

import java.io.IOException;

import com.github.startsmercury.simply.no.shading.impl.CloudRenderer;
import com.github.startsmercury.simply.no.shading.util.Copyable;
import com.github.startsmercury.simply.no.shading.util.Observable;
import com.github.startsmercury.simply.no.shading.util.Values;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(CLIENT)
@JsonAdapter(ShadingRules.JsonAdapter.class)
public class ShadingRules extends Values<ShadingRule> implements Copyable<ShadingRules>, Observable<ShadingRules> {
	public static class JsonAdapter extends TypeAdapter<ShadingRules> {
		@Override
		public ShadingRules read(final JsonReader in) throws IOException {
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

	public static class Observation<T extends ShadingRules> extends Observable.Observation<T, Minecraft> {
		public Observation(final T point) {
			super(point);
		}

		public Observation(final T past, final T present) {
			super(past, present);
		}

		@Override
		public void react(final Minecraft client) {
			if (rebuildChunks()) {
				client.levelRenderer.allChanged();
			}

			if (rebuildClouds()) {
				((CloudRenderer) client.levelRenderer).generateClouds();
			}
		}

		public boolean rebuildChunks() {
			return !this.past.blocks.wouldEquals(this.present.blocks)
			    || !this.past.liquids.wouldEquals(this.present.liquids);
		}

		public boolean rebuildClouds() {
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
