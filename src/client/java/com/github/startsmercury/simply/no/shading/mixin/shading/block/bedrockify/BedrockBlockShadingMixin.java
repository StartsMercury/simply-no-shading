package com.github.startsmercury.simply.no.shading.mixin.shading.block.bedrockify;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import me.juancarloscp52.bedrockify.client.features.bedrockShading.BedrockBlockShading;
import net.minecraft.core.Direction;

/**
 * The {@code BedrockBlockShadingMixin} is a {@linkplain Mixin mixin} class for
 * the {@link BedrockBlockShading} class.
 *
 * @since 5.0.0
 */
@Mixin(BedrockBlockShading.class)
public class BedrockBlockShadingMixin {
	/**
	 * This is an {@linkplain Inject injector} that runs additional code at the head
	 * of {@link BedrockBlockShading#getLiquidShade(Direction, boolean)}.
	 * <p>
	 * Returns the original method early with the float value of {@code 1.0f} if the
	 * {@linkplain Config#blockShadingEnabled block shading is not enabled}.
	 *
	 * @param callback the callback
	 */
	@Inject(method = "getLiquidShade(Lnet/minecraft/core/Direction;Z)F",
	        at = @At("HEAD"),
	        cancellable = true)
	private final void changeReturnedShade(final CallbackInfoReturnable<Float> callback) {
		final var blockShadingEnabled = SimplyNoShading.getFirstInstance().getConfig().blockShadingEnabled;

		if (!blockShadingEnabled)
			callback.setReturnValue(1.0F);
	}
}
