package com.github.startsmercury.simply.no.shading.mixin.shading.block.minecraft;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
	 * A copy of {@link #vertices}'s 0th color.
	 */
	@Unique
	private int color0;

	/**
	 * A copy of {@link #vertices}'s 1st color.
	 */
	@Unique
	private int color1;

	/**
	 * A copy of {@link #vertices}'s 2nd color.
	 */
	@Unique
	private int color2;

	/**
	 * A copy of {@link #vertices}'s 3rd color.
	 */
	@Unique
	private int color3;

	/**
	 * The field {@code vertices}} is a shadowed field from {@link BakedQuad}.
	 */
	@Final
	@Shadow
	protected int[] vertices;

	/**
	 * A private constructor that does nothing as of the writing of this
	 * documentation.
	 */
	private BakedQuadMixin() {
	}

	/**
	 * This is an {@linkplain Inject injector} that runs additional code at the
	 * return of {@link BakedQuad#getVertices()}.
	 * <p>
	 * Overwrites the return value the boolean value of {@code true} if the original
	 * return value was {@code true} and {@linkplain Config#blockShadingEnabled
	 * block shading is enabled}; {@code false} otherwise.
	 *
	 * @param callback the callback
	 */
	@Inject(method = "getVertices()[I",
	        at = @At("HEAD"))
	private final void changeReturnedShade(final CallbackInfoReturnable<int[]> callback) {
		if (
		    SimplyNoShading.getFirstInstance()
		        .getConfig()
		        .blockShadingEnabled
		) {
			this.vertices[0x03] = this.color0;
			this.vertices[0x0B] = this.color1;
			this.vertices[0x13] = this.color2;
			this.vertices[0x0B] = this.color3;
		} else {
			this.vertices[0x03] = 0xFFFFFFFF;
			this.vertices[0x0B] = 0xFFFFFFFF;
			this.vertices[0x13] = 0xFFFFFFFF;
			this.vertices[0x0B] = 0xFFFFFFFF;
		}
	}

	/**
	 * This is an {@linkplain Inject injector} that runs additional code at the
	 * return of {@link BakedQuad#getVertices()}.
	 * <p>
	 * Overwrites the return value the boolean value of {@code true} if the original
	 * return value was {@code true} and {@linkplain Config#blockShadingEnabled
	 * block shading is enabled}; {@code false} otherwise.
	 *
	 * @param callback the callback
	 */
	@Inject(
		method = "<init>([IILnet/minecraft/core/Direction;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V",
		at = @At("RETURN")
	)
	private final void onInitReturn(final CallbackInfo callback) {
		this.color0 = this.vertices[0x03];
		this.color1 = this.vertices[0x0B];
		this.color2 = this.vertices[0x13];
		this.color3 = this.vertices[0x0B];
	}
}
