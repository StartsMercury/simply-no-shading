package com.github.startsmercury.simply.no.shading.mixin.shading.cloud.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import me.jellysquid.mods.sodium.client.render.immediate.CloudRenderer;
import me.jellysquid.mods.sodium.client.util.color.ColorMixer;

@Mixin(CloudRenderer.class)
public class CloudRendererMixin {
	@Redirect(method = "rebuildGeometry(Lcom/mojang/blaze3d/vertex/BufferBuilder;IIII)V",
	          at = @At(value = "INVOKE",
	                   target = "Lme/jellysquid/mods/sodium/client/util/color/ColorMixer;mulARGB(II)I"))
	private int modifyMulARGB(final int multiplicand, final int multiplier) {
		if (!SimplyNoShading.getFirstInstance().getConfig().cloudShadingEnabled)
			return multiplicand;

		return ColorMixer.mulARGB(multiplicand, multiplier);
	}
}
