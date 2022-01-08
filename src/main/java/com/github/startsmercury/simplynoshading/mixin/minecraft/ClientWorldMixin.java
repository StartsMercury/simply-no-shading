package com.github.startsmercury.simplynoshading.mixin.minecraft;

import static net.fabricmc.api.EnvType.CLIENT;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.github.startsmercury.simplynoshading.SimplyNoShading;

import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;

@Environment(CLIENT)
@Mixin(ClientWorld.class)
public class ClientWorldMixin {
	@ModifyVariable(method = "getBrightness", at = @At("HEAD"), ordinal = 1, argsOnly = true)
	private final boolean onGetBrightnessHead(final boolean shaded) {
		return SimplyNoShading.getBakedConfig().shading() && shaded;
	}
}
