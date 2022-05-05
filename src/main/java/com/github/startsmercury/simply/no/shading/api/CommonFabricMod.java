package com.github.startsmercury.simply.no.shading.api;

import java.nio.file.Path;

import net.fabricmc.loader.api.FabricLoader;

public interface CommonFabricMod extends CommonMod {
	static FabricLoader getFabricLoader() {
		return FabricLoader.getInstance();
	}

	@Override
	default Path getConfigPath() {
		return getFabricLoader().getConfigDir().resolve(getName() + ".gson");
	}
}
