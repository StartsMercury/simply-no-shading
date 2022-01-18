package com.github.startsmercury.simplynoshading.mixin.minecraft;

import static net.fabricmc.api.EnvType.CLIENT;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simplynoshading.SimplyNoShading;

import net.fabricmc.api.Environment;
import net.minecraft.client.Keyboard;
import net.minecraft.client.util.InputUtil;

@Environment(CLIENT)
@Mixin(Keyboard.class)
public class KeyboardMixin {
	@Inject(method = "processF3", at = @At("HEAD"))
	private final void onProcessF3Head(final int key, final CallbackInfoReturnable<Boolean> callback) {
		switch (key) {
		case InputUtil.GLFW_KEY_A:
			SimplyNoShading.loadConfig();
			SimplyNoShading.bakeConfig();
			break;
		}
	}
}
