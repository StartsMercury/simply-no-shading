package com.github.startsmercury.simplynoshading.mixin.minecraft;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;

import com.github.startsmercury.simplynoshading.mixin.CompatibilityMixinPlugin;

/**
 * A simple {@linkplain IMixinConfigPlugin mixin plugin} that applies all mixins
 * under this.
 */
public class MinecraftMixinPlugin extends CompatibilityMixinPlugin {
	/**
	 * Creates a new {@code MinecraftMixinPlugin}.
	 */
	public MinecraftMixinPlugin() {
		// super("minecraft");
	}
}
