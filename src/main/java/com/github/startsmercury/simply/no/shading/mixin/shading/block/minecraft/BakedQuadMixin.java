package com.github.startsmercury.simply.no.shading.mixin.shading.block.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simply.no.shading.entrypoint.SimplyNoShadingClientMod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BakedQuad;

@Environment(EnvType.CLIENT)
@Mixin(BakedQuad.class)
public class BakedQuadMixin {
	@Inject(method = "isShade()Z", at = @At("RETURN"), cancellable = true)
	private final void changeReturnedShade(final CallbackInfoReturnable<Boolean> callback) {
		callback
		    .setReturnValue(SimplyNoShadingClientMod.getInstance().config.wouldShadeBlocks(callback.getReturnValueZ()));
	}
}
