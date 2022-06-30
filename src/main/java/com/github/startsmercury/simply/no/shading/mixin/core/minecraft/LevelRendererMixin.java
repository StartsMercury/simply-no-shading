package com.github.startsmercury.simply.no.shading.mixin.core.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.github.startsmercury.simply.no.shading.impl.CloudRenderer;

import net.minecraft.client.renderer.LevelRenderer;

/**
 * {@code LevelRenderer} mixin class.
 *
 * @since 5.0.0
 */
@Mixin(LevelRenderer.class)
public class LevelRendererMixin implements CloudRenderer {
	/**
	 * Generate clouds on the next rendered frame.
	 */
	@Shadow
	private boolean generateClouds;

	/**
	 * Sets {@link #generateClouds} to {@code true}.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public void generateClouds() {
		this.generateClouds = true;
	}
}
