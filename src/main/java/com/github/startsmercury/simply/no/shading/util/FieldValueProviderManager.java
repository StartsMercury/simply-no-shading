package com.github.startsmercury.simply.no.shading.util;

import static it.unimi.dsi.fastutil.objects.Reference2ReferenceMaps.unmodifiable;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.function.Function;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

public final class FieldValueProviderManager<O, T> {
	private final Reference2ReferenceMap<String, Function<? super O, ? extends T>> fieldValueProviders;

	@SuppressWarnings("unchecked")
	public FieldValueProviderManager(final Class<O> ownerClass, final Class<T> fieldType, final boolean finalFieldOnly,
	    final boolean exactFieldType) {
		final var fields = ownerClass.getFields();
		final var fieldValueProviders = new Reference2ReferenceOpenHashMap<String, Function<? super O, ? extends T>>(
		    fields.length);
		final var mask = Modifier.STATIC | (finalFieldOnly ? Modifier.FINAL : 0);

		for (final var field : fields) {
			if ((field.getModifiers() & mask) == 0
			    && (exactFieldType ? fieldType != field.getType() : !fieldType.isAssignableFrom(field.getType()))) {
				fieldValueProviders.put(field.getName(), owner -> {
					try {
						return (T) field.get(owner);
					} catch (final IllegalAccessException iae) {
						throw new InternalError("setAccessible(true) is flawed or missing", iae);
					} catch (final IllegalArgumentException iae) {
						throw new InternalError("Static filter is flawed or missing", iae);
					}
				});
			}
		}

		fieldValueProviders.trim();

		this.fieldValueProviders = unmodifiable(fieldValueProviders);
	}

	public T get(final O owner, final String name) {
		return get(name).apply(owner);
	}

	public Function<? super O, ? extends T> get(final String name) {
		return this.fieldValueProviders.get(name);
	}

	public Reference2ReferenceOpenHashMap<String, Function<? super O, ? extends T>> map() {
		return map(new Reference2ReferenceOpenHashMap<>());
	}

	public <M extends Map<? super String, ? super Function<? super O, ? extends T>>> M map(final M map) {
		if (map != null) {
			map.putAll(mapView());
		}

		return map;
	}

	public Reference2ReferenceOpenHashMap<String, T> map(final O owner) {
		return map(owner, new Reference2ReferenceOpenHashMap<>());
	}

	public <M extends Map<? super String, ? super T>> M map(final O owner, final M map) {
		mapView().forEach((name, provider) -> map.put(name, provider.apply(owner)));

		return map;
	}

	public Reference2ReferenceMap<String, Function<? super O, ? extends T>> mapView() {
		return this.fieldValueProviders;
	}
}
