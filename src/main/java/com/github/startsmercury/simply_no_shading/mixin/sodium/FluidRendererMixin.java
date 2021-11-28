package com.github.startsmercury.simply_no_shading.mixin.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simply_no_shading.util.ShadelessBlockRenderView;

import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadColorProvider;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

/**
 * Changes {@link me.jellysquid.mods.sodium sodium}'s rendering to remove
 * shading from fluids.
 *
 * @see com.github.startsmercury.simply_no_shading.mixin.sodium
 * @see FluidRenderer
 * @see Mixin
 * @see #modifyBrightness(float)
 */
@Environment(EnvType.CLIENT)
@Mixin(FluidRenderer.class)
public abstract class FluidRendererMixin {
	private FluidRendererMixin() {
	}

	/**
	 * Always return {@code 1}.
	 *
	 * @param brightness
	 * @return {@code 1}
	 * @see FluidRendererMixin
	 * @see ModifyVariable
	 * @see FluidRenderer#calculateQuadColors(ModelQuadView, BlockRenderView,
	 *      BlockPos, LightPipeline, Direction, float, ModelQuadColorProvider,
	 *      FluidState)
	 * @see ShadelessBlockRenderView#of
	 */
	@ModifyVariable(method = "calculateQuadColors", at = @At(value = "INVOKE"), argsOnly = true)
	private final float modifyBrightness(final float brightness) {
		return 1.0F;
	}
}
