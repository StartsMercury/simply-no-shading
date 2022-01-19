package com.github.startsmercury.simplynoshading.mixin.minecraft;

import static net.fabricmc.api.EnvType.CLIENT;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.Environment;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

@Environment(CLIENT)
@Mixin(Keyboard.class)
public class KeyboardMixin {
	@Inject(method = "processF3", at = @At("HEAD"))
	@SuppressWarnings("resource")
	private final void onProcessF3Head(final int key, final CallbackInfoReturnable<Boolean> callback) {
		if (key == InputUtil.GLFW_KEY_A) {
			MinecraftClient.getInstance().options.load();
		}
	}
}
