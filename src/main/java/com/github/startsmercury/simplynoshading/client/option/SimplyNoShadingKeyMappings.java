package com.github.startsmercury.simplynoshading.client.option;

import static com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingKeyBinding.SHADING_CATEGORY;
import static com.github.startsmercury.simplynoshading.client.util.SimplyNoShadingInputUtil.GLFW_KEY_UNKNOWN;
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
	 * @see SimplyNoShadingGameOptions#keyCycleShadeAll()
	 */
	public static final KeyMapping KEY_CYCLE_SHADE_ALL;

	/**
	 * @see SimplyNoShadingGameOptions#keyCycleShadeBlocks()
	 */
	public static final KeyMapping KEY_CYCLE_SHADE_BLOCKS;

	/**
	 * @see SimplyNoShadingGameOptions#keyCycleShadeClouds()
	 */
	public static final KeyMapping KEY_CYCLE_SHADE_CLOUDS;

	/**
	 * @see SimplyNoShadingGameOptions#keyCycleShadeFluids()
	 */
	public static final KeyMapping KEY_CYCLE_SHADE_FLUIDS;

	static {
		KEY_CYCLE_SHADE_ALL = new KeyMapping("simply-no-shading.key.cycle_shade_all", KEY_F6, SHADING_CATEGORY);
		KEY_CYCLE_SHADE_BLOCKS = new KeyMapping("simply-no-shading.key.cycle_shade_blocks", GLFW_KEY_UNKNOWN,
				SHADING_CATEGORY);
		KEY_CYCLE_SHADE_CLOUDS = new KeyMapping("simply-no-shading.key.cycle_shade_clouds", GLFW_KEY_UNKNOWN,
				SHADING_CATEGORY);
		KEY_CYCLE_SHADE_FLUIDS = new KeyMapping("simply-no-shading.key.cycle_shade_fluids", GLFW_KEY_UNKNOWN,
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
