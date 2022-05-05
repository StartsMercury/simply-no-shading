package com.github.startsmercury.simply.no.shading.api;

import net.fabricmc.api.ClientModInitializer;

public interface ClientFabricMod extends ClientMod, CommonFabricMod {
	@Override
	ClientModInitializer getInitializer();
}
