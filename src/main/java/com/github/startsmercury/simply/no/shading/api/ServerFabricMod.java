package com.github.startsmercury.simply.no.shading.api;

import net.fabricmc.api.DedicatedServerModInitializer;

public interface ServerFabricMod extends CommonFabricMod, ServerMod {
	@Override
	DedicatedServerModInitializer getInitializer();
}
