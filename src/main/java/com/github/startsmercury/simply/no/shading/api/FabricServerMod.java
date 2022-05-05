package com.github.startsmercury.simply.no.shading.api;

import net.fabricmc.api.DedicatedServerModInitializer;

public interface FabricServerMod extends FabricCommonMod, ServerMod {
	@Override
	DedicatedServerModInitializer getInitializer();
}
