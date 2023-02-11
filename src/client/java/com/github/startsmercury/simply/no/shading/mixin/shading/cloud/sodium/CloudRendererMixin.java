package com.github.startsmercury.simply.no.shading.mixin.shading.cloud.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import me.jellysquid.mods.sodium.client.render.immediate.CloudRenderer;
import me.jellysquid.mods.sodium.client.util.color.ColorMixer;

/**
 * The {@code CloudRendererMixin} is a {@linkplain Mixin mixin} class for the
 * {@link CloudRenderer} class.
 *
 * @since 6.0.0
 */
@Mixin(CloudRenderer.class)
public class CloudRendererMixin {
	/**
	 * This is a {@linkplain Redirect redirector} that redirects all calls to
	 * {@link ColorMixer#mulARGB(int, int)} in
	 * {@code CloudRenderer.rebuildGeometry(BufferBuilder, int, int, int, int)}.
	 * <p>
	 * Returns {@code ColorMixer.mulARGB(baseColor, multiplier)} when
	 * {@linkplain Config#blockShadingEnabled cloud shading is enabled}; otherwise
	 * the {@code baseColor}
	 *
	 * @param baseColor  the base color
	 * @param multiplier the multiplier color
	 * @return {@code ColorMixer.mulARGB(baseColor, multiplier)} when
	 *         {@linkplain Config#blockShadingEnabled cloud shading is enabled};
	 *         otherwise the {@code baseColor}
	 */
	@Redirect(method = "rebuildGeometry(Lcom/mojang/blaze3d/vertex/BufferBuilder;IIII)V",
	          at = @At(value = "INVOKE",
	                   target = "Lme/jellysquid/mods/sodium/client/util/color/ColorMixer;mulARGB(II)I"))
	private int modifyMulARGB(final int baseColor, final int multiplier) {
		if (!SimplyNoShading.getFirstInstance().getConfig().cloudShadingEnabled)
			return baseColor;

		return ColorMixer.mulARGB(baseColor, multiplier);
	}
}
