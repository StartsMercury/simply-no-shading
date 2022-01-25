package com.github.startsmercury.simplynoshading.client.event;

import static net.fabricmc.api.EnvType.CLIENT;

import org.jetbrains.annotations.ApiStatus.Internal;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;

import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@Environment(CLIENT)
@Internal
public class SimplyNoShadingLifecycleEvents {
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
