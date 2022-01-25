package com.github.startsmercury.simplynoshading.mixin.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;

@Mixin(SodiumGameOptionPages.class)
public interface SodiumGameOptionPagesAccessor {
	@Accessor(value = "vanillaOpts", remap = false)
	static MinecraftOptionsStorage getVanillaOpts() {
		throw new AssertionError();
	}
}
