package com.github.startsmercury.simply.no.shading.mixin.shading.block.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simply.no.shading.client.Config;
import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;

/**
 * The {@code ClientLevelMixin} is a {@linkplain Mixin mixin} class for the
 * {@link ClientLevel} class.
 *
 * @since 5.0.0
 */
@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
	/**
	 * This is a {@linkplain ModifyVariable variable modifier} that modifies the
	 * parameter {@code shade} in {@link ClientLevel#getShade(Direction, boolean)}
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
		final var blockShadingEnabled = SimplyNoShading.getFirstInstance().getConfig().blockShadingEnabled;

		return shade && blockShadingEnabled;
	}
}
