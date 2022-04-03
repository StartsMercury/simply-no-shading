package com.github.startsmercury.simplynoshading.config;

import com.github.startsmercury.simplynoshading.json.JsonWrapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

class ConfigJson extends JsonWrapper {
	public ConfigJson() {
	}

	public ConfigJson(final JsonElement json) {
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
