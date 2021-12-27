package com.github.startsmercury.simplynoshading.mixin.minecraft;

import static net.fabricmc.api.EnvType.CLIENT;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simplynoshading.SimplyNoShading;

import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.BakedQuad;

@Environment(CLIENT)
@Mixin(BakedQuad.class)
public abstract class BakedQuadMixin {
	@Final
	@Shadow
	private boolean shade;

	@Inject(method = "hasShade", at = @At("HEAD"), cancellable = true)
	private final void onHasShadeHead(final CallbackInfoReturnable<Boolean> callback) {
		callback.setReturnValue(SimplyNoShading.getBakedConfig().shading() && this.shade);
	}
}
