package com.github.startsmercury.simply.no.shading.mixin.shading.block.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.core.Direction;

/**
 * The {@code RenderChunkRegion} is a {@linkplain Mixin mixin} class for the
 * {@link RenderChunkRegion} class.
 *
 * @since 6.0.0
 */
@Mixin(RenderChunkRegion.class)
public abstract class RenderChunkRegionMixin {
	/**
	 * A private constructor that does nothing as of the writing of this
	 * documentation.
	 */
	private RenderChunkRegionMixin() {
	}

	/**
	 * This is a {@linkplain ModifyVariable variable modifier} that modifies the
	 * parameter {@code shade} in
	 * {@link RenderChunkRegion#getShade(Direction, boolean)}
	 * <p>
	 * Returns {@code true} if {@code shade} is {@code true} and
	 * {@link Config#blockShadingEnabled block shading is enabled}; {@code false}
	 * otherwise.
	 *
	 * @param shade the shade
	 * @return {@code true} if {@code shade} is {@code true} and
	 *         {@link Config#blockShadingEnabled block shading is enabled};
	 *         {@code false} otherwise
	 */
	@ModifyVariable(method = "getShade(Lnet/minecraft/core/Direction;Z)F",
	                at = @At("HEAD"),
	                argsOnly = true)
	private final boolean changeShade(final boolean shade) {
		final boolean blockShadingEnabled = SimplyNoShading.getFirstInstance().getConfig().blockShadingEnabled;

		return shade && blockShadingEnabled;
	}
}