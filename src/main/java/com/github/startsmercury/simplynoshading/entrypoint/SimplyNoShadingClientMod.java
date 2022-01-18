package com.github.startsmercury.simplynoshading.entrypoint;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simplynoshading.client.event.SimplyNoShadingLifecycleEvents;
import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingKeyBindings;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(CLIENT)
public class SimplyNoShadingClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		final FabricLoader fabricLoaderInstance;

		fabricLoaderInstance = FabricLoader.getInstance();

		if (fabricLoaderInstance.isModLoaded("fabric-lifecycle-events-v1")
				&& fabricLoaderInstance.isModLoaded("fabric-keybinding-api-v1")) {
			SimplyNoShadingKeyBindings.registerKeyBindings();

			SimplyNoShadingLifecycleEvents.registerLifecycleEvents();
		}
	}
}
