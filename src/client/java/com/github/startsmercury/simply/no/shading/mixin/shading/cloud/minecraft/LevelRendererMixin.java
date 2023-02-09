package com.github.startsmercury.simply.no.shading.mixin.shading.cloud.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import net.minecraft.client.renderer.LevelRenderer;

/**
 * {@code LevelRenderer} mixin class.
 *
 * @since 5.0.0
 */
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	/**
	 * Changes the cloud shading by adjusting the applied brightness.
	 *
	 * @param brightness the original brightness
	 * @return the modified brightness
	 */
	@ModifyConstant(method = "buildClouds(Lcom/mojang/blaze3d/vertex/BufferBuilder;DDDLnet/minecraft/world/phys/Vec3;)Lcom/mojang/blaze3d/vertex/BufferBuilder$RenderedBuffer;",
	                constant = { @Constant(floatValue = 0.9F,
	                                       ordinal = 0),
	                        @Constant(floatValue = 0.9F,
	                                  ordinal = 1),
	                        @Constant(floatValue = 0.9F,
	                                  ordinal = 2),
	                        @Constant(floatValue = 0.7F,
	                                  ordinal = 0),
	                        @Constant(floatValue = 0.7F,
	                                  ordinal = 1),
	                        @Constant(floatValue = 0.7F,
	                                  ordinal = 2),
	                        @Constant(floatValue = 0.8F,
	                                  ordinal = 0),
	                        @Constant(floatValue = 0.8F,
	                                  ordinal = 1),
	                        @Constant(floatValue = 0.8F,
	                                  ordinal = 2) })
	private final float changeCloudBrightness(final float brightness) {
		return SimplyNoShading.getFirstInstance().getConfig().cloudShadingEnabled ? brightness : 1.0F;
	}
}
