package com.github.startsmercury.simply.no.shading.mixin.shading.block.bedrockify;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import me.juancarloscp52.bedrockify.client.features.bedrockShading.BedrockBlockShading;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;

/**
 * The {@code BedrockBlockShadingMixin} is a {@linkplain Mixin mixin} class for
 * the {@link BedrockBlockShading} class.
 *
 * @since 6.1.5
 */
@Mixin(BedrockBlockShading.class)
public abstract class BedrockBlockShadingMixin {
	/**
	 * A private constructor that does nothing as of the writing of this
	 * documentation.
	 */
	private BedrockBlockShadingMixin() {
	}

	/**
	 * This is an {@linkplain Inject injector} that runs additional code at the
	 * return of {@link BedrockBlockShading#getBlockShade(Direction)}.
	 * <p>
	 * Overwrites the return value the boolean value of {@code true} if the original
	 * return value was {@code true} and {@linkplain Config#blockShadingEnabled
	 * block shading is enabled}; {@code false} otherwise.
	 *
	 * @param callback the callback
	 */
	@Inject(method = "getBlockShade(Lnet/minecraft/core/Direction;)F",
	                at = @At("RETURN"),
	                cancellable = true)
	private final void overwriteBlockShade(final CallbackInfoReturnable<Float> callback) {
		final var blockShadingEnabled = SimplyNoShading.getFirstInstance().getConfig().blockShadingEnabled;

		if (!blockShadingEnabled)
			callback.setReturnValue(1.0f);
	}

	/**
	 * This is an {@linkplain Inject injector} that runs additional code at the
	 * return of {@link BedrockBlockShading#getLiquidShade(Direction, boolean)}.
	 * <p>
	 * Overwrites the return value the boolean value of {@code true} if the original
	 * return value was {@code true} and {@linkplain Config#blockShadingEnabled
	 * block shading is enabled}; {@code false} otherwise.
	 *
	 * @param callback the callback
	 */
	@Inject(method = "getLiquidShade(Lnet/minecraft/core/Direction;Z)F",
	                at = @At("RETURN"),
	                cancellable = true)
	private final void overwriteLiquidShade(final CallbackInfoReturnable<Float> callback) {
		final var blockShadingEnabled = SimplyNoShading.getFirstInstance().getConfig().blockShadingEnabled;

		if (!blockShadingEnabled)
			callback.setReturnValue(1.0f);
	}
}
