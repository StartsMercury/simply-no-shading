package com.github.startsmercury.simplynoshading.mixin.minecraft;

import static net.fabricmc.api.EnvType.CLIENT;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;

import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedQuad;

@Environment(CLIENT)
@Mixin(BakedQuad.class)
public class BakedQuadMixin {
	@Inject(method = "hasShade", at = @At("RETURN"), cancellable = true)
	@SuppressWarnings("resource")
	private final void onHasShadeHead(final CallbackInfoReturnable<Boolean> callback) {
		final SimplyNoShadingGameOptions options;

		options = (SimplyNoShadingGameOptions) MinecraftClient.getInstance().options;

		callback.setReturnValue(callback.getReturnValueZ() && (options.isShadeAll() || options.isShadeBlocks()));
	}
}
