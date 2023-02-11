package com.github.startsmercury.simply.no.shading.mixin.shading.block.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BakedQuad;

/**
 * The {@code BakedQuadMixin} is a {@linkplain Mixin mixin} class for the
 * {@link BakedQuad} class.
 *
 * @since 5.0.0
 */
@Environment(EnvType.CLIENT)
@Mixin(BakedQuad.class)
public abstract class BakedQuadMixin {
	/**
	 * This is an {@linkplain Inject injector} that runs additional code at the
	 * return of {@link BakedQuad#isShade()}.
	 * <p>
	 * Overwrites the return value the boolean value of {@code true} if the original
	 * return value was {@code true} and {@linkplain Config#blockShadingEnabled
	 * block shading is enabled}; {@code false} otherwise.
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
