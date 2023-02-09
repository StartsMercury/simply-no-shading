package com.github.startsmercury.simply.no.shading.mixin.shading.block.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.FluidRenderer;

/**
 * {@code FluidRenderer} mixin class.
 */
@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
	/**
	 * Modifies the liquid shade.
	 *
	 * @param brightness the original brightness
	 * @return the modified brightness
	 */
	@ModifyVariable(method = "updateQuad(Lme/jellysquid/mods/sodium/client/model/quad/ModelQuadView;Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lme/jellysquid/mods/sodium/client/model/light/LightPipeline;Lnet/minecraft/core/Direction;FLme/jellysquid/mods/sodium/client/model/quad/blender/ColorSampler;Lnet/minecraft/world/level/material/FluidState;)V",
	                at = @At("HEAD"),
	                argsOnly = true)
	private final float changeShade(final float brightness) {
		return SimplyNoShading.getFirstInstance().getConfig().blockShadingEnabled ? brightness : 1.0F;
	}
}
