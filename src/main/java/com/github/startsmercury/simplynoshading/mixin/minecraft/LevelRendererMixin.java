package com.github.startsmercury.simplynoshading.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.github.startsmercury.simplynoshading.client.SimplyNoShadingOptions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;

/**
 * {@link Mixin mixin} for the class {@link LevelRendererMixin}.
 */
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	/**
	 * Makes all cloud faces require either
	 * {@link SimplyNoShadingOptions#isShadeAll()} or
	 * {@link SimplyNoShadingOptions#isShadeClouds()} to return {@code true} to
	 * shade.
	 *
	 * @implSpec {@code hasShade() && (isShadeAll || isShadeClouds())}
	 */
	@ModifyConstant(method = "buildClouds", constant = { @Constant(floatValue = 0.9F, ordinal = 0),
			@Constant(floatValue = 0.9F, ordinal = 1), @Constant(floatValue = 0.9F, ordinal = 2),
			@Constant(floatValue = 0.7F, ordinal = 0), @Constant(floatValue = 0.7F, ordinal = 1),
			@Constant(floatValue = 0.7F, ordinal = 2), @Constant(floatValue = 0.8F, ordinal = 0),
			@Constant(floatValue = 0.8F, ordinal = 1), @Constant(floatValue = 0.8F, ordinal = 2) })
	@SuppressWarnings("resource")
	private float changeCloudShade(final float shade) {
		final SimplyNoShadingOptions options;

		options = (SimplyNoShadingOptions) Minecraft.getInstance().options;

		return options.isShadeAll() || options.isShadeClouds() ? shade : 1.0F;
	}
}
