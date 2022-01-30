package com.github.startsmercury.simplynoshading.client.event;

import org.jetbrains.annotations.ApiStatus.Internal;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;
import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingKeyBindings;
import com.github.startsmercury.simplynoshading.entrypoint.SimplyNoShadingClientMod;
import com.github.startsmercury.simplynoshading.mixin.minecraft.WorldRendererAccessor;

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
	 * {@link SimplyNoShadingKeyBindings#registerKeyBindings()}.
	 */
	public static void registerLifecycleEvents() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			final SimplyNoShadingGameOptions clientOptions;
			boolean reload;

			clientOptions = (SimplyNoShadingGameOptions) client.options;
			reload = false;

			if (clientOptions.keyCycleShadeAll().wasPressed()) {
				reload = true;

				clientOptions.cycleShadeAll();
			}

			if (clientOptions.keyCycleShadeBlocks().wasPressed()) {
				reload = true;

				clientOptions.cycleShadeBlocks();
			}

			if (clientOptions.keyCycleShadeClouds().wasPressed()) {
				clientOptions.cycleShadeClouds();

				((WorldRendererAccessor) client.worldRenderer).setCloudsDirty(true);
			}

			if (clientOptions.keyCycleShadeFluids().wasPressed()) {
				reload = true;

				clientOptions.cycleShadeFluids();
			}

			if (reload) {
				client.worldRenderer.reload();
			}
		});
	}
}
