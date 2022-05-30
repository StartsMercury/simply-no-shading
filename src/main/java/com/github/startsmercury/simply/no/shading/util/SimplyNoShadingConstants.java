package com.github.startsmercury.simply.no.shading.util;

import com.github.startsmercury.simply.no.shading.config.ShadingRules;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class SimplyNoShadingConstants {
	public static final Gson GSON = new GsonBuilder()
	    .registerTypeHierarchyAdapter(ShadingRules.class, new ShadingRules.JsonAdapter()).setPrettyPrinting().create();

	private SimplyNoShadingConstants() {
	}
}
