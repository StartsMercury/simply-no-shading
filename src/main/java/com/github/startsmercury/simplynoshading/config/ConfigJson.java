package com.github.startsmercury.simplynoshading.config;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public abstract class ConfigJson implements Cloneable {
	protected static abstract class Adapter<T extends ConfigJson> extends TypeAdapter<T> {
		protected abstract T parse(JsonObject raw);

		@Override
		public T read(final JsonReader in) throws IOException {
			return parse(Streams.parse(in).getAsJsonObject());
		}

		@Override
		public void write(final JsonWriter out, final T config) throws IOException {
			Streams.write(config.raw(), out);
		}
	}

	protected static final int getVersion(final JsonObject raw) {
		try {
			return raw.get("version").getAsInt();
		} catch (final ClassCastException cce) {
			return -1;
		}
	}

	private JsonObject raw;

	protected ConfigJson(final int version) {
		this(new JsonObject());

		this.raw.addProperty("version", version);
	}

	protected ConfigJson(final JsonObject raw) {
		this.raw = raw;
	}

	@Override
	public ConfigJson clone() {
		try {
			final ConfigJson clone = (ConfigJson) super.clone();

			clone.raw = this.raw.deepCopy();

			return clone;
		} catch (final CloneNotSupportedException cnse) {
			throw new InternalError(cnse);
		}
	}

	public abstract ConfigJson degrade();

	public final ConfigJson degradeTo(final int version) {
		final var thisVersion = getVersion();

		if (thisVersion > version) {
			return this;
		}

		final var degraded = degrade();

		if (thisVersion == degraded.getVersion()) {
			return this;
		}

		return degraded.degradeTo(version);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof final ConfigJson other)) {
			return false;
		}

		return this.raw.equals(other.raw);
	}

	public final int getVersion() {
		return getVersion(this.raw);
	}

	@Override
	public int hashCode() {
		return this.raw.hashCode();
	}

	public final JsonObject raw() {
		return this.raw;
	}

	@Override
	public String toString() {
		return this.raw.toString();
	}

	public abstract ConfigJson upgrade();

	public final ConfigJson upgradeTo(final int version) {
		final var thisVersion = getVersion();

		if (thisVersion < version) {
			return this;
		}

		final var upgraded = upgrade();

		if (thisVersion == upgraded.getVersion()) {
			return this;
		}

		return upgraded.upgradeTo(version);
	}
}
