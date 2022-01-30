package com.github.startsmercury.simplynoshading.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;

/**
 * {@link Mixin mixin} for the class {@link WorldRendererMixin}.
 */
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	/**
	 * Makes all model faces require either
	 * {@link SimplyNoShadingGameOptions#isShadeAll()} or
	 * {@link SimplyNoShadingGameOptions#isShadeClouds()} to return {@code true} to
	 * shade.
	 *
	 * @param callback the callback
	 * @implSpec {@code hasShade() && (isShadeAll || isShadeClouds())}
	 */
	@ModifyConstant(method = "renderClouds(Lnet/minecraft/client/render/BufferBuilder;DDDLnet/minecraft/util/math/Vec3d;)V", constant = {
			@Constant(floatValue = 0.9F, ordinal = 0), @Constant(floatValue = 0.9F, ordinal = 1),
			@Constant(floatValue = 0.9F, ordinal = 2), @Constant(floatValue = 0.7F, ordinal = 0),
			@Constant(floatValue = 0.7F, ordinal = 1), @Constant(floatValue = 0.7F, ordinal = 2),
			@Constant(floatValue = 0.8F, ordinal = 0), @Constant(floatValue = 0.8F, ordinal = 1),
			@Constant(floatValue = 0.8F, ordinal = 2) })
	@SuppressWarnings("resource")
	private float changeCloudShade(final float shade) {
		final SimplyNoShadingGameOptions options;

		options = (SimplyNoShadingGameOptions) MinecraftClient.getInstance().options;

		return options.isShadeAll() || options.isShadeClouds() ? shade : 1.0F;
	}
}
