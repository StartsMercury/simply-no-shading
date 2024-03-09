package com.github.startsmercury.simply.no.shading.util.storage;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;

/**
 * A {@code JsonPathStorage} is a concrete implementation of {@code PathStorage}
 * in that serialized state is stored in the JSON file format.
 *
 * @param <T> the supported type for storing
 */
public class JsonPathStorage<T> extends PathStorage<T> {
	/**
	 * The fallback gson used when an instance accessing {@link #getGson()} received
	 * {@code null}.
	 */
	private static final Gson FALLBACK_GSON = new Gson();

	/**
	 * The gson used for (de)serialization.
	 */
	private final Gson gson;

	/**
	 * The supported type for storing.
	 */
	private final Type type;

	/**
	 * Creates a new {@code JsonPathStorage} with a set path, defaulted gson, and
	 * without runtime type-checking.
	 *
	 * @param path the path where object state is (de)serialized
	 */
	public JsonPathStorage(final Path path) {
		this(path, FALLBACK_GSON, (Type) null);
	}

	/**
	 * Creates a new {@code JsonPathStorage} with a set path, defaulted gson, and
	 * class.
	 *
	 * @param path  the path where object state is (de)serialized
	 * @param clazz the supported type
	 */
	public JsonPathStorage(final Path path, final Class<T> clazz) {
		this(path, FALLBACK_GSON, (Type) clazz);
	}

	/**
	 * Creates a new {@code JsonPathStorage} with a set path, gson, and without
	 * runtime type-checking.
	 *
	 * @param path the path where object state is (de)serialized
	 * @param gson the gson used for (de)serialization
	 */
	public JsonPathStorage(final Path path, final Gson gson) {
		this(path, gson, (Type) null);
	}

	/**
	 * Creates a new {@code JsonPathStorage} with a set path, gson, and class.
	 *
	 * @param path  the path where object state is (de)serialized
	 * @param gson  the gson used for (de)serialization
	 * @param clazz the supported type
	 */
	public JsonPathStorage(final Path path, final Gson gson, final Class<T> clazz) {
		this(path, gson, (Type) clazz);
	}

	/**
	 * Creates a new {@code JsonPathStorage} with a set path, gson, and type.
	 *
	 * @param path the path where object state is (de)serialized
	 * @param gson the gson used for (de)serialization
	 * @param type the supported type
	 */
	JsonPathStorage(final Path path, final Gson gson, final Type type) {
		super(path);

		this.gson = gson;
		this.type = type;
	}

	/**
	 * Returns the gson used for (de)serialization.
	 *
	 * @return the gson used for (de)serialization
	 */
	public Gson getGson() {
		return this.gson;
	}

	/**
	 * This is a utility method wraps around {@link #getGson()}, returning when not
	 * null; returns a fallback otherwise.
	 *
	 * @return the gson used for (de)serialization
	 */
	protected final Gson getGsonElseFallback() {
		final var gson = getGson();

		if (gson == null)
			return JsonPathStorage.FALLBACK_GSON;
		return gson;
	}

	/**
	 * Returns the supported type for storing.
	 *
	 * @return the supported type for storing
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
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
