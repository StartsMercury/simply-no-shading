package com.github.startsmercury.simply.no.shading.v6.util;

/**
 * A {@code Copyable} represents an object that capable of copying from and to
 * objects, optionally including itself.
 *
 * @param <T> the type
 * @since 5.0.0
 */
public interface Copyable<T extends Copyable<T>> {
	/**
	 * Copies this object as a separate copy.
	 *
	 * @return a new separate copy
	 * @throws UnsupportedOperationException if this operation is not supported
	 */
	default T copy() {
		throw new UnsupportedOperationException("copy");
	}

	/**
	 * Copies from another object.
	 *
	 * @param other the other object
	 */
	void copyFrom(T other);

	/**
	 * Copies into another object.
	 *
	 * @param other the other object
	 */
	void copyTo(T other);
}
