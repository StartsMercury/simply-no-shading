package com.github.startsmercury.simply.no.shading.mixin.shading.block.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import net.minecraft.client.renderer.block.LiquidBlockRenderer;

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
	 * This is a {@linkplain ModifyConstant constant modifier} that modifies all
	 * 1st to 4th float constants with the value of {@code 0.5f} and 1st to 2nd
	 * float constants with the value of {@code 0.8f} and {@code 0.6f} in
	 * {@code LevelRenderer.buildClouds(BufferBuilder, double, double, double, Vec3)}.
	 * <p>
	 * Returns the original float constant value when
	 * {@linkplain Config#blockShadingEnabled block shading is enabled};
	 * {@code 1.0f} otherwise.
	 *
	 * @param constantValue the constant value
	 * @return the original float constant value when
	 *         {@linkplain Config#blockShadingEnabled block shading is enabled};
	 *         {@code 1.0f} otherwise
	 */
	@ModifyConstant(
	    method = "tesselate(Lnet/minecraft/world/level/BlockAndBiomeGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/BufferBuilder;Lnet/minecraft/world/level/material/FluidState;)Z",
	    constant = {
	        @Constant(floatValue = 0.5F, ordinal = 0),
	        @Constant(floatValue = 0.8F, ordinal = 0),
	        @Constant(floatValue = 0.6F, ordinal = 0),
	        @Constant(floatValue = 0.5F, ordinal = 1),
	        @Constant(floatValue = 0.5F, ordinal = 2),
	        @Constant(floatValue = 0.5F, ordinal = 3),
	        @Constant(floatValue = 0.8F, ordinal = 1),
	        @Constant(floatValue = 0.6F, ordinal = 1)
	    }
	)
	private final float changeVertexBrightness(final float constantValue) {
		final boolean blockShadingEnabled = SimplyNoShading.getFirstInstance().getConfig().blockShadingEnabled;

		if (blockShadingEnabled)
			return constantValue;
		else
			return 1.0f;
	}
}
