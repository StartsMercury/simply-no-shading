package com.github.startsmercury.simplynoshading.client;

import static com.github.startsmercury.simplynoshading.blaze3d.platform.SimplyNoShadingInputConstants.KEY_UNKNOWN;
import static com.github.startsmercury.simplynoshading.client.SimplyNoShadingKeyMapping.SHADING_CATEGORY;
import static net.fabricmc.api.EnvType.CLIENT;
import static net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding;
import static com.mojang.blaze3d.platform.InputConstants.KEY_F6;

import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.ApiStatus.Internal;

import com.github.startsmercury.simplynoshading.client.event.SimplyNoShadingLifecycleEvents;
import com.github.startsmercury.simplynoshading.entrypoint.SimplyNoShadingClientMod;

import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

/**
 * This class contains the method {@link #registerKeyBindings()} which is called
 * by {@link SimplyNoShadingClientMod#onInitializeClient()}.
 */
@Environment(CLIENT)
@Internal
public final class SimplyNoShadingKeyMappings {
	/**
	 * @see SimplyNoShadingOptions#keyCycleShadeAll()
	 */
	public static final KeyMapping KEY_CYCLE_SHADE_ALL;

	/**
	 * @see SimplyNoShadingOptions#keyCycleShadeBlocks()
	 */
	public static final KeyMapping KEY_CYCLE_SHADE_BLOCKS;

	/**
	 * @see SimplyNoShadingOptions#keyCycleShadeClouds()
	 */
	public static final KeyMapping KEY_CYCLE_SHADE_CLOUDS;

	/**
	 * @see SimplyNoShadingOptions#keyCycleShadeFluids()
	 */
	public static final KeyMapping KEY_CYCLE_SHADE_FLUIDS;

	static {
		KEY_CYCLE_SHADE_ALL = new KeyMapping("simply-no-shading.key.cycle_shade_all", KEY_F6, SHADING_CATEGORY);
		KEY_CYCLE_SHADE_BLOCKS = new KeyMapping("simply-no-shading.key.cycle_shade_blocks", KEY_UNKNOWN,
				SHADING_CATEGORY);
		KEY_CYCLE_SHADE_CLOUDS = new KeyMapping("simply-no-shading.key.cycle_shade_clouds", KEY_UNKNOWN,
				SHADING_CATEGORY);
		KEY_CYCLE_SHADE_FLUIDS = new KeyMapping("simply-no-shading.key.cycle_shade_fluids", KEY_UNKNOWN,
				SHADING_CATEGORY);
	}

	/**
	 * {@linkplain KeyBindingHelper#registerKeyBinding(KeyMapping) Registers} all
	 * custom key bindings. This method should only be called once by
	 * {@link SimplyNoShadingClientMod#onInitializeClient()} before
	 * {@link SimplyNoShadingLifecycleEvents#registerLifecycleEvents()}.
	 */
	public static void registerKeyBindings() {
		registerKeyBinding(KEY_CYCLE_SHADE_ALL);
		registerKeyBinding(KEY_CYCLE_SHADE_BLOCKS);
		registerKeyBinding(KEY_CYCLE_SHADE_CLOUDS);
		registerKeyBinding(KEY_CYCLE_SHADE_FLUIDS);
	}
}
