package com.github.startsmercury.simplynoshading.entrypoint;

import static net.fabricmc.api.EnvType.CLIENT;

import org.jetbrains.annotations.ApiStatus.Internal;

import com.github.startsmercury.simplynoshading.client.SimplyNoShadingKeyMappings;
import com.github.startsmercury.simplynoshading.client.event.SimplyNoShadingLifecycleEvents;

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
}
