package com.github.startsmercury.simplynoshading.mixin.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;

/**
 * Includes accessors to inaccessible members of the class
 * {@link SodiumGameOptionPages}
 */
@Mixin(SodiumGameOptionPages.class)
public interface SodiumGameOptionPagesAccessor {
	/**
	 * Returns the value of the private static field {@code vanillaOpts}.
	 *
	 * @return the vanilla option storage
	 */
	@Accessor(value = "vanillaOpts", remap = false)
	static MinecraftOptionsStorage getVanillaOpts() {
		throw new AssertionError();
	}
}
