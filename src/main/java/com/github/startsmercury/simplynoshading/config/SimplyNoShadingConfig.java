package com.github.startsmercury.simplynoshading.config;

import java.io.IOException;

import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

@Deprecated
@JsonAdapter(SimplyNoShadingConfig.JsonAdapter.class)
public interface SimplyNoShadingConfig {
	class JsonAdapter extends TypeAdapter<SimplyNoShadingConfig> {
		@Override
		public SimplyNoShadingConfig read(final JsonReader in) throws IOException {
			return new SimplyNoShadingConfigJson(JsonParser.parseReader(in));
		}

		@Override
		public void write(final JsonWriter out, final SimplyNoShadingConfig config) throws IOException {
			if (!(config instanceof final SimplyNoShadingConfigJson configJson)) {
				out.beginObject();
				out.endObject();
			} else {
				System.err.println(configJson.getJson());
				Streams.write(configJson.getJson(), out);
			}
		}
	}
}
