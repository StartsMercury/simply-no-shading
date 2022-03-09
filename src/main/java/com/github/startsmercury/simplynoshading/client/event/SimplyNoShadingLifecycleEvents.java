package com.github.startsmercury.simplynoshading.client.event;

import org.jetbrains.annotations.ApiStatus.Internal;

import com.github.startsmercury.simplynoshading.client.SimplyNoShadingKeyMappings;
import com.github.startsmercury.simplynoshading.client.SimplyNoShadingOptions;
import com.github.startsmercury.simplynoshading.entrypoint.SimplyNoShadingClientMod;
import com.github.startsmercury.simplynoshading.mixin.minecraft.LevelRendererAccessor;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

/**
 * This class contains the method {@link #registerLifecycleEvents()} which is
 * called by {@link SimplyNoShadingClientMod#onInitializeClient()}.
 */
@Internal
public class SimplyNoShadingLifecycleEvents {
	private static boolean consumeClick(final KeyMapping keyMapping, final Runnable action) {
		if (keyMapping.consumeClick()) {
			action.run();

			return true;
		} else {
			return false;
		}
	}

	private static void onClientEndTick(final Minecraft client) {
		final SimplyNoShadingOptions clientOptions = (SimplyNoShadingOptions) client.options;

		final boolean blocksOrFluidsChanged = consumeClick(clientOptions.keyCycleShadeAll(),
				clientOptions::cycleShadeAll) |
				consumeClick(clientOptions.keyCycleShadeBlocks(), clientOptions::cycleShadeBlocks) |
				consumeClick(clientOptions.keyCycleShadeFluids(), clientOptions::cycleShadeFluids);
		final boolean cloudsChanged = consumeClick(clientOptions.keyCycleShadeClouds(),
				clientOptions::cycleShadeClouds);

		if (blocksOrFluidsChanged) {
			client.levelRenderer.allChanged();
		} else if (cloudsChanged) {
			((LevelRendererAccessor) client.levelRenderer).setGenerateClouds(true);
		}
	}

	/**
	 * {@linkplain Event#register(Object) Registers}
	 * {@linkplain ClientTickEvents#END_CLIENT_TICK tick} listeners that anticipates
	 * key binding(s) being pressed. This method should only be called once by
	 * {@link SimplyNoShadingClientMod#onInitializeClient()} after
	 * {@link SimplyNoShadingKeyMappings#registerKeyBindings()}.
	 */
	public static void registerLifecycleEvents() {
		ClientTickEvents.END_CLIENT_TICK.register(SimplyNoShadingLifecycleEvents::onClientEndTick);
	}
}
