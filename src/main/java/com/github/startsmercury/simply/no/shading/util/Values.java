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
 * <b>Renamed to {@link MultiValuedContainer}</b> Represents a retrievable
 * collection of elements.
 *
 * @param <T> the element type
 * @since 5.0.0
 */
@Deprecated(forRemoval = true)
public class Values<T> implements Iterable<Entry<String, T>> {
	/**
	 * The values.
	 */
	private final transient Object2ReferenceRBTreeMap<String, T> values;

	/**
	 * The values view.
	 */
	private final transient Object2ReferenceSortedMap<String, T> valuesView;

	/**
	 * Creates a new instance of {@code Values}
	 *
	 * @since 5.0.0
	 */
	protected Values() {
		this.values = new Object2ReferenceRBTreeMap<>();
		this.valuesView = Object2ReferenceSortedMaps.unmodifiable(this.values);
	}

	/**
	 * Collects the values to a map.
	 *
	 * @return the map
	 * @since 5.0.0
	 */
	public final Object2ReferenceLinkedOpenHashMap<String, T> collectAsMap() {
		return collectTo(new Object2ReferenceLinkedOpenHashMap<>(size()));
	}

	/**
	 * Collects the values to a map.
	 *
	 * @param <M> the map type
	 * @param map the map
	 * @return the map
	 * @since 5.0.0
	 */
	public final <M extends Map<? super String, ? super T>> M collectTo(final M map) {
		if (map == null)
			return null;

		map.putAll(mapView());

		return map;
	}

	/**
	 * Checks if it contains an element with the given name.
	 *
	 * @param name the name
	 * @return a {@code boolean} value
	 * @since 5.0.0
	 */
	public final boolean contains(final String name) {
		return this.values.containsKey(name);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		else if (obj instanceof final Values<?> other)
			return this.values.equals(other.values);
		else
			return false;
	}

	/**
	 * @param action the action
	 * @since 5.0.0
	 */
	public void forEach(final BiConsumer<String, T> action) {
		mapView().forEach(action);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Deprecated
	@Override
	public void forEach(final Consumer<? super Entry<String, T>> action) {
		Object2ReferenceMaps.fastForEach(mapView(), action);
	}

	/**
	 * @param name the name
	 * @return the element with the given name
	 */
	public final T get(final String name) {
		return this.values.get(name);
	}

	/**
	 * @param name         the name
	 * @param defaultValue the default value
	 * @return the element with the given name or the defaul value if none
	 */
	public final T getOrDefault(final String name, final T defaultValue) {
		return this.values.getOrDefault(name, defaultValue);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public int hashCode() {
		return this.values.hashCode();
	}

	/**
	 * Checks if this object is empty.
	 *
	 * @return a {@code boolean} value
	 * @since 5.0.0
	 */
	public final boolean isEmpty() {
		return this.values.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public Iterator<Entry<String, T>> iterator() {
		return mapView().entrySet().iterator();
	}

	/**
	 * @return the map view
	 * @since 5.0.0
	 */
	public final Object2ReferenceSortedMap<String, T> mapView() {
		return this.valuesView;
	}

	/**
	 * Registers an element.
	 *
	 * @param <U>   the element type
	 * @param name  the name
	 * @param value the value
	 * @return the element
	 */
	protected final <U extends T> U register(final String name, final U value) {
		if (this.values.putIfAbsent(name, value) != null)
			throw new IllegalStateException('\'' + name + "' is already registered");
		else
			return value;
	}

	/**
	 * @return the size
	 * @since 5.0.0
	 */
	public final int size() {
		return this.values.size();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public String toString() {
		return this.values.toString();
	}
}
