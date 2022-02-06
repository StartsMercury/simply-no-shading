package com.github.startsmercury.simplynoshading.mixin.minecraft;

import static net.fabricmc.api.EnvType.CLIENT;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simplynoshading.client.SimplyNoShadingOptions;

import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;

/**
 * {@link Mixin mixin} for the class {@link BakedQuad}.
 */
@Environment(CLIENT)
@Mixin(BakedQuad.class)
public class BakedQuadMixin {
	/**
	 * @see BakedQuad#isShade()
	 */
	private static final String isShade = "Lnet/minecraft/client/renderer/block/model/BakedQuad;isShade()Z";

	/**
	 * Makes all block model faces require either
	 * {@link SimplyNoShadingOptions#isShadeAll()} or
	 * {@link SimplyNoShadingOptions#isShadeBlocks()} to return {@code true} to
	 * shade.
	 *
	 * @param callback the callback
	 * @implSpec {@code isShade() && (isShadeAll || isShadeBlocks())}
	 */
	@Inject(method = isShade, at = @At("RETURN"), cancellable = true)
	@SuppressWarnings("resource")
	private final void changeReturnedShade(final CallbackInfoReturnable<Boolean> callback) {
		final SimplyNoShadingOptions options;

		options = (SimplyNoShadingOptions) Minecraft.getInstance().options;

		callback.setReturnValue(callback.getReturnValueZ() && (options.isShadeAll() || options.isShadeBlocks()));
	}
}
