package com.github.startsmercury.simplynoshading.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.FluidRenderer;

@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
	@ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/BlockRenderView;getBrightness(Lnet/minecraft/util/math/Direction;Z)F"), index = 1)
	@SuppressWarnings("resource")
	private final boolean modifyShadedOnRender(final boolean shaded) {
		final SimplyNoShadingGameOptions options;

		options = (SimplyNoShadingGameOptions) MinecraftClient.getInstance().options;

		return shaded && (options.isShadeAll() || options.isShadeFluids());
	}
}
