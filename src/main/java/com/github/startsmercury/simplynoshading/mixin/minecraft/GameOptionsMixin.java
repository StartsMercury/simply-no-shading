package com.github.startsmercury.simplynoshading.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;

import net.minecraft.client.option.GameOptions;

@Mixin(GameOptions.class)
public class GameOptionsMixin implements SimplyNoShadingGameOptions {
	private boolean shading;

	@Override
	public boolean isShading() {
		return this.shading;
	}

	@Inject(method = "accept", at = @At("HEAD"))
	private final void onAcceptHead(final GameOptions.Visitor visitor, final CallbackInfo callback) {
		this.shading = visitor.visitBoolean("shading", this.shading);
	}

	@Override
	public void setShading(final boolean shading) {
		this.shading = shading;
	}
}
