package com.github.startsmercury.simplynoshading.client.event;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simplynoshading.SimplyNoShading;
import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingKeyBindings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

@Environment(CLIENT)
public class SimplyNoShadingLifecycleEvents {
	@SuppressWarnings("resource")
	public static void registerLifecycleEvents() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (SimplyNoShadingKeyBindings.getToggleKey().wasPressed()) {
				JsonElement config;

				config = SimplyNoShading.getConfig();

				if (!config.isJsonObject()) {
					config = new JsonObject();
				}

				config.getAsJsonObject().addProperty("shading", !SimplyNoShading.getBakedConfig().shading());

				SimplyNoShading.setConfig(config);
				SimplyNoShading.bakeConfig();

				MinecraftClient.getInstance().worldRenderer.reload();
			}
		});
	}
}
