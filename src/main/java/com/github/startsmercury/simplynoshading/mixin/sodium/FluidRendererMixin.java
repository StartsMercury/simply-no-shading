package com.github.startsmercury.simplynoshading.mixin.sodium;

import static net.minecraft.util.math.Direction.DOWN;
import static net.minecraft.util.math.Direction.UP;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.model.quad.blender.ColorSampler;
import me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
	@ModifyVariable(method = "calculateQuadColors", at = @At("HEAD"), argsOnly = true, remap = false)
	private final float modifyBrightnessOnCalculateQuadColors(final float target, final ModelQuadView quad,
			final BlockRenderView world, final BlockPos pos, final LightPipeline lighter, final Direction dir,
			final float brightness, final ColorSampler<FluidState> colorSampler, final FluidState fluidState) {
		return world.getBrightness(dir == DOWN ? UP : dir, true);
	}
}
