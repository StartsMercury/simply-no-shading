package com.github.startsmercury.simplynoshading.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.OptionsSubScreen;

@Mixin(OptionsSubScreen.class)
public interface OptionsSubScreenAccessor {
	@Accessor("options")
	Options getOptions();
}
