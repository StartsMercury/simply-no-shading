package com.github.startsmercury.simplynoshading.mixin.sodium;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;

import com.github.startsmercury.simplynoshading.mixin.CompatibilityMixinPlugin;

import net.fabricmc.loader.api.FabricLoader;

/**
 * A simple {@linkplain IMixinConfigPlugin mixin plugin} that applies all mixins
 * under this if {@linkplain #SODIUM_ID sodium}
 * {@linkplain FabricLoader#isModLoaded(String) is loaded}.
 */
public class SodiumMixinPlugin extends CompatibilityMixinPlugin {
	public SodiumMixinPlugin() {
		super("sodium");
	}
}
