package com.github.startsmercury.simply.no.shading.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import it.unimi.dsi.fastutil.objects.Object2ReferenceLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceMaps;
import it.unimi.dsi.fastutil.objects.Object2ReferenceRBTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceSortedMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceSortedMaps;

/**
 * The abstract class {@code MultiValuedContainer} represents a container of named
 * values. By default values are directly accessed by using {@link #get(String)}
 * or {@link #getOrDefault(String, Object)}.
 * <p>
 * Implementations are required to register their values through
 * {@link #register(String, Object)}, or by a custom procedure indirectly using
 * it.
 *
 * @param <T> the value type
 * @since 5.0.0
 */
public abstract class MultiValuedContainer<T> implements Iterable<Entry<String, T>> {
	/**
	 * The values stores the named values.
	 */
	private final transient Object2ReferenceRBTreeMap<String, T> values;

	/**
	 * This is a read-only view of {@link #values}.
	 */
	private final transient Object2ReferenceSortedMap<String, T> valuesView;

	/**
	 * Constructs a new MultiValuedContainer.
	 */
	protected MultiValuedContainer() {
		this.values = new Object2ReferenceRBTreeMap<>();
		this.valuesView = Object2ReferenceSortedMaps.unmodifiable(this.values);
	}

	/**
	 * Stores a shallow copy of the contained values to a map.
	 *
	 * @return the map
	 * @since 5.0.0
	 */
	public final Object2ReferenceLinkedOpenHashMap<String, T> collectAsMap() {
		return collectTo(new Object2ReferenceLinkedOpenHashMap<>(size()));
	}

	/**
	 * Stores a shallow copy of the contained values to a map.
	 *
	 * @param <M> the map type
	 * @param map the map
	 * @return the map
	 * @since 5.0.0
	 */
	public final <M extends Map<? super String, ? super T>> M collectTo(final M map) {
		if (map == null) {
			return null;
		}

		map.putAll(mapView());

		return map;
	}

	/**
	 * Returns {@code true} if this {@code MultiValuedContainer} contains a value
	 * for the specified name.
	 *
	 * @param name the name
	 * @return {@code true} if this {@code MultiValuedContainer} contains a value
	 *         for the specified name
	 * @since 5.0.0
	 */
	public final boolean contains(final String name) {
		return this.values.containsKey(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof final MultiValuedContainer<?> other) {
			return this.values.equals(other.values);
		} else {
			return false;
		}
	}

	/**
	 * Performs the given action for each named value until all entries have been
	 * processed or the action throws an exception. Exceptions thrown by the action
	 * are relayed to the caller.
	 *
	 * @param action the action
	 * @since 5.0.0
	 */
	public void forEach(final BiConsumer<String, T> action) {
		mapView().forEach(action);
	}

	/**
	 * <b>Deprecated.</b> <i>It is recommended to use {@link #forEach(BiConsumer)},
	 * instead of directly using this method</i>
	 * <p>
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public void forEach(final Consumer<? super Entry<String, T>> action) {
		Object2ReferenceMaps.fastForEach(mapView(), action);
	}

	/**
	 * Returns the value with the given name, or {@code null} if it is not assigned
	 * with any.
	 *
	 * @param name the name
	 * @return the value with the given name, or {@code null} if it is not assigned
	 *         with any
	 */
	public final T get(final String name) {
		return this.values.get(name);
	}

	/**
	 * Returns the value with the given name, or {@code defaultValue} if it is not
	 * assigned with any.
	 *
	 * @param name the name
	 * @return the value with the given name, or {@code defaultValue} if it is not
	 *         assigned with any
	 */
	public final T getOrDefault(final String name, final T defaultValue) {
		return this.values.getOrDefault(name, defaultValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return this.values.hashCode();
	}

	/**
	 * Returns {@code true} if this {@code MultiValuedContainer} contains no
	 * elements.
	 *
	 * @return {@code true} if this {@code MultiValuedContainer} contains no
	 *         elements
	 * @since 5.0.0
	 */
	public final boolean isEmpty() {
		return this.values.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Entry<String, T>> iterator() {
		return mapView().entrySet().iterator();
	}

	/**
	 * Returns a map view of the contained elements.
	 *
	 * @return a map view of the contained elements
	 * @since 5.0.0
	 */
	public final Object2ReferenceSortedMap<String, T> mapView() {
		return this.valuesView;
	}

	/**
	 * Registers a new named value. If this previously contained a value with the
	 * exact name, {@link IllegalStateException} is thrown.
	 *
	 * @param <U>   the value type
	 * @param name  the name
	 * @param value the value
	 * @return the value
	 * @throws IllegalStateException if an attempt was made to assign a new value to
	 *                               a taken name
	 */
	protected final <U extends T> U register(final String name, final U value) {
		if (this.values.putIfAbsent(name, value) != null) {
			throw new IllegalStateException('\'' + name + "' is already assigned with the value: " + value);
		} else {
			return value;
		}
	}

	/**
	 * Returns the number of named values in this {@code MultiValuedContainer}.
	 *
	 * @return the number of named values
	 * @since 5.0.0
	 */
	public final int size() {
		return this.values.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.values.toString();
	}
}
