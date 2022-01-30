package com.github.startsmercury.simplynoshading.client.event;

import org.jetbrains.annotations.ApiStatus.Internal;

import com.github.startsmercury.simplynoshading.client.SimplyNoShadingKeyMappings;
import com.github.startsmercury.simplynoshading.client.SimplyNoShadingOptions;
import com.github.startsmercury.simplynoshading.entrypoint.SimplyNoShadingClientMod;
import com.github.startsmercury.simplynoshading.mixin.minecraft.LevelRendererAccessor;

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
			final SimplyNoShadingOptions clientOptions;
			boolean changed;

			clientOptions = (SimplyNoShadingOptions) minecraft.options;
			changed = false;

			if (clientOptions.keyCycleShadeAll().consumeClick()) {
				changed = true;

				clientOptions.cycleShadeAll();
			}

			if (clientOptions.keyCycleShadeBlocks().consumeClick()) {
				changed = true;

				clientOptions.cycleShadeBlocks();
			}

			if (clientOptions.keyCycleShadeClouds().consumeClick()) {
				clientOptions.cycleShadeClouds();

				((LevelRendererAccessor) minecraft.levelRenderer).setGenerateClouds(true);
			}

			if (clientOptions.keyCycleShadeFluids().consumeClick()) {
				changed = true;

				clientOptions.cycleShadeFluids();
			}

			if (changed) {
				minecraft.levelRenderer.allChanged();
			}
		});
	}
}
