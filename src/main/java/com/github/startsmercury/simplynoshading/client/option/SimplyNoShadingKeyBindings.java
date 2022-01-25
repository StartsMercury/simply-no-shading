package com.github.startsmercury.simplynoshading.client.option;

import static com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingKeyBinding.SHADING_CATEGORY;
import static com.github.startsmercury.simplynoshading.client.util.SimplyNoShadingInputUtil.GLFW_KEY_UNKNOWN;
import static net.fabricmc.api.EnvType.CLIENT;
import static net.minecraft.client.util.InputUtil.GLFW_KEY_F6;

import org.jetbrains.annotations.ApiStatus.Internal;

import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;

@Environment(CLIENT)
@Internal
public final class SimplyNoShadingKeyBindings {
	/**
	 * @see SimplyNoShadingGameOptions#keyCycleShadeAll()
	 */
	public static final KeyBinding KEY_CYCLE_SHADE_ALL;

	/**
	 * @see SimplyNoShadingGameOptions#keyCycleShadeBlocks()
	 */
	public static final KeyBinding KEY_CYCLE_SHADE_BLOCKS;

	/**
	 * @see SimplyNoShadingGameOptions#keyCycleShadeFluids()
	 */
	public static final KeyBinding KEY_CYCLE_SHADE_FLUIDS;

	static {
		KEY_CYCLE_SHADE_ALL = new KeyBinding("simply-no-shading.key.cycle_shade_all", GLFW_KEY_F6, SHADING_CATEGORY);
		KEY_CYCLE_SHADE_BLOCKS = new KeyBinding("simply-no-shading.key.cycle_shade_blocks", GLFW_KEY_UNKNOWN,
				SHADING_CATEGORY);
		KEY_CYCLE_SHADE_FLUIDS = new KeyBinding("simply-no-shading.key.cycle_shade_fluids", GLFW_KEY_UNKNOWN,
				SHADING_CATEGORY);
	}

	/**
	 * Registers all custom key bindings.
	 *
	 * @see KeyBindingHelper#registerKeyBinding
	 */
	public static void registerKeyBindings() {
		KeyBindingHelper.registerKeyBinding(KEY_CYCLE_SHADE_ALL);
		KeyBindingHelper.registerKeyBinding(KEY_CYCLE_SHADE_BLOCKS);
		KeyBindingHelper.registerKeyBinding(KEY_CYCLE_SHADE_FLUIDS);
	}
}
