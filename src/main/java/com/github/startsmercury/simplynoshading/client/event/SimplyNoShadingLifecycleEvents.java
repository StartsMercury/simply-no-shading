package com.github.startsmercury.simplynoshading.client.event;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;
import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingKeyBindings;

import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

@Environment(CLIENT)
public class SimplyNoShadingLifecycleEvents {
	public static void registerLifecycleEvents() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (SimplyNoShadingKeyBindings.TOGGLE_KEY.wasPressed()) {
				final MinecraftClient minecraftClient = MinecraftClient.getInstance();

				((SimplyNoShadingGameOptions) minecraftClient.options).toggleShading();

				minecraftClient.worldRenderer.reload();
			}
		});
	}
}
