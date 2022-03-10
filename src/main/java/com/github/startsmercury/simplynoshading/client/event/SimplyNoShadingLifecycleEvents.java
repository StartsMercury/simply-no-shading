package com.github.startsmercury.simplynoshading.client.event;

import static net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK;

import org.jetbrains.annotations.ApiStatus.Internal;

import com.github.startsmercury.simplynoshading.client.SimplyNoShadingKeyMappings;
import com.github.startsmercury.simplynoshading.client.SimplyNoShadingOptions;
import com.github.startsmercury.simplynoshading.entrypoint.SimplyNoShadingClientMod;
import com.github.startsmercury.simplynoshading.mixin.minecraft.LevelRendererAccessor;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

/**
 * This class contains the method {@link #registerClientTickListeners()} which is
 * called by {@link SimplyNoShadingClientMod#onInitializeClient()}.
 */
@Internal
public final class SimplyNoShadingLifecycleEvents {
	/**
	 * Performs a given action if a given key mapping was pressed.
	 *
	 * @param keyMapping the key mapping
	 * @param action     the action
	 * @return {@code true} if the key mapping was pressed
	 */
	private static boolean consumeClick(final KeyMapping keyMapping, final Runnable action) {
		if (keyMapping.consumeClick()) {
			action.run();

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Called after every client tick.
	 *
	 * @param client the client
	 */
	private static void onClientTickEnd(final Minecraft client) {
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
	 * Registers listeners that anticipate key mapping presses.
	 * <p>
	 * This method should only be called once by
	 * {@link SimplyNoShadingClientMod#onInitializeClient()} after
	 * {@link SimplyNoShadingKeyMappings#registerKeyBindings()}.
	 */
	public static void registerClientTickListeners() {
		END_CLIENT_TICK.register(SimplyNoShadingLifecycleEvents::onClientTickEnd);
	}
}
