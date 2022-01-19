package com.github.startsmercury.simplynoshading.entrypoint;

import static net.fabricmc.api.EnvType.CLIENT;

import java.util.StringJoiner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.startsmercury.simplynoshading.client.event.SimplyNoShadingLifecycleEvents;
import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingKeyBindings;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(CLIENT)
public class SimplyNoShadingClientMod implements ClientModInitializer {
	public static final Logger LOGGER;

	static {
		LOGGER = LogManager.getLogger("simply-no-shading+client");
	}

	@Override
	public void onInitializeClient() {
		final boolean fabricKeyBindingApiV1Loaded;
		final boolean fabricLifecycleEventsV1Loaded;
		final FabricLoader fabricLoaderInstance;

		fabricLoaderInstance = FabricLoader.getInstance();
		fabricKeyBindingApiV1Loaded = fabricLoaderInstance.isModLoaded("fabric-key-binding-api-v1");
		fabricLifecycleEventsV1Loaded = fabricLoaderInstance.isModLoaded("fabric-lifecycle-events-v1");

		if (fabricKeyBindingApiV1Loaded && fabricLifecycleEventsV1Loaded) {
			SimplyNoShadingKeyBindings.registerKeyBindings();

			SimplyNoShadingLifecycleEvents.registerLifecycleEvents();
		} else {
			LOGGER.info(() -> {
				final StringJoiner joiner;

				joiner = new StringJoiner(" and ", "Unable to register key bindings and key listeners as mod(s) of id ",
						" is not loaded.");

				if (!fabricKeyBindingApiV1Loaded) {
					joiner.add("fabric-key-binding-api-v1");
				}

				if (!fabricLifecycleEventsV1Loaded) {
					joiner.add("fabric-lifecycle-events-v1");
				}

				return joiner;
			});
		}
	}
}
