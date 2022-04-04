package com.github.startsmercury.simplynoshading.config;

import static net.fabricmc.api.EnvType.SERVER;

import java.io.IOException;

import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.fabricmc.api.Environment;

/**
 * Represents Simply No Shading server configuration.
 */
@Deprecated
@Environment(SERVER)
@JsonAdapter(SimplyNoShadingServerConfig.JsonAdapter.class)
public interface SimplyNoShadingServerConfig {
	class JsonAdapter extends TypeAdapter<SimplyNoShadingServerConfig> {
		@Override
		public SimplyNoShadingServerConfig read(final JsonReader in) throws IOException {
			return new SimplyNoShadingServerConfigJson(JsonParser.parseReader(in));
		}

		@Override
		public void write(final JsonWriter out, final SimplyNoShadingServerConfig config) throws IOException {
			if (!(config instanceof final SimplyNoShadingServerConfigJson configJson)) {
				out.beginObject();
				out.endObject();
			} else {
				System.err.println(configJson.getJson());
				Streams.write(configJson.getJson(), out);
			}
		}
	}
}
