package com.github.startsmercury.simplynoshading.mixin.minecraft;

import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;

/**
 * Includes accessors to inaccessible members of the class
 * {@link SodiumGameOptionPages}
 */
@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {
	/**
	 * Returns the value of the private field {@code generateClouds}.
	 *
	 * @return {@code generateClouds}
	 */
	@Accessor(value = "generateClouds")
	boolean shouldGenerateClouds();

	/**
	 * Sets the value of the private field {@code generateClouds}.
	 *
	 * @param generateClouds the new value of {@code generateClouds}
	 */
	@Accessor(value = "generateClouds")
	void setGenerateClouds(boolean generateClouds);
}
