package com.github.startsmercury.simplynoshading.mixin.minecraft;

import static com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingOption.SHADE_ALL;
import static com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingOption.SHADE_BLOCKS;
import static com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingOption.SHADE_FLUIDS;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.option.Option;

@Mixin(VideoOptionsScreen.class)
public class VideoOptionsScreenMixin {
	@Final
	@Mutable
	@Shadow
	private static Option[] OPTIONS;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void onClinitReturn(final CallbackInfo callback) {
		final Option[] appendedOptions = new Option[OPTIONS.length + 3];

		System.arraycopy(OPTIONS, 0, appendedOptions, 0, OPTIONS.length);

		appendedOptions[OPTIONS.length] = SHADE_ALL;
		appendedOptions[OPTIONS.length + 1] = SHADE_BLOCKS;
		appendedOptions[OPTIONS.length + 2] = SHADE_FLUIDS;

		OPTIONS = appendedOptions;
	}
}
