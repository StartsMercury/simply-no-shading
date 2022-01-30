package com.github.startsmercury.simplynoshading.client.event;

import org.jetbrains.annotations.ApiStatus.Internal;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;
import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingKeyMappings;
import com.github.startsmercury.simplynoshading.entrypoint.SimplyNoShadingClientMod;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.Event;

/**
 * This class contains the method {@link #registerLifecycleEvents()} which is
 * called by {@link SimplyNoShadingClientMod#onInitializeClient()}.
 */
@Internal
public class SimplyNoShadingLifecycleEvents {
	/**
	 * {@linkplain Event#register(Object) Registers}
	 * {@linkplain ClientTickEvents#END_CLIENT_TICK tick} listeners that anticipates
	 * key binding(s) being pressed. This method should only be called once by
	 * {@link SimplyNoShadingClientMod#onInitializeClient()} after
	 * {@link SimplyNoShadingKeyMappings#registerKeyBindings()}.
	 */
	public static void registerLifecycleEvents() {
		ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
			final SimplyNoShadingGameOptions clientOptions;
			boolean reload;

			clientOptions = (SimplyNoShadingGameOptions) minecraft.options;
			reload = false;

			if (clientOptions.keyCycleShadeAll().consumeClick()) {
				reload = true;

				clientOptions.cycleShadeAll();
			}

			if (clientOptions.keyCycleShadeBlocks().consumeClick()) {
				reload = true;

				clientOptions.cycleShadeBlocks();
			}

			if (clientOptions.keyCycleShadeFluids().consumeClick()) {
				reload = true;

				clientOptions.cycleShadeFluids();
			}

			if (reload) {
				minecraft.levelRenderer.allChanged();
			}
		});
	}
}
