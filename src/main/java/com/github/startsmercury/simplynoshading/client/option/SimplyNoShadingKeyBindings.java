package com.github.startsmercury.simplynoshading.client.option;

import static net.fabricmc.api.EnvType.CLIENT;
import static net.minecraft.client.util.InputUtil.GLFW_KEY_F6;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Environment(CLIENT)
public final class SimplyNoShadingKeyBindings {
	private static final KeyBinding TOGGLE_KEY;

	private static List<KeyBinding> preRegistry;

	static {
		preRegistry = new LinkedList<>();

		TOGGLE_KEY = keyBinding("key.simply-no-shading.toggle_shading", GLFW_KEY_F6, "key.categories.misc");
	}

	public static KeyBinding getToggleKey() {
		return TOGGLE_KEY;
	}

	private static KeyBinding keyBinding(final String translationKey, final InputUtil.Type type, final int code,
			final String category) {
		final KeyBinding keyBinding = new KeyBinding(translationKey, code, category);

		preRegistry.add(keyBinding);

		return keyBinding;
	}

	private static KeyBinding keyBinding(final String translationKey, final int code, final String category) {
		return keyBinding(translationKey, InputUtil.Type.KEYSYM, code, category);
	}

	public static void registerKeyBindings() {
		final Iterator<KeyBinding> preRegistryIterator;

		preRegistryIterator = preRegistry.iterator();
		preRegistry = Collections.emptyList();

		while (preRegistryIterator.hasNext()) {
			KeyBindingHelper.registerKeyBinding(preRegistryIterator.next());

			preRegistryIterator.remove();
		}
	}
}
