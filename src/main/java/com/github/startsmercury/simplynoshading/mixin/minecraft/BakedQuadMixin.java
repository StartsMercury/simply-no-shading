package com.github.startsmercury.simplynoshading.mixin.minecraft;

import static net.fabricmc.api.EnvType.CLIENT;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;

import net.fabricmc.api.Environment;

/**
 * {@link Mixin mixin} for the class {@link BakedQuad}.
 */
@Environment(CLIENT)
@Mixin(BakedQuad.class)
public class BakedQuadMixin {
	/**
	 * Makes all model faces require either
	 * {@link SimplyNoShadingGameOptions#isShadeAll()} or
	 * {@link SimplyNoShadingGameOptions#isShadeBlocks()} to return {@code true} to
	 * shade.
	 *
	 * @param callback the callback
	 * @implSpec {@code hasShade() && (isShadeAll || isShadeBlocks())}
	 */
	@Inject(method = "isShade", at = @At("RETURN"), cancellable = true)
	@SuppressWarnings("resource")
	private final void onHasShadeHead(final CallbackInfoReturnable<Boolean> callback) {
		final SimplyNoShadingGameOptions options;

		options = (SimplyNoShadingGameOptions) Minecraft.getInstance().options;

		callback.setReturnValue(callback.getReturnValueZ() && (options.isShadeAll() || options.isShadeBlocks()));
	}
}
