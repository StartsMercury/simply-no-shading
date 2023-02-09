package com.github.startsmercury.simply.no.shading.util.storage;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;

public class JsonPathStorage<T> extends PathStorage<T> {
	/**
	 * The fallback gson used when an instance accessing {@link #getGson()} received
	 * {@code null}.
	 */
	private static final Gson FALLBACK_GSON = new Gson();

	private final Gson gson;

	private final Type type;

	public JsonPathStorage(final Path path) {
		this(path, FALLBACK_GSON, (Type) null);
	}

	public JsonPathStorage(final Path path, final Class<T> clazz) {
		this(path, FALLBACK_GSON, (Type) clazz);
	}

	public JsonPathStorage(final Path path, final Gson gson) {
		this(path, gson, (Type) null);
	}

	public JsonPathStorage(final Path path, final Gson gson, final Class<T> clazz) {
		this(path, gson, (Type) clazz);
	}

	JsonPathStorage(final Path path, final Gson gson, final Type type) {
		super(path);

		this.gson = gson;
		this.type = type;
	}

	public Gson getGson() {
		return this.gson;
	}

	protected final Gson getGsonElseFallback() {
		final var gson = getGson();

		if (gson == null)
			return JsonPathStorage.FALLBACK_GSON;
		return gson;
	}

	public Type getType() {
		return this.type;
	}

	@Override
	public T load() throws Exception {
		final var path = getPath();

		if (path == null)
			throw new IllegalStateException("Unable to load when " + getClass().getName() + "::getPath() returns null",
			        new NullPointerException("Source path was null"));

		final var type = getType();

		if (type == null)
			throw new IllegalStateException("Unable to load when " + getClass().getName() + "::getType() returns null",
			        new NullPointerException("type was null"));

		final var gson = getGsonElseFallback();

		try (final var reader = Files.newBufferedReader(path)) {
			return gson.fromJson(reader, type);
		}
	}

	@Override
	public void save(final T obj) throws Exception {
		final var path = getPath();

		if (path == null)
			throw new IllegalStateException("Unable to save when " + getClass().getName() + "::getPath() returns null",
			        new NullPointerException("Destination path was null"));

		final var type = getType();

		if (type == null)
			throw new IllegalStateException("Unable to save when " + getClass().getName() + "::getType() returns null",
			        new NullPointerException("type was null"));

		final var gson = getGsonElseFallback();

		try (final var writer = Files.newBufferedWriter(path)) {
			gson.toJson(obj, type, writer);
		}
	}
}
