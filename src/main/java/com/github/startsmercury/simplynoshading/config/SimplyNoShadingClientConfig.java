package com.github.startsmercury.simplynoshading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import java.io.IOException;

import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.fabricmc.api.Environment;

@Environment(CLIENT)
@JsonAdapter(SimplyNoShadingClientConfig.JsonAdapter.class)
public interface SimplyNoShadingClientConfig {
	class JsonAdapter extends TypeAdapter<SimplyNoShadingClientConfig> {
		@Override
		public SimplyNoShadingClientConfig read(final JsonReader in) throws IOException {
			return new SimplyNoShadingClientConfigJson(JsonParser.parseReader(in));
		}

		@Override
		public void write(final JsonWriter out, final SimplyNoShadingClientConfig config) throws IOException {
			if (!(config instanceof final SimplyNoShadingClientConfigJson configJson)) {
				out.beginObject();
				// @formatter:off
					out.name("shadeAll"); out.value(config.shouldShadeAll());
				// @formatter:on
				out.endObject();
			} else {
				System.err.println(configJson.getJson());
				Streams.write(configJson.getJson(), out);
			}
		}
	}

	default void set(final SimplyNoShadingClientConfig other) {
		setShadeAll(other.shouldShadeAll());
	}

	void setShadeAll(boolean shadeAll);

	boolean shouldShadeAll();
}
