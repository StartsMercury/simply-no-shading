package com.github.startsmercury.simplynoshading.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.OptionsSubScreen;

/**
 * Includes accessors to inaccessible members of the class
 * {@link OptionsSubScreen}
 */
@Mixin(OptionsSubScreen.class)
public interface OptionsSubScreenAccessor {
	/**
	 * Returns the value of the private field {@code options}.
	 *
	 * @return {@code options}
	 */
	@Accessor("options")
	Options getOptions();
}
