package com.github.startsmercury.simply.no.shading.mixin.shading.cloud.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import me.jellysquid.mods.sodium.client.render.immediate.CloudRenderer;

/**
 * The {@code CloudRendererMixin} is a {@linkplain Mixin mixin} class for the
 * {@link CloudRenderer} class.
 *
 * @since 6.0.0
 */
@Mixin(CloudRenderer.class)
public class CloudRendererMixin {
	/**
	 * The cached base cloud color.
	 */
	@Unique
	private int baseColor;

	/**
	 * A private constructor that does nothing as of the writing of this
	 * documentation.
	 */
	private CloudRendererMixin() {
	}

	/**
	 * This is a {@linkplain ModifyVariable variable modifier} that caches the value
	 * of the local variable {@code baseColor} in
	 * {@code CloudRenderer.rebuildGeometry(BufferBuilder, int, int, int)}.
	 * <p>
	 * Returns the parameter {@code baseColor} unmodified.
	 *
	 * @param baseColor the base cloud color
	 * @return the unmodified value of {@code baseColor}
	 */
	@ModifyVariable(method = "rebuildGeometry(Lcom/mojang/blaze3d/vertex/BufferBuilder;III)V",
	        at = @At(value = "STORE"),
	        name = "baseColor",
	        allow = 1)
	private int cacheBaseColor(final int baseColor) {
		return this.baseColor = baseColor;
	}

	/**
	 * This is an {@linkplain ModifyArg argument modifier} that replaces the
	 * {@code mixedColor} with the {@code baseColor} in
	 * {@code CloudRenderer.rebuildGeometry(BufferBuilder, int, int, int)} if
	 * {@linkplain Config#cloudShadingEnabled cloud shading is disabled}.
	 *
	 * @param mixedColor the mixed cloud color
	 * @return {@code mixedColor} when {@linkplain Config#cloudShadingEnabled cloud
	 *         shading is enabled}; {@code baseColor} otherwise
	 */
	@ModifyArg(method = "rebuildGeometry(Lcom/mojang/blaze3d/vertex/BufferBuilder;III)V",
	        at = @At(value = "INVOKE",
	                target = "Lme/jellysquid/mods/sodium/client/render/immediate/CloudRenderer;writeVertex(JFFFI)J"),
	        index = 4,
	        remap = false)
	private int undoColorMixing(final int mixedColor) {
		final var cloudShadingEnabled = SimplyNoShading.getFirstInstance().getConfig().cloudShadingEnabled;

		if (cloudShadingEnabled)
			return mixedColor;
		else
			return this.baseColor;
	}
}
