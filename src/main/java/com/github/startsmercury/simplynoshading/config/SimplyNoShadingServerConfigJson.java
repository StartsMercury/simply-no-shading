package com.github.startsmercury.simplynoshading.config;

import static net.fabricmc.api.EnvType.SERVER;

import com.google.gson.JsonElement;

import net.fabricmc.api.Environment;

@Deprecated
@Environment(SERVER)
public class SimplyNoShadingServerConfigJson extends ConfigJson implements SimplyNoShadingServerConfig {
	public SimplyNoShadingServerConfigJson() {
	}

	public SimplyNoShadingServerConfigJson(final JsonElement json) {
		super(json);
	}

	public SimplyNoShadingServerConfigJson(final SimplyNoShadingServerConfig other) {
		set(other);
	}

	public void set(final SimplyNoShadingServerConfig other) {
		if (other instanceof final SimplyNoShadingServerConfigJson otherJson) {
			setJson(otherJson.getJson(), JsonElement::deepCopy);
		}
	}
}
