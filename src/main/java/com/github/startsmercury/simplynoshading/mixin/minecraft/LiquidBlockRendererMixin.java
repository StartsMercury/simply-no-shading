package com.github.startsmercury.simplynoshading.mixin.minecraft;

import me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;

/**
 * {@link Mixin mixin} for the class {@link FluidRenderer}.
 */
@Mixin(LiquidBlockRenderer.class)
public class LiquidBlockRendererMixin {
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
	@ModifyArg(method = "tesselate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/BlockAndTintGetter;getShade(Lnet/minecraft/core/Direction;Z)F"), index = 1)
	@SuppressWarnings("resource")
	private final boolean modifyShadedOnRender(final boolean shaded) {
		final SimplyNoShadingGameOptions options;

		options = (SimplyNoShadingGameOptions) Minecraft.getInstance().options;

		return shaded && (options.isShadeAll() || options.isShadeFluids());
	}
}
