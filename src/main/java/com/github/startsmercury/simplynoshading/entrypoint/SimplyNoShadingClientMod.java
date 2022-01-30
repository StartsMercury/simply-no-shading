package com.github.startsmercury.simplynoshading.entrypoint;

import static net.fabricmc.api.EnvType.CLIENT;

import org.jetbrains.annotations.ApiStatus.Internal;

import com.github.startsmercury.simplynoshading.client.event.SimplyNoShadingLifecycleEvents;
import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingKeyMappings;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;

/**
 * This class contains the method {@link #onInitializeClient()} which is ran
 * during client initialization.
 *
 * @see ClientModInitializer
 */
@Environment(CLIENT)
@Internal
public class SimplyNoShadingClientMod implements ClientModInitializer {
	/**
	 * Registers custom key bindings.
	 */
	@Override
	public void onInitializeClient() {
		SimplyNoShadingKeyMappings.registerKeyBindings();

		SimplyNoShadingLifecycleEvents.registerLifecycleEvents();
	}

	//	/**
	//	 * Fabric Key Binding API (v1)'s {@linkplain ModMetadata#getId() id}.
	//	 */
	//	public static final String FABRIC_KEY_BINDING_API_V1_ID;
	//
	//	/**
	//	 * Fabric Lifecycle Events API (v1)'s {@linkplain ModMetadata#getId() id}.
	//	 */
	//	public static final String FABRIC_LIFECYCLE_EVENTS_V1_ID;
	//
	//	static {
	//		FABRIC_KEY_BINDING_API_V1_ID = "fabric-key-binding-api-v1";
	//		FABRIC_LIFECYCLE_EVENTS_V1_ID = "fabric-lifecycle-events-v1";
	//	}
	//
	//	/**
	//	 * Registers custom key bindings if both
	//	 * {@linkplain #FABRIC_KEY_BINDING_API_V1_ID Fabric Key Binding API (v1)} and
	//	 * {@linkplain #FABRIC_LIFECYCLE_EVENTS_V1_ID Fabric Lifecycle Events API (v1)}
	//	 * {@linkplain FabricLoader#isModLoaded(String) is loaded}. It
	//	 * {@link PrintStream#println(Object) prints} an {@link System#err error}
	//	 * otherwise.
	//	 */
	//	@Override
	//	public void onInitializeClient() {
	//		final boolean fabricKeyBindingApiV1Loaded;
	//		final boolean fabricLifecycleEventsV1Loaded;
	//		final FabricLoader fabricLoaderInstance;
	//
	//		fabricLoaderInstance = FabricLoader.getInstance();
	//		fabricKeyBindingApiV1Loaded = fabricLoaderInstance.isModLoaded(FABRIC_KEY_BINDING_API_V1_ID);
	//		fabricLifecycleEventsV1Loaded = fabricLoaderInstance.isModLoaded(FABRIC_LIFECYCLE_EVENTS_V1_ID);
	//
	//		if (fabricKeyBindingApiV1Loaded && fabricLifecycleEventsV1Loaded) {
	//			SimplyNoShadingKeyBindings.registerKeyBindings();
	//
	//			SimplyNoShadingLifecycleEvents.registerLifecycleEvents();
	//		} else {
	//			final StringJoiner joiner;
	//
	//			joiner = new StringJoiner(" and ");
	//
	//			if (!fabricKeyBindingApiV1Loaded) {
	//				joiner.add(FABRIC_KEY_BINDING_API_V1_ID);
	//			}
	//
	//			if (!fabricLifecycleEventsV1Loaded) {
	//				joiner.add(FABRIC_LIFECYCLE_EVENTS_V1_ID);
	//			}
	//
	//			System.err.println(
	//					getClass().getName() + " was unable to register key bindings and key listeners as mod(s) of id "
	//							+ joiner + " is not loaded.");
	//		}
	//	}
}
