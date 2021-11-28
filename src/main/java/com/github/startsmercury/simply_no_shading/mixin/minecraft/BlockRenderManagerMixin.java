package com.github.startsmercury.simply_no_shading.mixin.minecraft;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simply_no_shading.util.ShadelessBlockRenderView;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

/**
 * Changes {@link net.minecraft minecraft}'s rendering to remove shading.
 *
 * @see com.github.startsmercury.simply_no_shading.mixin.minecraft
 * @see BlockRenderManager
 * @see Mixin
 * @see #modifyWorld(BlockRenderView)
 */
@Mixin(BlockRenderManager.class)
public abstract class BlockRenderManagerMixin {
	private BlockRenderManagerMixin() {
	}

	/**
	 * Wraps a world value as an instance of {@link ShadelessBlockRenderView}.
	 *
	 * @param world the world
	 * @return an instance of {@link ShadelessBlockRenderView}
	 * @see BlockRenderManagerMixin
	 * @see BlockRenderView
	 * @see ModifyVariable
	 * @see BlockRenderManager#renderBlock(BlockState, BlockPos, BlockRenderView,
	 *      MatrixStack, VertexConsumer, boolean, Random)
	 * @see BlockRenderManager#renderDamage(BlockState, BlockPos, BlockRenderView,
	 *      MatrixStack, VertexConsumer)
	 * @see BlockRenderManager#renderFluid(BlockPos, BlockRenderView,
	 *      VertexConsumer, FluidState)
	 * @see ShadelessBlockRenderView#of
	 */
	@ModifyVariable(method = { "renderBlock", "renderDamage", "renderFluid" }, at = @At("HEAD"), argsOnly = true)
	private BlockRenderView modifyWorld(final BlockRenderView world) {
		return ShadelessBlockRenderView.of(world);
	}
}
