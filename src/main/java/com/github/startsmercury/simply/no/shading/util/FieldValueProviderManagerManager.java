package com.github.startsmercury.simply.no.shading.util;

import static java.util.Collections.unmodifiableMap;

import java.util.Map;
import java.util.WeakHashMap;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

public final class FieldValueProviderManagerManager<O, T> {
	private final Map<Class<? extends O>, FieldValueProviderManager<O, T>> cache;

	private final boolean exactFieldType;

	private final Class<T> fieldType;

	private final boolean finalFieldOnly;

	private final Map<Class<? extends O>, FieldValueProviderManager<O, T>> view;

	public FieldValueProviderManagerManager(final Class<T> fieldType, final boolean finalFieldOnly,
	    final boolean exactFieldType) {
		this.cache = new WeakHashMap<>();
		this.exactFieldType = exactFieldType;
		this.fieldType = fieldType;
		this.finalFieldOnly = finalFieldOnly;
		this.view = unmodifiableMap(this.cache);
	}

	@SuppressWarnings({
	    "rawtypes", "unchecked"
	})
	public FieldValueProviderManager<O, T> get(final Class<? extends O> clazz) {
		return this.cache.computeIfAbsent(clazz,
		    key -> new FieldValueProviderManager(key, this.fieldType, this.finalFieldOnly, this.exactFieldType));
	}

	public Reference2ReferenceOpenHashMap<Class<? extends O>, FieldValueProviderManager<O, T>> map() {
		return map(new Reference2ReferenceOpenHashMap<>());
	}

	public <M extends Map<? super Class<? extends O>, ? super FieldValueProviderManager<O, T>>> M map(final M map) {
		map.putAll(this.view);

		return map;
	}

	public Map<Class<? extends O>, FieldValueProviderManager<O, T>> mapView() {
		return this.view;
	}
}
