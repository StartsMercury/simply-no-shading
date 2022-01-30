package com.github.startsmercury.simplynoshading.mixin.sodium;

import static net.minecraft.util.math.Direction.DOWN;
import static net.minecraft.util.math.Direction.UP;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;

import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.model.quad.blender.ColorSampler;
import me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

/**
 * {@link Mixin mixin} for the class {@link FluidRenderer}.
 */
@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
	/**
	 * Makes all fluids (rendered by sodium) require either
	 * {@link SimplyNoShadingGameOptions#isShadeAll()} or
	 * {@link SimplyNoShadingGameOptions#isShadeFluids()} to return {@code true} to
	 * shade.
	 *
	 * @param shaded the raw shade
	 * @return the expected shade
	 * @implSpec {@code shaded && (isShadeAll || isShadeFluids())}
	 */
	@ModifyVariable(method = "calculateQuadColors", at = @At("HEAD"), argsOnly = true, remap = false)
	@SuppressWarnings("resource")
	private final float changeShade(final float target, final ModelQuadView quad, final BlockRenderView world,
			final BlockPos pos, final LightPipeline lighter, final Direction dir, final float brightness,
			final ColorSampler<FluidState> colorSampler, final FluidState fluidState) {
		final SimplyNoShadingGameOptions options;

		options = (SimplyNoShadingGameOptions) MinecraftClient.getInstance().options;

		return world.getBrightness(dir == DOWN ? UP : dir, options.isShadeAll() || options.isShadeFluids());
	}
}
