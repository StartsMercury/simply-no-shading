package com.github.startsmercury.simply.no.shading.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The {@code JsonUtils} class contains utility methods on operating with
 * {@link JsonElement json} types.
 */
public final class JsonUtils {
	/**
	 * Returns the {@code boolean} value of the given {@link JsonElement}, or
	 * {@code defaultValue} if it is null, not an instance of {@link JsonPrimitive}
	 * or its value is not a {@code boolean}.
	 *
	 * @param element      the element
	 * @param defaultValue the default value
	 * @return the {@code boolean} value of the given {@link JsonElement}, or
	 *         {@code defaultValue} if it is null, not an instance of
	 *         {@link JsonPrimitive} or its value is not a {@code boolean}
	 * @see #asBoolean(JsonPrimitive, boolean)
	 */
	public static boolean asBoolean(final JsonElement element, final boolean defaultValue) {
		return element instanceof final JsonPrimitive primitive ? asBooleanNonNull(primitive,
		                                                                           defaultValue) : defaultValue;
	}

	/**
	 * Returns the {@code boolean} value of the given {@link JsonPrimitive}, or
	 * {@code defaultValue} if it is null or its value is not a {@code boolean}.
	 *
	 * @param primitive    the primitive
	 * @param defaultValue the default value
	 * @return the {@code boolean} value of the given {@link JsonPrimitive}, or
	 *         {@code defaultValue} if it is null or its value is not a
	 *         {@code boolean}
	 * @see #asBooleanNonNull(JsonPrimitive, boolean)
	 */
	public static boolean asBoolean(final JsonPrimitive primitive, final boolean defaultValue) {
		return primitive != null ? asBooleanNonNull(primitive, defaultValue) : defaultValue;
	}

	/**
	 * Returns the {@code boolean} value of the given {@link JsonPrimitive}, or
	 * {@code defaultValue} if it is not a {@code boolean}.
	 *
	 * @param primitive    the primitive
	 * @param defaultValue the default value
	 * @return the {@code boolean} value of the given {@link JsonPrimitive}, or
	 *         {@code defaultValue} if it is not a {@code boolean}
	 * @throws NullPointerException if {@code primitive} is {@code null}
	 */
	public static boolean asBooleanNonNull(final JsonPrimitive primitive, final boolean defaultValue) {
		return primitive.isBoolean() ? primitive.getAsBoolean() : defaultValue;
	}

	/**
	 * Returns the named {@code boolean} value in the given {@link JsonElement}, or
	 * {@code defaultValue} if it is null, not an instance of {@link JsonObject},
	 * the property with the name is nonexistent, or the value is not a
	 * {@code boolean}.
	 *
	 * @param element      the element
	 * @param name         the name
	 * @param defaultValue the default value
	 * @return the named {@code boolean} value in the given {@link JsonElement}, or
	 *         {@code defaultValue} if it is null, not an instance of
	 *         {@link JsonObject}, the property with the name is nonexistent, or the
	 *         value is not a {@code boolean}
	 * @see #getBoolean(JsonObject, String, boolean)
	 */
	public static boolean getBoolean(final JsonElement element, final String name, final boolean defaultValue) {
		return element instanceof final JsonObject object ? getBooleanNonNull(object,
		                                                                      name,
		                                                                      defaultValue) : defaultValue;
	}

	/**
	 * Returns the named {@code boolean} value in the given {@link JsonObject}, or
	 * {@code defaultValue} if it is null, the property with the name is
	 * nonexistent, or the value is not a {@code boolean}.
	 *
	 * @param object       the object
	 * @param name         the name
	 * @param defaultValue the default value
	 * @return the named {@code boolean} value in the given {@link JsonObject}, or
	 *         {@code defaultValue} if it is null, the property with the name is
	 *         nonexistent, or the value is not a {@code boolean}
	 * @see #getBooleanNonNull(JsonObject, String, boolean)
	 */
	public static boolean getBoolean(final JsonObject object, final String name, final boolean defaultValue) {
		return object != null ? getBooleanNonNull(object, name, defaultValue) : defaultValue;
	}

	/**
	 * Returns the named {@code boolean} value in the given {@link JsonObject}, or
	 * {@code defaultValue} if the property with the name is nonexistent, or the
	 * value is not a {@code boolean}.
	 *
	 * @param object       the object
	 * @param name         the name
	 * @param defaultValue the default value
	 * @return the named {@code boolean} value in the given {@link JsonObject}, or
	 *         {@code defaultValue} if the property with the name is nonexistent, or
	 *         the value is not a {@code boolean}
	 * @throws NullPointerException if {@code object} is {@code null}
	 * @see #asBoolean(JsonElement, boolean)
	 */
	public static boolean getBooleanNonNull(final JsonObject object, final String name, final boolean defaultValue) {
		return asBoolean(object.get(name), defaultValue);
	}
}
