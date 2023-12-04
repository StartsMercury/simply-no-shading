package com.github.startsmercury.simply.no.shading.mixin.shading.entity.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(GlStateManager.class)
public abstract class GlStateManagerMixin {
    @Shadow
    private static int shadeModel;

    static {
        shadeModel = 7424;
    }

    @Inject(method = "_shadeModel(I)V", at = @At("HEAD"), cancellable = true)
    private final static void ignoreShadeModelChange(final CallbackInfo callback) {
        callback.cancel();
    }
}
