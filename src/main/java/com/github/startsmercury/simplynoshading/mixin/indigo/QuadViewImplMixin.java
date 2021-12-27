package com.github.startsmercury.simplynoshading.mixin.indigo;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simplynoshading.SimplyNoShading;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.QuadViewImpl;

@Mixin(QuadViewImpl.class)
public abstract class QuadViewImplMixin {
	@Shadow
	private boolean shade;

	@Inject(method = "hasShade", at = @At("HEAD"), cancellable = true, remap = false)
	private final void onHasShadeHead(final CallbackInfoReturnable<Boolean> callback) {
		callback.setReturnValue(SimplyNoShading.getBakedConfig().shading() && this.shade);
	}
}
