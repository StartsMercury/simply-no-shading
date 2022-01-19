package com.github.startsmercury.simplynoshading.mixin.indigo;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.QuadViewImpl;
import net.minecraft.client.MinecraftClient;

@Mixin(QuadViewImpl.class)
public class QuadViewImplMixin {
	@Inject(method = "hasShade", at = @At("RETURN"), cancellable = true, remap = false)
	private final void onHasShadeHead(final CallbackInfoReturnable<Boolean> callback) {
		callback.setReturnValue(
				callback.getReturnValueZ() && ((SimplyNoShadingGameOptions) MinecraftClient.getInstance().options).isShading());
	}
}
