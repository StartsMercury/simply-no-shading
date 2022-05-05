package com.github.startsmercury.simply.no.shading.entrypoint;

import com.github.startsmercury.simply.no.shading.impl.SimplyNoShadingFabricClientMod;

import net.fabricmc.api.ClientModInitializer;

public class SimplyNoShadingFabricClientEntrypoint implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SimplyNoShadingFabricClientMod.getInstance().getInitializer().onInitializeClient();
	}
}
