package com.github.startsmercury.simply.no.shading.mixin.shading.cloud.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod;

import net.minecraft.client.renderer.LevelRenderer;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	@ModifyConstant(method = "buildClouds(Lcom/mojang/blaze3d/vertex/BufferBuilder;DDDLnet/minecraft/world/phys/Vec3;)V",
	                constant = {
	                    @Constant(floatValue = 0.9F, ordinal = 0), @Constant(floatValue = 0.9F, ordinal = 1),
	                    @Constant(floatValue = 0.9F, ordinal = 2), @Constant(floatValue = 0.7F, ordinal = 0),
	                    @Constant(floatValue = 0.7F, ordinal = 1), @Constant(floatValue = 0.7F, ordinal = 2),
	                    @Constant(floatValue = 0.8F, ordinal = 0), @Constant(floatValue = 0.8F, ordinal = 1),
	                    @Constant(floatValue = 0.8F, ordinal = 2)
					})
	private final float changeCloudShade(final float shade) {
		return SimplyNoShadingClientMod.getInstance().config.wouldShadeClouds() ? shade : 1.0F;
	}
}
