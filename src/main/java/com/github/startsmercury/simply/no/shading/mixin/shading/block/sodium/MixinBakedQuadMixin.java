package com.github.startsmercury.simply.no.shading.mixin.shading.block.sodium;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import me.jellysquid.mods.sodium.mixin.core.pipeline.MixinBakedQuad;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BakedQuad;

/**
 * The {@code MixinBakedQuadMixin} is a {@linkplain Mixin mixin} class for the
 * {@link MixinBakedQuad} class.
 *
 * @since 5.0.0
 */
@Environment(EnvType.CLIENT)
@Mixin(BakedQuad.class)
public abstract class MixinBakedQuadMixin {
	/**
	 * This is an {@linkplain Inject injector} that runs additional code at the
	 * return of {@link MixinBakedQuad#getColor()}.
	 * <p>
	 * Overwrites the return value to return the unshaded color if
	 * {@linkplain Config#blockShadingEnabled
	 * block shading is enabled}; original color otherwise.
	 *
	 * @param idx the first captured parameter
	 * @param callback the callback
	 */
	@Dynamic("from me.jellysquid.mods.sodium.mixin.core.pipeline.MixinBakedQuad")
	@Inject(method = "getColor(I)I",
	        at = @At("RETURN"),
	        cancellable = true,
	        remap = false)
	private final void changeReturnedColor(
	    final int idx,
	    final CallbackInfoReturnable<Integer> callback
	) {
		if (
		    !SimplyNoShading.getFirstInstance()
		        .getConfig()
		        .blockShadingEnabled
		) {
			callback.setReturnValue(0xFFFFFFFF);
		}
	}
}
