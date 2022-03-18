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
	 * @param shade the raw shade
	 * @return the expected shade
	 * @implSpec {@code hasShade() && (isShadeAll || isShadeClouds())}
	 */
	@ModifyConstant(
			method = "Lnet/minecraft/client/renderer/LevelRenderer;buildClouds(Lcom/mojang/blaze3d/vertex/BufferBuilder;DDDLnet/minecraft/world/phys/Vec3;)V",
			constant = { @Constant(floatValue = 0.9F, ordinal = 0), @Constant(floatValue = 0.9F, ordinal = 1),
					@Constant(floatValue = 0.9F, ordinal = 2), @Constant(floatValue = 0.7F, ordinal = 0),
					@Constant(floatValue = 0.7F, ordinal = 1), @Constant(floatValue = 0.7F, ordinal = 2),
					@Constant(floatValue = 0.8F, ordinal = 0), @Constant(floatValue = 0.8F, ordinal = 1),
					@Constant(floatValue = 0.8F, ordinal = 2) })
	private float changeCloudShade(final float shade) {
		@SuppressWarnings("resource")
		final SimplyNoShadingOptions options = (SimplyNoShadingOptions) Minecraft.getInstance().options;

		return options.isShadeAll() || options.isShadeClouds() ? shade : 1.0F;
	}

	/**
	 * Makes the clouds not move.
	 */
	//	@ModifyVariable(method = "Lnet/minecraft/client/renderer/LevelRenderer;renderClouds(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/math/Matrix4f;FDDD)V", at = @At("INVOKE"), name = "m")
	//	private double holdClouds(final double shade) {
	//		return 0.0D;
	//	}
}
