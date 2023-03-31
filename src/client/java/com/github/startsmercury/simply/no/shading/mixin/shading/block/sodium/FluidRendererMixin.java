package com.github.startsmercury.simply.no.shading.mixin.shading.block.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer;

/**
 * The {@code FluidRendererMixin} is a {@linkplain Mixin mixin} class for the
 * {@link FluidRenderer} class.
 *
 * @since 5.0.0
 */
@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
	/**
	 * A private constructor that does nothing as of the writing of this
	 * documentation.
	 */
	private FluidRendererMixin() {
	}

	/**
	 * This is a {@linkplain ModifyVariable variable modifier} that modifies the
	 * parameter {@code brightness} in
	 * {@code FluidRenderer.updateQuad(ModelQuadView, BlockAndTintGetter, BlockPos, LightPipeline, Direction, float, ColorSampler, FluidState)}.
	 * <p>
	 * Returns the {@code brightness} when {@linkplain Config#blockShadingEnabled
	 * block shading is enabled}; {@code 1.0f} otherwise.
	 *
	 * @param brightness the brightness
	 * @return the block{@code brightness} when
	 *         {@linkplain Config#blockShadingEnabled block shading is enabled};
	 *         {@code 1.0f} otherwise
	 */
	@ModifyVariable(method = "calculateQuadColors(Lme/jellysquid/mods/sodium/client/model/quad/ModelQuadView;Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lme/jellysquid/mods/sodium/client/model/light/LightPipeline;Lnet/minecraft/core/Direction;FLme/jellysquid/mods/sodium/client/model/quad/blender/ColorSampler;Lnet/minecraft/world/level/material/FluidState;)V",
	                at = @At("HEAD"),
	                argsOnly = true)
	private final float changeShade(final float brightness) {
		final var blockShadingEnabled = SimplyNoShading.getFirstInstance().getConfig().blockShadingEnabled;

		if (blockShadingEnabled)
			return brightness;
		else
			return 1.0F;
	}
}
