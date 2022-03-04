package com.github.startsmercury.simplynoshading.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.github.startsmercury.simplynoshading.client.SimplyNoShadingOptions;
import com.mojang.blaze3d.vertex.VertexConsumer;

import me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.FluidState;

/**
 * {@link Mixin mixin} for the class {@link FluidRenderer}.
 */
@Mixin(LiquidBlockRenderer.class)
public class LiquidBlockRendererMixin {
	/**
	 * @see net.minecraft.world.level.BlockAndTintGetter BlockAndTintGetter
	 */
	private enum BlockAndTintGetter {
		;

		/**
		 * @see net.minecraft.world.level.BlockAndTintGetter#getShade(Direction,
		 *      boolean) BlockAndTintGetter.getShade(Direction, boolean)
		 */
		private static final String getShade = "Lnet/minecraft/world/level/BlockAndTintGetter;getShade(Lnet/minecraft/core/Direction;Z)F";
	}

	/**
	 * @see LiquidBlockRenderer#tesselate(BlockAndTintGetter, BlockPos,
	 *      VertexConsumer, FluidState)
	 */
	private static final String tesselate = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)Z";

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
	@ModifyArg(method = tesselate, at = @At(value = "INVOKE", target = BlockAndTintGetter.getShade), index = 1)
	@SuppressWarnings("resource")
	private final boolean changeShade(final boolean shaded) {
		final SimplyNoShadingOptions options;

		options = (SimplyNoShadingOptions) Minecraft.getInstance().options;

		return shaded && (options.isShadeAll() || options.isShadeFluids());
	}
}
