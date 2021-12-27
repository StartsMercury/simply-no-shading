package com.github.startsmercury.simplynoshading.entrypoint;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simplynoshading.client.event.SimplyNoShadingLifecycleEvents;
import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingKeyBindings;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(CLIENT)
public class SimplyNoShadingClientMod implements ClientModInitializer {
	private static SimplyNoShadingClientMod INSTANCE;

	public static SimplyNoShadingClientMod getInstance() {
		return INSTANCE;
	}

	private boolean isFabricKeyBindingApiV1Loaded;

	private boolean isFabricLifycycleEventsV1Loaded;

	private final void initModLoaded() {
		final FabricLoader fabricLoaderInstance;

		fabricLoaderInstance = FabricLoader.getInstance();
		this.isFabricLifycycleEventsV1Loaded = fabricLoaderInstance.isModLoaded("fabric-lifecycle-events-v1");
		this.isFabricKeyBindingApiV1Loaded = fabricLoaderInstance.isModLoaded("fabric-keybinding-api-v1");
	}

	public boolean isFabricKeyBindingApiV1Loaded() {
		return this.isFabricKeyBindingApiV1Loaded;
	}

	public boolean isFabricLifycycleEventsV1Loaded() {
		return this.isFabricLifycycleEventsV1Loaded;
	}

	@Override
	public void onInitializeClient() {
		INSTANCE = this;

		initModLoaded();

		if (this.isFabricLifycycleEventsV1Loaded) {
			SimplyNoShadingKeyBindings.registerKeyBindings();
		}

		if (this.isFabricKeyBindingApiV1Loaded) {
			SimplyNoShadingLifecycleEvents.registerLifecycleEvents();
		}
	}
}
