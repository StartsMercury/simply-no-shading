package com.github.startsmercury.simply.no.shading.util;

import static it.unimi.dsi.fastutil.objects.Reference2ReferenceMaps.unmodifiable;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

public class Values<T> implements Iterable<Entry<String, T>> {
	private final transient Reference2ReferenceMap<String, T> values;

	private final transient Reference2ReferenceMap<String, T> valuesView;

	protected Values() {
		this(16, false);
	}

	protected Values(final boolean ordered) {
		this(16, ordered);
	}

	protected Values(final int expected) {
		this(expected, false);
	}

	protected Values(final int expected, final boolean ordered) {
		this.values = ordered ? new Reference2ReferenceLinkedOpenHashMap<>()
		    : new Reference2ReferenceOpenHashMap<>(expected);
		this.valuesView = unmodifiable(this.values);
	}

	public final Reference2ReferenceOpenHashMap<String, T> collectAsMap() {
		return collectTo(new Reference2ReferenceOpenHashMap<>(size()));
	}

	public final <M extends Map<? super String, ? super T>> M collectTo(final M map) {
		map.putAll(mapView());

		return map;
	}

	public final boolean contains(final String name) {
		return this.values.containsKey(name);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof final Values<?> other) {
			return this.values.equals(other.values);
		} else {
			return false;
		}
	}

	public void forEach(final BiConsumer<String, T> action) {
		mapView().forEach(action);
	}

	@Deprecated
	@Override
	public void forEach(final Consumer<? super Entry<String, T>> action) {
		mapView().entrySet().forEach(action);
	}

	public final T get(final String name) {
		return this.values.get(name);
	}

	public final T getOrDefault(final String name, final T defaultValue) {
		return this.values.getOrDefault(name, defaultValue);
	}

	@Override
	public int hashCode() {
		return this.values.hashCode();
	}

	public final boolean isEmpty() {
		return this.values.isEmpty();
	}

	@Override
	public Iterator<Entry<String, T>> iterator() {
		return mapView().entrySet().iterator();
	}

	public final Reference2ReferenceMap<String, T> mapView() {
		return this.valuesView;
	}

	protected final <U extends T> U register(final String name, final U value) {
		if (this.values.putIfAbsent(name, value) != null) {
			throw new IllegalStateException('\'' + name + "' is already registered");
		} else {
			return value;
		}
	}

	public final int size() {
		return this.values.size();
	}

	@Override
	public String toString() {
		return this.values.toString();
	}

	public final void trim() {
		if (this.values instanceof final Reference2ReferenceOpenHashMap<String, T> values) {
			values.trim();
		} else if (this.values instanceof final Reference2ReferenceLinkedOpenHashMap<String, T> values) {
			values.trim();
		}
	}
}
