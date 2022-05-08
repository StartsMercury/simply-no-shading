package com.github.startsmercury.simply.no.shading.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Constants {
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private Constants() {
	}
}
