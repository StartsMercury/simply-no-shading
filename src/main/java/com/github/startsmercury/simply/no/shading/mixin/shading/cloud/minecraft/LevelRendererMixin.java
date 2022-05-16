package com.github.startsmercury.simply.no.shading.mixin.shading.cloud.minecraft;

import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod;

import net.fabricmc.loader.api.FabricLoader;
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

	@ApiStatus.Experimental
	@ModifyVariable(method = "renderClouds(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/math/Matrix4f;FDDD)V",
	                at = @At("INVOKE"), name = "m")
	private final double holdClouds(final double offset) {
		return FabricLoader.getInstance().isDevelopmentEnvironment() ? offset : 0.0D;
	}
}
