package com.github.startsmercury.simplynoshading.client.option;

import static net.fabricmc.api.EnvType.CLIENT;
import static net.minecraft.client.util.InputUtil.GLFW_KEY_F6;

import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;

@Environment(CLIENT)
public final class SimplyNoShadingKeyBindings {
	private static final KeyBinding TOGGLE_KEY;

	static {
		TOGGLE_KEY = new KeyBinding("key.simply-no-shading.toggle_shading", GLFW_KEY_F6, "key.categories.misc");
	}

	public static KeyBinding getToggleKey() {
		return TOGGLE_KEY;
	}

	public static void registerKeyBindings() {
		KeyBindingHelper.registerKeyBinding(TOGGLE_KEY);
	}
}
