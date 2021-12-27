package com.github.startsmercury.simplynoshading.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public interface JsonUtil {
	static BigDecimal getAsBigDecimalOrDefault(final JsonElement jsonElement, final BigDecimal defaultValue) {
		return jsonElement instanceof final JsonPrimitive jsonPrimitive && jsonPrimitive.isNumber()
				? jsonPrimitive.getAsBigDecimal()
				: defaultValue;
	}

	static BigDecimal getAsBigDecimalOrNull(final JsonElement jsonElement) {
		return getAsBigDecimalOrDefault(jsonElement, null);
	}

	static BigInteger getAsBigIntegerOrDefault(final JsonElement jsonElement, final BigInteger defaultValue) {
		return jsonElement instanceof final JsonPrimitive jsonPrimitive && jsonPrimitive.isNumber()
				? jsonPrimitive.getAsBigInteger()
				: defaultValue;
	}

	static BigInteger getAsBigIntegerOrNull(final JsonElement jsonElement) {
		return getAsBigIntegerOrDefault(jsonElement, null);
	}

	static Boolean getAsBooleanOrDefault(final JsonElement jsonElement, final Boolean defaultValue) {
		return jsonElement instanceof final JsonPrimitive jsonPrimitive && jsonPrimitive.isBoolean()
				? jsonPrimitive.getAsBoolean()
				: defaultValue;
	}

	static Boolean getAsBooleanOrNull(final JsonElement jsonElement) {
		return getAsBooleanOrDefault(jsonElement, null);
	}

	static Byte getAsByteOrDefault(final JsonElement jsonElement, final Byte defaultValue) {
		return jsonElement instanceof final JsonPrimitive jsonPrimitive && jsonPrimitive.isNumber()
				? jsonPrimitive.getAsByte()
				: defaultValue;
	}

	static Byte getAsByteOrNull(final JsonElement jsonElement) {
		return getAsByteOrDefault(jsonElement, null);
	}

	static Character getAsCharacterOrDefault(final JsonElement jsonElement, final Character defaultValue) {
		return jsonElement instanceof final JsonPrimitive jsonPrimitive && jsonPrimitive.isNumber()
				? jsonPrimitive.getAsCharacter()
				: defaultValue;
	}

	static Character getAsCharacterOrNull(final JsonElement jsonElement) {
		return getAsCharacterOrDefault(jsonElement, null);
	}

	static Double getAsDoubleOrDefault(final JsonElement jsonElement, final Double defaultValue) {
		return jsonElement instanceof final JsonPrimitive jsonPrimitive && jsonPrimitive.isNumber()
				? jsonPrimitive.getAsDouble()
				: defaultValue;
	}

	static Double getAsDoubleOrNull(final JsonElement jsonElement) {
		return getAsDoubleOrDefault(jsonElement, null);
	}

	static Float getAsFloatOrDefault(final JsonElement jsonElement, final Float defaultValue) {
		return jsonElement instanceof final JsonPrimitive jsonPrimitive && jsonPrimitive.isNumber()
				? jsonPrimitive.getAsFloat()
				: defaultValue;
	}

	static Float getAsFloatOrNull(final JsonElement jsonElement) {
		return getAsFloatOrDefault(jsonElement, null);
	}

	static Integer getAsIntegerOrDefault(final JsonElement jsonElement, final Integer defaultValue) {
		return jsonElement instanceof final JsonPrimitive jsonPrimitive && jsonPrimitive.isNumber()
				? jsonPrimitive.getAsInt()
				: defaultValue;
	}

	static Integer getAsIntegerOrNull(final JsonElement jsonElement) {
		return getAsIntegerOrDefault(jsonElement, null);
	}

	static Long getAsLongOrDefault(final JsonElement jsonElement, final Long defaultValue) {
		return jsonElement instanceof final JsonPrimitive jsonPrimitive && jsonPrimitive.isNumber()
				? jsonPrimitive.getAsLong()
				: defaultValue;
	}

	static Long getAsLongOrNull(final JsonElement jsonElement) {
		return getAsLongOrDefault(jsonElement, null);
	}

	static Number getAsNumberOrDefault(final JsonElement jsonElement, final Number defaultValue) {
		return jsonElement instanceof final JsonPrimitive jsonPrimitive && jsonPrimitive.isNumber()
				? jsonPrimitive.getAsNumber()
				: defaultValue;
	}

	static Number getAsNumberOrNull(final JsonElement jsonElement) {
		return getAsNumberOrDefault(jsonElement, null);
	}

	static Short getAsShortOrDefault(final JsonElement jsonElement, final Short defaultValue) {
		return jsonElement instanceof final JsonPrimitive jsonPrimitive && jsonPrimitive.isNumber()
				? jsonPrimitive.getAsShort()
				: defaultValue;
	}

	static Short getAsShortOrNull(final JsonElement jsonElement) {
		return getAsShortOrDefault(jsonElement, null);
	}

	static String getAsStringOrDefault(final JsonElement jsonElement, final String defaultValue) {
		return jsonElement instanceof final JsonPrimitive jsonPrimitive && jsonPrimitive.isString()
				? jsonPrimitive.getAsString()
				: defaultValue;
	}

	static String getAsStringOrNull(final JsonElement jsonElement) {
		return getAsStringOrDefault(jsonElement, null);
	}

	static BigDecimal getBigDecimalOrDefault(final JsonElement jsonElement, final String memberName,
			final BigDecimal defaultValue) {
		return jsonElement instanceof final JsonObject jsonObject
				? getAsBigDecimalOrDefault(jsonObject.get(memberName), defaultValue)
				: defaultValue;
	}

	static BigDecimal getBigDecimalOrDefaultAt(final JsonElement jsonElement, final int index,
			final BigDecimal defaultValue) {
		return jsonElement instanceof final JsonArray jsonArray
				? getAsBigDecimalOrDefault(jsonArray.get(index), defaultValue)
				: defaultValue;
	}

	static BigDecimal getBigDecimalOrNull(final JsonElement jsonElement, final String memberName) {
		return getBigDecimalOrDefault(jsonElement, memberName, null);
	}

	static BigDecimal getBigDecimalOrNullAt(final JsonElement jsonElement, final int index) {
		return getBigDecimalOrDefaultAt(jsonElement, index, null);
	}

	static BigInteger getBigIntegerOrDefault(final JsonElement jsonElement, final String memberName,
			final BigInteger defaultValue) {
		return jsonElement instanceof final JsonObject jsonObject
				? getAsBigIntegerOrDefault(jsonObject.get(memberName), defaultValue)
				: defaultValue;
	}

	static BigInteger getBigIntegerOrDefaultAt(final JsonElement jsonElement, final int index,
			final BigInteger defaultValue) {
		return jsonElement instanceof final JsonArray jsonArray
				? getAsBigIntegerOrDefault(jsonArray.get(index), defaultValue)
				: defaultValue;
	}

	static BigInteger getBigIntegerOrNull(final JsonElement jsonElement, final String memberName) {
		return getBigIntegerOrDefault(jsonElement, memberName, null);
	}

	static BigInteger getBigIntegerOrNullAt(final JsonElement jsonElement, final int index) {
		return getBigIntegerOrDefaultAt(jsonElement, index, null);
	}

	static Boolean getBooleanOrDefault(final JsonElement jsonElement, final String memberName,
			final Boolean defaultValue) {
		return jsonElement instanceof final JsonObject jsonObject
				? getAsBooleanOrDefault(jsonObject.get(memberName), defaultValue)
				: defaultValue;
	}

	static Boolean getBooleanOrDefaultAt(final JsonElement jsonElement, final int index, final Boolean defaultValue) {
		return jsonElement instanceof final JsonArray jsonArray
				? getAsBooleanOrDefault(jsonArray.get(index), defaultValue)
				: defaultValue;
	}

	static Boolean getBooleanOrNull(final JsonElement jsonElement, final String memberName) {
		return getBooleanOrDefault(jsonElement, memberName, null);
	}

	static Boolean getBooleanOrNullAt(final JsonElement jsonElement, final int index) {
		return getBooleanOrDefaultAt(jsonElement, index, null);
	}

	static Byte getByteOrDefault(final JsonElement jsonElement, final String memberName, final Byte defaultValue) {
		return jsonElement instanceof final JsonObject jsonObject
				? getAsByteOrDefault(jsonObject.get(memberName), defaultValue)
				: defaultValue;
	}

	static Byte getByteOrDefaultAt(final JsonElement jsonElement, final int index, final Byte defaultValue) {
		return jsonElement instanceof final JsonArray jsonArray ? getAsByteOrDefault(jsonArray.get(index), defaultValue)
				: defaultValue;
	}

	static Byte getByteOrNull(final JsonElement jsonElement, final String memberName) {
		return getByteOrDefault(jsonElement, memberName, null);
	}

	static Byte getByteOrNullAt(final JsonElement jsonElement, final int index) {
		return getByteOrDefaultAt(jsonElement, index, null);
	}

	static Character getCharacterOrDefault(final JsonElement jsonElement, final String memberName,
			final Character defaultValue) {
		return jsonElement instanceof final JsonObject jsonObject
				? getAsCharacterOrDefault(jsonObject.get(memberName), defaultValue)
				: defaultValue;
	}

	static Character getCharacterOrDefaultAt(final JsonElement jsonElement, final int index,
			final Character defaultValue) {
		return jsonElement instanceof final JsonArray jsonArray
				? getAsCharacterOrDefault(jsonArray.get(index), defaultValue)
				: defaultValue;
	}

	static Character getCharacterOrNull(final JsonElement jsonElement, final String memberName) {
		return getCharacterOrDefault(jsonElement, memberName, null);
	}

	static Character getCharacterOrNullAt(final JsonElement jsonElement, final int index) {
		return getCharacterOrDefaultAt(jsonElement, index, null);
	}

	static Double getDoubleOrDefault(final JsonElement jsonElement, final String memberName,
			final Double defaultValue) {
		return jsonElement instanceof final JsonObject jsonObject
				? getAsDoubleOrDefault(jsonObject.get(memberName), defaultValue)
				: defaultValue;
	}

	static Double getDoubleOrDefaultAt(final JsonElement jsonElement, final int index, final Double defaultValue) {
		return jsonElement instanceof final JsonArray jsonArray
				? getAsDoubleOrDefault(jsonArray.get(index), defaultValue)
				: defaultValue;
	}

	static Double getDoubleOrNull(final JsonElement jsonElement, final String memberName) {
		return getDoubleOrDefault(jsonElement, memberName, null);
	}

	static Double getDoubleOrNullAt(final JsonElement jsonElement, final int index) {
		return getDoubleOrDefaultAt(jsonElement, index, null);
	}

	static Float getFloatOrDefault(final JsonElement jsonElement, final String memberName, final Float defaultValue) {
		return jsonElement instanceof final JsonObject jsonObject
				? getAsFloatOrDefault(jsonObject.get(memberName), defaultValue)
				: defaultValue;
	}

	static Float getFloatOrDefaultAt(final JsonElement jsonElement, final int index, final Float defaultValue) {
		return jsonElement instanceof final JsonArray jsonArray
				? getAsFloatOrDefault(jsonArray.get(index), defaultValue)
				: defaultValue;
	}

	static Float getFloatOrNull(final JsonElement jsonElement, final String memberName) {
		return getFloatOrDefault(jsonElement, memberName, null);
	}

	static Float getFloatOrNullAt(final JsonElement jsonElement, final int index) {
		return getFloatOrDefaultAt(jsonElement, index, null);
	}

	static Integer getIntegerOrDefault(final JsonElement jsonElement, final String memberName,
			final Integer defaultValue) {
		return jsonElement instanceof final JsonObject jsonObject
				? getAsIntegerOrDefault(jsonObject.get(memberName), defaultValue)
				: defaultValue;
	}

	static Integer getIntegerOrDefaultAt(final JsonElement jsonElement, final int index, final Integer defaultValue) {
		return jsonElement instanceof final JsonArray jsonArray
				? getAsIntegerOrDefault(jsonArray.get(index), defaultValue)
				: defaultValue;
	}

	static Integer getIntegerOrNull(final JsonElement jsonElement, final String memberName) {
		return getIntegerOrDefault(jsonElement, memberName, null);
	}

	static Integer getIntegerOrNullAt(final JsonElement jsonElement, final int index) {
		return getIntegerOrDefaultAt(jsonElement, index, null);
	}

	static Long getLongOrDefault(final JsonElement jsonElement, final String memberName, final Long defaultValue) {
		return jsonElement instanceof final JsonObject jsonObject
				? getAsLongOrDefault(jsonObject.get(memberName), defaultValue)
				: defaultValue;
	}

	static Long getLongOrDefaultAt(final JsonElement jsonElement, final int index, final Long defaultValue) {
		return jsonElement instanceof final JsonArray jsonArray ? getAsLongOrDefault(jsonArray.get(index), defaultValue)
				: defaultValue;
	}

	static Long getLongOrNull(final JsonElement jsonElement, final String memberName) {
		return getLongOrDefault(jsonElement, memberName, null);
	}

	static Long getLongOrNullAt(final JsonElement jsonElement, final int index) {
		return getLongOrDefaultAt(jsonElement, index, null);
	}

	static Number getNumberOrDefault(final JsonElement jsonElement, final String memberName,
			final Number defaultValue) {
		return jsonElement instanceof final JsonObject jsonObject
				? getAsNumberOrDefault(jsonObject.get(memberName), defaultValue)
				: defaultValue;
	}

	static Number getNumberOrDefaultAt(final JsonElement jsonElement, final int index, final Number defaultValue) {
		return jsonElement instanceof final JsonArray jsonArray
				? getAsNumberOrDefault(jsonArray.get(index), defaultValue)
				: defaultValue;
	}

	static Number getNumberOrNull(final JsonElement jsonElement, final String memberName) {
		return getNumberOrDefault(jsonElement, memberName, null);
	}

	static Number getNumberOrNullAt(final JsonElement jsonElement, final int index) {
		return getNumberOrDefaultAt(jsonElement, index, null);
	}

	static Short getShortOrDefault(final JsonElement jsonElement, final String memberName, final Short defaultValue) {
		return jsonElement instanceof final JsonObject jsonObject
				? getAsShortOrDefault(jsonObject.get(memberName), defaultValue)
				: defaultValue;
	}

	static Short getShortOrDefaultAt(final JsonElement jsonElement, final int index, final Short defaultValue) {
		return jsonElement instanceof final JsonArray jsonArray
				? getAsShortOrDefault(jsonArray.get(index), defaultValue)
				: defaultValue;
	}

	static Short getShortOrNull(final JsonElement jsonElement, final String memberName) {
		return getShortOrDefault(jsonElement, memberName, null);
	}

	static Short getShortOrNullAt(final JsonElement jsonElement, final int index) {
		return getShortOrDefaultAt(jsonElement, index, null);
	}

	static String getStringOrDefault(final JsonElement jsonElement, final String memberName,
			final String defaultValue) {
		return jsonElement instanceof final JsonObject jsonObject
				? getAsStringOrDefault(jsonObject.get(memberName), defaultValue)
				: defaultValue;
	}

	static String getStringOrDefaultAt(final JsonElement jsonElement, final int index, final String defaultValue) {
		return jsonElement instanceof final JsonArray jsonArray
				? getAsStringOrDefault(jsonArray.get(index), defaultValue)
				: defaultValue;
	}

	static String getStringOrNull(final JsonElement jsonElement, final String memberName) {
		return getStringOrDefault(jsonElement, memberName, null);
	}

	static String getStringOrNullAt(final JsonElement jsonElement, final int index) {
		return getStringOrDefaultAt(jsonElement, index, null);
	}
}
