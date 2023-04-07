package com.github.startsmercury.simply.no.shading.mixin.shading.block.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

/**
 * The {@code LiquidBlockRendererMixin} is a {@linkplain Mixin mixin} class for
 * the {@link LiquidBlockRenderer} class.
 *
 * @since 5.0.0
 */
@Mixin(value = LiquidBlockRenderer.class,
        priority = 999)
public class LiquidBlockRendererMixin {
	/**
	 * A private constructor that does nothing as of the writing of this
	 * documentation.
	 */
	private LiquidBlockRendererMixin() {
	}

	/**
	 * This is a {@linkplain ModifyArg argument modifier} that modifies the
	 * 2<sup>nd</sup> argument of all calls to
	 * {@link BlockAndTintGetter#getShade(Direction, boolean)} in
	 * {@link LiquidBlockRenderer#tesselate(BlockAndTintGetter, BlockPos, VertexConsumer, BlockState, FluidState)}.
	 * <p>
	 * Returns {@code true} if {@code shade} is {@code true} and
	 * {@link Config#blockShadingEnabled block shading is enabled}; {@code false}
	 * otherwise.
	 *
	 * @param shade the shade
	 * @return {@code true} if {@code shade} is {@code true} and
	 *         {@link Config#blockShadingEnabled block shading is enabled};
	 *         {@code false} otherwise
	 */
	@ModifyArg(
	        method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/material/FluidState;)Z",
	        at = @At(value = "INVOKE",
	                target = "Lnet/minecraft/world/level/BlockAndTintGetter;getShade(Lnet/minecraft/core/Direction;Z)F"),
	        index = 1)
	private final boolean changeShade(final boolean shade) {
		final var blockShadingEnabled = SimplyNoShading.getFirstInstance().getConfig().blockShadingEnabled;

		return shade && blockShadingEnabled;
	}
}
