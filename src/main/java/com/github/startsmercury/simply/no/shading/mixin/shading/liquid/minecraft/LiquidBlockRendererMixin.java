package com.github.startsmercury.simply.no.shading.mixin.shading.liquid.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod;

import net.minecraft.client.renderer.block.LiquidBlockRenderer;

/**
 * {@code LiquidBlockRenderer} mixin class.
 */
@Mixin(value = LiquidBlockRenderer.class, priority = 999)
public class LiquidBlockRendererMixin {
	/**
	 * Modifies the liquid shade.
	 *
	 * @param shade the original shade
	 * @return the modified shade.
	 */
	@ModifyArg(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)Z",
	           at = @At(value = "INVOKE",
	                    target = "Lnet/minecraft/world/level/BlockAndTintGetter;getShade(Lnet/minecraft/core/Direction;Z)F"),
	           index = 1)
	private final boolean changeShade(final boolean shade) {
		return SimplyNoShadingClientMod.getInstance().config.shadingRules.liquids.wouldShade(shade);
	}
}
