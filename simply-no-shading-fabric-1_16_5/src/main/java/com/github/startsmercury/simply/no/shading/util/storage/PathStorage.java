package com.github.startsmercury.simply.no.shading.util.storage;

import java.nio.file.Path;
import java.util.Objects;

/**
 * A {@code PathStorage} is an abstract implementation of {@code Storage} that
 * will act as a base for states accessed with Java NIO. This class overrides
 * the {@link #equals(Object)}, {@link #hashCode()}, and {@link #toString()},
 * where {@link #getPath()} is used instead of a direct field access;
 * {@code null} returns from the aforementioned {@code #getPath()} is properly
 * taken care of.
 *
 * @param <T> the supported type for storing
 * @since 6.0.0
 */
public abstract class PathStorage<T> implements Storage<T> {
	/**
	 * The assigned path to where states are read and written into.
	 */
	private final Path path;

	/**
	 * Creates a new {@code PathStorage} with the assigned path to where states are
	 * read and written into.
	 *
	 * @param path the assigned path to where states are read and written into
	 * @implNote It is the responsibility of implementing classes, when overriding,
	 *           in handling {@code null} as a parameter.
	 */
	protected PathStorage(final Path path) {
		this.path = path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof PathStorage)
			return Objects.equals(this.getPath(), ((PathStorage<?>) obj).getPath());
		return false;
	}

	/**
	 * Returns the assigned path to where states are read and written into.
	 *
	 * @return the assigned path to where states are read and written into
	 * @implNote It is the responsibility of implementing classes, when overriding,
	 *           in handling {@code null}s .
	 */
	public Path getPath() {
		return this.path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(getPath());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final Path path = getPath();

		if (path == null)
			return super.toString();

		final String className = getClass().getName();

		return className + "[path=" + path + ']';
	}
}
