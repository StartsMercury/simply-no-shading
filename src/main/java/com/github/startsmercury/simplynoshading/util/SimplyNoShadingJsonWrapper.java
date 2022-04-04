package com.github.startsmercury.simplynoshading.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SimplyNoShadingJsonWrapper extends JsonWrapper {
	public SimplyNoShadingJsonWrapper() {
	}

	public SimplyNoShadingJsonWrapper(final JsonElement json) {
		super(json);
	}

	@Override
	protected boolean checks(final JsonElement json) {
		return json instanceof JsonObject;
	}

	@Override
	protected JsonElement createDefaultJson() {
		return new JsonObject();
	}
}
