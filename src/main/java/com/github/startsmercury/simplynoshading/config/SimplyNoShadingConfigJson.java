package com.github.startsmercury.simplynoshading.config;

import com.github.startsmercury.simplynoshading.util.SimplyNoShadingJsonWrapper;
import com.google.gson.JsonElement;

@Deprecated
public class SimplyNoShadingConfigJson extends SimplyNoShadingJsonWrapper implements SimplyNoShadingConfig {
	public SimplyNoShadingConfigJson() {
	}

	public SimplyNoShadingConfigJson(final JsonElement json) {
		super(json);
	}

	public SimplyNoShadingConfigJson(final SimplyNoShadingConfig other) {
		set(other);
	}

	public void set(final SimplyNoShadingConfig other) {
		if (other instanceof final SimplyNoShadingConfigJson otherJson) {
			setJson(otherJson.getJson(), JsonElement::deepCopy);
		}
	}
}
