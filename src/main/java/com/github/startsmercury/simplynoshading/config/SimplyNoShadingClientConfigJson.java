package com.github.startsmercury.simplynoshading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import java.io.IOException;

import com.github.startsmercury.simplynoshading.config.SimplyNoShadingClientConfigJson.Adapter;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;

import net.fabricmc.api.Environment;

@Environment(CLIENT)
@JsonAdapter(Adapter.class)
public abstract sealed class SimplyNoShadingClientConfigJson extends ConfigJson {
	public static class Adapter extends ConfigJson.Adapter<SimplyNoShadingClientConfigJson> {
		@Override
		protected SimplyNoShadingClientConfigJson parse(final JsonObject raw) {
			return of(raw);
		}

		@Override
		public void write(final JsonWriter out, final SimplyNoShadingClientConfigJson config) throws IOException {
			Streams.write(config.raw(), out);
		}
	}

	public static final class V0 extends SimplyNoShadingClientConfigJson {
		public V0() {
			super(0);
		}

		public V0(final JsonObject raw) {
			super(raw);
		}

		@Override
		public V0 clone() {
			return (V0) super.clone();
		}

		@Override
		public V0 degrade() {
			return this;
		}

		public boolean shouldShadeBlocks() {
			try {
				return this.raw().get("shadeBlocks").getAsBoolean();
			} catch (final ClassCastException cce) {
				return false;
			}
		}

		@Override
		public V0 upgrade() {
			return this;
		}
	}

	public static final SimplyNoShadingClientConfigJson of(final JsonObject raw) {
		return switch (getVersion(raw)) {
		case 0 -> new V0(raw);
		default -> null;
		};
	}

	protected SimplyNoShadingClientConfigJson(final int version) {
		super(version);
	}

	protected SimplyNoShadingClientConfigJson(final JsonObject raw) {
		super(raw);
	}

	@Override
	public SimplyNoShadingClientConfigJson clone() {
		return (SimplyNoShadingClientConfigJson) super.clone();
	}
}
