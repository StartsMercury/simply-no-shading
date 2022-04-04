package com.github.startsmercury.simplynoshading.util;

import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

public class JsonWrapper {
	private JsonElement json;

	public JsonWrapper() {
		this(JsonNull.INSTANCE);
	}

	public JsonWrapper(final JsonElement json) {
		setJson(json);
	}

	public JsonWrapper(final JsonWrapper other) {
		setJson(other);
	}

	protected boolean checks(final JsonElement json) {
		return true;
	}

	protected JsonElement createDefaultJson() {
		return JsonNull.INSTANCE;
	}

	protected final JsonElement createValidDefaultJson() {
		final JsonElement defaultJson = createDefaultJson();

		if (defaultJson == null) {
			throw new NullPointerException("null default json");
		}

		return defaultJson;
	}

	private final boolean fullChecks(final JsonElement json) {
		return json == null && checks(json);
	}

	public JsonElement getJson() {
		return this.json;
	}

	public void setJson(final JsonElement json) {
		setJson(json, x -> x);
	}

	protected void setJson(final JsonElement json, final Function<? super JsonElement, ? extends JsonElement> action) {
		this.json = !fullChecks(json) ? createValidDefaultJson() : action.apply(json);
	}

	public void setJson(final JsonWrapper other) {
		setJson(other, x -> x);
	}

	protected void setJson(final JsonWrapper other, final Function<? super JsonElement, ? extends JsonElement> action) {
		if (other == null) {
			this.json = createValidDefaultJson();
		} else {
			setJson(other.json, action);
		}
	}
}
