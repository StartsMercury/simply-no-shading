package com.github.startsmercury.simplynoshading.mixin.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simplynoshading.client.SimplyNoShadingOptions;

import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.model.quad.blender.ColorSampler;
import me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;

/**
 * {@link Mixin mixin} for the class {@link FluidRenderer}.
 */
@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
	/**
	 * Makes all fluid faces (rendered by sodium) require either
	 * {@link SimplyNoShadingOptions#isShadeAll()} or
	 * {@link SimplyNoShadingOptions#isShadeFluids()} to return {@code true} to
	 * shade.
	 *
	 * @param dir the raw shade
	 * @return the expected shade
	 * @implSpec {@code shaded && (isShadeAll || isShadeFluids())}
	 */
	@ModifyVariable(
			method = "Lme/jellysquid/mods/sodium/client/render/pipeline/FluidRenderer;calculateQuadColors(Lme/jellysquid/mods/sodium/client/model/quad/ModelQuadView;Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lme/jellysquid/mods/sodium/client/model/light/LightPipeline;Lnet/minecraft/core/Direction;FLme/jellysquid/mods/sodium/client/model/quad/blender/ColorSampler;Lnet/minecraft/world/level/material/FluidState;)V",
			at = @At("HEAD"), argsOnly = true)
	@SuppressWarnings("resource")
	private final float changeShade(final float target, final ModelQuadView quad, final BlockAndTintGetter world,
			final BlockPos pos, final LightPipeline lighter, final Direction dir, final float brightness,
			final ColorSampler<FluidState> colorSampler, final FluidState fluidState) {
		final SimplyNoShadingOptions options;

		options = (SimplyNoShadingOptions) Minecraft.getInstance().options;

		return world.getShade(dir == Direction.DOWN ? Direction.UP : dir,
				options.isShadeAll() || options.isShadeFluids());
	}
}
