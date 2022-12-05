package com.github.startsmercury.simply.no.shading.mixin.shading.liquid.bedrockify;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod;

import me.juancarloscp52.bedrockify.client.features.bedrockShading.BedrockBlockShading;

/**
 * {@code BedrockBlockShading} mixin class.
 */
@Mixin(BedrockBlockShading.class)
public class BedrockBlockShadingFabricMixin {
	/**
	 * Modifies the liquid shade.
	 *
	 * @param callback the callback
	 */
	@Inject(method = "getLiquidShade(Lnet/minecraft/core/Direction;Z)F",
	        at = @At("HEAD"),
	        cancellable = true)
	private final void changeReturnedShade(final CallbackInfoReturnable<Float> callback) {
		if (!SimplyNoShadingClientMod.getInstance().config.shadingRules.liquids.wouldShade())
			callback.setReturnValue(1.0F);
	}
}
