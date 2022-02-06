package com.github.startsmercury.simplynoshading.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.renderer.LevelRenderer;

/**
 * Includes accessors to inaccessible members of the class {@link LevelRenderer}
 */
@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {
	/**
	 * Sets the value of the private field {@code generateClouds}.
	 *
	 * @param generateClouds the new value of {@code generateClouds}
	 */
	@Accessor(value = "generateClouds")
	void setGenerateClouds(boolean generateClouds);

	/**
	 * Returns the value of the private field {@code generateClouds}.
	 *
	 * @return {@code generateClouds}
	 */
	@Accessor(value = "generateClouds")
	boolean shouldGenerateClouds();
}
