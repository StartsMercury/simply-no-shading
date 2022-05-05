package com.github.startsmercury.simply.no.shading.api;

import net.fabricmc.api.ClientModInitializer;

public interface FabricClientMod extends ClientMod, FabricCommonMod {
	@Override
	ClientModInitializer getInitializer();
}
