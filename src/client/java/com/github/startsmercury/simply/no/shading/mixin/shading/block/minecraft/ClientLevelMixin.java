package com.github.startsmercury.simply.no.shading.mixin.shading.block.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simply.no.shading.client.SimplyNoShading;

import net.minecraft.client.multiplayer.ClientLevel;

/**
 * {@code ClientLevel} mixin class.
 *
 * @since 5.0.0
 */
@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
	/**
	 * Modifies the shade whenever FRAPI support is needed.
	 *
	 * @param shade the original shade
	 * @return the modified shade
	 */
	@ModifyVariable(method = "getShade(Lnet/minecraft/core/Direction;Z)F",
	                at = @At("HEAD"),
	                argsOnly = true)
	private final boolean changeShade(final boolean shade) {
		return SimplyNoShading.getFirstInstance().getConfig().blockShadingEnabled && shade;
	}
}
