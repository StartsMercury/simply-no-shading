package com.github.startsmercury.simply.no.shading.mixin.shading.block.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BakedQuad;

/**
 * {@code BakedQuad} mixin class.
 *
 * @since 5.0.0
 */
@Environment(EnvType.CLIENT)
@Mixin(BakedQuad.class)
public abstract class BakedQuadMixin {
	/**
	 * Changes the returned shade by applying the {@link #shadingRule}.
	 *
	 * @param callback the callback
	 */
	@Inject(method = "isShade()Z",
	        at = @At("RETURN"),
	        cancellable = true)
	private final void changeReturnedShade(final CallbackInfoReturnable<Boolean> callback) {
		callback.setReturnValue(
		        SimplyNoShading.getFirstInstance().getConfig().blockShadingEnabled && callback.getReturnValueZ());
	}
}
