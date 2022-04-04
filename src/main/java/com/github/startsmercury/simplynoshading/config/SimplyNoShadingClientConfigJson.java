package com.github.startsmercury.simplynoshading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simplynoshading.util.SimplyNoShadingJsonWrapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.fabricmc.api.Environment;

@Environment(CLIENT)
public class SimplyNoShadingClientConfigJson extends SimplyNoShadingJsonWrapper implements SimplyNoShadingClientConfig {
	public SimplyNoShadingClientConfigJson() {
	}

	public SimplyNoShadingClientConfigJson(final JsonElement json) {
		super(json);
	}

	public SimplyNoShadingClientConfigJson(final SimplyNoShadingClientConfig other) {
		set(other);
	}

	@Override
	protected JsonElement createDefaultJson() {
		final JsonObject defaultJson = new JsonObject();

		defaultJson.addProperty("shadeAll", false);

		return defaultJson;
	}

	@Override
	public void set(final SimplyNoShadingClientConfig other) {
		if (other instanceof final SimplyNoShadingClientConfigJson otherJson) {
			setJson(otherJson.getJson(), JsonElement::deepCopy);
		} else {
			SimplyNoShadingClientConfig.super.set(other);
		}
	}

	@Override
	public void setShadeAll(final boolean shadeAll) {
		if (getJson() instanceof final JsonObject jsonObject) {
			jsonObject.addProperty("shadeAll", shadeAll);
		}
	}

	@Override
	public boolean shouldShadeAll() {
		// @formatter:off
		if (getJson() instanceof final JsonObject jsonObject &&
		    jsonObject.get("shadeAll") instanceof final JsonPrimitive jsonPrimitive &&
		    jsonPrimitive.isBoolean()) {
		// @formatter:on
			return jsonPrimitive.getAsBoolean();
		} else {
			return false;
		}
	}
}
