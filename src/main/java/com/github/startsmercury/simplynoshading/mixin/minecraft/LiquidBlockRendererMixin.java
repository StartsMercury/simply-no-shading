package com.github.startsmercury.simplynoshading.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.github.startsmercury.simplynoshading.client.SimplyNoShadingOptions;

import me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;

/**
 * {@link Mixin mixin} for the class {@link FluidRenderer}.
 */
@Mixin(LiquidBlockRenderer.class)
public class LiquidBlockRendererMixin {
	/**
	 * Makes all fluid faces require either
	 * {@link SimplyNoShadingOptions#isShadeAll()} or
	 * {@link SimplyNoShadingOptions#isShadeFluids()} to return {@code true} to
	 * shade.
	 *
	 * @param shaded the raw shade
	 * @return the expected shade
	 * @implSpec {@code shaded && (isShadeAll || isShadeFluids())}
	 */
	@ModifyArg(
			method = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)Z",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/world/level/BlockAndTintGetter;getShade(Lnet/minecraft/core/Direction;Z)F"),
			index = 1)
	@SuppressWarnings("resource")
	private final boolean changeShade(final boolean shaded) {
		final SimplyNoShadingOptions options;

		options = (SimplyNoShadingOptions) Minecraft.getInstance().options;

		return shaded && (options.isShadeAll() || options.isShadeFluids());
	}
}
