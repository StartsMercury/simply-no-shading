package com.github.startsmercury.simplynoshading.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import net.minecraft.client.render.WorldRenderer;

/**
 * Includes accessors to inaccessible members of the class
 * {@link SodiumGameOptionPages}
 */
@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {
	/**
	 * Returns the value of the private field {@code cloudsDirty}.
	 *
	 * @return {@code cloudsDirty}
	 */
	@Accessor(value = "cloudsDirty")
	boolean isCloudsDirty();

	/**
	 * Sets the value of the private field {@code cloudsDirty}.
	 *
	 * @param cloudsDirty the new value of {@code cloudsDirty}
	 */
	@Accessor(value = "cloudsDirty")
	void setCloudsDirty(boolean cloudsDirty);
}
