package com.github.startsmercury.simplynoshading.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.FluidRenderer;

/**
 * {@link Mixin mixin} for the class {@link FluidRenderer}.
 */
@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
	/**
	 * Makes all fluids require either
	 * {@link SimplyNoShadingGameOptions#isShadeAll()} or
	 * {@link SimplyNoShadingGameOptions#isShadeFluids()} to return {@code true} to
	 * shade.
	 *
	 * @param shaded the raw shade
	 * @return the expected shade
	 * @implSpec {@code shaded && (isShadeAll || isShadeFluids())}
	 */
	@ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/BlockRenderView;getBrightness(Lnet/minecraft/util/math/Direction;Z)F"), index = 1)
	@SuppressWarnings("resource")
	private final boolean changeShade(final boolean shaded) {
		final SimplyNoShadingGameOptions options;

		options = (SimplyNoShadingGameOptions) MinecraftClient.getInstance().options;

		return shaded && (options.isShadeAll() || options.isShadeFluids());
	}
}
