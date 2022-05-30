package com.github.startsmercury.simply.no.shading.mixin.core.minecraft;

import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.LevelRenderer;

@Mixin(LevelRenderer.class)
public class LevelRendererFabricMixin {
	@ApiStatus.Experimental
	@ModifyVariable(method = "renderClouds(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/math/Matrix4f;FDDD)V",
	                at = @At(value = "STORE", ordinal = 0), name = "i")
	private final double holdClouds(final double i, final PoseStack poseStack, final Matrix4f projectionMatrix,
	    final float partialTick, final double camX, final double camY, final double camZ) {
		return FabricLoader.getInstance().isDevelopmentEnvironment() ? camX / 12.0D : i;
	}
}
