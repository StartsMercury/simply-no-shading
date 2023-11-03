package com.github.startsmercury.simply.no.shading.mixin.shading.block.minecraft;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.multiplayer.ClientLevel;

/**
 * The {@code ClientLevelMixin} is a {@linkplain Mixin mixin} class for the
 * {@link ClientLevel} class.
 *
 * @since 5.0.0
 */
@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
	/**
	 * A private constructor that does nothing as of the writing of this
	 * documentation.
	 */
	private ClientLevelMixin() {
	}
}
