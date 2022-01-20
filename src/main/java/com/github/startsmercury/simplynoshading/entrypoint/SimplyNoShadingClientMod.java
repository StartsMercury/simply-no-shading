package com.github.startsmercury.simplynoshading.entrypoint;

import static net.fabricmc.api.EnvType.CLIENT;

import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.startsmercury.simplynoshading.client.event.SimplyNoShadingLifecycleEvents;
import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingKeyBindings;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(CLIENT)
public class SimplyNoShadingClientMod implements ClientModInitializer {
	private static final String FABRIC_KEY_BINDING_API_V1_ID;

	private static final String FABRIC_LIFECYCLE_EVENTS_V1_ID;

	public static final Logger LOGGER;

	static {
		FABRIC_KEY_BINDING_API_V1_ID = "fabric-key-binding-api-v1";
		FABRIC_LIFECYCLE_EVENTS_V1_ID = "fabric-lifecycle-events-v1";
		LOGGER = LoggerFactory.getLogger("simply-no-shading+client");
	}

	@Override
	public void onInitializeClient() {
		final boolean fabricKeyBindingApiV1Loaded;
		final boolean fabricLifecycleEventsV1Loaded;
		final FabricLoader fabricLoaderInstance;

		fabricLoaderInstance = FabricLoader.getInstance();
		fabricKeyBindingApiV1Loaded = fabricLoaderInstance.isModLoaded(FABRIC_KEY_BINDING_API_V1_ID);
		fabricLifecycleEventsV1Loaded = fabricLoaderInstance.isModLoaded(FABRIC_LIFECYCLE_EVENTS_V1_ID);

		if (fabricKeyBindingApiV1Loaded && fabricLifecycleEventsV1Loaded) {
			SimplyNoShadingKeyBindings.registerKeyBindings();

			SimplyNoShadingLifecycleEvents.registerLifecycleEvents();
		} else {
			LOGGER.atInfo().log(() -> {
				final StringJoiner joiner;

				joiner = new StringJoiner(" and ", "Unable to register key bindings and key listeners as mod(s) of id ",
						" is not loaded.");

				if (!fabricKeyBindingApiV1Loaded) {
					joiner.add(FABRIC_KEY_BINDING_API_V1_ID);
				}

				if (!fabricLifecycleEventsV1Loaded) {
					joiner.add(FABRIC_LIFECYCLE_EVENTS_V1_ID);
				}

				return joiner.toString();
			});
		}
	}
}
