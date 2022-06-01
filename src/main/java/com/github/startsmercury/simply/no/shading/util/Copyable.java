package com.github.startsmercury.simply.no.shading.util;

/**
 * Represents a copyable object which it can copy from and to other objects, and
 * copy itself as a separate copy.
 *
 * @param <T> the type
 * @since 5.0.0
 */
public interface Copyable<T extends Copyable<T>> {
	/**
	 * Copies this object as a separate copy.
	 *
	 * @return a new separate copy
	 * @since 5.0.0
	 */
	T copy();

	/**
	 * Copies from another object.
	 *
	 * @param other the other object
	 * @since 5.0.0
	 */
	void copyFrom(T other);

	/**
	 * Copies into another object.
	 *
	 * @param other the other object
	 * @since 5.0.0
	 */
	void copyTo(T other);
}
