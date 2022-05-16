package com.github.startsmercury.simply.no.shading.mixin.core.minecraft;

import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simply.no.shading.impl.CloudRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.LevelRenderer;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin implements CloudRenderer {
	@Shadow
	private boolean generateClouds;

	@Override
	public void generateClouds() {
		this.generateClouds = true;
	}

	@ApiStatus.Experimental
	@ModifyVariable(method = "renderClouds(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/math/Matrix4f;FDDD)V",
	                at = @At(value = "STORE", ordinal = 0), name = "m")
	private final double holdClouds(final double m, final PoseStack poseStack, final Matrix4f matrix4f, final float f,
	    final double d, final double e, final double g) {
		return FabricLoader.getInstance().isDevelopmentEnvironment() ? d / 12.0D : m;
	}
}
