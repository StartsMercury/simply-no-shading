package com.github.startsmercury.simplynoshading.mixin.minecraft;

import static com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingOption.SHADING;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.option.Option;

@Mixin(VideoOptionsScreen.class)
public class VideoOptionsScreenMixin {
	@Final
	@Mutable
	private static Option[] OPTIONS;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void onClinitReturn(final CallbackInfo callback) {
		final Option[] appendedOptions = new Option[OPTIONS.length + 1];

		System.arraycopy(OPTIONS, 0, appendedOptions, 0, OPTIONS.length);

		appendedOptions[OPTIONS.length] = SHADING;

		OPTIONS = appendedOptions;
	}
}
