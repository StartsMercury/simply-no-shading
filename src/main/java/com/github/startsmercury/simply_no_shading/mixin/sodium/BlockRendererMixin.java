package com.github.startsmercury.simply_no_shading.mixin.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.github.startsmercury.simply_no_shading.util.ShadelessBlockRenderView;

import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.pipeline.BlockRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockRenderView;

/**
 * Changes {@link me.jellysquid.mods.sodium sodium}'s rendering to remove
 * shading from blocks.
 *
 * @see com.github.startsmercury.simply_no_shading.mixin.sodium
 * @see BlockRenderer
 * @see Mixin
 * @see #modifyShade(boolean)
 */
@Environment(EnvType.CLIENT)
@Mixin(BlockRenderer.class)
public abstract class BlockRendererMixin {
	private BlockRendererMixin() {
	}

	/**
	 * Always return {@code false}.
	 *
	 * @param shade
	 * @return {@code false}
	 * @see BlockRendererMixin
	 * @see ModifyArg
	 * @see BlockRenderer#renderQuadList(BlockRenderView, BlockState, BlockPos,
	 *      BlockPos, LightPipeline, Vec3d, ChunkModelBuilder, List,
	 *      ModelQuadFacing)
	 * @see ShadelessBlockRenderView#of
	 */
	@ModifyArg(method = "renderQuadList", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/model/light/LightPipeline;calculate(Lme/jellysquid/mods/sodium/client/model/quad/ModelQuadView;Lnet/minecraft/util/math/BlockPos;Lme/jellysquid/mods/sodium/client/model/light/data/QuadLightData;Lnet/minecraft/util/math/Direction;Z)V"), index = 4)
	private final boolean modifyShade(final boolean shade) {
		return false;
	}
}
