package com.github.startsmercury.simply.no.shading.mixin.shading.cloud.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import net.minecraft.client.renderer.LevelRenderer;

/**
 * The {@code LevelRendererMixin} is a {@linkplain Mixin mixin} class for the
 * {@link LevelRenderer} class.
 *
 * @since 5.0.0
 */
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	/**
	 * A private constructor that does nothing as of the writing of this
	 * documentation.
	 */
	private LevelRendererMixin() {
	}

	/**
	 * This is a {@linkplain ModifyConstant constant modifier} that modifies all 1st
	 * to 3rd float constants with the value of {@code 0.7f}, {@code 0.8f}, and
	 * {@code 0.9f} in
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
	@ModifyConstant(method = "buildClouds(Lcom/mojang/blaze3d/vertex/BufferBuilder;DDDLnet/minecraft/world/phys/Vec3;)V",
	                constant = { @Constant(floatValue = 0.9f,
	                                       ordinal = 0),
	                        @Constant(floatValue = 0.9f,
	                                  ordinal = 1),
	                        @Constant(floatValue = 0.9f,
	                                  ordinal = 2),
	                        @Constant(floatValue = 0.7f,
	                                  ordinal = 0),
	                        @Constant(floatValue = 0.7f,
	                                  ordinal = 1),
	                        @Constant(floatValue = 0.7f,
	                                  ordinal = 2),
	                        @Constant(floatValue = 0.8f,
	                                  ordinal = 0),
	                        @Constant(floatValue = 0.8f,
	                                  ordinal = 1),
	                        @Constant(floatValue = 0.8f,
	                                  ordinal = 2) })
	private final float changeCloudBrightness(final float constantValue) {
		final boolean cloudShadingEnabled = SimplyNoShading.getFirstInstance().getConfig().cloudShadingEnabled;

		if (cloudShadingEnabled)
			return constantValue;
		else
			return 1.0f;
	}
}
